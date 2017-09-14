/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.xmlrpc.serializer.test;

import java.io.FileInputStream;
import java.io.InputStream;
import junit.framework.TestCase;
import com.bridge.ena.xmlrpc.IXMLRPCClient;
import com.bridge.ena.xmlrpc.XMLRPCClient;
import com.bridge.ena.xmlrpc.serializer.MethodCall;
import com.bridge.ena.xmlrpc.serializer.MethodResponse;
import com.bridge.ena.xmlrpc.serializer.Serializer;
import com.bridge.ena.xmlrpc.command.XMLRPCTokens;
import org.junit.Before;

/**
 *
 * @author db2admin
 */
public class XMLRPCClientTestCase extends TestCase {

    private IXMLRPCClient client;
    private String originTimeStamp = "20070903T16:55:05+0200";
    String path =
        "D://NetBeansWorkspaces//MCel//TUMP//Ena//test//com//bridge//ena//xmlrpc//serializer//test//";
    
    @Before
    public void setUp() throws Exception {
        client = new XMLRPCClient("http://192.168.40.31:10010/Air", 12000, "UGw Server/2.1/1.0", 5, 3000);
    }
    
    public void testAdjustmentRequest() throws Exception {
        InputStream reader = new FileInputStream(path + "adjustmentRequest.xml");
        Serializer ser = new Serializer(reader);
        MethodCall call = (MethodCall)ser.parse();
        String response = client.sendMessage(call.toXML(new StringBuffer()));
        ser = new Serializer(response);
        MethodResponse resp = (MethodResponse)ser.parse();
        if(resp.getFault() != null) {
            System.out.println("Fault:" + resp.getFault().getFault().toXML(new StringBuffer()));
            assertTrue(1 == 2);
            return;
        }
        Integer responseCode = (Integer)resp.getMember(XMLRPCTokens.ResponseCode.toString()).getValue();
        assertTrue(responseCode < 100);
    }
}
