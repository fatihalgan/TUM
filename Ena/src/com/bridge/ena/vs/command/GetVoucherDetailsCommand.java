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
public class GetVoucherDetailsCommand extends AbstractVSCommand {

    private String serialNumber;
    private float decimalDenominator;
    
    GetVoucherDetailsCommand(String serialNumber, float decimalDenominator) {
        this.serialNumber = serialNumber;
        this.decimalDenominator = decimalDenominator;
    }
    
    public void prepareRequest() {
        request.setMethodName(VSTokens.GetVoucherDetailsMethodCallName.toString());
        request.addMember(new Member(VSTokens.SerialNumber.toString(), serialNumber));
    }
    
    public String getSerialNumber() {
        return serialNumber;
    }

    @Override
    public Float getDecimalDenominator() {
        return decimalDenominator;
    }

}
