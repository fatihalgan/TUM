/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.cs.value;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author db2admin
 */
public class SubscriberInfo implements Serializable {
    private String subscriberMSISDN;
    private String currency;
    private Float accountValue;
    private Date supervisionDate;
    private Date creditClearenceDate;
    private Date serviceRemovalDate;
    private Date serviceFeeDate;
    private Integer smsBalance;
    private Integer mmsBalance;
    private Float freeCallBalance;
    private Integer languageID;
    private Float balanceBefore;
    private Integer serviceClassCurrent;
    private Integer serviceClassBefore;
    private Integer smsBalanceBefore;
    private Integer mmsBalanceBefore;
        
    
    public SubscriberInfo() {
        super();
    }
    
    public Float getAccountValue() {
        return accountValue;
    }

    public void setAccountValue(Float accountValue) {
        this.accountValue = accountValue;
    }

    public Date getCreditClearenceDate() {
        return creditClearenceDate;
    }

    public void setCreditClearenceDate(Date creditClearenceDate) {
        this.creditClearenceDate = creditClearenceDate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Date getServiceFeeDate() {
        return serviceFeeDate;
    }

    public void setServiceFeeDate(Date serviceFeeDate) {
        this.serviceFeeDate = serviceFeeDate;
    }

    public Date getServiceRemovalDate() {
        return serviceRemovalDate;
    }

    public void setServiceRemovalDate(Date serviceRemovalDate) {
        this.serviceRemovalDate = serviceRemovalDate;
    }

    public String getSubscriberMSISDN() {
        return subscriberMSISDN;
    }

    public void setSubscriberMSISDN(String subscriberMSISDN) {
        this.subscriberMSISDN = subscriberMSISDN;
    }

    public Date getSupervisionDate() {
        return supervisionDate;
    }

    public void setSupervisionDate(Date supervisionDate) {
        this.supervisionDate = supervisionDate;
    }

    public Integer getMmsBalance() {
        return mmsBalance;
    }

    public void setMmsBalance(Integer mmsBalance) {
        this.mmsBalance = mmsBalance;
    }

    public Integer getSmsBalance() {
        return smsBalance;
    }

    public void setSmsBalance(Integer smsBalance) {
        this.smsBalance = smsBalance;
    }

    public Integer getLanguageID() {
        return languageID;
    }

    public void setLanguageID(Integer language) {
        this.languageID = language;
    }
    
    public Locale getLanguageLocale() {
        if(languageID == 2) return Locale.ENGLISH;
        else return new Locale("por");
    }

    public Float getBalanceBefore() {
        return balanceBefore;
    }

    public void setBalanceBefore(Float balanceBefore) {
        this.balanceBefore = balanceBefore;
    }

    public Integer getServiceClassCurrent() {
        return serviceClassCurrent;
    }

    public void setServiceClassCurrent(Integer serviceClassCurrent) {
        this.serviceClassCurrent = serviceClassCurrent;
    }

    public Float getFreeCallBalance() {
        return freeCallBalance;
    }

    public void setFreeCallBalance(Float freeCallBalance) {
        this.freeCallBalance = freeCallBalance;
    }

    public Integer getSmsBalanceBefore() {
        return smsBalanceBefore;
    }

    public void setSmsBalanceBefore(Integer smsBalanceBefore) {
        this.smsBalanceBefore = smsBalanceBefore;
    }

    public Integer getMmsBalanceBefore() {
        return mmsBalanceBefore;
    }

    public void setMmsBalanceBefore(Integer mmsBalanceBefore) {
        this.mmsBalanceBefore = mmsBalanceBefore;
    }

    public Integer getServiceClassBefore() {
        return serviceClassBefore;
    }

    public void setServiceClassBefore(Integer serviceClassBefore) {
        this.serviceClassBefore = serviceClassBefore;
    }
}
