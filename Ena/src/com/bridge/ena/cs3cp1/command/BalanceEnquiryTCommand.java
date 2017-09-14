/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.cs3cp1.command;

import com.bridge.ena.cs.command.CSTokens;
import java.util.Date;
import com.bridge.ena.xmlrpc.serializer.Member;

/**
 *
 * @author db2admin
 */
public class BalanceEnquiryTCommand extends AbstractCS3CP1Command {

    private String subscriberNumber;
    private Date originTimestamp;
    private String originTransactionID;
    private Float decimalDenominator;
        
    BalanceEnquiryTCommand(String subscriberNumber, Date originTimestamp, String originTransactionID,
            float decimalDenominator, String subscriberNoPrefix, Integer subscriberNumberNAI) {
        super(subscriberNoPrefix, subscriberNumberNAI);
        this.subscriberNumber = subscriberNumber;
        this.originTimestamp = originTimestamp;
        this.originTransactionID = originTransactionID;
        this.decimalDenominator = decimalDenominator;
    }
    
    public void prepareRequest() {
        request.setMethodName(CS3CP1Tokens.BalanceEnquiryMethodCallName.toString());
        request.addMember(new Member(CSTokens.OriginNodeType.toString(), getOriginNodeType()));
        request.addMember(new Member(CSTokens.OriginHostName.toString(), getOriginHostName()));
        request.addMember(new Member(CSTokens.OriginTransactionID.toString(),getOriginTransactionID()));
        request.addMember(new Member(CSTokens.OriginTimeStamp.toString(),getOriginTimestamp()));
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

    @Override
    public Float getDecimalDenominator() {
        return decimalDenominator;
    }

    @Override
    public boolean supportsGetSubscriberInfo() {
        return true;
    }
    
}
