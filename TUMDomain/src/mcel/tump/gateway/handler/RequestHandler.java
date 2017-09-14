/*
 * Handler.java
 * 
 * Created on Sep 10, 2007, 12:54:35 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.gateway.handler;

import com.bridge.ena.xmlrpc.serializer.MethodCall;
import com.bridge.ena.xmlrpc.serializer.MethodResponse;

/**
 *
 * @author db2admin
 */
public interface RequestHandler {
    public void processRequest(MethodCall request, MethodResponse response) throws TUMRequestHandlerException;
    public RequestHandler getNext();
    public void setNext(RequestHandler handler);
}
