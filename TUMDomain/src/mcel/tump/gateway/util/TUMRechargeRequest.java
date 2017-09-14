/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.gateway.util;


/**
 *
 * @author db2admin
 */
public interface TUMRechargeRequest extends TUMRechargeMessage, TUMRequestMessage {
	
	public String getVoucherActivationCode();
    public void setVoucherActivationCode(String voucherActivationCode);
    public Integer getTransactionAmountCommission();
    public void setTransactionAmountCommission(Integer transactionAmountCommission);
}
