package mcel.tump.gateway.handler;

import mcel.tump.gateway.util.TUMGWTokens;
import mcel.tump.gateway.util.TUMPRequest;
import mcel.tump.gateway.util.TUMPResponse;
import mcel.tump.gateway.util.TUMResponseCodes;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bridge.ena.cs.command.CSTokens;
import com.bridge.ena.xmlrpc.serializer.Member;
import com.bridge.ena.xmlrpc.serializer.MethodCall;
import com.bridge.ena.xmlrpc.serializer.MethodResponse;

public class TransactionAmountCommissionTransformerHandler extends AbstractTUMRequestHandler {

	private static final Log logger = LogFactory.getLog(TransactionAmountCommissionTransformerHandler.class);
	
	@Override
	public void processRequest(TUMPRequest request, TUMPResponse response) throws TUMRequestHandlerException {
		logger.debug("TransactionAmountCommissionTransformer handler started for transaciton: " + request.getRequestTransactionID());
		try {
			if(request.getMethodName().equals(TUMGWTokens.RechargeSubscriberRequest.toString())) {
				Integer transferAmount = request.getTransferAmount();
				if(request.getRequestSubDealerID() != null && request.getRequestSubDealerID().equals("SUBSCRIBER")) {
					int comissionAmount = transferAmount / 10;
					transferAmount = transferAmount + comissionAmount;
					request.setTransferAmount(transferAmount);
					request.setTransactionAmountCommission(comissionAmount);
					logger.debug("TransactionAmountCommissionTransformer handler added TransactionAmountCommission for transaction: " + request.getRequestTransactionID());
				}
			}
			logger.debug("TransactionAmountCommissionTransformer handler completed for transaction: " + request.getRequestTransactionID());
			if(getNext() != null) getNext().processRequest(request, response);
		} catch(Exception e) {
			logger.error("Error in TransactionAmountCommissionTransformerHandler: " + e.getMessage());
			throw new TUMRequestHandlerException(TUMResponseCodes.SYSTEM_ERROR.getResponseCode(), e.getMessage());
		}
		
	}

}
