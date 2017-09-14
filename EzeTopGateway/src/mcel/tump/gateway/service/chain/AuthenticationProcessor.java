package mcel.tump.gateway.service.chain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mcel.tump.gateway.util.TUMRechargeRequest;
import mcel.tump.gateway.util.TUMRechargeResponse;
import mcel.tump.gateway.util.TUMResponseCodes;
import mcel.tump.security.domain.User;
import mcel.tump.security.service.ISecurityService;

public class AuthenticationProcessor implements RechargeRequestProcessor {

	private ISecurityService securityService;
	private static final Log logger = LogFactory.getLog(AuthenticationProcessor.class);
	
	private RechargeRequestProcessor nextHandler;
    private RechargeRequestProcessor onFaultHandler;
    
	@Override
	public void process(TUMRechargeRequest request, TUMRechargeResponse response) {
		String username = request.getUsername();
		String password = request.getPassword();
		String dealerCode = request.getRequestDealerID();
		try {
			User user = getSecurityService().loadUserByUsername(username);
			if(!user.getPassword().equals(password)) throw new RuntimeException();
			if(!user.getOwningDealer().getDealerCode().equals(dealerCode)) throw new RuntimeException();
		} catch(Exception e) {
			logger.error("Authentication of user failed.. Will terminate the handler chain..");
			response.generateFaultResponse(TUMResponseCodes.INVALID_LOGIN_CREDENTIALS.getResponseCode(), request.getRequestTransactionID());
			return;
		}
		if(getNextHandler() != null) getNextHandler().process(request, response); 
	}
	
	public RechargeRequestProcessor getNextHandler() {
		return this.nextHandler;
	}

	@Override
	public void setNextHandler(RechargeRequestProcessor processor) {
		this.nextHandler = processor;		
	}

	@Override
	public void setOnFaultHandler(RechargeRequestProcessor processor) {
		this.onFaultHandler = processor;		
	}
	
	public RechargeRequestProcessor getOnFaultHandler() {
		return onFaultHandler;
	}

	public ISecurityService getSecurityService() {
		return securityService;
	}

	public void setSecurityService(ISecurityService securityService) {
		this.securityService = securityService;
	}
	
}
