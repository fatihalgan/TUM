/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.dealer.domain;

import java.io.Serializable;
import mcel.tump.util.persistence.Auditable;
import mcel.tump.util.view.beans.SelectableDataItem;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *
 * @author db2admin
 */
public class IPAddress implements Serializable, Comparable<IPAddress>, SelectableDataItem, Auditable {

    private Long id;
    private AbstractDealer owningShop = null;
    private String ipAddress;
    private String auditMessage;
    
    public IPAddress() {
        super();
    }
    
    public IPAddress(DealerShop shop, String ipAddress) {
        this.owningShop = shop;
        this.ipAddress = ipAddress;
    }

    
    public AbstractDealer getOwningShop() {
        return owningShop;
    }

    public void setOwningShop(AbstractDealer owningShop) {
        this.owningShop = owningShop;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    
    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof IPAddress)) {
            return false;
        }
        IPAddress castOther = (IPAddress) other;
        return new EqualsBuilder().append(ipAddress, castOther.getIpAddress()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(-1300409017, 1507648711).append(getIpAddress()).toHashCode();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int compareTo(IPAddress o) {
        return this.getIpAddress().compareTo(o.getIpAddress());
    }

    public String getAuditMessage() {
        return auditMessage;
    }

    public void setAuditMessage(String auditMessage) {
        this.auditMessage = auditMessage;
    }
}
