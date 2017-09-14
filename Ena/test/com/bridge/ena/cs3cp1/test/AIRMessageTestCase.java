/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package com.bridge.ena.cs3cp1.test;

//~--- non-JDK imports --------------------------------------------------------

import com.bridge.ena.cs.value.FAFNumber;
import com.bridge.ena.cs3cp1.adjustment.policy.AdjustmentBase;
import com.bridge.ena.cs3cp1.adjustment.policy.AdjustmentRestriction;
import com.bridge.ena.cs3cp1.adjustment.policy.AdjustmentRestrictions;
import com.bridge.ena.cs3cp1.adjustment.policy.DedicatedAccountAdjustment;
import com.bridge.ena.cs3cp1.adjustment.policy.DedicatedAccountAdjustmentValue;
import com.bridge.ena.cs3cp1.adjustment.policy.DedicatedAccountAdjustments;
import com.bridge.ena.cs3cp1.adjustment.policy.ServiceClassRestriction;
import junit.framework.TestCase;

import com.bridge.ena.cs3cp1.command.BalanceEnquiryTCommand;
import com.bridge.ena.cs3cp1.command.GetAccountDetailsTCommand;
import com.bridge.ena.util.DateUtils;
import com.bridge.ena.xmlrpc.IXMLRPCClient;
import com.bridge.ena.xmlrpc.XMLRPCClient;
import com.bridge.ena.xmlrpc.exception.XmlRpcConnectionException;
import com.bridge.ena.xmlrpc.serializer.MethodResponse;

import org.junit.Before;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bridge.ena.cs3cp1.command.AdjustmentTCommand;
import com.bridge.ena.cs3cp1.command.CommandFactory;
import com.bridge.ena.cs3cp1.command.GetAllowedServiceClassChangesTCommand;
import com.bridge.ena.cs3cp1.command.GetFaFListTCommand;
import com.bridge.ena.cs3cp1.command.TariffedAdjustmentCommand;
import com.bridge.ena.cs3cp1.command.UpdateFaFTCommand;
import com.bridge.ena.cs3cp1.command.UpdateServiceClassTCommand;
import com.bridge.ena.cs3cp1.value.FAFAction;
import com.bridge.ena.xml.BadXmlFormatException;
import com.bridge.ena.xmlrpc.command.XMLRPCTokens;
import org.joda.time.MutablePeriod;
import org.joda.time.PeriodType;

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
        client = new XMLRPCClient("http://192.168.40.35:10010/Air", 500, "UGw Server/2.1/1.0", 1, 1000);
        factory = new CommandFactory(client, "MCELTUMTEST01", "258", 1);
    }

    
    public void testBalanceEnquiryMessage() throws XmlRpcConnectionException {
        Date requestTimestamp = DateUtils.convertISO8601DateForTest(originTimeStamp);
        BalanceEnquiryTCommand cmd = factory.getBalanceEnquiryTCommand("826307248", requestTimestamp, "34656554", 100);
        cmd.execute();
        MethodResponse resp = cmd.getResponse();
        assertEquals(resp.getMemberValue(XMLRPCTokens.ResponseCode.toString()), 0);
    }


    
    public void testGetAccountDetailsMessage() throws XmlRpcConnectionException {
        Date requstTimestamp = DateUtils.convertISO8601DateForTest(originTimeStamp);
        GetAccountDetailsTCommand cmd = factory.getAccountDetailsTCommand("12345765", requstTimestamp,
        "826307248", 100);
        cmd.execute();
        MethodResponse resp = cmd.getResponse();
        assertEquals(resp.getMemberValue(XMLRPCTokens.ResponseCode.toString()), 0);
    }
    
    
    public void testMakeAdjustmentBalanceCheckMessage() throws XmlRpcConnectionException {
        Date requestTimestamp = DateUtils.convertISO8601DateForTest(originTimeStamp);
        BalanceEnquiryTCommand enquiry = factory.getBalanceEnquiryTCommand("826307248", requestTimestamp, "76123495", 100);
        enquiry.execute();
        Float balanceBefore = enquiry.getSubscriberBalance(enquiry.getDecimalDenominator());
        Date supervisionBefore = enquiry.getSupervisionExpiryDate();
        Date serviceFeeBefore = enquiry.getServiceFeeExpiryDate();
        AdjustmentTCommand adjustment = factory.getAdjustmentTCommand("7612349501", requestTimestamp, "826307248", 5f, "test", "test", 1, 1, 0, 0, 100);
        adjustment.execute();
        enquiry.execute();
        Float balanceAfter = enquiry.getSubscriberBalance(enquiry.getDecimalDenominator());
        assertEquals(balanceAfter - balanceBefore, 5f);
        MutablePeriod period = new MutablePeriod(supervisionBefore.getTime(), enquiry.getSupervisionExpiryDate().getTime(), PeriodType.days());
        assertEquals(period.getDays(), 1);
        period = new MutablePeriod(serviceFeeBefore.getTime(), enquiry.getServiceFeeExpiryDate().getTime(), PeriodType.days());
        assertEquals(period.getDays(), 1);
    }
    

    /**
    public void testAdjustmentWithManualFreeCallsMessage() throws XmlRpcConnectionException {
        Date requestTimestamp = DateUtils.convertISO8601DateForTest(originTimeStamp);
        BalanceEnquiryTCommand enquiry = factory.getBalanceEnquiryTCommand("826307248", requestTimestamp, "76123495", 100);
        enquiry.execute();
        Float balanceBefore = enquiry.getSubscriberBalance(enquiry.getDecimalDenominator());
        Float freeCallsBefore = enquiry.getDedicatedAccountValue(100f, 3);
        AdjustmentTCommand adjustment = factory.getFreeCallAdjustmentTCommand("7612349501", requestTimestamp, "826307248", 5f, "test", "test", 0, 0, 0, 0, 10f, 3, 100);
        adjustment.execute();
        enquiry.execute();
        Float balanceAfter = enquiry.getSubscriberBalance(enquiry.getDecimalDenominator());
        Float freeCallsAfter = enquiry.getDedicatedAccountValue(100f, 3);
        assertEquals(balanceAfter - balanceBefore, 5f);
        assertEquals(freeCallsAfter - freeCallsBefore, 10f);
    }
    **/
    /**
    private DedicatedAccountAdjustments prepareDedicatedAccountAdjustments(BalanceEnquiryTCommand enquiry) {
        AdjustmentRestrictions restrictions = new AdjustmentRestrictions();
        AdjustmentRestriction restriction = new ServiceClassRestriction(enquiry.getServiceClassCurrent().toString());
        restrictions.addServiceClassRestriction(restriction);
        DedicatedAccountAdjustments adjustments = new DedicatedAccountAdjustments();
        DedicatedAccountAdjustment daadj = new DedicatedAccountAdjustment(5, new Date(System.currentTimeMillis() - 100000),
                DateUtils.convertXSDDate("2016-09-01"), new DedicatedAccountAdjustmentValue(
                    AdjustmentBase.FreeAmount, 20f), restrictions);
        adjustments.setCurrentServiceClass(enquiry.getServiceClassCurrent().toString());
        adjustments.addAdjustment(daadj);
        return adjustments;
    }
    **/
    /**
    public void testMakeAdjustmentWithDedicatedAccountsBalanceCheckMessage() throws XmlRpcConnectionException {
        Date requestTimestamp = DateUtils.convertISO8601DateForTest(originTimeStamp);
        BalanceEnquiryTCommand enquiry = factory.getBalanceEnquiryTCommand("826307248", requestTimestamp, "76123495", 100);
        enquiry.execute();
        Float freeCallsBefore = enquiry.getDedicatedAccountValue(enquiry.getDecimalDenominator(), 5);
        DedicatedAccountAdjustments adjustments = prepareDedicatedAccountAdjustments(enquiry);
        AdjustmentTCommand adjustment = factory.getAdjustmentTCommand("7612349501", requestTimestamp, "826307248", 5f, "test", "test", -15, -15, 0, 0, adjustments, 100);
        adjustments.setDecimalDenominator(adjustment.getDecimalDenominator());
        adjustments.setMainAdjustmentAmonut(adjustment.getAdjustmentAmount());
        adjustment.execute();
        enquiry.execute();
        Float freeCallsAfter = enquiry.getDedicatedAccountValue(enquiry.getDecimalDenominator(), 5);
        assertEquals(20f, freeCallsAfter - freeCallsBefore);
    }
    **/
    /**
    public void testMakeTariffedAdjustmentBalanceCheckMessage() throws XmlRpcConnectionException, BadXmlFormatException {
        Date requestTimestamp = DateUtils.convertISO8601DateForTest(originTimeStamp);
        BalanceEnquiryTCommand enquiry = factory.getBalanceEnquiryTCommand("826307248", requestTimestamp, "76123495", 100);
        enquiry.execute();
        Float balanceBefore = enquiry.getSubscriberBalance(enquiry.getDecimalDenominator());
        Date supervisionBefore = enquiry.getSupervisionDate();
        Date serviceFeeBefore = enquiry.getServiceFeeDate();
        TariffedAdjustmentCommand adjustment = factory.getTariffedAdjustmentCommand("7612349501", requestTimestamp, "826307248", 5f, "test", "test", 20, 5, 0, 0, 15, 100);
        adjustment.execute();
        enquiry.execute();
        Float balanceAfter = enquiry.getSubscriberBalance(enquiry.getDecimalDenominator());
        assertEquals(balanceAfter - balanceBefore, 5f);
        MutablePeriod period = new MutablePeriod(supervisionBefore.getTime(), enquiry.getSupervisionDate().getTime(), PeriodType.days());
        assertEquals(period.getDays(), adjustment.getCalculatedMakeCallIncrease());
        period = new MutablePeriod(serviceFeeBefore.getTime(), enquiry.getServiceFeeDate().getTime(), PeriodType.days());
        assertEquals(period.getDays(), adjustment.getCalculatedReceiveCallIncrease());
    }
     */
    /**
    //TODO: prepare data for Fama Service Class
    public void testDedicatedAccountpolicyTariffedAdjustmentMessage() throws XmlRpcConnectionException, BadXmlFormatException {
        Date requestTimestamp = DateUtils.convertISO8601DateForTest(originTimeStamp);
        BalanceEnquiryTCommand enquiry = factory.getBalanceEnquiryTCommand("826307248", requestTimestamp, "76123495", 100);
        enquiry.execute();
        Float balanceBefore = enquiry.getSubscriberBalance(enquiry.getDecimalDenominator());
        Float freeCallsBefore = enquiry.getDedicatedAccountValue(enquiry.getDecimalDenominator(), 5);
        TariffedAdjustmentCommand adjustment = factory.getTariffedAdjustmentCommand("7612349501", requestTimestamp, "828263806", 5f, "test", "test", 0, 0, 2f, 0, 15, 100);
        adjustment.execute();
        enquiry.execute();
        Float balanceAfter = enquiry.getSubscriberBalance(enquiry.getDecimalDenominator());
        Float freeCallsAfter = enquiry.getDedicatedAccountValue(enquiry.getDecimalDenominator(), 5);
        assertEquals(7f, balanceAfter - balanceBefore, 0.001f);
        assertEquals(30f, freeCallsAfter - freeCallsBefore);
    }
     */
    /**
    public void testUpdateServiceClassMessage() throws XmlRpcConnectionException {
    	Date requstTimestamp = DateUtils.convertISO8601DateForTest(originTimeStamp);
        UpdateServiceClassTCommand cmd = factory.getUpdateServiceClassTCommand("12345765", requstTimestamp,
        "826307248",3, 1);
        cmd.execute();
        MethodResponse resp = cmd.getResponse();
    }
        
    
    public void testGetAllowedServiceClassChangesTCommand() throws XmlRpcConnectionException {
    	Date requstTimestamp = DateUtils.convertISO8601DateForTest(originTimeStamp);
    	GetAllowedServiceClassChangesTCommand cmd = factory.getAllowedServiceClassChangesTCommand("12345765", requstTimestamp,
        "826307248");
        cmd.execute();
        MethodResponse resp = cmd.getResponse();
        System.out.println(resp.toXML(new StringBuffer()));
        assertEquals(resp.getMemberValue(XMLRPCTokens.ResponseCode.toString()), 0);
    }
        
    */


    public void testUpdateFaFMessage() throws XmlRpcConnectionException {
    	Date requstTimestamp = DateUtils.convertISO8601DateForTest(originTimeStamp);
        List<String> fafNumbers = new ArrayList<String>();
        fafNumbers.add("905334486479");
        UpdateFaFTCommand cmd = factory.getUpdateFaFTCommand("12345765", requstTimestamp,
        "826307248", fafNumbers, FAFAction.DELETE);
        cmd.execute();
        MethodResponse resp = cmd.getResponse();
    }
    
    public void testGetFaFListTCommand() throws XmlRpcConnectionException {
    	Date requstTimestamp = DateUtils.convertISO8601DateForTest(originTimeStamp);
    	GetFaFListTCommand cmd = factory.getFaFListTCommand("12345765", requstTimestamp,
        "826307248");
        cmd.execute();
        MethodResponse resp = cmd.getResponse();
        List<FAFNumber> fafInformationList = cmd.getFAFInformationList();
        for(FAFNumber i : fafInformationList) {
            System.out.println(i.getMsisdn() + " " + i.getIndicator());
        }
    }
    
    
}


//~ Formatted by Jindent --- http://www.jindent.com
