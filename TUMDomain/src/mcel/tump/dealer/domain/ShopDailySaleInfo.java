/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.dealer.domain;

import java.io.Serializable;
import java.util.Date;
import mcel.tump.util.view.beans.SelectableDataItem;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *
 * @author db2admin
 */
public class ShopDailySaleInfo implements Serializable, SelectableDataItem {

    private Long id;
    private Date date;
    private int numberOfSales;
    private long salesAmount;
    private DealerShop owningDealer = new DealerShop();
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getNumberOfSales() {
        return numberOfSales;
    }

    public void setNumberOfSales(int numberOfSales) {
        this.numberOfSales = numberOfSales;
    }

    public long getSalesAmount() {
        return salesAmount;
    }

    public void setSalesAmount(long salesAmount) {
        this.salesAmount = salesAmount;
    }

    public DealerShop getOwningDealer() {
        return owningDealer;
    }

    public void setOwningDealer(DealerShop owningDealer) {
        this.owningDealer = owningDealer;
    }
    
    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ShopDailySaleInfo)) {
            return false;
        }
        ShopDailySaleInfo castOther = (ShopDailySaleInfo) other;
        return new EqualsBuilder().append(id, castOther.id).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(-1300409017, 1507648707).append(id).toHashCode();
    }
}
