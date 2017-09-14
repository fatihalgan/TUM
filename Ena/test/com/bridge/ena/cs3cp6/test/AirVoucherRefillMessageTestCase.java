/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.cs3cp6.test;

import com.bridge.ena.cs3cp6.command.CommandFactory;
import com.bridge.ena.cs3cp6.command.VoucherRefillCommand;
import com.bridge.ena.util.DateUtils;
import com.bridge.ena.xmlrpc.IXMLRPCClient;
import com.bridge.ena.xmlrpc.XMLRPCClient;
import com.bridge.ena.xmlrpc.command.XMLRPCTokens;
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
        client = new XMLRPCClient("http://192.168.40.35:10010/Air", 5000, "UGw Server/3.1/1.0", "Basic dHVtX2FwcDp0dW1fYXBw", 5, 3000);
        factory = new CommandFactory(client, "MCELTUMTEST01", "258", 1);
    }

    public void testVoucherRefillMessage() throws Exception {
        Date requestTimestamp = DateUtils.convertISO8601DateForTest(originTimeStamp);
        VoucherRefillCommand cmd = factory.getVoucherRefillCommand("123456789", requestTimestamp, "822902341", "32632824386183", "Test", "Test", 100f);
        cmd.execute();
        MethodResponse resp = cmd.getResponse();
        assertEquals(resp.getMemberValue(XMLRPCTokens.ResponseCode.toString()), 0);
    }
}
