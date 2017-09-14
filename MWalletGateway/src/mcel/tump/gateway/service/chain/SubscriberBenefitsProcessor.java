/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.gateway.service.chain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.bridge.ena.cs3cp6.command.CommandFactory;
import com.bridge.ena.cs3cp6.command.GenericUpdateBalanceAndDateCommand;
import com.bridge.ena.cs3cp6.value.DedicatedAccountInformation;
import com.bridge.ena.xmlrpc.exception.XmlRpcConnectionException;

import mcel.tump.gateway.util.TUMGWTokens;
import mcel.tump.gateway.util.TUMRechargeRequest;
import mcel.tump.gateway.util.TUMRechargeResponse;
import mcel.tump.reference.service.IParametersService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author db2admin
 */
public class SubscriberBenefitsProcessor implements RechargeRequestProcessor {

    private RechargeRequestProcessor nextHandler;
    private RechargeRequestProcessor onFaultHandler;
    private IParametersService parametersService;
    private CommandFactory commandFactory;
    private static final Log logger = LogFactory.getLog(SubscriberBenefitsProcessor.class);

    public RechargeRequestProcessor getNextHandler() {
        return nextHandler;
    }

    public void setNextHandler(RechargeRequestProcessor nextHandler) {
        this.nextHandler = nextHandler;
    }

    public RechargeRequestProcessor getOnFaultHandler() {
        return onFaultHandler;
    }

    public void setOnFaultHandler(RechargeRequestProcessor onFaultHandler) {
        this.onFaultHandler = onFaultHandler;
    }

    public IParametersService getParametersService() {
        return parametersService;
    }

    public void setParametersService(IParametersService parametersService) {
        this.parametersService = parametersService;
    }

    public CommandFactory getCommandFactory() {
        return commandFactory;
    }

    public void setCommandFactory(CommandFactory commandFactory) {
        this.commandFactory = commandFactory;
    }
    
    public void process(TUMRechargeRequest request, TUMRechargeResponse response) {
    	logger.debug("SubscriberBenefits processor for MWalletGateway started...");
    	if(!response.isFault() && !request.getMethodName().equals(TUMGWTokens.RechargeSubscriberPinRequest.toString())
    		&& !request.getMethodName().equals(TUMGWTokens.AdjustSubscriberAccountRequest.toString()) && !request.getMethodName().equals(TUMGWTokens.RechargeSubscriberSMSRequest.toString())) {
        	GenericUpdateBalanceAndDateCommand cmd = null;
      	
        	//Calculate SMS bonuses
        	int bonusSMS = 0;
        	int bonusMB = 0;
        	//Calculate the vouhcer denomination bonus to DA        	
        	float bonusAmount = 0f;
        	float mainAccountAdjustmentAmount = 0f;
        	int bonusValidityDates = 0;
        	        	
        	int transferAmount = request.getTransferAmount().intValue();
        	//Give bonus to all mKesh transactions, not only to self top ups for now.
        	if(transferAmount >= 50 && transferAmount < 100) {
        		bonusAmount = 12.5f;
        		bonusMB = 5;
        		bonusSMS = 25;
        	} else if(transferAmount >=100 && transferAmount < 150) {
        		bonusAmount = 25f;
        		bonusMB = 10;
        		bonusSMS = 50;
        	} else if(transferAmount >= 150) {
        		bonusAmount = 37.5f;
        		bonusMB = 15;
        		bonusSMS = 75;		
        	} 
        	List<DedicatedAccountInformation> dedicatedAccountAdjustments = new ArrayList<DedicatedAccountInformation>();
        	
        	if(bonusSMS > 0) {
        		DedicatedAccountInformation smsDa = new DedicatedAccountInformation();
        		smsDa.setDedicatedAccountId(1);
        		smsDa.setDedicatedAccountValue(new Float(bonusSMS));
        		dedicatedAccountAdjustments.add(smsDa);
        	}
        	if(bonusMB > 0) {
        		DedicatedAccountInformation mbDa = new DedicatedAccountInformation();
        		mbDa.setDedicatedAccountId(9);
        		mbDa.setDedicatedAccountValue(new Float(bonusMB));
        		dedicatedAccountAdjustments.add(mbDa);
        	}
        	if(bonusAmount > 0) {
        		DedicatedAccountInformation bonusDA = new DedicatedAccountInformation();
        		bonusDA.setDedicatedAccountId(7);
        		bonusDA.setDedicatedAccountValue(new Float(bonusAmount));
        		//Comment out bonusDA.setExpiryDate() to not set any expiry date
        		Calendar cal = Calendar.getInstance();
        		cal.add(Calendar.DATE, bonusValidityDates);
        		Date expiryDate = cal.getTime();
        		//bonusDA.setExpiryDate(expiryDate);
        		dedicatedAccountAdjustments.add(bonusDA);
        	}
        	
        	
        	logger.info("Transfer Amount is: " + request.getTransferAmount().toString());
        	logger.info("Main Account Adjustment Amount is: " + mainAccountAdjustmentAmount);
        	logger.info("Free Call Account Adjustment Amount is:" + bonusAmount);
        	logger.info("Bonus SMS Amount is: " + bonusSMS);
        	logger.info("Bonus MB Amount is: " + bonusMB);
        	
        	cmd = commandFactory.getGenericUpdateBalanceAndDateCommand(request.getRequestTransactionID(), request.getRequestTimeStamp(), request.getSubscriberMSISDN(), 
        			mainAccountAdjustmentAmount, String.valueOf(mainAccountAdjustmentAmount), response.getTUMTransactionID(), 0, 0, 100f, dedicatedAccountAdjustments);
        			
            try {
                logger.debug("Sending Subscriber Benefit Request to Charging System...");
                cmd.execute();
                if(cmd.isFault()) {
                    logger.warn("Received Fault response from Charging System while processing Subscriber Benefit: " + cmd.getFaultCode());
                    //Do nothing..
                } else if(cmd.isErrorOrFaultResponse()) {
                    logger.warn("Received Error response from Charging System while processing Subscriber Benefit: " + cmd.getResponseCode());
                } else {
                    logger.debug("Received Success response from Charging System for Subscriber Benefits: " + cmd.getResponseCode());
                    Float subscriberBalanceAfter = Float.parseFloat(response.getSubscriberBalanceAfter());
                    //subscriberBalanceAfter = subscriberBalanceAfter - transactionAmountComission;
                    response.setSubscriberBalanceAfter(String.valueOf(subscriberBalanceAfter));
                    response.setFreeSMS(response.getFreeSMS());
                   	response.setSubscriberBenefitSMS(bonusSMS);
                }
            } catch(XmlRpcConnectionException e) {
                logger.error("Could not connect to Charging System for processing Subscriber Benefits: " + e.getMessage());
            } catch(Exception e) {
                logger.error("Exception while processing Subscriber Benefits: " + e.getMessage());
            }
        } else {
           logger.debug("Will not execute subscriber benefits processor for this request.");
        }
       
        if(nextHandler != null) nextHandler.process(request, response);
    }
}
