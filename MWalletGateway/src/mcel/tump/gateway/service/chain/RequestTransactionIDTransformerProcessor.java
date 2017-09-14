package mcel.tump.gateway.service.chain;

import mcel.tump.gateway.util.TUMRechargeRequest;
import mcel.tump.gateway.util.TUMRechargeResponse;
import mcel.tump.gateway.util.TUMResponseCodes;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RequestTransactionIDTransformerProcessor implements RechargeRequestProcessor {

	private RechargeRequestProcessor nextHandler;
    private RechargeRequestProcessor onFaultHandler;
    private static final Log logger = LogFactory.getLog(RequestTransactionIDTransformerProcessor.class);
    
    public void setNextHandler(RechargeRequestProcessor processor) {
        this.nextHandler = processor;
    }
    
    public RechargeRequestProcessor getNextHandler() {
        return this.nextHandler;
    }

    public void process(TUMRechargeRequest request, TUMRechargeResponse response) {
        logger.debug("RequestTransactionIDTransformer processor started...");
        try {
        	String originTransId = request.getRequestTransactionID();
        	if(originTransId.length() != 14) {
        		logger.error("Origin TransactionID length is not good, it should be 14 digits for Fundamo...");
        		response.generateFaultResponse(TUMResponseCodes.FAILED_REQUEST_TRANSACTION_ID.getResponseCode(), originTransId);
        		return;
        	}
        	request.setRequestTransactionID("3" + originTransId);
        	logger.debug("Transformed the RequestTransactionID: to " + request.getRequestTransactionID());
        } catch(Exception e) {
            logger.error("Failed to convert RequestTransactionID: " + e.getMessage());
            response.generateFaultResponse(TUMResponseCodes.FAILED_REQUEST_TRANSACTION_ID.getResponseCode(), "0");
        }
        if(getNextHandler() != null) getNextHandler().process(request, response);
    }
    
    public void setOnFaultHandler(RechargeRequestProcessor processor) {
        this.onFaultHandler = processor;
    }

    public RechargeRequestProcessor getOnFaultHandler() {
        return onFaultHandler;
    }
}
