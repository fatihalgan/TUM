package mcel.tump.gateway.service.chain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpStatus;

import mcel.tump.gateway.util.TUMRechargeRequest;
import mcel.tump.gateway.util.TUMRechargeResponse;
import mcel.tump.gateway.util.TUMResponseCodes;

import com.bridge.ena.vs.command.CommandFactory;
import com.bridge.ena.vs.command.ReserveVoucherCommand;
import com.bridge.ena.xmlrpc.exception.XmlRpcConnectionException;

public class VSReserveVoucherAdapter implements CSCommandAdapter {
	
	private CommandFactory commandFactory;
	private TUMRechargeRequest request;
	private TUMRechargeResponse response;
	private static final Log logger = LogFactory.getLog(VSReserveVoucherAdapter.class);
	
	public VSReserveVoucherAdapter(CommandFactory factory, TUMRechargeRequest request, TUMRechargeResponse response) {
		this.request = request;
		this.response = response;
		this.commandFactory = factory;
	}
	
	public TUMRechargeRequest getRequest() {
		return request;
	}

	public TUMRechargeResponse getResponse() {
		return response;
	}
	
	public void execute() {
		ReserveVoucherCommand cmd = commandFactory.getReserveVoucherCommand(request.getVoucherActivationCode(), request.getSubscriberMSISDN(), request.getRequestTransactionID(), 100f);
		try {
			logger.debug("Sending Reserve Voucher command to Voucher Server..");
			cmd.execute();
			response.setHttpStatusCode(HttpStatus.SC_OK);
			if(cmd.isFault()) {
                logger.warn("Received Fault response from Voucher Server: " + cmd.getFaultCode());
                response.generateFaultResponse(cmd.getFaultCode(), request.getRequestTransactionID());
            } else if(cmd.isErrorOrFaultResponse()) {
                logger.warn("Received Error response from Voucher Server: " + cmd.getResponseCode());
                response.generateFaultResponse(cmd.getResponseCode(), request.getRequestTransactionID());
            } else {
            	 logger.debug("Received Success response from Voucher Server: " + cmd.getResponseCode());
            	 response.setVoucherSerialNumber(cmd.getSerialNumber());
            	 response.setVoucherValue(String.valueOf(cmd.getVoucherValue(100f)));	 
            }
		}  catch(XmlRpcConnectionException e) {
            logger.error("Could not connect to Voucher Server: " + e.getMessage());
            response.generateFaultResponse(TUMResponseCodes.CS_UNAVAILABLE.getResponseCode(), request.getRequestTransactionID());
            response.setHttpStatusCode(HttpStatus.SC_NOT_FOUND);
        } finally {
        	if(request.getSenderMSISDN() != null) response.setSenderMSISDN(request.getSenderMSISDN());
        }
	}
}
