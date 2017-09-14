/*
 * Handler.java
 * 
 * Created on Sep 10, 2007, 12:54:35 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.gateway.handler;

import mcel.tump.gateway.util.TUMPRequest;
import mcel.tump.gateway.util.TUMPResponse;

/**
 *
 * @author db2admin
 */
public interface RequestHandler {
    public void processRequest(TUMPRequest request, TUMPResponse response) throws TUMRequestHandlerException;
    public RequestHandler getNext();
    public void setNext(RequestHandler handler);
}
