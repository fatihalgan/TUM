package mcel.tump.gateway.handler;

import mcel.tump.gateway.util.TUMPRequest;
import mcel.tump.gateway.util.TUMPResponse;
import mcel.tump.gateway.util.TUMResponseCodes;
import mcel.tump.security.domain.User;
import mcel.tump.security.service.ISecurityService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PasswordAuthenticationHandler extends AbstractTUMRequestHandler {
	
	private static Log logger = LogFactory.getLog(PasswordAuthenticationHandler.class);
	private ISecurityService securityService;
	
	public PasswordAuthenticationHandler() {
		super();
	}

	public ISecurityService getSecurityService() {
		return securityService;
	}

	public void setSecurityService(ISecurityService securityService) {
		this.securityService = securityService;
	}
	
	 public void processRequest(TUMPRequest request, TUMPResponse response) throws TUMRequestHandlerException {
		 logger.debug("Password authentication handler is processingt the request..");
		 User user = securityService.loadUserByUsername(request.getUsername());
		 if(user != null) logger.debug("Found user " + user.getUsername() + " in TUM Database..");
		 if(user == null) {
			 logger.debug("User could not be located for dealer: " + request.getRequestDealerID());
	    	 throw new TUMRequestHandlerException(TUMResponseCodes.USER_NOT_EXISTS.getResponseCode(), request.getRequestTransactionID());
	     }
	     if(!user.getUsername().equals(request.getUsername())) {
	    	 throw new TUMRequestHandlerException(TUMResponseCodes.USER_NOT_EXISTS.getResponseCode(), request.getRequestTransactionID());
	     }
	     if(!user.getPassword().equals(request.getPassword())) {
	    	 logger.debug("Password wrong for user " + user.getUsername());
	    	 throw new TUMRequestHandlerException(TUMResponseCodes.USER_NOT_EXISTS.getResponseCode(), request.getRequestTransactionID());
	     }
	     logger.debug("Password and User verified.");
	     if(getNext() != null) getNext().processRequest(request, response);
	 }
}
