package mcel.tump.security.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import mcel.tump.dealer.domain.AbstractDealer;
import mcel.tump.util.persistence.Auditable;
import mcel.tump.util.view.beans.SelectableDataItem;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.userdetails.UserDetails;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.joda.time.MutableDateTime;

public class User implements Serializable, Comparable<User>, UserDetails, SelectableDataItem, Auditable {

    /**
     *
     */
    private static final long serialVersionUID = 8835103865005881172L;
    private Long id;
    private String username;
    private String password;
    private String confirmPassword;
    private String email;
    private Date lastValidDate;
    private UserStatus status = UserStatus.Active;
    private AbstractDealer owningDealer = null;
    private int version;

    private Set<Role> userRoles = new TreeSet<Role>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getLastValidDate() {
        return lastValidDate;
    }

    public void setLastValidDate(Date lastValidDate) {
        this.lastValidDate = lastValidDate;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof User)) {
            return false;
        }
        User castOther = (User) other;
        return new EqualsBuilder().append(username, castOther.username).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(-1987210545, -1382830007).append(username).toHashCode();
    }

    @Override
    public int compareTo(User o) {
        return this.getUsername().compareTo(o.getUsername());
    }

    public Set<Role> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(Set<Role> userRoles) {
        this.userRoles = userRoles;
    }

    public void addRole(Role role) {
        getUserRoles().add(role);
        role.getUsers().add(this);
    }

    public void removeRole(Role role) {
        getUserRoles().remove(role);
        role.getUsers().remove(this);
    }
    
    public void removeRole(String roleName) {
        Set<Role> items = getUserRoles();
        for(Role role : items) {
            if(role.getRoleName().equals(roleName)) {
                removeRole(role);
            }
        }
    }

    public void removeNonMemberships(List<Role> memberships) {
        Role[] currentMemberSet = getUserRoles().toArray(new Role[getUserRoles().size()]);
    	for (Role r : currentMemberSet) {
            if(!memberships.contains(r)) removeRole(r);
        }
    }

    public GrantedAuthority[] getAuthorities() {
        ArrayList<Authorization> auths = new ArrayList<Authorization>();
        for (Role role : getUserRoles()) {
            auths.addAll(role.getRoleAuthorizations());
        }
        GrantedAuthority[] returnVal = new GrantedAuthority[auths.size()];
        int i = 0;
        for (Authorization auth : auths) {
            returnVal[i] = auth;
            i++;
        }
        return returnVal;
    }

    public boolean isAccountNonExpired() {
        return lastValidDate.after(new Date());
    }

    public boolean isAccountNonLocked() {
        return status == status.Active;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return true;
    }

    public String toString() {
        return getUsername();
    }
    
    public void checkPasswordCreation() {
        if(!password.equals(confirmPassword)) {
            throw new RuntimeException("Password and password confirmation fields do not match. Please re-type password again.");
        }
    }
    
    public boolean hasRole(Role check) {
        for(Role role : getUserRoles()) {
            if(role.equals(check)) return true;
        }
        return false;
    }

    public String getAuditMessage() {
        return "";
    }

    public void setAuditMessage(String message) {
        
    }

    public AbstractDealer getOwningDealer() {
        return owningDealer;
    }

    public void setOwningDealer(AbstractDealer owningDealer) {
        this.owningDealer = owningDealer;
    }
    
    public boolean updatePassword(String newPassword, String confirmPassword) {
        if(newPassword.equals(confirmPassword)) {
            setPassword(newPassword);
            setConfirmPassword(confirmPassword);
            MutableDateTime exprDate = new MutableDateTime();
            exprDate.addMonths(1);
            setLastValidDate(exprDate.toDate());
            return true;
        }
        return false;
    }
    
    public boolean isShopSupervisor() {
        for(Role role : getUserRoles()) {
            if(role.getRoleName().equals(Role.ROLE_SHOP_SUPERVISOR)) return true;
        }
        return false;
    }
    
}
