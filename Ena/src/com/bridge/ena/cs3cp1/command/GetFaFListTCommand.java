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
public class GetFaFListTCommand extends AbstractCS3CP1Command {

    private String originTransactionId;
    private Date originTimestamp;
    private String subscriberNumber;
                    
    GetFaFListTCommand(String originTransactionId, Date originTimestamp, String subscriberNumber,
        String subscriberNoPrefix, Integer subscriberNumberNAI) {
        super(subscriberNoPrefix, subscriberNumberNAI);
        this.originTransactionId = originTransactionId;
        this.originTimestamp = originTimestamp;
        this.subscriberNumber = subscriberNumber;
    }
    
    @Override
    public void prepareRequest() {
        request.setMethodName(CS3CP1Tokens.GetFaFListTMethodCallName.toString());
        request.addMember(new Member(CSTokens.OriginNodeType.toString(), getOriginNodeType()));
        request.addMember(new Member(CSTokens.OriginHostName.toString(), getOriginHostName()));
        request.addMember(new Member(CSTokens.OriginTransactionID.toString(),getOriginTransactionId()));
        request.addMember(new Member(CSTokens.OriginTimeStamp.toString(),getOriginTimestamp()));
        request.addMember(new Member(CSTokens.SubscriberNumberNAI.toString(), getSubscriberNumberNAI()));
        request.addMember(new Member(CSTokens.SubscriberNumber.toString(),getSubscriberNoPrefix() + getSubscriberNumber()));
        request.addMember(new Member(CSTokens.RequestedOwner.toString(), 1));
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

    @Override
    public Float getDecimalDenominator() {
        throw new UnsupportedOperationException("Operation not supported");
    }

    @Override
    public boolean supportsGetSubscriberInfo() {
        return true;
    }
}
