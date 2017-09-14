/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.gateway.util;

/**
 *
 * @author db2admin
 */
public interface TUMRechargeMessage {
	public String getRequestTransactionID();
    public String getSubscriberMSISDN();
    public void setSubscriberMSISDN(String subscriberMSISDN);
    public Integer getTransferAmount();
    public void setTransferAmount(Integer transferAmount);
    public String getSenderMSISDN();
    public void setSenderMSISDN(String senderMSISDN);
    public String getVoucherSerialNumber();
    public void setVoucherSerialNumber(String voucherSerialNumber);
}
