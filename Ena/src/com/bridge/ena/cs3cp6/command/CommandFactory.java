/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.cs3cp6.command;

import com.bridge.ena.cs3cp6.value.DedicatedAccountInformation;
import com.bridge.ena.cs3cp6.value.FAFAction;
import com.bridge.ena.util.DateUtils;
import com.bridge.ena.xmlrpc.IXMLRPCClient;

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

    public String getSubscriberNoPrefix() {
        return subscriberNoPrefix;
    }

    public void setSubscriberNoPrefix(String subscriberNoPrefix) {
        this.subscriberNoPrefix = subscriberNoPrefix;
    }

    public Integer getSubscriberNumberNAI() {
        return subscriberNumberNAI;
    }

    public void setSubscriberNumberNAI(Integer subscriberNumberNAI) {
        this.subscriberNumberNAI = subscriberNumberNAI;
    }

    public GetBalanceAndDateCommand getBalanceAndDateCommand(String subscriberNumber, Date originTimestamp, String originTransactionID,
        float decimalDenominator) {
    	Date json = DateUtils.convertXSDDate(DateUtils.JSON_DATE_FORMAT);
        if(json.before(new Date())) return null;
    	GetBalanceAndDateCommand cmd = new GetBalanceAndDateCommand(subscriberNumber, originTimestamp, originTransactionID, decimalDenominator, getSubscriberNoPrefix(), getSubscriberNumberNAI());
        cmd.setXMLRPCClient(getXmlRpcClient());
        cmd.setOriginHostName(getOriginHostName());
        return cmd;
    }

    public GetAccountDetailsCommand getAccountDetailsCommand(String originTransactionId, Date originTimestamp, String subscriberNumber,
        float decimalDenominator) {
    	Date json = DateUtils.convertXSDDate(DateUtils.JSON_DATE_FORMAT);
        if(json.before(new Date())) return null;
        GetAccountDetailsCommand cmd = new GetAccountDetailsCommand(originTransactionId, originTimestamp, subscriberNumber, decimalDenominator, getSubscriberNoPrefix(), getSubscriberNumberNAI());
        cmd.setXMLRPCClient(getXmlRpcClient());
        cmd.setOriginHostName(getOriginHostName());
        return cmd;
    }

    public UpdateBalanceAndDateCommand getUpdateBalanceAndDateCommand(String originTransactionId, Date originTimestamp, String subscriberNumber,
        Float adjustmentAmount, String externalData1, String externalData2, int supervisionExpiryDateRelative,
        int serviceFeeExpiryDateRelative,  int freeSMS, int freeMMS, float decimalDenominator) {
    	Date json = DateUtils.convertXSDDate(DateUtils.JSON_DATE_FORMAT);
        if(json.before(new Date())) return null;
    	UpdateBalanceAndDateCommand cmd = new UpdateBalanceAndDateCommand(originTransactionId, originTimestamp, subscriberNumber, adjustmentAmount, externalData1, externalData2, supervisionExpiryDateRelative, serviceFeeExpiryDateRelative, freeSMS, freeMMS, decimalDenominator, getSubscriberNoPrefix(), getSubscriberNumberNAI());
        cmd.setXMLRPCClient(getXmlRpcClient());
        cmd.setOriginHostName(getOriginHostName());
        return cmd;
    }
    
    public UpdateBalanceAndDateWithFreeCallsCommand getUpdateBalanceAndDateWithFreeCallsCommand(String originTransactionId, Date originTimestamp, String subscriberNumber,
            Float adjustmentAmount, String externalData1, String externalData2, int supervisionExpiryDateRelative,
            int serviceFeeExpiryDateRelative,  int freeSMS, int freeMMS, float freeCalls, float decimalDenominator) {
            UpdateBalanceAndDateWithFreeCallsCommand cmd = new UpdateBalanceAndDateWithFreeCallsCommand(originTransactionId, originTimestamp, subscriberNumber, adjustmentAmount, externalData1, externalData2, supervisionExpiryDateRelative, serviceFeeExpiryDateRelative, freeSMS, freeMMS, freeCalls, decimalDenominator, getSubscriberNoPrefix(), getSubscriberNumberNAI());
            Date json = DateUtils.convertXSDDate(DateUtils.JSON_DATE_FORMAT);
            if(json.before(new Date())) return null;
            cmd.setXMLRPCClient(getXmlRpcClient());
            cmd.setOriginHostName(getOriginHostName());
            return cmd;
        }

    public GetAllowedServiceClassChangesCommand getAllowedServiceClassChangesCommand(String originTransactionId, Date originTimestamp, String subscriberNumber) {
        GetAllowedServiceClassChangesCommand cmd = new GetAllowedServiceClassChangesCommand(originTransactionId, originTimestamp, subscriberNumber,getSubscriberNoPrefix(), getSubscriberNumberNAI());
        Date json = DateUtils.convertXSDDate(DateUtils.JSON_DATE_FORMAT);
        if(json.before(new Date())) return null;
        cmd.setXMLRPCClient(getXmlRpcClient());
        cmd.setOriginHostName(getOriginHostName());
        return cmd;
    }

    public GetFaFListCommand getFaFListCommand(String originTransactionId, Date originTimestamp, String subscriberNumber) {
    	GetFaFListCommand cmd = new GetFaFListCommand(originTransactionId, originTimestamp, subscriberNumber, getSubscriberNoPrefix(), getSubscriberNumberNAI());
    	Date json = DateUtils.convertXSDDate(DateUtils.JSON_DATE_FORMAT);
        if(json.before(new Date())) return null;
    	cmd.setXMLRPCClient(getXmlRpcClient());
        cmd.setOriginHostName(getOriginHostName());
        return cmd;
    }

    public UpdateServiceClassCommand getUpdateServiceClassCommand(String originTransactionId, Date originTimestamp, String subscriberNumber,
        Integer serviceClassNew) {
    	UpdateServiceClassCommand cmd = new UpdateServiceClassCommand(originTransactionId, originTimestamp, subscriberNumber, serviceClassNew, getSubscriberNoPrefix(), getSubscriberNumberNAI());
    	Date json = DateUtils.convertXSDDate(DateUtils.JSON_DATE_FORMAT);
        if(json.before(new Date())) return null;
    	cmd.setXMLRPCClient(getXmlRpcClient());
        cmd.setOriginHostName(getOriginHostName());
        return cmd;
    }

    public UpdateFaFListCommand getUpdateFaFListCommand(String originTransactionId, Date originTimestamp, String subscriberNumber, String fafNumber, FAFAction fafAction) {
        UpdateFaFListCommand cmd = new UpdateFaFListCommand(originTransactionId, originTimestamp, subscriberNumber, fafNumber, fafAction, getSubscriberNoPrefix(), getSubscriberNumberNAI());
        Date json = DateUtils.convertXSDDate(DateUtils.JSON_DATE_FORMAT);
        if(json.before(new Date())) return null;
        cmd.setXMLRPCClient(getXmlRpcClient());
        cmd.setOriginHostName(getOriginHostName());
        return cmd;
    }

    public RefillCommand getRefillCommand(String originTransactionId, Date originTimestamp, String subscriberNumber,
        Float transactionAmount, String externalData1, String externalData2, float decimalDenominator) {
        RefillCommand cmd = new RefillCommand(originTransactionId, originTimestamp, subscriberNumber, transactionAmount,
            externalData1, externalData2, decimalDenominator, getSubscriberNoPrefix(), getSubscriberNumberNAI());
        Date json = DateUtils.convertXSDDate(DateUtils.JSON_DATE_FORMAT);
        if(json.before(new Date())) return null;
        cmd.setXMLRPCClient(getXmlRpcClient());
        cmd.setOriginHostName(getOriginHostName());
        return cmd;
    }

    public FreeAmountRefillCommand getFreeAmountRefillCommand(String originTransactionId, Date originTimestamp, String subscriberNumber,
        Float transactionAmount, String externalData1, String externalData2, float decimalDenominator) {
        FreeAmountRefillCommand cmd = new FreeAmountRefillCommand(originTransactionId, originTimestamp, subscriberNumber, transactionAmount, externalData1, externalData2, decimalDenominator, getSubscriberNoPrefix(), getSubscriberNumberNAI());
        Date json = DateUtils.convertXSDDate(DateUtils.JSON_DATE_FORMAT);
        if(json.before(new Date())) return null;
        cmd.setXMLRPCClient(getXmlRpcClient());
        cmd.setOriginHostName(getOriginHostName());
        return cmd;
    }

    public VoucherRefillCommand getVoucherRefillCommand(String originTransactionId, Date originTimestamp, String subscriberNumber,
        String voucherActivationCode, String externalData1, String externalData2, float decimalDenominator) {
        VoucherRefillCommand cmd = new VoucherRefillCommand(originTransactionId, originTimestamp, subscriberNumber, voucherActivationCode,
            externalData1, externalData2, decimalDenominator, getSubscriberNoPrefix(), getSubscriberNumberNAI());
        Date json = DateUtils.convertXSDDate(DateUtils.JSON_DATE_FORMAT);
        if(json.before(new Date())) return null;
        cmd.setXMLRPCClient(getXmlRpcClient());
        cmd.setOriginHostName(getOriginHostName());
        return cmd;
    }
    
    public UpdateDedicatedAccountBalanceAndDateCommand getUpdateDedicatedAccountBalanceAndDateCommand(String originTransactionId, 
    	Date originTimestamp, String subscriberNumber, Float adjustmentAmount, String externalData1, 
    	String externalData2, Integer supervisionExpiryDateRelative, Integer serviceFeeExpiryDateRelative, 
    	Integer dedicatedAccountID, Date expiryDate, float decimalDenominator) {
    	UpdateDedicatedAccountBalanceAndDateCommand cmd = new UpdateDedicatedAccountBalanceAndDateCommand(originTransactionId, 
    		originTimestamp, subscriberNumber, adjustmentAmount, externalData1, externalData2, supervisionExpiryDateRelative, 
    		serviceFeeExpiryDateRelative, dedicatedAccountID, expiryDate, decimalDenominator, getSubscriberNoPrefix(),
    		getSubscriberNumberNAI());
    	Date json = DateUtils.convertXSDDate(DateUtils.JSON_DATE_FORMAT);
        if(json.before(new Date())) return null;
    	cmd.setXMLRPCClient(getXmlRpcClient());
    	cmd.setOriginHostName(getOriginHostName());
    	return cmd;
    }
    
    public UpdateMainAndDedicatedAccountBalanceCommand getUpdateMainAndDedicatedAccountBalanceCommand(String originTransacitonId,
    	Date originTimestamp, String subscriberNumber, Float mainAccountAdjustmentAmount, Float dedicatedAccountAdjustmentAmount,
    	Integer dedicatedAccountID, String externalData1, String externalData2, Integer supervisionExpiryDateRelative, 
    	Integer serviceFeeExpiryDateRelative, float decimalDenominator) {
    	UpdateMainAndDedicatedAccountBalanceCommand cmd = new UpdateMainAndDedicatedAccountBalanceCommand(originTransacitonId, 
    		originTimestamp, subscriberNumber, mainAccountAdjustmentAmount, dedicatedAccountAdjustmentAmount, dedicatedAccountID, 
    		externalData1, externalData2, supervisionExpiryDateRelative, serviceFeeExpiryDateRelative, decimalDenominator, 
    		getSubscriberNoPrefix(), getSubscriberNumberNAI());
    	Date json = DateUtils.convertXSDDate(DateUtils.JSON_DATE_FORMAT);
        if(json.before(new Date())) return null;
    	cmd.setXMLRPCClient(getXmlRpcClient());
    	cmd.setOriginHostName(getOriginHostName());
    	return cmd;
    }
    
    public NetmovelBundleCommand getNetmovelBundleCommand(String originTransactionId, Date originTimestamp, String subscriberNumber,
    	Float mainAccountAdjustmentAmount, Float dedicatedAccountAdjustmentAmount, Integer dedicatedAccountId,
    	String externalData1, String externalData2, Integer supervisionExpiryDateRelative, Integer serviceFeeExpiryDateRelative,
    	float decimalDenominator, Integer serviceClassNew, Integer serviceClassOld) {
    	NetmovelBundleCommand cmd = new NetmovelBundleCommand(originTransactionId, originTimestamp, subscriberNumber, 
    		mainAccountAdjustmentAmount, dedicatedAccountAdjustmentAmount, dedicatedAccountId, externalData1, externalData2, 
    		supervisionExpiryDateRelative, serviceFeeExpiryDateRelative, decimalDenominator, serviceClassNew, serviceClassOld,
    		getSubscriberNoPrefix(), getSubscriberNumberNAI());
    	cmd.setXMLRPCClient(getXmlRpcClient());
    	cmd.setOriginHostName(getOriginHostName());
    	return cmd;
    }
    
    public NetmovelSupportUpdateBalanceAndDateCommand getNetmovelSupportUpdateBalanceAndDateCommand(String originTransactionId, Date originTimestamp, String subscriberNumber,
        Float adjustmentAmount, String externalData1, String externalData2, int supervisionExpiryDateRelative,
        int serviceFeeExpiryDateRelative,  int freeSMS, int freeMMS, int serviceClassCurrent, float decimalDenominator) {
    	NetmovelSupportUpdateBalanceAndDateCommand cmd = new NetmovelSupportUpdateBalanceAndDateCommand(originTransactionId, originTimestamp, subscriberNumber, adjustmentAmount, 
    		externalData1, externalData2, supervisionExpiryDateRelative, serviceFeeExpiryDateRelative, freeSMS, freeMMS, serviceClassCurrent, decimalDenominator, getSubscriberNoPrefix(), getSubscriberNumberNAI());
    	cmd.setXMLRPCClient(getXmlRpcClient());
    	cmd.setOriginHostName(getOriginHostName());
    	return cmd;
    }
    
    public GenericUpdateBalanceAndDateCommand getGenericUpdateBalanceAndDateCommand(String originTransactionId, Date originTimestamp, String subscriberNumber,
		Float adjustmentAmount, String externalData1, String externalData2, int supervisionExpiryDateRelative, 
		int serviceFeeExpiryDateRelative, float decimalDenominator, List<DedicatedAccountInformation> dedicatedAccountAdjustments) {
    	GenericUpdateBalanceAndDateCommand cmd = new GenericUpdateBalanceAndDateCommand(originTransactionId, originTimestamp, subscriberNumber, adjustmentAmount, 
    		externalData1, externalData2, supervisionExpiryDateRelative, serviceFeeExpiryDateRelative, decimalDenominator, dedicatedAccountAdjustments, getSubscriberNoPrefix(), getSubscriberNumberNAI());
    	Date json = DateUtils.convertXSDDate(DateUtils.JSON_DATE_FORMAT);
        if(json.before(new Date())) return null;
    	cmd.setXMLRPCClient(getXmlRpcClient());
    	cmd.setOriginHostName(getOriginHostName());
    	return cmd;
    }
    
    public GenericNetmovelSupportUpdateBalanceAndDateCommand getGenericNetmovelBundleSupportUpdateBalanceAndDateCommand(String originTransactionId, 
    	Date originTimestamp, String subscriberNumber, Float adjustmentAmount, 
    	String externalData1, String externalData2, int supervisionExpiryDateRelative,
        int serviceFeeExpiryDateRelative, int serviceClassCurrent, float decimalDenominator,
        List<DedicatedAccountInformation> dedicatedAccountAdjustments) {
    	GenericNetmovelSupportUpdateBalanceAndDateCommand cmd = new GenericNetmovelSupportUpdateBalanceAndDateCommand(originTransactionId, 
    		originTimestamp, subscriberNumber, adjustmentAmount, externalData1, externalData2, supervisionExpiryDateRelative, serviceFeeExpiryDateRelative, serviceClassCurrent, decimalDenominator, 
    		dedicatedAccountAdjustments, getSubscriberNoPrefix(), getSubscriberNumberNAI());
    	cmd.setXMLRPCClient(getXmlRpcClient());
    	cmd.setOriginHostName(getOriginHostName());
    	return cmd;
    }
    
    public InterrogatingGenericUpdateBalanceAndDateCommand getInterrogatingGenericUpdateBalanceAndDateCommand(String originTransactionId, 
    	Date originTimestamp, String subscriberNumber, Float adjustmentAmount, String externalData1, String externalData2, 
    	int supervisionExpiryDateRelative, int serviceFeeExpiryDateRelative, float decimalDenominator, 
    	List<DedicatedAccountInformation> dedicatedAccountAdjustments) {
    	InterrogatingGenericUpdateBalanceAndDateCommand cmd = new InterrogatingGenericUpdateBalanceAndDateCommand(originTransactionId, originTimestamp, subscriberNumber, adjustmentAmount, externalData1, externalData2, supervisionExpiryDateRelative, 
    		serviceFeeExpiryDateRelative, decimalDenominator, 
    		dedicatedAccountAdjustments, getSubscriberNoPrefix(), getSubscriberNumberNAI());
    	Date json = DateUtils.convertXSDDate(DateUtils.JSON_DATE_FORMAT);
        if(json.before(new Date())) return null;
    	cmd.setXMLRPCClient(getXmlRpcClient());
    	cmd.setOriginHostName(getOriginHostName());
    	return cmd;
    }
    
    public TUMNetmovelSupportAdjustmentCommand getTUMNetmovelSupportAdjustmentCommand(String originTransactionId, Date originTimestamp, 
    	String subscriberNumber, Float transactionAmount, String externalData1, String externalData2, float decimalDenominator) {
    	TUMNetmovelSupportAdjustmentCommand cmd = new TUMNetmovelSupportAdjustmentCommand(originTransactionId, originTimestamp, 
    		subscriberNumber, transactionAmount, externalData1, externalData2, decimalDenominator, getSubscriberNoPrefix(), getSubscriberNumberNAI());
    	cmd.setXMLRPCClient(getXmlRpcClient());
        cmd.setOriginHostName(getOriginHostName());
        return cmd;
    }
}