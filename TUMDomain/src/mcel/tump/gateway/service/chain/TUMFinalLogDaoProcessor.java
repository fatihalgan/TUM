/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.gateway.service.chain;

import java.util.Date;
import mcel.tump.gateway.dao.ITUMPGatewayDao;
import mcel.tump.gateway.util.TUMGWTokens;
import mcel.tump.gateway.util.TUMRechargeRequest;
import mcel.tump.gateway.util.TUMRechargeResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author db2admin
 */
public class TUMFinalLogDaoProcessor implements RechargeRequestProcessor {

    private RechargeRequestProcessor nextHandler;
    private RechargeRequestProcessor onFaultHandler;
    private ITUMPGatewayDao tumpGatewayDao;
    private static final Log logger = LogFactory.getLog(TUMFinalLogDaoProcessor.class);
        
    public void process(TUMRechargeRequest request, TUMRechargeResponse response) {
        logger.debug("TUMFinalLogDao processor started for transaction " + request.getRequestTransactionID());
        if(request.getMethodName().equals(TUMGWTokens.RechargeSubscriberPinRequest.toString()) 
        	|| request.getMethodName().equals(TUMGWTokens.RechargeSubscriberRequest.toString())
        	|| request.getMethodName().equals(TUMGWTokens.RechargeSubscriberSMSRequest.toString())
        	|| request.getMethodName().equals(TUMGWTokens.AdjustSubscriberAccountRequest.toString())) {
        		try {
        			String tumTransactionId = response.getTUMTransactionID();
        			Integer csResult = response.getFullResultCode();
        			Date csTimestamp = new Date();
        			String balanceBefore = null;
        			if(response.getSubscriberBalanceBefore() != null) balanceBefore = response.getSubscriberBalanceBefore();
        			else balanceBefore = "0";
        			String balanceAfter = null;
        			if(response.getSubscriberBalanceAfter() != null) balanceAfter = response.getSubscriberBalanceAfter();
        			else balanceAfter = "0";
        			Integer serviceClassOld = 0;
        			if(response.getServiceClassBefore() != null) serviceClassOld = response.getServiceClassBefore();
        			Float transactionAmount = 0f;
        			if(response.getTransferAmount() != null) transactionAmount = response.getTransferAmount().floatValue();
        			Float refillPortion = 0f;
        			if(response.getRefillPortion() != null) refillPortion = response.getRefillPortion();
        			Float adjustmentPortion = 0f;
        			if(response.getAdjustmentPortion() != null) adjustmentPortion = response.getAdjustmentPortion();
        			String voucherProfileID = "";
        			if(response.getVoucherProfileID() != null) voucherProfileID = response.getVoucherProfileID();
        			String voucherProfileName = "";
        			if(response.getVoucherProfileName() != null) voucherProfileName = response.getVoucherProfileName();
        			//TODO: This property is Not needed anymore
        			Integer serviceClassNew = 0;
        			if(response.getServiceClassCurrent() != null) serviceClassNew = response.getServiceClassCurrent();
        			String subscriberMSISDN = request.getSubscriberMSISDN();
        			Integer refillFlag = -1;
        			if(response.getRefillResultCode() != null) refillFlag = response.getRefillResultCode(); 
        			Integer adjustmentFlag = -1;
        			if(response.getAdjustmentResultCode() != null) adjustmentFlag = response.getAdjustmentResultCode();
        			getTumpGatewayDao().writeFinalLog(Long.parseLong(tumTransactionId), csResult, csTimestamp, balanceBefore, balanceAfter,
        					serviceClassOld, transactionAmount, refillPortion, adjustmentPortion, voucherProfileID, voucherProfileName,
        					null, serviceClassNew, subscriberMSISDN, null, refillFlag,
        					adjustmentFlag, null);
        			logger.debug("Final log has been processed successfully for transaction " + request.getRequestTransactionID());
        		} catch(Exception e) {
        			logger.error("Something failed while writing the final log to TUM DB for transaction " + request.getRequestTransactionID() + ": " + e.getMessage());
        		}
        } else if(request.getMethodName().equals(TUMGWTokens.ReserveVoucherRequest.toString()) ||
        		request.getMethodName().equals(TUMGWTokens.CommitVoucherRequest.toString()) ||
        		request.getMethodName().equals(TUMGWTokens.CancelVoucherRequest.toString())) {
        	try {
        		getTumpGatewayDao().writeVoucherFinalLog(request, response);
        	} catch(Exception e) {
        		logger.error("Something failed while writing the final log to TUM DB for transaction " + request.getRequestTransactionID() + ": " + e.getMessage());
        	}
        } 
        if(nextHandler != null) this.nextHandler.process(request, response);
    }

    public void setNextHandler(RechargeRequestProcessor processor) {
        this.nextHandler = processor;
    }

    public RechargeRequestProcessor getNextHandler() {
        return nextHandler;
    }
    
    public ITUMPGatewayDao getTumpGatewayDao() {
        return tumpGatewayDao;
    }

    public void setTumpGatewayDao(ITUMPGatewayDao tumpGatewayDao) {
        this.tumpGatewayDao = tumpGatewayDao;
    }
    
    public RechargeRequestProcessor getOnFaultHandler() {
        return onFaultHandler;
    }

    public void setOnFaultHandler(RechargeRequestProcessor onFaultHandler) {
        this.onFaultHandler = onFaultHandler;
    }
}
