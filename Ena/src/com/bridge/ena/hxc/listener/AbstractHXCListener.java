package com.bridge.ena.hxc.listener;

import java.util.Date;

import org.apache.http.HttpStatus;

import com.bridge.ena.xml.BadXmlFormatException;
import com.bridge.ena.xmlrpc.command.XMLRPCTokens;
import com.bridge.ena.xmlrpc.listener.AbstractListener;
import com.bridge.ena.xmlrpc.serializer.Fault;
import com.bridge.ena.xmlrpc.serializer.Member;
import com.bridge.ena.xmlrpc.serializer.MethodCall;
import com.bridge.ena.xmlrpc.serializer.MethodResponse;
import com.bridge.ena.xmlrpc.serializer.Serializer;

public abstract class AbstractHXCListener extends AbstractListener {

	public MethodResponse handleRequest(String strRequest) throws BadXmlFormatException {
		try {
			Serializer ser = new Serializer(strRequest);
			request = (MethodCall)ser.parse();
			processRequest();
		} catch(BadXmlFormatException e) {
			httpStatusCode = HttpStatus.SC_BAD_REQUEST;
			throw e;
		}
		return response;
	}
	
	public abstract void processRequest();
	
	protected void generateFaultResponse(Integer faultCode, String message, String transactionID, Date transactionTime) {
        Fault fault = new Fault();
        fault.getFault().addMember(new Member(XMLRPCTokens.FaultCode.toString(), faultCode));
        fault.getFault().addMember(new Member(XMLRPCTokens.FaultString.toString(), message));
        fault.getFault().addMember(new Member(HXCTokens.TransactionID.toString(), transactionID));
        fault.getFault().addMember(new Member(HXCTokens.TransactionTime.toString(), transactionTime));
        response.setFault(fault);
    }
	
	public boolean isErrorOrFaultResponse() {
        if(isFault()) return true;
        Member member = response.getMember(XMLRPCTokens.ResponseCode.toString());
        Integer responseCode = null;
        if(member != null) responseCode = (Integer)member.getValue();
        if(responseCode != 0) return true;
        return false;
    }

    public boolean isFault() {
        if(response.getFault() != null) return true;
        else return false;
    }

    public String getTransactionID() {
    	Object value = request.getMembers().getValue(HXCTokens.TransactionID.toString());
        if(value != null) return (String)value;
        else return null;
    }
    
    public Date getTransactionTime() {
    	Object value = request.getMembers().getValue(HXCTokens.TransactionTime.toString());
        if(value != null) return (Date)value;
        else return null;
    }
    
    public String getMSISDN() {
    	Object value = request.getMembers().getValue(HXCTokens.MSISDN.toString());
        if(value != null) return (String)value;
        else return null;
    }
    
    public String getUSSDServiceCode() {
    	Object value = request.getMembers().getValue(HXCTokens.USSDServiceCode.toString());
        if(value != null) return (String)value;
        else return null;
    }
    
    public String getUSSDRequestString() {
    	Object value = request.getMembers().getValue(HXCTokens.USSDRequestString.toString());
        if(value != null) return (String)value;
        else return null;
    }
    
    public String getUSSDEncoding() {
    	Object value = request.getMembers().getValue(HXCTokens.USSDEncoding.toString());
        if(value != null) return (String)value;
        else return null;
    }
    
    public String getUSSDResponse() {
    	Object value = request.getMembers().getValue(HXCTokens.Response.toString());
        if(value != null) return (String)value;
        else return null;
    }

}
