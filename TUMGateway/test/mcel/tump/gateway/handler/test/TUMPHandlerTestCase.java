/*
 * AuthorizeTUMPRequestTestCase.java
 * 
 * Created on Sep 13, 2007, 1:01:33 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.gateway.handler.test;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.StringReader;
import mcel.tump.gateway.handler.SignatureGenerationHandler;
import mcel.tump.gateway.handler.SignatureVerificationHandler;
import mcel.tump.gateway.service.ITUMPGatewayService;
import mcel.tump.gateway.util.TUMPRequest;
import mcel.tump.gateway.util.TUMPResponse;
import com.bridge.ena.xmlrpc.serializer.Member;
import com.bridge.ena.xmlrpc.serializer.MethodCall;
import com.bridge.ena.xmlrpc.serializer.MethodResponse;
import com.bridge.ena.xmlrpc.serializer.Serializer;
import org.custommonkey.xmlunit.XMLTestCase;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 *
 * @author db2admin
 */
public class TUMPHandlerTestCase extends XMLTestCase {

    ApplicationContext ctx = null;
    ITUMPGatewayService service = null;
    String ctxpath = "D:/NetBeansWorkspaces/MCel/TUMP/TUMPManager/test/mcel/tump/gateway/handler/test/";
    SignatureVerificationHandler verifyHandler = null;
    SignatureGenerationHandler generationHandler = null;
    
    @Before
    public void setUp() throws Exception {
        super.setUp();
        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setIgnoreComments(true);
        XMLUnit.setIgnoreAttributeOrder(true);
        ctx = new FileSystemXmlApplicationContext( ctxpath + "handlerContextUnitTest.xml");
        service = (ITUMPGatewayService)ctx.getBean("tumpGatewayService");
        verifyHandler = (SignatureVerificationHandler)ctx.getBean("signatureVerificationHandler");
        generationHandler = (SignatureGenerationHandler)ctx.getBean("signatureGenerationHandler");
    }
        
    @After
    public void tearDown() throws Exception {
        super.tearDown();
        ctx = null;
        service = null;
    }
    
    @Test
    public void testAuthenticateRequest() throws Exception {
        FileInputStream file = new FileInputStream(ctxpath + "rechargeSubscriberRequest.xml");
        Serializer ser = new Serializer(file);
        MethodCall call = (MethodCall)ser.parse();
        TUMPRequest request = new TUMPRequest(call);
        TUMPResponse response = new TUMPResponse();
        //authHandler.processRequest(request, response);
        assertFalse(response.isFault());
    }
    
    @Test
    public void testAuthenticationFailureRequest() throws Exception {
        FileInputStream file = new FileInputStream(ctxpath + "rechargeSubscriberRequest.xml");
        Serializer ser = new Serializer(file);
        MethodCall call = (MethodCall)ser.parse();
        call.addMember(new Member("Password", "wrong pass"));
        TUMPRequest request = new TUMPRequest(call);
        TUMPResponse response = new TUMPResponse();
        //authHandler.processRequest(request, response);
        assertTrue(response.isFault());
        StringReader resp = new StringReader(response.toXML());
        FileReader reader = new FileReader(ctxpath + "authorizationFaultResponse.xml");
        assertXMLEqual(reader, resp);
    }
    
    @Test
    public void testCorrectSignatureRequest() throws Exception {
        FileInputStream file = new FileInputStream(ctxpath + "rechargeSubscriberRequest.xml");
        Serializer ser = new Serializer(file);
        MethodCall call = (MethodCall)ser.parse();
        TUMPRequest request = new TUMPRequest(call);
        TUMPResponse response = new TUMPResponse();
        verifyHandler.processRequest(request, response);
        assertFalse(response.isFault());
    }
    
    @Test
    public void testFalseSignatureRequest() throws Exception {
        FileInputStream file = new FileInputStream(ctxpath + "rechargeSubscriberRequest.xml");
        Serializer ser = new Serializer(file);
        MethodCall call = (MethodCall)ser.parse();
        call.addMember(new Member("RequestTimeStamp", null));
        TUMPRequest request = new TUMPRequest(call);
        TUMPResponse response = new TUMPResponse();
        verifyHandler.processRequest(request, response);
        assertTrue(response.isFault());
        StringReader resp = new StringReader(response.toXML());
        FileReader reader = new FileReader(ctxpath + "signatureFaultResponse.xml");
        assertXMLEqual(reader, resp);
    }
    
    @Test
    public void testSignatureResponse() throws Exception {
        FileInputStream file = new FileInputStream(ctxpath + "balanceCheckResponse.xml");
        Serializer ser = new Serializer(file);
        MethodCall call = new MethodCall();
        call.setMethodName("BalanceCheck");
        MethodResponse res = (MethodResponse)ser.parse();
        TUMPResponse response = new TUMPResponse(res);
        TUMPRequest request = new TUMPRequest(call);
        generationHandler.processRequest(request, response);
        StringReader resp = new StringReader(response.toXML());
        FileReader reader = new FileReader(ctxpath + "balanceCheckResponseWithSignature.xml");
        assertXMLEqual(reader, resp);
    }
}