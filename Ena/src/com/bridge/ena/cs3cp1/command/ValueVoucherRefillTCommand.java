/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.cs3cp1.command;

import com.bridge.ena.cs.command.CSTokens;
import com.bridge.ena.xmlrpc.serializer.Member;
import java.util.Date;

/**
 *
 * @author db2admin
 */
public class ValueVoucherRefillTCommand extends AbstractCS3CP1Command {

    private String originTransactionId;
    private Date originTimestamp;
    private String subscriberNumber;
    private String activationNumber;
    private String externalData1;
    private String externalData2;
    private Float decimalDenominator;

    protected ValueVoucherRefillTCommand(String originTransactionId, Date originTimestamp, String subscriberNumber,
        String activationNumber, String externalData1, String externalData2, float decimalDenominator,
        String subscriberNoPrefix, Integer subscriberNumberNAI) {
        super(subscriberNoPrefix, subscriberNumberNAI);
        this.originTransactionId = originTransactionId;
        this.originTimestamp = originTimestamp;
        this.subscriberNumber = subscriberNumber;
        this.activationNumber = activationNumber;
        this.externalData1 = externalData1;
        this.externalData2 = externalData2;
        this.decimalDenominator = decimalDenominator;
    }

    public void prepareRequest() {
        request.setMethodName(CS3CP1Tokens.ValueVoucherRefillMethodCallName.toString());
        request.addMember(new Member(CSTokens.OriginNodeType.toString(), getOriginNodeType()));
        request.addMember(new Member(CSTokens.OriginHostName.toString(), getOriginHostName()));
        request.addMember(new Member(CSTokens.OriginTransactionID.toString(), getOriginTransactionId()));
        request.addMember(new Member(CSTokens.OriginTimeStamp.toString(), getOriginTimestamp()));
        request.addMember(new Member(CSTokens.SubscriberNumberNAI.toString(), getSubscriberNumberNAI()));
        request.addMember(new Member(CSTokens.SubscriberNumber.toString(), getSubscriberNoPrefix() + getSubscriberNumber()));
        request.addMember(new Member(CS3CP1Tokens.ActivationNumber.toString(), getActivationNumber()));
        if((externalData1 != null) && (externalData1.trim().length() != 0))
        request.addMember(new Member(CSTokens.ExternalData1.toString(), getExternalData1()));
        if((externalData2 != null) && (externalData2.trim().length() != 0))
        request.addMember(new Member(CSTokens.ExternalData2.toString(), getExternalData2()));
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

    public String getExternalData1() {
        return externalData1;
    }

    public String getExternalData2() {
        return externalData2;
    }

    public String getActivationNumber() {
        return activationNumber;
    }

    @Override
    public boolean supportsGetSubscriberInfo() {
        return false;
    }

    @Override
    public Float getDecimalDenominator() {
        return decimalDenominator;
    }
}
