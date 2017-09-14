package mcel.tump.gateway.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bridge.ena.xmlrpc.IXMLRPCClient;
import com.bridge.ena.xmlrpc.exception.XmlRpcConnectionException;

import mcel.tump.gateway.handler.TUMRequestHandlerException;
import mcel.tump.gateway.util.TUMGWTokens;
import mcel.tump.gateway.util.TUMPRequest;
import mcel.tump.gateway.util.TUMPResponse;
import mcel.tump.gateway.util.TUMRechargeRequest;
import mcel.tump.gateway.util.TUMRechargeResponse;
import mcel.tump.gateway.util.TUMResponseCodes;
import mcel.tump.util.cert.ISignatureVerifier;

public class TUMGatewayClient {

	private IXMLRPCClient xmlRpcClient;
	private ISignatureVerifier signatureVerifier;
	private static Log logger = LogFactory.getLog(TUMGatewayClient.class);

	public IXMLRPCClient getXmlRpcClient() {
		return xmlRpcClient;
	}

	public void setXmlRpcClient(IXMLRPCClient xmlRpcClient) {
		this.xmlRpcClient = xmlRpcClient;
	}
	
	public ISignatureVerifier getSignatureVerifier() {
		return signatureVerifier;
	}

	public void setSignatureVerifier(ISignatureVerifier signatureVerifier) {
		this.signatureVerifier = signatureVerifier;
	}

	public TUMPResponse sendMessage(TUMRechargeRequest request) throws XmlRpcConnectionException, TUMRequestHandlerException {
		TUMPResponse response = null;
		generateRequestSignature(request);
		String remoteResp = getXmlRpcClient().sendMessage(request.toXML());
		response = new TUMPResponse(remoteResp);
		verifyResponseSignature(request, response);
		return response;
	}
	
	private void generateRequestSignature(TUMRechargeRequest request) {
		TUMPRequest tumpRequest = (TUMPRequest)request;
		String signatureData = null;
		if(tumpRequest.getMethodName().equals(TUMGWTokens.ReserveVoucherRequest.toString())) {
			signatureData = tumpRequest.generateReservePinSignature();
		} else if(tumpRequest.getMethodName().equals(TUMGWTokens.CommitVoucherRequest.toString())) {
			signatureData = tumpRequest.generateCommitPinSignature();
		} else if(tumpRequest.getMethodName().equals(TUMGWTokens.CancelVoucherRequest.toString())) {
			signatureData = tumpRequest.generateCancelPinSignature();
		} else if(tumpRequest.getMethodName().equals(TUMGWTokens.RechargeSubscriberRequest.toString())) {
			signatureData = tumpRequest.generateRechargeSubscriberSignature();
		}
		byte[] signature = getSignatureVerifier().sign(signatureData);
		tumpRequest.setSignature(signature);
	}
	
	private void verifyResponseSignature(TUMRechargeRequest request, TUMRechargeResponse response) throws TUMRequestHandlerException {
		String requestTransactionId = request.getRequestTransactionID();
		try {
			TUMPResponse tumpResponse = (TUMPResponse)response;
			String methodName = request.getMethodName();
			String signature = tumpResponse.getSignature();
			if(signature == null || signature.length() == 0) throw new TUMRequestHandlerException(TUMResponseCodes.SIGNATURE_ERROR.getResponseCode(), requestTransactionId);
			String signatureData = null;
			if(methodName.equals(TUMGWTokens.BalanceCheckRequest.toString())) {
				signatureData = tumpResponse.generateBalanceCheckSignature();
			} else if(methodName.equals(TUMGWTokens.RechargeSubscriberRequest.toString())) {
				signatureData = tumpResponse.generateRechargeSubscriberSignature();
			} else if(methodName.equals(TUMGWTokens.RechargeSubscriberPinRequest.toString())) {
				signatureData = tumpResponse.generateRechargeSubscriberPinSignature();
			} else if(methodName.equals(TUMGWTokens.RechargeSubscriberSMSRequest.toString())) {
				signatureData = tumpResponse.generateRechargeSubscriberSMSSignature();
			} else if(methodName.equals(TUMGWTokens.AdjustSubscriberAccountRequest.toString())) {
				signatureData = tumpResponse.generateAdjustSubscriberAccountSignature();
			} else if(methodName.equals(TUMGWTokens.ReserveRechargeSubscriberRequest.toString())) {
				signatureData = tumpResponse.generateReserveRechargeSubscriberSignature();
			} else if(methodName.equals(TUMGWTokens.CheckSubscriberValidRequest.toString())) {
				signatureData = tumpResponse.generateCheckSubscriberValidSignature();
			} else if(methodName.equals(TUMGWTokens.CheckTransactionStatusRequest.toString())) {
				signatureData = tumpResponse.generateCheckTransactionStatusSignature();
			} else if(methodName.equals(TUMGWTokens.ChangePasswordRequest.toString())) {
				signatureData = tumpResponse.generateChangePasswordSignature();
			} else if(methodName.equals(TUMGWTokens.RechargeLogsRequest.toString())) {
				signatureData = tumpResponse.generateRechargeLogsSignature();
			} else if(methodName.equals(TUMGWTokens.TotalDeailySalesReportRequest.toString())) {
				signatureData = tumpResponse.generateTotalDailySalesReportSignature();
			} else if(methodName.equals(TUMGWTokens.CancelReservationRequest.toString())) {
				signatureData = tumpResponse.generateCancelReservationSignature();
			} else if(methodName.equals(TUMGWTokens.ReserveVoucherRequest.toString())) {
				signatureData = tumpResponse.generateReservePinSignature();
			} else if(methodName.equals(TUMGWTokens.CommitVoucherRequest.toString())) {
				signatureData = tumpResponse.generateCommitPinSignature();
			} else if(methodName.equals(TUMGWTokens.CancelVoucherRequest.toString())) {
				signatureData = tumpResponse.generateCancelPinSignature();
			}
			boolean status = getSignatureVerifier().verify(null, signature, signatureData);
			if(status == false) response.generateFaultResponse(TUMResponseCodes.SIGNATURE_ERROR.getResponseCode(), requestTransactionId);
		} catch(Exception e) {
			logger.error("Unexpected error while verifying the response signature: " + e.getMessage());
			throw new TUMRequestHandlerException(TUMResponseCodes.SIGNATURE_ERROR.getResponseCode(), requestTransactionId);
		}
	}
	
	
}
