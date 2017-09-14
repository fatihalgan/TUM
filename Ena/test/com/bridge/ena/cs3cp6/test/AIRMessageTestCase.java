/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.cs3cp6.test;

import junit.framework.TestCase;
import com.bridge.ena.xmlrpc.IXMLRPCClient;
import com.bridge.ena.xmlrpc.XMLRPCClient;
import com.bridge.ena.cs3cp6.command.CommandFactory;
import com.bridge.ena.cs3cp6.command.GetAccountDetailsCommand;
import com.bridge.ena.cs3cp6.command.GetAllowedServiceClassChangesCommand;
import com.bridge.ena.cs3cp6.command.GetBalanceAndDateCommand;
import com.bridge.ena.cs3cp6.command.GetFaFListCommand;
import com.bridge.ena.cs3cp6.command.UpdateBalanceAndDateCommand;
import com.bridge.ena.cs3cp6.command.UpdateBalanceAndDateWithFreeCallsCommand;
import com.bridge.ena.cs3cp6.command.UpdateDedicatedAccountBalanceAndDateCommand;
import com.bridge.ena.cs3cp6.command.UpdateFaFListCommand;
import com.bridge.ena.cs3cp6.command.UpdateServiceClassCommand;
import com.bridge.ena.cs.value.FAFNumber;
import com.bridge.ena.cs3cp6.value.FAFAction;
import com.bridge.ena.xmlrpc.exception.XmlRpcConnectionException;
import com.bridge.ena.util.DateUtils;
import com.bridge.ena.xmlrpc.serializer.MethodResponse;
import com.bridge.ena.xmlrpc.command.XMLRPCTokens;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.joda.time.MutablePeriod;
import org.joda.time.PeriodType;
import org.junit.Before;

/**
 *
 * @author db2admin
 */
public class AIRMessageTestCase extends TestCase {

    private String originTimeStamp = "20080320T16:55:05+0200";
    String path =
        "D://NetBeansWorkspaces//MCel//TUMP//Ena//test//com//bridge//ena//cs//test//";
    private IXMLRPCClient client;
    private CommandFactory factory;

    @Before
    public void setUp() throws Exception {
        //client = new XMLRPCClient("http://192.168.40.35:10010/Air", 500, "UGw Server/3.1/1.0", "Basic dHVtX2FwcDp0dW1fYXBw", 1, 1000);
        //client = new XMLRPCClient("http://10.255.0.154:10010/Air", 500, "TUM/4.2/1.0", "Basic dHVtdXNlcjp0dW11c2Vy", 1, 1000);
    	client = new XMLRPCClient("http://10.255.0.163:10010/Air", 5, "TUM/4.2/1.0", "Basic dHVtdXNlcjp0dW11c2Vy", 1, 1000);
        factory = new CommandFactory(client, "MCELTUMTEST01MKESH", "258", 1);
    }

    
    public void testGetBalanceAndDateMessage() throws XmlRpcConnectionException {
        Date requestTimestamp = DateUtils.convertISO8601DateForTest(originTimeStamp);
        GetBalanceAndDateCommand cmd = factory.getBalanceAndDateCommand("826307248", requestTimestamp, "34656554", 100);
        cmd.execute();
        MethodResponse resp = cmd.getResponse();
        System.out.println(resp.toXML(new StringBuffer()));
        assertEquals(resp.getMemberValue(XMLRPCTokens.ResponseCode.toString()), 0);
        System.out.println("Account Value: " + cmd.getAccount().getAccountValue());
        System.out.println("SupervisionDate: " + cmd.getAccount().getSupervisionExpiryDate());
        System.out.println("ServiceFeeDate: " + cmd.getAccount().getServiceFeeExpiryDate());
        System.out.println("ServiceClassCurrent: " + cmd.getAccount().getServiceClassCurrent());
        for(int i = 0; i < 10; i++) {
        	System.out.println("Dedicated Account " + (i+1) + ": ");
        	System.out.println(cmd.getDedicatedAccountValue(100f, (i+1)));
        }
    }
    
    
    
    public void testGetAccountDetailsMessage() throws XmlRpcConnectionException {
        Date requstTimestamp = DateUtils.convertISO8601DateForTest(originTimeStamp);
        GetAccountDetailsCommand cmd = factory.getAccountDetailsCommand("12345765", requstTimestamp,
        "826307248", 100);
        cmd.execute();
        MethodResponse resp = cmd.getResponse();
        assertEquals(resp.getMemberValue(XMLRPCTokens.ResponseCode.toString()), 0);
    }
    
	
    
    public void testNegativeAdjustmentMessage() throws XmlRpcConnectionException {
        UpdateBalanceAndDateCommand adjustment = factory.getUpdateBalanceAndDateCommand("50278", new Date(), "826307248", new Float("10.0"), "sysadm", "Reset", 0, 0, new Integer("0"), new Integer("0"), 100f);
        adjustment.execute();
        assertTrue(adjustment.getResponseCode() < 100);
    }
	

    public void testMakeAdjustmentBalanceCheckMessage() throws XmlRpcConnectionException {
        Date requestTimestamp = DateUtils.convertISO8601DateForTest(originTimeStamp);
        GetBalanceAndDateCommand enquiry = factory.getBalanceAndDateCommand("826307248", requestTimestamp, String.valueOf(System.currentTimeMillis()), 100);
        enquiry.execute();
        Float balanceBefore = enquiry.getAccount().getAccountValue();
        Date supervisionBefore = enquiry.getAccount().getSupervisionExpiryDate();
        Date serviceFeeBefore = enquiry.getAccount().getServiceFeeExpiryDate();
        
        UpdateBalanceAndDateCommand adjustment = factory.getUpdateBalanceAndDateCommand("988657301", requestTimestamp, "822902341", 8f, "", "SDP 10 Test", 2, 2, 2, 2, 100f);
        adjustment.execute();
        enquiry.execute();
        Float balanceAfter = enquiry.getAccount().getAccountValue();
        assertEquals(balanceAfter - balanceBefore, 20f);
        
        //MutablePeriod period = new MutablePeriod(supervisionBefore.getTime(), enquiry.getSupervisionExpiryDate().getTime(), PeriodType.days());
        //assertEquals(period.getDays(), 1);
        //period = new MutablePeriod(serviceFeeBefore.getTime(), enquiry.getServiceFeeExpiryDate().getTime(), PeriodType.days());
        //assertEquals(period.getDays(), 1);
    }
    
    
    
    public void testMakeAdjustmentWithFreeCalls() throws XmlRpcConnectionException {
    	Date requestTimestamp = DateUtils.convertISO8601DateForTest(originTimeStamp);
        GetBalanceAndDateCommand enquiry = factory.getBalanceAndDateCommand("826307248", requestTimestamp, String.valueOf(System.currentTimeMillis()), 100);
        enquiry.execute();
        Float freeCallsBalanceBefore = enquiry.getDedicatedAccountValue(100f, 4);
       
        
        UpdateBalanceAndDateWithFreeCallsCommand adjustment = factory.getUpdateBalanceAndDateWithFreeCallsCommand("98865730", requestTimestamp, "820579029", 20f, "Test For DWS Fix 1", "Test For DWS Fix 2", 0, 0, 0, 0, 15, 100f);
        adjustment.execute();
        enquiry.execute();
        Float freeCallsBalanceAfter = enquiry.getDedicatedAccountValue(100f, 4);
        assertEquals(freeCallsBalanceAfter - freeCallsBalanceBefore, 15f);
        
        //MutablePeriod period = new MutablePeriod(supervisionBefore.getTime(), enquiry.getSupervisionExpiryDate().getTime(), PeriodType.days());
        //assertEquals(period.getDays(), 1);
        //period = new MutablePeriod(serviceFeeBefore.getTime(), enquiry.getServiceFeeExpiryDate().getTime(), PeriodType.days());
        //assertEquals(period.getDays(), 1);
    }
    
    
    public void testMakeDedicatedAccountAdjustmentMessage() throws XmlRpcConnectionException {
    	Date requestTimestamp = new Date(System.currentTimeMillis());
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(requestTimestamp);
    	cal.roll(Calendar.DATE, true);
    	Date expiryDate = cal.getTime();
    	UpdateDedicatedAccountBalanceAndDateCommand cmd = factory.getUpdateDedicatedAccountBalanceAndDateCommand("98823658", requestTimestamp, "826307248", 49f, "SDP10 Test", "SDP10 Test", 0, 0, 3, null, 100f);
    	cmd.execute();
    	assertTrue(cmd.getResponseCode() < 100);
    }
    
    
    
    public void testGetAllowedServiceClassChangesTCommand() throws XmlRpcConnectionException {
    	Date requstTimestamp = DateUtils.convertISO8601DateForTest(originTimeStamp);
    	GetAllowedServiceClassChangesCommand cmd = factory.getAllowedServiceClassChangesCommand("12345765", requstTimestamp,
        "826307248");
        cmd.execute();
        MethodResponse resp = cmd.getResponse();
        assertEquals(resp.getMemberValue(XMLRPCTokens.ResponseCode.toString()), 0);
    }
    
    
    
    
    public void testUpdateServiceClassMessage() throws XmlRpcConnectionException {
    	Date requstTimestamp = DateUtils.convertISO8601DateForTest(originTimeStamp);
        UpdateServiceClassCommand cmd = factory.getUpdateServiceClassCommand("12345765", requstTimestamp,
        "826307248", 9);
        cmd.execute();
        MethodResponse resp = cmd.getResponse();
        assertEquals(resp.getMemberValue(XMLRPCTokens.ResponseCode.toString()), 0);
    }
    
    
    
    public void testUpdateFamilyAndFriendsNumbersMessage() throws XmlRpcConnectionException {
        String fafNumber = "00905334486479";
        UpdateFaFListCommand cmd = factory.getUpdateFaFListCommand("52922625", new Date(), "826307248", fafNumber, FAFAction.ADD);
        cmd.execute();
        MethodResponse resp = cmd.getResponse();
        assertEquals(resp.getMemberValue(XMLRPCTokens.ResponseCode.toString()), 0);
    }
    
    
    
    public void testGetFaFListCommand() throws XmlRpcConnectionException {
    	Date requstTimestamp = DateUtils.convertISO8601DateForTest(originTimeStamp);
    	GetFaFListCommand cmd = factory.getFaFListCommand("12345765", requstTimestamp,
        "826307248");
        cmd.execute();
        MethodResponse resp = cmd.getResponse();
        List<FAFNumber> fafList = cmd.getFAFInformationList();
        for(FAFNumber number : fafList) {
            System.out.println("FAF Number: " + number.getMsisdn() + " " + number.getIndicator());
        }
        assertEquals(resp.getMemberValue(XMLRPCTokens.ResponseCode.toString()), 0);
    }
    
}
