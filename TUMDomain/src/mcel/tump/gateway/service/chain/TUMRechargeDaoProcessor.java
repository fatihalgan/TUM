/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.gateway.service.chain;

import mcel.tump.gateway.dao.ITUMPGatewayDao;
import mcel.tump.gateway.util.TUMGWTokens;
import mcel.tump.gateway.util.TUMPResponse;
import mcel.tump.gateway.util.TUMRechargeRequest;
import mcel.tump.gateway.util.TUMRechargeResponse;
import mcel.tump.gateway.util.TUMResponseCodes;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author db2admin
 */
public class TUMRechargeDaoProcessor implements RechargeRequestProcessor {
    
    private RechargeRequestProcessor nextHandler;
    private RechargeRequestProcessor onFaultHandler;
    private ITUMPGatewayDao tumpGatewayDao;
    private static final Log logger = LogFactory.getLog(TUMRechargeDaoProcessor.class);
    
    
    public void setNextHandler(RechargeRequestProcessor processor) {
        this.nextHandler = processor;
    }
    
    public RechargeRequestProcessor getNextHandler() {
        return this.nextHandler;
    }

    public void process(TUMRechargeRequest request, TUMRechargeResponse response) {
        logger.debug("TUMRechargeDao processor started for transaction " + request.getRequestTransactionID());
        if(!response.isFault()) {
            logger.debug("Trying to recharge subscriber in TUM DB for transaction " + request.getRequestTransactionID());
            if(request.getMethodName().equals(TUMGWTokens.RechargeSubscriberPinRequest.toString())) {
            	getTumpGatewayDao().rechargeSubscriberPinned(request, response);
            } else if(request.getMethodName().equals(TUMGWTokens.RechargeSubscriberRequest.toString())
            	|| request.getMethodName().equals(TUMGWTokens.RechargeSubscriberSMSRequest.toString())
            	|| request.getMethodName().equals(TUMGWTokens.AdjustSubscriberAccountRequest.toString())){
            	getTumpGatewayDao().rechargeSubscriber(request, response);
            } else if(request.getMethodName().equals(TUMGWTokens.ReserveRechargeSubscriberRequest.toString())) {
            	getTumpGatewayDao().reserveRechargeSubscriber(request, response);
            } else if(request.getMethodName().equals(TUMGWTokens.CancelReservationRequest.toString())) {
            	getTumpGatewayDao().checkoutReservationForCancel(request, response);
            } else if(request.getMethodName().equals(TUMGWTokens.ReserveVoucherRequest.toString())) {
            	getTumpGatewayDao().reserveVoucher(request, response);
            } else if(request.getMethodName().equals(TUMGWTokens.CommitVoucherRequest.toString())) {
            	getTumpGatewayDao().commitReserveVoucher(request, response);
            } else if(request.getMethodName().equals(TUMGWTokens.CancelVoucherRequest.toString())) {
            	getTumpGatewayDao().cancelReserveVoucher(request, response);
            }
            logger.debug("Response returned for recharge subscriber in TUM DB for transaction " + request.getRequestTransactionID());
            if(response.isFault()) {
                logger.debug("Error response code " + response.getFaultCode() + " received while trying to recharge subscriber in TUMDB for transaction " 
                	+ request.getRequestTransactionID() + ". Will continue with the fault handler...");
                return;
            }
        } else {
             logger.debug("An error response has been passed to handler. Will not process this request.");
        }
        if(nextHandler != null) getNextHandler().process(request, response);
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
