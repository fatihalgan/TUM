/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.xmlrpc.test;

import com.bridge.ena.xmlrpc.IXMLRPCClient;
import com.bridge.ena.xmlrpc.exception.XmlRpcConnectionException;
import java.io.FileReader;
import java.io.StringReader;
import org.custommonkey.xmlunit.XMLTestCase;

/**
 *
 * @author db2admin
 */
public class MockXMLRPCClient implements IXMLRPCClient{

    private String requestFilePath;
    private String requestFile;
    private XMLTestCase testCase;
    
    public MockXMLRPCClient(String requestFilePath, String requestFile, XMLTestCase testCase) {
        super();
        this.requestFilePath = requestFilePath;
        this.requestFile = requestFile;
        this.testCase = testCase;
    }
    
    public MockXMLRPCClient(String requestFilePath, XMLTestCase testCase) {
        super();
        this.requestFilePath = requestFilePath;
        this.testCase = testCase;
    }
    
    public String sendMessage(String xmlMessage) throws XmlRpcConnectionException {
        StringReader reader = new StringReader(xmlMessage);
        System.out.println(xmlMessage);
        try {
            FileReader freader = new FileReader(requestFilePath + requestFile);
            testCase.assertXMLEqual(reader, freader);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void setAuthentication(String username, String password) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getRequestFilePath() {
        return requestFilePath;
    }

    public void setRequestFilePath(String requestFilePath) {
        this.requestFilePath = requestFilePath;
    }

    public String getRequestFile() {
        return requestFile;
    }

    public void setRequestFile(String requestFile) {
        this.requestFile = requestFile;
    }
    
}
