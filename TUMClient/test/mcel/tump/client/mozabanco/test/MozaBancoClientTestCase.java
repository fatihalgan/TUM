package mcel.tump.client.mozabanco.test;

import java.io.FileInputStream;
import java.io.InputStream;

import mcel.tump.gateway.util.TUMPRequest;
import mcel.tump.util.cert.ISignatureVerifier;

import org.custommonkey.xmlunit.XMLTestCase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.bridge.ena.util.DateUtils;
import com.bridge.ena.xmlrpc.IXMLRPCClient;
import com.bridge.ena.xmlrpc.serializer.MethodCall;
import com.bridge.ena.xmlrpc.serializer.MethodResponse;
import com.bridge.ena.xmlrpc.serializer.Serializer;
import com.bridge.ena.xmlrpc.serializer.XMLRPCMessage;

public class MozaBancoClientTestCase extends XMLTestCase {

	ApplicationContext ctx = null;
    IXMLRPCClient xmlRpcClient = null;
    InputStream requestData = null;
    String requestPath = null;
    ISignatureVerifier signatureVerifier = null;
    
    public void setUp() throws Exception {
        ctx = new FileSystemXmlApplicationContext("D:/RHDSWorkspaces/MCel/TUMP/TUMClient/test/mcel/tump/client/mozabanco/test/applicationContext.xml");
        xmlRpcClient = (IXMLRPCClient)ctx.getBean("xmlRpcClient");
        requestPath = "D:/RHDSWorkspaces/MCel/TUMP/TUMClient/test/mcel/tump/client/mozabanco/test/";
        signatureVerifier = (ISignatureVerifier)ctx.getBean("signatureVerifier");
    }
    
    public void tearDown() throws Exception {
        ctx = null;
        xmlRpcClient = null;
        requestData = null;
        signatureVerifier = null;
    }
    
    
    public void testRechargeSubscriber() throws Exception {
        requestData = new FileInputStream(requestPath + "RechargeSubscriberRequest.xml");
        XMLRPCMessage msg = (XMLRPCMessage)new Serializer(requestData).parse();
        generateSignature(msg);
        String response = xmlRpcClient.sendMessage(msg.toXML(new StringBuffer()));
        System.out.println("RechargeSubscriberResponse:");
        System.out.println(response);
        MethodResponse result = (MethodResponse)new Serializer(response).parse();
        assertFalse(result.isFault());
    }
    
    /**
    public void testCheckBalance() throws Exception {
    	requestData = new FileInputStream(requestPath + "CheckBalanceRequest.xml");
        XMLRPCMessage msg = (XMLRPCMessage)new Serializer(requestData).parse();
        String response = xmlRpcClient.sendMessage(msg.toXML(new StringBuffer()));
        System.out.println("CheckBalanceResponse:");
        System.out.println(response);
        MethodResponse result = (MethodResponse)new Serializer(response).parse();
        assertFalse(result.isFault());
    }
    **/
    
    public void generateSignature(XMLRPCMessage msg) {
    	TUMPRequest request = new TUMPRequest((MethodCall)msg);
        String signatureData = generateSignatureData(request);
        byte[] signature = signatureVerifier.sign(signatureData);
        request.setSignature(signature);
    }
    
    public String generateSignatureData(TUMPRequest request) {
    	String signatureData = "";
        if(request.getUsername() != null) signatureData = signatureData + request.getUsername();
        if(request.getPassword() != null) signatureData = signatureData + request.getPassword();
        if(request.getRequestTransactionID() != null) signatureData = signatureData + request.getRequestTransactionID();
        if(request.getRequestDealerID() != null) signatureData = signatureData + request.getRequestDealerID();
        if(request.getRequestSubDealerID() != null) signatureData = signatureData + request.getRequestSubDealerID();
        if(request.getSubscriberMSISDN() != null) signatureData = signatureData + request.getSubscriberMSISDN();
        if(request.getTransferAmount() != null) signatureData = signatureData + request.getTransferAmount();
        if(request.getRequestTimeStamp() != null) signatureData = signatureData + DateUtils.convertISO8601String(request.getRequestTimeStamp());
        return signatureData;
    }
}
