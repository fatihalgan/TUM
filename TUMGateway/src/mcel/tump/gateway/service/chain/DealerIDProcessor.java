/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.gateway.service.chain;

import mcel.tump.dealer.domain.AbstractDealer;
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
public class DealerIDProcessor implements RechargeRequestProcessor {

    private RechargeRequestProcessor nextHandler;
    private RechargeRequestProcessor onFaultHandler;
    private IDealerService dealerService;
    private static final Log logger = LogFactory.getLog(DealerIDProcessor.class);
    
    public void process(TUMRechargeRequest request, TUMRechargeResponse response) {
        logger.debug("DealerID processor has started");
        if(!response.isFault()) {
            if(request.getRequestDealerID().equals("0")) {
                logger.debug("RequestDealerID not provided. Trying to obtain it from the provided username");
                AbstractDealer dealer = getDealerService().getDealerByUserName(request.getUsername());
                if(dealer == null) {
                    logger.error("Could not find a dealer for the given username... Generating a fault response.");
                    response.generateFaultResponse(TUMResponseCodes.DEALER_NOT_EXISTS.getResponseCode(), request.getRequestTransactionID());
                    return;
                }
                logger.debug("Obtained the RequestDealerID from the username.. Processing will continue");
                request.setRequestDealerID(dealer.getDealerCode());
                //TODO: Find request subdealer id here
            }
        } else {
            logger.debug("An error response has been passed to handler. Will not process this request.");
        }
        if(getNextHandler() != null) getNextHandler().process(request, response);
    }

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

    public RechargeRequestProcessor getOnFaultHandler() {
        return onFaultHandler;
    }

    public void setOnFaultHandler(RechargeRequestProcessor onFaultHandler) {
        this.onFaultHandler = onFaultHandler;
    }
    
    
}
