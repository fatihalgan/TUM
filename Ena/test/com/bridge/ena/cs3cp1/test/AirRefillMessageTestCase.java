/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.cs3cp1.test;

import com.bridge.ena.cs3cp1.command.CommandFactory;
import com.bridge.ena.cs3cp1.command.PinlessFreeAmountRefillTCommand;
import com.bridge.ena.cs3cp1.command.PinlessRefillTCommand;
import com.bridge.ena.cs3cp1.command.RefillTCommand;
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
public class AirRefillMessageTestCase extends TestCase {

    private String originTimeStamp = "20080320T16:55:05+0200";
    String path =
        "D://NetBeansWorkspaces//MCel//TUMP//Ena//test//com//bridge//ena//cs//test//";
    private IXMLRPCClient client;
    private CommandFactory factory;

    @Before
    public void setUp() throws Exception {
        client = new XMLRPCClient("http://192.168.40.31:10010/Air", 5000, "UGw Server/3.1/1.0", 5, 3000);
        factory = new CommandFactory(client, "MCELTUMTEST01", "258", 1);
    }

    
    public void testV20RefillMessage() throws XmlRpcConnectionException {
        Date requestTimestamp = DateUtils.convertISO8601DateForTest(originTimeStamp);
        RefillTCommand cmd = factory.getRefillTCommand("123456789", requestTimestamp, "826307248", 20f, "Test", "Test", "19", 100);
        cmd.execute();
        MethodResponse resp = cmd.getResponse();
        assertFalse(resp.isFault());
    }

   
    public void testV20RefillMessageFreeAmount() throws XmlRpcConnectionException {
        Date requestTimestamp = DateUtils.convertISO8601DateForTest(originTimeStamp);
        RefillTCommand cmd = factory.getRefillTCommand("123456789", requestTimestamp, "826307248", 23f, "Test", "Test", "19", 100);
        cmd.execute();
        MethodResponse resp = cmd.getResponse();
        System.out.println(resp.toXML(new StringBuffer()));
        assertFalse(resp.isFault());
    }
    

    /**
    public void testV630RefillMessage() throws XmlRpcConnectionException {
        Date requestTimestamp = DateUtils.convertISO8601DateForTest(originTimeStamp);
        RefillTCommand cmd = factory.getRefillTCommand("123456789", requestTimestamp, "826307248", 600f, "Test", "Test", "28", 100);
        cmd.execute();
        System.out.println(cmd.getRequest().toXML(new StringBuffer()));
        MethodResponse resp = cmd.getResponse();
        System.out.println(resp.toXML(new StringBuffer()));
        assertFalse(resp.isFault());
    }
    **/
    /**
    public void testV2000RefillMessage() throws XmlRpcConnectionException {
        Date requestTimestamp = DateUtils.convertISO8601DateForTest(originTimeStamp);
        RefillTCommand cmd = factory.getRefillTCommand("123456789", requestTimestamp, "826307248", 2000f, "Test", "Test", "29", 100);
        cmd.execute();
        MethodResponse resp = cmd.getResponse();
        System.out.println(resp.toXML(new StringBuffer()));
        assertFalse(resp.isFault());
    }
    
    public void testV50RefillMessage() throws XmlRpcConnectionException {
        Date requestTimestamp = DateUtils.convertISO8601DateForTest(originTimeStamp);
        RefillTCommand cmd = factory.getRefillTCommand("123456789", requestTimestamp, "826307248", 50f, "Test", "Test", "22", 100);
        cmd.execute();
        MethodResponse resp = cmd.getResponse();
        System.out.println(resp.toXML(new StringBuffer()));
        assertFalse(resp.isFault());
    }
    */

    /**
    public void testPinlessRefillTCommand() throws XmlRpcConnectionException, BadXmlFormatException {
        Date requestTimestamp = DateUtils.convertISO8601DateForTest(originTimeStamp);
        PinlessRefillTCommand cmd = factory.getPinlessRefillTCommand("123456789", requestTimestamp, "826307248", 300f, "Test", "Test", 100);
        cmd.execute();
        MethodResponse resp = cmd.getLastCommand().getResponse();
        System.out.println(resp.toXML(new StringBuffer()));
        assertFalse(resp.isFault());
    }
    */

    public void testPinlessFreeAmountRefillTCommand() throws XmlRpcConnectionException, BadXmlFormatException {
        Date requestTimestamp = DateUtils.convertISO8601DateForTest(originTimeStamp);
        PinlessFreeAmountRefillTCommand cmd = factory.getPinlessFreeAmountRefillTCommand("123456789", requestTimestamp, "826307248", 105f, "Test", "Test", 100);
        cmd.execute();
        MethodResponse resp = cmd.getLastCommand().getResponse();
        System.out.println(resp.toXML(new StringBuffer()));
        assertFalse(resp.isFault());
    }

}
