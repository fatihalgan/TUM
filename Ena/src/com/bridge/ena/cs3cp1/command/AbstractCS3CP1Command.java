///*

package com.bridge.ena.cs3cp1.command;
import com.bridge.ena.cs.command.AbstractCSCommand;
import com.bridge.ena.cs.command.CSTokens;
import com.bridge.ena.cs.value.FAFNumber;
import com.bridge.ena.cs.value.SubscriberInfo;
import com.bridge.ena.xmlrpc.serializer.Array;
import com.bridge.ena.xmlrpc.serializer.Struct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 *
 * @author db2admin
 */
public abstract class AbstractCS3CP1Command extends AbstractCSCommand {
        
    public AbstractCS3CP1Command(String subscriberNoPrefix, Integer subscriberNumberNAI) {
        super(subscriberNoPrefix, subscriberNumberNAI);
    }

    public Float getAccountValueAfter1(Float decimalDenominator) {
        Object value = response.getMembers().getValue(CS3CP1Tokens.AccountValueAfter1.toString());
        if(value != null) return convertFromCents((String)value, decimalDenominator);
        else return null;
    }

    public Integer getCurrentLanguageID() {
        Object value = response.getMembers().getValue(CS3CP1Tokens.CurrentLanguageID.toString());
        if(value != null) return ((Integer)value);
        else return null;
    }

    @Override
    public Date getSupervisionExpiryDate() {
        Object value = response.getMembers().getValue(CS3CP1Tokens.SupervisionDate.toString());
        if(value != null) return (chopHoursOfDate((Date)value));
        else return null;
    }

    @Override
    public Date getServiceFeeExpiryDate() {
        Object value = response.getMembers().getValue(CS3CP1Tokens.ServiceFeeDate.toString());
        if(value != null) return (chopHoursOfDate((Date)value));
        else return null;
    }

    public Float getTransactionAmountRefill(Float decimalDenominator) {
        Object value = response.getMembers().getValue(CS3CP1Tokens.TransactionAmountRefill.toString());
        if(value != null) return convertFromCents((String)value, decimalDenominator);
        else return null;
    }

    public Integer getTransactionServiceClass() {
        Object value = response.getMembers().getValue(CS3CP1Tokens.TransactionServiceClass.toString());
        if(value != null) return (Integer)value;
        else return null;
    }

    public Integer getTransactionVVPeriodExt() {
        Object value = response.getMembers().getValue(CS3CP1Tokens.TransactionVVPeriodExt.toString());
        if(value != null) return (Integer)value;
        else return null;
    }

    public Date getValueVoucherDateAfter() {
        Object value = response.getMembers().getValue(CS3CP1Tokens.ValueVoucherDateAfter.toString());
        if(value != null) return (Date)value;
        else return null;
    }

    public Float getTransactionAmount1Refill(Float decimalDenominator) {
        Object value = response.getMembers().getValue(CS3CP1Tokens.TransactionAmount1Refill.toString());
        if(value != null) return convertFromCents((String)value, decimalDenominator);
        else return null;
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
        info.setFreeCallBalance(getDedicatedAccountValue(getDecimalDenominator(), 5));
        info.setServiceFeeDate(getServiceFeeExpiryDate());
        info.setServiceRemovalDate(getServiceRemovalDate());
        info.setSubscriberMSISDN(getSubscriberMSISDN());
        info.setSupervisionDate(getSupervisionExpiryDate());
        info.setServiceClassCurrent(getServiceClassCurrent());
        return info;
    }

    public List<FAFNumber> getFAFInformationList() {
        List<FAFNumber> retrunVal = new ArrayList<FAFNumber>();
        Object value = response.getMembers().getValue(CSTokens.FafInformation.toString());
        if(value == null) return null;
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
    
}
