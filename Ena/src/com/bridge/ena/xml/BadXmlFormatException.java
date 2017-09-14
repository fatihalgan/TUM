/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.xml;

/**
 *
 * @author db2admin
 */
public class BadXmlFormatException extends Exception {

    //Fault Code for illegal request message
    private int faultCode = 1000;
    private String message = "Illegal request message";
    
    public BadXmlFormatException() {
        super();
    }

    public BadXmlFormatException(int faultCode, String message) {
        this.faultCode = faultCode;
        this.message = message;
    }
    
    public int getFaultCode() {
        return faultCode;
    }

    public String getMessage() {
        return message;
    }
    
}
