package mcel.tump.gateway.service;

import mcel.tump.gateway.service.chain.RechargeRequestProcessor;
import mcel.tump.gateway.service.chain.TUMRechargeFaultResponseException;
import mcel.tump.gateway.util.TUMRechargeRequest;
import mcel.tump.gateway.util.TUMRechargeResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class VoucherGatewayService implements IVoucherGatewayService {

	 private static final Log logger = LogFactory.getLog(VoucherGatewayService.class);
	 
	 private RechargeRequestProcessor rechargeRequestProcessor;
	 
	 public VoucherGatewayService() {
		 super();
	 }
	 
	 public RechargeRequestProcessor getRechargeRequestProcessor() {
		 return rechargeRequestProcessor;
	 }

	 public void setRechargeRequestProcessor(RechargeRequestProcessor rechargeRequestProcessor) {
		 this.rechargeRequestProcessor = rechargeRequestProcessor;
	 }

	@Override
	public void reserveVoucher(TUMRechargeRequest request, TUMRechargeResponse response) {
		logger.debug("Will perform a Reserve Voucher operation for transaction: " + request.getRequestTransactionID());
    	getRechargeRequestProcessor().process(request, response);
	}

	@Override
	public void commitReserveVoucher(TUMRechargeRequest request, TUMRechargeResponse response) {
		logger.debug("Will perform a Commit Reserve Voucher operation for transaction: " + request.getRequestTransactionID());
    	getRechargeRequestProcessor().process(request, response);		
	}

	@Override
	public void cancelReserveVoucher(TUMRechargeRequest request, TUMRechargeResponse response) {
		logger.debug("Will perform a Cancel Reserve Voucher operation for transaction: " + request.getRequestTransactionID());
    	getRechargeRequestProcessor().process(request, response);		
	}
	
	
	 
	 
}
