package mcel.tump.client.mtn.test;

import java.io.FileInputStream;
import java.io.InputStream;

import org.custommonkey.xmlunit.XMLTestCase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.bridge.ena.xmlrpc.IXMLRPCClient;
import com.bridge.ena.xmlrpc.serializer.MethodResponse;
import com.bridge.ena.xmlrpc.serializer.Serializer;
import com.bridge.ena.xmlrpc.serializer.XMLRPCMessage;

public class MTNReserveVoucherTestCase extends XMLTestCase {

	ApplicationContext ctx = null;
    IXMLRPCClient xmlRpcClient = null;
    InputStream requestData = null;
    String requestPath = null;
    
    public void setUp() throws Exception {
        ctx = new FileSystemXmlApplicationContext("D:/RHDSWorkspaces/MCel/TUMP/TUMClient/test/mcel/tump/client/mtn/test/mtn_applicationContext.xml");
        xmlRpcClient = (IXMLRPCClient)ctx.getBean("xmlRpcClient");
        requestPath = "D:/RHDSWorkspaces/MCel/TUMP/TUMClient/test/mcel/tump/client/mtn/test/";
    }
    
    public void tearDown() throws Exception {
        ctx = null;
        xmlRpcClient = null;
        requestData = null;
    }
    
    
    public void testReserveVoucher() throws Exception {
    	 requestData = new FileInputStream(requestPath + "ReserveVoucherRequest.xml");
         XMLRPCMessage msg = (XMLRPCMessage)new Serializer(requestData).parse();
         String response = xmlRpcClient.sendMessage(msg.toXML(new StringBuffer()));
         System.out.println("ReserveVoucherResponse:");
         System.out.println(response);
         MethodResponse result = (MethodResponse)new Serializer(response).parse();
         assertFalse(result.isFault());
    }
    
    
    /**
    public void testCancelReserveVoucher() throws Exception {
    	requestData = new FileInputStream(requestPath + "CancelVoucherRequest.xml");
        XMLRPCMessage msg = (XMLRPCMessage)new Serializer(requestData).parse();
        String response = xmlRpcClient.sendMessage(msg.toXML(new StringBuffer()));
        System.out.println("CancelVoucherResponse:");
        System.out.println(response);
        MethodResponse result = (MethodResponse)new Serializer(response).parse();
        assertFalse(result.isFault());
    }
    **/
    
    /**
    public void testCommitReserveVoucher() throws Exception {
    	requestData = new FileInputStream(requestPath + "CommitVoucherRequest.xml");
        XMLRPCMessage msg = (XMLRPCMessage)new Serializer(requestData).parse();
        String response = xmlRpcClient.sendMessage(msg.toXML(new StringBuffer()));
        System.out.println("CancelVoucherResponse:");
        System.out.println(response);
        MethodResponse result = (MethodResponse)new Serializer(response).parse();
        assertFalse(result.isFault());
    }
    **/
}
