package com.bridge.ena.vs.command;

import com.bridge.ena.xmlrpc.serializer.Member;

public class GetVoucherSerialCommand extends AbstractVSCommand {
	
	private String activationCode;
	private float decimalDenominator;
	
	public GetVoucherSerialCommand(String activationCode, float decimalDenominator) {
		this.activationCode = activationCode;
		this.decimalDenominator = decimalDenominator;
	}
	
	public void prepareRequest() {
		request.setMethodName(VSTokens.GetVoucherDetailsMethodCallName.toString());
	    request.addMember(new Member(VSTokens.ActivationCode.toString(), activationCode));
	}

	public String getActivationCode() {
		return activationCode;
	}

	public Float getDecimalDenominator() {
		return decimalDenominator;
	}
	
	

}
