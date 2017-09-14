/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.vs.command;

import com.bridge.ena.xmlrpc.serializer.Member;

/**
 *
 * @author db2admin
 */
public class UpdateVoucherStateCommand extends AbstractVSCommand {

    private String serialNumber;
    private int oldState;
    private int newState;
    
    UpdateVoucherStateCommand(String serialNumber,int oldState,int newState) {
        this.serialNumber = serialNumber;
        this.oldState 	  = oldState;
        this.newState     = newState;
    }
    
    public void prepareRequest() {
        request.setMethodName(VSTokens.UpdateVoucherStateMethodCallName.toString());
        request.addMember(new Member(VSTokens.SerialNumber.toString(), serialNumber));
        request.addMember(new Member(VSTokens.NewState.toString(), newState));
        request.addMember(new Member(VSTokens.OldState.toString(), oldState));        
    }
    
    public String getSerialNumber() {
        return serialNumber;
    }

    @Override
    public Float getDecimalDenominator() {
        throw new UnsupportedOperationException("This command does not support decimal denominator");
    }
}
