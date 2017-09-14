/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.cs3cp6.command;

import com.bridge.ena.cs.command.CompositeCSCommand;
import com.bridge.ena.cs.value.SubscriberInfo;
import com.bridge.ena.cs3cp6.value.DedicatedAccountInformation;
import com.bridge.ena.vs.profiles.VoucherProfile;
import com.bridge.ena.vs.profiles.VoucherProfiles;
import com.bridge.ena.vs.profiles.VoucherProfilesLoader;
import com.bridge.ena.xml.BadXmlFormatException;
import com.bridge.ena.xmlrpc.exception.XmlRpcConnectionException;
import com.bridge.ena.xmlrpc.serializer.MethodResponse;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpStatus;

/**
 *
 * @author db2admin
 */
public class FreeAmountRefillCommand extends CompositeCSCommand<AbstractCS3CP6Command> {

    private static Log logger = LogFactory.getLog(FreeAmountRefillCommand.class);
    private String originTransactionId;
    private Date originTimestamp;
    private String subscriberNumber;
    private Float transactionAmount;
    private String refillProfileID;
    private String voucherProfileName;
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

    public FreeAmountRefillCommand(String originTransactionId, Date originTimestamp, String subscriberNumber,
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
        VoucherProfile profile = matchPaymentProfile(transactionAmount);
        if(profile != null) {
            this.refillProfileID = profile.getProfileID();
            this.voucherProfileName = profile.getProfileDescription();
        }

        if(getTransactionAmount() >= profile.getMainAccountValue()) {
            refillPortion = profile.getMainAccountValue();
            adjustmentPortion  = getTransactionAmount() - refillPortion;
        } else {
            refillPortion = 0f;
            adjustmentPortion = getTransactionAmount();
        }

        if(refillPortion > 0) {
            RefillCommand cmd = new RefillCommand(getOriginTransactionId(), getOriginTimestamp(), getSubscriberNumber(), refillPortion, getExternalData1(), getExternalData2(), getDecimalDenominator(), getSubscriberNoPrefix(), getSubscriberNumberNAI());
            addCommand(cmd);
            cmd.setXMLRPCClient(getXMLRPCClient());
            try {
                cmd.execute();
            } finally {
                if((cmd.getHttpStatusCode() != HttpStatus.SC_OK) || cmd.isErrorOrFaultResponse()) refillFlag = cmd.getFullResultCode();
                else refillFlag = 0;
            }
            if(cmd.isErrorOrFaultResponse()) return cmd.getResponse();
            else {
                balanceBefore = cmd.getAccountBeforeRefill().getAccountValue();
                balanceAfter = cmd.getAccountAfterRefill().getAccountValue();
                serviceClassOld = cmd.getAccountBeforeRefill().getServiceClassCurrent();
                serviceClassNew = cmd.getAccountAfterRefill().getServiceClassCurrent();
                languageIDCurrent = cmd.getCurrentLanguageID();
                //if supervision date does not change, CS does not put the info in AccountAfterRefill, instead get from AccountBeforeRefill
                if(cmd.getAccountAfterRefill().getSupervisionExpiryDate() != null)
                    supervisionExpiryDate = cmd.getAccountAfterRefill().getSupervisionExpiryDate();
                else supervisionExpiryDate = cmd.getAccountBeforeRefill().getSupervisionExpiryDate();
                //if service fee date does not change, CS does not put the info in AccountAfterRefill, instead get from AccountBeforeRefill
                if(cmd.getAccountAfterRefill().getServiceFeeExpiryDate() != null)
                    serviceFeeExpiryDate = cmd.getAccountAfterRefill().getServiceFeeExpiryDate();
                else serviceFeeExpiryDate = cmd.getAccountBeforeRefill().getServiceFeeExpiryDate();
                
                if(cmd.getSMSAccountBeforeRefill() == null) smsBalanceBefore = 0f;
                else smsBalanceBefore = cmd.getSMSAccountBeforeRefill().getDedicatedAccountValue();
                if(cmd.getSMSAccountAfterRefill() == null) smsBalanceAfter = 0f;
                else smsBalanceAfter = cmd.getSMSAccountAfterRefill().getDedicatedAccountValue();
                if(cmd.getMMSAccountBeforeRefill() == null) mmsBalanceBefore = 0f;
                else mmsBalanceBefore = cmd.getMMSAccountBeforeRefill().getDedicatedAccountValue();
                if(cmd.getMMSAccountAfterRefill() == null) mmsBalanceAfter = 0f;
                else mmsBalanceAfter = cmd.getMMSAccountAfterRefill().getDedicatedAccountValue();
                //get the balances in dedicated accounts
                for(int i = 0; i < 10; i++) {
                	DedicatedAccountInformation dedicatedAccountAfterRefill = cmd.getDedicatedAccountAfterRefill(i+1);
                	DedicatedAccountInformation dedicatedAccountBeforeRefill = cmd.getDedicatedAccountBeforeRefill(i+1);
                	if(dedicatedAccountAfterRefill == null) dedicatedAccountValueChanges[i] = new Float(0);
                	else dedicatedAccountValueChanges[i] = dedicatedAccountAfterRefill.getDedicatedAccountValue() - dedicatedAccountBeforeRefill.getDedicatedAccountValue();
                }
            }
        } else if(refillPortion == 0) {
            GetBalanceAndDateCommand cmd = new GetBalanceAndDateCommand(getSubscriberNumber(), getOriginTimestamp(), getOriginTransactionId(), getDecimalDenominator(), getSubscriberNoPrefix(), getSubscriberNumberNAI());
            addCommand(cmd);
            cmd.setXMLRPCClient(getXMLRPCClient());
            cmd.execute();
            if(cmd.isErrorOrFaultResponse()) return cmd.getResponse();
            else {
                balanceBefore = cmd.getSubscriberBalance(decimalDenominator);
                serviceClassOld = cmd.getServiceClassCurrent();
                serviceClassNew = cmd.getServiceClassCurrent();
                languageIDCurrent = cmd.getCurrentLanguageID();
                balanceAfter = balanceBefore;
                supervisionExpiryDate = cmd.getAccount().getSupervisionExpiryDate();
                serviceFeeExpiryDate = cmd.getAccount().getServiceFeeExpiryDate();
                smsBalanceBefore = cmd.getSMSAccount().getDedicatedAccountValue();
                smsBalanceAfter = smsBalanceBefore;
                mmsBalanceBefore = cmd.getMMSAccount().getDedicatedAccountValue();
                mmsBalanceAfter = mmsBalanceBefore;
            }
        }

        if(adjustmentPortion > 0) {
            UpdateBalanceAndDateCommand cmd = new UpdateBalanceAndDateCommand(getOriginTransactionId(), getOriginTimestamp(), getSubscriberNumber(), adjustmentPortion, getExternalData1(), getExternalData2(), 0, 0, 0, 0, getDecimalDenominator(), getSubscriberNoPrefix(), getSubscriberNumberNAI());
            addCommand(cmd);
            cmd.setXMLRPCClient(getXMLRPCClient());
            try {
                cmd.execute();
            } catch(XmlRpcConnectionException e) {
                //If adjustment could not be done, return a success response after setting adjustment flag to error code
                adjustmentFlag = e.getStatusCode();
                if(refillPortion > 0 && refillFlag == 0) return getChain().get(0).getResponse();
            }
            //Set the adjustment flag with the response code from adjustment
            if(cmd.isErrorOrFaultResponse()) adjustmentFlag = cmd.getFullResultCode();
            else adjustmentFlag = 0;
            if(cmd.isErrorOrFaultResponse()) {
                //if adjustment is not successful, return success response after setting adjustment flag to error code
                if(refillPortion > 0 && refillFlag == 0) return getChain().get(0).getResponse();
                //if no refill was done, no special action necessary, just return the result of adjustment request
                return cmd.getResponse();
            }
            else {
                //if adjustment is successful as well, set the final balance..
                balanceAfter = balanceAfter + adjustmentPortion;
            }
        }
        //return the result of last command normally
        return getLastCommand().getResponse();
    }

    @Override
    public boolean isErrorOrFaultResponse() {
       if(refillPortion > 0 && refillFlag == 0 && adjustmentFlag > 0) return getChain().get(0).isErrorOrFaultResponse();
       return super.isErrorOrFaultResponse();
    }

    @Override
    public boolean isFault(){
	   if(refillPortion > 0 && refillFlag == 0 && adjustmentFlag > 0) return getChain().get(0).isFault();
       return super.isFault();
    }

    @Override
    public Integer getResponseCode() {
       if(refillPortion > 0 && refillFlag == 0 && adjustmentFlag > 0) return getChain().get(0).getResponseCode();
       return super.getResponseCode();
   }

    private VoucherProfile matchPaymentProfile(Float transactionAmountRefill) {
        VoucherProfile profile = getVoucherProfiles().getByClosestNominalValue(transactionAmountRefill);
        return profile;
    }

    private VoucherProfiles getVoucherProfiles() {
        return VoucherProfilesLoader.instance().getVoucherProfiles();
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

    public String getRefillProfileId() {
        return refillProfileID;
    }

    public String getVoucherProfileName() {
        return voucherProfileName;
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
