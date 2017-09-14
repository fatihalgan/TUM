/*
 * BlacklistItem.java
 * 
 * Created on Aug 13, 2007, 10:54:44 AM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.reference.domain;

import java.io.Serializable;
import mcel.tump.account.domain.*;
import mcel.tump.util.view.beans.SelectableDataItem;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *
 * @author db2admin
 */
public class BlacklistItem implements Serializable, SelectableDataItem {

    private Long id;
    private Account account = new Account();
    private String subDealerId;
    
    
    public BlacklistItem() {
        super();
    }
    
    public BlacklistItem(Account account, String subDealerId) {
        this.account = account;
        this.subDealerId = subDealerId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getSubDealerId() {
        return subDealerId;
    }

    public void setSubDealerId(String subDealerId) {
        this.subDealerId = subDealerId;
    }
    
    public String getDealerCode() {
        return getAccount().getDealer().getDealerCode();
    }
    
    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof BlacklistItem)) {
            return false;
        }
        BlacklistItem castOther = (BlacklistItem) other;
        return new EqualsBuilder().append(getAccount(), castOther.getAccount()).append(subDealerId, castOther.subDealerId).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(736173745, 1705237981).append(getAccount()).append(subDealerId).toHashCode();
    }
}
