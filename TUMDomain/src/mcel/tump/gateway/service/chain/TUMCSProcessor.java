/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.gateway.service.chain;

import com.bridge.ena.cs3cp6.command.CommandFactory;

import mcel.tump.gateway.util.TUMGWTokens;
import mcel.tump.gateway.util.TUMRechargeRequest;
import mcel.tump.gateway.util.TUMRechargeResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author db2admin
 */
public class TUMCSProcessor implements RechargeRequestProcessor {

    private RechargeRequestProcessor nextHandler;
    private RechargeRequestProcessor onFaultHandler;
    private CommandFactory commandFactory;
    private static final Log logger = LogFactory.getLog(TUMCSProcessor.class);

    public void setNextHandler(RechargeRequestProcessor processor) {
        this.nextHandler = processor;
    }

    public RechargeRequestProcessor getNextHandler() {
        return this.nextHandler;
    }

    public CommandFactory getCommandFactory() {
        return commandFactory;
    }

    public void setCommandFactory(CommandFactory commandFactory) {
        this.commandFactory = commandFactory;
    }

    public RechargeRequestProcessor getOnFaultHandler() {
        return onFaultHandler;
    }

    public void setOnFaultHandler(RechargeRequestProcessor onFaultHandler) {
        this.onFaultHandler = onFaultHandler;
    }

    public void process(TUMRechargeRequest request, TUMRechargeResponse response) {
        logger.debug("TUM CS Processor started for transaction: " + request.getRequestTransactionID());
        if(response.isFault()) {
            logger.error("Fault response from Charging System received with code " + response.getFaultCode() + " from charging system.");
            if(nextHandler !=  null) getNextHandler().process(request, response);
            return;
        }
        CSCommandAdapter adapter = null;
        if(request.getMethodName().equals(TUMGWTokens.RechargeSubscriberRequest.toString())) {
        	logger.debug("Trying to make pinless refill from Charging System for transaction " + request.getRequestTransactionID());
        	adapter = new CS3CP6RefillAdapter(getCommandFactory(), request, response);
        	adapter.execute();
        } else if(request.getMethodName().equals(TUMGWTokens.RechargeSubscriberSMSRequest.toString())) {
        	logger.debug("Trying to make SMS adjustment from Charging System for transaction " + request.getRequestTransactionID());
        	adapter = new CS3CP6SMSAdjustmentAdapter(getCommandFactory(), request, response);
        	adapter.execute();
        } else if(request.getMethodName().equals(TUMGWTokens.AdjustSubscriberAccountRequest.toString())) {
        	logger.debug("Trying to make adjustment topup from Charging system for transaction " + request.getRequestTransactionID());
        	adapter = new CS3CP6AdjustmentAdapter(getCommandFactory(), request, response);
        	adapter.execute();
        } else if(request.getMethodName().equals(TUMGWTokens.RechargeSubscriberPinRequest.toString())) {
        	logger.debug("Trying to make pinned refill from Charging System for transaction " + request.getRequestTransactionID());
        	adapter = new CS3CP6PinnedRefillAdapter(getCommandFactory(), request, response);
        	adapter.execute();
        } else if(request.getMethodName().equals(TUMGWTokens.CheckSubscriberValidRequest.toString())) {
        	logger.debug("Trying to get Account Details from Charging System " + request.getRequestTransactionID());
        	adapter = new CS3CP6AccountQueryAdapter(getCommandFactory(), request, response);
        	adapter.execute();
        } 
        if(nextHandler !=  null) getNextHandler().process(request, response);
    }
}
