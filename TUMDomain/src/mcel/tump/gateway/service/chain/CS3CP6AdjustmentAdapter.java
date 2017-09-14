package mcel.tump.gateway.service.chain;

import java.util.ArrayList;
import java.util.List;

import mcel.tump.gateway.util.TUMRechargeRequest;
import mcel.tump.gateway.util.TUMRechargeResponse;
import mcel.tump.gateway.util.TUMResponseCodes;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpStatus;

import com.bridge.ena.cs3cp6.command.CommandFactory;
import com.bridge.ena.cs3cp6.command.InterrogatingGenericUpdateBalanceAndDateCommand;
import com.bridge.ena.cs3cp6.value.DedicatedAccountInformation;
import com.bridge.ena.xml.BadXmlFormatException;
import com.bridge.ena.xmlrpc.exception.XmlRpcConnectionException;

/**
 * 
 * @author db2admin
 * This adapter is designed only for MCel loyalty application and it only does
 * an adjustment of request.getTransferAmount() adjustment to Dedicated Account 5.
 * It does not increase supervision/service fee dates or dedicated account expiry date.
 */

public class CS3CP6AdjustmentAdapter implements CSCommandAdapter {

	private CommandFactory commandFactory;
    private TUMRechargeRequest request;
    private TUMRechargeResponse response;
    private static final Log logger = LogFactory.getLog(CS3CP6AdjustmentAdapter.class);
    
    public CS3CP6AdjustmentAdapter(CommandFactory factory, TUMRechargeRequest request, TUMRechargeResponse response) {
    	this.request = request;
    	this.response = response;
    	this.commandFactory = factory;
    }
    
    public TUMRechargeRequest getRequest() {
        return request;
    }

    public TUMRechargeResponse getResponse() {
        return response;
    }
    
    public void execute() {
    	List<DedicatedAccountInformation> das = new ArrayList<DedicatedAccountInformation>();
    	DedicatedAccountInformation da = new DedicatedAccountInformation();
    	da.setDedicatedAccountId(5);
    	da.setDedicatedAccountValue(request.getTransferAmount().floatValue());
    	das.add(da);
    	InterrogatingGenericUpdateBalanceAndDateCommand cmd = commandFactory.getInterrogatingGenericUpdateBalanceAndDateCommand(request.getRequestTransactionID(),
    		request.getRequestTimeStamp(), request.getSubscriberMSISDN(), 0f, request.getTransferAmount().toString(), response.getTUMTransactionID(), 
    		0, 0, 100f, das);
    	try {
    		logger.debug("Sending Adjustment Request to Charging System fro transaction " + request.getRequestTransactionID());
    		 cmd.execute();
             response.setHttpStatusCode(HttpStatus.SC_OK);
             if(cmd.isFault()) {
                 logger.warn("Received Fault response from Charging System " + cmd.getFaultCode() + " for transaction " + request.getRequestTransactionID());
                 response.generateFaultResponse(cmd.getFaultCode(), request.getRequestTransactionID());
             } else if(cmd.isErrorOrFaultResponse()) {
                 logger.warn("Received Error response from Charging System " + cmd.getResponseCode() + " for transaction " + request.getRequestTransactionID());
                 response.generateFaultResponse(cmd.getResponseCode(), request.getRequestTransactionID());
             } else {
            	 logger.debug("Received Success response from Charging System " + cmd.getResponseCode() + " for transaction " + request.getRequestTransactionID());
                 response.setSubscriberBalanceBefore(cmd.getDedicatedAccountBefore(5).getDedicatedAccountValue().toString());
                 response.setSubscriberBalanceAfter(cmd.getDedicatedAccountAfter(5).getDedicatedAccountValue().toString());
                 response.setSubscriberLanguage(cmd.getLanguageIDCurrent());
                 response.setServiceClassBefore(cmd.getServiceClassCurrent());
                 response.setServiceClassCurrent(cmd.getServiceClassCurrent());
                 response.setSupervisionDate(cmd.getSupervisionExpiryDateAfter());
                 response.setServiceFeeDate(cmd.getServiceFeeExpiryDateAfter());
                 response.setFreeSMS(cmd.getDedicatedAccountAfter(1).getDedicatedAccountValue().intValue() - cmd.getDedicatedAccountBefore(1).getDedicatedAccountValue().intValue());
                 response.setFreeMMS(cmd.getDedicatedAccountAfter(2).getDedicatedAccountValue().intValue() - cmd.getDedicatedAccountBefore(2).getDedicatedAccountValue().intValue());
                 response.setDedicatedAccountValues(new Float[] {0f, 0f, 0f, 0f, request.getTransferAmount().floatValue(), 0f, 0f, 0f, 0f, 0f});
             }
    	} catch(XmlRpcConnectionException e) {
            logger.error("Could not connect to Charging System for transaction " + request.getRequestTransactionID() + ": " + e.getMessage());
            response.generateFaultResponse(TUMResponseCodes.CS_UNAVAILABLE.getResponseCode(), request.getRequestTransactionID());
            response.setHttpStatusCode(HttpStatus.SC_NOT_FOUND);
            response.setRefillResultCode(cmd.getHttpStatusCode());
        } catch(BadXmlFormatException e) {
            logger.error("Bad XML Format in message to Charging System for transaction " + request.getRequestTransactionID() + ": " + e.getMessage());
            response.generateFaultResponse(TUMResponseCodes.CS_ILLEGAL_REQUEST_MESSAGE.getResponseCode(), request.getRequestTransactionID());
            response.setHttpStatusCode(HttpStatus.SC_BAD_REQUEST);
            response.setRefillResultCode(HttpStatus.SC_BAD_REQUEST);
        } finally {
            response.setAdjustmentPortion(cmd.getAdjustmentAmount());
            response.setRefillPortion(0f);
            response.setRefillResultCode(0);
            response.setAdjustmentResultCode(cmd.getLastCommand().getFullResultCode());
            if(request.getSenderMSISDN() != null) response.setSenderMSISDN(request.getSenderMSISDN());
        }
    }
}
