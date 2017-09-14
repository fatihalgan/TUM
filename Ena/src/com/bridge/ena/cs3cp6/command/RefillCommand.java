/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.cs3cp6.command;

import com.bridge.ena.cs.command.CSTokens;
import com.bridge.ena.vs.profiles.VoucherProfile;
import com.bridge.ena.vs.profiles.VoucherProfilesLoader;
import java.util.Date;
import com.bridge.ena.xmlrpc.serializer.Member;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author db2admin
 */
public class RefillCommand extends AbstractRefillCommand {

    private static Log logger = LogFactory.getLog(RefillCommand.class);
    private Float transactionAmount;
    private String refillProfileID;
    
    RefillCommand(String originTransactionId, Date originTimestamp, String subscriberNumber,
        Float transactionAmount, String externalData1, String externalData2, float decimalDenominator,
        String subscriberNoPrefix, Integer subscriberNumberNAI) {
        super(originTransactionId, originTimestamp, subscriberNumber, externalData1, externalData2,
            decimalDenominator, subscriberNoPrefix, subscriberNumberNAI);
        this.transactionAmount = transactionAmount;
    }

    public void prepareRequest() {
        request.setMethodName(CS3CP6Tokens.RefillMethodCallName.toString());
        request.addMember(new Member(CSTokens.OriginNodeType.toString(), getOriginNodeType()));
        request.addMember(new Member(CSTokens.OriginHostName.toString(), getOriginHostName()));
        request.addMember(new Member(CSTokens.OriginTransactionID.toString(), getOriginTransactionId()));
        request.addMember(new Member(CSTokens.OriginTimeStamp.toString(), getOriginTimestamp()));
        request.addMember(new Member(CSTokens.SubscriberNumberNAI.toString(), getSubscriberNumberNAI()));
        request.addMember(new Member(CSTokens.SubscriberNumber.toString(), getSubscriberNoPrefix() + getSubscriberNumber()));
        Float refillAmount = getDecimalDenominator() * getTransactionAmount();
        request.addMember(new Member(CS3CP6Tokens.TransactionAmount.toString(), String.valueOf(refillAmount.longValue())));
        request.addMember(new Member(CSTokens.TransactionCurrency.toString(), getTransactionCurrency()));
        if(externalData1 != null && externalData1.length() > 0)
        request.addMember(new Member(CSTokens.ExternalData1.toString(),getExternalData1()));
        if(externalData2 != null && externalData2.length() > 0)
        request.addMember(new Member(CSTokens.ExternalData2.toString(),getExternalData2()));
        request.addMember(new Member(CS3CP6Tokens.RequestRefillAccountBeforeFlag.toString(), new Boolean(true)));
        request.addMember(new Member(CS3CP6Tokens.RequestRefillAccountAfterFlag.toString(), new Boolean(true)));
        request.addMember(new Member(CS3CP6Tokens.RequestRefillDetailsFlag.toString(), new Boolean(true)));
        VoucherProfile profile = matchPaymentProfile(transactionAmount);
        if(profile == null) {
            logger.error("Voucher profile could not be matched from the voucher profiles defined in voucherprofiles.xml for transaction Amount: " + transactionAmount);
        } else {
            this.refillProfileID = profile.getProfileID();
            request.addMember(new Member(CS3CP6Tokens.RefillProfileID.toString(), getRefillProfileID()));
        }
    }

    public Float getTransactionAmount() {
        return transactionAmount;
    }

    public String getRefillProfileID() {
        return refillProfileID;
    }

    @Override
    public String getVoucherGroup() {
        VoucherProfile profile = VoucherProfilesLoader.instance().getVoucherProfiles().getByProfileID(refillProfileID);
        return profile.getProfileName();
    }
}
