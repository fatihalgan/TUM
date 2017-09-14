/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.cs3cp6.command;

import com.bridge.ena.cs.command.CSTokens;
import com.bridge.ena.cs3cp6.value.FAFAction;
import java.util.Date;
import java.util.List;
import com.bridge.ena.xmlrpc.serializer.Member;
import com.bridge.ena.xmlrpc.serializer.Array;
import com.bridge.ena.xmlrpc.serializer.Struct;

/**
 *
 * @author db2admin
 */
public class UpdateFaFListCommand extends AbstractCS3CP6Command {

    private String originTransactionId;
    private Date originTimestamp;
    private String subscriberNumber;
    private String fafNumber;
    private FAFAction fafAction;

    UpdateFaFListCommand(String originTransactionId, Date originTimestamp, String subscriberNumber,
            String fafNumber, FAFAction fafAction, String subscriberNoPrefix, Integer subscriberNumberNAI) {
        super(subscriberNoPrefix, subscriberNumberNAI);
        this.originTransactionId = originTransactionId;
        this.originTimestamp = originTimestamp;
        this.subscriberNumber = subscriberNumber;
        this.fafNumber = fafNumber;
        this.fafAction = fafAction;
    }

    @Override
    public void prepareRequest() {
        request.setMethodName(CS3CP6Tokens.UpdateFaFListMethodCallName.toString());
        request.addMember(new Member(CSTokens.OriginNodeType.toString(), getOriginNodeType()));
        request.addMember(new Member(CSTokens.OriginHostName.toString(), getOriginHostName()));
        request.addMember(new Member(CSTokens.OriginTransactionID.toString(),getOriginTransactionId()));
        request.addMember(new Member(CSTokens.OriginTimeStamp.toString(),getOriginTimestamp()));
        request.addMember(new Member(CSTokens.SubscriberNumberNAI.toString(), getSubscriberNumberNAI()));
        request.addMember(new Member(CSTokens.SubscriberNumber.toString(),getSubscriberNoPrefix() + getSubscriberNumber()));
        request.addMember(new Member(CSTokens.FafAction.toString(), fafAction.toString()));
        Struct fafInfoStruct =  new Struct();
        fafInfoStruct.addMember(new Member(CSTokens.FafNumber.toString(),fafNumber));
        fafInfoStruct.addMember(new Member(CSTokens.FafIndicator.toString(),fafNumber.startsWith(getSubscriberNoPrefix())?1:2));
        fafInfoStruct.addMember(new Member(CSTokens.FafOwner.toString(), CS3CP6Tokens.FafOwnerSubscriber.toString()));
        request.addMember(new Member(CSTokens.FafInformation.toString(), fafInfoStruct));
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
