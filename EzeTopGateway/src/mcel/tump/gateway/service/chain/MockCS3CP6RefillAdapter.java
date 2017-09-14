package mcel.tump.gateway.service.chain;

import mcel.tump.gateway.util.TUMRechargeRequest;
import mcel.tump.gateway.util.TUMRechargeResponse;
import mcel.tump.gateway.util.TUMResponseCodes;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpStatus;

import com.bridge.ena.cs3cp6.command.CommandFactory;
import com.bridge.ena.cs3cp6.command.FreeAmountRefillCommand;
import com.bridge.ena.xml.BadXmlFormatException;
import com.bridge.ena.xmlrpc.exception.XmlRpcConnectionException;

public class MockCS3CP6RefillAdapter implements CSCommandAdapter {

	private CommandFactory commandFactory;
    private TUMRechargeRequest request;
    private TUMRechargeResponse response;
    private static final Log logger = LogFactory.getLog(CS3CP6RefillAdapter.class);

    public MockCS3CP6RefillAdapter(CommandFactory factory, TUMRechargeRequest request, TUMRechargeResponse response) {
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
        FreeAmountRefillCommand cmd = commandFactory.getFreeAmountRefillCommand(request.getRequestTransactionID(), request.getRequestTimeStamp(),
            request.getSubscriberMSISDN(), new Float(request.getTransferAmount()), request.getTransferAmount().toString(), response.getTUMTransactionID(), 100f);
        try {
            logger.debug("Sending Free Amount Refill Request to Charging System...");
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
                response.setSubscriberBalanceBefore(cmd.getBalanceBefore().toString());
                response.setSubscriberBalanceAfter(cmd.getBalanceAfter().toString());
                response.setSubscriberLanguage(cmd.getLanguageIDCurrent());
                response.setServiceClassBefore(cmd.getServiceClassOld());
                response.setServiceClassCurrent(cmd.getServiceClassNew());
                response.setSupervisionDate(cmd.getSupervisionExpiryDate());
                response.setServiceFeeDate(cmd.getServiceFeeExpiryDate());
                response.setFreeSMS(cmd.getSmsBalanceAfter().intValue() - cmd.getSmsBalanceBefore().intValue());
                response.setFreeMMS(cmd.getMmsBalanceAfter().intValue() - cmd.getMmsBalanceBefore().intValue());
                response.setDedicatedAccountValues(cmd.getDedicatedAccountValueChanges());
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
            response.setVoucherProfileID(cmd.getRefillProfileId());
            response.setVoucherProfileName(cmd.getVoucherProfileName());
            response.setAdjustmentPortion(cmd.getAdjustmentPortion());
            response.setRefillPortion(cmd.getRefillPortion());
            response.setRefillResultCode(cmd.getRefillFlag());
            response.setAdjustmentResultCode(cmd.getAdjustmentFlag());
            if(request.getSenderMSISDN() != null) response.setSenderMSISDN(request.getSenderMSISDN());
        }
    }

    
}
