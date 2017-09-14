package mcel.tump.gateway.service.chain;

import mcel.tump.gateway.util.TUMRechargeRequest;
import mcel.tump.gateway.util.TUMRechargeResponse;
import mcel.tump.gateway.util.TUMResponseCodes;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpStatus;

import com.bridge.ena.cs3cp6.command.CommandFactory;
import com.bridge.ena.cs3cp6.command.VoucherRefillCommand;
import com.bridge.ena.xmlrpc.exception.XmlRpcConnectionException;

public class CS3CP6PinnedRefillAdapter implements CSCommandAdapter {

	private CommandFactory commandFactory;
    private TUMRechargeRequest request;
    private TUMRechargeResponse response;
    private static final Log logger = LogFactory.getLog(CS3CP6PinnedRefillAdapter.class);

    public CS3CP6PinnedRefillAdapter(CommandFactory factory, TUMRechargeRequest request, TUMRechargeResponse response) {
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
    	VoucherRefillCommand cmd = commandFactory.getVoucherRefillCommand(request.getRequestTransactionID(), request.getRequestTimeStamp(), 
        	request.getSubscriberMSISDN(), request.getVoucherActivationCode(), "", response.getTUMTransactionID(), 100f);
        try {
        	logger.debug("Sending Voucher Refill Request to Charging System...");
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
                response.setSubscriberBalanceBefore(cmd.getAccountBeforeRefill().getAccountValue().toString());
                response.setSubscriberBalanceAfter(cmd.getAccountAfterRefill().getAccountValue().toString());
                response.setSubscriberLanguage(cmd.getCurrentLanguageID());
                response.setServiceClassBefore(cmd.getAccountBeforeRefill().getServiceClassCurrent());
                response.setServiceClassCurrent(cmd.getAccountAfterRefill().getServiceClassCurrent());
                response.setSupervisionDate(cmd.getAccountAfterRefill().getSupervisionExpiryDate());
                response.setServiceFeeDate(cmd.getAccountAfterRefill().getServiceFeeExpiryDate());
                if(cmd.getSMSAccountAfterRefill() != null) response.setFreeSMS(cmd.getSMSAccountAfterRefill().getDedicatedAccountValue().intValue() - cmd.getSMSAccountBeforeRefill().getDedicatedAccountValue().intValue());
                	else response.setFreeSMS(0);
                if(cmd.getMMSAccountAfterRefill() != null) response.setFreeMMS(cmd.getMMSAccountAfterRefill().getDedicatedAccountValue().intValue() - cmd.getMMSAccountBeforeRefill().getDedicatedAccountValue().intValue());
                    else response.setFreeMMS(0);
                response.setDedicatedAccountValues(cmd.getDedicatedAccountValueChanges());
                response.setVoucherProfileName(cmd.getVoucherGroup());
                response.setAdjustmentPortion(0f);
                response.setRefillPortion(cmd.getRefillInfo().getRefillValueWithoutPromotion().getRefillAmount1());
                //Set the Transfer amount in request for compatibility with Final Log Dao Processor..
                request.setTransferAmount(cmd.getRefillInfo().getRefillValueWithoutPromotion().getRefillAmount1().intValue());
                response.setRefillResultCode(cmd.getFullResultCode());
                response.setAdjustmentResultCode(-1);
                response.setVoucherSerialNumber(cmd.getVoucherSerialNumber());
                if(request.getSenderMSISDN() != null) response.setSenderMSISDN(request.getSenderMSISDN());
            }
        } catch(XmlRpcConnectionException e) {
        	logger.error("Could not connect to Charging System: " + e.getMessage());
            response.generateFaultResponse(TUMResponseCodes.CS_UNAVAILABLE.getResponseCode(), request.getRequestTransactionID());
            response.setHttpStatusCode(HttpStatus.SC_NOT_FOUND);
            response.setRefillResultCode(cmd.getHttpStatusCode());
        }
    }
}
