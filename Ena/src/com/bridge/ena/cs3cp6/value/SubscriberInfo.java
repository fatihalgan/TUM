/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.cs3cp6.value;

import java.io.Serializable;

/**
 *
 * @author db2admin
 */
public class SubscriberInfo implements Serializable {

    private String subscriberMSISDN;
    private AccountInfo accountBefore;
    private AccountInfo accountAfter;

    public SubscriberInfo() {
        super();
    }
    
    /**
     * @return the subscriberMSISDN
     */
    public String getSubscriberMSISDN() {
        return subscriberMSISDN;
    }

    /**
     * @param subscriberMSISDN the subscriberMSISDN to set
     */
    public void setSubscriberMSISDN(String subscriberMSISDN) {
        this.subscriberMSISDN = subscriberMSISDN;
    }

    /**
     * @return the accountBefore
     */
    public AccountInfo getAccountBefore() {
        return accountBefore;
    }

    /**
     * @param accountBefore the accountBefore to set
     */
    public void setAccountBefore(AccountInfo accountBefore) {
        this.accountBefore = accountBefore;
    }

    /**
     * @return the accountAfter
     */
    public AccountInfo getAccountAfter() {
        return accountAfter;
    }

    /**
     * @param accountAfter the accountAfter to set
     */
    public void setAccountAfter(AccountInfo accountAfter) {
        this.accountAfter = accountAfter;
    }

    

}
