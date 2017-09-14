package mcel.tump.gateway.service.chain;

import mcel.tump.gateway.util.TUMGWTokens;
import mcel.tump.gateway.util.TUMRechargeRequest;
import mcel.tump.gateway.util.TUMRechargeResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bridge.ena.vs.command.CommandFactory;

public class TUMVSProcessor implements RechargeRequestProcessor {

	private RechargeRequestProcessor nextHandler;
	private RechargeRequestProcessor onFaultHandler;
	private CommandFactory commandFactory;
	private static final Log logger = LogFactory.getLog(TUMVSProcessor.class);
	
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
		 logger.debug("TUM VS Processor started..");
	     if(response.isFault()) {
	    	 logger.error("Fault response received with code " + response.getFaultCode() + " from TUM System. Will skip going to Voucher Server..");
	         if(nextHandler !=  null) getNextHandler().process(request, response);
	         return;
	     }
	     CSCommandAdapter adapter = null;
	     if(request.getMethodName().equals(TUMGWTokens.ReserveVoucherRequest.toString())) {
	    	 logger.debug("Trying to reserve a voucher from Voucher Server: " + request.getVoucherActivationCode());
	         adapter = new VSReserveVoucherAdapter(getCommandFactory(), request, response);
	         adapter.execute();
	     } else if(request.getMethodName().equals(TUMGWTokens.CommitVoucherRequest.toString())) {
	    	 logger.debug("Trying to commit reserved voucher from voucher Server: " + request.getVoucherActivationCode());
	         adapter = new VSCommitReserveVoucherAdapter(getCommandFactory(), request, response);
	         adapter.execute();
	     } else if(request.getMethodName().equals(TUMGWTokens.CancelVoucherRequest.toString())) {
	    	 logger.debug("Trying to cancel reserved voucher from voucher Server: " + request.getVoucherActivationCode());
	         adapter = new VSCancelReserveVoucherAdapter(getCommandFactory(), request, response);
	         adapter.execute();
	     }
	     if(response.isFault()) convertToTUMFault(response);
	     if(nextHandler !=  null) getNextHandler().process(request, response);
	}
	
	private void convertToTUMFault(TUMRechargeResponse response) {
		if(response.getFaultCode() == 10) {
			response.generateFaultResponse(204, response.getRequestTransactionID());
		} else if(response.getFaultCode() == 11) {
			response.generateFaultResponse(207, response.getRequestTransactionID());
		} else if(response.getFaultCode() == 12) {
			response.generateFaultResponse(209, response.getRequestTransactionID());
		} else if(response.getFaultCode() == 13) {
			response.generateFaultResponse(210, response.getRequestTransactionID());
		} else if(response.getFaultCode() == 100) {
			response.generateFaultResponse(200, response.getRequestTransactionID());
		} else if(response.getFaultCode() == 101) {
			response.generateFaultResponse(201, response.getRequestTransactionID());
		} else if(response.getFaultCode() == 102) {
			response.generateFaultResponse(202, response.getRequestTransactionID());
		} else if(response.getFaultCode() == 108) {
			response.generateFaultResponse(208, response.getRequestTransactionID());
		}
	}
}
