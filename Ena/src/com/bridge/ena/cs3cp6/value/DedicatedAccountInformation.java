/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.cs3cp6.value;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author db2admin
 */
public class DedicatedAccountInformation implements Serializable {

    private Integer dedicatedAccountId;
    private Float dedicatedAccountValue;
    private Date expiryDate;

    public DedicatedAccountInformation() {
        super();
    }

    public Integer getDedicatedAccountId() {
        return dedicatedAccountId;
    }

    public void setDedicatedAccountId(Integer dedicatedAccountId) {
        this.dedicatedAccountId = dedicatedAccountId;
    }

    public Float getDedicatedAccountValue() {
        return dedicatedAccountValue;
    }

    public void setDedicatedAccountValue(Float dedicatedAccountValue) {
        this.dedicatedAccountValue = dedicatedAccountValue;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
    
    public DedicatedAccountInformation copyPrototype() {
    	DedicatedAccountInformation val = new DedicatedAccountInformation();
    	val.setDedicatedAccountId(getDedicatedAccountId().intValue());
    	val.setDedicatedAccountValue(getDedicatedAccountValue().floatValue());
    	val.setExpiryDate(new Date(getExpiryDate().getTime()));
    	return val;
    }
    
    public void addAmount(DedicatedAccountInformation relative) {
    	this.dedicatedAccountValue = this.dedicatedAccountValue.floatValue() + relative.dedicatedAccountValue.floatValue();
    }
}
