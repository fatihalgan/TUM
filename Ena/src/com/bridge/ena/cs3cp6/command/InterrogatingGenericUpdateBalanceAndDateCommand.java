package com.bridge.ena.cs3cp6.command;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bridge.ena.cs.command.CompositeCSCommand;
import com.bridge.ena.cs.value.SubscriberInfo;
import com.bridge.ena.cs3cp6.value.DedicatedAccountInformation;
import com.bridge.ena.xml.BadXmlFormatException;
import com.bridge.ena.xmlrpc.exception.XmlRpcConnectionException;
import com.bridge.ena.xmlrpc.serializer.MethodResponse;

public class InterrogatingGenericUpdateBalanceAndDateCommand extends CompositeCSCommand<AbstractCS3CP6Command> {

	private static Log logger = LogFactory.getLog(InterrogatingGenericUpdateBalanceAndDateCommand.class);
	
	private String originTransactionId;
	private Date originTimestamp;
	private String subscriberNumber;
	private Float accountBalanceBefore;
	private Float adjustmentAmount;
	private String externalData1;
	private String externalData2;
	private int supervisionExpiryDateRelative;
	private int serviceFeeExpiryDateRelative;
	private Float decimalDenominator;
	private Integer serviceClassCurrent;
	private Date serviceFeeExpiryDateBefore;
	private Date supervisionExpiryDateBefore;
	private Integer languageIDCurrent;
	private List<DedicatedAccountInformation> dedicatedAccountAdjustments;
	private List<DedicatedAccountInformation> dedicatedAccountValuesBefore;
		
	public InterrogatingGenericUpdateBalanceAndDateCommand(String originTransactionId, Date originTimestamp, String subscriberNumber,
		Float adjustmentAmount, String externalData1, String externalData2, int supervisionExpiryDateRelative, 
		int serviceFeeExpiryDateRelative, float decimalDenominator, List<DedicatedAccountInformation> dedicatedAccountAdjustments,
		String subscriberNoPrefix, Integer subscriberNumberNAI) {
		super(subscriberNoPrefix, subscriberNumberNAI);
		this.originTransactionId = originTransactionId;
		this.originTimestamp = originTimestamp;
		this.subscriberNumber = subscriberNumber;
		this.adjustmentAmount = adjustmentAmount;
		this.externalData1 = externalData1;
		this.externalData2 = externalData2;
		this.supervisionExpiryDateRelative = supervisionExpiryDateRelative;
		this.serviceFeeExpiryDateRelative = serviceFeeExpiryDateRelative;
		this.decimalDenominator = decimalDenominator;
		this.dedicatedAccountAdjustments = dedicatedAccountAdjustments;
	}
	
	public MethodResponse execute() throws XmlRpcConnectionException, BadXmlFormatException {
		GetBalanceAndDateCommand cmd = new GetBalanceAndDateCommand(subscriberNumber, originTimestamp, originTransactionId, decimalDenominator, getSubscriberNoPrefix(), getSubscriberNumberNAI());
		addCommand(cmd);
		cmd.setXMLRPCClient(getXMLRPCClient());
		cmd.execute();
		if(cmd.isErrorOrFaultResponse()) return cmd.getResponse();
		else {
			accountBalanceBefore = cmd.getAccount().getAccountValue();
			serviceClassCurrent = cmd.getAccount().getServiceClassCurrent();
			serviceFeeExpiryDateBefore = cmd.getAccount().getServiceFeeExpiryDate();
			supervisionExpiryDateBefore = cmd.getAccount().getSupervisionExpiryDate();
			languageIDCurrent = cmd.getCurrentLanguageID();
			dedicatedAccountValuesBefore = cmd.getAccount().getDedicatedAccounts();
		}
		
		GenericUpdateBalanceAndDateCommand update = new GenericUpdateBalanceAndDateCommand(originTransactionId, originTimestamp, subscriberNumber, adjustmentAmount, externalData1, externalData2, supervisionExpiryDateRelative, serviceFeeExpiryDateRelative, decimalDenominator, dedicatedAccountAdjustments, getSubscriberNoPrefix(), getSubscriberNumberNAI());
		addCommand(update);
		update.setXMLRPCClient(getXMLRPCClient());
		update.execute();
		return update.getResponse();
	}

	public Float getAccountBalanceBefore() {
		return accountBalanceBefore;
	}

	public Float getAdjustmentAmount() {
		return adjustmentAmount;
	}

	public Integer getSupervisionExpiryDateRelative() {
		return supervisionExpiryDateRelative;
	}

	public Integer getServiceFeeExpiryDateRelative() {
		return serviceFeeExpiryDateRelative;
	}

	public Integer getServiceClassCurrent() {
		return serviceClassCurrent;
	}

	public Date getServiceFeeExpiryDateBefore() {
		return serviceFeeExpiryDateBefore;
	}

	public Date getSupervisionExpiryDateBefore() {
		return supervisionExpiryDateBefore;
	}

	public Integer getLanguageIDCurrent() {
		return languageIDCurrent;
	}

	public List<DedicatedAccountInformation> getDedicatedAccountAdjustments() {
		return dedicatedAccountAdjustments;
	}

	public List<DedicatedAccountInformation> getDedicatedAccountValuesBefore() {
		return dedicatedAccountValuesBefore;
	}
	
	public Float getAccountBalanceAfter() {
		if(getLastCommand().isErrorOrFaultResponse()) return getAccountBalanceBefore();
		else return(getAccountBalanceBefore().floatValue() + getAdjustmentAmount().floatValue());
	}
	
	public Date getSupervisionExpiryDateAfter() {
		if(getLastCommand().isErrorOrFaultResponse()) return getSupervisionExpiryDateBefore();
		else {
			Calendar cal = Calendar.getInstance();
			cal.setTime(supervisionExpiryDateBefore);
			cal.add(Calendar.DATE, supervisionExpiryDateRelative);
			return cal.getTime();
		}
	}
	
	public Date getServiceFeeExpiryDateAfter() {
		if(getLastCommand().isErrorOrFaultResponse()) return getServiceFeeExpiryDateBefore();
		else {
			Calendar cal = Calendar.getInstance();
			cal.setTime(serviceFeeExpiryDateBefore);
			cal.add(Calendar.DATE, serviceFeeExpiryDateRelative);
			return cal.getTime();
		}
	}
	
	private DedicatedAccountInformation getDedicatedAccountAdjustment(int dedicatedAccountId) {
		for(DedicatedAccountInformation da : dedicatedAccountAdjustments) {
			if(da.getDedicatedAccountId().intValue() == dedicatedAccountId) return da;
		}
		return null;
	}
	
	public DedicatedAccountInformation getDedicatedAccountBefore(int dedicatedAccountId) {
		for(DedicatedAccountInformation da : dedicatedAccountValuesBefore) {
			if(da.getDedicatedAccountId().intValue() == dedicatedAccountId) return da;
		}
		return null;
	}
	
	public DedicatedAccountInformation getDedicatedAccountAfter(int dedicatedAccountId) {
		DedicatedAccountInformation adjustment = getDedicatedAccountAdjustment(dedicatedAccountId);
		if(adjustment == null) return getDedicatedAccountBefore(dedicatedAccountId);
		else {
			DedicatedAccountInformation before = getDedicatedAccountBefore(dedicatedAccountId).copyPrototype();
			before.addAmount(adjustment);
			return before;
		}
	}
	
	@Override
	public boolean supportsGetSubscriberInfo() {
		return false;
	}

	@Override
	public SubscriberInfo getSubscriberInfo() {
		return null;
	}
	
}
