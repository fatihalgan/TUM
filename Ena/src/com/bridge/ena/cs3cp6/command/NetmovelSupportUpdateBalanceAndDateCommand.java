package com.bridge.ena.cs3cp6.command;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bridge.ena.cs.command.CompositeCSCommand;
import com.bridge.ena.cs.value.SubscriberInfo;
import com.bridge.ena.xml.BadXmlFormatException;
import com.bridge.ena.xmlrpc.exception.XmlRpcConnectionException;
import com.bridge.ena.xmlrpc.serializer.MethodResponse;

public class NetmovelSupportUpdateBalanceAndDateCommand extends AbstractCS3CP6Command {

	private static Log logger = LogFactory.getLog(NetmovelSupportUpdateBalanceAndDateCommand.class);
		
	private String originTransactionId;
    private Date originTimestamp;
    private String subscriberNumber;
    private Float adjustmentAmount;
    private String externalData1;
    private String externalData2;
    private Integer supervisionExpiryDateRelative;
    private Integer serviceFeeExpiryDateRelative;
    private Integer freeSMS;
    private Integer freeMMS;
    private Float decimalDenominator;
    private Integer serviceClassCurrent;
	
    NetmovelSupportUpdateBalanceAndDateCommand(String originTransactionId, Date originTimestamp, String subscriberNumber,
    	Float adjustmentAmount, String externalData1, String externalData2, int supervisionExpiryDateRelative,
        int serviceFeeExpiryDateRelative,  int freeSMS, int freeMMS, int serviceClassCurrent, float decimalDenominator,
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
        this.freeSMS = freeSMS;
        this.freeMMS = freeMMS;
        this.decimalDenominator = decimalDenominator;
        this.serviceClassCurrent = serviceClassCurrent;
    }
    
   	
	@Override
	public MethodResponse execute() throws XmlRpcConnectionException {
		AbstractCS3CP6Command cmd = null;
		if(serviceClassCurrent.equals(NETMOVEL_BUNDLE_SERVICE_CLASS)) {
			cmd = new UpdateDedicatedAccountBalanceAndDateCommand(originTransactionId, originTimestamp, subscriberNumber, adjustmentAmount, externalData1, externalData2, supervisionExpiryDateRelative, serviceFeeExpiryDateRelative, NETMOVEL_BUNDLE_DEDICATED_ACCOUNT_ID, null, decimalDenominator, getSubscriberNoPrefix(), getSubscriberNumberNAI());
		} else {
			cmd = new UpdateBalanceAndDateCommand(originTransactionId, originTimestamp, subscriberNumber, adjustmentAmount, externalData1, externalData2, supervisionExpiryDateRelative, serviceFeeExpiryDateRelative, freeSMS, freeMMS, decimalDenominator, getSubscriberNoPrefix(), getSubscriberNumberNAI());
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

    public Integer getFreeSMS() {
        return freeSMS;
    }

    public Integer getFreeMMS() {
        return freeMMS;
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


	@Override
	public void prepareRequest() {
		return;		
	}
}
