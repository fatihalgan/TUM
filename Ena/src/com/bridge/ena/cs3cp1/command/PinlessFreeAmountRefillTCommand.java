/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.cs3cp1.command;

import com.bridge.ena.vs.profiles.VoucherProfile;
import com.bridge.ena.xml.BadXmlFormatException;
import com.bridge.ena.xmlrpc.exception.XmlRpcConnectionException;
import com.bridge.ena.xmlrpc.serializer.MethodResponse;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author db2admin
 */
public class PinlessFreeAmountRefillTCommand extends PinlessRefillTCommand {

    private Float refillPortion = null;
    private Float adjustmentPortion = null;
    private Integer adjustmentFlag = -1;
    

    public PinlessFreeAmountRefillTCommand(String originTransactionId, Date originTimestamp, String subscriberNumber,
        Float transactionAmountRefill, String externalData1, String externalData2, float decimalDenominator, String subscriberNoPrefix, Integer subscriberNumberNAI) {
        super(originTransactionId, originTimestamp, subscriberNumber, transactionAmountRefill, externalData1,
           externalData2, decimalDenominator, subscriberNoPrefix, subscriberNumberNAI);
    }

    public MethodResponse execute() throws XmlRpcConnectionException, BadXmlFormatException {
        VoucherProfile profile = matchPaymentProfile(getTransactionAmountRefill());
        if(profile != null) {
            this.voucherProfileId = profile.getProfileID().trim();
            this.voucherProfileName = profile.getProfileName().trim();
        }

        if(getTransactionAmountRefill() >= profile.getMainAccountValue()) {
            refillPortion = profile.getMainAccountValue();
            adjustmentPortion  = getTransactionAmountRefill() - refillPortion;
        } else {
            refillPortion = 0f;
            adjustmentPortion = getTransactionAmountRefill();
        }

        AbstractCS3CP1Command cmd = new BalanceEnquiryTCommand( getSubscriberNumber(), getOriginTimestamp(),getOriginTransactionId(), getDecimalDenominator(), getSubscriberNoPrefix(), getSubscriberNumberNAI());
        addCommand(cmd);
        cmd.setXMLRPCClient(getXMLRPCClient());
        cmd.execute();
        if(cmd.isErrorOrFaultResponse()) return cmd.getResponse();
        balanceBefore = cmd.getSubscriberBalance(decimalDenominator);
        serviceClassOld = cmd.getServiceClassCurrent();
        balanceAfter = balanceBefore;

        if(willServiceClassChange(cmd.getServiceClassCurrent(), profile)) {
            Integer serviceClassCurrent = cmd.getServiceClassCurrent();
            cmd = new UpdateServiceClassTCommand(getOriginTransactionId(), getOriginTimestamp(), getSubscriberNumber(),serviceClassCurrent, profile.getNewTemporaryServiceClass(), getSubscriberNoPrefix(), getSubscriberNumberNAI());
            addCommand(cmd);
            cmd.setXMLRPCClient(getXMLRPCClient());
            try {
                cmd.execute();
            } finally {
                updateServiceClassFlag = cmd.getFullResultCode();
            }
            if(cmd.isErrorOrFaultResponse()) return cmd.getResponse();
            else {
                serviceClassNew = cmd.getServiceClassCurrent();
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, profile.getServiceClassExtension());
                newServiceClassExpiryDate = cal.getTime();
            }
        }

        if(refillPortion > 0) {
            cmd = new RefillTCommand( getOriginTransactionId(),getOriginTimestamp(), getSubscriberNumber(), refillPortion, getExternalData1(), getExternalData2(), profile.getProfileID(), getDecimalDenominator(), getSubscriberNoPrefix(), getSubscriberNumberNAI());
            addCommand(cmd);
            cmd.setXMLRPCClient(getXMLRPCClient());
            try {
                cmd.execute();
            } finally {
                refillFlag = cmd.getFullResultCode();
            }
            if(cmd.isErrorOrFaultResponse()) return cmd.getResponse();
            else {
                balanceAfter = cmd.getAccountValueAfter1(decimalDenominator);
            }
        }

        if(adjustmentPortion > 0) {
            cmd = new AdjustmentTCommand(getOriginTransactionId(), getOriginTimestamp(), getSubscriberNumber(),adjustmentPortion, getExternalData1(), getExternalData2(), 0, 0, 0, 0, null, getDecimalDenominator(), getSubscriberNoPrefix(), getSubscriberNumberNAI());
            addCommand(cmd);
            cmd.setXMLRPCClient(getXMLRPCClient());
            try {
                cmd.execute();
            } finally {
                adjustmentFlag = cmd.getFullResultCode();
            }
            if(cmd.isErrorOrFaultResponse()) return cmd.getResponse();
            else {
                balanceAfter = balanceAfter + adjustmentPortion;
            }
        }

        cmd = new BalanceEnquiryTCommand( getSubscriberNumber(), getOriginTimestamp(),getOriginTransactionId(), getDecimalDenominator(), getSubscriberNoPrefix(), getSubscriberNumberNAI());
        addCommand(cmd);
        cmd.setXMLRPCClient(getXMLRPCClient());
        try {
            cmd.execute();
        } finally {
            balanceEnquiryFlag = cmd.getFullResultCode();
        }
        if(cmd.isErrorOrFaultResponse()) return cmd.getResponse();
        else {
            balanceAfter = cmd.getSubscriberBalance(decimalDenominator);
            serviceClassNew = cmd.getServiceClassCurrent();
            return cmd.getResponse();
        }
    }

    private VoucherProfile matchPaymentProfile(Float transactionAmountRefill) {
        VoucherProfile profile = getVoucherProfiles().getByClosestNominalValue(transactionAmountRefill);
        return profile;
    }

    private boolean willServiceClassChange(Integer currentServiceClass, VoucherProfile profile) {
        if(profile.getNewTemporaryServiceClass() == null) return false;
        if(profile.hasInFromServiceClasses(currentServiceClass)) return true;
        return false;
    }

    public Float getRefillPortion() {
        return refillPortion;
    }

    public Float getAdjustmentPortion() {
        return adjustmentPortion;
    }

    public Integer getAdjustmentFlag() {
        return adjustmentFlag;
    }
}
