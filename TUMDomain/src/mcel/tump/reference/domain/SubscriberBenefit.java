/*
 * SubscriberBenefit.java
 * 
 * Created on Aug 10, 2007, 12:44:16 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.reference.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import mcel.tump.util.persistence.Auditable;
import mcel.tump.util.value.Money;
import mcel.tump.util.view.beans.SelectableDataItem;

/**
 *
 * @author db2admin
 */
public class SubscriberBenefit implements Serializable, Auditable, SelectableDataItem, Comparable<SubscriberBenefit> {
    
    private Long id;
    private Money lowerLimit = new Money();
    private Money upperLimit = new Money();
    private int freeSMS;
    private int freeMMS;
    private Money freeCall = new Money();
    private Money instantBonus = new Money();
    private int makeCallTimeFrame;
    private int receiveCallTimeFrame;
        
    private String auditMessage;
    
    public SubscriberBenefit() {
        super();
    }
    
    public SubscriberBenefit(Long lowerLimit, Long upperLimit, int freeSMS, int freeMMS, long freeCall, float instantBonus, int makeCall, int receiveCall) {
        this.lowerLimit = new Money(new BigDecimal(lowerLimit));
        this.upperLimit = new Money(new BigDecimal(upperLimit));
        this.freeSMS = freeSMS;
        this.freeMMS = freeMMS;
        this.instantBonus = new Money(new BigDecimal(instantBonus));
        this.makeCallTimeFrame = makeCall;
        this.receiveCallTimeFrame = receiveCall;
        this.freeCall = new Money(new BigDecimal(freeCall));
    }
    
    public Money getInstantBonus() {
        return instantBonus;
    }

    public void setInstantBonus(Money bonus) {
        this.instantBonus = bonus;
    }

    public int getFreeMMS() {
        return freeMMS;
    }

    public void setFreeMMS(int freeMMS) {
        this.freeMMS = freeMMS;
    }

    public int getFreeSMS() {
        return freeSMS;
    }

    public void setFreeSMS(int freeSMS) {
        this.freeSMS = freeSMS;
    }

    public Money getLowerLimit() {
        return lowerLimit;
    }

    public void setLowerLimit(Money lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    public int getMakeCallTimeFrame() {
        return makeCallTimeFrame;
    }

    public void setMakeCallTimeFrame(int makeCallTimeFrame) {
        this.makeCallTimeFrame = makeCallTimeFrame;
    }

    public int getReceiveCallTimeFrame() {
        return receiveCallTimeFrame;
    }

    public void setReceiveCallTimeFrame(int receiveCallTimeFrame) {
        this.receiveCallTimeFrame = receiveCallTimeFrame;
    }

    public Money getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(Money upperLimit) {
        this.upperLimit = upperLimit;
    }

    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public String getAuditMessage() {
        return auditMessage;
    }

    public void setAuditMessage(String message) {
        this.auditMessage = message;
    }

    public int compareTo(SubscriberBenefit o) {
        return this.lowerLimit.compareTo(o.lowerLimit);
    }
    
    public void validate() {
        if(lowerLimit.getAmount().doubleValue() > getUpperLimit().getAmount().doubleValue()) throw new RuntimeException("Lower limit cannot be greater than upper limit.");
        if(upperLimit.getAmount().doubleValue() < lowerLimit.getAmount().doubleValue()) throw new RuntimeException("Upper limit cannot be less than than lower limit.");
    }

    public Money getFreeCall() {
        return freeCall;
    }

    public void setFreeCall(Money freeCall) {
        this.freeCall = freeCall;
    }
}
