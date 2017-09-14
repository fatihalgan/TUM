/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.cs3cp1.command;

import com.bridge.ena.util.ServiceSupervisionFeeUtil;
import com.bridge.ena.cs.command.CompositeCSCommand;
import com.bridge.ena.cs3cp1.adjustment.policy.DedicatedAccountAdjustmentPolicyLoader;
import com.bridge.ena.cs3cp1.adjustment.policy.DedicatedAccountAdjustments;
import java.util.Date;

import org.apache.http.HttpStatus;

import com.bridge.ena.cs.value.SubscriberInfo;
import com.bridge.ena.xml.BadXmlFormatException;
import com.bridge.ena.xmlrpc.exception.XmlRpcConnectionException;
import com.bridge.ena.xmlrpc.serializer.MethodResponse;

/**
 *
 * @author db2admin
 */
public class TariffedAdjustmentCommand extends CompositeCSCommand<AbstractCS3CP1Command> {
    private String originTransactionId;
    private Date originTimestamp;
    private String subscriberNumber;
    private Float adjustmentAmount;
    private String externalData1;
    private String externalData2;
    private Integer freeSMS;
    private Integer freeMMS;
    private Float bonus;
    private Integer makeCallTimeFrame;
    private Integer receiveCallTimeFrame;
    private Float decimalDenominator;
    private int calculatedMakeCallIncrease;
    private int calculatedReceiveCallIncrease;
            
    TariffedAdjustmentCommand(String originTransactionId, Date originTimestamp, String subscriberNumber,
        float adjustmentAmount, String externalData1, String externalData2, int freeSMS, int freeMMS, 
        float bonus, int makeCallTimeFrame, int receiveCallTimeFrame, float decimalDenominator,
        String subscriberNoPrefix, Integer subscriberNumberNAI) {
        super(subscriberNoPrefix, subscriberNumberNAI);
        this.originTransactionId = originTransactionId;
        this.originTimestamp = originTimestamp;
        this.subscriberNumber = subscriberNumber;
        this.adjustmentAmount = adjustmentAmount;
        this.externalData1 = externalData1;
        this.externalData2 = externalData2;
        this.freeSMS = freeSMS;
        this.freeMMS = freeMMS;
        this.bonus = bonus;
        this.makeCallTimeFrame = makeCallTimeFrame;
        this.receiveCallTimeFrame = receiveCallTimeFrame;
        this.decimalDenominator = decimalDenominator;
    }

    public MethodResponse execute() throws XmlRpcConnectionException, BadXmlFormatException {
        AbstractCS3CP1Command cmd = new GetAccountDetailsTCommand(originTransactionId, originTimestamp, subscriberNumber, decimalDenominator, getSubscriberNoPrefix(), getSubscriberNumberNAI());
        addCommand(cmd);
        cmd.setXMLRPCClient(getXMLRPCClient());
        cmd.execute();
        if(cmd.isErrorOrFaultResponse()) return cmd.getResponse();
        calculatedReceiveCallIncrease = ServiceSupervisionFeeUtil.findServiceFeeDateIncreaseDates(receiveCallTimeFrame, cmd.getServiceFeeExpiryDate());
        calculatedMakeCallIncrease = ServiceSupervisionFeeUtil.findSupervisionDateIncreaseDates(makeCallTimeFrame, cmd.getSupervisionExpiryDate());
        float amountWithBonus = findBonus(bonus, adjustmentAmount);
        //TODO: Create DedicatedAccountAdjustments here
        DedicatedAccountAdjustments dedicatedAccountAdjustments = getDedicatedAccountAdjustmentPolicies();
        if(dedicatedAccountAdjustments != null) dedicatedAccountAdjustments.setCurrentServiceClass(cmd.getServiceClassCurrent().toString());
        cmd = new AdjustmentTCommand(originTransactionId, originTimestamp, subscriberNumber, 
            amountWithBonus, externalData1, externalData2, calculatedMakeCallIncrease, 
            calculatedReceiveCallIncrease, freeSMS, freeMMS, dedicatedAccountAdjustments, decimalDenominator, getSubscriberNoPrefix(), getSubscriberNumberNAI());
        addCommand(cmd);
        cmd.setXMLRPCClient(getXMLRPCClient());
        cmd.execute();
        return cmd.getResponse();
    }
    
    private DedicatedAccountAdjustments getDedicatedAccountAdjustmentPolicies() {
        return DedicatedAccountAdjustmentPolicyLoader.instance().getDedicatedAccountAdjustments(adjustmentAmount, decimalDenominator);               
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

    public Float getAdjustmentAmount() {
        return adjustmentAmount;
    }

    public String getExternalData1() {
        return externalData1;
    }

    public String getExternalData2() {
        return externalData2;
    }

    public Integer getFreeSMS() {
        return freeSMS;
    }

    public Integer getFreeMMS() {
        return freeMMS;
    }
    
    public Float getBonus() {
        return bonus;
    }

    public Integer getMakeCallTimeFrame() {
        return makeCallTimeFrame;
    }

    public Integer getReceiveCallTimeFrame() {
        return receiveCallTimeFrame;
    }

    public Float getDecimalDenominator() {
        return decimalDenominator;
    }
    
    private Float findBonus(Float bonus, Float amount) {
        Float amountWithBonus = amount + bonus;
        return amountWithBonus;
    }

    public int getCalculatedMakeCallIncrease() {
        return calculatedMakeCallIncrease;
    }

    public int getCalculatedReceiveCallIncrease() {
        return calculatedReceiveCallIncrease;
    }

    @Override
    public boolean supportsGetSubscriberInfo() {
        return true;
    }

    @Override
    public SubscriberInfo getSubscriberInfo() {
        SubscriberInfo info = null;
        GetAccountDetailsTCommand before = (GetAccountDetailsTCommand)getChain().get(0);
        if((before.getHttpStatusCode() != HttpStatus.SC_OK) || before.isErrorOrFaultResponse()) {
            info = new SubscriberInfo();
            info.setAccountValue(0f);
            info.setBalanceBefore(0f);
            info.setMmsBalance(0);
            info.setSmsBalance(0);
            info.setSmsBalanceBefore(0);
            info.setMmsBalanceBefore(0);
            info.setSubscriberMSISDN(before.getSubscriberNumber());
            info.setServiceClassCurrent(0);
            info.setFreeCallBalance(0f);
            info.setLanguageID(1);
            return info;
        }
        AdjustmentTCommand after = (AdjustmentTCommand)getChain().get(1);
        if((after.getHttpStatusCode() != HttpStatus.SC_OK) || after.isErrorOrFaultResponse()) return before.getSubscriberInfo();
        info = before.getSubscriberInfo();
        info.setBalanceBefore(info.getAccountValue());
        info.setAccountValue(after.getAdjustmentAmount() + info.getBalanceBefore());
        info.setSmsBalanceBefore(before.getSubscriberInfo().getSmsBalance());
        info.setMmsBalanceBefore(before.getSubscriberInfo().getMmsBalance());
        info.setServiceClassBefore(before.getSubscriberInfo().getServiceClassCurrent());
        return info;
    }

}
