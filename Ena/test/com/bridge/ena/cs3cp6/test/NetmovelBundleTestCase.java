package com.bridge.ena.cs3cp6.test;

import java.util.Date;

import org.junit.Before;

import com.bridge.ena.cs3cp6.command.CommandFactory;
import com.bridge.ena.cs3cp6.command.NetmovelBundleCommand;
import com.bridge.ena.util.DateUtils;
import com.bridge.ena.xmlrpc.IXMLRPCClient;
import com.bridge.ena.xmlrpc.XMLRPCClient;
import com.bridge.ena.xmlrpc.command.XMLRPCTokens;
import com.bridge.ena.xmlrpc.serializer.MethodResponse;

import junit.framework.TestCase;

public class NetmovelBundleTestCase extends TestCase {

	private String originTimeStamp = "20080320T16:55:05+0200";
    String path =
        "D://NetBeansWorkspaces//MCel//TUMP//Ena//test//com//bridge//ena//cs//test//";
    private IXMLRPCClient client;
    private CommandFactory factory;

    @Before
    public void setUp() throws Exception {
        client = new XMLRPCClient("http://192.168.40.35:10010/Air", 500, "UGw Server/3.1/1.0", "Basic dHVtX2FwcDp0dW1fYXBw", 1, 1000);
        factory = new CommandFactory(client, "MCELTUMTEST01", "258", 1);
    }

    public void testStartBunlde() throws Exception {
    	Date requestTimestamp = DateUtils.convertISO8601DateForTest(originTimeStamp);
    	NetmovelBundleCommand cmd = factory.getNetmovelBundleCommand("1234567812342231", requestTimestamp, "826307248", 30f, -25.5f, 3, "Netmovel Test", "Netmovel Test", 0, 0, 100, 31, 31);
    	cmd.execute();
        MethodResponse resp = cmd.getLastCommand().getResponse();
        System.out.println(resp.toXML(new StringBuffer()));
        assertEquals(resp.getMemberValue(XMLRPCTokens.ResponseCode.toString()), 0);
    }
}
