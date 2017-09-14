/*
 * RegisterPaymentDBResponse.java
 * 
 * Created on Oct 17, 2007, 1:53:27 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.account.dao;

/**
 *
 * @author db2admin
 */
public class RegisterPaymentDBResponse {

    private Long tumTransactionId;
    private Integer dbResult;
    private Integer balanceBefore;
    private Integer balanceAfter;
    private Integer tumDealerID;
    
    public RegisterPaymentDBResponse() {
        super();
    }

    public Integer getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(Integer balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    public Integer getBalanceBefore() {
        return balanceBefore;
    }

    public void setBalanceBefore(Integer balanceBefore) {
        this.balanceBefore = balanceBefore;
    }

    public Integer getDbResult() {
        return dbResult;
    }

    public void setDbResult(Integer dbResult) {
        this.dbResult = dbResult;
    }

    public Long getTumTransactionId() {
        return tumTransactionId;
    }

    public void setTumTransactionId(Long tumTransactionId) {
        this.tumTransactionId = tumTransactionId;
    }

    public Integer getTumDealerID() {
        return tumDealerID;
    }

    public void setTumDealerID(Integer tumDealerID) {
        this.tumDealerID = tumDealerID;
    }
}
