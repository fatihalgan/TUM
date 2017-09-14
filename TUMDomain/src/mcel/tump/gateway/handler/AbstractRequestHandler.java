/*
 * AbstractRequestHandler.java
 * 
 * Created on Sep 10, 2007, 1:12:23 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.gateway.handler;

/**
 *
 * @author db2admin
 */
public abstract class AbstractRequestHandler implements RequestHandler {

    private RequestHandler next;
    
    public RequestHandler getNext() {
        return next;
    }

    public void setNext(RequestHandler handler) {
        this.next = handler;
    }
}
