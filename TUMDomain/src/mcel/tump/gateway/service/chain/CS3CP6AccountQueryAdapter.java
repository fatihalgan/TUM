package mcel.tump.gateway.service.chain;

import java.util.Date;

import mcel.tump.gateway.util.TUMPResponse;
import mcel.tump.gateway.util.TUMRechargeRequest;
import mcel.tump.gateway.util.TUMRechargeResponse;
import mcel.tump.gateway.util.TUMResponseCodes;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpStatus;

import com.bridge.ena.cs3cp6.command.CommandFactory;
import com.bridge.ena.cs3cp6.command.GetAccountDetailsCommand;
import com.bridge.ena.xmlrpc.exception.XmlRpcConnectionException;

public class CS3CP6AccountQueryAdapter implements CSCommandAdapter {

	private CommandFactory commandFactory;
    private TUMRechargeRequest request;
    private TUMRechargeResponse response;
    private static final Log logger = LogFactory.getLog(CS3CP6AccountQueryAdapter.class);
    
    public CS3CP6AccountQueryAdapter(CommandFactory factory, TUMRechargeRequest request, TUMRechargeResponse response) {
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

	@Override
	public void execute() {
		GetAccountDetailsCommand cmd = commandFactory.getAccountDetailsCommand(request.getRequestTransactionID(), request.getRequestTimeStamp(), request.getSubscriberMSISDN(), 100f);
		try {
			logger.debug("Sending GetAccountDetails command to Charging System..");
			cmd.execute();
			response.setHttpStatusCode(HttpStatus.SC_OK);
			if(cmd.isFault()) {
                logger.warn("Received Fault response from Charging System: " + cmd.getFaultCode());
                response.generateFaultResponse(cmd.getFaultCode(), request.getRequestTransactionID());
            } else if(cmd.isErrorOrFaultResponse()) {
                logger.warn("Received Error response from Charging System: " + cmd.getResponseCode());
                response.generateFaultResponse(cmd.getResponseCode(), request.getRequestTransactionID());
            } else{
            	logger.debug("Received Success response from Charging System: " + cmd.getResponseCode());
            	TUMPResponse res = (TUMPResponse)response;
            	res.generateCheckSubscriberValidResponse(request.getRequestTransactionID(), new Date(), cmd.getTemporaryBlockedFlag(), cmd.getRefillUnbarDateTime(), 0);
            }
		} catch(XmlRpcConnectionException e) {
            logger.error("Could not connect to Charging System: " + e.getMessage());
            response.generateFaultResponse(TUMResponseCodes.CS_UNAVAILABLE.getResponseCode(), request.getRequestTransactionID());
            response.setHttpStatusCode(HttpStatus.SC_NOT_FOUND);
            response.setRefillResultCode(cmd.getHttpStatusCode());
        } 
	}
}
