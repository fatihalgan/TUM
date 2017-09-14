package com.bridge.ena.vs.command;

import com.bridge.ena.xmlrpc.serializer.Member;

public class ChangeVoucherStateCommand extends AbstractVSCommand {

	private String serialNumberFisrt;
	private String serialNumberLast;
	private int newState;

	protected ChangeVoucherStateCommand(String serialNumberFirst,
			String serialNumberLast, int newState) {
		this.serialNumberFisrt = serialNumberFirst;
		this.serialNumberLast = serialNumberLast;
		this.newState = newState;
	}

	public void prepareRequest() {
		request.setMethodName(VSTokens.ChangeVoucherStateMethodCallName
				.toString());
		request.addMember(new Member(VSTokens.SerialNumberFirst.toString(),
				serialNumberFisrt));
		request.addMember(new Member(VSTokens.SerialNumberLast.toString(),
				serialNumberLast));
		request.addMember(new Member(VSTokens.NewState.toString(), newState));
	}

	public String getSerialNumberFisrt() {
		return serialNumberFisrt;
	}

	public String getSerialNumberLast() {
		return serialNumberLast;
	}

	public int getNewState() {
		return newState;
	}

	@Override
	public Float getDecimalDenominator() {
		throw new UnsupportedOperationException(
				"This command does not support decimal denominator");
	}
}
