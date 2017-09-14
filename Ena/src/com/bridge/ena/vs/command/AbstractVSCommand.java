/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.vs.command;

import com.bridge.ena.vs.profiles.VoucherProfile;
import com.bridge.ena.vs.profiles.VoucherProfiles;
import com.bridge.ena.vs.profiles.VoucherProfilesLoader;
import com.bridge.ena.xmlrpc.command.AbstractCommand;
import com.bridge.ena.xmlrpc.serializer.MethodResponse;
import com.bridge.ena.xmlrpc.exception.XmlRpcConnectionException;
import com.bridge.ena.xmlrpc.serializer.Member;
import com.bridge.ena.xmlrpc.serializer.Serializer;
import com.bridge.ena.xml.BadXmlFormatException;
import com.bridge.ena.xmlrpc.command.XMLRPCTokens;
import com.bridge.ena.xmlrpc.serializer.Fault;
import java.util.Date;


/**
 *
 * @author db2admin
 */
public abstract class AbstractVSCommand extends AbstractCommand {

	private String originHostName = null;
    public abstract Float getDecimalDenominator();

    protected Float convertFromCents(String value, Float decimalDenominator) {
        Float realVal = Float.parseFloat(value);
        return (realVal / decimalDenominator);
    }

    @Override
    public MethodResponse execute() throws XmlRpcConnectionException {
        try {
            prepareRequest();
            String strResponse = getXMLRPCClient().sendMessage(request.toXML(new StringBuffer()));
            Serializer ser = new Serializer(strResponse);
            response = (MethodResponse)ser.parse();
        } catch(BadXmlFormatException e) {
            generateFaultResponse(e.getFaultCode(), e.getMessage());
        } catch(XmlRpcConnectionException xe) {
            httpStatusCode = xe.getStatusCode();
            throw xe;
        }
        return response;
    }
    
     protected void generateFaultResponse(Integer faultCode, String message) {
        Fault fault = new Fault();
        fault.getFault().addMember(new Member(XMLRPCTokens.FaultCode.toString(), faultCode));
        fault.getFault().addMember(new Member(XMLRPCTokens.FaultString.toString(), message));
        response.setFault(fault);
    }
    
    public boolean isErrorOrFaultResponse() {
        if(isFault()) return true;
        Member member = response.getMember(XMLRPCTokens.ResponseCode.toString());
        Integer responseCode = null;
        if(member != null) responseCode = (Integer)member.getValue();
        if(responseCode >= 10) return true;
        return false;
    }
    
    public boolean isFault() {
        if(response.getFault() != null) return true;
        else return false;
    }
    
    public String getSerialNumber() {
        Object value = response.getMembers().getValue(VSTokens.SerialNumber.toString());
        if(value != null) return ((String)value);
        else return null;
    }
    
    public String getActivationCode() {
        Object value = response.getMembers().getValue(VSTokens.ActivationCode.toString());
        if(value != null) return ((String)value);
        else return null;
    }
    
    public String getCurrency() {
        Object value = response.getMembers().getValue(VSTokens.Currency.toString());
        if(value != null) return ((String)value);
        else return null;
    }
    
    public String getValue() {
    	Object value = response.getMembers().getValue(VSTokens.Value.toString());
    	if(value != null) return ((String)value);
    	else return null;
    }
    
    public Integer getResponseCode() {
        Object value = response.getMembers().getValue(XMLRPCTokens.ResponseCode.toString());
        if(value != null) return ((Integer)value);
        else return null;
    }
    
    public String getVoucherGroup() {
        Object value = response.getMembers().getValue(VSTokens.VoucherGroup.toString());
        if(value != null) return ((String)value);
        else return null;
    }
    
    public String getAgent() {
        Object value = response.getMembers().getValue(VSTokens.Agent.toString());
        if(value != null) return ((String)value);
        else return null;
    }
    
    public String getBatchID() {
        Object value = response.getMembers().getValue(VSTokens.BatchID.toString());
        if(value != null) return ((String)value);
        else return null;
    }
    
    public Date getExpiryDate() {
        Object value = response.getMembers().getValue(VSTokens.ExpiryDate.toString());
        if(value != null) return ((Date)value);
        else return null;
    }
    
    public String getOperatorID() {
        Object value = response.getMembers().getValue(VSTokens.OperatorID.toString());
        if(value != null) return ((String)value);
        else return null;
    }
    
    public String getSubscriberNumber() {
        Object value = response.getMembers().getValue(VSTokens.SubscriberID.toString());
        if(value != null) return ((String)value);
        else return null;
    }
    
    public Date getTimeStamp() {
        Object value = response.getMembers().getValue(VSTokens.Timestamp.toString());
        if(value != null) return ((Date)value);
        else return null;
    }
    
    public VoucherState getVoucherState() {
        Object value = response.getMembers().getValue(VSTokens.State.toString());
        if(value != null) return VoucherState.values()[(Integer)value];
        else return null;
    }
    
    public Float getVoucherValue(Float decimalDenominator) {
        Object value = response.getMembers().getValue(VSTokens.Value.toString());
        if(value != null) return (convertFromCents((String)value, decimalDenominator));
        else return null;
    }
    
    public Integer getFaultCode(){
    	if(!isFault()) return null;
    	Object value = getResponse().getFault().getFault().getValue(XMLRPCTokens.FaultCode.toString());
    	 if(value != null) return (Integer)value;
         else return null;
    }
    
    public String getFaultString(){
    	if(!isFault()) return null;
    	Object value = getResponse().getFault().getFault().getValue(XMLRPCTokens.FaultString.toString());
    	 if(value != null) return (String)value;
         else return null;
    }

    public String getProfileID() {
        if(getVoucherGroup() == null) return null;
        VoucherProfiles profiles = VoucherProfilesLoader.instance().getVoucherProfiles();
        VoucherProfile profile = profiles.getByProfileName(getVoucherGroup());
        return profile.getProfileID();
    }

    public static String getProfileID(String voucherGroup) {
        VoucherProfiles profiles = VoucherProfilesLoader.instance().getVoucherProfiles();
        VoucherProfile profile = profiles.getByProfileName(voucherGroup);
        return profile.getProfileID();
    }

    public static Float getNominalValue(String voucherGroup) {
        VoucherProfiles profiles = VoucherProfilesLoader.instance().getVoucherProfiles();
        VoucherProfile profile = profiles.getByProfileName(voucherGroup);
        return profile.getMainAccountValue();
    }
    
    public String getOriginHostName() {
        return originHostName;
    }
    
    public void setOriginHostName(String originHostName) {
        this.originHostName = originHostName;
    }
}
