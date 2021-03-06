/*
 * SignatureGenerationHandler.java
 * 
 * Created on Sep 13, 2007, 10:33:22 AM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.gateway.handler;

import mcel.tump.gateway.util.TUMGWTokens;
import mcel.tump.gateway.util.TUMPRequest;
import mcel.tump.gateway.util.TUMPResponse;
import mcel.tump.gateway.util.TUMResponseCodes;
import mcel.tump.util.cert.ISignatureVerifier;

/**
 *
 * @author db2admin
 */
public class SignatureGenerationHandler extends AbstractTUMRequestHandler {

    private ISignatureVerifier signatureVerifier;
    
    public ISignatureVerifier getSignatureVerifier() {
        return signatureVerifier;
    }

    public void setSignatureVerifier(ISignatureVerifier signatureVerifier) {
        this.signatureVerifier = signatureVerifier;
    }
    
    public void processRequest(TUMPRequest request, TUMPResponse response) throws TUMRequestHandlerException {
        try {
            String methodName = request.getMethodName();
            String signatureData = null;
            if(methodName.equals(TUMGWTokens.BalanceCheckRequest.toString())) {
                signatureData = response.generateBalanceCheckSignature();
            } else if(methodName.equals(TUMGWTokens.RechargeSubscriberRequest.toString())) {
                signatureData = response.generateRechargeSubscriberSignature();
            } else if(methodName.equals(TUMGWTokens.RechargeSubscriberPinRequest.toString())) {
            	signatureData = response.generateRechargeSubscriberPinSignature();
            } else if(methodName.equals(TUMGWTokens.RechargeSubscriberSMSRequest.toString())) {
            	signatureData = response.generateRechargeSubscriberSMSSignature();
            } else if(methodName.equals(TUMGWTokens.AdjustSubscriberAccountRequest.toString())) {
            	signatureData = response.generateAdjustSubscriberAccountSignature();
            } else if(methodName.equals(TUMGWTokens.ReserveRechargeSubscriberRequest.toString())) {
            	signatureData = response.generateReserveRechargeSubscriberSignature();
            } else if(methodName.equals(TUMGWTokens.CommitReservationRequest.toString())) {
            	signatureData = response.generateCommitReserveRechargeSubscriberSignature();
            } else if(methodName.equals(TUMGWTokens.CheckSubscriberValidRequest.toString())) {
            	signatureData = response.generateCheckSubscriberValidSignature();
            } else if(methodName.equals(TUMGWTokens.CheckTransactionStatusRequest.toString())) {
            	signatureData = response.generateCheckTransactionStatusSignature();
            } else if(methodName.equals(TUMGWTokens.ChangePasswordRequest.toString())) {
                signatureData = response.generateChangePasswordSignature();
            } else if(methodName.equals(TUMGWTokens.RechargeLogsRequest.toString())) {
                signatureData = response.generateRechargeLogsSignature();
            } else if(methodName.equals(TUMGWTokens.TotalDeailySalesReportRequest.toString())) {
                signatureData = response.generateTotalDailySalesReportSignature();
            } else if(methodName.equals(TUMGWTokens.CancelReservationRequest.toString())) {
            	signatureData = response.generateCancelReservationSignature();
            } else if(methodName.equals(TUMGWTokens.ReserveVoucherRequest.toString())) {
            	signatureData = response.generateReservePinSignature();
            } else if(methodName.equals(TUMGWTokens.CommitVoucherRequest.toString())) {
            	signatureData = response.generateCommitPinSignature();
            } else if(methodName.equals(TUMGWTokens.CancelVoucherRequest.toString())) {
            	signatureData = response.generateCancelPinSignature();
            }
            byte[] signature = getSignatureVerifier().sign(signatureData);
            response.setSignature(signature);
            if(getNext() != null) getNext().processRequest(request, response);
        } catch(Exception e) {
            throw new TUMRequestHandlerException(TUMResponseCodes.SIGNATURE_ERROR.getResponseCode(), request.getRequestTransactionID());
        }
    }
}
