/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.cs3cp1.command;

import com.bridge.ena.cs.command.AbstractCSCommand;
import com.bridge.ena.cs.command.CompositeCSCommand;
import com.bridge.ena.cs.value.SubscriberInfo;
import com.bridge.ena.vs.profiles.VoucherProfile;
import com.bridge.ena.vs.profiles.VoucherProfiles;
import com.bridge.ena.vs.profiles.VoucherProfilesLoader;
import com.bridge.ena.xml.BadXmlFormatException;
import com.bridge.ena.xmlrpc.exception.XmlRpcConnectionException;
import com.bridge.ena.xmlrpc.serializer.MethodResponse;
import java.util.Date;

/**
 *
 * @author db2admin
 */
public class VoucherRefillTCommand extends CompositeCSCommand<AbstractCSCommand> {

    private String originTransactionId;
    private Date originTimestamp;
    private String subscriberNumber;
    private String activationNumber;
    private String externalData1;
    private String externalData2;
    private Float decimalDenominator;
    //True indicates that the voucher status is updated but no refill could be done..
    private boolean voucherStatusCorrupted = false;

    protected VoucherRefillTCommand(String originTransactionId, Date originTimestamp, String subscriberNumber,
        String activationNumber, String externalData1, String externalData2, float decimalDenominator,
        String subscriberNoPrefix, Integer subscriberNumberNAI) {
        super(subscriberNoPrefix, subscriberNumberNAI);
        this.originTransactionId = originTransactionId;
        this.originTimestamp = originTimestamp;
        this.subscriberNumber = subscriberNumber;
        this.activationNumber = activationNumber;
        this.externalData1 = externalData1;
        this.externalData2 = externalData2;
        this.decimalDenominator = decimalDenominator;
    }

    @Override
    public boolean supportsGetSubscriberInfo() {
        return false;
    }

    @Override
    public SubscriberInfo getSubscriberInfo() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public MethodResponse execute() throws XmlRpcConnectionException, BadXmlFormatException {
        AbstractCS3CP1Command cmd = new StandartVoucherRefillTCommand(originTransactionId, originTimestamp, subscriberNumber, activationNumber, externalData1, externalData2, decimalDenominator, getSubscriberNoPrefix(), getSubscriberNumberNAI());
        addCommand(cmd);
        cmd.setXMLRPCClient(getXMLRPCClient());
        cmd.execute();
        if(cmd.getResponseCode().equals(114)) {
            //This must be a value voucher, Refill it and get the voucher profile
            cmd = new ValueVoucherRefillTCommand(originTransactionId, originTimestamp, subscriberNumber, activationNumber, externalData1, externalData2, decimalDenominator, getSubscriberNoPrefix(), getSubscriberNumberNAI());
            addCommand(cmd);
            cmd.setXMLRPCClient(getXMLRPCClient());
            cmd.execute();
            if(cmd.isErrorOrFaultResponse()) return cmd.getResponse();
            //if successful mark the voucher as used..
            voucherStatusCorrupted = true;
            String profileID = matchPaymentProfileID(cmd);
            Float refillAmount = cmd.getTransactionAmount1Refill(decimalDenominator);
            cmd = new RefillTCommand(originTransactionId, originTimestamp, subscriberNumber, refillAmount, externalData1, externalData2, profileID, decimalDenominator, getSubscriberNoPrefix(), getSubscriberNumberNAI());
            addCommand(cmd);
            cmd.setXMLRPCClient(getXMLRPCClient());
            cmd.execute();
            if(cmd.getResponseCode() == 0) voucherStatusCorrupted = false;
            return cmd.getResponse();
        } else if(cmd.isErrorOrFaultResponse()) return cmd.getResponse();
        return null;
    }

    private String matchPaymentProfileID(AbstractCS3CP1Command cmd) {
        Float refillAmount = cmd.getTransactionAmount1Refill(decimalDenominator);
        VoucherProfile profile = getVoucherProfiles().getByNominalValue(refillAmount);
        return profile.getProfileID();
    }

    private VoucherProfiles getVoucherProfiles() {
        return VoucherProfilesLoader.instance().getVoucherProfiles();
    }

    public boolean isVoucherStatusCorrupted() {
        return voucherStatusCorrupted;
    }

}
