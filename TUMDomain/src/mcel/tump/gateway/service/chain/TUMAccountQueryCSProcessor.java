package mcel.tump.gateway.service.chain;

import mcel.tump.gateway.util.TUMGWTokens;
import mcel.tump.gateway.util.TUMRechargeRequest;
import mcel.tump.gateway.util.TUMRechargeResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bridge.ena.cs3cp6.command.CommandFactory;

public class TUMAccountQueryCSProcessor implements RechargeRequestProcessor {

	private RechargeRequestProcessor nextHandler;
    private RechargeRequestProcessor onFaultHandler;
    private CommandFactory commandFactory;
    private static final Log logger = LogFactory.getLog(TUMAccountQueryCSProcessor.class);
    
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
        if(request.getMethodName().equals(TUMGWTokens.ReserveRechargeSubscriberRequest.toString())) {
        	logger.debug("TUM Account Query CS Processor started..");
        	CSCommandAdapter adapter = null;
        	logger.debug("Trying to check subscriber validity from Charging System");
        	adapter = new CS3CP6AccountQueryAdapter(getCommandFactory(), request, response);
        	adapter.execute();
        	if(response.isFault()) {
        		logger.error("Fault response from Charging System received with code " + response.getFaultCode() + " from charging system.");
        		return;
        	}
        }
        if(nextHandler !=  null) getNextHandler().process(request, response);
    }
}
