package com.bridge.ena.cs3cp6.command;

import java.util.Date;

import com.bridge.ena.cs.command.CSTokens;
import com.bridge.ena.xmlrpc.serializer.Array;
import com.bridge.ena.xmlrpc.serializer.Member;
import com.bridge.ena.xmlrpc.serializer.Struct;

public class UpdateMainAndDedicatedAccountBalanceCommand extends AbstractCS3CP6Command {

	private String originTransacitonId;
	private Date originTimestamp;
	private String subscriberNumber;
    private Float mainAccountAdjustmentAmount;
    private Float dedicatedAccountAdjustmentAmount;
    private Integer dedicatedAccountID;
    private String externalData1;
    private String externalData2;
    private Integer supervisionExpiryDateRelative;
    private Integer serviceFeeExpiryDateRelative;
    private float decimalDenominator;
    
    public UpdateMainAndDedicatedAccountBalanceCommand(String originTransactionId, Date originTimestamp, String subscriberNumber,
    	Float mainAccountAdjustmentAmount, Float dedicatedAccountAdjustmentAmount, Integer dedicatedAccountId,
    	String externalData1, String externalData2, Integer supervisionExpiryDateRelative, Integer serviceFeeExpiryDateRelative,
    	float decimalDenominator, String subscriberNoPrefix, Integer subscriberNumberNAI) {
		super(subscriberNoPrefix, subscriberNumberNAI);
    	this.originTransacitonId = originTransactionId;
    	this.originTimestamp = originTimestamp;
    	this.subscriberNumber = subscriberNumber;
    	this.mainAccountAdjustmentAmount = mainAccountAdjustmentAmount;
    	this.dedicatedAccountAdjustmentAmount = dedicatedAccountAdjustmentAmount;
    	this.dedicatedAccountID = dedicatedAccountId;
    	this.externalData1 = externalData1;
    	this.externalData2 = externalData2;
    	this.supervisionExpiryDateRelative = supervisionExpiryDateRelative;
    	this.serviceFeeExpiryDateRelative = serviceFeeExpiryDateRelative;
    	this.decimalDenominator = decimalDenominator;
	}
    
    @Override
	public Float getDecimalDenominator() {
		return decimalDenominator;
	}

	@Override
	public boolean supportsGetSubscriberInfo() {
		return false;
	}

	@Override
	public void prepareRequest() {
		request.setMethodName(CS3CP6Tokens.UpdateBalanceAndDateMethodCallName.toString());
		request.addMember(new Member(CSTokens.OriginNodeType.toString(), getOriginNodeType()));
		request.addMember(new Member(CSTokens.OriginHostName.toString(), getOriginHostName()));
		request.addMember(new Member(CSTokens.OriginTransactionID.toString(), getOriginTransacitonId()));
		request.addMember(new Member(CSTokens.OriginTimeStamp.toString(), getOriginTimestamp()));
		request.addMember(new Member(CSTokens.SubscriberNumberNAI.toString(), getSubscriberNumberNAI()));
		request.addMember(new Member(CSTokens.SubscriberNumber.toString(), getSubscriberNoPrefix() + getSubscriberNumber()));
		Float adjustment = getDecimalDenominator() * getMainAccountAdjustmentAmount();
        request.addMember(new Member(CS3CP6Tokens.AdjustmentAmountRelative.toString(), String.valueOf(adjustment.longValue())));
		request.addMember(new Member(CSTokens.TransactionCurrency.toString(), getTransactionCurrency()));
		if(externalData1 != null && externalData1.length() > 0)
            request.addMember(new Member(CSTokens.ExternalData1.toString(),getExternalData1()));
        if(externalData2 != null && externalData2.length() > 0)
            request.addMember(new Member(CSTokens.ExternalData2.toString(),getExternalData2()));
        if(supervisionExpiryDateRelative != 0)
            request.addMember(new Member(CS3CP6Tokens.SupervisionExpiryDateRelative.toString(),getSupervisionExpiryDateRelative()));
        if(serviceFeeExpiryDateRelative != 0)
            request.addMember(new Member(CS3CP6Tokens.ServiceFeeExpiryDateRelative.toString(),getServiceFeeExpiryDateRelative()));
        adjustment = (getDecimalDenominator() * getDedicatedAccountAdjustmentAmount());
        Array dedicatedAccount = new Array();
        Struct da1 = new Struct();
        da1.addMember(new Member(CSTokens.DedicatedAccountID.toString(), dedicatedAccountID));
        da1.addMember(new Member(CS3CP6Tokens.AdjustmentAmountRelative.toString(), String.valueOf(adjustment.longValue())));
        dedicatedAccount.addStruct(da1);
        //if dedicated account adjustment amount is 0 do not include it in the message
        if(getDedicatedAccountAdjustmentAmount().floatValue() != 0)
        	request.addMember(new Member(CS3CP6Tokens.DedicatedAccountUpdateInformation.toString(), dedicatedAccount));
	}

	public String getOriginTransacitonId() {
		return originTransacitonId;
	}

	public Date getOriginTimestamp() {
		return originTimestamp;
	}

	public String getSubscriberNumber() {
		return subscriberNumber;
	}

	public Float getMainAccountAdjustmentAmount() {
		return mainAccountAdjustmentAmount;
	}

	public Float getDedicatedAccountAdjustmentAmount() {
		return dedicatedAccountAdjustmentAmount;
	}

	public Integer getDedicatedAccountID() {
		return dedicatedAccountID;
	}

	public String getExternalData1() {
		return externalData1;
	}

	public String getExternalData2() {
		return externalData2;
	}

	public Integer getSupervisionExpiryDateRelative() {
		return supervisionExpiryDateRelative;
	}

	public Integer getServiceFeeExpiryDateRelative() {
		return serviceFeeExpiryDateRelative;
	}

}
