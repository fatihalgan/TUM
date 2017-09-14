/*
 * CSConnectionException.java
 * 
 * Created on Aug 30, 2007, 5:05:53 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.xmlrpc.exception;

/**
 *
 * @author db2admin
 */
public class XmlRpcConnectionException extends Exception {

    private int statusCode;
    
    public XmlRpcConnectionException() {
        super();
    }
    
    public XmlRpcConnectionException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
    
    public int getStatusCode() {
        return statusCode;
    }
}
