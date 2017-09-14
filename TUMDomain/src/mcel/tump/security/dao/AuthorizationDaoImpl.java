package mcel.tump.security.dao;

import java.util.Iterator;
import java.util.List;
import org.hibernate.LockMode;
import mcel.tump.security.domain.Authorization;
import mcel.tump.security.domain.Role;
import mcel.tump.security.domain.User;
import mcel.tump.util.persistence.BaseDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;

public class AuthorizationDaoImpl extends BaseDao implements IAuthorizationDao {

    @Override
    public List<Authorization> findAllAuthorizations() {
        return getHibernateTemplate().find("from mcel.tump.security.domain.Authorization as auth");
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public List<Role> findAllRoles() {
        return getHibernateTemplate().find("from mcel.tump.security.domain.Role as role");
    }

    @Override
    public List<User> findAllUsers() {
        @SuppressWarnings("unchecked")
        List<User> users = getHibernateTemplate().find("from mcel.tump.security.domain.User " + "as user order by user.username");
        return users;
    }
    
    public List<User> findUsersByCriteria(String username, String email) {
        if(username == null) username = "";
        if(email == null) email = "";
        DetachedCriteria crit = DetachedCriteria.forClass(User.class)
                .add(Property.forName("username").like(username, MatchMode.ANYWHERE))
                .add(Property.forName("email").like(email, MatchMode.ANYWHERE))
                .addOrder(Order.asc("username"));
        List<User> users = getHibernateTemplate().findByCriteria(crit);
        return users;
        
    }

    @Override
    public Authorization findAuthorizationById(Long id) {
        return (Authorization) getHibernateTemplate().load(Authorization.class, id, LockMode.UPGRADE);
    }

    @Override
    public List<Authorization> findAuthorizationsOfPath(String path) {
        List<Authorization> auths = getHibernateTemplate().find("from mcel.tump.security.domain.Authorization " + "as auth where auth.authorizationURI like ?", '%' + path);
        return auths;
    }

    @Override
    public Role findRoleById(Long id) {
        return (Role) getHibernateTemplate().load(Role.class, id, LockMode.UPGRADE);
    }

    @Override
    public Role findRoleByName(String name) {
        List<Role> roles = getHibernateTemplate().find("from mcel.tump.security.domain.Role " + "as role where role.roleName = ?", new Object[]{name});
        if (roles.size() == 0) {
            return null;
        }
        Iterator<Role> it = roles.iterator();
        return it.next();
    }

    @Override
    public User findUserById(Long id) {
        return (User) getHibernateTemplate().load(User.class, id, LockMode.UPGRADE);
    }

    @Override
    public User findUserByUserName(String userName) {
        List<User> users = getHibernateTemplate().find("from mcel.tump.security.domain.User as user where user.username = ?", new Object[]{userName});
        if (users.size() == 0) {
            return null;
        }
        Iterator<User> it = users.iterator();
        return it.next();
    }

    public boolean userExists(String username) {
        List<User> users = getHibernateTemplate().find("from mcel.tump.security.domain.User as user where user.username = ?", username);
        if(users.size() == 0) return false;
        else return true;
    }
    
    public List<User> findUsersByDealerID(String dealerId) {
        return getHibernateTemplate().find("from mcel.tump.security.domain.User as user where user.owningDealer.dealerCode = ?", dealerId);
    }
}
