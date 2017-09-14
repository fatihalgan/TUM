/*
 * TUMPRequest.java
 * 
 * Created on Sep 5, 2007, 7:05:30 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.gateway.util;

import java.io.Serializable;
import java.util.Date;
import com.bridge.ena.util.DateUtils;
import com.bridge.ena.xmlrpc.serializer.Member;
import com.bridge.ena.xmlrpc.serializer.MethodCall;

/**
 *
 * @author db2admin
 */
public class TUMPRequest implements Serializable, TUMRechargeRequest {

    private MethodCall methodCall;
    
    public TUMPRequest() {
        super();
        this.methodCall = new MethodCall();
    }
    
    public TUMPRequest(MethodCall methodCall) {
        super();
        this.methodCall = methodCall;
    }
    
    public void setRequestTransactionID(String requestTransactionID) {
        methodCall.addMember(new Member(TUMGWTokens.RequestTransactionID.toString(), requestTransactionID));
    }
    
    public void setRequestDealerID(String requestDealerID) {
        methodCall.addMember(new Member(TUMGWTokens.RequestDealerID.toString(), requestDealerID));
    }
    
    public void setRequestSubDealerID(String requestSubDealerID) {
        methodCall.addMember(new Member(TUMGWTokens.RequestSubDealerID.toString(), requestSubDealerID));
    }
    
    public void generateBalanceCheckRequest(String username, String password, String requestTransactionId, String requestDealerId, Date requestTimestamp) {
        methodCall.setMethodName(TUMGWTokens.BalanceCheckRequest.toString());
        methodCall.addMember(new Member(TUMGWTokens.UserName.toString(), username));
        methodCall.addMember(new Member(TUMGWTokens.Password.toString(), password));
        methodCall.addMember(new Member(TUMGWTokens.RequestTransactionID.toString(), requestTransactionId));
        methodCall.addMember(new Member(TUMGWTokens.RequestDealerID.toString(), requestDealerId));
        methodCall.addMember(new Member(TUMGWTokens.RequestTimeStamp.toString(), requestTimestamp));
    }
    
    public void generateRechargeSubscriberRequest(String username, String password, String requestTransactionId, String requestDealerId, String requestSubdealerId, String subscriberMSISDN, Integer transferAmount, Date requestTimestamp) {
        methodCall.setMethodName(TUMGWTokens.RechargeSubscriberRequest.toString());
        methodCall.addMember(new Member(TUMGWTokens.UserName.toString(), username));
        methodCall.addMember(new Member(TUMGWTokens.Password.toString(), password));
        methodCall.addMember(new Member(TUMGWTokens.RequestTransactionID.toString(), requestTransactionId));
        methodCall.addMember(new Member(TUMGWTokens.RequestDealerID.toString(), requestDealerId));
        methodCall.addMember(new Member(TUMGWTokens.RequestSubDealerID.toString(), requestSubdealerId));
        methodCall.addMember(new Member(TUMGWTokens.SubscriberMSISDN.toString(), subscriberMSISDN));
        methodCall.addMember(new Member(TUMGWTokens.TransferAmount.toString(), transferAmount));
        methodCall.addMember(new Member(TUMGWTokens.RequestTimeStamp.toString(), requestTimestamp));
    }
    
    public void generateReserveRechargeSubscriberRequest(String username, String password, String requestTransactionId, String requestDealerId, String requestSubdealerId, String subscriberMSISDN, Integer transferAmount, Date requestTimestamp) {
    	methodCall.setMethodName(TUMGWTokens.ReserveRechargeSubscriberRequest.toString());
        methodCall.addMember(new Member(TUMGWTokens.UserName.toString(), username));
        methodCall.addMember(new Member(TUMGWTokens.Password.toString(), password));
        methodCall.addMember(new Member(TUMGWTokens.RequestTransactionID.toString(), requestTransactionId));
        methodCall.addMember(new Member(TUMGWTokens.RequestDealerID.toString(), requestDealerId));
        methodCall.addMember(new Member(TUMGWTokens.RequestSubDealerID.toString(), requestSubdealerId));
        methodCall.addMember(new Member(TUMGWTokens.SubscriberMSISDN.toString(), subscriberMSISDN));
        methodCall.addMember(new Member(TUMGWTokens.TransferAmount.toString(), transferAmount));
        methodCall.addMember(new Member(TUMGWTokens.RequestTimeStamp.toString(), requestTimestamp));
    }
    
    public void generateAdjustSubscriberAccountRequest(String username, String password, String requestTransactionId, String requestDealerId, String requestSubdealerId, String subscriberMSISDN, Integer transferAmount, Date requestTimestamp) {
    	methodCall.setMethodName(TUMGWTokens.AdjustSubscriberAccountRequest.toString());
        methodCall.addMember(new Member(TUMGWTokens.UserName.toString(), username));
        methodCall.addMember(new Member(TUMGWTokens.Password.toString(), password));
        methodCall.addMember(new Member(TUMGWTokens.RequestTransactionID.toString(), requestTransactionId));
        methodCall.addMember(new Member(TUMGWTokens.RequestDealerID.toString(), requestDealerId));
        methodCall.addMember(new Member(TUMGWTokens.RequestSubDealerID.toString(), requestSubdealerId));
        methodCall.addMember(new Member(TUMGWTokens.SubscriberMSISDN.toString(), subscriberMSISDN));
        methodCall.addMember(new Member(TUMGWTokens.TransferAmount.toString(), transferAmount));
        methodCall.addMember(new Member(TUMGWTokens.RequestTimeStamp.toString(), requestTimestamp));
    }
    
    public void generatePaymentArriveRequest(String erpDealerId, String erpReference, String erpOrderId, Date erpTimestamp, String username, Integer amount) {
        methodCall.setMethodName(TUMGWTokens.PaymentArriveRequest.toString());
        methodCall.addMember(new Member(TUMGWTokens.ERPDealerID.toString(), erpDealerId));
        methodCall.addMember(new Member(TUMGWTokens.ERPReference.toString(), erpReference));
        methodCall.addMember(new Member(TUMGWTokens.ERPOrderID.toString(), erpOrderId));
        methodCall.addMember(new Member(TUMGWTokens.ERPTimeStamp.toString(), erpTimestamp));
        methodCall.addMember(new Member(TUMGWTokens.UserName.toString(), username));
        methodCall.addMember(new Member(TUMGWTokens.ERPAmount.toString(), amount));
    }
    
    public void generateChangePasswordRequest(String username, String password, String requestTransactionID, Date requestTimestamp, String newPassword, String confirmPassword) {
        methodCall.setMethodName(TUMGWTokens.ChangePasswordRequest.toString());
        methodCall.addMember(new Member(TUMGWTokens.UserName.toString(), username));
        methodCall.addMember(new Member(TUMGWTokens.Password.toString(), password));
        methodCall.addMember(new Member(TUMGWTokens.RequestTransactionID.toString(), requestTransactionID));
        methodCall.addMember(new Member(TUMGWTokens.RequestTimeStamp.toString(), requestTimestamp));
        methodCall.addMember(new Member(TUMGWTokens.NewPassword.toString(), newPassword));
        methodCall.addMember(new Member(TUMGWTokens.ConfirmPassword.toString(), confirmPassword));
    }
    
    public void generateRechargeLogRequest(String username, String password, String requestTransactionId) {
        methodCall.setMethodName(TUMGWTokens.RechargeLogsRequest.toString());
        methodCall.addMember(new Member(TUMGWTokens.UserName.toString(), username));
        methodCall.addMember(new Member(TUMGWTokens.Password.toString(), password));
        methodCall.addMember(new Member(TUMGWTokens.RequestTransactionID.toString(), requestTransactionId));
    }
    
    public void generateTotalDailySalesReportRequest(String username, String password, String requestTransactionId) {
        methodCall.setMethodName(TUMGWTokens.TotalDeailySalesReportRequest.toString());
        methodCall.addMember(new Member(TUMGWTokens.UserName.toString(), username));
        methodCall.addMember(new Member(TUMGWTokens.Password.toString(), password));
        methodCall.addMember(new Member(TUMGWTokens.RequestTransactionID.toString(), requestTransactionId));
    }
    
    public void generateReserveVoucherRequest(String username, String password, String requestTransactionId, String requestDealerId, String requestSubDealerId, String subscriberMsisdn, String voucherActivationCode, Date requestTimestamp) {
    	methodCall.setMethodName(TUMGWTokens.ReserveVoucherRequest.toString());
    	methodCall.addMember(new Member(TUMGWTokens.UserName.toString(), username));
    	methodCall.addMember(new Member(TUMGWTokens.Password.toString(), password));
    	methodCall.addMember(new Member(TUMGWTokens.RequestTransactionID.toString(), requestTransactionId));
    	methodCall.addMember(new Member(TUMGWTokens.RequestDealerID.toString(), requestDealerId));
    	if(requestSubDealerId != null) methodCall.addMember(new Member(TUMGWTokens.RequestSubDealerID.toString(), requestSubDealerId));
    	methodCall.addMember(new Member(TUMGWTokens.SubscriberMSISDN.toString(), subscriberMsisdn));
    	methodCall.addMember(new Member(TUMGWTokens.VoucherActivationCode.toString(), voucherActivationCode));
    	methodCall.addMember(new Member(TUMGWTokens.RequestTimeStamp.toString(), requestTimestamp));
    }
    
    public void generateCommitReserveVoucherRequest(String username, String password, String requestTransactionId, String requestDealerId, String requestSubDealerId, Date requestTimestamp) {
    	methodCall.setMethodName(TUMGWTokens.CommitVoucherRequest.toString());
    	methodCall.addMember(new Member(TUMGWTokens.UserName.toString(), username));
    	methodCall.addMember(new Member(TUMGWTokens.Password.toString(), password));
    	methodCall.addMember(new Member(TUMGWTokens.RequestTransactionID.toString(), requestTransactionId));
    	methodCall.addMember(new Member(TUMGWTokens.RequestDealerID.toString(), requestDealerId));
    	if(requestSubDealerId != null) methodCall.addMember(new Member(TUMGWTokens.RequestSubDealerID.toString(), requestSubDealerId));
    	methodCall.addMember(new Member(TUMGWTokens.RequestTimeStamp.toString(), requestTimestamp));
    }
    
    public void generateCancelReserveVoucherRequest(String username, String password, String requestTransactionId, String requestDealerId, String requestSubDealerId, Date requestTimestamp) {
    	methodCall.setMethodName(TUMGWTokens.CancelVoucherRequest.toString());
    	methodCall.addMember(new Member(TUMGWTokens.UserName.toString(), username));
    	methodCall.addMember(new Member(TUMGWTokens.Password.toString(), password));
    	methodCall.addMember(new Member(TUMGWTokens.RequestTransactionID.toString(), requestTransactionId));
    	methodCall.addMember(new Member(TUMGWTokens.RequestDealerID.toString(), requestDealerId));
    	if(requestSubDealerId != null) methodCall.addMember(new Member(TUMGWTokens.RequestSubDealerID.toString(), requestSubDealerId));
    	methodCall.addMember(new Member(TUMGWTokens.RequestTimeStamp.toString(), requestTimestamp));
    }
            
    public String toXML() {
    	return methodCall.toXML(new StringBuffer());
    }
    
    public String getUsername() {
        Object value = methodCall.getMembers().getValue(TUMGWTokens.UserName.toString());
        if(value != null) return (String)value;
        else return null;
    }
    
    public String getPassword() {
        Object value = methodCall.getMembers().getValue(TUMGWTokens.Password.toString());
        if(value != null) return (String)value;
        else return null;
    }
    
    public String getMethodName() {
        return methodCall.getMethodName();
    }
    
    public String getSignature() {
        Object value = methodCall.getMembers().getValue(TUMGWTokens.Signature.toString());
        if(value != null) return new String((byte[])value);
        else return null;
    }
     
    public String getRequestTransactionID() {
        Object value = methodCall.getMembers().getValue(TUMGWTokens.RequestTransactionID.toString());
        if(value != null) return (String)value;
        else return null;
    }
    
    public String getRequestDealerID() {
        Object value = methodCall.getMembers().getValue(TUMGWTokens.RequestDealerID.toString());
        if(value != null) return (String)value;
        else return null;
    }
    
    public Date getRequestTimeStamp() {
        Object value = methodCall.getMembers().getValue(TUMGWTokens.RequestTimeStamp.toString());
        if(value != null) return (Date)value;
        else return null;
    }
    
    public String getRequestSubDealerID() {
        Object value = methodCall.getMembers().getValue(TUMGWTokens.RequestSubDealerID.toString());
        if(value != null) return (String)value;
        else return null;
    }
    
    public String getSubscriberMSISDN() {
        Object value = methodCall.getMembers().getValue(TUMGWTokens.SubscriberMSISDN.toString());
        if(value != null) return (String)value;
        else return null;
    }
    
    public String getSenderMSISDN() {
    	Object value = methodCall.getMembers().getValue(TUMGWTokens.SenderMSISDN.toString());
    	if(value != null) return (String)value;
    	else return null;
    }
    
    public Integer getTransferAmount() {
        Object value = methodCall.getMembers().getValue(TUMGWTokens.TransferAmount.toString());
        if(value != null) return (Integer)value;
        else return null;
    }
    
    public Integer getTransactionAmountCommission() {
    	Object value = methodCall.getMembers().getValue(TUMGWTokens.TransactionAmountComission.toString());
    	if(value != null) return (Integer)value;
    	else return null;
    }
    
    public String getVoucherActivationCode() {
    	Object value = methodCall.getMembers().getValue(TUMGWTokens.VoucherActivationCode.toString());
        if(value != null) return (String)value;
        else return null;
    }
    
    public String getVoucherSerialNumber() {
    	Object value = methodCall.getMembers().getValue(TUMGWTokens.VoucherSerialNumber.toString());
        if(value != null) return (String)value;
        else return null;
    }
    
    public Integer getTUMPaymentID() {
        Object value = methodCall.getMembers().getValue(TUMGWTokens.TUMPaymentID.toString());
        if(value != null) return (Integer)value;
        else return null;
    }
    
    public String getERPReference() {
        Object value = methodCall.getMembers().getValue(TUMGWTokens.ERPReference.toString());
        if(value != null) return (String)value;
        else return null;
    }
    
    public String getERPOrderID() {
        Object value = methodCall.getMembers().getValue(TUMGWTokens.ERPOrderID.toString());
        if(value != null) return (String)value;
        else return null;
    }
    
    public Date getERPTimeStamp() {
        Object value = methodCall.getMembers().getValue(TUMGWTokens.ERPTimeStamp.toString());
        if(value != null) return (Date)value;
        else return null;
    }
    
    public Integer getERPAmount() {
        Object value = methodCall.getMembers().getValue(TUMGWTokens.ERPAmount.toString());
        if(value != null) return (Integer)value;
        else return null;
    }
    
    public String getNewPassword() {
        Object value = methodCall.getMembers().getValue(TUMGWTokens.NewPassword.toString());
        if(value != null) return (String)value;
        else return null;
    }
    
    public String getConfirmPassword() {
        Object value = methodCall.getMembers().getValue(TUMGWTokens.ConfirmPassword.toString());
        if(value != null) return (String)value;
        else return null;
    }
    
    public String generateBalanceCheckSignature() {
        String signatureData = "";
        if(getUsername() != null) signatureData = signatureData + getUsername();
        if(getPassword() != null) signatureData = signatureData + getPassword();
        if(getRequestTransactionID() != null) signatureData = signatureData + getRequestTransactionID();
        if(getRequestDealerID() != null) signatureData = signatureData + getRequestDealerID();
        if(getRequestTimeStamp() != null) signatureData = signatureData + DateUtils.convertISO8601String(getRequestTimeStamp());
        return signatureData;
    }
    
    public String generateRechargeSubscriberSignature() {
        String signatureData = "";
        if(getUsername() != null) signatureData = signatureData + getUsername();
        if(getPassword() != null) signatureData = signatureData + getPassword();
        if(getRequestTransactionID() != null) signatureData = signatureData + getRequestTransactionID();
        if(getRequestDealerID() != null) signatureData = signatureData + getRequestDealerID();
        if(getRequestSubDealerID() != null) signatureData = signatureData + getRequestSubDealerID();
        if(getSubscriberMSISDN() != null) signatureData = signatureData + getSubscriberMSISDN();
        if(getTransferAmount() != null) signatureData = signatureData + getTransferAmount();
        if(getRequestTimeStamp() != null) signatureData = signatureData + DateUtils.convertISO8601String(getRequestTimeStamp());
        return signatureData;
    }
    
    public String generateReserveRechargeSubscriberSignature() {
    	return generateRechargeSubscriberSignature();
    }
    
    public String generateCancelReserveRechargeSubscriberSignature() {
    	String signatureData = "";
    	if(getUsername() != null) signatureData = signatureData + getUsername();
    	if(getPassword() != null) signatureData = signatureData + getPassword();
    	if(getRequestTransactionID() != null) signatureData = signatureData + getRequestTransactionID();
    	if(getRequestDealerID() != null) signatureData = signatureData + getRequestDealerID();
    	if(getRequestSubDealerID() != null) signatureData = signatureData + getRequestSubDealerID();
    	if(getRequestTimeStamp() != null) signatureData = signatureData + DateUtils.convertISO8601String(getRequestTimeStamp());
    	return signatureData;
    }
    
    public String generateCommitReserveRechargeSubscriberSignature() {
    	return generateCancelReserveRechargeSubscriberSignature();
    }
    
    public String generateAdjustSubscriberAccountSignature() {
    	String signatureData = "";
        if(getUsername() != null) signatureData = signatureData + getUsername();
        if(getPassword() != null) signatureData = signatureData + getPassword();
        if(getRequestTransactionID() != null) signatureData = signatureData + getRequestTransactionID();
        if(getRequestDealerID() != null) signatureData = signatureData + getRequestDealerID();
        if(getRequestSubDealerID() != null) signatureData = signatureData + getRequestSubDealerID();
        if(getSubscriberMSISDN() != null) signatureData = signatureData + getSubscriberMSISDN();
        if(getTransferAmount() != null) signatureData = signatureData + getTransferAmount();
        if(getRequestTimeStamp() != null) signatureData = signatureData + DateUtils.convertISO8601String(getRequestTimeStamp());
        return signatureData;
    }
    
    public String generateRechargeSubscriberPinSignature() {
    	String signatureData = "";
    	if(getUsername() != null) signatureData = signatureData + getUsername();
        if(getPassword() != null) signatureData = signatureData + getPassword();
        if(getRequestTransactionID() != null) signatureData = signatureData + getRequestTransactionID();
        if(getRequestDealerID() != null) signatureData = signatureData + getRequestDealerID();
        if(getRequestSubDealerID() != null) signatureData = signatureData + getRequestSubDealerID();
        if(getSubscriberMSISDN() != null) signatureData = signatureData + getSubscriberMSISDN();
        if(getVoucherSerialNumber() != null) signatureData = signatureData + getVoucherSerialNumber();
        if(getVoucherActivationCode() != null) signatureData = signatureData + getVoucherActivationCode();
        if(getRequestTimeStamp() != null) signatureData = signatureData + DateUtils.convertISO8601String(getRequestTimeStamp());
        return signatureData;
    }
    
    public String generateReservePinSignature() {
    	String signatureData = "";
    	if(getUsername() != null) signatureData = signatureData + getUsername();
        if(getPassword() != null) signatureData = signatureData + getPassword();
        if(getRequestTransactionID() != null) signatureData = signatureData + getRequestTransactionID();
        if(getRequestDealerID() != null) signatureData = signatureData + getRequestDealerID();
        if(getRequestSubDealerID() != null) signatureData = signatureData + getRequestSubDealerID();
        if(getSubscriberMSISDN() != null) signatureData = signatureData + getSubscriberMSISDN();
        if(getVoucherActivationCode() != null) signatureData = signatureData + getVoucherActivationCode();
        if(getRequestTimeStamp() != null) signatureData = signatureData + DateUtils.convertISO8601String(getRequestTimeStamp());
        return signatureData;
    }
    
    public String generateCommitPinSignature() {
    	String signatureData = "";
    	if(getUsername() != null) signatureData = signatureData + getUsername();
        if(getPassword() != null) signatureData = signatureData + getPassword();
        if(getRequestTransactionID() != null) signatureData = signatureData + getRequestTransactionID();
        if(getRequestDealerID() != null) signatureData = signatureData + getRequestDealerID();
        if(getRequestSubDealerID() != null) signatureData = signatureData + getRequestSubDealerID();
        if(getRequestTimeStamp() != null) signatureData = signatureData + DateUtils.convertISO8601String(getRequestTimeStamp());
        return signatureData;
    }
    
    public String generateCancelPinSignature() {
    	return generateCommitPinSignature();
    }
    
    public String generateRechargeSubscriberSMSSignature() {
    	String signatureData = "";
    	if(getUsername() != null) signatureData = signatureData + getUsername();
    	if(getPassword() != null) signatureData = signatureData + getPassword();
    	if(getRequestTransactionID() != null) signatureData = signatureData + getRequestTransactionID();
    	if(getRequestDealerID() != null) signatureData = signatureData + getRequestDealerID();
        if(getRequestSubDealerID() != null) signatureData = signatureData + getRequestSubDealerID();
        if(getSubscriberMSISDN() != null) signatureData = signatureData + getSubscriberMSISDN();
        if(getTransferAmount() != null) signatureData = signatureData + getTransferAmount();
        if(getRequestTimeStamp() != null) signatureData = signatureData + DateUtils.convertISO8601String(getRequestTimeStamp());
        return signatureData;
    }
    
    public String generateCheckSubscriberValidSignature() {
    	String signatureData = "";
    	if(getUsername() != null) signatureData = signatureData + getUsername();
    	if(getPassword() != null) signatureData = signatureData + getPassword();
    	if(getRequestTransactionID() != null) signatureData = signatureData + getRequestTransactionID();
    	if(getRequestTimeStamp() != null) signatureData = signatureData + getRequestTimeStamp();
    	if(getSubscriberMSISDN() != null) signatureData = signatureData + getSubscriberMSISDN();
    	return signatureData;
    }
    
    public String generateCheckTransactionStatusSignature() {
    	String signatureData = "";
    	if(getUsername() != null) signatureData = signatureData + getUsername();
    	if(getPassword() != null) signatureData = signatureData + getPassword();
    	if(getRequestTransactionID() != null) signatureData = signatureData + getRequestTransactionID();
    	if(getRequestDealerID() != null) signatureData = signatureData + getRequestDealerID();
    	if(getRequestTimeStamp() != null) signatureData = signatureData + DateUtils.convertISO8601String(getRequestTimeStamp());
    	return signatureData;
    }
    
    public String generateChangePasswordSignature() {
        String signatureData = "";
        if(getUsername() != null) signatureData = signatureData + getUsername();
        if(getPassword() != null) signatureData = signatureData + getPassword();
        if(getRequestTransactionID() != null) signatureData = signatureData + getRequestTransactionID();
        if(getRequestTimeStamp() != null) signatureData = signatureData + DateUtils.convertISO8601String(getRequestTimeStamp());
        if(getNewPassword() != null) signatureData = signatureData + getNewPassword();
        if(getConfirmPassword() != null) signatureData = signatureData + getConfirmPassword();
        return signatureData;
    }
    
    public String generateRechargeLogSignature() {
        String signatureData = "";
        if(getUsername() != null) signatureData = signatureData + getUsername();
        return signatureData;
    }
    
    public String generateTotalDailySalesReportSignature() {
        String signatureData = "";
        if(getUsername() != null) signatureData = signatureData + getUsername();
        return signatureData;
    }
    
    public void setSignature(byte[] signature) {
        Member member = new Member(TUMGWTokens.Signature.toString(), signature);
        methodCall.addMember(member);
    }

    public void setUsername(String username) {
        methodCall.addMember(new Member(TUMGWTokens.UserName.toString(), username));
    }

    public void setPassword(String password) {
        methodCall.addMember(new Member(TUMGWTokens.Password.toString(), password));
    }

    public void setSignature(String signature) {
        setSignature(signature.getBytes());
    }
    
    public void setRequestTimeStamp(Date requestTimeStamp) {
        methodCall.addMember(new Member(TUMGWTokens.RequestTimeStamp.toString(), requestTimeStamp));
    }

    public void setSubscriberMSISDN(String subscriberMSISDN) {
        methodCall.addMember(new Member(TUMGWTokens.SubscriberMSISDN.toString(), subscriberMSISDN));
    }

    public void setTransferAmount(Integer transferAmount) {
        if(methodCall.getMember(TUMGWTokens.TransferAmount.toString()) != null) {
        	Member member = methodCall.getMember(TUMGWTokens.TransferAmount.toString());
        	member.setValue(transferAmount);
        } else {
        	methodCall.addMember(new Member(TUMGWTokens.TransferAmount.toString(), transferAmount));
        }
    }
    
    public void setTransactionAmountCommission(Integer transactionAmountCommission) {
    	if(methodCall.getMember(TUMGWTokens.TransactionAmountComission.toString()) != null) {
        	Member member = methodCall.getMember(TUMGWTokens.TransactionAmountComission.toString());
        	member.setValue(transactionAmountCommission);
        } else {
        	methodCall.addMember(new Member(TUMGWTokens.TransactionAmountComission.toString(), transactionAmountCommission));
        }
    }
    
    public void setVoucherActivationCode(String voucherActivationCode) {
    	methodCall.addMember(new Member(TUMGWTokens.VoucherActivationCode.toString(), voucherActivationCode));
    }
    
    public void setVoucherSerialNumber(String voucherSerialNumber) {
    	methodCall.addMember(new Member(TUMGWTokens.VoucherSerialNumber.toString(), voucherSerialNumber));
    }
    
    public void setSenderMSISDN(String senderMSISDN) {
    	methodCall.addMember(new Member(TUMGWTokens.SenderMSISDN.toString(), senderMSISDN));
    }
    
}
