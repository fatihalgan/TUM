/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.cs3cp1.command;

import com.bridge.ena.cs.command.CSTokens;
import com.bridge.ena.cs3cp1.adjustment.policy.DedicatedAccountAdjustments;
import java.util.Date;
import com.bridge.ena.xmlrpc.serializer.Member;
import com.bridge.ena.xmlrpc.serializer.Array;
import com.bridge.ena.xmlrpc.serializer.Struct;

/**
 *
 * @author db2admin
 */
public class AdjustmentTCommand extends AbstractCS3CP1Command {

    private String originTransactionId;
    private Date originTimestamp;
    private String subscriberNumber;
    private Float adjustmentAmount;
    private String externalData1;
    private String externalData2;
    private Integer supervisionFeeIncrease;
    private Integer serviceFeeIncrease;
    private Integer freeSMS;
    private Integer freeMMS;
    private DedicatedAccountAdjustments dedicatedAccountAdjustments;
    private Float decimalDenominator;
        
    AdjustmentTCommand(String originTransactionId, Date originTimestamp, String subscriberNumber,
        Float adjustmentAmount, String externalData1, String externalData2, int supervisionFeeIncrease,
        int serviceFeeIncrease,  int freeSMS, int freeMMS, DedicatedAccountAdjustments dedicatedAccountAdjustments, float decimalDenominator,
        String subscriberNoPrefix, Integer subscriberNumberNAI) {
        super(subscriberNoPrefix, subscriberNumberNAI);
        this.originTransactionId = originTransactionId;
        this.originTimestamp = originTimestamp;
        this.subscriberNumber = subscriberNumber;
        this.adjustmentAmount = adjustmentAmount;
        this.externalData1 = externalData1;
        this.externalData2 = externalData2;
        this.supervisionFeeIncrease = supervisionFeeIncrease;
        this.serviceFeeIncrease = serviceFeeIncrease;
        this.freeSMS = freeSMS;
        this.freeMMS = freeMMS;
        this.dedicatedAccountAdjustments = dedicatedAccountAdjustments;
        this.decimalDenominator = decimalDenominator;
    }
    
    
    public void prepareRequest() {
        request.setMethodName(CS3CP1Tokens.AdjustmentMethodCallName.toString());
        request.addMember(new Member(CSTokens.OriginNodeType.toString(), getOriginNodeType()));
        request.addMember(new Member(CSTokens.OriginHostName.toString(), getOriginHostName()));
        request.addMember(new Member(CSTokens.OriginTransactionID.toString(),getOriginTransactionId()));
        request.addMember(new Member(CSTokens.OriginTimeStamp.toString(),getOriginTimestamp()));
        request.addMember(new Member(CSTokens.SubscriberNumberNAI.toString(), getSubscriberNumberNAI()));
        request.addMember(new Member(CSTokens.SubscriberNumber.toString(), getSubscriberNoPrefix() + getSubscriberNumber()));
        Float adjustment = getDecimalDenominator() * getAdjustmentAmount();
        request.addMember(new Member(CS3CP1Tokens.AdjustmentAmount.toString(), String.valueOf(adjustment.longValue())));
        request.addMember(new Member(CSTokens.TransactionCurrency.toString(), getTransactionCurrency()));
        request.addMember(new Member(CSTokens.ExternalData1.toString(),getExternalData1()));
        request.addMember(new Member(CSTokens.ExternalData2.toString(),getExternalData2()));
        request.addMember(new Member(CS3CP1Tokens.RelativeDateAdjustmentSupervision.toString(),getSupervisionFeeIncrease()));
        request.addMember(new Member(CS3CP1Tokens.RelativeDateAdjustmentServiceFee.toString(),getServiceFeeIncrease()));
        Integer sms = (int)(getFreeSMS() * getDecimalDenominator());
        Integer mms = (int)(getFreeMMS() * getDecimalDenominator());
        Array dedicatedAccount = new Array();
        Struct da1 = new Struct();
        da1.addMember(new Member(CSTokens.DedicatedAccountID.toString(), 1));
        da1.addMember(new Member(CS3CP1Tokens.AdjustmentAmount.toString(), sms.toString()));
        dedicatedAccount.addStruct(da1);
        Struct da2 = new Struct();
        da2.addMember(new Member(CSTokens.DedicatedAccountID.toString(), 2));
        da2.addMember(new Member(CS3CP1Tokens.AdjustmentAmount.toString(), mms.toString()));
        dedicatedAccount.addStruct(da2);
        applyDedicatedAccountAdjustments(dedicatedAccount);
        request.addMember(new Member(CSTokens.DedicatedAccountInformation.toString(), dedicatedAccount));
    }
    
    public void applyDedicatedAccountAdjustments(Array dedicatedAccount) {
        if(dedicatedAccountAdjustments != null)
            dedicatedAccountAdjustments.applyAdjustments(dedicatedAccount);
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

    public Float getAdjustmentAmount() {
        return adjustmentAmount;
    }

    public String getExternalData1() {
        return externalData1;
    }

    public String getExternalData2() {
        return externalData2;
    }

    public Integer getSupervisionFeeIncrease() {
        return supervisionFeeIncrease;
    }

    public Integer getServiceFeeIncrease() {
        return serviceFeeIncrease;
    }

    public Integer getFreeSMS() {
        return freeSMS;
    }

    public Integer getFreeMMS() {
        return freeMMS;
    }

    public Float getDecimalDenominator() {
        return decimalDenominator;
    }
    
    @Override
    public boolean supportsGetSubscriberInfo() {
        return false;
    }
}
