/*
 * SignatureHandler.java
 * 
 * Created on Sep 10, 2007, 3:23:22 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.gateway.handler;

import mcel.tump.dealer.domain.SignatureKeyHolder;
import mcel.tump.dealer.service.IDealerService;
import mcel.tump.gateway.util.TUMGWTokens;
import mcel.tump.gateway.util.TUMPRequest;
import mcel.tump.gateway.util.TUMPResponse;
import mcel.tump.gateway.util.TUMResponseCodes;
import mcel.tump.util.cert.ISignatureVerifier;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author db2admin
 */
public class SignatureVerificationHandler extends AbstractTUMRequestHandler {

    private static Log logger = LogFactory.getLog(SignatureVerificationHandler.class);
    private ISignatureVerifier signatureVerifier;
    private IDealerService dealerService;
    
    public SignatureVerificationHandler() {
        super();
    }

    public ISignatureVerifier getSignatureVerifier() {
        return signatureVerifier;
    }

    public void setSignatureVerifier(ISignatureVerifier signatureVerifier) {
        this.signatureVerifier = signatureVerifier;
    }
    
    public IDealerService getDealerService() {
        return dealerService;
    }
    
    public void setDealerService(IDealerService dealerService) {
        this.dealerService = dealerService;
    }

    public void processRequest(TUMPRequest request, TUMPResponse response) throws TUMRequestHandlerException {
        logger.debug("Signature verification handler is processing the request");
        //Bypass the digital signature verification for Millenium BIM requests.. 
        /**
        if(request.getRequestDealerID().equals("CON00240")) {
        	logger.debug("Bypassing signature verification for the dealer..Will continue with the next handler..");
        	if(getNext() != null) getNext().processRequest(request, response);
        	return;
        }
        **/
        String methodName = request.getMethodName();
        String signature = null;
        String data = null;
        String requestTransactionId = request.getRequestTransactionID();
        if(methodName.equals(TUMGWTokens.BalanceCheckRequest.toString())) {
            data = request.generateBalanceCheckSignature();
        } else if(methodName.equals(TUMGWTokens.RechargeSubscriberRequest.toString())) {
            data = request.generateRechargeSubscriberSignature();
        } else if(methodName.equals(TUMGWTokens.RechargeSubscriberPinRequest.toString())) {
        	data = request.generateRechargeSubscriberPinSignature();
        } else if(methodName.equals(TUMGWTokens.RechargeSubscriberSMSRequest.toString())) {
        	data = request.generateRechargeSubscriberSMSSignature();
        } else if(methodName.equals(TUMGWTokens.ReserveRechargeSubscriberRequest.toString())) {
        	data = request.generateReserveRechargeSubscriberSignature();
        } else if(methodName.equals(TUMGWTokens.CommitReservationRequest.toString())) {
        	data = request.generateCommitReserveRechargeSubscriberSignature();
        } else if(methodName.equals(TUMGWTokens.AdjustSubscriberAccountRequest.toString())) {
        	data = request.generateAdjustSubscriberAccountSignature();
        } else if(methodName.equals(TUMGWTokens.CheckSubscriberValidRequest.toString())) {
        	data = request.generateCheckSubscriberValidSignature();
        } else if(methodName.equals(TUMGWTokens.CheckTransactionStatusRequest.toString())) {
        	data = request.generateCheckTransactionStatusSignature();
        } else if(methodName.equals(TUMGWTokens.ChangePasswordRequest.toString())) {
            data = request.generateChangePasswordSignature();
        } else if(methodName.equals(TUMGWTokens.RechargeLogsRequest.toString())) {
            data = request.generateRechargeLogSignature();
        } else if(methodName.equals(TUMGWTokens.TotalDeailySalesReportRequest.toString())) {
            data = request.generateTotalDailySalesReportSignature();
        } else if(methodName.equals(TUMGWTokens.CancelReservationRequest.toString())) {
        	data = request.generateCancelReserveRechargeSubscriberSignature();
        } else if(methodName.equals(TUMGWTokens.ReserveVoucherRequest.toString())) {
        	data = request.generateReservePinSignature();
        } else if(methodName.equals(TUMGWTokens.CommitVoucherRequest.toString())) {
        	data = request.generateCommitPinSignature();
        } else if(methodName.equals(TUMGWTokens.CancelVoucherRequest.toString())) {
        	data = request.generateCancelPinSignature();
        }
        SignatureKeyHolder shop = (SignatureKeyHolder)getDealerService().getDealerByUserName(request.getUsername().trim());
        if(shop == null) {
        	logger.debug("Signature is not verified. Processing will be stopped with a TUM Signature Error.");
            throw new TUMRequestHandlerException(TUMResponseCodes.INVALID_LOGIN_CREDENTIALS.getResponseCode(), requestTransactionId);
        }
        signature = request.getSignature();
        if(signature == null || signature.length() == 0) throw new TUMRequestHandlerException(TUMResponseCodes.SIGNATURE_ERROR.getResponseCode(), requestTransactionId);
        boolean status = getSignatureVerifier().verify(shop.getKeyAlias(), signature, data);
        if(!status) {
            logger.debug("Signature is not verified. Processing will be stopped with a TUM Signature Error.");
            throw new TUMRequestHandlerException(TUMResponseCodes.SIGNATURE_ERROR.getResponseCode(), requestTransactionId);
        }
        logger.debug("Signature verified.");
        if(getNext() != null) getNext().processRequest(request, response);
    }
}
