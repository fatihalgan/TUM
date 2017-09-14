/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.cs.command;

import com.bridge.ena.cs.value.FAFNumber;
import com.bridge.ena.xml.BadXmlFormatException;
import com.bridge.ena.xmlrpc.command.AbstractCommand;
import com.bridge.ena.xmlrpc.exception.XmlRpcConnectionException;
import com.bridge.ena.xmlrpc.serializer.MethodResponse;
import com.bridge.ena.xmlrpc.serializer.Serializer;
import com.bridge.ena.xmlrpc.serializer.Fault;
import com.bridge.ena.xmlrpc.serializer.Member;
import com.bridge.ena.xmlrpc.serializer.Struct;
import com.bridge.ena.xmlrpc.command.XMLRPCTokens;
import com.bridge.ena.cs.value.SubscriberInfo;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpStatus;

/**
 *
 * @author db2admin
 */
public abstract class AbstractCSCommand extends AbstractCommand {

    private static final String ORIGIN_NODE_TYPE = "EXT";
    private static final String TRANSACTION_CURRENCY = "MZM";

    public static final int SMS_DEDICATED_ACCOUNT_ID = 1;
    public static final int MMS_DEDICATED_ACCOUNT_ID = 2;
    public static final int NETMOVEL_BUNDLE_DEDICATED_ACCOUNT_ID = 8;
    public static final int FREE_CALLS_DEDICATED_ACCOUNT_ID = 4;
    
    public static final Integer NETMOVEL_BUNDLE_SERVICE_CLASS = 34;
    

    private String originHostName = null;
    private String subscriberNoPrefix;
    private Integer subscriberNumberNAI;

    public abstract Float getDecimalDenominator();

    public AbstractCSCommand(String subscriberNoPrefix, Integer subscriberNumberNAI) {
        super();
        this.subscriberNoPrefix = subscriberNoPrefix;
        this.subscriberNumberNAI = subscriberNumberNAI;
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

    public void generateFaultResponse(Integer faultCode, String message) {
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
        if(responseCode >= 100) return true;
        return false;
    }

    public boolean isFault() {
        if(response.getFault() != null) return true;
        else return false;
    }

    public String getOriginNodeType() {
        return ORIGIN_NODE_TYPE;
    }

    public String getTransactionCurrency() {
        return TRANSACTION_CURRENCY;
    }

    public String getSubscriberNoPrefix() {
        return subscriberNoPrefix;
    }

    public Integer getSubscriberNumberNAI() {
        return subscriberNumberNAI;
    }

    public abstract boolean supportsGetSubscriberInfo();

    public abstract SubscriberInfo getSubscriberInfo();

    public String getOriginTransactionID() {
        Object value = response.getMembers().getValue(CSTokens.OriginTransactionID.toString());
        if(value != null) return(String)value;
        else return null;
    }

    public Date getOriginTimeStamp() {
        Object value = response.getMembers().getValue(CSTokens.OriginTimeStamp.toString());
        if(value != null) return(Date)value;
        else return null;
    }

    public Integer getFreeSMS(Float decimalDenominator) {
        Object value = response.getMembers().getValue(CSTokens.DedicatedAccountInformation + "." + CSTokens.DedicatedAccountID, SMS_DEDICATED_ACCOUNT_ID);
        Struct dedicatedAccount = null;
        if(value != null) dedicatedAccount = (Struct)value;
        else return null;
        value = dedicatedAccount.getValue(CSTokens.DedicatedAccountValue1.toString());
        if(value != null) {
            return convertFromCents((String)value, decimalDenominator).intValue();
        }
        else return null;
    }

    public Date getFreeSMSExpiryDate(Float decimalDenominator) {
        Object value = response.getMembers().getValue(CSTokens.DedicatedAccountInformation + "." + CSTokens.DedicatedAccountID, SMS_DEDICATED_ACCOUNT_ID);
        Struct dedicatedAccount = null;
        if(value != null) dedicatedAccount = (Struct)value;
        else return null;
        value = dedicatedAccount.getValue(CSTokens.ExpiryDate.toString());
        if(value != null) {
            return chopHoursOfDate((Date)value);
        }
        else return null;
    }

    public Integer getFreeMMS(Float decimalDenominator) {
        Object value = response.getMembers().getValue(CSTokens.DedicatedAccountInformation + "." + CSTokens.DedicatedAccountID, MMS_DEDICATED_ACCOUNT_ID);
        Struct dedicatedAccount = null;
        if(value != null) dedicatedAccount = (Struct)value;
        else return null;
        value = dedicatedAccount.getValue(CSTokens.DedicatedAccountValue1.toString());
        if(value != null) {
            return convertFromCents((String)value, decimalDenominator).intValue();
        }
        else return null;
    }

    public Float getDedicatedAccountValue(Float decimalDenominator, Integer dedicatedAccountID) {
        Object value = response.getMembers().getValue(CSTokens.DedicatedAccountInformation + "." + CSTokens.DedicatedAccountID, dedicatedAccountID);
        Struct dedicatedAccount = null;
        if(value != null) dedicatedAccount = (Struct)value;
        else return 0f;
        value = dedicatedAccount.getValue(CSTokens.DedicatedAccountValue1.toString());
        if(value != null) {
            return convertFromCents((String)value, decimalDenominator);
        }
        else return 0f;
    }

    public Date getFreeMMSExpiryDate(Float decimalDenominator) {
        Object value = response.getMembers().getValue(CSTokens.DedicatedAccountInformation + "." + CSTokens.DedicatedAccountID, MMS_DEDICATED_ACCOUNT_ID);
        Struct dedicatedAccount = null;
        if(value != null) dedicatedAccount = (Struct)value;
        else return null;
        value = dedicatedAccount.getValue(CSTokens.ExpiryDate.toString());
        if(value != null) {
            return chopHoursOfDate((Date)value);
        }
        else return null;
    }
    
    public Float getFreeCalls(Float decimalDenominator) {
        Object value = response.getMembers().getValue(CSTokens.DedicatedAccountInformation + "." + CSTokens.DedicatedAccountID, FREE_CALLS_DEDICATED_ACCOUNT_ID);
        Struct dedicatedAccount = null;
        if(value != null) dedicatedAccount = (Struct)value;
        else return null;
        value = dedicatedAccount.getValue(CSTokens.DedicatedAccountValue1.toString());
        if(value != null) {
            return convertFromCents((String)value, decimalDenominator).floatValue();
        }
        else return null;
    }

    public Date getFreeCallsExpiryDate(Float decimalDenominator) {
        Object value = response.getMembers().getValue(CSTokens.DedicatedAccountInformation + "." + CSTokens.DedicatedAccountID, FREE_CALLS_DEDICATED_ACCOUNT_ID);
        Struct dedicatedAccount = null;
        if(value != null) dedicatedAccount = (Struct)value;
        else return null;
        value = dedicatedAccount.getValue(CSTokens.ExpiryDate.toString());
        if(value != null) {
            return chopHoursOfDate((Date)value);
        }
        else return null;
    }

    public Integer getResponseCode() {
        Object value = response.getMembers().getValue(XMLRPCTokens.ResponseCode.toString());
        if(value != null) return (Integer)value;
        else return null;
    }

    public String getCurrency() {
        Object value = response.getMembers().getValue(CSTokens.Currency1.toString());
        if(value != null) return ((String)value);
        else return null;
    }

    public Float getSubscriberBalance(Float decimalDenominator) {
        Object value = response.getMembers().getValue(CSTokens.AccountValue1.toString());
        if(value != null) return (convertFromCents((String)value, decimalDenominator));
        else return null;
    }

    public Date getCreditClearenceDate() {
        Object value = response.getMembers().getValue(CSTokens.CreditClearenceDate.toString());
        if(value != null) return (chopHoursOfDate((Date)value));
        else return null;
    }

    public Date getServiceRemovalDate() {
        Object value = response.getMembers().getValue(CSTokens.ServiceRemovalDate.toString());
        if(value != null) return(chopHoursOfDate((Date)value));
        else return null;
    }

    public abstract Integer getCurrentLanguageID();
    public abstract Date getSupervisionExpiryDate();
    public abstract Date getServiceFeeExpiryDate();

    public String getSubscriberMSISDN() {
        Object value = response.getMembers().getValue(CSTokens.SubscriberNumber.toString());
        if(value != null) return (String)value;
        else return null;
    }

    @SuppressWarnings("deprecation")
    public Date chopHoursOfDate(Date date) {
        if(date == null) return null;
        date.setHours(0);
        return date;
    }

    protected Float convertFromCents(String value, Float decimalDenominator) {
        Float realVal = Float.parseFloat(value);
        return (realVal / decimalDenominator);
    }

    public Integer getServiceClassCurrent() {
        Object value = response.getMembers().getValue(CSTokens.ServiceClassCurrent.toString());
        if(value != null) return (Integer)value;
        else return null;
    }

    public Boolean getTemporaryBlockedFlag() {
    	Object value = response.getMembers().getValue(CSTokens.TemporaryBlockedFlag.toString());
        if(value != null) return (Boolean)value;
        else return null;
    }
    
    public Date getRefillUnbarDateTime() {
    	Object value = response.getMembers().getValue(CSTokens.RefillUnbarDateTime.toString());
    	if(value != null) return ((Date)value);
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

    public Integer getFullResultCode() {
        if(getHttpStatusCode() != HttpStatus.SC_OK) return getHttpStatusCode();
        if(isFault()) return getFaultCode();
        return getResponseCode();
    }

    public abstract List<FAFNumber> getFAFInformationList();
    
    public String getOriginHostName() {
        return originHostName;
    }
    
    public void setOriginHostName(String originHostName) {
        this.originHostName = originHostName;
    }

}
