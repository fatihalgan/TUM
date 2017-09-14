/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.xmlrpc.command;

/**
 *
 * @author db2admin
 */
public enum XMLRPCTokens {

    FaultCode("faultCode"),
    FaultString("faultString"),
    ResponseCode("responseCode");

    private final String text;

    private XMLRPCTokens(String text) {
        this.text = text;
    }

    public String toString() {
        return text;
    }

}
