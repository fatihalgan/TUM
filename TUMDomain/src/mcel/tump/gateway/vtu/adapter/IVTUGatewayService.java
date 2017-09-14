/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.gateway.vtu.adapter;

import mcel.tump.gateway.handler.TUMRequestHandlerException;

import com.bridge.ena.xmlrpc.exception.XmlRpcConnectionException;
import com.bridge.ena.xmlrpc.serializer.MethodCall;
import com.bridge.ena.xmlrpc.serializer.MethodResponse;

/**
 *
 * @author db2admin
 */
public interface IVTUGatewayService {

    public MethodResponse balanceCheck(MethodCall vtuRequest) throws XmlRpcConnectionException, TUMRequestHandlerException;
    public MethodResponse rechargeSubscriber(MethodCall vtuRequest) throws XmlRpcConnectionException, TUMRequestHandlerException;
    public MethodResponse reserveVoucher(MethodCall vtuRequest) throws XmlRpcConnectionException, TUMRequestHandlerException;
    public MethodResponse endVoucherReservation(MethodCall vtuRequest) throws XmlRpcConnectionException, TUMRequestHandlerException;

}
