package mcel.tump.client.kazang.test;

import java.io.FileInputStream;
import java.io.InputStream;

import mcel.tump.util.cert.ISignatureVerifier;

import org.custommonkey.xmlunit.XMLTestCase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.bridge.ena.xmlrpc.IXMLRPCClient;
import com.bridge.ena.xmlrpc.serializer.MethodResponse;
import com.bridge.ena.xmlrpc.serializer.Serializer;
import com.bridge.ena.xmlrpc.serializer.XMLRPCMessage;

public class KazangClientTestCase extends XMLTestCase {

	ApplicationContext ctx = null;
    IXMLRPCClient xmlRpcClient = null;
    InputStream requestData = null;
    String requestPath = null;
    ISignatureVerifier signatureVerifier = null;
    
    public void setUp() throws Exception {
        ctx = new FileSystemXmlApplicationContext("D:/RHDSWorkspaces/MCel/TUMP/TUMClient/test/mcel/tump/client/kazang/test/applicationContext.xml");
        xmlRpcClient = (IXMLRPCClient)ctx.getBean("xmlRpcClient");
        requestPath = "D:/RHDSWorkspaces/MCel/TUMP/TUMClient/test/mcel/tump/client/kazang/test/";
        signatureVerifier = (ISignatureVerifier)ctx.getBean("signatureVerifier");
    }
    
    public void tearDown() throws Exception {
        ctx = null;
        xmlRpcClient = null;
        requestData = null;
        signatureVerifier = null;
    }
    
    /**
    public void testBalanceCheck() throws Exception {
    	requestData = new FileInputStream(requestPath + "BalanceCheckRequest.xml");
    	XMLRPCMessage msg = (XMLRPCMessage)new Serializer(requestData).parse();
        //generateSignature(msg);
        String response = xmlRpcClient.sendMessage(msg.toXML(new StringBuffer()));
        System.out.println("BalanceCheckResponse:");
        System.out.println(response);
        MethodResponse result = (MethodResponse)new Serializer(response).parse();
        assertFalse(result.isFault());
    }
    **/
    
    public void testRechargeSubscriber() throws Exception {
    	requestData = new FileInputStream(requestPath + "RechargeSubscriberRequest.xml");
    	XMLRPCMessage msg = (XMLRPCMessage)new Serializer(requestData).parse();
        //generateSignature(msg);
        String response = xmlRpcClient.sendMessage(msg.toXML(new StringBuffer()));
        System.out.println("RechargeSubscriberResponse:");
        System.out.println(response);
        MethodResponse result = (MethodResponse)new Serializer(response).parse();
        assertFalse(result.isFault());
    }
}
