package mcel.tump.gateway.handler;

import mcel.tump.gateway.util.TUMGWTokens;
import mcel.tump.gateway.util.TUMPRequest;
import mcel.tump.gateway.util.TUMPResponse;
import mcel.tump.gateway.util.TUMResponseCodes;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RevertTransactionAmountCommissionTransformerHandler extends AbstractTUMRequestHandler {

	private static final Log logger = LogFactory.getLog(RevertTransactionAmountCommissionTransformerHandler.class);

	@Override
	public void processRequest(TUMPRequest request, TUMPResponse response) throws TUMRequestHandlerException {
		logger.debug("RevertTransactionAmountCommissionTransformer handler started for transaciton: " + request.getRequestTransactionID());
		try {
			if(request.getMethodName().equals(TUMGWTokens.RechargeSubscriberRequest.toString()) 
					&& !response.isFault()) {
				Integer commissionAmount = request.getTransactionAmountCommission();
				if(commissionAmount != null && commissionAmount > 0) {
					Integer transferAmount = response.getTransferAmount();
					transferAmount = transferAmount - commissionAmount;
					response.setTransferAmount(transferAmount);
					logger.debug("RevertTransactionAmountCommissionTransformer handler removed TransactionAmountCommission for transaction: " + request.getRequestTransactionID());
				}
			}
			logger.debug("RevertTransactionAmountCommissionTransformer handler completed for transaction: " + request.getRequestTransactionID());
			if(getNext() != null) getNext().processRequest(request, response);
		} catch(Exception e) {
			logger.error("Error in RevertTransactionAmountCommissionTransformerHandler: " + e.getMessage());
			throw new TUMRequestHandlerException(TUMResponseCodes.SYSTEM_ERROR.getResponseCode(), e.getMessage());
		}
		
	}
	
	
}
