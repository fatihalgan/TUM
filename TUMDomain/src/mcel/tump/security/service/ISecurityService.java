package mcel.tump.security.service;

import java.util.List;
import mcel.tump.security.domain.Authorization;
import mcel.tump.security.domain.Role;
import mcel.tump.security.domain.User;
import mcel.tump.util.service.IBaseService;

public interface ISecurityService extends IBaseService {

    public List<User> getAllUsers();
    public List<User> getUsersByCriteria(String username, String email);
    public List<Authorization> getAllAuthorizations();
    public User getUser(Long id);
    public Role getRole(Long id);
    public Authorization getAuthorization(Long id);
    public void saveUser(User user);
    public void resetUserPassword(User user);
    public void createUser(User user, List<Role> selectedRoles);
    public void saveUser(User user, List<Role> selectedRoles);
    public void saveAuthorization(Authorization auth);
    public void saveAuthorization(Authorization auth, List<Role> selectedRoles);
    public void saveRole(Role role, List<User> selectedUsers);
    public void saveRole(Role role);
    public List<Role> getAllRoles();
    public void deleteUser(User user);
    public void deleteAuthorization(Authorization authorization);
    public void deleteRole(Role role);
    public Role getRoleByName(String roleName);
    public Authorization getAuthorizationOfPath(String path);
    public User loadUserByUsername(String username);
    public Role getDealerRole();
    public void authorizeDealer(String username, String password);
    public void createUserForDealer(Long dealerId, User user);
    public User getApplicationAdminUser();
    public User getUserForDealer(String dealerID);
    public List<User> getAllDealerUsers();
    public List<User> getAllShopSupervisorUsers();
    public void changeUsersShop(User shop, Long shopId);
    public void assignAsShopSupervisor(User user);
}
