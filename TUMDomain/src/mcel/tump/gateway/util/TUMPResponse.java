/*
 * TUMPResponse.java
 * 
 * Created on Sep 5, 2007, 7:38:17 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.gateway.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bridge.ena.xmlrpc.command.XMLRPCTokens;
import com.bridge.ena.xmlrpc.serializer.Array;
import com.bridge.ena.xmlrpc.serializer.Member;
import com.bridge.ena.xmlrpc.serializer.MethodResponse;
import com.bridge.ena.xmlrpc.serializer.Serializer;
import com.bridge.ena.xmlrpc.serializer.Struct;
import com.bridge.ena.util.DateUtils;
import com.bridge.ena.xml.BadXmlFormatException;
import com.bridge.ena.xmlrpc.serializer.Fault;
import java.util.Locale;

import org.apache.http.HttpStatus;
import org.dom4j.Element;

/**
 *
 * @author db2admin
 */
public class TUMPResponse implements Serializable, TUMRechargeResponse {

    private MethodResponse response = null;
    private int httpStatusCode;
    private int responseCode;
    private Locale subscriberLanguage;
    private Integer serviceClassCurrent;
    private Integer serviceClassBefore;
    private Date supervisionDate;
    private Date serviceFeeDate;
    private Integer freeSMS;
    private Integer freeMMS;

    private String voucherProfileName;
    private String voucherProfileID;
    private Float adjustmentPortion;
    private Float refillPortion;

    private Integer adjustmentResultCode;
    private Integer refillResultCode;
    
    private Integer subscriberBenefitSMS;
    private Float subscriberBenefitBonusAmount;
    
    private Float[] dedicatedAccounts = new Float[10];
                    
    public TUMPResponse() {
        super();
        this.response = new MethodResponse();
    }
    
    public TUMPResponse(String xml) {
        try {
            Serializer ser = new Serializer(xml);
            response = (MethodResponse)ser.parse();
        } catch(BadXmlFormatException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
    
    public TUMPResponse(MethodResponse response) {
        this.response = response;
    }

    public TUMPResponse(Element element) {
        response = new MethodResponse(element);
    }
    
    public boolean hasMembers() {
        if(response.getMembers() != null) return true;
        else return false;
    }
    
    public boolean isFault() {
        if(response.getFault() != null) return true;
        else return false;
    }
    
    public String getTUMTransactionID() {
        Object value = response.getMembers().getValue(TUMGWTokens.TUMTransactionID.toString());
        if(value != null) return (String)value;
        else return null;
    }
    
    public String getRequestTransactionID() {
        if(response.getFault() != null) return (String)response.getFault().getFault().getValue(TUMGWTokens.RequestTransactionID.toString());
        Object value = response.getMembers().getValue(TUMGWTokens.RequestTransactionID.toString());
        if(value != null) return (String)value;
        else return null;
    }
    
    public String getRequestDealerID() {
        Object value = response.getMembers().getValue(TUMGWTokens.RequestDealerID.toString());
        if(value != null) return (String)value;
        else return null;
    }
    
    public Integer getBalance() {
        Object value = response.getMembers().getValue(TUMGWTokens.Balance.toString());
        if(value != null) return (Integer)value;
        else return null;
    }
    
    public Date getTUMTimeStamp() {
        Object value = response.getMembers().getValue(TUMGWTokens.TUMTimeStamp.toString());
        if(value != null) return (Date)value;
        else return null;
    }
    
    public String getSignature() {
        Object value = null;
    	if(isFault()) value = response.getFault().getFault().getValue(TUMGWTokens.Signature.toString());
    	else value = response.getMembers().getValue(TUMGWTokens.Signature.toString());
        if(value != null) return new String((byte[])value);
        else return null;
    }
    
    public List<RechargeLogRecord> getRechargeLogRecords() {
        List<RechargeLogRecord> returnVal = new ArrayList<RechargeLogRecord>();
        Object value = response.getMembers().getValue(TUMGWTokens.RechargeLogsRequest.toString());
        if(value == null) return returnVal;
        Array arr = (Array)value;
        for(Struct struct : arr.getStructs()) {
            RechargeLogRecord log = new RechargeLogRecord();
            log.setAmount((Integer)struct.getValue(TUMGWTokens.Amount.toString()));
            log.setSubscriberBalanceAfter((String)struct.getValue(TUMGWTokens.SubscriberBalanceAfter.toString()));
            log.setSubscriberBalanceBefore((String)struct.getValue(TUMGWTokens.SubscriberBalanceBefore.toString()));
            log.setSubscriberMSISDN((String)struct.getValue(TUMGWTokens.SubscriberMSISDN.toString()));
            log.setTumDealerId((String)struct.getValue(TUMGWTokens.TUMDealerID.toString()));
            log.setTumTimestamp((Date)struct.getValue(TUMGWTokens.TUMTimeStamp.toString()));
            returnVal.add(log);
        }
        return returnVal;
    }
    
    public List<UserSalesRecord> getUserSalesRecords() {
        List<UserSalesRecord> returnVal = new ArrayList<UserSalesRecord>();
        Object value = response.getMembers().getValue(TUMGWTokens.UserSalesRecord.toString());
        if(value == null) return returnVal;
        Array arr = (Array)value;
        for(Struct struct : arr.getStructs()) {
            UserSalesRecord rec = new UserSalesRecord();
            rec.setAmount((Integer)struct.getValue(TUMGWTokens.Amount.toString()));
            rec.setDate((Date)struct.getValue(TUMGWTokens.TUMTimeStamp.toString()));
            rec.setEdgeDealerId((String)struct.getValue(TUMGWTokens.TUMDealerID.toString()));
            rec.setUsername((String)struct.getValue(TUMGWTokens.UserName.toString()));
            returnVal.add(rec);
        }
        return returnVal;
    }
    
    public String getRequestSubDealerID() {
        Object value = response.getMembers().getValue(TUMGWTokens.RequestSubDealerID.toString());
        if(value != null) return (String)value;
        else return null;
    }
    
    public String getSubscriberMSISDN() {
        Object value = response.getMembers().getValue(TUMGWTokens.SubscriberMSISDN.toString());
        if(value != null) return (String)value;
        else return null;
    }
    
    public String getSenderMSISDN() {
    	Object value = response.getMembers().getValue(TUMGWTokens.SenderMSISDN.toString());
    	if(value != null) return (String)value;
    	else return null;
    }
    
    public Integer getTransferAmount() {
        Object value = response.getMembers().getValue(TUMGWTokens.TransferAmount.toString());
        if(value != null) return (Integer)value;
        else return null;
    }
    
    public Boolean getTransactionStatus() {
    	Object value = response.getMembers().getValue(TUMGWTokens.TransactionStatus.toString());
        if(value != null) return (Boolean)value;
        else return null;
    }
    
    public String getVoucherSerialNumber() {
    	Object value = response.getMembers().getValue(TUMGWTokens.VoucherSerialNumber.toString());
    	if(value != null) return (String)value;
    	else return null;
    }
    
    public String getVoucherActivationCode() {
    	Object value = response.getMembers().getValue(TUMGWTokens.VoucherActivationCode.toString());
    	if(value != null) return (String)value;
    	else return null;
    }
    
    public Integer getDealerBalanceBefore() {
        Object value = response.getMembers().getValue(TUMGWTokens.DealerBalanceBefore.toString());
        if(value != null) return (Integer)value;
        else return null;
    }
    
    public Integer getDealerBalanceAfter() {
        Object value = response.getMembers().getValue(TUMGWTokens.DealerBalanceAfter.toString());
        if(value != null) return (Integer)value;
        else return null;
    }
    
    public String getSubscriberBalanceBefore() {
        Object value = response.getMembers().getValue(TUMGWTokens.SubscriberBalanceBefore.toString());
        if(value != null) return (String)value;
        else return null;
    }
    
    public String getSubscriberBalanceAfter() {
        Object value = response.getMembers().getValue(TUMGWTokens.SubscriberBalanceAfter.toString());
        if(value != null) return (String)value;
        else return null;
    }
    
    public Boolean getSubscriberTemporaryBlocked() {
    	Object value = response.getMembers().getValue(TUMGWTokens.SubscriberTemporaryBlocked.toString());
        if(value != null) return (Boolean)value;
        else return null;
    }
    
    public Date getSubscriberRefillUnbarDateTime() {
    	Object value = response.getMembers().getValue(TUMGWTokens.SubscriberRefillUnbarDateTime.toString());
        if(value != null) return (Date)value;
        else return null;
    }
    
    public String getERPOrderID() {
        Object value = response.getMembers().getValue(TUMGWTokens.ERPOrderID.toString());
        if(value != null) return (String)value;
        else return null;
    }
    
    public void generateBalanceCheckResponse(String tumTransactionId, String requestTransactionId, String requestDealerId, Integer balance, Date tumTimestamp, Integer resultCode) {
        if(!resultCode.equals(TUMResponseCodes.SUCCESS.getResponseCode())) {
            generateFaultResponse(resultCode, requestTransactionId);
            return;
        }
        response.addMember(new Member(TUMGWTokens.TUMTransactionID.toString(), tumTransactionId));
        response.addMember(new Member(TUMGWTokens.RequestTransactionID.toString(), requestTransactionId));
        response.addMember(new Member(TUMGWTokens.RequestDealerID.toString(), requestDealerId));
        response.addMember(new Member(TUMGWTokens.Balance.toString(), balance));
        response.addMember(new Member(TUMGWTokens.TUMTimeStamp.toString(), tumTimestamp));
    }
    
    public void generateRechargeLogResponse(String requestTransactionId, List<RechargeLogRecord> records, Integer resultCode) {
        if(!(resultCode.equals(TUMResponseCodes.SUCCESS.getResponseCode()))) {
            generateFaultResponse(resultCode, requestTransactionId);
            return;
        }
        response.addMember(new Member(TUMGWTokens.RequestTransactionID.toString(), requestTransactionId));
        Array array = new Array();
        for(RechargeLogRecord log : records) {
            Struct struct = new Struct();
            struct.addMember(new Member(TUMGWTokens.TUMDealerID.toString(), log.getTumDealerId()));
            struct.addMember(new Member(TUMGWTokens.TUMTimeStamp.toString(), log.getTumTimestamp()));
            struct.addMember(new Member(TUMGWTokens.Amount.toString(), log.getAmount()));
            struct.addMember(new Member(TUMGWTokens.SubscriberBalanceBefore.toString(), log.getSubscriberBalanceBefore()));
            struct.addMember(new Member(TUMGWTokens.SubscriberBalanceAfter.toString(), log.getSubscriberBalanceAfter()));
            struct.addMember(new Member(TUMGWTokens.SubscriberMSISDN.toString(), log.getSubscriberMSISDN()));
            array.addStruct(struct);
        }
        response.addMember(new Member(TUMGWTokens.RechargeLogsRequest.toString(), array));
    }
   
    public void generateTotalDailySalesReportResponse(String requestTransactionId, List<UserSalesRecord> records) {
        response.addMember(new Member(TUMGWTokens.RequestTransactionID.toString(), requestTransactionId));
        Array array = new Array();
        for(UserSalesRecord rec : records) {
            Struct struct = new Struct();
            struct.addMember(new Member(TUMGWTokens.TUMDealerID.toString(), rec.getEdgeDealerId()));
            struct.addMember(new Member(TUMGWTokens.Amount.toString(), rec.getAmount()));
            struct.addMember(new Member(TUMGWTokens.TUMTimeStamp.toString(), rec.getDate()));
            struct.addMember(new Member(TUMGWTokens.UserName.toString(), rec.getUsername()));
            array.addStruct(struct);
        }
        response.addMember(new Member(TUMGWTokens.UserSalesRecord.toString(), array));
    }
    
    public String generateBalanceCheckSignature() {
        if(response.getFault() != null) return generateFaultSignature();
        String signatureData = "";
        if(getTUMTransactionID() != null) signatureData = signatureData + getTUMTransactionID();
        if(getRequestTransactionID() != null) signatureData = signatureData + getRequestTransactionID();
        if(getRequestDealerID() != null) signatureData = signatureData + getRequestDealerID();
        if(getBalance() != null) signatureData = signatureData + getBalance();
        if(getTUMTimeStamp() != null) signatureData = signatureData + DateUtils.convertISO8601String(getTUMTimeStamp());
        return signatureData;
    }
    
    public void generateRechargeSubscriberResponse(String tumTransactionId, String requestTransactionId, String requestDealerId, String requestSubDealerId, String subscriberMSISDN, Integer transferAmount, Date tumTimestamp, Integer dealerBalanceBefore, Integer dealerBalanceAfter, Integer resultCode, Integer alertCode) {
        if(!(resultCode.equals(TUMResponseCodes.SUCCESS.getResponseCode()))) {
            generateFaultResponse(resultCode, requestTransactionId);
            return;
        }
        response.addMember(new Member(TUMGWTokens.TUMTransactionID.toString(), tumTransactionId));
        response.addMember(new Member(TUMGWTokens.RequestTransactionID.toString(), requestTransactionId));
        response.addMember(new Member(TUMGWTokens.RequestDealerID.toString(), requestDealerId));
        response.addMember(new Member(TUMGWTokens.RequestSubDealerID.toString(), requestSubDealerId));
        response.addMember(new Member(TUMGWTokens.SubscriberMSISDN.toString(), subscriberMSISDN));
        response.addMember(new Member(TUMGWTokens.TransferAmount.toString(), transferAmount));
        response.addMember(new Member(TUMGWTokens.TUMTimeStamp.toString(), tumTimestamp));
        response.addMember(new Member(TUMGWTokens.DealerBalanceBefore.toString(), dealerBalanceBefore));
        response.addMember(new Member(TUMGWTokens.DealerBalanceAfter.toString(), dealerBalanceAfter));
        response.addMember(new Member(TUMGWTokens.AlertCode.toString(), alertCode));
    }
    
    public void generateReserveRechargeSubscriberResponse(String requestTransactionId, String requestDealerId, String requestSubDealerId, String subscriberMSISDN, Integer transferAmount, Date tumTimestamp, Integer resultCode) {
    	if(!(resultCode.equals(TUMResponseCodes.SUCCESS.getResponseCode()))) {
            generateFaultResponse(resultCode, requestTransactionId);
            return;
        }
    	response.addMember(new Member(TUMGWTokens.RequestTransactionID.toString(), requestTransactionId));
    	response.addMember(new Member(TUMGWTokens.RequestDealerID.toString(), requestDealerId));
    	response.addMember(new Member(TUMGWTokens.RequestSubDealerID.toString(), requestSubDealerId));
    	response.addMember(new Member(TUMGWTokens.SubscriberMSISDN.toString(), subscriberMSISDN));
    	response.addMember(new Member(TUMGWTokens.TransferAmount.toString(), transferAmount));
    	response.addMember(new Member(TUMGWTokens.TUMTimeStamp.toString(), tumTimestamp));
    }
    
    public void generateCancelReservationResponse(String requestTransactionId, String requestDealerId, String requestSubDealerId, String subscriberMSISDN, Integer transferAmount, Date tumTimestamp, Integer resultCode) {
    	if(!(resultCode.equals(TUMResponseCodes.SUCCESS.getResponseCode()))) {
            generateFaultResponse(resultCode, requestTransactionId);
            return;
        }
    	response.addMember(new Member(TUMGWTokens.RequestTransactionID.toString(), requestTransactionId));
    	response.addMember(new Member(TUMGWTokens.RequestDealerID.toString(), requestDealerId));
    	response.addMember(new Member(TUMGWTokens.RequestSubDealerID.toString(), requestSubDealerId));
    	response.addMember(new Member(TUMGWTokens.SubscriberMSISDN.toString(), subscriberMSISDN));
    	response.addMember(new Member(TUMGWTokens.TransferAmount.toString(), transferAmount));
    	response.addMember(new Member(TUMGWTokens.TUMTimeStamp.toString(), tumTimestamp));
    }
    
    public void generateAdjustSubscriberAccountResponse(String tumTransactionId, String requestTransactionId, String requestDealerId, String requestSubDealerId, String subscriberMSISDN, Integer transferAmount, Date tumTimestamp, Integer dealerBalanceBefore, Integer dealerBalanceAfter, Integer resultCode, Integer alertCode) {
        if(!(resultCode.equals(TUMResponseCodes.SUCCESS.getResponseCode()))) {
            generateFaultResponse(resultCode, requestTransactionId);
            return;
        }
        response.addMember(new Member(TUMGWTokens.TUMTransactionID.toString(), tumTransactionId));
        response.addMember(new Member(TUMGWTokens.RequestTransactionID.toString(), requestTransactionId));
        response.addMember(new Member(TUMGWTokens.RequestDealerID.toString(), requestDealerId));
        response.addMember(new Member(TUMGWTokens.RequestSubDealerID.toString(), requestSubDealerId));
        response.addMember(new Member(TUMGWTokens.SubscriberMSISDN.toString(), subscriberMSISDN));
        response.addMember(new Member(TUMGWTokens.TransferAmount.toString(), transferAmount));
        response.addMember(new Member(TUMGWTokens.TUMTimeStamp.toString(), tumTimestamp));
        response.addMember(new Member(TUMGWTokens.DealerBalanceBefore.toString(), dealerBalanceBefore));
        response.addMember(new Member(TUMGWTokens.DealerBalanceAfter.toString(), dealerBalanceAfter));
        response.addMember(new Member(TUMGWTokens.AlertCode.toString(), alertCode));
    }
    
    public void generateRechargeSubscriberPinResponse(String tumTransactionId, String requestTransactionId, String requestDealerId, String requestSubDealerId, String subscriberMSISDN, String voucherSerialNumber, Date tumTimestamp, Integer resultCode, Integer alertCode) {
    	if(!(resultCode.equals(TUMResponseCodes.SUCCESS.getResponseCode()))) {
            generateFaultResponse(resultCode, requestTransactionId);
            return;
        }
    	response.addMember(new Member(TUMGWTokens.TUMTransactionID.toString(), tumTransactionId));
        response.addMember(new Member(TUMGWTokens.RequestTransactionID.toString(), requestTransactionId));
        response.addMember(new Member(TUMGWTokens.RequestDealerID.toString(), requestDealerId));
        response.addMember(new Member(TUMGWTokens.RequestSubDealerID.toString(), requestSubDealerId));
        response.addMember(new Member(TUMGWTokens.SubscriberMSISDN.toString(), subscriberMSISDN));
        response.addMember(new Member(TUMGWTokens.VoucherSerialNumber.toString(), voucherSerialNumber));
        response.addMember(new Member(TUMGWTokens.TUMTimeStamp.toString(), tumTimestamp));
        response.addMember(new Member(TUMGWTokens.AlertCode.toString(), alertCode));
    }
    
    public void generateCheckSubscriberValidResponse(String requestTransactionId, Date tumTimestamp, Boolean subscriberTemporaryBlocked, Date subscriberRefillUnbarDateTime, Integer resultCode) {
    	if(!(resultCode.equals(TUMResponseCodes.SUCCESS.getResponseCode()))) {
            generateFaultResponse(resultCode, requestTransactionId);
            return;
        }
    	response.addMember(new Member(TUMGWTokens.RequestTransactionID.toString(), requestTransactionId));
    	response.addMember(new Member(TUMGWTokens.TUMTimeStamp.toString(), tumTimestamp));
    	response.addMember(new Member(TUMGWTokens.SubscriberTemporaryBlocked.toString(), subscriberTemporaryBlocked));
    	response.addMember(new Member(TUMGWTokens.SubscriberRefillUnbarDateTime.toString(), subscriberRefillUnbarDateTime));
    }
    
    public void generateCheckTransactionStatusResponse(String requestTransactionId, Date tumTimestamp, Boolean transactionStatus, Integer resultCode) {
    	if(!(resultCode.equals(TUMResponseCodes.SUCCESS.getResponseCode()))) {
    		generateFaultResponse(resultCode, requestTransactionId);
    		return;
    	}
    	response.addMember(new Member(TUMGWTokens.RequestTransactionID.toString(), requestTransactionId));
    	response.addMember(new Member(TUMGWTokens.TUMTimeStamp.toString(), tumTimestamp));
    	response.addMember(new Member(TUMGWTokens.TransactionStatus.toString(), transactionStatus));
    }
    
    public void generateReserveVoucherResponse(String requestTransactionId, String requestDealerId, String requestSubDealerId, String subscriberMsisdn, Date tumTimestamp, Integer resultCode) {
    	if(!(resultCode.equals(TUMResponseCodes.SUCCESS.getResponseCode()))) {
            generateFaultResponse(resultCode, requestTransactionId);
            return;
        }
    	response.addMember(new Member(TUMGWTokens.RequestTransactionID.toString(), requestTransactionId));
    	response.addMember(new Member(TUMGWTokens.RequestDealerID.toString(), requestDealerId));
    	if(requestSubDealerId != null) response.addMember(new Member(TUMGWTokens.RequestSubDealerID.toString(), requestSubDealerId));
    	response.addMember(new Member(TUMGWTokens.SubscriberMSISDN.toString(), subscriberMsisdn));
    	response.addMember(new Member(TUMGWTokens.TUMTimeStamp.toString(), tumTimestamp));
    }
    
    public String generateReservePinSignature() {
    	if(response.getFault() != null) return generateFaultSignature();
    	String signatureData = "";
    	if(getRequestTransactionID() != null) signatureData = signatureData + getRequestTransactionID();
    	if(getRequestDealerID() != null) signatureData = signatureData + getRequestDealerID();
    	if(getRequestSubDealerID() != null) signatureData = signatureData + getRequestSubDealerID();
    	if(getSubscriberMSISDN() != null) signatureData = signatureData + getSubscriberMSISDN();
    	if(getVoucherValue() != null) signatureData = signatureData + getVoucherValue();
    	if(getVoucherSerialNumber() != null) signatureData = signatureData + getVoucherSerialNumber();
    	if(getTUMTimeStamp() != null) signatureData = signatureData + DateUtils.convertISO8601String(getTUMTimeStamp());
    	return signatureData;
    }
    
    public void generateCancelVoucherResponse(String requestTransactionId, String requestDealerId, String requestSubDealerId, String subscriberMsisdn, String voucherSerialNumber, String voucherActivationCode, String voucherValue, Date tumTimestamp, Integer resultCode) {
    	if(!(resultCode.equals(TUMResponseCodes.SUCCESS.getResponseCode()))) {
            generateFaultResponse(resultCode, requestTransactionId);
            return;
        }
    	response.addMember(new Member(TUMGWTokens.RequestTransactionID.toString(), requestTransactionId));
    	response.addMember(new Member(TUMGWTokens.RequestDealerID.toString(), requestDealerId));
    	if(requestSubDealerId != null) response.addMember(new Member(TUMGWTokens.RequestSubDealerID.toString(), requestSubDealerId));
    	response.addMember(new Member(TUMGWTokens.SubscriberMSISDN.toString(), subscriberMsisdn));
    	response.addMember(new Member(TUMGWTokens.VoucherSerialNumber.toString(), voucherSerialNumber));
    	response.addMember(new Member(TUMGWTokens.VoucherActivationCode.toString(), voucherActivationCode));
    	response.addMember(new Member(TUMGWTokens.VoucherValue.toString(), voucherValue));
    	response.addMember(new Member(TUMGWTokens.TUMTimeStamp.toString(), tumTimestamp));
    }
    
    public String generateCancelPinSignature() {
    	return generateReservePinSignature();
    }
    
    public String generateCommitPinSignature() {
    	return generateReservePinSignature();
    }
    
    public void generateCommitVoucherResponse(String tumTransactionId, String requestTransactionId, String requestDealerId, String requestSubDealerId, String subscriberMsisdn, String voucherSerialNumber, String voucherActivationCode, String voucherValue, Date tumTimestamp, Integer resultCode) {
    	if(!(resultCode.equals(TUMResponseCodes.SUCCESS.getResponseCode()))) {
            generateFaultResponse(resultCode, requestTransactionId);
            return;
        }
    	response.addMember(new Member(TUMGWTokens.TUMTransactionID.toString(), tumTransactionId));
    	response.addMember(new Member(TUMGWTokens.RequestTransactionID.toString(), requestTransactionId));
    	response.addMember(new Member(TUMGWTokens.RequestDealerID.toString(), requestDealerId));
    	if(requestSubDealerId != null) response.addMember(new Member(TUMGWTokens.RequestSubDealerID.toString(), requestSubDealerId));
    	response.addMember(new Member(TUMGWTokens.SubscriberMSISDN.toString(), subscriberMsisdn));
    	response.addMember(new Member(TUMGWTokens.VoucherSerialNumber.toString(), voucherSerialNumber));
    	response.addMember(new Member(TUMGWTokens.VoucherActivationCode.toString(), voucherActivationCode));
    	response.addMember(new Member(TUMGWTokens.VoucherValue.toString(), voucherValue));
    	response.addMember(new Member(TUMGWTokens.TUMTimeStamp.toString(), tumTimestamp));
    }
    
    public String generateRechargeSubscriberSignature() {
        if(response.getFault() != null) return generateFaultSignature();
        String signatureData = "";
        if(getTUMTransactionID() != null) signatureData = signatureData + getTUMTransactionID();
        if(getRequestTransactionID() != null) signatureData = signatureData + getRequestTransactionID();
        if(getRequestDealerID() != null) signatureData = signatureData + getRequestDealerID();
        if(getRequestSubDealerID() != null) signatureData = signatureData + getRequestSubDealerID();
        if(getSubscriberMSISDN() != null) signatureData = signatureData + getSubscriberMSISDN();
        if(getTransferAmount() != null) signatureData = signatureData + getTransferAmount();
        if(getTUMTimeStamp() != null) signatureData = signatureData + DateUtils.convertISO8601String(getTUMTimeStamp());
        if(getDealerBalanceBefore() != null) signatureData = signatureData + getDealerBalanceBefore();
        if(getDealerBalanceAfter() != null) signatureData = signatureData + getDealerBalanceAfter();
        if(getSubscriberBalanceBefore() != null) signatureData = signatureData + getSubscriberBalanceBefore();
        if(getSubscriberBalanceAfter() != null) signatureData = signatureData + getSubscriberBalanceAfter();
        return signatureData;
    }
    
    public String generateCommitReserveRechargeSubscriberSignature() {
    	return generateRechargeSubscriberSignature();
    }
    
    public String generateCancelReserveRechargeSubscriberSignature() {
    	if(response.isFault()) return generateFaultSignature();
    	String signatureData = "";
    	if(getRequestTransactionID() != null) signatureData = signatureData + getRequestTransactionID();
    	if(getRequestDealerID() != null) signatureData = signatureData + getRequestDealerID();
    	if(getRequestSubDealerID() != null) signatureData = signatureData + getRequestSubDealerID();
    	if(getSubscriberMSISDN() != null) signatureData = signatureData + getSubscriberMSISDN();
    	if(getTransferAmount() != null) signatureData = signatureData + getTransferAmount();
    	if(getTUMTimeStamp() != null) signatureData =  signatureData + DateUtils.convertISO8601String(getTUMTimeStamp());
    	return signatureData;
    }
    
    public String generateRechargeSubscriberSMSSignature() {
    	if(response.getFault() != null) return generateFaultSignature();
    	String signatureData = "";
    	if(getTUMTransactionID() != null) signatureData = signatureData + getTUMTransactionID();
    	if(getRequestTransactionID() != null) signatureData = signatureData + getRequestTransactionID();
    	if(getRequestDealerID() != null) signatureData = signatureData + getRequestDealerID();
    	if(getRequestSubDealerID() != null) signatureData = signatureData + getRequestSubDealerID();
    	if(getSubscriberMSISDN() != null) signatureData = signatureData + getSubscriberMSISDN();
    	if(getTransferAmount() != null) signatureData = signatureData + getTransferAmount();
    	if(getTUMTimeStamp() != null) signatureData = signatureData + DateUtils.convertISO8601String(getTUMTimeStamp());
    	if(getDealerBalanceBefore() != null) signatureData = signatureData + getDealerBalanceBefore();
    	if(getDealerBalanceAfter() != null) signatureData = signatureData + getDealerBalanceAfter();
    	if(getSubscriberBalanceBefore() != null) signatureData = signatureData + getSubscriberBalanceBefore();
    	if(getSubscriberBalanceAfter() != null) signatureData = signatureData + getSubscriberBalanceAfter();
    	return signatureData;
    }
    
    public String generateReserveRechargeSubscriberSignature() {
    	if(response.getFault() != null) return generateFaultSignature();
    	String signatureData = "";
    	if(getRequestTransactionID() != null) signatureData = signatureData + getRequestTransactionID();
        if(getRequestDealerID() != null) signatureData = signatureData + getRequestDealerID();
        if(getRequestSubDealerID() != null) signatureData = signatureData + getRequestSubDealerID();
        if(getSubscriberMSISDN() != null) signatureData = signatureData + getSubscriberMSISDN();
        if(getTransferAmount() != null) signatureData = signatureData + getTransferAmount();
        if(getTUMTimeStamp() != null) signatureData = signatureData + DateUtils.convertISO8601String(getTUMTimeStamp());
        return signatureData;
    }
    
    public String generateCancelReservationSignature() {
    	if(response.getFault() != null) return generateFaultSignature();
    	String signatureData = "";
    	if(getTUMTransactionID() != null) signatureData = signatureData + getTUMTransactionID();
        if(getRequestTransactionID() != null) signatureData = signatureData + getRequestTransactionID();
        if(getRequestDealerID() != null) signatureData = signatureData + getRequestDealerID();
        if(getRequestSubDealerID() != null) signatureData = signatureData + getRequestSubDealerID();
        if(getSubscriberMSISDN() != null) signatureData = signatureData + getSubscriberMSISDN();
        if(getTransferAmount() != null) signatureData = signatureData + getTransferAmount();
        if(getTUMTimeStamp() != null) signatureData = signatureData + DateUtils.convertISO8601String(getTUMTimeStamp());
        if(getDealerBalanceBefore() != null) signatureData = signatureData + getDealerBalanceBefore();
        if(getDealerBalanceAfter() != null) signatureData = signatureData + getDealerBalanceAfter();
        if(getSubscriberBalanceBefore() != null) signatureData = signatureData + getSubscriberBalanceBefore();
        if(getSubscriberBalanceAfter() != null) signatureData = signatureData + getSubscriberBalanceAfter();
        return signatureData;
    }
     
    public String generateAdjustSubscriberAccountSignature() {
        if(response.getFault() != null) return generateFaultSignature();
        String signatureData = "";
        if(getTUMTransactionID() != null) signatureData = signatureData + getTUMTransactionID();
        if(getRequestTransactionID() != null) signatureData = signatureData + getRequestTransactionID();
        if(getRequestDealerID() != null) signatureData = signatureData + getRequestDealerID();
        if(getRequestSubDealerID() != null) signatureData = signatureData + getRequestSubDealerID();
        if(getSubscriberMSISDN() != null) signatureData = signatureData + getSubscriberMSISDN();
        if(getTransferAmount() != null) signatureData = signatureData + getTransferAmount();
        if(getTUMTimeStamp() != null) signatureData = signatureData + DateUtils.convertISO8601String(getTUMTimeStamp());
        if(getDealerBalanceBefore() != null) signatureData = signatureData + getDealerBalanceBefore();
        if(getDealerBalanceAfter() != null) signatureData = signatureData + getDealerBalanceAfter();
        if(getSubscriberBalanceBefore() != null) signatureData = signatureData + getSubscriberBalanceBefore();
        if(getSubscriberBalanceAfter() != null) signatureData = signatureData + getSubscriberBalanceAfter();
        return signatureData;
    }
    
    public String generateRechargeSubscriberPinSignature() {
    	if(response.getFault() != null) return generateFaultSignature();
        String signatureData = "";
        if(getTUMTransactionID() != null) signatureData = signatureData + getTUMTransactionID();
        if(getRequestTransactionID() != null) signatureData = signatureData + getRequestTransactionID();
        if(getRequestDealerID() != null) signatureData = signatureData + getRequestDealerID();
        if(getRequestSubDealerID() != null) signatureData = signatureData + getRequestSubDealerID();
        if(getSubscriberMSISDN() != null) signatureData = signatureData + getSubscriberMSISDN();
        if(getTUMTimeStamp() != null) signatureData = signatureData + DateUtils.convertISO8601String(getTUMTimeStamp());
        if(getSubscriberBalanceBefore() != null) signatureData = signatureData + getSubscriberBalanceBefore();
        if(getSubscriberBalanceAfter() != null) signatureData = signatureData + getSubscriberBalanceAfter();
        return signatureData;
    }
    
    public String generateCheckSubscriberValidSignature() {
    	if(response.getFault() != null) return generateFaultSignature();
        String signatureData = "";
        if(getRequestTransactionID() != null) signatureData = signatureData + getRequestTransactionID();
        if(getTUMTimeStamp() != null) signatureData = signatureData + DateUtils.convertISO8601String(getTUMTimeStamp());
        if(getSubscriberTemporaryBlocked() != null) signatureData = signatureData + getSubscriberTemporaryBlocked();
        if(getSubscriberRefillUnbarDateTime() != null) signatureData = signatureData + getSubscriberRefillUnbarDateTime();
        return signatureData;
    }
    
    public String generateCheckTransactionStatusSignature() {
    	if(response.getFault() != null) return generateFaultSignature();
    	String signatureData = "";
    	if(getRequestTransactionID() != null) signatureData = signatureData + getRequestTransactionID();
    	if(getTransactionStatus() != null) signatureData = signatureData + getTransactionStatus();
    	if(getTUMTimeStamp() != null) signatureData = signatureData + DateUtils.convertISO8601String(getTUMTimeStamp());
    	return signatureData;
    }
    
    public String generateRechargeLogsSignature() {
        if(response.getFault() != null) return generateFaultSignature();
        String signatureData = "";
        if(getRequestTransactionID() != null) signatureData = signatureData + getRequestTransactionID();
        return signatureData;
    }
    
    public String generateTotalDailySalesReportSignature() {
        if(response.getFault() != null) return generateFaultSignature();
        String signatureData = "";
        if(getRequestTransactionID() != null) signatureData = signatureData + getRequestTransactionID();
        return signatureData;
    }
    
    public void generatePaymentArriveResponse(String tumTransactionId, Integer resultCode) {
        if(!(resultCode.equals(TUMResponseCodes.SUCCESS.getResponseCode()))) {
            generateFaultResponse(resultCode, tumTransactionId);
            return;
        }
        response.addMember(new Member(TUMGWTokens.TUMTransactionID.toString(), tumTransactionId));       
    }
    
    public void generateChangePasswordResponse(String tumTransactionId, Integer resultCode) {
        if(!resultCode.equals(TUMResponseCodes.SUCCESS.getResponseCode())) {
            generateFaultResponse(resultCode, tumTransactionId);
            return;
        }
        response.addMember(new Member(TUMGWTokens.TUMTransactionID.toString(), tumTransactionId));
    }
    
    public String generatePaymentArriveSignature() {
        if(response.getFault() != null) return generateFaultSignature();
        String signatureData = "";
        if(getTUMTransactionID() != null) signatureData = signatureData + getTUMTransactionID();
        return signatureData;
    }
    
    public void generateFaultResponse(Integer resultCode, String requestTransactionId) {
        Fault fault = new Fault();
        fault.getFault().addMember(new Member(XMLRPCTokens.FaultCode.toString(), resultCode));
        fault.getFault().addMember(new Member(XMLRPCTokens.FaultString.toString(), TUMResponseCodes.getResponseCode(resultCode).toString()));
        fault.getFault().addMember(new Member(TUMGWTokens.RequestTransactionID.toString(), requestTransactionId));
        response.setFault(fault);
    }
    
    public String generateFaultSignature() {
        String signatureData = "";
        signatureData = signatureData + response.getFault().getFault().getValue(XMLRPCTokens.FaultCode.toString());
        signatureData = signatureData + response.getFault().getFault().getValue(TUMGWTokens.RequestTransactionID.toString());
        return signatureData;
    }
    
    public String generateChangePasswordSignature() {
        if(response.getFault() != null) return generateFaultSignature();
        String signatureData = "";
        if(getTUMTransactionID() != null) signatureData = signatureData + getTUMTransactionID();
        return signatureData;
    }
    
    public String getERPReference() {
        Object value = response.getMembers().getValue(TUMGWTokens.ERPReference.toString());
        if(value != null) return (String)value;
        else return null;
    }
    
    public Integer getFaultCode() {
        if(response.getFault() == null) return null;
        return (Integer)response.getFault().getFault().getValue(XMLRPCTokens.FaultCode.toString());
        
    }
    
    public Integer getAlertCode() {
        Object value = response.getMembers().getValue(TUMGWTokens.AlertCode.toString());
        if(value != null) return (Integer)value;
        else return null;
    }
    
    public void setSignature(byte[] signature) {
        Member member = new Member(TUMGWTokens.Signature.toString(), signature);
        if(response.getFault() != null) {
            response.getFault().getFault().addMember(member);
        } else {
            response.addMember(member);
        }
    }
    
    public String toXML() {
        return response.toXML(new StringBuffer());
    }
    
    public String toXML(StringBuffer buffer) {
    	return response.toXML(buffer);
    }

    public void setTUMTransactionID(String tumTransactionID) {
        response.addMember(new Member(TUMGWTokens.TUMTransactionID.toString(), tumTransactionID));
    }

    public void setTUMTimeStamp(Date tumTimeStamp) {
        response.addMember(new Member(TUMGWTokens.TUMTimeStamp.toString(), tumTimeStamp));
    }

    public void setDealerBalanceBefore(Integer dealerBalanceBefore) {
        response.addMember(new Member(TUMGWTokens.DealerBalanceBefore.toString(), dealerBalanceBefore));
    }

    public void setDealerBalanceAfter(Integer dealerBalanceAfter) {
        response.addMember(new Member(TUMGWTokens.DealerBalanceAfter.toString(), dealerBalanceAfter));
    }

    public void setSubscriberBalanceBefore(String subscriberBalanceBefore) {
        response.addMember(new Member(TUMGWTokens.SubscriberBalanceBefore.toString(), subscriberBalanceBefore));
    }

    public void setSubscriberBalanceAfter(String subscriberBalanceAfter) {
    	if(response.getMember(TUMGWTokens.SubscriberBalanceAfter.toString()) != null) {
        	Member member = response.getMember(TUMGWTokens.SubscriberBalanceAfter.toString());
        	member.setValue(subscriberBalanceAfter);
        } else {
        	response.addMember(new Member(TUMGWTokens.SubscriberBalanceAfter.toString(), subscriberBalanceAfter));
        }
    }

    public void setRequestTransactionID(String requestTransactionID) {
        response.addMember(new Member(TUMGWTokens.RequestTransactionID.toString(), requestTransactionID));
    }

    public void setRequestDealerID(String requestDealerID) {
        response.addMember(new Member(TUMGWTokens.RequestDealerID.toString(), requestDealerID));
    }

    public void setSignature(String signature) {
        setSignature(signature.getBytes());
    }

    public void setSubscriberMSISDN(String subscriberMSISDN) {
        response.addMember(new Member(TUMGWTokens.SubscriberMSISDN.toString(), subscriberMSISDN));
    }

    public void setRequestSubDealerID(String requestSubDealerID) {
        response.addMember(new Member(TUMGWTokens.RequestSubDealerID.toString(), requestSubDealerID));
    }

    public void setTransferAmount(Integer transferAmount) {
    	if(response.getMember(TUMGWTokens.TransferAmount.toString()) != null) {
        	Member member = response.getMember(TUMGWTokens.TransferAmount.toString());
        	member.setValue(transferAmount);
        } else {
        	response.addMember(new Member(TUMGWTokens.TransferAmount.toString(), transferAmount));
        }
    }
    
    public void setVoucherSerialNumber(String voucherSerialNumber) {
    	response.addMember(new Member(TUMGWTokens.VoucherSerialNumber.toString(), voucherSerialNumber));
    }
    
    public void setSenderMSISDN(String senderMSISDN) {
    	response.addMember(new Member(TUMGWTokens.SenderMSISDN.toString(), senderMSISDN));
    }
    
    public void setSubscriberTemporayBlocked(Boolean temporaryBlocked) {
    	response.addMember(new Member(TUMGWTokens.SubscriberTemporaryBlocked.toString(), temporaryBlocked));
    }
    
    public void setSubscriberRefillUnbarDateTime(Date refillUnbarDateTime) {
    	response.addMember(new Member(TUMGWTokens.SubscriberRefillUnbarDateTime.toString(), refillUnbarDateTime));
    }

    public int getHttpStatusCode() {
        return this.httpStatusCode;
    }
    
    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public Integer getResponseCode() {
        return this.responseCode;
    }
    
    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public Locale getSubscriberLanguage() {
        return this.subscriberLanguage;
    }

    public void setSubscriberLanguage(Locale language) {
        this.subscriberLanguage = language;
    }

    public Integer getServiceClassCurrent() {
        return this.serviceClassCurrent;
    }

    public void setServiceClassCurrent(Integer serviceClassCurrent) {
        this.serviceClassCurrent = serviceClassCurrent;
    }

    public Integer getServiceClassBefore() {
        return this.serviceClassBefore;
    }

    public void setServiceClassBefore(Integer serviceClassBefore) {
        this.serviceClassBefore = serviceClassBefore;
    }

    public Date getSupervisionDate() {
        return this.supervisionDate;
    }

    public void setSupervisionDate(Date supervisionDate) {
        this.supervisionDate = supervisionDate;
    }

    public Date getServiceFeeDate() {
        return this.serviceFeeDate;
    }

    public void setServiceFeeDate(Date serviceFeeDate) {
        this.serviceFeeDate = serviceFeeDate;
    }

    /**
     * @return the freeSMS
     */
    public Integer getFreeSMS() {
        return freeSMS;
    }

    public void setFreeSMS(Integer freeSMS) {
        this.freeSMS = freeSMS;
    }

    public Integer getFreeMMS() {
        return freeMMS;
    }

    public void setFreeMMS(Integer freeMMS) {
        this.freeMMS = freeMMS;
    }
    
    public Float getDedicatedAccountValue(int dedicatedAccountID) {
    	if(dedicatedAccountID > dedicatedAccounts.length) return new Float(0);
    	Float value = dedicatedAccounts[dedicatedAccountID - 1];
    	if(value == null) return new Float(0);
    	return value;
    }
    
    public void setDedicatedAccountValue(int dedicatedAccountID, Float value) {
    	if(dedicatedAccountID > dedicatedAccounts.length) return;
    	dedicatedAccounts[dedicatedAccountID - 1] = value;
    }

    public Integer getFullResultCode() {
        if(getHttpStatusCode() != HttpStatus.SC_OK) return getHttpStatusCode();
        if(isFault()) return getFaultCode();
        return getResponseCode();
    }

    public Integer getAdjustmentResultCode() {
        return adjustmentResultCode;
    }

    public void setAdjustmentResultCode(Integer adjustmentResultCode) {
        this.adjustmentResultCode = adjustmentResultCode;
    }

    public Integer getRefillResultCode() {
        return refillResultCode;
    }

    public void setRefillResultCode(Integer refillResultCode) {
        this.refillResultCode = refillResultCode;
    }

    public String getVoucherProfileName() {
        return voucherProfileName;
    }

    public void setVoucherProfileName(String voucherProfileName) {
        this.voucherProfileName = voucherProfileName;
    }

    public String getVoucherProfileID() {
        return voucherProfileID;
    }

    public void setVoucherProfileID(String voucherProfileID) {
        this.voucherProfileID = voucherProfileID;
    }
    
    public Float getAdjustmentPortion() {
        return adjustmentPortion;
    }

    public void setAdjustmentPortion(Float adjustmentPortion) {
        this.adjustmentPortion = adjustmentPortion;
    }

    public Float getRefillPortion() {
        return refillPortion;
    }

    public void setRefillPortion(Float refillPortion) {
        this.refillPortion = refillPortion;
    }

    public void setSubscriberLanguage(Integer languageId) {
        if(languageId == 2) this.subscriberLanguage = Locale.ENGLISH;
        else subscriberLanguage = new Locale("por");
    }

	@Override
	public void setDedicatedAccountValues(Float[] dedicatedAccountValues) {
		this.dedicatedAccounts = dedicatedAccountValues;
	}

	public Integer getSubscriberBenefitSMS() {
		return subscriberBenefitSMS;
	}

	public void setSubscriberBenefitSMS(Integer subscriberBenefitSMS) {
		this.subscriberBenefitSMS = subscriberBenefitSMS;
	}

	public Float getSubscriberBenefitBonusAmount() {
		return subscriberBenefitBonusAmount;
	}

	public void setSubscriberBenefitBonusAmount(Float subscriberBenefitBonusAmount) {
		this.subscriberBenefitBonusAmount = subscriberBenefitBonusAmount;
	}

	@Override
	public String getVoucherValue() {
		Object value = response.getMembers().getValue(TUMGWTokens.VoucherValue.toString());
	    if(value != null) return (String)value;
	    else return null;
	}

	@Override
	public void setVoucherValue(String value) {
		response.addMember(new Member(TUMGWTokens.VoucherValue.toString(), value));		
	}
	
}
