package com.bridge.ena.cs3cp6.command;

import java.util.Date;

import com.bridge.ena.cs.command.CSTokens;
import com.bridge.ena.xmlrpc.serializer.Member;

public class GetOffersCommand extends AbstractCS3CP6Command {

	private String originTransactionId;
	private Date originTimestamp;
	private String subscriberNumber;
	private Float decimalDenominator;

	public GetOffersCommand(String originTransactionId, Date originTimestamp,
			String subscriberNumber, float decimalDenominator,
			String subscriberNoPrefix, Integer subscriberNumberNAI) {
		super(subscriberNoPrefix, subscriberNumberNAI);
		this.originTransactionId = originTransactionId;
		this.originTimestamp = originTimestamp;
		this.subscriberNumber = subscriberNumber;
		this.decimalDenominator = decimalDenominator;
	}

	public void prepareRequest() {
		request.setMethodName(CS3CP6Tokens.getOffersMethodCallName.toString());
		request.addMember(new Member(CSTokens.OriginNodeType.toString(),
				getOriginNodeType()));
		request.addMember(new Member(CSTokens.OriginHostName.toString(),
				getOriginHostName()));
		request.addMember(new Member(CSTokens.OriginTransactionID.toString(),
				getOriginTransactionId()));
		request.addMember(new Member(CSTokens.OriginTimeStamp.toString(),
				getOriginTimestamp()));
		request.addMember(new Member(CSTokens.SubscriberNumberNAI.toString(),
				getSubscriberNumberNAI()));
		request.addMember(new Member(CSTokens.SubscriberNumber.toString(),
				getSubscriberNoPrefix() + getSubscriberNumber()));
		request.addMember(new Member(CSTokens.RequestInactiveOffersFlag.toString(),
				new Boolean(true)));
		request.addMember(new Member(CSTokens.RequestDedicatedAccountDetailsFlag.toString(),
				new Boolean(true)));
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

	public Float getDecimalDenominator() {
		return decimalDenominator;
	}

	@Override
	public boolean supportsGetSubscriberInfo() {
		return true;
	}

}
