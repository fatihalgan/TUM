package com.bridge.ena.cs3cp6.command;

import java.util.Date;
import java.util.List;

import com.bridge.ena.cs.command.CSTokens;
import com.bridge.ena.cs3cp6.value.DedicatedAccountInformation;
import com.bridge.ena.xmlrpc.serializer.Array;
import com.bridge.ena.xmlrpc.serializer.Member;
import com.bridge.ena.xmlrpc.serializer.Struct;

public class GenericUpdateBalanceAndDateCommand extends AbstractCS3CP6Command {

	private String originTransactionId;
	private Date originTimestamp;
	private String subscriberNumber;
	private Float adjustmentAmount;
	private String externalData1;
	private String externalData2;
	private Integer supervisionExpiryDateRelative;
	private Integer serviceFeeExpiryDateRelative;
	private Float decimalDenominator;
	private List<DedicatedAccountInformation> dedicatedAccountAdjustments;

	GenericUpdateBalanceAndDateCommand(String originTransactionId, Date originTimestamp, String subscriberNumber,
		Float adjustmentAmount, String externalData1, String externalData2,
		int supervisionExpiryDateRelative, int serviceFeeExpiryDateRelative, 
		float decimalDenominator, List<DedicatedAccountInformation> dedicatedAccountAdjustments,
		String subscriberNoPrefix, Integer subscriberNumberNAI) {
		super(subscriberNoPrefix, subscriberNumberNAI);
		this.originTransactionId = originTransactionId;
		this.originTimestamp = originTimestamp;
		this.subscriberNumber = subscriberNumber;
		this.adjustmentAmount = adjustmentAmount;
		this.externalData1 = externalData1;
		this.externalData2 = externalData2;
		this.supervisionExpiryDateRelative = supervisionExpiryDateRelative;
		this.serviceFeeExpiryDateRelative = serviceFeeExpiryDateRelative;
		this.dedicatedAccountAdjustments = dedicatedAccountAdjustments;
		this.decimalDenominator = decimalDenominator;
	}

	public void prepareRequest() {
		request.setMethodName(CS3CP6Tokens.UpdateBalanceAndDateMethodCallName.toString());
		request.addMember(new Member(CSTokens.OriginNodeType.toString(), getOriginNodeType()));
		request.addMember(new Member(CSTokens.OriginHostName.toString(), getOriginHostName()));
		request.addMember(new Member(CSTokens.OriginTransactionID.toString(), getOriginTransactionId()));
		request.addMember(new Member(CSTokens.OriginTimeStamp.toString(), getOriginTimestamp()));
		request.addMember(new Member(CSTokens.SubscriberNumberNAI.toString(), getSubscriberNumberNAI()));
		request.addMember(new Member(CSTokens.SubscriberNumber.toString(), getSubscriberNoPrefix() + getSubscriberNumber()));
		Float adjustment = getDecimalDenominator() * getAdjustmentAmount();
		request.addMember(new Member(CS3CP6Tokens.AdjustmentAmountRelative.toString(), String.valueOf(adjustment.longValue())));
		request.addMember(new Member(CSTokens.TransactionCurrency.toString(),getTransactionCurrency()));
		if (externalData1 != null && externalData1.length() > 0)
			request.addMember(new Member(CSTokens.ExternalData1.toString(), getExternalData1()));
		if (externalData2 != null && externalData2.length() > 0)
			request.addMember(new Member(CSTokens.ExternalData2.toString(), getExternalData2()));
		if (supervisionExpiryDateRelative != 0)
			request.addMember(new Member(CS3CP6Tokens.SupervisionExpiryDateRelative.toString(), getSupervisionExpiryDateRelative()));
		if (serviceFeeExpiryDateRelative != 0)request.addMember(new Member(CS3CP6Tokens.ServiceFeeExpiryDateRelative.toString(), getServiceFeeExpiryDateRelative()));
		Array dedicatedAccountUpdateInfo = new Array();
		for(DedicatedAccountInformation da : dedicatedAccountAdjustments) {
			Struct das = new Struct();
			das.addMember(new Member(CSTokens.DedicatedAccountID.toString(), da.getDedicatedAccountId()));
			Float daAdjustment = getDecimalDenominator() * da.getDedicatedAccountValue();
			das.addMember(new Member(CS3CP6Tokens.AdjustmentAmountRelative.toString(), String.valueOf(daAdjustment.intValue())));
			if(da.getExpiryDate() != null) das.addMember(new Member(CSTokens.ExpiryDate.toString(), da.getExpiryDate()));
			dedicatedAccountUpdateInfo.addStruct(das);
		}
		request.addMember(new Member(CS3CP6Tokens.DedicatedAccountUpdateInformation.toString(), dedicatedAccountUpdateInfo));
		
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

	public Integer getSupervisionExpiryDateRelative() {
		return supervisionExpiryDateRelative;
	}

	public Integer getServiceFeeExpiryDateRelative() {
		return serviceFeeExpiryDateRelative;
	}

	public Float getDecimalDenominator() {
		return decimalDenominator;
	}

	@Override
	public boolean supportsGetSubscriberInfo() {
		return false;
	}

	public List<DedicatedAccountInformation> getDedicatedAccountAdjustments() {
		return dedicatedAccountAdjustments;
	}

}
