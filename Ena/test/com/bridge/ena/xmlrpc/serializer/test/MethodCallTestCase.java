package com.bridge.ena.xmlrpc.serializer.test;

import java.util.Date;
import org.junit.After;
import org.junit.Before;
import com.bridge.ena.cs3cp1.command.AdjustmentTCommand;
import com.bridge.ena.cs3cp1.command.BalanceEnquiryTCommand;
import com.bridge.ena.cs3cp1.command.CommandFactory;
import com.bridge.ena.cs3cp1.command.GetAccountDetailsTCommand;
import com.bridge.ena.util.DateUtils;
import com.bridge.ena.xmlrpc.IXMLRPCClient;
import com.bridge.ena.xmlrpc.test.MockXMLRPCClient;
import org.custommonkey.xmlunit.XMLTestCase;
import org.custommonkey.xmlunit.XMLUnit;

public class MethodCallTestCase extends XMLTestCase {

    private IXMLRPCClient client;
    private CommandFactory factory;
    String path = "D://NetBeansWorkspaces//MCel//TUMP//Ena//test//com//bridge//ena//xmlrpc//serializer//test//";
        
    @Before
    public void setUp() throws Exception {
        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setIgnoreComments(true);
        XMLUnit.setIgnoreAttributeOrder(true);
        factory = new CommandFactory(client, "MCELTUMTEST01", "", 1);
    }

    @After
    public void tearDown() throws Exception {
    }

    public void testGenerateAdjustmentRequest() throws Exception {
        Date originTimestamp = DateUtils.convertISO8601DateForTest("20070903T16:55:05+0300");
        AdjustmentTCommand cmd = factory.getAdjustmentTCommand("12332432432", originTimestamp, "826307248", 53750.0f, "500", "3434543534", -1, -1, 2, 1, 1);
        cmd.setOriginHostName("MCELTUMTEST01");
        cmd.prepareRequest();
        client = new MockXMLRPCClient(path, "adjustmentRequest.xml", this);
        client.sendMessage(cmd.getRequest().toXML(new StringBuffer()));
    }

    public void testGenerateBalanceEnquiryRequest() throws Exception {
        Date originTimestamp = DateUtils.convertISO8601DateForTest("20070903T18:01:28+0300");
        BalanceEnquiryTCommand cmd = factory.getBalanceEnquiryTCommand("821234567", originTimestamp, "34656554", 1);
        cmd.setOriginHostName("MCELTUMTEST01");
        cmd.prepareRequest();
        client = new MockXMLRPCClient(path, "balanceEnquiryRequest.xml", this);
        client.sendMessage(cmd.getRequest().toXML(new StringBuffer()));
    }
    
    public void testGenerateGetAccountDetailsRequest() throws Exception {
        Date originTimestamp = DateUtils.convertISO8601DateForTest("20070903T18:11:35+0300");
        GetAccountDetailsTCommand cmd = factory.getAccountDetailsTCommand("12345765", originTimestamp, "821234567", 1);
        cmd.setOriginHostName("MCELTUMTEST01");
        cmd.prepareRequest();
        client = new MockXMLRPCClient(path, "accountDetailsRequest.xml", this);
        client.sendMessage(cmd.getRequest().toXML(new StringBuffer()));
    }
}
