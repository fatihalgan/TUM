package mcel.tump.gateway.service.chain;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpStatus;

import mcel.tump.gateway.util.TUMRechargeRequest;
import mcel.tump.gateway.util.TUMRechargeResponse;
import mcel.tump.gateway.util.TUMResponseCodes;

import com.bridge.ena.cs3cp6.command.CommandFactory;
import com.bridge.ena.cs3cp6.command.InterrogatingGenericUpdateBalanceAndDateCommand;
import com.bridge.ena.cs3cp6.value.DedicatedAccountInformation;
import com.bridge.ena.xml.BadXmlFormatException;
import com.bridge.ena.xmlrpc.exception.XmlRpcConnectionException;

public class CS3CP6SMSAdjustmentAdapter implements CSCommandAdapter {

	private CommandFactory commandFactory;
	private TUMRechargeRequest request;
	private TUMRechargeResponse response;
	private static final Log logger = LogFactory.getLog(CS3CP6SMSAdjustmentAdapter.class);
	
	public CS3CP6SMSAdjustmentAdapter(CommandFactory factory, TUMRechargeRequest request, TUMRechargeResponse response) {
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
    	int transferAmount = request.getTransferAmount();
    	Float sms = new Float(transferAmount * 2);
    	DedicatedAccountInformation da = new DedicatedAccountInformation();
    	da.setDedicatedAccountId(1);
    	da.setDedicatedAccountValue(sms);
    	List<DedicatedAccountInformation> das = new ArrayList<DedicatedAccountInformation>();
    	das.add(da);
    	InterrogatingGenericUpdateBalanceAndDateCommand cmd = commandFactory.getInterrogatingGenericUpdateBalanceAndDateCommand(request.getRequestTransactionID(), request.getRequestTimeStamp(), request.getSubscriberMSISDN(), 0f, request.getTransferAmount().toString(), response.getTUMTransactionID(), 0, 0, 100f, das);
    	try {
    		logger.debug("Sending Adjustment Request to Charging System...");
    		 cmd.execute();
             response.setHttpStatusCode(HttpStatus.SC_OK);
             if(cmd.isFault()) {
                 logger.warn("Received Fault response from Charging System: " + cmd.getFaultCode());
                 response.generateFaultResponse(cmd.getFaultCode(), request.getRequestTransactionID());
             } else if(cmd.isErrorOrFaultResponse()) {
                 logger.warn("Received Error response from Charging System: " + cmd.getResponseCode());
                 response.generateFaultResponse(cmd.getResponseCode(), request.getRequestTransactionID());
             } else {
            	 logger.debug("Received Success response from Charging System: " + cmd.getResponseCode());
                 response.setSubscriberBalanceBefore(cmd.getDedicatedAccountBefore(1).getDedicatedAccountValue().toString());
                 response.setSubscriberBalanceAfter(cmd.getDedicatedAccountAfter(1).getDedicatedAccountValue().toString());
                 response.setSubscriberLanguage(cmd.getLanguageIDCurrent());
                 response.setServiceClassBefore(cmd.getServiceClassCurrent());
                 response.setServiceClassCurrent(cmd.getServiceClassCurrent());
                 response.setSupervisionDate(cmd.getSupervisionExpiryDateAfter());
                 response.setServiceFeeDate(cmd.getServiceFeeExpiryDateAfter());
                 response.setFreeSMS(cmd.getDedicatedAccountAfter(1).getDedicatedAccountValue().intValue() - cmd.getDedicatedAccountBefore(1).getDedicatedAccountValue().intValue());
                 response.setDedicatedAccountValues(new Float[] {sms});
             }
    	} catch(XmlRpcConnectionException e) {
            logger.error("Could not connect to Charging System: " + e.getMessage());
            response.generateFaultResponse(TUMResponseCodes.CS_UNAVAILABLE.getResponseCode(), request.getRequestTransactionID());
            response.setHttpStatusCode(HttpStatus.SC_NOT_FOUND);
            response.setRefillResultCode(cmd.getHttpStatusCode());
        } catch(BadXmlFormatException e) {
            logger.error("Bad XML Format in message to Charging System: " + e.getMessage());
            response.generateFaultResponse(TUMResponseCodes.CS_ILLEGAL_REQUEST_MESSAGE.getResponseCode(), request.getRequestTransactionID());
            response.setHttpStatusCode(HttpStatus.SC_BAD_REQUEST);
            response.setRefillResultCode(HttpStatus.SC_BAD_REQUEST);
        } finally {
            if(request.getSenderMSISDN() != null) response.setSenderMSISDN(request.getSenderMSISDN());
        }
    }
    
}
