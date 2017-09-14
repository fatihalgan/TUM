package mcel.tump.security.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import mcel.tump.dealer.domain.AbstractDealer;
import mcel.tump.dealer.domain.DealerShop;
import mcel.tump.dealer.service.IDealerService;
import mcel.tump.notification.service.INotificationService;
import mcel.tump.security.dao.IAuthorizationDao;
import mcel.tump.security.domain.Authorization;
import mcel.tump.security.domain.Role;
import mcel.tump.security.domain.User;
import mcel.tump.security.domain.UserStatus;
import mcel.tump.security.exceptions.InvalidLoginCredentialsException;
import mcel.tump.security.exceptions.UserNotActiveException;
import org.acegisecurity.userdetails.UserDetailsService;

public class SecurityService implements ISecurityService, UserDetailsService {

    private IAuthorizationDao authorizationDao;
    private AuthorizationsCache authorizationsCache;
    private IDealerService dealerService;
    private INotificationService notificationService;

    public SecurityService() {
        authorizationsCache = new AuthorizationsCache();
    }

    public IAuthorizationDao getAuthorizationDao() {
        return authorizationDao;
    }

    public void setAuthorizationDao(IAuthorizationDao authorizationDao) {
        this.authorizationDao = authorizationDao;
    }

    public IDealerService getDealerService() {
        return dealerService;
    }

    public void setDealerService(IDealerService dealerService) {
        this.dealerService = dealerService;
    }
    
    @Override
    public void deleteAuthorization(Authorization authorization) {
        getAuthorizationDao().delete(authorization);
        authorizationsCache.loadAuthorizations(getAuthorizationDao().findAllAuthorizations());
    }

    @Override
    public void deleteRole(Role role) {
        getAuthorizationDao().attach(role);
        if(role.getUsers().size() != 0) {
            User user = role.getUsers().iterator().next();
            throw new RuntimeException("A user with username " + user.getUsername() + " is associated with this role. You must delete the user first.");
        }
        if(role.getRoleAuthorizations().size() != 0) {
            Authorization auth = role.getRoleAuthorizations().iterator().next();
            throw new RuntimeException("This role has been associated with the authorization " + auth.getAuthorizationName() + ". You must remove the role from this authorization first.");
        }
        getAuthorizationDao().delete(role);
    }

    @Override
    public void deleteUser(User user) {
        getAuthorizationDao().delete(user);
    }

    @Override
    public List<Authorization> getAllAuthorizations() {
        return getAuthorizationDao().findAllAuthorizations();
    }

    @Override
    public List<Role> getAllRoles() {
        return getAuthorizationDao().findAllRoles();
    }

    @Override
    public List<User> getAllUsers() {
        return getAuthorizationDao().findAllUsers();
    }
    
    public List<User> getUsersByCriteria(String username, String email) {
        return getAuthorizationDao().findUsersByCriteria(username.trim(), email.trim());
    }

    @Override
    public Authorization getAuthorization(Long id) {
        return getAuthorizationDao().findAuthorizationById(id);
    }

    @Override
    public Authorization getAuthorizationOfPath(String path) {
        if (!authorizationsCache.initialized()) {
            authorizationsCache.loadAuthorizations(getAuthorizationDao().findAllAuthorizations());
        }
        return authorizationsCache.getAuthorization(path);
    }

    @Override
    public Role getRole(Long id) {
        return getAuthorizationDao().findRoleById(id);
    }

    @Override
    public Role getRoleByName(String roleName) {
        return getAuthorizationDao().findRoleByName(roleName);
    }

    @Override
    public User getUser(Long id) {
        return getAuthorizationDao().findUserById(id);
    }

    @Override
    public void saveAuthorization(Authorization auth) {
        getAuthorizationDao().saveOrUpdate(auth);
        authorizationsCache.loadAuthorizations(getAuthorizationDao().findAllAuthorizations());
    }

    @Override
    public void saveRole(Role role) {
        getAuthorizationDao().saveOrUpdate(role);
    }

    @Override
    public void saveRole(Role role, List<User> selectedUsers) {
    	getAuthorizationDao().saveOrUpdate(role);
        role.removeNonMembers(selectedUsers);
        addNonMembers(role, selectedUsers);        
    }

    private void addNonMembers(Role role, List<User> members) {
    	for(User u : members) {
    		if(!role.getUsers().contains(u)) {
        		getAuthorizationDao().attach(u);
        		role.addUser(u);
        	}
    	}
    }
    
    
    public void saveUser(User user, List<Role> selectedRoles) {
        getAuthorizationDao().saveOrUpdate(user);
        user.removeNonMemberships(selectedRoles);
        addMemberships(user, selectedRoles);
    }
    
    private void addMemberships(User user, List<Role> memberships) {
    	for(Role r : memberships) {
    		if(!user.getUserRoles().contains(r)) {
        		getAuthorizationDao().attach(r);
        		user.addRole(r);
        	}
    	}
    }
    
    public void saveAuthorization(Authorization auth, List<Role> selectedRoles) {
        getAuthorizationDao().saveOrUpdate(auth);
        auth.removeUnauthorizedRoles(selectedRoles);
        addAuthorizedRoles(auth, selectedRoles);
        
    }
    
    private void addAuthorizedRoles(Authorization auth, List<Role> authorizedRoles) {
    	for(Role r : authorizedRoles) {
    		if(!auth.getAuthorizedRoles().contains(r)) {
    			r = (Role)getAuthorizationDao().load(Role.class, r.getId());
    			auth.addRole(r);
    		}
    	}
    }

    @Override
    public void saveUser(User user) {
        getAuthorizationDao().saveOrUpdate(user);
    }
    
    public void resetUserPassword(User user) {
        boolean success = user.updatePassword(user.getPassword(), user.getConfirmPassword());
        if(!success) throw new RuntimeException("Password and password confirmation fields do not match.");
        else {
            saveUser(user);
            getNotificationService().userPasswordResetNotificationMail(user);
        }
    }

    public User loadUserByUsername(String username) {
        return getAuthorizationDao().findUserByUserName(username);
    }

    public INotificationService getNotificationService() {
        return notificationService;
    }

    public void setNotificationService(INotificationService notificationService) {
        this.notificationService = notificationService;
    }

/**
     * This class is an inline cache used by @com.dna.pert.util.acegi.UrlToUserFilterSecurityInterceptor
     * which queries for an authorization for a sprecific path(URI). This interceptor queries for
     * authorization of a path repeatedly for creating an acegi ConfigAttributeDefinition, which hits
     * the database over and over again in each page to determine if the current user is authorized
     * for the page. This cache eliminates the need to hit the database. However, this is a simple
     * and primitive cache used for only that purpose and should not be used anywhere else or any
     * other purpose in the application.
     */
    private class AuthorizationsCache {

        final Map<String, Authorization> authorizationsCache = new HashMap<String, Authorization>();
        final ReadWriteLock lock = new ReentrantReadWriteLock();
        final Lock r = lock.readLock();
        final Lock w = lock.writeLock();

        Authorization getAuthorization(String URI) {
            r.lock();
            try {
                return authorizationsCache.get(URI);
            } finally {
                r.unlock();
            }
        }

        public void loadAuthorizations(List<Authorization> authList) {
            w.lock();
            try {
                authorizationsCache.clear();
                for (Authorization auth : authList) {
                    authorizationsCache.put(auth.getAuthorizationURI(), auth);
                }
            } finally {
                w.unlock();
            }
        }

        public boolean initialized() {
            r.lock();
            try {
                return authorizationsCache.size() > 0;
            } finally {
                r.unlock();
            }
        }
    }

    public void createUser(User user, List<Role> selectedRoles) {
        user.setOwningDealer(null);
        if (getAuthorizationDao().userExists(user.getUsername())) {
            throw new RuntimeException("User already exists. Please choose another username.");
        }
        user.checkPasswordCreation();
        saveUser(user, selectedRoles);
    }

    public Role getDealerRole() {
        List<Role> roles = getAuthorizationDao().findAllRoles();
        return Role.getRole(roles, Role.ROLE_DEALER);
    }

    public void authorizeDealer(String username, String password) {
        User user = getAuthorizationDao().findUserByUserName(username);
        if(user == null || !user.getPassword().trim().equals(password.trim())) {
            throw new InvalidLoginCredentialsException();
        }
        //check for user status
        if(!user.isAccountNonExpired()) throw new UserNotActiveException();
        Role dealerRole = getDealerRole();
        if(!user.hasRole(dealerRole)) throw new InvalidLoginCredentialsException();
        //check for dealer status
        if(user.getOwningDealer().getDealerStatus().equals(UserStatus.Suspended)) throw new UserNotActiveException();
    }
    
    public User getUserForDealer(String dealerID) {
        List<User> users = getAuthorizationDao().findUsersByDealerID(dealerID);
        if(users.size() > 0) return users.get(0);
        else return null;
    }

    public void createUserForDealer(Long dealerId, User user) {
        AbstractDealer dealer = getDealerService().getDealer(dealerId);
        dealer.addUser(user);
        Role role = getDealerRole();
        role.addUser(user);
        getAuthorizationDao().saveOrUpdate(dealer);
        getNotificationService().sendUserCreatedInformationMail(user);
    }

    public User getApplicationAdminUser() {
        Role role = getRoleByName(Role.ROLE_ADMIN);
        Set<User> users = role.getUsers();
        for(User user : users) {
            return user; 
        }
        return null;
    }
    
    public List<User> getAllDealerUsers() {
        Role role = getRoleByName(Role.ROLE_DEALER);
        Set<User> users = role.getUsers();
        return new ArrayList<User>(users);
    }
    
    public List<User> getAllShopSupervisorUsers() {
        Role role = getRoleByName(Role.ROLE_SHOP_SUPERVISOR);
        Set<User> users = role.getUsers();
        return new ArrayList<User>(users);
    }
    
    public void changeUsersShop(User user, Long shopId) {
        getAuthorizationDao().update(user);
        if(shopId == 0l) {
            user.setOwningDealer(null);
        } else {
            AbstractDealer dealer = getDealerService().getDealer(shopId);
            user.setOwningDealer(dealer);
        }
    }
    
    public void assignAsShopSupervisor(User user) {
        getAuthorizationDao().update(user);
        User oldUser = ((DealerShop)user.getOwningDealer()).getShopSupervisor();
        if(oldUser != null) {
            oldUser.removeRole(Role.ROLE_SHOP_SUPERVISOR);
            getAuthorizationDao().update(oldUser);
        }
        Role supervisorRole = getAuthorizationDao().findRoleByName(Role.ROLE_SHOP_SUPERVISOR);
        user.addRole(supervisorRole);
    }

	@Override
	public void attach(Object o) {
		getAuthorizationDao().attach(o);		
	}

}
