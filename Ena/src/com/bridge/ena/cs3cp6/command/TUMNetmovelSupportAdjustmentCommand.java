package com.bridge.ena.cs3cp6.command;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bridge.ena.cs.command.AbstractCSCommand;
import com.bridge.ena.cs.command.CompositeCSCommand;
import com.bridge.ena.cs.value.SubscriberInfo;
import com.bridge.ena.cs3cp6.value.DedicatedAccountInformation;
import com.bridge.ena.xml.BadXmlFormatException;
import com.bridge.ena.xmlrpc.exception.XmlRpcConnectionException;
import com.bridge.ena.xmlrpc.serializer.MethodResponse;

public class TUMNetmovelSupportAdjustmentCommand extends CompositeCSCommand<AbstractCS3CP6Command> {

	 private static Log logger = LogFactory.getLog(TUMNetmovelSupportAdjustmentCommand.class);
	 
	 private String originTransactionId;
	 private Date originTimestamp;
	 private String subscriberNumber;
	 private Float transactionAmount;
	 private String externalData1;
	 private String externalData2;
	 private Float decimalDenominator;
	 private Float refillPortion;
	 private Float adjustmentPortion;
	 private Integer languageIDCurrent;
	 private Integer adjustmentFlag = -1;
	 private Integer refillFlag = -1;
	 private Float balanceAfter;
	 private Integer serviceClassNew;
	 private Integer serviceClassOld;
	 private Float balanceBefore;
	 private Date supervisionExpiryDate;
	 private Date serviceFeeExpiryDate;
	 private Float smsBalanceBefore;
	 private Float smsBalanceAfter;
	 private Float mmsBalanceBefore;
	 private Float mmsBalanceAfter;
	 
	 private Float[] dedicatedAccountValueChanges = new Float[10]; 
	    
	 public TUMNetmovelSupportAdjustmentCommand(String originTransactionId, Date originTimestamp, String subscriberNumber,
        Float transactionAmount, String externalData1, String externalData2, float decimalDenominator, String subscriberNoPrefix, Integer subscriberNumberNAI) {
        super(subscriberNoPrefix, subscriberNumberNAI);
        this.originTransactionId = originTransactionId;
        this.originTimestamp = originTimestamp;
        this.subscriberNumber = subscriberNumber;
        this.transactionAmount = transactionAmount;
        this.externalData1 = externalData1;
        this.externalData2 = externalData2;
        this.decimalDenominator = decimalDenominator;
     }
	 
	 public MethodResponse execute() throws XmlRpcConnectionException, BadXmlFormatException {
		this.refillPortion = 0f;
		this.adjustmentPortion = getTransactionAmount();
		GetBalanceAndDateCommand cmd = new GetBalanceAndDateCommand(getSubscriberNumber(), getOriginTimestamp(), getOriginTransactionId(), getDecimalDenominator(), getSubscriberNoPrefix(), getSubscriberNumberNAI());
		addCommand(cmd);
        cmd.setXMLRPCClient(getXMLRPCClient());
        cmd.execute();
        if(cmd.isErrorOrFaultResponse()) return cmd.getResponse();
        else {
        	serviceClassOld = cmd.getServiceClassCurrent();
            serviceClassNew = cmd.getServiceClassCurrent();
            if(serviceClassOld == AbstractCSCommand.NETMOVEL_BUNDLE_SERVICE_CLASS) {
            	balanceBefore = cmd.getNetmovelBundleAccount().getDedicatedAccountValue();
            } else {
            	balanceBefore = cmd.getSubscriberBalance(decimalDenominator);
            }
            balanceAfter = balanceBefore;
            languageIDCurrent = cmd.getCurrentLanguageID();
            supervisionExpiryDate = cmd.getAccount().getSupervisionExpiryDate();
            serviceFeeExpiryDate = cmd.getAccount().getServiceFeeExpiryDate();
            smsBalanceBefore = cmd.getSMSAccount().getDedicatedAccountValue();
            smsBalanceAfter = smsBalanceBefore;
            mmsBalanceBefore = cmd.getMMSAccount().getDedicatedAccountValue();
            mmsBalanceAfter = mmsBalanceBefore;
        }
        NetmovelSupportUpdateBalanceAndDateCommand cmd2 = new NetmovelSupportUpdateBalanceAndDateCommand(getOriginTransactionId(), getOriginTimestamp(), getSubscriberNumber(), adjustmentPortion, getExternalData1(), getExternalData2(), 0, 0, 0, 0, serviceClassNew, getDecimalDenominator(), getSubscriberNoPrefix(), getSubscriberNumberNAI());
        addCommand(cmd2);
        cmd2.setXMLRPCClient(getXMLRPCClient());
        cmd2.execute();
        if(cmd2.isErrorOrFaultResponse()) adjustmentFlag = cmd.getFullResultCode();
        if(cmd2.isErrorOrFaultResponse()) return cmd2.getResponse();
        balanceAfter = balanceAfter + adjustmentPortion;
        for(int i = 0; i < 10; i++) {
        	dedicatedAccountValueChanges[i] = new Float(0);
        	if(i == AbstractCSCommand.SMS_DEDICATED_ACCOUNT_ID - 1)	dedicatedAccountValueChanges[i] = smsBalanceAfter - smsBalanceBefore;
        	else if(i == AbstractCSCommand.MMS_DEDICATED_ACCOUNT_ID - 1) dedicatedAccountValueChanges[i] = mmsBalanceAfter - mmsBalanceBefore;
        }
        //return the result of last command normally
        return getLastCommand().getResponse();
	 }
	 
	 @Override
	 public boolean isErrorOrFaultResponse() {
		 if(adjustmentFlag == -1) return getChain().get(0).isErrorOrFaultResponse();
	     return super.isErrorOrFaultResponse();
	 }

	 @Override
	 public boolean isFault(){
		 if(adjustmentFlag == -1) return getChain().get(0).isFault();
	     return super.isFault();
	 }

	 @Override
	 public Integer getResponseCode() {
		 if(adjustmentFlag == -1) return getChain().get(0).getResponseCode();
	     return super.getResponseCode();
	 }
	 
	 @Override
	 public boolean supportsGetSubscriberInfo() {
		 return false;
	 }

	 @Override
	 public SubscriberInfo getSubscriberInfo() {
		 throw new UnsupportedOperationException("Not supported yet.");
	 }

	 public Float getTransactionAmount() {
		 return transactionAmount;
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

	 public Float getBalanceAfter() {
		 return balanceAfter;
	 }

	 public Integer getRefillFlag() {
		 return refillFlag;
	 }

	 public Integer getAdjustmentFlag() {
		 return adjustmentFlag;
	 }

	 public Integer getLanguageIDCurrent() {
		 return languageIDCurrent;
	 }

	 public Float getRefillPortion() {
		 return refillPortion;
	 }

	 public Float getAdjustmentPortion() {
		 return adjustmentPortion;
	 }

	 public Date getSupervisionExpiryDate() {
		 return supervisionExpiryDate;
	 }

	 public Date getServiceFeeExpiryDate() {
		 return serviceFeeExpiryDate;
	 }

	 public Float getSmsBalanceBefore() {
		 return smsBalanceBefore;
	 }

	 public Float getSmsBalanceAfter() {
		 return smsBalanceAfter;
	 }

	 public Float getMmsBalanceBefore() {
		 return mmsBalanceBefore;
	 }

	 public Float getMmsBalanceAfter() {
		 return mmsBalanceAfter;
	 }
	 
	 public Float[] getDedicatedAccountValueChanges() {
	 	return dedicatedAccountValueChanges;
	 }
}
