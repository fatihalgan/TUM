/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.cs3cp6.command;

import com.bridge.ena.cs.command.CSTokens;
import java.util.Date;
import com.bridge.ena.xmlrpc.serializer.Member;

/**
 *
 * @author db2admin
 */
public class UpdateServiceClassCommand extends AbstractCS3CP6Command {

    private String originTransactionId;
    private Date originTimestamp;
    private String subscriberNumber;
    private Float decimalDenominator;
    private Integer serviceClassNew;

    UpdateServiceClassCommand(String originTransactionId, Date originTimestamp, String subscriberNumber,
        Integer serviceClassNew, String subscriberNoPrefix, Integer subscriberNumberNAI) {
        super(subscriberNoPrefix, subscriberNumberNAI);
        this.originTransactionId = originTransactionId;
        this.originTimestamp = originTimestamp;
        this.subscriberNumber = subscriberNumber;
        this.serviceClassNew = serviceClassNew;
    }

    @Override
    public void prepareRequest() {
        request.setMethodName(CS3CP6Tokens.UpdateServiceClassMethodCallName.toString());
        request.addMember(new Member(CSTokens.OriginNodeType.toString(), getOriginNodeType()));
        request.addMember(new Member(CSTokens.OriginHostName.toString(), getOriginHostName()));
        request.addMember(new Member(CSTokens.OriginTransactionID.toString(),getOriginTransactionId()));
        request.addMember(new Member(CSTokens.OriginTimeStamp.toString(),getOriginTimestamp()));
        request.addMember(new Member(CSTokens.SubscriberNumberNAI.toString(), getSubscriberNumberNAI()));
        request.addMember(new Member(CSTokens.SubscriberNumber.toString(),getSubscriberNoPrefix() + getSubscriberNumber()));
        request.addMember(new Member(CSTokens.ServiceClassNew.toString(),getServiceClassNew()));
        request.addMember(new Member(CS3CP6Tokens.ServiceClassAction.toString(), CS3CP6Tokens.ServiceClassActionSetOriginal.toString()));
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
        return decimalDenominator;
    }

    @Override
    public boolean supportsGetSubscriberInfo() {
        return true;
    }

	public Integer getServiceClassNew() {
		return serviceClassNew;
	}
}
