package mcel.tump.security.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import mcel.tump.util.persistence.Auditable;
import mcel.tump.util.view.beans.SelectableDataItem;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

public class Role implements Serializable, Comparable<Role>, SelectableDataItem, Auditable {

    /**
     *
     */
    private static final long serialVersionUID = 6741247021745155103L;
    
    public static final String ROLE_DEALER = "ROLE_DEALER";
    public static final String ROLE_SHOP_SUPERVISOR = "ROLE_SHOP_SUPERVISOR";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    
    private Long id;
    private String roleName;
    private String description;

    private Set<User> users = new TreeSet<User>();
    private Set<Authorization> roleAuthorizations = new TreeSet<Authorization>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Role)) {
            return false;
        }
        Role castOther = (Role) other;
        return new EqualsBuilder().append(roleName, castOther.roleName).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(-2129993481, 2132470805).append(roleName).toHashCode();
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public int compareTo(Role o) {
        return this.getRoleName().compareTo(o.getRoleName());
    }

    public void addUser(User user) {
        getUsers().add(user);
        user.getUserRoles().add(this);
    }

    public void removeUser(User user) {
        getUsers().remove(user);
        user.getUserRoles().remove(this);
    }
    
    public void addUsers(List<User> users) {
    	for(User u: users) {
    		if(!getUsers().contains(u)) {
    			getUsers().add(u);
    			u.getUserRoles().add(this);
    		}
    	}
    }

    public Set<Authorization> getRoleAuthorizations() {
        return roleAuthorizations;
    }

    public void setRoleAuthorizations(Set<Authorization> roleAuthorizations) {
        this.roleAuthorizations = roleAuthorizations;
    }

    public void addAuthorization(Authorization auth) {
        getRoleAuthorizations().add(auth);
        auth.getAuthorizedRoles().add(this);
    }

    public void removeAuthorization(Authorization auth) {
        getRoleAuthorizations().remove(auth);
        auth.getAuthorizedRoles().remove(this);
    }

    public void removeAuthorizations(List<Long> list) {
        Authorization[] items = getRoleAuthorizations().toArray(new Authorization[getRoleAuthorizations().size()]);
        for (Long i : list) {
            for (Authorization auth : items) {
                if (auth.getId().equals(i)) {
                    removeAuthorization(auth);
                }
            }
        }
    }
    
    public void removeNonMembers(List<User> members) {
        User[] currentMemberSet = getUsers().toArray(new User[getUsers().size()]);
    	for (User i : currentMemberSet) {
            if(!members.contains(i)) removeUser(i);
        }
    }

    @Override
    public String toString() {
        return getDescription();
    }
    
    public static Role getRole(Collection<Role> roles, String roleName) {
        Iterator<Role> it = roles.iterator();
        while(it.hasNext()) {
            Role role = it.next();
            if(role.getRoleName().equals(roleName)) return role;
        }
        return null;
    }
    
    public String getAuditMessage() {
        return "";
    }

    public void setAuditMessage(String message) {
        
    }
    
    public void removeAllUsers() {
		for(User u : getUsers()) {
			removeUser(u);
		}
	}
}
