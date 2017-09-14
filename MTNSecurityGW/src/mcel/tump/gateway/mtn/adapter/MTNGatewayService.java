package mcel.tump.gateway.mtn.adapter;

import com.bridge.ena.xmlrpc.exception.XmlRpcConnectionException;
import com.bridge.ena.xmlrpc.serializer.MethodCall;
import com.bridge.ena.xmlrpc.serializer.MethodResponse;

import mcel.tump.gateway.handler.TUMRequestHandlerException;
import mcel.tump.gateway.service.chain.VTURequestProcessor;
import mcel.tump.gateway.vtu.adapter.IVTUGatewayService;

public class MTNGatewayService implements IVTUGatewayService {

	private VTURequestProcessor refillProcessor;
	
	public VTURequestProcessor getRefillProcessor() {
        return refillProcessor;
    }

    public void setRefillProcessor(VTURequestProcessor refillProcessor) {
        this.refillProcessor = refillProcessor;
    }

	@Override
	public MethodResponse balanceCheck(MethodCall vtuRequest) {
		return null;
	}

	@Override
	public MethodResponse rechargeSubscriber(MethodCall vtuRequest) throws XmlRpcConnectionException, TUMRequestHandlerException {
		return getRefillProcessor().process(vtuRequest);
	}
	
	@Override
	public MethodResponse reserveVoucher(MethodCall vtuRequest) throws XmlRpcConnectionException, TUMRequestHandlerException {
		return getRefillProcessor().process(vtuRequest);
	}
	
	@Override
	public MethodResponse endVoucherReservation(MethodCall vtuRequest) throws XmlRpcConnectionException, TUMRequestHandlerException {
		return getRefillProcessor().process(vtuRequest);
	}
    
}
