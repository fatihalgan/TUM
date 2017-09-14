package mcel.tump.gateway.service.chain;

import mcel.tump.gateway.util.TUMGWTokens;
import mcel.tump.gateway.util.TUMRechargeRequest;
import mcel.tump.gateway.util.TUMRechargeResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bridge.ena.cs3cp6.command.CommandFactory;

public class ReservedRechargeAccountQueryProcessor implements RechargeRequestProcessor {

	private RechargeRequestProcessor nextHandler;
    private RechargeRequestProcessor onFaultHandler;
    private CommandFactory commandFactory;
    private static final Log logger = LogFactory.getLog(ReservedRechargeAccountQueryProcessor.class);
    
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
    	 logger.debug("TUM AccountQuery Processor started for transaction: " + request.getRequestTransactionID());
         if(response.isFault()) {
             logger.error("Fault response from Charging System received with code " + response.getFaultCode() + " from charging system.");
             if(nextHandler !=  null) getNextHandler().process(request, response);
             return;
         }
         
         CSCommandAdapter adapter = null;
         if(request.getMethodName().equals(TUMGWTokens.ReserveRechargeSubscriberRequest.toString())) {
        	 logger.debug("Trying to do GetAccountDetails Query to see if a Recharge Reservation can be made for transaction " + request.getRequestTransactionID());
        	 adapter = new CS3CP6ReserveRechargeSubscriberAdapter(getCommandFactory(), request, response);
        	 adapter.execute();       	 
         }
         if(nextHandler !=  null) getNextHandler().process(request, response);
    }
}
