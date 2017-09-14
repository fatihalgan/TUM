package mcel.tump.util.value;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Money implements Serializable, Comparable<Money> {

    /**
     *
     */
    private static final long serialVersionUID = 1908288387912262332L;
    public static final String AIR_CREDIT = "MTS";
    private String currencySymbol;
    private BigDecimal amount = new BigDecimal(0);

    public Money() {
        this.currencySymbol = Money.AIR_CREDIT;
    }

    public Money(BigDecimal amount) {
        this.currencySymbol = Money.AIR_CREDIT;
        this.amount = amount;
    }

    public Money(String currencySymbol, BigDecimal amount) {
        this.currencySymbol = currencySymbol;
        this.amount = amount;
    }

    public Money(Currency currency, BigDecimal amount) {
        this.currencySymbol = currency.getSymbol();
        this.amount = amount;
    }

    public void setCurrencySymbol(String currency) {
        this.currencySymbol = currency;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public Currency getCurrency() {
        Currency cur = Currency.getInstance(currencySymbol);
        if (cur == null) {
            throw new RuntimeException("Unsupported Java currency code..");
        }
        return cur;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Money)) {
            return false;
        }
        Money other = (Money) o;
        return new EqualsBuilder().append(currencySymbol, other.currencySymbol).append(amount, other.amount).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder(437512835, -2076982985).append(currencySymbol).append(amount).toHashCode();
    }

    public String toString() {
        String returnVal = "";
        if(amount == null) return "-";
        return getAmount().toPlainString() + " " + currencySymbol;
    }

    public int compareTo(Money other) {
        if (!this.currencySymbol.equals(other.currencySymbol)) {
            throw new RuntimeException("Cannot compare monetary amounts from different currencies.");
        }
        return this.amount.compareTo(other.amount);
    }

    public static List<Money> getRangeBetween(Money start, Money end, Money artisDegeri) {
        List<Money> returnVal = new ArrayList<Money>();
        BigDecimal baslangic = start.getAmount();
        BigDecimal bitis = end.getAmount();
        BigDecimal araDeger = baslangic;
        while (true) {
            araDeger = araDeger.add(artisDegeri.getAmount());
            if (araDeger.doubleValue() <= bitis.doubleValue()) {
                returnVal.add(new Money(araDeger));
            } else {
                break;
            }
        }
        return returnVal;
    }
    
    public Money addAmount(Money val) {
        if(!getCurrencySymbol().equals(val.getCurrencySymbol())) throw new RuntimeException("Money: Cannot add amount from different currency!");
        BigDecimal amount = this.getAmount().add(val.getAmount());
        return new Money(val.getCurrencySymbol(), amount);
    }
    
    public Money subtractAmount(Money val) {
        if(!getCurrencySymbol().equals(val.getCurrencySymbol())) throw new RuntimeException("Money: Cannot add amount from different currency!");
        BigDecimal amount = this.getAmount().subtract(val.getAmount());
        return new Money(val.getCurrencySymbol(), amount);
    }
}
