/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.cs3cp6.command;

import com.bridge.ena.cs.command.CSTokens;
import com.bridge.ena.cs.value.SubscriberInfo;
import com.bridge.ena.cs3cp6.value.AccountInfo;
import com.bridge.ena.cs3cp6.value.DedicatedAccountInformation;
import com.bridge.ena.cs3cp6.value.RefillInfo;
import com.bridge.ena.cs3cp6.value.RefillValue;
import com.bridge.ena.vs.profiles.VoucherProfile;
import com.bridge.ena.vs.profiles.VoucherProfiles;
import com.bridge.ena.vs.profiles.VoucherProfilesLoader;
import com.bridge.ena.xmlrpc.serializer.Array;
import com.bridge.ena.xmlrpc.serializer.Struct;
import java.util.Date;
import java.util.List;

/**
 *
 * @author db2admin
 */
public abstract class AbstractRefillCommand extends AbstractCS3CP6Command {

    protected String originTransactionId;
    protected Date originTimestamp;
    protected String subscriberNumber;
    protected String externalData1;
    protected String externalData2;
    protected float decimalDenominator;
    
    private Float[] dedicatedAccountValueChanges = null; 

    protected AbstractRefillCommand(String originTransactionId, Date originTimestamp, String subscriberNumber,
        String externalData1, String externalData2, float decimalDenominator, String subscriberNoPrefix, Integer subscriberNumberNAI) {
        super(subscriberNoPrefix, subscriberNumberNAI);
        this.originTransactionId = originTransactionId;
        this.originTimestamp = originTimestamp;
        this.subscriberNumber = subscriberNumber;
        this.externalData1 = externalData1;
        this.externalData2 = externalData2;
        this.decimalDenominator = decimalDenominator;
    }

    protected VoucherProfiles getVoucherProfiles() {
        return VoucherProfilesLoader.instance().getVoucherProfiles();
    }

    protected VoucherProfile matchPaymentProfile(Float transactionAmount) {
        VoucherProfile profile = getVoucherProfiles().getByNominalValue(transactionAmount);
        return profile;
    }

    @Override
    public boolean supportsGetSubscriberInfo() {
        return false;
    }

    @Override
    public SubscriberInfo getSubscriberInfo() {
        throw new UnsupportedOperationException("Operation not supported");
    }

    @Override
    public Float getDecimalDenominator() {
        return decimalDenominator;
    }

    public String getExternalData1() {
        return externalData1;
    }

    public String getExternalData2() {
        return externalData2;
    }

    public String getOriginTransactionId() {
        return originTransactionId;
    }

    public Date getOriginTimestamp() {
        return originTimestamp;
    }

    public String getSubscriberNumber() {
        return subscriberNumber;
    }

    public AccountInfo getAccountBeforeRefill() {
        Object value = response.getMemberValue(CS3CP6Tokens.AccountBeforeRefill.toString());
        if(value == null) return null;
        Struct accountBeforeRefill = (Struct)value;
        return unmarshallAccountInfo(accountBeforeRefill);
    }
    
    private RefillInfo unmarshallRefillInfo(Struct refillInfo) {
    	RefillInfo returnVal = new RefillInfo();
    	Object temp = refillInfo.getValue(CS3CP6Tokens.RefillValueTotal.toString());
    	if(temp != null) returnVal.setRefillValueTotal(unmarshallRefillValue((Struct)temp));
    	temp = refillInfo.getValue(CS3CP6Tokens.RefillValuePromotion.toString());
    	if(temp != null) returnVal.setRefillValuePromotion(unmarshallRefillValue((Struct)temp));
    	return returnVal;
    }
    
    private RefillValue unmarshallRefillValue(Struct refillValue) {
    	RefillValue returnVal = new RefillValue();
    	Object temp = refillValue.getValue(CS3CP6Tokens.RefillAmount1.toString());
    	if(temp != null) returnVal.setRefillAmount1(convertFromCents((String)temp, decimalDenominator));
    	temp = refillValue.getValue(CS3CP6Tokens.SupervisionDaysExtended.toString());
    	if(temp != null) returnVal.setSupervisionDaysExtended((Integer)temp);
    	temp = refillValue.getValue(CS3CP6Tokens.ServiceFeeDaysExtended.toString());
    	if(temp != null) returnVal.setServiceFeeDaysExtended((Integer)temp);
    	return returnVal;
    }

    private AccountInfo unmarshallAccountInfo(Struct accountInfo) {
        AccountInfo returnVal = new AccountInfo();
        Object temp = accountInfo.getValue(CS3CP6Tokens.ServiceClassTemporaryExpiryDate.toString());
        if(temp != null) returnVal.setServiceClassTemporaryExpiryDate((Date)temp);
        temp = accountInfo.getValue(CS3CP6Tokens.ServiceClassOriginal.toString());
        if(temp != null) returnVal.setServiceClassOriginal((Integer)temp);
        temp = accountInfo.getValue(CSTokens.ServiceClassCurrent.toString());
        if(temp != null) returnVal.setServiceClassCurrent((Integer)temp);
        temp = accountInfo.getValue(CS3CP6Tokens.ServiceFeeExpiryDate.toString());
        if(temp != null) returnVal.setServiceFeeExpiryDate((Date)temp);
        temp = accountInfo.getValue(CS3CP6Tokens.SupervisionExpiryDate.toString());
        if(temp != null) returnVal.setSupervisionExpiryDate((Date)temp);
        temp = accountInfo.getValue(CSTokens.CreditClearenceDate.toString());
        if(temp != null) returnVal.setCreditClearenceDate((Date)temp);
        temp = accountInfo.getValue(CSTokens.ServiceRemovalDate.toString());
        if(temp != null) returnVal.setServiceRemovalDate((Date)temp);
        temp = accountInfo.getValue(CSTokens.AccountValue1.toString());
        if(temp != null) returnVal.setAccountValue(convertFromCents((String)temp, decimalDenominator));
        temp = accountInfo.getValue(CSTokens.DedicatedAccountInformation.toString());
        if(temp != null) {
            Array dedicatedAccountsArray = (Array)temp;
            List<Struct> dedicatedAccounts = dedicatedAccountsArray.getStructs();
            for(Struct i : dedicatedAccounts) {
             returnVal.addDedicatedAccountInfo(unmarshallDedicatedAccountInformation(i));
            }
        }
        return returnVal;
    }

    public AccountInfo getAccountAfterRefill() {
        Object value = response.getMemberValue(CS3CP6Tokens.AccountAfterRefill.toString());
        if(value == null) return null;
        Struct accountAfterRefill = (Struct)value;
        return unmarshallAccountInfo(accountAfterRefill);
    }
    
    public RefillInfo getRefillInfo() {
    	Object value = response.getMemberValue(CS3CP6Tokens.RefillInformation.toString());
    	if(value == null) return null;
    	Struct refillInfo = (Struct)value;
    	return unmarshallRefillInfo(refillInfo);
    }

    public DedicatedAccountInformation getSMSAccountAfterRefill() {
       return getAccountAfterRefill().getDedicatedAccount(SMS_DEDICATED_ACCOUNT_ID);
    }

    public DedicatedAccountInformation getMMSAccountAfterRefill() {
        return getAccountAfterRefill().getDedicatedAccount(MMS_DEDICATED_ACCOUNT_ID);
    }

    public DedicatedAccountInformation getSMSAccountBeforeRefill() {
        return getAccountBeforeRefill().getDedicatedAccount(SMS_DEDICATED_ACCOUNT_ID);
    }

    public DedicatedAccountInformation getMMSAccountBeforeRefill() {
        return getAccountBeforeRefill().getDedicatedAccount(MMS_DEDICATED_ACCOUNT_ID);
    }
    
    public DedicatedAccountInformation getDedicatedAccountBeforeRefill(int dedicatedAccountID) {
    	return getAccountBeforeRefill().getDedicatedAccount(dedicatedAccountID);
    }
    
    public DedicatedAccountInformation getDedicatedAccountAfterRefill(int dedicatedAccountID) {
    	return getAccountAfterRefill().getDedicatedAccount(dedicatedAccountID);
    }

    public abstract Float getTransactionAmount();

    public abstract String getVoucherGroup();

    public abstract String getRefillProfileID();
    
    public Float[] getDedicatedAccountValueChanges() {
		if(dedicatedAccountValueChanges == null) {
			dedicatedAccountValueChanges = new Float[10];
			for(int i = 0; i < 10; i++) {
            	DedicatedAccountInformation dedicatedAccountAfterRefill = getDedicatedAccountAfterRefill(i+1);
            	DedicatedAccountInformation dedicatedAccountBeforeRefill = getDedicatedAccountBeforeRefill(i+1);
            	if(dedicatedAccountAfterRefill == null) dedicatedAccountValueChanges[i] = new Float(0);
            	else dedicatedAccountValueChanges[i] = dedicatedAccountAfterRefill.getDedicatedAccountValue() - dedicatedAccountBeforeRefill.getDedicatedAccountValue();
            }
		}
		return dedicatedAccountValueChanges;
	}
}
