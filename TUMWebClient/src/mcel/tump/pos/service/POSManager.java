package mcel.tump.pos.service;

import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;

import mcel.tump.dealer.domain.AbstractDealer;
import mcel.tump.gateway.util.TUMGWTokens;
import mcel.tump.gateway.util.TUMPRequest;
import mcel.tump.gateway.util.TUMPResponse;
import mcel.tump.security.domain.User;
import mcel.tump.util.AlertCodes;
import mcel.tump.util.FaultCodes;
import mcel.tump.util.cert.SignatureVerifier;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;

import com.bridge.ena.xmlrpc.XMLRPCClient;
import com.bridge.ena.xmlrpc.exception.XmlRpcConnectionException;
import com.bridge.ena.xmlrpc.serializer.MethodResponse;
import com.bridge.ena.xmlrpc.serializer.Serializer;

@Name("posManager")
@AutoCreate
public class POSManager {

	@Logger
	Log log;
	
	@In
	Identity identity;
	
	@In("#{xmlrpcClient}")
	XMLRPCClient xmlRpcClient;
	
	@In("#{signatureVerifier}")
	SignatureVerifier signatureVerifier;
	
	@In(scope=ScopeType.SESSION)
	User loggedInUser;
	
	@Out(scope=ScopeType.PAGE, required=false)
	TUMPResponse response = null;
	
	@In
	private FacesMessages facesMessages;
	
	
	private String subscriberMSISDN;
    
	private Integer transferAmount;
    
	private String password;
	
	private String newPassword;
	
	private String confirmPassword;
	
	public TUMPResponse getResponse() {
		return response;
	}

	public void setResponse(TUMPResponse response) {
		this.response = response;
	}
	
	public String getSubscriberMSISDN() {
		return subscriberMSISDN;
	}

	public void setSubscriberMSISDN(String subscriberMSISDN) {
		this.subscriberMSISDN = subscriberMSISDN;
	}

	public Integer getTransferAmount() {
		return transferAmount;
	}

	public void setTransferAmount(Integer transferAmount) {
		this.transferAmount = transferAmount;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	private TUMPResponse sendMessage(TUMPRequest request) throws XmlRpcConnectionException {
		String xmlResponse = xmlRpcClient.sendMessage(request.toXML());
		Serializer ser = null;
		try {
			ser = new Serializer(xmlResponse);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
		MethodResponse methodResponse = (MethodResponse) ser.parse();
		return new TUMPResponse(methodResponse);
	}
	
	public void generateSignature(TUMPRequest request) {
        String signatureData = generateSignatureData(request);
        byte[] signature = signatureVerifier.sign(signatureData);
        request.setSignature(signature);
    }
	
	public String generateSignatureData(TUMPRequest request) {
        if(request.getMethodName().equals(TUMGWTokens.RechargeSubscriberRequest.toString()))
            return request.generateRechargeSubscriberSignature();
        else if(request.getMethodName().equals(TUMGWTokens.BalanceCheckRequest.toString()))
            return request.generateBalanceCheckSignature();
        else if(request.getMethodName().equals(TUMGWTokens.ChangePasswordRequest.toString()))
            return request.generateChangePasswordSignature();
        else if(request.getMethodName().equals(TUMGWTokens.RechargeLogsRequest.toString()))
            return request.generateRechargeLogSignature();
        else if(request.getMethodName().equals(TUMGWTokens.TotalDeailySalesReportRequest.toString()))
            return request.generateTotalDailySalesReportSignature();
        return null;
    }
	
	public String generateBalanceCheckRequest() throws XmlRpcConnectionException {
        try {
        	AbstractDealer dealer = loggedInUser.getOwningDealer();
        	TUMPRequest request = new TUMPRequest();
        	request.generateBalanceCheckRequest(loggedInUser.getUsername(), loggedInUser.getPassword(), "0", dealer.getDealerCode(), new Date());
        	log.debug(request.toXML());
        	generateSignature(request);
        	this.response = sendMessage(request);
        	log.debug(response.toXML());
        	if(!response.isFault()) facesMessages.add(FacesMessage.SEVERITY_INFO, "Request processed successfully...");
        	else facesMessages.add(FacesMessage.SEVERITY_ERROR, "Error: " + response.getFaultCode() + "-" + getFaultString());
        } catch(Exception e) {
        	facesMessages.add(FacesMessage.SEVERITY_ERROR, "Request could not be sent successfully: " + e.getMessage());
        }
        return null;
    }
	
	public String generateRechargeSubscriberRequest() throws XmlRpcConnectionException {
        try {
        	TUMPRequest request = new TUMPRequest();
        	request.generateRechargeSubscriberRequest(loggedInUser.getUsername(), password, "0", "0", "0", subscriberMSISDN, transferAmount, new Date());
        	generateSignature(request);
        	log.debug(request.toXML());
        	this.response = sendMessage(request);
        	log.debug(response.toXML());
        	if(!response.isFault()) facesMessages.add(FacesMessage.SEVERITY_INFO, "Request processed successfully...");
        	else facesMessages.add(FacesMessage.SEVERITY_ERROR, "Error: " + response.getFaultCode() + "-" + getFaultString());
        } catch(Exception e) {
        	facesMessages.add(FacesMessage.SEVERITY_ERROR, "Request could not be sent successfully: " + e.getMessage());
        }
        return null;
    }
	
	public String generateChangePasswordRequest() throws XmlRpcConnectionException {
        try {
        	TUMPRequest request = new TUMPRequest();
        	request.generateChangePasswordRequest(loggedInUser.getUsername(), password, "0", new Date(), getNewPassword(), getConfirmPassword());
        	generateSignature(request);
        	log.debug(request.toXML());
        	this.response = sendMessage(request);
        	log.debug(response.toXML());
        	if(!response.isFault()) facesMessages.add(FacesMessage.SEVERITY_INFO, "Request processed successfully...");
        	else facesMessages.add(FacesMessage.SEVERITY_ERROR, "Error: " + response.getFaultCode() + "-" + getFaultString());
        } catch(Exception e) {
        	facesMessages.add(FacesMessage.SEVERITY_ERROR, "Request could not be sent successfully: " + e.getMessage());
        }
        return null;
    }
	
	public String generateGetLast5RechargesRequest() throws XmlRpcConnectionException {
        try {
        	TUMPRequest request = new TUMPRequest();
        	request.generateRechargeLogRequest(loggedInUser.getUsername(), loggedInUser.getPassword(), "0");
        	generateSignature(request);
        	log.debug(request.toXML());
        	this.response = sendMessage(request);
        	log.debug(response.toXML());
        	if(!response.isFault()) facesMessages.add(FacesMessage.SEVERITY_INFO, "Request processed successfully...");
        	else facesMessages.add(FacesMessage.SEVERITY_ERROR, "Error: " + response.getFaultCode() + "-" + getFaultString());
        } catch(Exception e) {
        	facesMessages.add(FacesMessage.SEVERITY_ERROR, "Request could not be sent successfully: " + e.getMessage());
        }
        return null;
    }
	
	public String generateTotalDailySalesReportRequest() throws XmlRpcConnectionException {
		try {
        	TUMPRequest request = new TUMPRequest();
        	request.generateTotalDailySalesReportRequest(loggedInUser.getUsername(), loggedInUser.getPassword(), "0");
        	generateSignature(request);
        	log.debug(request.toXML());
        	this.response = sendMessage(request);
        	log.debug(response.toXML());
        	if(!response.isFault()) facesMessages.add(FacesMessage.SEVERITY_INFO, "Request processed successfully...");
        	else facesMessages.add(FacesMessage.SEVERITY_ERROR, "Error: " + response.getFaultCode() + "-" + getFaultString());
        } catch(Exception e) {
        	facesMessages.add(FacesMessage.SEVERITY_ERROR, "Request could not be sent successfully: " + e.getMessage());
        }
        return null;
	}
	
	public String clearRecharge() {
		subscriberMSISDN = null;
		transferAmount = null;
		password = null;
		return null;
	}
	
	public String clearChangePassword() {
		password = null;
		newPassword = null;
		confirmPassword = null;
		return null;
	}
	
	public String getAlertString() {
        if(response == null) return "";
        Integer alertCode = response.getAlertCode();
        if(alertCode == null) return "";
        return AlertCodes.getString(alertCode.toString());
    }
    
    public String getFaultString() {
        Integer faultCode = response.getFaultCode();
        if(faultCode == null) return "";
        return FaultCodes.getString(faultCode.toString());
    }
    
    public boolean isFaultResponse() {
        if(response == null) return false;
        if (response.isFault()) return true;
        return false;
    }
    
    public boolean isSuccessResponse() {
        if(response == null) return false;
        if(response.isFault()) return false;
        return true;
    }
}
