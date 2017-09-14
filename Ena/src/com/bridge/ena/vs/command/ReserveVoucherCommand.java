package com.bridge.ena.vs.command;

import com.bridge.ena.xmlrpc.serializer.Member;

public class ReserveVoucherCommand extends AbstractVSCommand {

	private String activationCode;
	private String subscriberId;
	private String transactionId;
	private float decimalDenominator;
	
	ReserveVoucherCommand(String activationCode, String subscriberId, String transactionId, float decimalDenominator) {
		this.activationCode = activationCode;
		this.subscriberId = subscriberId;
		this.transactionId = transactionId;
		this.decimalDenominator = decimalDenominator;
	}
	
	public void prepareRequest() {
		request.setMethodName(VSTokens.ReserveVoucherMethodCallName.toString());
		request.addMember(new Member(VSTokens.ActivationCode.toString(), activationCode));
		request.addMember(new Member(VSTokens.SubscriberID.toString(), subscriberId));
		request.addMember(new Member(VSTokens.TransactionID.toString(), transactionId));
	}

	@Override
	public Float getDecimalDenominator() {
		return decimalDenominator;
	}

	
}
