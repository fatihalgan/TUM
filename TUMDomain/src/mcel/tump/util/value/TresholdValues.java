package mcel.tump.util.value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

public class TresholdValues implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1824714635294937727L;
    private Money maxAmountPerTransaction = new Money();
    private Money minAmountPerTransaction = new Money();
    private Money maxAmountPerDay = new Money();
    private int maxTransactionsPerDay;
    private Money minBalanceAlertValue = new Money();
    private Money minBalanceCriticalAlertValue = new Money();

    public Money getMaxAmountPerTransaction() {
        return maxAmountPerTransaction;
    }

    public void setMaxAmountPerTransaction(Money maxAmountPerTransaction) {
        this.maxAmountPerTransaction = maxAmountPerTransaction;
    }

    public Money getMinAmountPerTransaction() {
        return minAmountPerTransaction;
    }

    public void setMinAmountPerTransaction(Money minAmountPerTransaction) {
        this.minAmountPerTransaction = minAmountPerTransaction;
    }

    public Money getMaxAmountPerDay() {
        return maxAmountPerDay;
    }

    public void setMaxAmountPerDay(Money maxAmountPerDay) {
        this.maxAmountPerDay = maxAmountPerDay;
    }

    public int getMaxTransactionsPerDay() {
        return maxTransactionsPerDay;
    }

    public void setMaxTransactionsPerDay(int maxTransactionsPerDay) {
        this.maxTransactionsPerDay = maxTransactionsPerDay;
    }

    public Money getMinBalanceAlertValue() {
        return minBalanceAlertValue;
    }

    public void setMinBalanceAlertValue(Money minBalanceAlertValue) {
        this.minBalanceAlertValue = minBalanceAlertValue;
    }

    public Money getMinBalanceCriticalAlertValue() {
        return minBalanceCriticalAlertValue;
    }

    public void setMinBalanceCriticalAlertValue(Money minBalanceCriticalAlertValue) {
        this.minBalanceCriticalAlertValue = minBalanceCriticalAlertValue;
    }
    
    public String toString() {
        return "Max. Amount Per Transaction: " + getMaxAmountPerTransaction().toString() +
                " - Min. Amount Per Transaction: " + getMinAmountPerTransaction().toString() +
                " - Max. Amount Per Day: " + getMaxAmountPerDay() +
                " - Max. Transactions Per Day: " + getMaxTransactionsPerDay() +
                " - Min. Balance Alert Value: " + getMinBalanceAlertValue() +
                " - Min. Balance Critical Alert Value: " + getMinBalanceCriticalAlertValue();
    }
    
    public void validate() {
        if(maxAmountPerTransaction.getAmount().doubleValue() < getMinAmountPerTransaction().getAmount().doubleValue()) throw new RuntimeException("Max. amount per transaction cannot be less than min. amount per transaction");
        if(maxAmountPerTransaction.getAmount().doubleValue() > getMaxAmountPerDay().getAmount().doubleValue()) throw new RuntimeException("Max. amount per transaction cannot be greater than max. amount per day.");
        if(minAmountPerTransaction.getAmount().doubleValue() > getMaxAmountPerTransaction().getAmount().doubleValue()) throw new RuntimeException("Min. amount per transaction cannot be more than max. amount per transaction");
        if(maxAmountPerDay.getAmount().doubleValue() < getMaxAmountPerTransaction().getAmount().doubleValue()) throw new RuntimeException("Max. amount per day cannot be less than max. amount per transaction.");
        if(minBalanceAlertValue.getAmount().doubleValue() < getMinBalanceCriticalAlertValue().getAmount().doubleValue()) throw new RuntimeException("Min. balance alert values cannot be less than min. balance critical alert value");
        if(minBalanceCriticalAlertValue.getAmount().doubleValue() > getMinBalanceAlertValue().getAmount().doubleValue()) throw new RuntimeException("Min. balance critical alert value cannot be greater than min. balance alert value.");
    }
    
    public TresholdValues clone() {
        TresholdValues returnVal = new TresholdValues();
        returnVal.setMaxAmountPerDay(maxAmountPerDay);
        returnVal.setMaxAmountPerTransaction(maxAmountPerTransaction);
        returnVal.setMaxTransactionsPerDay(maxTransactionsPerDay);
        returnVal.setMinAmountPerTransaction(minAmountPerTransaction);
        returnVal.setMinBalanceAlertValue(minBalanceAlertValue);
        returnVal.setMinBalanceCriticalAlertValue(minBalanceCriticalAlertValue);
        return returnVal;
    }
    public TresholdValues add(TresholdValues values) {
        TresholdValues returnVal = clone();
        returnVal.setMaxAmountPerDay(returnVal.getMaxAmountPerDay().addAmount(values.getMaxAmountPerDay()));
        returnVal.setMaxAmountPerTransaction(returnVal.getMaxAmountPerTransaction().addAmount(values.getMaxAmountPerTransaction()));
        returnVal.setMaxTransactionsPerDay(returnVal.getMaxTransactionsPerDay() + values.getMaxTransactionsPerDay());
        returnVal.setMinAmountPerTransaction(returnVal.getMinAmountPerTransaction().addAmount(values.getMinAmountPerTransaction()));
        returnVal.setMinBalanceAlertValue(returnVal.getMinBalanceAlertValue().addAmount(values.getMinBalanceAlertValue()));
        returnVal.setMinBalanceCriticalAlertValue(returnVal.getMinBalanceCriticalAlertValue().addAmount(values.getMinBalanceCriticalAlertValue()));
        return returnVal;
    }
    
    public TresholdValues subtract(TresholdValues values) {
        TresholdValues returnVal = clone();
        returnVal.setMaxAmountPerDay(returnVal.getMaxAmountPerDay().subtractAmount(values.getMaxAmountPerDay()));
        returnVal.setMaxAmountPerTransaction(returnVal.getMaxAmountPerTransaction().subtractAmount(values.getMaxAmountPerTransaction()));
        returnVal.setMaxTransactionsPerDay(returnVal.getMaxTransactionsPerDay() - values.getMaxTransactionsPerDay());
        returnVal.setMinAmountPerTransaction(returnVal.getMinAmountPerTransaction().subtractAmount(values.getMinAmountPerTransaction()));
        returnVal.setMinBalanceAlertValue(returnVal.getMinBalanceAlertValue().subtractAmount(values.getMinBalanceAlertValue()));
        returnVal.setMinBalanceCriticalAlertValue(returnVal.getMinBalanceCriticalAlertValue().subtractAmount(values.getMinBalanceCriticalAlertValue()));
        return returnVal;
    }
    
    public TresholdValues subtractAll(List<TresholdValues> list) {
        TresholdValues returnVal = clone();
        Iterator<TresholdValues> it = list.iterator();
        while(it.hasNext()) {
            returnVal = returnVal.subtract(it.next());
        }
        return returnVal;
    }
    
    public static void validateSystemVsCumulative(TresholdValues system, TresholdValues cumulative, TresholdValues accountTresholds) {
        if(system.getMaxAmountPerDay().getAmount().doubleValue() < cumulative.getMaxAmountPerDay().getAmount().doubleValue()) {
            BigDecimal difference = cumulative.getMaxAmountPerDay().getAmount().subtract(system.getMaxAmountPerDay().getAmount());
            BigDecimal minAmount = accountTresholds.getMaxAmountPerDay().getAmount().subtract(difference);
            throw new RuntimeException("With the current settings, the dealer's max. amount per day value cannot be more than " + minAmount.toPlainString() + ". Please increase system wide parameters.");
        }
            
        if(system.getMaxTransactionsPerDay() < cumulative.getMaxTransactionsPerDay()) {
            int difference = cumulative.getMaxTransactionsPerDay() - system.getMaxTransactionsPerDay();
            int minAmount = accountTresholds.getMaxTransactionsPerDay() - difference;
            throw new RuntimeException("With the current settings, the dealer's max. transactions per day value cannot be more than " + minAmount + ". Please increase system wide parameters.");
        }
    }
    
    public static void validateSystemVsCumulative(TresholdValues system, TresholdValues cumulative) {
        if(system.getMaxAmountPerDay().getAmount().doubleValue() < cumulative.getMaxAmountPerDay().getAmount().doubleValue()) {
            throw new RuntimeException("The system wide max. amount per day value (" + system.getMaxAmountPerDay().getAmount().toPlainString() + ") cannot be less than the current cumulative amount per day values of all dealers (" + cumulative.getMaxAmountPerDay().getAmount().toPlainString() + ").");
        }
            
        if(system.getMaxTransactionsPerDay() < cumulative.getMaxTransactionsPerDay()) {
            throw new RuntimeException("The system wide max. transactions per day value (" + system.getMaxTransactionsPerDay() + ") cannot be less than the current cumulative number of max. transactions per day value of all dealers (" + cumulative.getMaxTransactionsPerDay() + ").");   
        }
    }
    
    public static void validateSystemVsAccount(TresholdValues system, TresholdValues account) {
        if(account.getMaxAmountPerTransaction().getAmount().doubleValue() > system.getMaxAmountPerTransaction().getAmount().doubleValue()) throw new RuntimeException("Max. amount per transaction value of a dealer (" + account.getMaxAmountPerTransaction().getAmount().toPlainString() + ") cannot be greater than the system max. amount per transaction value (" + system.getMaxAmountPerTransaction().getAmount().toPlainString() + ").");
    }
}
