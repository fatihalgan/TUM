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
public class ExternalDealer extends AccpacAccountable implements SignatureKeyHolder {

    protected User user = null;
    protected IPAddress ipAddress = null;
    protected String keyAlias;

    public ExternalDealer() {
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

    /**
     * @return the keyAlias
     */
    public String getKeyAlias() {
        return keyAlias;
    }

    /**
     * @param keyAlias the keyAlias to set
     */
    public void setKeyAlias(String keyAlias) {
        this.keyAlias = keyAlias;
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
        if (!(other instanceof ExternalDealer)) {
            return false;
        }
        ExternalDealer castOther = (ExternalDealer) other;
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
    public String getAccpacCode() {
        return getDealerCode();
    }

    @Override
    public DealerTypes getDealerType() {
        return DealerTypes.ExternalDealer;
    }
}