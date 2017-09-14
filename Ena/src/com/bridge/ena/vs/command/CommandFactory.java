/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.vs.command;

import com.bridge.ena.xmlrpc.IXMLRPCClient;

/**
 *
 * @author db2admin
 */
public class CommandFactory {

    private IXMLRPCClient xmlRpcClient;
    private String originHostName = null;

    public CommandFactory() {
        super();
    }
    
    public CommandFactory(IXMLRPCClient xmlRpcClient, String originHostName) {
        this.xmlRpcClient = xmlRpcClient;
        this.originHostName = originHostName;
    }
    
    public IXMLRPCClient getXmlRpcClient() {
        return xmlRpcClient;
    }

    public void setXmlRpcClient(IXMLRPCClient xmlRpcClient) {
        this.xmlRpcClient = xmlRpcClient;
    }

    public String getOriginHostName() {
        return originHostName;
    }

    public void setOriginHostName(String originHostName) {
        this.originHostName = originHostName;
    }
    
    public GetVoucherDetailsCommand getVoucherDetailsCommand(String serialNumber, float decimalDenominator) {
        GetVoucherDetailsCommand cmd = new GetVoucherDetailsCommand(serialNumber, decimalDenominator);
        cmd.setXMLRPCClient(getXmlRpcClient());
        cmd.setOriginHostName(getOriginHostName());
        return cmd;
    }
    
    public GetVoucherSerialCommand getVoucherSerialCommand(String activationCode, float decimalDenominator) {
    	GetVoucherSerialCommand cmd = new GetVoucherSerialCommand(activationCode, decimalDenominator);
    	cmd.setXMLRPCClient(getXmlRpcClient());
    	cmd.setOriginHostName(getOriginHostName());
    	return cmd;
    }
    
    public GetVoucherHistoryCommand getVoucherHistoryCommand(String serialNumber, float decimalDenominator) {
        GetVoucherHistoryCommand cmd = new GetVoucherHistoryCommand(serialNumber, decimalDenominator);
        cmd.setXMLRPCClient(getXmlRpcClient());
        cmd.setOriginHostName(getOriginHostName());
        return cmd;
    }
    
    public UpdateVoucherStateCommand getUpdateVoucherStateCommand(String serialNumber,int oldState,int newState) {
        UpdateVoucherStateCommand cmd = new UpdateVoucherStateCommand(serialNumber, oldState, newState);
        cmd.setXMLRPCClient(getXmlRpcClient());
        cmd.setOriginHostName(getOriginHostName());
        return cmd;
    }
    
    public ChangeVoucherStateCommand getChangeVoucherStateCommand(String serialNumberFirst, String serialNumberLast, int newState) {
    	ChangeVoucherStateCommand cmd = new ChangeVoucherStateCommand(serialNumberFirst,
    			serialNumberLast, newState);
    	cmd.setXMLRPCClient(getXmlRpcClient());
    	cmd.setOriginHostName(getOriginHostName());
    	return cmd;
    }
    
    public ReserveVoucherCommand getReserveVoucherCommand(String activationCode, String subscriberId, String transactionId, float decimalDenominator) {
    	ReserveVoucherCommand cmd = new ReserveVoucherCommand(activationCode, subscriberId, transactionId, decimalDenominator);
    	cmd.setXMLRPCClient(getXmlRpcClient());
    	cmd.setOriginHostName(getOriginHostName());
    	return cmd;
    }
    
    public EndReservationCommand getEndReservationCommand(String activationCode, Action action, String subscriberId, String transactionId) {
    	EndReservationCommand cmd = new EndReservationCommand(activationCode, action, subscriberId, transactionId);
    	cmd.setXMLRPCClient(getXmlRpcClient());
    	cmd.setOriginHostName(getOriginHostName());
    	return cmd;
    }
}
