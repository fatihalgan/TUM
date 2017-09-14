package com.bridge.ena.cs3cp6.test;

import java.util.Date;

import org.junit.Before;

import com.bridge.ena.cs3cp6.command.CommandFactory;
import com.bridge.ena.cs3cp6.command.GetBalanceAndDateCommand;
import com.bridge.ena.cs3cp6.command.NetmovelSupportUpdateBalanceAndDateCommand;
import com.bridge.ena.util.DateUtils;
import com.bridge.ena.xml.BadXmlFormatException;
import com.bridge.ena.xmlrpc.IXMLRPCClient;
import com.bridge.ena.xmlrpc.XMLRPCClient;
import com.bridge.ena.xmlrpc.exception.XmlRpcConnectionException;
import com.bridge.ena.xmlrpc.serializer.MethodResponse;

import junit.framework.TestCase;

public class TomalaAdjustmentTestCase extends TestCase {

	private String originTimeStamp = "20080320T16:55:05+0200";
    private IXMLRPCClient client;
    private CommandFactory factory;
    
    @Before
    public void setUp() throws Exception {
        client = new XMLRPCClient("http://192.168.40.35:10010/Air", 500, "UGw Server/3.1/1.0", "Basic dHVtX2FwcDp0dW1fYXBw", 1, 1000);
        factory = new CommandFactory(client, "MCELTUMTEST01", "258", 1);
    }
    
    public void testAdjustmentMessageWithSC34() throws XmlRpcConnectionException, BadXmlFormatException {
    	Date requestTimestamp = DateUtils.convertISO8601DateForTest(originTimeStamp);
    	GetBalanceAndDateCommand enquiry = factory.getBalanceAndDateCommand("825784759", requestTimestamp, String.valueOf(System.currentTimeMillis()), 100);
        enquiry.execute();
        Float mainAccountBalanceBefore = enquiry.getAccount().getAccountValue();
        Float dedicatedAccountBalanceBefore = enquiry.getAccount().getDedicatedAccount(3).getDedicatedAccountValue();
    	
    	NetmovelSupportUpdateBalanceAndDateCommand cmd = factory.getNetmovelSupportUpdateBalanceAndDateCommand("1234567898765", requestTimestamp, "825784759", 50f, "Test", "Test", 0, 0, 0, 0, 34, 100f);
    	MethodResponse resp = cmd.execute();
    	assertEquals(cmd.getResponseCode().intValue(), 0);
    	
    	enquiry.execute();
    	Float mainAccountBalanceAfter = enquiry.getAccount().getAccountValue();
    	Float dedicatedAccountBalanceAfter = enquiry.getAccount().getDedicatedAccount(3).getDedicatedAccountValue();
    	
    	assertEquals(mainAccountBalanceAfter, mainAccountBalanceBefore);
    	assertNotSame(dedicatedAccountBalanceAfter, dedicatedAccountBalanceBefore);
    }
}
