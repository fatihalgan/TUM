/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.cs3cp1.command;

import com.bridge.ena.cs3cp1.adjustment.policy.AdjustmentBase;
import com.bridge.ena.cs3cp1.adjustment.policy.AdjustmentRestrictions;
import com.bridge.ena.cs3cp1.adjustment.policy.DedicatedAccountAdjustment;
import com.bridge.ena.cs3cp1.adjustment.policy.DedicatedAccountAdjustmentValue;
import com.bridge.ena.cs3cp1.adjustment.policy.DedicatedAccountAdjustments;
import com.bridge.ena.cs3cp1.value.FAFAction;
import com.bridge.ena.util.DateUtils;
import com.bridge.ena.xmlrpc.IXMLRPCClient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author db2admin
 */
public class CommandFactory {

    private IXMLRPCClient xmlRpcClient;
    private String originHostName = null;
    private String subscriberNoPrefix;
    private Integer subscriberNumberNAI;

    public CommandFactory() {
        super();
    }
    
    public CommandFactory(IXMLRPCClient xmlRpcClient, String originHostName, String subscriberNoPrefix, Integer subscriberNumberNAI) {
        this.xmlRpcClient = xmlRpcClient;
        this.originHostName = originHostName;
        this.subscriberNoPrefix = subscriberNoPrefix;
        this.subscriberNumberNAI = subscriberNumberNAI;
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

    /**
     * @return the subscriberNoPrefix
     */
    public String getSubscriberNoPrefix() {
        return subscriberNoPrefix;
    }

    /**
     * @param subscriberNoPrefix the subscriberNoPrefix to set
     */
    public void setSubscriberNoPrefix(String subscriberNoPrefix) {
        this.subscriberNoPrefix = subscriberNoPrefix;
    }

    /**
     * @return the subscriberNumberNAI
     */
    public Integer getSubscriberNumberNAI() {
        return subscriberNumberNAI;
    }

    /**
     * @param subscriberNumberNAI the subscriberNumberNAI to set
     */
    public void setSubscriberNumberNAI(Integer subscriberNumberNAI) {
        this.subscriberNumberNAI = subscriberNumberNAI;
    }
    
    public AdjustmentTCommand getAdjustmentTCommand(String originTransactionId, Date originTimestamp, String subscriberNumber,
        Float adjustmentAmount, String externalData1, String externalData2, int supervisionFeeIncrease,
        int serviceFeeIncrease,  int freeSMS, int freeMMS, float decimalDenominator) {
        AdjustmentTCommand cmd = new AdjustmentTCommand(originTransactionId, originTimestamp, subscriberNumber, adjustmentAmount, externalData1, externalData2, supervisionFeeIncrease, serviceFeeIncrease, freeSMS, freeMMS, null, decimalDenominator, getSubscriberNoPrefix(), getSubscriberNumberNAI());
        cmd.setXMLRPCClient(getXmlRpcClient());
        cmd.setOriginHostName(getOriginHostName());
        return cmd;
    }

    public AdjustmentTCommand getFreeCallAdjustmentTCommand(String originTransactionId, Date originTimestamp, String subscriberNumber,
        Float adjustmentAmount, String externalData1, String externalData2, int supervisionFeeIncrease,
        int serviceFeeIncrease,  int freeSMS, int freeMMS, Float freeCall, int freeCallDedicatedAccountId, float decimalDenominator) {
        AdjustmentRestrictions restrictions = new AdjustmentRestrictions();
        DedicatedAccountAdjustment daadj = new DedicatedAccountAdjustment(freeCallDedicatedAccountId, DateUtils.findDayBefore(new Date()),
            DateUtils.findDayAfter(new Date()), new DedicatedAccountAdjustmentValue(
                    AdjustmentBase.FreeAmount, freeCall), restrictions);
        List<DedicatedAccountAdjustment> daadjList = new ArrayList<DedicatedAccountAdjustment>();
        daadjList.add(daadj);
        DedicatedAccountAdjustments adjustments = new DedicatedAccountAdjustments(daadjList, adjustmentAmount, decimalDenominator, "");
        AdjustmentTCommand cmd = new AdjustmentTCommand(originTransactionId, originTimestamp, subscriberNumber, adjustmentAmount, externalData1, externalData2, supervisionFeeIncrease, serviceFeeIncrease, freeSMS, freeMMS, adjustments, decimalDenominator, getSubscriberNoPrefix(), getSubscriberNumberNAI());
        cmd.setXMLRPCClient(getXmlRpcClient());
        cmd.setOriginHostName(getOriginHostName());
        return cmd;
    }
    
    public AdjustmentTCommand getAdjustmentTCommand(String originTransactionId, Date originTimestamp, String subscriberNumber,
        Float adjustmentAmount, String externalData1, String externalData2, int supervisionFeeIncrease,
        int serviceFeeIncrease,  int freeSMS, int freeMMS, DedicatedAccountAdjustments dedicatedAccountAdjustments, float decimalDenominator) {
        
        AdjustmentTCommand cmd = new AdjustmentTCommand(originTransactionId, originTimestamp, subscriberNumber, adjustmentAmount, externalData1, externalData2, supervisionFeeIncrease, serviceFeeIncrease, freeSMS, freeMMS, dedicatedAccountAdjustments, decimalDenominator, getSubscriberNoPrefix(), getSubscriberNumberNAI());
        cmd.setXMLRPCClient(getXmlRpcClient());
        cmd.setOriginHostName(getOriginHostName());
        return cmd;
    }
    
    public BalanceEnquiryTCommand getBalanceEnquiryTCommand(String subscriberNumber, Date originTimestamp, String originTransactionID,
        float decimalDenominator) {
        BalanceEnquiryTCommand cmd = new BalanceEnquiryTCommand(subscriberNumber, originTimestamp, originTransactionID, decimalDenominator, getSubscriberNoPrefix(), getSubscriberNumberNAI());
        cmd.setXMLRPCClient(getXmlRpcClient());
        cmd.setOriginHostName(getOriginHostName());
        return cmd;
    }
    
    public GetAccountDetailsTCommand getAccountDetailsTCommand(String originTransactionId, Date originTimestamp, String subscriberNumber,
        float decimalDenominator) {
        GetAccountDetailsTCommand cmd = new GetAccountDetailsTCommand(originTransactionId, originTimestamp, subscriberNumber, decimalDenominator, getSubscriberNoPrefix(), getSubscriberNumberNAI());
        cmd.setXMLRPCClient(getXmlRpcClient());
        cmd.setOriginHostName(getOriginHostName());
        return cmd;
    }
    
    public UpdateFaFTCommand getUpdateFaFTCommand(String originTransactionId, Date originTimestamp, String subscriberNumber,
            List<String> fafNumbers, FAFAction fafAction) {
    	UpdateFaFTCommand cmd = new UpdateFaFTCommand(originTransactionId, originTimestamp, subscriberNumber,fafNumbers, fafAction, getSubscriberNoPrefix(), getSubscriberNumberNAI());
        cmd.setXMLRPCClient(getXmlRpcClient());
        cmd.setOriginHostName(getOriginHostName());
        return cmd;
    }
    
    public UpdateServiceClassTCommand getUpdateServiceClassTCommand(String originTransactionId, Date originTimestamp, String subscriberNumber,
            Integer serviceClassCurrent, Integer serviceClassNew) {
    	UpdateServiceClassTCommand cmd = new UpdateServiceClassTCommand(originTransactionId, originTimestamp, subscriberNumber,serviceClassCurrent, serviceClassNew, getSubscriberNoPrefix(), getSubscriberNumberNAI());
        cmd.setXMLRPCClient(getXmlRpcClient());
        cmd.setOriginHostName(getOriginHostName());
        return cmd;
    }
    
    public GetAllowedServiceClassChangesTCommand getAllowedServiceClassChangesTCommand(String originTransactionId, Date originTimestamp, String subscriberNumber) {
    	GetAllowedServiceClassChangesTCommand cmd = new GetAllowedServiceClassChangesTCommand(originTransactionId, originTimestamp, subscriberNumber,getSubscriberNoPrefix(), getSubscriberNumberNAI());
        cmd.setXMLRPCClient(getXmlRpcClient());
        cmd.setOriginHostName(getOriginHostName());
        return cmd;
    }
    
    public GetFaFListTCommand getFaFListTCommand(String originTransactionId, Date originTimestamp, String subscriberNumber) {
    	GetFaFListTCommand cmd = new GetFaFListTCommand(originTransactionId, originTimestamp, subscriberNumber, getSubscriberNoPrefix(), getSubscriberNumberNAI());
        cmd.setXMLRPCClient(getXmlRpcClient());
        cmd.setOriginHostName(getOriginHostName());
        return cmd;
    }
    
    public TariffedAdjustmentCommand getTariffedAdjustmentCommand(String originTransactionId, Date originTimestamp, String subscriberNumber,
        float adjustmentAmount, String externalData1, String externalData2, int freeSMS, int freeMMS, 
        float bonus, int makeCallTimeFrame, int receiveCallTimeFrame, float decimalDenominator) {
        TariffedAdjustmentCommand cmd = new TariffedAdjustmentCommand(originTransactionId, originTimestamp, subscriberNumber, adjustmentAmount, externalData1, externalData2, freeSMS, freeMMS, bonus, makeCallTimeFrame, receiveCallTimeFrame, decimalDenominator, getSubscriberNoPrefix(), getSubscriberNumberNAI());
        cmd.setXMLRPCClient(getXmlRpcClient());
        cmd.setOriginHostName(getOriginHostName());
        return cmd;
    }

    public RefillTCommand getRefillTCommand(String originTransactionId, Date originTimestamp, String subscriberNumber,
        Float transactionAmountRefill, String externalData1, String externalData2, String paymentProfileID,
        float decimalDenominator) {
        RefillTCommand cmd = new RefillTCommand(originTransactionId, originTimestamp, subscriberNumber, transactionAmountRefill,
            externalData1, externalData2, paymentProfileID, decimalDenominator, getSubscriberNoPrefix(), getSubscriberNumberNAI());
        cmd.setXMLRPCClient(getXmlRpcClient());
        cmd.setOriginHostName(getOriginHostName());
        return cmd;
    }

    public RefillTCommand getRefillTCommand(String originTransactionId, Date originTimestamp, String subscriberNumber,
        Float transactionAmountRefill, String externalData1, String externalData2, float decimalDenominator) {
        RefillTCommand cmd = new RefillTCommand(originTransactionId, originTimestamp, subscriberNumber, transactionAmountRefill,
            externalData1, externalData2, decimalDenominator, getSubscriberNoPrefix(), getSubscriberNumberNAI());
        cmd.setXMLRPCClient(getXmlRpcClient());
        cmd.setOriginHostName(getOriginHostName());
        return cmd;
    }


    public StandartVoucherRefillTCommand getStandartVoucherRefillTCommand(String originTransactionId, Date originTimestamp, String subscriberNumber,
        String activationNumber, String externalData1, String externalData2, float decimalDenominator) {
        StandartVoucherRefillTCommand cmd = new StandartVoucherRefillTCommand(originTransactionId, originTimestamp, subscriberNumber, activationNumber, externalData1, externalData2, decimalDenominator, subscriberNoPrefix, subscriberNumberNAI);
        cmd.setXMLRPCClient(getXmlRpcClient());
        cmd.setOriginHostName(getOriginHostName());
        return cmd;
    }

    public ValueVoucherRefillTCommand getValueVoucherRefillTCommand(String originTransactionId, Date originTimestamp, String subscriberNumber,
        String activationNumber, String externalData1, String externalData2, float decimalDenominator) {
        ValueVoucherRefillTCommand cmd = new ValueVoucherRefillTCommand(originTransactionId, originTimestamp, subscriberNumber, activationNumber, externalData1, externalData2, decimalDenominator, subscriberNoPrefix, subscriberNumberNAI);
        cmd.setXMLRPCClient(getXmlRpcClient());
        cmd.setOriginHostName(getOriginHostName());
        return cmd;
    }

    public VoucherRefillTCommand getVoucherRefillTCommand(String originTransactionId, Date originTimestamp, String subscriberNumber,
        String activationNumber, String externalData1, String externalData2, float decimalDenominator) {
        VoucherRefillTCommand cmd = new VoucherRefillTCommand(originTransactionId, originTimestamp, subscriberNumber, activationNumber, externalData1, externalData2, decimalDenominator, subscriberNoPrefix, subscriberNumberNAI);
        cmd.setXMLRPCClient(getXmlRpcClient());
        cmd.setOriginHostName(getOriginHostName());
        return cmd;
    }

    public PinlessRefillTCommand getPinlessRefillTCommand(String originTransactionId, Date originTimestamp, String subscriberNumber,
        Float transactionAmountRefill, String externalData1, String externalData2, float decimalDenominator) {
        PinlessRefillTCommand cmd = new PinlessRefillTCommand(originTransactionId, originTimestamp, subscriberNumber, transactionAmountRefill,
            externalData1, externalData2, decimalDenominator, getSubscriberNoPrefix(), getSubscriberNumberNAI());
        cmd.setXMLRPCClient(getXmlRpcClient());
        cmd.setOriginHostName(getOriginHostName());
        return cmd;
    }

    public PinlessFreeAmountRefillTCommand getPinlessFreeAmountRefillTCommand(String originTransactionId, Date originTimestamp, String subscriberNumber,
        Float transactionAmountRefill, String externalData1, String externalData2, float decimalDenominator) {
        PinlessFreeAmountRefillTCommand cmd = new PinlessFreeAmountRefillTCommand(originTransactionId, originTimestamp, subscriberNumber, transactionAmountRefill,
            externalData1, externalData2, decimalDenominator, getSubscriberNoPrefix(), getSubscriberNumberNAI());
        cmd.setXMLRPCClient(getXmlRpcClient());
        cmd.setOriginHostName(getOriginHostName());
        return cmd;
    }
}
