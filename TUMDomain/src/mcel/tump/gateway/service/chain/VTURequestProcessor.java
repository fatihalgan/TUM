/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.gateway.service.chain;

import mcel.tump.gateway.handler.TUMRequestHandlerException;

import com.bridge.ena.xmlrpc.exception.XmlRpcConnectionException;
import com.bridge.ena.xmlrpc.serializer.MethodCall;
import com.bridge.ena.xmlrpc.serializer.MethodResponse;

/**
 *
 * @author db2admin
 */
public interface VTURequestProcessor {

    public MethodResponse process(MethodCall request) throws XmlRpcConnectionException, TUMRequestHandlerException;
}
