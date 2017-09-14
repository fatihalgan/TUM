package com.bridge.ena.cs3cp6.command;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpStatus;

import com.bridge.ena.cs.command.CompositeCSCommand;
import com.bridge.ena.cs.value.SubscriberInfo;
import com.bridge.ena.xml.BadXmlFormatException;
import com.bridge.ena.xmlrpc.exception.XmlRpcConnectionException;
import com.bridge.ena.xmlrpc.serializer.MethodResponse;

public class NetmovelBundleCommand extends CompositeCSCommand<AbstractCS3CP6Command> {

	private static Log logger = LogFactory.getLog(NetmovelBundleCommand.class);
	private String originTransacitonId;
	private Date originTimestamp;
	private String subscriberNumber;
    private Float mainAccountAdjustmentAmount;
    private Float dedicatedAccountAdjustmentAmount;
    private Integer dedicatedAccountID;
    private String externalData1;
    private String externalData2;
    private Integer supervisionExpiryDateRelative;
    private Integer serviceFeeExpiryDateRelative;
    private float decimalDenominator;
    private Integer serviceClassOld;
    private Integer serviceClassNew;
    private Integer adjustmentFlag;
    private Integer updateServiceClassFlag;
    
    public NetmovelBundleCommand(String originTransactionId, Date originTimestamp, String subscriberNumber,
    	Float mainAccountAdjustmentAmount, Float dedicatedAccountAdjustmentAmount, Integer dedicatedAccountId,
    	String externalData1, String externalData2, Integer supervisionExpiryDateRelative, Integer serviceFeeExpiryDateRelative,
    	float decimalDenominator, Integer serviceClassNew, Integer serviceClassOld, String subscriberNoPrefix, Integer subscriberNumberNAI) {
			super(subscriberNoPrefix, subscriberNumberNAI); 
			this.originTransacitonId = originTransactionId;
	    	this.originTimestamp = originTimestamp;
	    	this.subscriberNumber = subscriberNumber;
	    	this.mainAccountAdjustmentAmount = mainAccountAdjustmentAmount;
	    	this.dedicatedAccountAdjustmentAmount = dedicatedAccountAdjustmentAmount;
	    	this.dedicatedAccountID = dedicatedAccountId;
	    	this.externalData1 = externalData1;
	    	this.externalData2 = externalData2;
	    	this.supervisionExpiryDateRelative = supervisionExpiryDateRelative;
	    	this.serviceFeeExpiryDateRelative = serviceFeeExpiryDateRelative;
	    	this.decimalDenominator = decimalDenominator;
	    	this.serviceClassNew = serviceClassNew;
	    	this.serviceClassOld = serviceClassOld;
	    	this.adjustmentFlag = -1;
	    	this.updateServiceClassFlag = -1;
	}

	public String getOriginTransacitonId() {
		return originTransacitonId;
	}

	public Date getOriginTimestamp() {
		return originTimestamp;
	}

	public String getSubscriberNumber() {
		return subscriberNumber;
	}

	public Float getMainAccountAdjustmentAmount() {
		return mainAccountAdjustmentAmount;
	}

	public Float getDedicatedAccountAdjustmentAmount() {
		return dedicatedAccountAdjustmentAmount;
	}

	public Integer getDedicatedAccountID() {
		return dedicatedAccountID;
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

	public float getDecimalDenominator() {
		return decimalDenominator;
	}

	public Integer getServiceClassOld() {
		return serviceClassOld;
	}

	public Integer getServiceClassNew() {
		return serviceClassNew;
	}

	public Integer getAdjustmentFlag() {
		return adjustmentFlag;
	}

	public Integer getUpdateServiceClassFlag() {
		return updateServiceClassFlag;
	}

	@Override
	public SubscriberInfo getSubscriberInfo() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean supportsGetSubscriberInfo() {
		return false;
	}

	@Override
	public MethodResponse execute() throws XmlRpcConnectionException, BadXmlFormatException {
		adjustmentFlag = -1;
		updateServiceClassFlag = -1;
		AbstractCS3CP6Command cmd =  null;
		//First perform the service class update
		if(serviceClassOld != serviceClassNew) {
			cmd = new UpdateServiceClassCommand(getOriginTransacitonId(), getOriginTimestamp(), getSubscriberNumber(), getServiceClassNew(), getSubscriberNoPrefix(), getSubscriberNumberNAI());
			addCommand(cmd);
			cmd.setXMLRPCClient(getXMLRPCClient());
			try {
				cmd.execute();
			} finally {
				if((cmd.getHttpStatusCode() != HttpStatus.SC_OK) || cmd.isErrorOrFaultResponse()) updateServiceClassFlag = cmd.getFullResultCode();
	            else updateServiceClassFlag = 0;
			}
		} else {
			updateServiceClassFlag = 0;
		}
		if(updateServiceClassFlag != 0) return cmd.getResponse();
		if(getMainAccountAdjustmentAmount() != 0f || getDedicatedAccountAdjustmentAmount() != 0f) {
			cmd = new UpdateMainAndDedicatedAccountBalanceCommand(getOriginTransacitonId(), getOriginTimestamp(), getSubscriberNumber(),
					getMainAccountAdjustmentAmount(), getDedicatedAccountAdjustmentAmount(), getDedicatedAccountID(),
					getExternalData1(), getExternalData2(), getSupervisionExpiryDateRelative(), getServiceFeeExpiryDateRelative(),
					getDecimalDenominator(), getSubscriberNoPrefix(), getSubscriberNumberNAI());
			addCommand(cmd);
			cmd.setXMLRPCClient(getXMLRPCClient());
			try {
				cmd.execute();
			} finally {
				if((cmd.getHttpStatusCode() != HttpStatus.SC_OK) || cmd.isErrorOrFaultResponse()) adjustmentFlag = cmd.getFullResultCode();
				else adjustmentFlag = 0;
			}
		}
		return getLastCommand().getResponse();
	}
	
	@Override
    public boolean isErrorOrFaultResponse() {
       return getLastCommand().isErrorOrFaultResponse();
    }

    @Override
    public boolean isFault(){
	   return getLastCommand().isFault();
    }

    @Override
    public Integer getResponseCode() {
       return getLastCommand().getResponseCode();
   }
    
    
}
