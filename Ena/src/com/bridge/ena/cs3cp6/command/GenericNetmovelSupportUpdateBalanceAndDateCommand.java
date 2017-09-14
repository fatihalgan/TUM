package com.bridge.ena.cs3cp6.command;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bridge.ena.cs.value.SubscriberInfo;
import com.bridge.ena.cs3cp6.value.DedicatedAccountInformation;
import com.bridge.ena.xmlrpc.exception.XmlRpcConnectionException;
import com.bridge.ena.xmlrpc.serializer.MethodResponse;

public class GenericNetmovelSupportUpdateBalanceAndDateCommand extends AbstractCS3CP6Command {

	private static Log logger = LogFactory.getLog(GenericNetmovelSupportUpdateBalanceAndDateCommand.class);
	private static final Integer NETMOVEL_BUNDLE_DEDICATED_ACCOUNT_ID = 3;
	private static final Integer NETMOVEL_BUNDLE_SERVICE_CLASS = 34;
	
	private String originTransactionId;
    private Date originTimestamp;
    private String subscriberNumber;
    private Float adjustmentAmount;
    private String externalData1;
    private String externalData2;
    private Integer supervisionExpiryDateRelative;
    private Integer serviceFeeExpiryDateRelative;
    private Float decimalDenominator;
    private List<DedicatedAccountInformation> dedicatedAccountAdjustments;
    private Integer serviceClassCurrent;
    
    GenericNetmovelSupportUpdateBalanceAndDateCommand(String originTransactionId, 
    	Date originTimestamp, String subscriberNumber, Float adjustmentAmount, 
    	String externalData1, String externalData2, int supervisionExpiryDateRelative,
        int serviceFeeExpiryDateRelative, int serviceClassCurrent, float decimalDenominator,
        List<DedicatedAccountInformation> dedicatedAccountAdjustments,
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
        this.serviceClassCurrent = serviceClassCurrent;
        this.dedicatedAccountAdjustments = dedicatedAccountAdjustments;
    }
    
    @Override
	public MethodResponse execute() throws XmlRpcConnectionException {
		AbstractCS3CP6Command cmd = null;
		if(serviceClassCurrent.equals(NETMOVEL_BUNDLE_SERVICE_CLASS)) {
			DedicatedAccountInformation dai = new DedicatedAccountInformation();
			dai.setDedicatedAccountId(NETMOVEL_BUNDLE_DEDICATED_ACCOUNT_ID);
			dai.setDedicatedAccountValue(adjustmentAmount);
			dedicatedAccountAdjustments.add(dai);
			cmd = new GenericUpdateBalanceAndDateCommand(originTransactionId, originTimestamp, subscriberNumber, 0f, externalData1, externalData2, supervisionExpiryDateRelative, serviceFeeExpiryDateRelative, decimalDenominator, dedicatedAccountAdjustments, getSubscriberNoPrefix(), getSubscriberNumberNAI());
		} else {
			cmd = new GenericUpdateBalanceAndDateCommand(originTransactionId, originTimestamp, subscriberNumber, adjustmentAmount, externalData1, externalData2, supervisionExpiryDateRelative, serviceFeeExpiryDateRelative, decimalDenominator, dedicatedAccountAdjustments, getSubscriberNoPrefix(), getSubscriberNumberNAI());
		}
		cmd.setXMLRPCClient(getXMLRPCClient());
		cmd.setOriginHostName(getOriginHostName());
		this.response = cmd.execute();
		return response;
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

    public Integer getSupervisionExpiryDateRelative() {
        return supervisionExpiryDateRelative;
    }

    public Integer getServiceFeeExpiryDateRelative() {
        return serviceFeeExpiryDateRelative;
    }

    public Float getDecimalDenominator() {
        return decimalDenominator;
    }

    @Override
    public boolean supportsGetSubscriberInfo() {
        return false;
    }
    
    public Integer getServiceClassCurrent() {
		return serviceClassCurrent;
	}


	@Override
	public SubscriberInfo getSubscriberInfo() {
		throw new UnsupportedOperationException("Unsupported Operation..");
	}
	
	public List<DedicatedAccountInformation> getDedicatedAccountAdjustments() {
		return dedicatedAccountAdjustments;
	}

	@Override
	public void prepareRequest() {
		return;		
	}
}
