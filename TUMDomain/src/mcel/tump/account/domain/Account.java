package mcel.tump.account.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import mcel.tump.dealer.domain.AbstractDealer;
import mcel.tump.dealer.domain.ExternalDealer;
import mcel.tump.util.persistence.Auditable;
import mcel.tump.util.value.Money;
import mcel.tump.util.value.ShopBalanceAlertType;
import mcel.tump.util.value.TresholdValues;
import mcel.tump.util.view.beans.SelectableDataItem;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

public class Account implements Serializable, SelectableDataItem, Auditable {

    /**
     *
     */
    private static final long serialVersionUID = 8138180112050973123L;
    private Long id;
    private Money balance = new Money();
    private TresholdValues tresholdValues = new TresholdValues();
    private Set<Transaction> transactions = new HashSet<Transaction>();
    private AbstractDealer dealer = null;
            
    
    private String auditMessage = null;
    
    public Account() {
        super();
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Money getBalance() {
        return balance;
    }

    public void setBalance(Money balance) {
        if(balance.getAmount().doubleValue() < 0) throw new RuntimeException("Balance not enough!");
        this.balance = balance;
    }

    public AbstractDealer getDealer() {
        return dealer;
    }

    public void setDealer(AbstractDealer dealerShop) {
        this.dealer = dealerShop;
    }

    public TresholdValues getTresholdValues() {
        if(tresholdValues == null) tresholdValues = new TresholdValues();
        return tresholdValues;
    }

    public void setTresholdValues(TresholdValues tresholdValues) {
        this.tresholdValues = tresholdValues;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Account)) {
            return false;
        }
        Account castOther = (Account) other;
        return new EqualsBuilder().append(dealer, castOther.dealer).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(736273745, 1705287981).append(dealer).toHashCode();
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void addTransaction(Transaction trans) {
        this.getTransactions().add(trans);
    }
    
    public String getAuditMessage() {
        return auditMessage;
    }

    public void setAuditMessage(String message) {
        this.auditMessage = message;
    }
    
    public ShopBalanceAlertType getAlertFlag(Integer balanceBefore, Integer balanceAfter) {
        if(getDealer() instanceof ExternalDealer) return ShopBalanceAlertType.NoAlert;
        Money minBalanceAlert = getTresholdValues().getMinBalanceAlertValue();
        Money criticalBalanceAlert = getTresholdValues().getMinBalanceCriticalAlertValue();
        if((balanceBefore.compareTo(criticalBalanceAlert.getAmount().intValue()) > 0)
                && (balanceAfter.compareTo(criticalBalanceAlert.getAmount().intValue()) <= 0)) {
            return ShopBalanceAlertType.CriticalBalanceAlert;
        } else if((balanceBefore.compareTo(minBalanceAlert.getAmount().intValue()) > 0)
                && (balanceAfter.compareTo(minBalanceAlert.getAmount().intValue()) <= 0)) {
            return ShopBalanceAlertType.LowBalanceAlert;
        } else return ShopBalanceAlertType.NoAlert;
    }
    
    public void validateTresholds(TresholdValues cumulativeTresholds, TresholdValues systemTresholds) {
        cumulativeTresholds = cumulativeTresholds.add(getTresholdValues());
        TresholdValues.validateSystemVsCumulative(systemTresholds, cumulativeTresholds, getTresholdValues());
        TresholdValues.validateSystemVsAccount(systemTresholds, getTresholdValues());
        getTresholdValues().validate();
    }
}
