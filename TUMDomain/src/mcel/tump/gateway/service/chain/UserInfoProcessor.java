/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.gateway.service.chain;

import mcel.tump.gateway.util.TUMRechargeRequest;
import mcel.tump.gateway.util.TUMRechargeResponse;
import mcel.tump.gateway.util.TUMResponseCodes;
import mcel.tump.security.domain.User;
import mcel.tump.security.service.ISecurityService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author db2admin
 */
public class UserInfoProcessor implements RechargeRequestProcessor {

    private RechargeRequestProcessor nextHandler;
    private RechargeRequestProcessor onFaultHandler;
    private ISecurityService securityService;
    private static final Log logger = LogFactory.getLog(UserInfoProcessor.class);

    public void process(TUMRechargeRequest request, TUMRechargeResponse response) {
        logger.debug("User Info processor has started...");
        if(!response.isFault()) {
        	if(request.getUsername() != null) {
            	logger.debug("Username already exists in message.. Will skip retrieving user info from Dealer ID..");
            	if(getNextHandler() != null) {
            		getNextHandler().process(request, response);
            		return;
            	} else return;
            }
        	User user = getSecurityService().getUserForDealer(request.getRequestDealerID());
            if(user == null) {
                logger.error("A user could not be found for the dealer id: " + request.getRequestDealerID() + "... Request will not be processed..");
                response.generateFaultResponse(TUMResponseCodes.DEALER_NOT_EXISTS.getResponseCode(), request.getRequestTransactionID());
                return;
            }
            request.setUsername(user.getUsername());
        } else {
            logger.debug("An error response has been passed to handler. Will not process this request.");
        }
        if(getNextHandler() != null) getNextHandler().process(request, response);
    }

    public void setNextHandler(RechargeRequestProcessor processor) {
        this.nextHandler = processor;
    }

    public void setOnFaultHandler(RechargeRequestProcessor processor) {
        this.onFaultHandler = processor;
    }

    /**
     * @return the nextHandler
     */
    public RechargeRequestProcessor getNextHandler() {
        return nextHandler;
    }

    /**
     * @return the onFaultHandler
     */
    public RechargeRequestProcessor getOnFaultHandler() {
        return onFaultHandler;
    }

    /**
     * @return the securityService
     */
    public ISecurityService getSecurityService() {
        return securityService;
    }

    /**
     * @param securityService the securityService to set
     */
    public void setSecurityService(ISecurityService securityService) {
        this.securityService = securityService;
    }
}
