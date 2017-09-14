package com.bridge.ena.cs3cp6.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Before;

import com.bridge.ena.cs3cp6.command.CommandFactory;
import com.bridge.ena.cs3cp6.command.GenericNetmovelSupportUpdateBalanceAndDateCommand;
import com.bridge.ena.cs3cp6.command.GenericUpdateBalanceAndDateCommand;
import com.bridge.ena.cs3cp6.command.InterrogatingGenericUpdateBalanceAndDateCommand;
import com.bridge.ena.cs3cp6.command.TUMNetmovelSupportAdjustmentCommand;
import com.bridge.ena.cs3cp6.command.UpdateBalanceAndDateCommand;
import com.bridge.ena.cs3cp6.value.DedicatedAccountInformation;
import com.bridge.ena.util.DateUtils;
import com.bridge.ena.xml.BadXmlFormatException;
import com.bridge.ena.xmlrpc.IXMLRPCClient;
import com.bridge.ena.xmlrpc.XMLRPCClient;
import com.bridge.ena.xmlrpc.command.XMLRPCTokens;
import com.bridge.ena.xmlrpc.exception.XmlRpcConnectionException;
import com.bridge.ena.xmlrpc.serializer.MethodResponse;

import junit.framework.TestCase;

public class AIRAdjustmentMessageTestCase extends TestCase {

	private String originTimeStamp = "20080320T16:55:05+0200";
    String path =
        "D://NetBeansWorkspaces//MCel//TUMP//Ena//test//com//bridge//ena//cs//test//";
    private IXMLRPCClient client;
    private CommandFactory factory;
    
    @Before
    public void setUp() throws Exception {
        //client = new XMLRPCClient("http://192.168.40.35:10010/Air", 500, "UGw Server/3.1/1.0", "Basic dHVtX2FwcDp0dW1fYXBw", 1, 1000);
        client = new XMLRPCClient("http://10.255.0.154:10010/Air", 500, "TUM/4.2/1.0", "Basic dHVtdXNlcjp0dW11c2Vy", 1, 1000);
        factory = new CommandFactory(client, "MCELTUMTEST01", "258", 1);
    }
    
    
    public void testGenericAdjustmentMessage() throws XmlRpcConnectionException {
    	Date requestTimestamp = DateUtils.convertISO8601DateForTest(originTimeStamp);
    	DedicatedAccountInformation da1 = new DedicatedAccountInformation();
    	da1.setDedicatedAccountId(3);
    	da1.setDedicatedAccountValue(1.0f);
    	List<DedicatedAccountInformation> das = new ArrayList<DedicatedAccountInformation>();
    	//das.add(da1);
    	GenericUpdateBalanceAndDateCommand cmd = factory.getGenericUpdateBalanceAndDateCommand("1234567893234353322", requestTimestamp, "824066948", -50f, "Test", "", 0, 0, 100f, das);
    	cmd.execute();
        MethodResponse resp = cmd.getResponse();
        System.out.println(resp.toXML(new StringBuffer()));
        assertEquals(resp.getMemberValue(XMLRPCTokens.ResponseCode.toString()), 0);
    }
    
    
    /**
    public void testLoyaltyAdjustmentMessage() throws XmlRpcConnectionException, BadXmlFormatException {
    	Date requestTimestamp = DateUtils.convertISO8601DateForTest(originTimeStamp);
    	TUMNetmovelSupportAdjustmentCommand cmd = factory.getTUMNetmovelSupportAdjustmentCommand("1234567893234353322", requestTimestamp, "826307248", 100f, "Test", "Test", 100f);
    	MethodResponse resp = cmd.execute();
        System.out.println(resp.toXML(new StringBuffer()));
        assertEquals(resp.getMemberValue(XMLRPCTokens.ResponseCode.toString()), 0);
    }
    
    
    
    public void testGenericNetmovelBundleSupportGenericAdjustmentMessage() throws XmlRpcConnectionException {
    	Date requestTimestamp = DateUtils.convertISO8601DateForTest(originTimeStamp);
    	DedicatedAccountInformation da1 = new DedicatedAccountInformation();
    	da1.setDedicatedAccountId(1);
    	da1.setDedicatedAccountValue(10f);
    	DedicatedAccountInformation da2 = new DedicatedAccountInformation();
    	da2.setDedicatedAccountId(8);
    	da2.setDedicatedAccountValue(20f);
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(new Date());
    	cal.add(Calendar.DAY_OF_MONTH, 2);
    	da2.setExpiryDate(cal.getTime());
    	List<DedicatedAccountInformation> das = new ArrayList<DedicatedAccountInformation>();
    	das.add(da1);
    	das.add(da2);
    	GenericNetmovelSupportUpdateBalanceAndDateCommand cmd = factory.getGenericNetmovelBundleSupportUpdateBalanceAndDateCommand("1234567893234353322", requestTimestamp, "826307248", 5f, "Test", "Test", 0, 0, 2, 100f, das);
    	cmd.execute();
        MethodResponse resp = cmd.getResponse();
        System.out.println(resp.toXML(new StringBuffer()));
        assertEquals(resp.getMemberValue(XMLRPCTokens.ResponseCode.toString()), 0);
    }
    
    
    
    public void testInterrogatingGenericUpdateBalanceAndDateMessage() throws XmlRpcConnectionException, BadXmlFormatException {
    	Date requestTimestamp = DateUtils.convertISO8601DateForTest(originTimeStamp);
    	DedicatedAccountInformation adjDa1 = new DedicatedAccountInformation();
    	adjDa1.setDedicatedAccountId(1);
    	adjDa1.setDedicatedAccountValue(10f);
    	List<DedicatedAccountInformation> das = new ArrayList<DedicatedAccountInformation>();
    	das.add(adjDa1);
    	InterrogatingGenericUpdateBalanceAndDateCommand cmd = factory.getInterrogatingGenericUpdateBalanceAndDateCommand("1234567893234353322", requestTimestamp, "826307248", 10f, "Test", "Test", 0, 0, 100f, das);
    	cmd.execute();
    	System.out.println("Account Balance Before: " + cmd.getAccountBalanceBefore());
    	System.out.println("Account Balance After: " + cmd.getAccountBalanceAfter());
    	System.out.println("Service Class: " + cmd.getServiceClassCurrent());
    	System.out.println("Supervision Date Before: " + cmd.getSupervisionExpiryDateBefore());
    	System.out.println("Supervision Date After: " + cmd.getSupervisionExpiryDateAfter());
    	System.out.println("Service Fee Date Before: " + cmd.getServiceFeeExpiryDateBefore());
    	System.out.println("Service Fee Date After: " + cmd.getServiceFeeExpiryDateAfter());
    	System.out.println("Language ID: " + cmd.getLanguageIDCurrent());
    	System.out.println("SMS Account Before: " + cmd.getDedicatedAccountBefore(1).getDedicatedAccountValue().floatValue());
    	System.out.println("SMS Account After: " + cmd.getDedicatedAccountAfter(1).getDedicatedAccountValue().floatValue());
    }
    
    
    
    
    public void testChangeSupervisionPeriodMessage() throws XmlRpcConnectionException, BadXmlFormatException {
    	Date requestTimestamp = DateUtils.convertISO8601DateForTest(originTimeStamp);
    	UpdateBalanceAndDateCommand cmd = factory.getUpdateBalanceAndDateCommand("1234567893234353322", requestTimestamp, "826307248", 0f, "Test", "Test", -20, 0, 0, 0, 100f);
    	cmd.execute();
    	 MethodResponse resp = cmd.getResponse();
         System.out.println(resp.toXML(new StringBuffer()));
         assertEquals(resp.getMemberValue(XMLRPCTokens.ResponseCode.toString()), 0);
    }
    **/
    
    
}
