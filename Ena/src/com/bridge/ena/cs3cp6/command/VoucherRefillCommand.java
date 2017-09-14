/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.cs3cp6.command;

import com.bridge.ena.cs.command.CSTokens;
import com.bridge.ena.cs.value.SubscriberInfo;
import com.bridge.ena.vs.profiles.VoucherProfile;
import com.bridge.ena.vs.profiles.VoucherProfilesLoader;
import com.bridge.ena.xmlrpc.serializer.Member;
import java.util.Date;

/**
 *
 * @author db2admin
 */
public class VoucherRefillCommand extends AbstractRefillCommand {

    private String voucherActivationCode;
    
    protected VoucherRefillCommand(String originTransactionId, Date originTimestamp, String subscriberNumber,
        String voucherActivationCode, String externalData1, String externalData2, float decimalDenominator,
        String subscriberNoPrefix, Integer subscriberNumberNAI) {
        super(originTransactionId, originTimestamp, subscriberNumber, externalData1, externalData2,
            decimalDenominator, subscriberNoPrefix, subscriberNumberNAI);
        this.voucherActivationCode = voucherActivationCode;
    }

    public void prepareRequest() {
        request.setMethodName(CS3CP6Tokens.RefillMethodCallName.toString());
        request.addMember(new Member(CSTokens.OriginNodeType.toString(), getOriginNodeType()));
        request.addMember(new Member(CSTokens.OriginHostName.toString(), getOriginHostName()));
        request.addMember(new Member(CSTokens.OriginTransactionID.toString(), getOriginTransactionId()));
        request.addMember(new Member(CSTokens.OriginTimeStamp.toString(), getOriginTimestamp()));
        request.addMember(new Member(CSTokens.SubscriberNumberNAI.toString(), getSubscriberNumberNAI()));
        request.addMember(new Member(CSTokens.SubscriberNumber.toString(), getSubscriberNoPrefix() + getSubscriberNumber()));
        request.addMember(new Member(CS3CP6Tokens.VocuherActivationCode.toString(), getVoucherActivationCode()));
        if(externalData1 != null && externalData1.length() > 0)
        request.addMember(new Member(CSTokens.ExternalData1.toString(),getExternalData1()));
        if(externalData2 != null && externalData2.length() > 0)
        request.addMember(new Member(CSTokens.ExternalData2.toString(),getExternalData2()));
        request.addMember(new Member(CS3CP6Tokens.RequestRefillAccountAfterFlag.toString(), new Boolean(true)));
        request.addMember(new Member(CS3CP6Tokens.RequestRefillAccountBeforeFlag.toString(), new Boolean(true)));
        request.addMember(new Member(CS3CP6Tokens.RequestRefillDetailsFlag.toString(), new Boolean(true)));
    }

    public String getVoucherActivationCode() {
        return voucherActivationCode;
    }

    @Override
    public Float getTransactionAmount() {
        Object value = response.getMembers().getValue(CS3CP6Tokens.TransactionAmount.toString());
        if(value != null) return convertFromCents((String)value, decimalDenominator);
        else return null;
    }
    
    public Float getTransactionAmountConverted() {
    	Object value = response.getMembers().getValue(CS3CP6Tokens.TransactionAmountConverted.toString());
        if(value != null) return convertFromCents((String)value, decimalDenominator);
        else return null;
    }

    @Override
    public String getVoucherGroup() {
        Object value = response.getMembers().getValue(CS3CP6Tokens.VoucherGroup.toString());
        if(value != null) return (String)value;
        else return null;
    }
    
    public String getVoucherSerialNumber() {
    	Object value = response.getMembers().getValue(CS3CP6Tokens.VoucherSerialNumber.toString());
    	if(value != null) return (String)value;
    	else return null;
    }

    @Override
    public String getRefillProfileID() {
        if(getVoucherGroup() == null) return null;
        VoucherProfile profile = VoucherProfilesLoader.instance().getVoucherProfiles().getByProfileName(getVoucherGroup());
        return profile.getProfileID();
    }
}
