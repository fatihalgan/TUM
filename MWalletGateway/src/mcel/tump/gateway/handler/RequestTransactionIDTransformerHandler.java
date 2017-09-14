package mcel.tump.gateway.handler;

import mcel.tump.gateway.util.TUMResponseCodes;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bridge.ena.cs.command.CSTokens;
import com.bridge.ena.xmlrpc.serializer.MethodCall;
import com.bridge.ena.xmlrpc.serializer.MethodResponse;

public class RequestTransactionIDTransformerHandler extends AbstractRequestHandler {

	private static final Log logger = LogFactory.getLog(RequestTransactionIDTransformerHandler.class);
	
	@Override
	public void processRequest(MethodCall request, MethodResponse response) throws TUMRequestHandlerException {
		logger.debug("RequestTransactionIDTransformer handler started...");
		try {
			String originTransId = (String)request.getMemberValue(CSTokens.OriginTransactionID.toString());
			if(originTransId.length() != 14) {
        		logger.error("Origin TransactionID length is not good, it should be 14 digits for Fundamo...");
        		throw new TUMRequestHandlerException(TUMResponseCodes.CS_ILLEGAL_REQUEST_MESSAGE.getResponseCode(), "originTransactionID is not valid..");
        	}
        	request.getMember(CSTokens.OriginTransactionID.toString()).setValue("3" + originTransId);
        	logger.debug("Transformed the RequestTransactionID: to " + request.getMemberValue(CSTokens.OriginTransactionID.toString()));
        	if(getNext() != null) getNext().processRequest(request, response);
		} catch(Exception e) {
            logger.error("Failed to convert RequestTransactionID: " + e.getMessage());
            throw new TUMRequestHandlerException(TUMResponseCodes.CS_OTHER_ERROR.getResponseCode(), e.getMessage());
        }
		
	}

	
}
