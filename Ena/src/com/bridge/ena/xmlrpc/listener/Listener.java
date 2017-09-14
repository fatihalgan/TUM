package com.bridge.ena.xmlrpc.listener;

import com.bridge.ena.xml.BadXmlFormatException;
import com.bridge.ena.xmlrpc.exception.XmlRpcConnectionException;
import com.bridge.ena.xmlrpc.serializer.MethodResponse;

public interface Listener {
	public MethodResponse handleRequest(String strRequest) throws BadXmlFormatException;
}
