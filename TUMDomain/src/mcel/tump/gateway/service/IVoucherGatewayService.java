package mcel.tump.gateway.service;

import mcel.tump.gateway.util.TUMRechargeRequest;
import mcel.tump.gateway.util.TUMRechargeResponse;

public interface IVoucherGatewayService {

	public void reserveVoucher(TUMRechargeRequest request, TUMRechargeResponse response);
	
	public void commitReserveVoucher(TUMRechargeRequest request, TUMRechargeResponse response);
	
	public void cancelReserveVoucher(TUMRechargeRequest request, TUMRechargeResponse response);
}
