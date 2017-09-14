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
public class SignatureVerificationHandler extends AbstractRequestHandler {

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
        if(request.getRequestDealerID().equals("CLI00300")) {
        	if(getNext() !=  null)getNext().processRequest(request, response);
        	else return;
        }
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
        } else if(methodName.equals(TUMGWTokens.AdjustSubscriberAccountRequest.toString())) {
        	data = request.generateAdjustSubscriberAccountSignature();
        } else if(methodName.equals(TUMGWTokens.ChangePasswordRequest.toString())) {
            data = request.generateChangePasswordSignature();
        } else if(methodName.equals(TUMGWTokens.RechargeLogsRequest.toString())) {
            data = request.generateRechargeLogSignature();
        } else if(methodName.equals(TUMGWTokens.TotalDeailySalesReportRequest.toString())) {
            data = request.generateTotalDailySalesReportSignature();
        }
        SignatureKeyHolder shop = (SignatureKeyHolder)getDealerService().getDealerByUserName(request.getUsername().trim());
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
