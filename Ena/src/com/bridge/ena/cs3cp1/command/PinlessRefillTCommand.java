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
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author db2admin
 */
public class PinlessRefillTCommand extends CompositeCSCommand<AbstractCSCommand> {

    protected String originTransactionId;
    protected Date originTimestamp;
    protected String subscriberNumber;
    protected Float transactionAmountRefill;
    protected String externalData1;
    protected String externalData2;
    protected Float decimalDenominator;

    protected Integer serviceClassOld = null;
    protected Integer serviceClassNew = null;
    protected Float balanceBefore = null;
    protected String voucherProfileId = null;
    protected String voucherProfileName = null;
    protected Date newServiceClassExpiryDate = null;
    protected Float balanceAfter = null;

    protected Integer updateServiceClassFlag = -1;
    protected Integer refillFlag = -1;
    protected Integer balanceEnquiryFlag = -1;

    public PinlessRefillTCommand(String originTransactionId, Date originTimestamp, String subscriberNumber,
        Float transactionAmountRefill, String externalData1, String externalData2, float decimalDenominator, String subscriberNoPrefix, Integer subscriberNumberNAI) {
        super(subscriberNoPrefix, subscriberNumberNAI);
        this.originTransactionId = originTransactionId;
        this.originTimestamp = originTimestamp;
        this.subscriberNumber = subscriberNumber;
        this.transactionAmountRefill = transactionAmountRefill;
        this.externalData1 = externalData1;
        this.externalData2 = externalData2;
        this.decimalDenominator = decimalDenominator;
    }

    @Override
    public boolean supportsGetSubscriberInfo() {
        return true;
    }

    @Override
    public SubscriberInfo getSubscriberInfo() {
        SubscriberInfo info = null;
        BalanceEnquiryTCommand before = (BalanceEnquiryTCommand)getChain().get(0);
        if(before.getFullResultCode() != 0) {
            info = new SubscriberInfo();
            info.setAccountValue(0f);
            info.setBalanceBefore(0f);
            info.setSmsBalanceBefore(0);
            info.setMmsBalanceBefore(0);
            info.setMmsBalance(0);
            info.setSmsBalance(0);
            info.setSubscriberMSISDN(before.getSubscriberNumber());
            info.setServiceClassCurrent(0);
            info.setServiceClassBefore(0);
            info.setFreeCallBalance(0f);
            info.setLanguageID(1);
            return info;
        }
        if(getSize() > 2) {
            BalanceEnquiryTCommand after = (BalanceEnquiryTCommand)getChain().get(getSize() - 1);
            if(after.getFullResultCode() != 0) return before.getSubscriberInfo();
            info = after.getSubscriberInfo();
            info.setBalanceBefore(before.getSubscriberInfo().getAccountValue());
            info.setSmsBalanceBefore(before.getSubscriberInfo().getSmsBalance());
            info.setMmsBalanceBefore(before.getSubscriberInfo().getMmsBalance());
            info.setServiceClassBefore(before.getSubscriberInfo().getServiceClassCurrent());
            return info;
        }
        return null;
    }

    public MethodResponse execute() throws XmlRpcConnectionException, BadXmlFormatException {
        VoucherProfile profile = matchPaymentProfile(transactionAmountRefill);
        if(profile != null) {
            this.voucherProfileId = profile.getProfileID();
            this.voucherProfileName = profile.getProfileName();
        }
        AbstractCS3CP1Command cmd = new BalanceEnquiryTCommand(subscriberNumber, originTimestamp, originTransactionId, decimalDenominator, getSubscriberNoPrefix(), getSubscriberNumberNAI());
        addCommand(cmd);
        cmd.setXMLRPCClient(getXMLRPCClient());
        cmd.execute();
        if(cmd.isErrorOrFaultResponse()) return cmd.getResponse();
        balanceBefore = cmd.getSubscriberBalance(decimalDenominator);
        serviceClassOld = cmd.getServiceClassCurrent();
        balanceAfter = balanceBefore;

        if(willServiceClassChange(cmd.getServiceClassCurrent(), profile)) {
            Integer serviceClassCurrent = cmd.getServiceClassCurrent();
            cmd = new UpdateServiceClassTCommand(originTransactionId, originTimestamp, subscriberNumber, serviceClassCurrent, profile.getNewTemporaryServiceClass(), getSubscriberNoPrefix(), getSubscriberNumberNAI());
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

        cmd = new RefillTCommand(originTransactionId, originTimestamp, subscriberNumber, transactionAmountRefill, externalData1, externalData2, profile.getProfileID(), decimalDenominator, getSubscriberNoPrefix(), getSubscriberNumberNAI());
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
        
        cmd = new BalanceEnquiryTCommand(subscriberNumber, originTimestamp, originTransactionId, decimalDenominator, getSubscriberNoPrefix(), getSubscriberNumberNAI());
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

    protected VoucherProfiles getVoucherProfiles() {
        return VoucherProfilesLoader.instance().getVoucherProfiles();
    }

    private VoucherProfile matchPaymentProfile(Float transactionAmountRefill) {
        VoucherProfile profile = getVoucherProfiles().getByNominalValue(transactionAmountRefill);
        return profile;
    }

    private boolean willServiceClassChange(Integer currentServiceClass, VoucherProfile profile) {
        if(profile.getNewTemporaryServiceClass() == null) return false;
        if(profile.hasInFromServiceClasses(currentServiceClass)) return true;
        return false;
    }

    public String getOriginTransactionId() {
        return originTransactionId;
    }

    public Date getOriginTimestamp() {
        return originTimestamp;
    }

    public String getSubscriberNumber() {
        return subscriberNumber;
    }

    public Float getTransactionAmountRefill() {
        return transactionAmountRefill;
    }

    public String getExternalData1() {
        return externalData1;
    }

    public String getExternalData2() {
        return externalData2;
    }

    public Float getDecimalDenominator() {
        return decimalDenominator;
    }

    public Integer getServiceClassOld() {
        return serviceClassOld;
    }

    public Integer getServiceClassNew() {
        return serviceClassNew;
    }

    public Float getBalanceBefore() {
        return balanceBefore;
    }

    public String getVoucherProfileId() {
        return voucherProfileId;
    }

    public String getVoucherProfileName() {
        return voucherProfileName;
    }

    public Date getNewServiceClassExpiryDate() {
        return newServiceClassExpiryDate;
    }

    public Float getBalanceAfter() {
        return balanceAfter;
    }

    public Integer getUpdateServiceClassFlag() {
        return updateServiceClassFlag;
    }

    public Integer getRefillFlag() {
        return refillFlag;
    }

    public Integer getBalanceEnquiryFlag() {
        return balanceEnquiryFlag;
    }

}
