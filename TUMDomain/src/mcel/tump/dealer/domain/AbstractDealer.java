/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.dealer.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import mcel.tump.account.domain.Account;
import mcel.tump.security.domain.User;
import mcel.tump.security.domain.UserStatus;
import mcel.tump.util.persistence.Auditable;
import mcel.tump.util.view.beans.SelectableDataItem;

/**
 *
 * @author db2admin
 */
public abstract class AbstractDealer implements Serializable, AccountHolder, Auditable, Comparable<AbstractDealer>, SelectableDataItem  {

    protected Long id;
    protected String dealerName;
    protected String dealerCode;
    protected boolean deleted;
    protected Account account = new Account();
    protected UserStatus dealerStatus = UserStatus.Active;
    protected Set<AbstractDealer> subDealers = new HashSet<AbstractDealer>();
    protected AbstractDealer parentDealer = null;
    protected String auditMessage;

    public AbstractDealer() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getDealerCode() {
        return dealerCode;
    }

    public void setDealerCode(String dealerCode) {
        this.dealerCode = dealerCode;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getAuditMessage() {
        return auditMessage;
    }

    public void setAuditMessage(String message) {
        this.auditMessage = message;
    }

    public AbstractDealer getParentDealer() {
        return parentDealer;
    }

    public void setParentDealer(AbstractDealer parentDealer) {
        this.parentDealer = parentDealer;
    }

    public Set<AbstractDealer> getSubDealers() {
        return subDealers;
    }

    public void setSubDealers(Set<AbstractDealer> subDealers) {
        this.subDealers = subDealers;
    }

    public void addDealer(AbstractDealer shop) {
        if(shop.getParentDealer() != null) shop.getParentDealer().removeDealer(shop);
        getSubDealers().add(shop);
        shop.setParentDealer(this);
    }

    public void removeDealer(AbstractDealer shop) {
        getSubDealers().remove(shop);
        shop.setParentDealer(null);
    }

    public void setNewParent(AbstractDealer parent) {
        if(getParentDealer() != null) getParentDealer().removeDealer(this);
        if(parent != null) {
            parent.addDealer(this);
        } else {
            setParentDealer(null);
        }
    }

    public UserStatus getDealerStatus() {
        return dealerStatus;
    }

    public void setDealerStatus(UserStatus dealerStatus) {
        this.dealerStatus = dealerStatus;
    }

    public abstract void deactivateAllUsers();

    public int compareTo(AbstractDealer o) {
        return this.getDealerName().compareTo(o.getDealerName());
    }

    public String toString() {
        return getDealerCode() + " " + getDealerName();
    }

    public abstract void removeIPAddress(IPAddress address);

    public abstract void addIPAddress(IPAddress address);

    public abstract void addUser(User user);

    public abstract boolean hasIPAddress(String ipAddress);

    public abstract DealerTypes getDealerType();

}
