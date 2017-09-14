package com.bridge.ena.xmlrpc.listener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpStatus;

import com.bridge.ena.xml.BadXmlFormatException;
import com.bridge.ena.xmlrpc.serializer.MethodCall;
import com.bridge.ena.xmlrpc.serializer.MethodResponse;

public abstract class AbstractListener implements Listener {

	protected MethodCall request;
    protected MethodResponse response = new MethodResponse();
    protected int httpStatusCode = HttpStatus.SC_OK;
    private static Log logger = LogFactory.getLog(AbstractListener.class);
    
    public abstract MethodResponse handleRequest(String strRequest) throws BadXmlFormatException;
        
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
