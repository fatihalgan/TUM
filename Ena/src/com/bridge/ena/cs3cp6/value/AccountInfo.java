/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.cs3cp6.value;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author db2admin
 */
public class AccountInfo implements Serializable {

    private Date serviceClassTemporaryExpiryDate;
    private Integer serviceClassOriginal;
    private Integer serviceClassCurrent;
    private Date serviceFeeExpiryDate;
    private Date supervisionExpiryDate;
    private Date creditClearenceDate;
    private Date serviceRemovalDate;
    private Float accountValue;
    private boolean temporaryBlocked;
    private List<DedicatedAccountInformation> dedicatedAccounts = new ArrayList<DedicatedAccountInformation>();

    public AccountInfo() {
        super();
    }
    /**
     * @return the serviceClassTemporaryExpiryDate
     */
    public Date getServiceClassTemporaryExpiryDate() {
        return serviceClassTemporaryExpiryDate;
    }

    /**
     * @param serviceClassTemporaryExpiryDate the serviceClassTemporaryExpiryDate to set
     */
    public void setServiceClassTemporaryExpiryDate(Date serviceClassTemporaryExpiryDate) {
        this.serviceClassTemporaryExpiryDate = serviceClassTemporaryExpiryDate;
    }

    /**
     * @return the serviceClassOriginal
     */
    public Integer getServiceClassOriginal() {
        return serviceClassOriginal;
    }

    /**
     * @param serviceClassOriginal the serviceClassOriginal to set
     */
    public void setServiceClassOriginal(Integer serviceClassOriginal) {
        this.serviceClassOriginal = serviceClassOriginal;
    }

    /**
     * @return the serviceClassCurrent
     */
    public Integer getServiceClassCurrent() {
        return serviceClassCurrent;
    }

    /**
     * @param serviceClassCurrent the serviceClassCurrent to set
     */
    public void setServiceClassCurrent(Integer serviceClassCurrent) {
        this.serviceClassCurrent = serviceClassCurrent;
    }

    /**
     * @return the serviceFeeExpiryDate
     */
    public Date getServiceFeeExpiryDate() {
        return serviceFeeExpiryDate;
    }

    /**
     * @param serviceFeeExpiryDate the serviceFeeExpiryDate to set
     */
    public void setServiceFeeExpiryDate(Date serviceFeeExpiryDate) {
        this.serviceFeeExpiryDate = serviceFeeExpiryDate;
    }

    /**
     * @return the supervisionExpiryDate
     */
    public Date getSupervisionExpiryDate() {
        return supervisionExpiryDate;
    }

    /**
     * @param supervisionExpiryDate the supervisionExpiryDate to set
     */
    public void setSupervisionExpiryDate(Date supervisionExpiryDate) {
        this.supervisionExpiryDate = supervisionExpiryDate;
    }

    /**
     * @return the creditClearenceDate
     */
    public Date getCreditClearenceDate() {
        return creditClearenceDate;
    }

    /**
     * @param creditClearenceDate the creditClearenceDate to set
     */
    public void setCreditClearenceDate(Date creditClearenceDate) {
        this.creditClearenceDate = creditClearenceDate;
    }

    /**
     * @return the serviceRemovalDate
     */
    public Date getServiceRemovalDate() {
        return serviceRemovalDate;
    }

    /**
     * @param serviceRemovalDate the serviceRemovalDate to set
     */
    public void setServiceRemovalDate(Date serviceRemovalDate) {
        this.serviceRemovalDate = serviceRemovalDate;
    }

    /**
     * @return the accountValue
     */
    public Float getAccountValue() {
        return accountValue;
    }

    /**
     * @param accountValue the accountValue to set
     */
    public void setAccountValue(Float accountValue) {
        this.accountValue = accountValue;
    }

    /**
     * @return the dedicatedAccounts
     */
    public List<DedicatedAccountInformation> getDedicatedAccounts() {
        return dedicatedAccounts;
    }

    /**
     * @param dedicatedAccounts the dedicatedAccounts to set
     */
    public void setDedicatedAccounts(List<DedicatedAccountInformation> dedicatedAccounts) {
        this.dedicatedAccounts = dedicatedAccounts;
    }

    
    public void addDedicatedAccountInfo(DedicatedAccountInformation info) {
        getDedicatedAccounts().add(info);
    }

    public DedicatedAccountInformation getDedicatedAccount(Integer dedicatedAccountID) {
        for(DedicatedAccountInformation i : dedicatedAccounts) {
            if(i.getDedicatedAccountId().equals(dedicatedAccountID)) return i;
        }
        return null;
    }

    public boolean isTemporaryBlocked() {
        return temporaryBlocked;
    }

    public void setTemporaryBlocked(boolean temporaryBlocked) {
        this.temporaryBlocked = temporaryBlocked;
    }

    
    
}
