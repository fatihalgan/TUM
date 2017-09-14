/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.cs3cp6.test;

import junit.framework.TestCase;
import com.bridge.ena.xmlrpc.IXMLRPCClient;
import com.bridge.ena.cs3cp6.command.CommandFactory;
import com.bridge.ena.cs3cp6.command.FreeAmountRefillCommand;
import com.bridge.ena.cs3cp6.command.RefillCommand;
import com.bridge.ena.util.DateUtils;
import com.bridge.ena.xmlrpc.XMLRPCClient;
import com.bridge.ena.xmlrpc.command.XMLRPCTokens;
import com.bridge.ena.xmlrpc.serializer.MethodResponse;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;

/**
 *
 * @author db2admin
 */
public class AirRefillMessageTestCase extends TestCase {

    private String originTimeStamp = "20080320T16:55:05+0200";
    String path =
        "D://NetBeansWorkspaces//MCel//TUMP//Ena//test//com//bridge//ena//cs//test//";
    private IXMLRPCClient client;
    private CommandFactory factory;
    private static Log logger = LogFactory.getLog(AirRefillMessageTestCase.class);

    @Before
    public void setUp() throws Exception {
        client = new XMLRPCClient("http://10.255.0.163:10010/Air", 5000, "UGw Server/3.1/1.0", "Basic dHVtX2FwcDp0dW1fYXBw", 5, 3000);
        factory = new CommandFactory(client, "MCELTUMTEST01 MKESH", "258", 1);
    }

    
    
    public void testRefillMessage() throws Exception {
        Date requestTimestamp = DateUtils.convertISO8601DateForTest(originTimeStamp);
        RefillCommand cmd = factory.getRefillCommand("1234567893234353322", requestTimestamp, "826307248", 20f, "", "", 100);
        cmd.execute();
        MethodResponse resp = cmd.getResponse();
        if(resp.isFault()) {
        	logger.debug(resp.toXML(new StringBuffer()));
        	return;
        }
        assertTrue(((Integer)resp.getMemberValue(XMLRPCTokens.ResponseCode.toString())) < 100);
        logger.debug("Account before Refill:");
        logger.debug("Main Account Value: " + cmd.getAccountBeforeRefill().getAccountValue());
        logger.debug("SMS Account Value: " + cmd.getSMSAccountBeforeRefill().getDedicatedAccountValue());
        if(cmd.getMMSAccountBeforeRefill() != null) logger.debug("MMS Account Value: " + cmd.getMMSAccountBeforeRefill().getDedicatedAccountValue());
        logger.debug("Service Class Before: " + cmd.getAccountBeforeRefill().getServiceClassCurrent());
        logger.debug("SupervisionExpiryDate: " + cmd.getAccountBeforeRefill().getSupervisionExpiryDate());
        logger.debug("ServiceFeeExpiryDate: " + cmd.getAccountBeforeRefill().getServiceFeeExpiryDate());
        logger.debug("Account after Refill:");
        logger.debug("Main Account Value: " + cmd.getAccountAfterRefill().getAccountValue());
        logger.debug("SMS Account Value: " + cmd.getSMSAccountAfterRefill().getDedicatedAccountValue());
        if(cmd.getMMSAccountAfterRefill() != null) logger.debug("MMS Account Value: " + cmd.getMMSAccountAfterRefill().getDedicatedAccountValue());
        logger.debug("Service Class After: " + cmd.getAccountAfterRefill().getServiceClassCurrent());
        logger.debug("SupervisionExpiryDate: " + cmd.getAccountAfterRefill().getSupervisionExpiryDate());
        logger.debug("ServiceFeeExpiryDate: " + cmd.getAccountAfterRefill().getServiceFeeExpiryDate());
    }
    
    
    /**
    public void testFreeAmountRefillMessage() throws Exception {
        Date requestTimestamp = DateUtils.convertISO8601DateForTest(originTimeStamp);
        FreeAmountRefillCommand cmd = factory.getFreeAmountRefillCommand(String.valueOf(System.currentTimeMillis()), requestTimestamp, "826307248", 20f, "Test", "Test", 100);
        cmd.execute();
        assertFalse(cmd.isFault());
        System.out.println(cmd.getResponseCode());
        assertTrue(cmd.getResponseCode() < 100);
        logger.debug("Account before Refill:");
        logger.debug("Main Account Value: " + cmd.getBalanceBefore());
        logger.debug("Account after Refill:");
        logger.debug("Main Account Value: " + cmd.getBalanceAfter());
        logger.debug("Dedicated Account Values Changes: " );
        Float[] da = cmd.getDedicatedAccountValueChanges();        
        for(int i = 0; i < da.length; i++) {
        	logger.debug("Dedicated Account " + (i+1) + ": " + da[i]);
        }
    }
    **/
    
}
