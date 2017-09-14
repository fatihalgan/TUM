package mcel.tump.gateway.service.chain;

import mcel.tump.gateway.util.TUMRechargeRequest;
import mcel.tump.gateway.util.TUMRechargeResponse;
import mcel.tump.gateway.util.TUMResponseCodes;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bridge.ena.cs.command.AbstractCSCommand;
import com.bridge.ena.cs3cp6.command.CommandFactory;
import com.bridge.ena.cs3cp6.command.VoucherRefillCommand;
import com.bridge.ena.xml.BadXmlFormatException;
import com.bridge.ena.xmlrpc.exception.XmlRpcConnectionException;

public class TUMPinnedRechargeCSProcessor implements RechargeRequestProcessor {

	private RechargeRequestProcessor nextHandler;
    private RechargeRequestProcessor onFaultHandler;
    private CommandFactory commandFactory;
    private static final Log logger = LogFactory.getLog(TUMPinnedRechargeCSProcessor.class);
    
    public void setNextHandler(RechargeRequestProcessor processor) {
        this.nextHandler = processor;
    }

    public RechargeRequestProcessor getNextHandler() {
        return this.nextHandler;
    }

    public CommandFactory getCommandFactory() {
        return commandFactory;
    }

    public void setCommandFactory(CommandFactory commandFactory) {
        this.commandFactory = commandFactory;
    }

    public RechargeRequestProcessor getOnFaultHandler() {
        return onFaultHandler;
    }

    public void setOnFaultHandler(RechargeRequestProcessor onFaultHandler) {
        this.onFaultHandler = onFaultHandler;
    }
    
    public void process(TUMRechargeRequest request, TUMRechargeResponse response) {
    	logger.debug("TUM Pinned Recharge CS Processor started..");
    	logger.debug("Trying to make a pinned refill from Charging System");
    	VoucherRefillCommand cmd = commandFactory.getVoucherRefillCommand(request.getRequestTransactionID(), request.getRequestTimeStamp(), 
    		request.getSubscriberMSISDN(), request.getVoucherActivationCode(), "", response.getTUMTransactionID(), 100f);
    	try {
    		logger.debug("Sending Voucher Refill Request to Charging System...");
    		cmd.execute();
            response.setHttpStatusCode(org.apache.http.HttpStatus.SC_OK);
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
            response.setHttpStatusCode(org.apache.http.HttpStatus.SC_NOT_FOUND);
            response.setRefillResultCode(cmd.getHttpStatusCode());
        } finally {
        	if(response.isFault()) {
                logger.error("Fault response from Charging System received with code " + response.getFaultCode() + " from charging system.");
            }
            if(nextHandler !=  null) getNextHandler().process(request, response);
        }
    }
    
}
