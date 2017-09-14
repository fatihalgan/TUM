package mcel.tump.security.dao;

import java.util.List;

import mcel.tump.security.domain.Authorization;
import mcel.tump.security.domain.Role;
import mcel.tump.security.domain.User;
import mcel.tump.util.persistence.IBaseDao;

public interface IAuthorizationDao extends IBaseDao {
    public User findUserByUserName(String userName);
    public boolean userExists(String username);
    public List<Authorization> findAuthorizationsOfPath(String path);
    public List<Authorization> findAllAuthorizations();
    public Role findRoleByName(String name);
    public List<User> findAllUsers();
    public List<User> findUsersByCriteria(String username, String email);
    public List<Role> findAllRoles();
    public Authorization findAuthorizationById(Long id);
    public Role findRoleById(Long id);
    public User findUserById(Long id);
    public List<User> findUsersByDealerID(String dealerId);
}
