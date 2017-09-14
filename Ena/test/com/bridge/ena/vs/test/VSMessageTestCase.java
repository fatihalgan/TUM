/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.vs.test;

import junit.framework.TestCase;

import com.bridge.ena.vs.command.Action;
import com.bridge.ena.vs.command.ChangeVoucherStateCommand;
import com.bridge.ena.vs.command.CommandFactory;
import com.bridge.ena.vs.command.EndReservationCommand;
import com.bridge.ena.vs.command.GetVoucherDetailsCommand;
import com.bridge.ena.vs.command.GetVoucherHistoryCommand;
import com.bridge.ena.vs.command.GetVoucherSerialCommand;
import com.bridge.ena.vs.command.ReserveVoucherCommand;
import com.bridge.ena.vs.command.UpdateVoucherStateCommand;
import com.bridge.ena.vs.command.VoucherState;
import com.bridge.ena.xmlrpc.IXMLRPCClient;
import com.bridge.ena.xmlrpc.XMLRPCClient;
import com.bridge.ena.xmlrpc.exception.XmlRpcConnectionException;
import com.bridge.ena.xmlrpc.serializer.MethodResponse;
import org.junit.Before;

/**
 *
 * @author db2admin
 */
public class VSMessageTestCase extends TestCase {

    private String originTimeStamp = "20070903T16:55:05+0200";
    String path = "D://NetBeansWorkspaces//MCel//TUMP//Ena//test//com//bridge//ena//vs//test//";
    private IXMLRPCClient client;
    private CommandFactory factory;

    @Before
    public void setUp() throws Exception {
        client = new XMLRPCClient("http://192.168.40.61:10020/VoucherUsage", 12000, "UGw Server/2.1/1.0", "app_voucher", "vsexec", 5, 3000);
        factory = new CommandFactory(client, "MCELTUMTEST01");
    }
    
    
    public void testGetVoucherDetailsMessage() throws XmlRpcConnectionException {
        GetVoucherDetailsCommand cmd = factory.getVoucherDetailsCommand("07570000189913", 100f);
        cmd.execute();
        System.out.println(cmd.getRequest().toXML(new StringBuffer()));
        MethodResponse response = cmd.getResponse();
        System.out.println(response.toXML(new StringBuffer()));
    }
    
    
    /**
    public void testGetVoucherDetailsByActivationCodeMessage() throws XmlRpcConnectionException {
    	GetVoucherSerialCommand cmd = factory.getVoucherSerialCommand("07570000189913", 100f);
    	cmd.execute();
    	System.out.println(cmd.getRequest().toXML(new StringBuffer()));
        MethodResponse response = cmd.getResponse();
        System.out.println(response.toXML(new StringBuffer()));
    }
    **/
    
    /**
    public void testChangeVoucherStateBatch() throws XmlRpcConnectionException  {
    	ChangeVoucherStateCommand cmd = factory.getChangeVoucherStateCommand("091307864001900", "091307864001900", 2);
    	cmd.execute();
    	System.out.println(cmd.getRequest().toXML(new StringBuffer()));
        MethodResponse response = cmd.getResponse();
        System.out.println(response.toXML(new StringBuffer()));
    }
	**/
    /**
    public void testGetVoucherHistoryMessage() throws XmlRpcConnectionException {
        GetVoucherHistoryCommand cmd = factory.getVoucherHistoryCommand("081306572603886", 100f);
        cmd.execute();
        System.out.println(cmd.getRequest().toXML(new StringBuffer()));
        MethodResponse response = cmd.getResponse();
        System.out.println(response.toXML(new StringBuffer()));
    }
    **/
    /**
    public void testUpdateVoucherState() throws XmlRpcConnectionException {
        UpdateVoucherStateCommand cmd = factory.getUpdateVoucherStateCommand("060606339685923", VoucherState.Available.getCode(), VoucherState.Damaged.getCode());
        cmd.execute();
        assertFalse(cmd.isErrorOrFaultResponse());
        System.out.println(cmd.getRequest().toXML(new StringBuffer()));
        MethodResponse response = cmd.getResponse();
        System.out.println(response.toXML(new StringBuffer()));
    }
    **/
    
    /**
    public void testReserveCancelVoucher() throws XmlRpcConnectionException {
    	String requestTransId = String.valueOf(System.currentTimeMillis());
    	System.out.print("Request Trans Id is: " + requestTransId);
    	ReserveVoucherCommand cmd = factory.getReserveVoucherCommand("07570000209869", "27832137877", requestTransId, 100f);
    	cmd.execute();
    	assertFalse(cmd.isErrorOrFaultResponse());
    	EndReservationCommand cmd2 = factory.getEndReservationCommand("07570000209869", Action.Rollback, "27832137877", requestTransId);
    	cmd2.execute();
    	assertFalse(cmd.isErrorOrFaultResponse());
    }
    **/
}
