/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.xmlrpc.command;

import com.bridge.ena.xmlrpc.exception.XmlRpcConnectionException;
import com.bridge.ena.xmlrpc.serializer.MethodResponse;
import com.bridge.ena.xml.BadXmlFormatException;

/**
 *
 * @author db2admin
 */
public interface Command {
    
    public MethodResponse execute() throws XmlRpcConnectionException, BadXmlFormatException;
    
}
