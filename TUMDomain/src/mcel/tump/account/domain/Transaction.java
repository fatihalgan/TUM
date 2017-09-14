package mcel.tump.account.domain;

import java.io.Serializable;
import java.util.Date;
import mcel.tump.util.persistence.Auditable;
import mcel.tump.util.value.Money;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

public class Transaction implements Auditable, Serializable, Comparable<Transaction> {

    private Long id;
    private Money amount;
    private Date transTime;
    private String subscriberMSISDN;
    private String username;
    private String edgeDealerId;
    private String edgeSubdealerId;
    private String edgeTransactionId;
    private Date edgeTransTime;
    private Money beforeBalance = new Money();
    private Money afterBalance = new Money();
        
    private String auditMessage = null;

    public Transaction() {
        super();
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    

    public Money getAmount() {
        return amount;
    }

    public void setAmount(Money amount) {
        this.amount = amount;
    }

    public Date getTransTime() {
        return transTime;
    }

    public void setTransTime(Date transTime) {
        this.transTime = transTime;
    }

    @Override
    public int compareTo(Transaction arg0) {
        return this.getTransTime().compareTo(arg0.getTransTime());
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Transaction)) {
            return false;
        }
        Transaction castOther = (Transaction) other;
        return new EqualsBuilder().append(transTime, castOther.transTime).append(subscriberMSISDN, castOther.subscriberMSISDN).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(422638383, 1940021615).append(transTime).append(subscriberMSISDN).toHashCode();
    }

    public Money getAfterBalance() {
        return afterBalance;
    }

    public void setAfterBalance(Money afterBalance) {
        this.afterBalance = afterBalance;
    }

    public Money getBeforeBalance() {
        return beforeBalance;
    }

    public void setBeforeBalance(Money beforeBalance) {
        this.beforeBalance = beforeBalance;
    }

    public String getEdgeSubdealerId() {
        return edgeSubdealerId;
    }

    public void setEdgeSubdealerId(String edgeSubdealerId) {
        this.edgeSubdealerId = edgeSubdealerId;
    }

    public Date getEdgeTransTime() {
        return edgeTransTime;
    }

    public void setEdgeTransTime(Date edgeTransTime) {
        this.edgeTransTime = edgeTransTime;
    }

    public String getEdgeTransactionId() {
        return edgeTransactionId;
    }

    public void setEdgeTransactionId(String edgeTransactionId) {
        this.edgeTransactionId = edgeTransactionId;
    }

    public String getEdgeDealerId() {
        return edgeDealerId;
    }

    public void setEdgeDealerId(String edgeDealerId) {
        this.edgeDealerId = edgeDealerId;
    }

    public String getSubscriberMSISDN() {
        return subscriberMSISDN;
    }

    public void setSubscriberMSISDN(String subscriberMSISDN) {
        this.subscriberMSISDN = subscriberMSISDN;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuditMessage() {
       return auditMessage;
    }

    public void setAuditMessage(String message) {
        this.auditMessage = auditMessage;
    }
    
}
