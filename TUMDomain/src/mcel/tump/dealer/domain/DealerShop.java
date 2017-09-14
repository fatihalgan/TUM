package mcel.tump.dealer.domain;

import java.util.HashSet;
import java.util.Set;
import mcel.tump.security.domain.User;
import mcel.tump.security.domain.UserStatus;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

public class DealerShop extends AccpacAccountable implements SignatureKeyHolder {

    private Set<User> users = new HashSet<User>();
    private Set<IPAddress> ipAddresses = new HashSet<IPAddress>();
    private String keyAlias = null;
    
    public DealerShop() {
        super();
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof DealerShop)) {
            return false;
        }
        DealerShop castOther = (DealerShop) other;
        return new EqualsBuilder().append(dealerCode, castOther.dealerCode).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(-1300409025, 1507648711).append(dealerCode).toHashCode();
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
    
    public void addUser(User user) {
        getUsers().add(user);
        user.setOwningDealer(this);
    }
    
    public void removeUser(User user) {
        getUsers().remove(user);
        user.setOwningDealer(null);
    }
    
    public void deactivateAllUsers() {
        for(User user : getUsers()) {
            user.setStatus(UserStatus.Suspended);
        }
    }

    public Set<IPAddress> getIpAddresses() {
        return ipAddresses;
    }
    
    public void addIPAddress(IPAddress address) {
        getIpAddresses().add(address);
        address.setOwningShop(this);
    }
    
    public void removeIPAddress(IPAddress address) {
        getIpAddresses().remove(address);
        address.setOwningShop(null);
    }

    public void setIpAddresses(Set<IPAddress> ipAddresses) {
        this.ipAddresses = ipAddresses;
    }
    
    public boolean hasIPAddress(String ipAddress) {
        for(IPAddress address : getIpAddresses()) {
            if(address.getIpAddress().equals(ipAddress)) return true;
        }
        return false;
    }
    
    public User getShopSupervisor() {
        for(User user : getUsers()) {
            if(user.isShopSupervisor()) return user;
        }
        return null;
    }

    public String getKeyAlias() {
        return keyAlias;
    }

    public void setKeyAlias(String keyAlias) {
        this.keyAlias = keyAlias;
    }

    @Override
    public String getAccpacCode() {
        return dealerCode;
    }

    @Override
    public DealerTypes getDealerType() {
        return DealerTypes.DealerShop;
    }

    
}
