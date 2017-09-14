/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.gateway.util;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author db2admin
 */
public class RechargeLogRecord implements Serializable {

    private String tumDealerId;
    private Date tumTimestamp;
    private Integer amount;
    private String subscriberBalanceBefore;
    private String subscriberBalanceAfter;
    private String subscriberMSISDN;
    
    public RechargeLogRecord() {
        super();
    }

    public String getTumDealerId() {
        return tumDealerId;
    }

    public void setTumDealerId(String tumDealerId) {
        this.tumDealerId = tumDealerId;
    }

    public Date getTumTimestamp() {
        return tumTimestamp;
    }

    public void setTumTimestamp(Date tumTimestamp) {
        this.tumTimestamp = tumTimestamp;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getSubscriberBalanceBefore() {
        return subscriberBalanceBefore;
    }

    public void setSubscriberBalanceBefore(String subscriberBalanceBefore) {
        this.subscriberBalanceBefore = subscriberBalanceBefore;
    }

    public String getSubscriberBalanceAfter() {
        return subscriberBalanceAfter;
    }

    public void setSubscriberBalanceAfter(String subscriberBalanceAfter) {
        this.subscriberBalanceAfter = subscriberBalanceAfter;
    }

    public String getSubscriberMSISDN() {
        return subscriberMSISDN;
    }

    public void setSubscriberMSISDN(String subscriberMSISDN) {
        this.subscriberMSISDN = subscriberMSISDN;
    }
    
    
}
