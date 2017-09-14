package com.bridge.ena.hxc.listener;

public enum HXCTokens {

	HandleUSSDRequestMethodCallName("handleUSSDRequest"),
	TransactionID("TransactionId"),
	TransactionTime("TransactionTime"),
	MSISDN("MSISDN"),
	USSDServiceCode("USSDServiceCode"),
	USSDRequestString("USSDRequestString"),
	USSDResponseString("USSDResponseString"),
	USSDEncoding("USSDEncoding"),
	Response("response"),
	Action("action");
	
	private final String text;

    private HXCTokens(String text) {
        this.text = text;
    }

    public String toString() {
        return text;
    }
	
}
