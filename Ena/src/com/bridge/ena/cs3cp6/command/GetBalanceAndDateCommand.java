/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.cs3cp6.command;

import com.bridge.ena.cs.command.CSTokens;
import com.bridge.ena.cs3cp6.value.AccountInfo;
import com.bridge.ena.cs3cp6.value.DedicatedAccountInformation;
import com.bridge.ena.xmlrpc.serializer.Array;
import java.util.Date;
import com.bridge.ena.xmlrpc.serializer.Member;
import com.bridge.ena.xmlrpc.serializer.Struct;
import java.util.List;

/**
 *
 * @author db2admin
 */
public class GetBalanceAndDateCommand extends AbstractCS3CP6Command {

    private String subscriberNumber;
    private Date originTimestamp;
    private String originTransactionID;
    private Float decimalDenominator;

    public GetBalanceAndDateCommand(String subscriberNumber, Date originTimestamp, String originTransactionID,
        float decimalDenominator, String subscriberNoPrefix, Integer subscriberNumberNAI) {
        super(subscriberNoPrefix, subscriberNumberNAI);
        this.subscriberNumber = subscriberNumber;
        this.originTimestamp = originTimestamp;
        this.originTransactionID = originTransactionID;
        this.decimalDenominator = decimalDenominator;
    }

    public void prepareRequest() {
        request.setMethodName(CS3CP6Tokens.GetBalanceAndDateMethodCallName.toString());
        request.addMember(new Member(CSTokens.OriginNodeType.toString(), getOriginNodeType()));
        request.addMember(new Member(CSTokens.OriginHostName.toString(), getOriginHostName()));
        request.addMember(new Member(CSTokens.OriginTransactionID.toString(), getOriginTransactionID()));
        request.addMember(new Member(CSTokens.OriginTimeStamp.toString(), getOriginTimestamp()));
        request.addMember(new Member(CSTokens.SubscriberNumberNAI.toString(), getSubscriberNumberNAI()));
        request.addMember(new Member(CSTokens.SubscriberNumber.toString(), getSubscriberNoPrefix() + getSubscriberNumber()));

    }

    public String getSubscriberNumber() {
        return subscriberNumber;
    }

    public Date getOriginTimestamp() {
        return originTimestamp;
    }

    @Override
    public String getOriginTransactionID() {
        return originTransactionID;
    }

    public Float getDecimalDenominator() {
        return decimalDenominator;
    }

    @Override
    public boolean supportsGetSubscriberInfo() {
        return true;
    }

    public AccountInfo getAccount() {
        AccountInfo returnVal = new AccountInfo();
        Object temp = response.getMemberValue(CSTokens.AccountValue1.toString());
        if(temp != null) returnVal.setAccountValue(convertFromCents((String)temp, decimalDenominator));
        temp = response.getMemberValue(CSTokens.CreditClearenceDate.toString());
        if(temp != null) returnVal.setCreditClearenceDate((Date)temp);
        temp = response.getMemberValue(CSTokens.ServiceClassCurrent.toString());
        if(temp != null) returnVal.setServiceClassCurrent((Integer)temp);
        temp = response.getMemberValue(CS3CP6Tokens.ServiceFeeExpiryDate.toString());
        if(temp != null) returnVal.setServiceFeeExpiryDate((Date)temp);
        temp = response.getMemberValue(CSTokens.ServiceRemovalDate.toString());
        if(temp != null) returnVal.setServiceRemovalDate((Date)temp);
        temp = response.getMemberValue(CS3CP6Tokens.SupervisionExpiryDate.toString());
        if(temp != null) returnVal.setSupervisionExpiryDate((Date)temp);
        temp = response.getMemberValue(CSTokens.DedicatedAccountInformation.toString());
        if(temp != null) {
            Array dedicatedAccountsArray = (Array)temp;
            List<Struct> dedicatedAccounts = dedicatedAccountsArray.getStructs();
            for(Struct i : dedicatedAccounts) {
             returnVal.addDedicatedAccountInfo(unmarshallDedicatedAccountInformation(i));
            }
        }
        temp = response.getMemberValue(CSTokens.TemporaryBlockedFlag.toString());
        if(temp != null) returnVal.setTemporaryBlocked((Boolean)temp);
        return returnVal;
    }

    public DedicatedAccountInformation getSMSAccount() {
       return getAccount().getDedicatedAccount(SMS_DEDICATED_ACCOUNT_ID);
    }

    public DedicatedAccountInformation getMMSAccount() {
        return getAccount().getDedicatedAccount(MMS_DEDICATED_ACCOUNT_ID);
    }
    
    public DedicatedAccountInformation getFreeCallsAccount() {
    	return getAccount().getDedicatedAccount(FREE_CALLS_DEDICATED_ACCOUNT_ID);
    }
    
    public DedicatedAccountInformation getNetmovelBundleAccount() {
    	return getAccount().getDedicatedAccount(NETMOVEL_BUNDLE_DEDICATED_ACCOUNT_ID);
    }
}
