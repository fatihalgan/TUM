/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.cs3cp1.test;

import com.bridge.ena.cs3cp1.command.CommandFactory;
import com.bridge.ena.cs3cp1.command.StandartVoucherRefillTCommand;
import com.bridge.ena.cs3cp1.command.ValueVoucherRefillTCommand;
import com.bridge.ena.cs3cp1.command.VoucherRefillTCommand;
import com.bridge.ena.util.DateUtils;
import com.bridge.ena.xml.BadXmlFormatException;
import com.bridge.ena.xmlrpc.IXMLRPCClient;
import com.bridge.ena.xmlrpc.XMLRPCClient;
import com.bridge.ena.xmlrpc.exception.XmlRpcConnectionException;
import com.bridge.ena.xmlrpc.serializer.MethodResponse;
import java.util.Date;
import junit.framework.TestCase;
import org.junit.Before;

/**
 *
 * @author db2admin
 */
public class AirVoucherRefillMessageTestCase extends TestCase {

    private String originTimeStamp = "20080320T16:55:05+0200";
    String path =
        "D://NetBeansWorkspaces//MCel//TUMP//Ena//test//com//bridge//ena//cs//test//";
    private IXMLRPCClient client;
    private CommandFactory factory;

    @Before
    public void setUp() throws Exception {
        client = new XMLRPCClient("http://192.168.40.31:10010/Air", 5000, "UGw Server/2.1/1.0", 5, 3000);
        factory = new CommandFactory(client, "MCELTUMTEST01", "258", 1);
    }

    /**
    public void testStandartVoucherRefillT() throws XmlRpcConnectionException {
        Date requestTimestamp = DateUtils.convertISO8601DateForTest(originTimeStamp);
        StandartVoucherRefillTCommand cmd = factory.getStandartVoucherRefillTCommand("111111111", new Date(), "826307248", "63162385661535", "Test", "Test", 100f);
        cmd.execute();
        MethodResponse resp = cmd.getResponse();
        System.out.println(resp.toXML(new StringBuffer()));
        assertFalse(resp.isFault());
    }
    
    public void testValueVoucherRefillT() throws XmlRpcConnectionException {
        Date requestTimestamp = DateUtils.convertISO8601DateForTest(originTimeStamp);
        ValueVoucherRefillTCommand cmd = factory.getValueVoucherRefillTCommand("11111111", new Date(), "826307248", "63162385661535", "Test", "Test", 100f);
        cmd.execute();
        MethodResponse resp = cmd.getResponse();
        System.out.println(resp.toXML(new StringBuffer()));
        assertFalse(resp.isFault());
    }
    **/
    
    public void testVoucherRefillT() throws XmlRpcConnectionException, BadXmlFormatException {
        Date requestTimestamp = DateUtils.convertISO8601DateForTest(originTimeStamp);
        VoucherRefillTCommand cmd = factory.getVoucherRefillTCommand("11111111", new Date(), "826307248", "70212050172875", "Test", "Test", 100f);
        cmd.execute();
        MethodResponse resp = cmd.getLastCommand().getResponse();
        System.out.println(resp.toXML(new StringBuffer()));
        assertFalse(resp.isFault());
    }
}
