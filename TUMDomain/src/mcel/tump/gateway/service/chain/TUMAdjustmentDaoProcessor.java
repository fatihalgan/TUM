package mcel.tump.gateway.service.chain;

import mcel.tump.gateway.dao.ITUMPGatewayDao;
import mcel.tump.gateway.util.TUMRechargeRequest;
import mcel.tump.gateway.util.TUMRechargeResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TUMAdjustmentDaoProcessor implements RechargeRequestProcessor {

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
        logger.debug("TUMRechargeDao processor started...");
        if(!response.isFault()) {
            logger.debug("Trying to recharge subscriber in TUM DB for transaction " + request.getRequestTransactionID());
            getTumpGatewayDao().rechargeSubscriber(request, response);
            logger.debug("Recharging of subscriber in TUMDB is complete for transaction " + request.getRequestTransactionID());
            if(response.isFault()) {
                logger.debug("Error response code " + response.getFaultCode() + " received while trying to recharge subscriber in TUMDB..Will continue with the fault handler...");
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
