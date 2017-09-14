/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.gateway.service.chain;

import mcel.tump.dealer.service.IDealerService;
import mcel.tump.gateway.util.TUMRechargeRequest;
import mcel.tump.gateway.util.TUMRechargeResponse;
import mcel.tump.gateway.util.TUMResponseCodes;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author db2admin
 */
public class RequestTransactionIDGeneratorProcessor implements RechargeRequestProcessor {
    
    private RechargeRequestProcessor nextHandler;
    private RechargeRequestProcessor onFaultHandler;
    private IDealerService dealerService;
    private static final Log logger = LogFactory.getLog(RequestTransactionIDGeneratorProcessor.class);
    
    public void setNextHandler(RechargeRequestProcessor processor) {
        this.nextHandler = processor;
    }
    
    public RechargeRequestProcessor getNextHandler() {
        return this.nextHandler;
    }

    public IDealerService getDealerService() {
        return dealerService;
    }

    public void setDealerService(IDealerService dealerService) {
        this.dealerService = dealerService;
    }

    public void process(TUMRechargeRequest request, TUMRechargeResponse response) {
        logger.debug("RequestTransactionIDGeneration processor started...");
        if(request.getRequestTransactionID() == null || request.getRequestTransactionID().equals("0")) {
            try {
                logger.debug("Trying to obtain a new RequestTransactionID");
                request.setRequestTransactionID(getRequestTransactionId().toString());
                logger.debug("Obtained new RequestTransactionID: " + request.getRequestTransactionID());
            } catch(Exception e) {
                logger.error("Failed to obtain a new RequestTransactionID: " + e.getMessage());
                response.generateFaultResponse(TUMResponseCodes.FAILED_REQUEST_TRANSACTION_ID.getResponseCode(), "0");
            }
        } else {
            logger.debug("A RequestTransactionID has already been provided by client. No RequestTransactionID will be produced.");
        }
        if(getNextHandler() != null) getNextHandler().process(request, response);
    }
    
    private Long getRequestTransactionId() {
        return getDealerService().fetchNextRequestTransactionID();
    }

    public void setOnFaultHandler(RechargeRequestProcessor processor) {
        this.onFaultHandler = processor;
    }

    public RechargeRequestProcessor getOnFaultHandler() {
        return onFaultHandler;
    }
}
