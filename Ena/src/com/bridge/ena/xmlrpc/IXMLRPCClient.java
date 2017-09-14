/*
 * IXMLRPCClient.java
 * 
 * Created on Aug 29, 2007, 1:36:18 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.xmlrpc;

import com.bridge.ena.xmlrpc.exception.XmlRpcConnectionException;


/**
 *
 * @author db2admin
 */
public interface IXMLRPCClient {

     public String sendMessage(String xmlMessage) throws XmlRpcConnectionException;
     public void setAuthentication(String username, String password);
}
