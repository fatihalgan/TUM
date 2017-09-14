/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.cs3cp6.command;

import com.bridge.ena.cs.command.AbstractCSCommand;
import com.bridge.ena.cs.command.CSTokens;
import com.bridge.ena.cs.value.SubscriberInfo;
import com.bridge.ena.cs3cp6.value.AccountFlags;
import com.bridge.ena.cs3cp6.value.DedicatedAccountInformation;
import com.bridge.ena.cs.value.FAFNumber;
import com.bridge.ena.xmlrpc.serializer.Array;
import com.bridge.ena.xmlrpc.serializer.Struct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author db2admin
 */
public abstract class AbstractCS3CP6Command extends AbstractCSCommand {

    public AbstractCS3CP6Command(String subscriberNoPrefix, Integer subscriberNumberNAI) {
        super(subscriberNoPrefix, subscriberNumberNAI);
    }

    public Integer getCurrentLanguageID() {
        Object value = response.getMembers().getValue(CS3CP6Tokens.LanguageIDCurrent.toString());
        if(value != null) return ((Integer)value);
        else return null;
    }

    @Override
    public Date getSupervisionExpiryDate() {
        Object value = response.getMembers().getValue(CS3CP6Tokens.SupervisionExpiryDate.toString());
        if(value != null) return (chopHoursOfDate((Date)value));
        else return null;
    }

    @Override
    public Date getServiceFeeExpiryDate() {
        Object value = response.getMembers().getValue(CS3CP6Tokens.ServiceFeeExpiryDate.toString());
        if(value != null) return (chopHoursOfDate((Date)value));
        else return null;
    }

    public List<FAFNumber> getFAFInformationList() {
        List<FAFNumber> retrunVal = new ArrayList<FAFNumber>();
        Object value = response.getMembers().getValue(CS3CP6Tokens.FafInformationList.toString());
        if(value == null) return new ArrayList<FAFNumber>();
        Array fafInformationList = (Array)value;
        List<Struct> fafNumbers = fafInformationList.getStructs();
        for(Struct i : fafNumbers) {
            String msisdn = (String)i.getMember(CSTokens.FafNumber.toString()).getValue();
            Integer fafIndicator = (Integer)i.getMember(CSTokens.FafIndicator.toString()).getValue();
            FAFNumber fafNumber = new FAFNumber(msisdn, fafIndicator);
            retrunVal.add(fafNumber);
        }
        return retrunVal;
    }
    
    public AccountFlags getAccountFlags() {
    	AccountFlags flags = null;
    	Object value = response.getMemberValue(CS3CP6Tokens.AccountFlags.toString());
    	if(value == null) return null;
    	flags = new AccountFlags();
    	Struct accountFlags = (Struct)value;
    	Object temp = accountFlags.getValue(CS3CP6Tokens.ActivationStatusFlag.toString());
    	if(temp != null) flags.setActivationStatusFlag((Boolean)temp);
    	temp = accountFlags.getValue(CS3CP6Tokens.NegativeBarringStatusFlag.toString());
    	if(temp != null) flags.setNegativeBarringStatusFlag((Boolean)temp);
    	temp = accountFlags.getValue(CS3CP6Tokens.ServiceFeePeriodExpiryFlag.toString());
    	if(temp != null) flags.setServiceFeePeriodExpiryFlag((Boolean)temp);
    	temp = accountFlags.getValue(CS3CP6Tokens.ServiceFeePeriodWarningActiveFlag.toString());
    	if(temp != null) flags.setServiceFeePeriodWarningActiveFlag((Boolean)temp);
    	temp = accountFlags.getValue(CS3CP6Tokens.SupervisionPeriodExpiryFlag.toString());
    	if(temp != null) flags.setSupervisionPeriodExpiryFlag((Boolean)temp);
    	temp = accountFlags.getValue(CS3CP6Tokens.SupervisionPeriodWarningActiveFlag.toString());
    	if(temp != null) flags.setSupervisionPeriodWarningActiveFlag((Boolean)temp);
    	return flags;
    }

    @Override
    public SubscriberInfo getSubscriberInfo() {
        SubscriberInfo info = new SubscriberInfo();
        info.setAccountValue(getSubscriberBalance(getDecimalDenominator()));
        info.setBalanceBefore(getSubscriberBalance(getDecimalDenominator()));
        info.setCreditClearenceDate(getCreditClearenceDate());
        info.setCurrency(getCurrency());
        info.setLanguageID(getCurrentLanguageID());
        info.setMmsBalance(getFreeMMS(getDecimalDenominator()));
        info.setSmsBalance(getFreeSMS(getDecimalDenominator()));
        info.setFreeCallBalance(getFreeCalls(getDecimalDenominator()));
        info.setServiceFeeDate(getServiceFeeExpiryDate());
        info.setServiceRemovalDate(getServiceRemovalDate());
        info.setSubscriberMSISDN(getSubscriberMSISDN());
        info.setSupervisionDate(getSupervisionExpiryDate());
        info.setServiceClassCurrent(getServiceClassCurrent());
        return info;
    }

    protected DedicatedAccountInformation unmarshallDedicatedAccountInformation(Struct dedicatedAccountInfo) {
        DedicatedAccountInformation returnVal = new DedicatedAccountInformation();
        Object temp = dedicatedAccountInfo.getValue(CSTokens.DedicatedAccountID.toString());
        if(temp != null) returnVal.setDedicatedAccountId((Integer)temp);
        temp = dedicatedAccountInfo.getValue(CSTokens.DedicatedAccountValue1.toString());
        if(temp != null) returnVal.setDedicatedAccountValue(convertFromCents((String)temp, getDecimalDenominator()));
        temp = dedicatedAccountInfo.getValue(CSTokens.ExpiryDate.toString());
        if(temp != null) returnVal.setExpiryDate((Date)temp);
        return returnVal;
    }
    
}
