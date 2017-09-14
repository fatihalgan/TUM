/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.gateway.util;

import java.util.Date;
import java.util.Locale;

/**
 *
 * @author db2admin
 */
public interface TUMRechargeResponse extends TUMRechargeMessage, TUMResponseMessage {

    public String getTUMTransactionID();
    public void setTUMTransactionID(String tumTransactionID);
    public Date getTUMTimeStamp();
    public void setTUMTimeStamp(Date tumTimeStamp);
    public Integer getDealerBalanceBefore();
    public void setDealerBalanceBefore(Integer dealerBalanceBefore);
    public Integer getDealerBalanceAfter();
    public void setDealerBalanceAfter(Integer dealerBalanceAfter);
    public String getSubscriberBalanceBefore();
    public void setSubscriberBalanceBefore(String subscriberBalanceBefore);
    public String getSubscriberBalanceAfter();
    public void setSubscriberBalanceAfter(String subscriberBalanceAfter);
    public int getHttpStatusCode();
    public void setHttpStatusCode(int status);
    public boolean isFault();
    public Integer getResponseCode();
    public Integer getFaultCode();
    public Integer getFullResultCode();
    public void setResponseCode(Integer responseCode);
    public Locale getSubscriberLanguage();
    public void setSubscriberLanguage(Locale language);
    public void setSubscriberLanguage(Integer languageId);
    public Integer getServiceClassCurrent();
    public void setServiceClassCurrent(Integer serviceClassCurrent);
    public Integer getServiceClassBefore();
    public void setServiceClassBefore(Integer serviceClassBefore);
    public Date getSupervisionDate();
    public void setSupervisionDate(Date supervisionDate);
    public Date getServiceFeeDate();
    public void setServiceFeeDate(Date serviceFeeDate);
    public Integer getFreeSMS();
    public void setFreeSMS(Integer freeSMS);
    public Integer getFreeMMS();
    public void setFreeMMS(Integer freeMMS);
    public Float getDedicatedAccountValue(int dedicatedAccountID);
    public void setDedicatedAccountValue(int dedicatedAccountID, Float value);
    public void setDedicatedAccountValues(Float[] dedicatedAccountValues);
    public Float getRefillPortion();
    public void setRefillPortion(Float refillPortion);
    public Float getAdjustmentPortion();
    public void setAdjustmentPortion(Float adjustmentPortion);
    public String getVoucherProfileID();
    public void setVoucherProfileID(String profileID);
    public String getVoucherProfileName();
    public void setVoucherProfileName(String profileName);
    public String getVoucherSerialNumber();
    public String getVoucherActivationCode();
    public void setVoucherSerialNumber(String voucherSerialNumber);
    public String getVoucherValue();
    public void setVoucherValue(String value);
    public Integer getRefillResultCode();
    public void setRefillResultCode(Integer result);
    public Integer getAdjustmentResultCode();
    public void setAdjustmentResultCode(Integer result);
    public Integer getSubscriberBenefitSMS();
    public void setSubscriberBenefitSMS(Integer subscriberBenefitSMS);
    public Float getSubscriberBenefitBonusAmount();
    public void setSubscriberBenefitBonusAmount(Float bonusAmount);

}
