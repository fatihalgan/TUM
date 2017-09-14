/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.dealer.domain;

import mcel.tump.security.domain.User;
import mcel.tump.security.domain.UserStatus;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *
 * @author db2admin
 */
public class PersonalDealer extends AbstractDealer {

    private User user;
    private IPAddress ipAddress;

    public PersonalDealer() {
        super();
    }
    
    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return the ipAddress
     */
    public IPAddress getIpAddress() {
        return ipAddress;
    }

    /**
     * @param ipAddress the ipAddress to set
     */
    public void setIpAddress(IPAddress ipAddress) {
        this.ipAddress = ipAddress;
    }


    @Override
    public void deactivateAllUsers() {
        user.setStatus(UserStatus.Suspended);
    }

    

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof PersonalDealer)) {
            return false;
        }
        PersonalDealer castOther = (PersonalDealer) other;
        return new EqualsBuilder().append(dealerCode, castOther.dealerCode).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(-1300409025, 1507648711).append(dealerCode).toHashCode();
    }

    @Override
    public void removeIPAddress(IPAddress address) {
        setIpAddress(null);
    }

    @Override
    public void addIPAddress(IPAddress address) {
        setIpAddress(address);
        address.setOwningShop(this);
    }

    @Override
    public void addUser(User user) {
        setUser(user);
        user.setOwningDealer(this);
    }

    @Override
    public boolean hasIPAddress(String ipAddress) {
        if(getIpAddress().getIpAddress().equals(ipAddress)) return true;
        return false;
    }

    @Override
    public DealerTypes getDealerType() {
        return DealerTypes.PersonalDealer;
    }

   
}
