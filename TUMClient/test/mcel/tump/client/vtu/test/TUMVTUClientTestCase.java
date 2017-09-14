/*
 * TUMClientTestCase.java
 * 
 * Created on Sep 21, 2007, 10:29:41 AM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.client.vtu.test;

import java.io.FileInputStream;
import java.io.InputStream;
import com.bridge.ena.xmlrpc.IXMLRPCClient;
import com.bridge.ena.xmlrpc.serializer.MethodResponse;
import com.bridge.ena.xmlrpc.serializer.Serializer;
import com.bridge.ena.xmlrpc.serializer.XMLRPCMessage;
import org.custommonkey.xmlunit.XMLTestCase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 *
 * @author db2admin
 */
public class TUMVTUClientTestCase extends XMLTestCase {
    ApplicationContext ctx = null;
    IXMLRPCClient xmlRpcClient = null;
    InputStream requestData = null;
    String requestPath = null;    
    
    public void setUp() throws Exception {
        ctx = new FileSystemXmlApplicationContext("D:/RHDSWorkspaces/MCel/TUMP/TUMClient/test/mcel/tump/client/vtu/test/vtu_applicationContext.xml");
        xmlRpcClient = (IXMLRPCClient)ctx.getBean("xmlRpcClient");
        requestPath = "D:/RHDSWorkspaces/MCel/TUMP/TUMClient/test/mcel/tump/client/vtu/test/";
    }
    
    public void tearDown() throws Exception {
        ctx = null;
        xmlRpcClient = null;
        requestData = null;
    }

    /**
    public void testBalanceEnquiry() throws Exception {
        requestData = new FileInputStream(requestPath + "balanceEnquiryRequest.xml");
        XMLRPCMessage msg = (XMLRPCMessage)new Serializer(requestData).parse();
        String response = xmlRpcClient.sendMessage(msg.toXML(new StringBuffer()));
        System.out.println("BalanceEnquiryResponse:");
        System.out.println(response);
        MethodResponse result = (MethodResponse)new Serializer(response).parse();
        assertFalse(result.isFault());
    }
    **/
    /**
    public void testBalanceEnquiryWithHttpClient() throws Exception {
        requestData = new FileInputStream(requestPath + "balanceEnquiryRequest.xml");
        int i;
        StringBuffer requestBody = new StringBuffer();
        while((i = requestData.read()) != -1) {
            requestBody.append((char)i);
        }
        PostMethod postMethod = new PostMethod("http://localhost:8080/Air");
        postMethod.setRequestHeader("Content-Length", String.valueOf(requestBody.length()));
        postMethod.setRequestHeader("Content-Type", "text/xml");
        postMethod.setRequestHeader("Host", "ws2258:10010");
        postMethod.setRequestHeader("User-Agent", "UGw Server/2.0.03/1.0");
        postMethod.setRequestHeader("Authorization", "Basic dnRtOnZ0bTEyMw==");
        RequestEntity entity = new StringRequestEntity(requestBody.toString());
        postMethod.setRequestEntity(entity);
        HttpClient httpClient = new HttpClient();
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(500);
        HttpMethodExecutor executor = new HttpMethodExecutor(httpClient, 1, 1000);
        int status = executor.executeMethod(postMethod);
        String response = postMethod.getResponseBodyAsString();
        System.out.println(response);
    }
    **/
    
    public void testRechargeSubscriber() throws Exception {
        requestData = new FileInputStream(requestPath + "rechargeSubscriberRequest.xml");
        XMLRPCMessage msg = (XMLRPCMessage)new Serializer(requestData).parse();
        String response = xmlRpcClient.sendMessage(msg.toXML(new StringBuffer()));
        System.out.println("RechargeSubscriberResponse:");
        System.out.println(response);
        MethodResponse result = (MethodResponse)new Serializer(response).parse();
        assertFalse(result.isFault());
    }
    

    
    /**
    public void testMandatoryFieldMissingRechargeSubscriber() throws Exception {
        requestData = new FileInputStream(requestPath + "mandatoryFieldMissingRechargeRequest.xml");
        XMLRPCMessage msg = (XMLRPCMessage)new Serializer(requestData).parse();
        String response = xmlRpcClient.sendMessage(msg.toXML(new StringBuffer()));
        System.out.println("MandatoryFieldMissingRechargeSubscriberResponse:");
        System.out.println(response);
        MethodResponse result = (MethodResponse)new Serializer(response).parse();
        assertTrue(result.isFault());
    }
    **/
}
