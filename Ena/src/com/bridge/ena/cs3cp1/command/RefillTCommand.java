/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.cs3cp1.command;

import com.bridge.ena.cs.command.CSTokens;
import com.bridge.ena.vs.profiles.VoucherProfile;
import com.bridge.ena.vs.profiles.VoucherProfiles;
import com.bridge.ena.vs.profiles.VoucherProfilesLoader;
import com.bridge.ena.xmlrpc.serializer.Member;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author db2admin
 */
public class RefillTCommand extends AbstractCS3CP1Command {

    private static Log logger = LogFactory.getLog(RefillTCommand.class);
    private String originTransactionId;
    private Date originTimestamp;
    private String subscriberNumber;
    private Float transactionAmountRefill;
    private String paymentProfileID;
    private String externalData1;
    private String externalData2;
    private Float decimalDenominator;

    public RefillTCommand(String originTransactionId, Date originTimestamp, String subscriberNumber,
        Float transactionAmountRefill, String externalData1, String externalData2, String paymentProfileID,
        float decimalDenominator, String subscriberNoPrefix, Integer subscriberNumberNAI) {
        super(subscriberNoPrefix, subscriberNumberNAI);
        this.originTransactionId = originTransactionId;
        this.originTimestamp = originTimestamp;
        this.subscriberNumber = subscriberNumber;
        this.transactionAmountRefill = transactionAmountRefill;
        this.externalData1 = externalData1;
        this.externalData2 = externalData2;
        this.paymentProfileID = paymentProfileID;
        this.decimalDenominator = decimalDenominator;
    }

    public RefillTCommand(String originTransactionId, Date originTimestamp, String subscriberNumber,
        Float transactionAmountRefill, String externalData1, String externalData2, float decimalDenominator,
        String subscriberNoPrefix, Integer subscriberNumberNAI) {
        super(subscriberNoPrefix, subscriberNumberNAI);
        this.originTransactionId = originTransactionId;
        this.originTimestamp = originTimestamp;
        this.subscriberNumber = subscriberNumber;
        this.transactionAmountRefill = transactionAmountRefill;
        this.externalData1 = externalData1;
        this.externalData2 = externalData2;
        this.decimalDenominator = decimalDenominator;
    }

    public void prepareRequest() {
        request.setMethodName(CS3CP1Tokens.RefillMethodCallName.toString());
        request.addMember(new Member(CSTokens.OriginNodeType.toString(), getOriginNodeType()));
        request.addMember(new Member(CSTokens.OriginHostName.toString(), getOriginHostName()));
        request.addMember(new Member(CSTokens.OriginTransactionID.toString(), getOriginTransactionId()));
        request.addMember(new Member(CSTokens.OriginTimeStamp.toString(), getOriginTimestamp()));
        request.addMember(new Member(CSTokens.SubscriberNumberNAI.toString(), getSubscriberNumberNAI()));
        request.addMember(new Member(CSTokens.SubscriberNumber.toString(), getSubscriberNoPrefix() + getSubscriberNumber()));
        Float refillAmount = getDecimalDenominator() * getTransactionAmountRefill();
        request.addMember(new Member(CS3CP1Tokens.TransactionAmountRefill.toString(), String.valueOf(refillAmount.longValue())));
        request.addMember(new Member(CSTokens.TransactionCurrency.toString(), getTransactionCurrency()));
        request.addMember(new Member(CSTokens.ExternalData1.toString(),getExternalData1()));
        request.addMember(new Member(CSTokens.ExternalData2.toString(),getExternalData2()));
        if(getPaymentProfileID() != null) {
            request.addMember(new Member(CS3CP1Tokens.PaymentProfileID.toString(), getPaymentProfileID()));
        } else {
            VoucherProfile profile = matchPaymentProfile(refillAmount);
            if(profile == null) {
                logger.error("Voucher profile could not be matched from the voucher profiles defined in voucherprofiles.xml for transaction Amount: " + refillAmount);
            } else {
                this.paymentProfileID = profile.getProfileID();
                request.addMember(new Member(CS3CP1Tokens.PaymentProfileID.toString(), getPaymentProfileID()));
            }
        }
    }

    protected VoucherProfiles getVoucherProfiles() {
        return VoucherProfilesLoader.instance().getVoucherProfiles();
    }

    private VoucherProfile matchPaymentProfile(Float transactionAmount) {
        VoucherProfile profile = getVoucherProfiles().getByNominalValue(transactionAmount);
        return profile;
    }

    @Override
    public boolean supportsGetSubscriberInfo() {
        return false;
    }

    @Override
    public Float getDecimalDenominator() {
        return decimalDenominator;
    }

    public Float getTransactionAmountRefill() {
        return transactionAmountRefill;
    }

    public String getExternalData1() {
        return externalData1;
    }

    public String getExternalData2() {
        return externalData2;
    }

    public String getPaymentProfileID() {
        return paymentProfileID;
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
    
}
