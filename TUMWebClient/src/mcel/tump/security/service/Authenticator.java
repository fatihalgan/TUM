package mcel.tump.security.service;

import java.util.List;

import mcel.tump.security.dao.IAuthorizationDao;
import mcel.tump.security.domain.User;

import org.hibernate.Query;
import org.hibernate.Session;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;

@Name("authenticator")
public class Authenticator {

	@Logger
	Log log;
	
	@In
	Identity identity;
	
	@In("#{authorizationDao}")
	IAuthorizationDao authDao;
	
	@Out(scope=ScopeType.SESSION)
	User loggedInUser;
	
	public boolean authenticate() {
		log.info("authenticating #0", identity.getUsername());
		loggedInUser = authDao.findUserByUserName(identity.getUsername());
		if(loggedInUser == null) return false;
		if(loggedInUser.getPassword().trim().equals(identity.getPassword())) return true;
		else loggedInUser = null;
		return false;
	}
}
