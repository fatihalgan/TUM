package com.bridge.ena.vs.command;

import com.bridge.ena.xmlrpc.serializer.Member;

public class EndReservationCommand extends AbstractVSCommand {

	private String activationCode;
	private String action;
	private String subscriberId;
	private String transactionId;
	
	public EndReservationCommand(String activationCode, Action action, String subscriberId, String transactionId) {
		this.activationCode = activationCode;
		this.action = action.toString();
		this.subscriberId = subscriberId;
		this.transactionId = transactionId;
	}
	
	public void prepareRequest() {
		request.setMethodName(VSTokens.EndReservationMethodCallName.toString());
		request.addMember(new Member(VSTokens.ActivationCode.toString(), activationCode));
		request.addMember(new Member(VSTokens.Action.toString(), action));
		request.addMember(new Member(VSTokens.SubscriberID.toString(), subscriberId));
		request.addMember(new Member(VSTokens.TransactionID.toString(), transactionId));
	}

	@Override
	public Float getDecimalDenominator() {
		throw new UnsupportedOperationException("This command does not support decimal denominator");
	}
}
