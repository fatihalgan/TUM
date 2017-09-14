/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.xmlrpc.command;

import com.bridge.ena.xmlrpc.IXMLRPCClient;
import com.bridge.ena.xmlrpc.exception.XmlRpcConnectionException;
import com.bridge.ena.xmlrpc.serializer.MethodCall;
import com.bridge.ena.xmlrpc.serializer.MethodResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpStatus;


/**
 *
 * @author db2admin
 */
public abstract class AbstractCommand implements Command {

    protected MethodCall request = new MethodCall();
    protected MethodResponse response;
    private IXMLRPCClient xmlRpcClient;
    protected int httpStatusCode = HttpStatus.SC_OK;
    private static Log logger = LogFactory.getLog(AbstractCommand.class);
    
    public abstract void prepareRequest();
        
    public abstract MethodResponse execute() throws XmlRpcConnectionException; 

    public IXMLRPCClient getXMLRPCClient() {
        return xmlRpcClient;
    }
    
    public void setXMLRPCClient(IXMLRPCClient xmlRpcClient) {
        this.xmlRpcClient = xmlRpcClient;
    }
    
    
    
    public MethodResponse getResponse() {
        return response;
    }
    
    public MethodCall getRequest() {
        return request;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }
    
}
