/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.gateway.vtu.adapter;

import com.bridge.ena.xmlrpc.exception.XmlRpcConnectionException;
import com.bridge.ena.xmlrpc.serializer.MethodCall;
import com.bridge.ena.xmlrpc.serializer.MethodResponse;

import mcel.tump.gateway.handler.TUMRequestHandlerException;
import mcel.tump.gateway.service.chain.VTURequestProcessor;

/**
 *
 * @author db2admin
 */
public class VTUGatewayService implements IVTUGatewayService {

    private VTURequestProcessor balanceEnquiryProcessor;
    private VTURequestProcessor refillProcessor;

    public MethodResponse balanceCheck(MethodCall vtuRequest) throws XmlRpcConnectionException, TUMRequestHandlerException {
        return getBalanceEnquiryProcessor().process(vtuRequest);
    }

    public MethodResponse rechargeSubscriber(MethodCall vtuRequest) throws XmlRpcConnectionException, TUMRequestHandlerException {
        return getRefillProcessor().process(vtuRequest);
    }
    /**
     * @return the balanceEnquiryProcessor
     */
    public VTURequestProcessor getBalanceEnquiryProcessor() {
        return balanceEnquiryProcessor;
    }

    /**
     * @param balanceEnquiryProcessor the balanceEnquiryProcessor to set
     */
    public void setBalanceEnquiryProcessor(VTURequestProcessor balanceEnquiryProcessor) {
        this.balanceEnquiryProcessor = balanceEnquiryProcessor;
    }

    /**
     * @return the refillProcessor
     */
    public VTURequestProcessor getRefillProcessor() {
        return refillProcessor;
    }

    /**
     * @param refillProcessor the refillProcessor to set
     */
    public void setRefillProcessor(VTURequestProcessor refillProcessor) {
        this.refillProcessor = refillProcessor;
    }

	@Override
	public MethodResponse reserveVoucher(MethodCall vtuRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MethodResponse endVoucherReservation(MethodCall vtuRequest) {
		// TODO Auto-generated method stub
		return null;
	}

}
