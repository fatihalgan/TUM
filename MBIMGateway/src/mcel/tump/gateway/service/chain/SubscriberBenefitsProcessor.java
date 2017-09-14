/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.gateway.service.chain;

import java.util.ArrayList;
import java.util.List;

import com.bridge.ena.cs3cp6.command.CommandFactory;
import com.bridge.ena.cs3cp6.command.GenericNetmovelSupportUpdateBalanceAndDateCommand;
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
    	logger.debug("SubscriberBenefits processor started...");
    	/**
    	if(!response.isFault() && !request.getMethodName().equals(TUMGWTokens.RechargeSubscriberPinRequest.toString())
    		&& !request.getMethodName().equals(TUMGWTokens.AdjustSubscriberAccountRequest.toString())) {
        	GenericNetmovelSupportUpdateBalanceAndDateCommand cmd = null;
        	int bonusSMS = 0;
        	float freeCalls = 0f;
        	
        	if(request.getTransferAmount().intValue() > 0 && request.getTransferAmount().intValue() <= 19) bonusSMS = 2;
        	else if(request.getTransferAmount().intValue() >= 20 && request.getTransferAmount().intValue() <= 49) bonusSMS = 4;
        	else if(request.getTransferAmount().intValue() >= 50 && request.getTransferAmount().intValue() <= 79) bonusSMS = 10;
        	else if(request.getTransferAmount().intValue() >= 80 && request.getTransferAmount().intValue() <= 99) bonusSMS = 15;
        	else if(request.getTransferAmount().intValue() >= 100 && request.getTransferAmount().intValue() <= 149) bonusSMS = 40;
        	else if(request.getTransferAmount().intValue() >= 150 && request.getTransferAmount().intValue() <= 299) bonusSMS = 50;
        	else if(request.getTransferAmount().intValue() >= 300 && request.getTransferAmount().intValue() <= 599) bonusSMS = 50;
        	else if(request.getTransferAmount().intValue() >= 600 && request.getTransferAmount().intValue() <= 1999) bonusSMS = 50;
        	else if(request.getTransferAmount().intValue() >= 2000) bonusSMS = 50;
        	
        	List<DedicatedAccountInformation> dedicatedAccountAdjustments = new ArrayList<DedicatedAccountInformation>();
        	
        	DedicatedAccountInformation smsDa = new DedicatedAccountInformation();
        	smsDa.setDedicatedAccountId(1);
        	smsDa.setDedicatedAccountValue(new Float(bonusSMS));
        	dedicatedAccountAdjustments.add(smsDa);
        	
        	cmd = commandFactory.getGenericNetmovelBundleSupportUpdateBalanceAndDateCommand(request.getRequestTransactionID(),
        		request.getRequestTimeStamp(), request.getSubscriberMSISDN(), 0f, "0", response.getTUMTransactionID(), 0, 0, response.getServiceClassCurrent(), 100f, dedicatedAccountAdjustments);
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
                    response.setFreeSMS(response.getFreeSMS() + bonusSMS);
                    response.setSubscriberBenefitBonusAmount(freeCalls);
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
        **/
        if(nextHandler != null) nextHandler.process(request, response);
    }
}
