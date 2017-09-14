package mcel.tump.gateway.service.chain;

import java.util.Date;

import mcel.tump.gateway.handler.TUMRequestHandlerException;
import mcel.tump.gateway.service.TUMGatewayClient;
import mcel.tump.gateway.util.TUMPRequest;
import mcel.tump.gateway.util.TUMPResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bridge.ena.cs.command.CSTokens;
import com.bridge.ena.cs3cp6.command.CS3CP6Tokens;
import com.bridge.ena.vs.command.Action;
import com.bridge.ena.vs.command.VSTokens;
import com.bridge.ena.xmlrpc.exception.XmlRpcConnectionException;
import com.bridge.ena.xmlrpc.serializer.Member;
import com.bridge.ena.xmlrpc.serializer.MethodCall;
import com.bridge.ena.xmlrpc.serializer.MethodResponse;
import com.bridge.ena.xmlrpc.serializer.Struct;

public class RefillProcessor implements VTURequestProcessor {

	private TUMGatewayClient tumGatewayClient;
	private static Log logger = LogFactory.getLog(RefillProcessor.class);
   	
    public TUMGatewayClient getTumGatewayClient() {
		return tumGatewayClient;
	}

	public void setTumGatewayClient(TUMGatewayClient tumGatewayClient) {
		this.tumGatewayClient = tumGatewayClient;
	}
	
	public MethodResponse process(MethodCall request) throws XmlRpcConnectionException, TUMRequestHandlerException {
        logger.debug("Refill Processor started..");
        TUMPResponse tumpResponse = null;
        if(request.getMethodName().equals(VSTokens.ReserveVoucherMethodCallName.toString())) {
        	logger.debug("Requested operation is: " + VSTokens.ReserveVoucherMethodCallName.toString());
        	TUMPRequest tumpRequest = convertReserveVoucherRequestMessage(request);
        	tumpResponse = getTumGatewayClient().sendMessage(tumpRequest);
        	logger.debug("Response received from TUM system. Converting ReservePin result to VTU mode..");
        	return convertReserveVoucherResponseMessage(tumpResponse);
        } else if(request.getMethodName().equals(VSTokens.EndReservationMethodCallName.toString())) {
        	logger.debug("Requested operation is: " + VSTokens.EndReservationMethodCallName + " with action: " + request.getMemberValue(VSTokens.Action.toString()));
        	if(request.getMemberValue(VSTokens.Action.toString()).equals(Action.Commit.toString())) {
        		TUMPRequest tumpRequest = convertCommitVoucherReservationRequestMessage(request);
        		tumpResponse = getTumGatewayClient().sendMessage(tumpRequest);
        		logger.debug("Response received from TUM system. Converting EndReservePin result to VTU mode..");
        		return convertEndReservationResponseMessage(tumpResponse);
        	} else if(request.getMemberValue(VSTokens.Action.toString()).equals(Action.Rollback.toString())) {
        		TUMPRequest tumpRequest = convertCancelVoucherReservationRequestMessage(request);
        		tumpResponse = getTumGatewayClient().sendMessage(tumpRequest);
        		logger.debug("Response received from TUM system. Converting EndReservePin result to VTU mode..");
        		return convertEndReservationResponseMessage(tumpResponse);
        	}
        } else if(request.getMethodName().equals(CS3CP6Tokens.RefillMethodCallName.toString())) {
        	TUMPRequest tumpRequest = convertRefillRequestMessage(request);
        	try {
        		tumpResponse = getTumGatewayClient().sendMessage(tumpRequest);
        	} catch(TUMRechargeFaultResponseException re) {
        		//Do nothing.. Fault response will be generated after catch block..
        	}
        	logger.debug("Response received from TUM system. Converting RechargeSubscriber result to VTU mode..");
        	return convertRechargeSubscriberResponseMessage(tumpResponse);
        } else {
        	tumpResponse.generateFaultResponse(1000, "Illegal Request Message");
        	MethodResponse out = new MethodResponse();
        	convertCSFaultResponseMessage(tumpResponse, out);
        	return out;
        }
        return null;
    }
	
	private TUMPRequest convertRefillRequestMessage(MethodCall in) {
		TUMPRequest out = new TUMPRequest();
		//TODO: Need to convert origin Transaction ID?
		String requestTransactionId = (String)in.getMemberValue(CSTokens.OriginTransactionID.toString());
		String subscriberMSISDN = (String)in.getMemberValue(CSTokens.SubscriberNumber.toString());
		if(subscriberMSISDN.length() == 12) {
			subscriberMSISDN = subscriberMSISDN.substring(3);
		}
		String transactionAmount = (String)in.getMemberValue(CS3CP6Tokens.TransactionAmount.toString());
		float amount = Float.parseFloat(transactionAmount) / 100;
		Integer transferAmount = Math.round(amount);
		Date requestTimestamp = (Date)in.getMemberValue(CSTokens.OriginTimeStamp.toString());
		out.generateRechargeSubscriberRequest("mtnuser", "kzt21e", requestTransactionId, "CLI07050", null, subscriberMSISDN, transferAmount, requestTimestamp);
		return out;
	}
	
	private TUMPRequest convertReserveVoucherRequestMessage(MethodCall in) {
		TUMPRequest out = new TUMPRequest();
		String requestTransactionId = (String)in.getMemberValue(VSTokens.TransactionID.toString());
		String subscriberMsisdn = (String)in.getMemberValue(VSTokens.SubscriberID.toString());
		String vouhcerActivationCode = (String)in.getMemberValue(VSTokens.ActivationCode.toString());
		out.generateReserveVoucherRequest("mtnuser", "kzt21e", requestTransactionId, "CLI07050", null, subscriberMsisdn, vouhcerActivationCode, new Date());
		return out;
	}
	
	private TUMPRequest convertCommitVoucherReservationRequestMessage(MethodCall in) {
		TUMPRequest out = new TUMPRequest();
		String requestTransactionId = (String)in.getMemberValue(VSTokens.TransactionID.toString());
		out.generateCommitReserveVoucherRequest("mtnuser", "kzt21e", requestTransactionId, "CLI07050", null, new Date());
		return out;
	}
	
	private TUMPRequest convertCancelVoucherReservationRequestMessage(MethodCall in) {
		TUMPRequest out = new TUMPRequest();
		String requestTransactionId = (String)in.getMemberValue(VSTokens.TransactionID.toString());
		out.generateCancelReserveVoucherRequest("mtnuser", "kzt21e", requestTransactionId, "CLI07050", null, new Date());
		return out;
	}
	
	private void convertVSFaultResponseMessage(TUMPResponse in, MethodResponse out) {
		logger.debug("Found a fault response..");
		 if(in.getFaultCode() >= 1000) {
			 logger.debug("Charging System Error With FaultCode: " + in.getFaultCode());
            out.generateFaultResponse(in.getFaultCode(), "Charging System Error");
        } else {
       	 int responseCode = 0;
       	 if(in.getFaultCode() == 20) responseCode = 10;
       	 else if(in.getFaultCode() == 21) responseCode = 11;
       	 else if(in.getFaultCode() == 22) responseCode = 12;
       	 else if(in.getFaultCode() == 23) responseCode = 13;
       	 else if(in.getFaultCode() == 40) responseCode = 100;
       	 else if(in.getFaultCode() == 41) responseCode = 100;
       	 else if(in.getFaultCode() == 48) responseCode = 100;
       	 else if(in.getFaultCode() == 49) responseCode = 100;
       	 else if(in.getFaultCode() == 10) responseCode = 100;
       	 else if(in.getFaultCode() == 11) responseCode = 100;
       	 else if(in.getFaultCode() == 12) responseCode = 100;
       	 else if(in.getFaultCode() == 200) responseCode = 100;
       	 else if(in.getFaultCode() == 201) responseCode = 101;
       	 else if(in.getFaultCode() == 202) responseCode = 102;
       	 else if(in.getFaultCode() == 203) responseCode = 110;
       	 else if(in.getFaultCode() == 207) responseCode = 11;
       	 else if(in.getFaultCode() == 208) responseCode = 108;
       	 else responseCode = in.getFaultCode();
       	 logger.debug("Charging System Error With ResponseCode: " + responseCode);
       	 out.addMember(new Member(VSTokens.ResponseCode.toString(), responseCode));
        }
	}
	
	private void convertCSFaultResponseMessage(TUMPResponse in, MethodResponse out) {
		logger.debug("Found a fault response..");
		 if(in.getFaultCode() >= 1000) {
			 logger.debug("Charging System Error With FaultCode: " + in.getFaultCode());
           out.generateFaultResponse(in.getFaultCode(), "Charging System Error");
       } else {
    	   int responseCode = 0;
    	   if(in.getFaultCode() >= 10 && in.getFaultCode() <= 100) responseCode = 100;
    	   else if(in.getFaultCode() > 101 && in.getFaultCode() <= 136) responseCode = in.getFaultCode();
    	   else responseCode = in.getFaultCode();
    	   logger.debug("Charging System Error With ResponseCode: " + responseCode);
           out.addMember(new Member(CSTokens.ResponseCode.toString(), responseCode));
       }
	}
	
	private MethodResponse convertReserveVoucherResponseMessage(TUMPResponse in) {
		MethodResponse out = new MethodResponse();
		if(in.isFault()) {
			convertVSFaultResponseMessage(in, out); 
		} else {
			out.addMember(new Member(VSTokens.ResponseCode.toString(), 0));
			out.addMember(new Member(VSTokens.Agent.toString(), "TUM"));
			out.addMember(new Member(VSTokens.Currency.toString(), "MZM"));
			out.addMember(new Member(VSTokens.SerialNumber.toString(), in.getVoucherSerialNumber()));
			Float voucherVal = Float.parseFloat(in.getVoucherValue());
			String strVal = voucherVal.intValue() + "00";
			out.addMember(new Member(VSTokens.Value.toString(), strVal));
		}
		return out;
	}
	
	private MethodResponse convertRechargeSubscriberResponseMessage(TUMPResponse in) {
		MethodResponse out = new MethodResponse();
		if(in.isFault()) {
			convertCSFaultResponseMessage(in, out);
		} else {
			out.addMember(new Member(CSTokens.Currency1.toString(), "MZM"));
			out.addMember(new Member(CS3CP6Tokens.LanguageIDCurrent.toString(), new Integer(1)));
			out.addMember(new Member(CSTokens.OriginTransactionID.toString(), in.getRequestTransactionID()));
			out.addMember(new Member(CSTokens.ResponseCode.toString(), new Integer(0)));
			int transferAmount = in.getTransferAmount();
			transferAmount = transferAmount * 100;
			out.addMember(new Member(CS3CP6Tokens.TransactionAmount.toString(), String.valueOf(transferAmount)));
			out.addMember(new Member(CSTokens.TransactionCurrency.toString(), "MZM"));
			out.addMember(new Member(CS3CP6Tokens.VoucherGroup.toString(), "VSRS"));
			Struct aaf = new Struct();
			float subscriberBalanceAfter = Float.parseFloat(in.getSubscriberBalanceAfter());
			subscriberBalanceAfter = subscriberBalanceAfter * 100;
			aaf.addMember(new Member(CSTokens.AccountValue1.toString(), String.valueOf(new Float(subscriberBalanceAfter).intValue())));
			out.addMember(new Member(CS3CP6Tokens.AccountAfterRefill.toString(), aaf));
			Struct abf = new Struct();
			float subscriberBalanceBefore = Float.parseFloat(in.getSubscriberBalanceBefore());
			subscriberBalanceBefore = subscriberBalanceBefore * 100;
			abf.addMember(new Member(CSTokens.AccountValue1.toString(), String.valueOf(new Float(subscriberBalanceBefore).intValue())));
			out.addMember(new Member(CS3CP6Tokens.AccountBeforeRefill.toString(), abf));
		}
		return out;
	}
	
	private MethodResponse convertEndReservationResponseMessage(TUMPResponse in) {
		MethodResponse out = new MethodResponse();
		if(in.isFault()) {
			convertVSFaultResponseMessage(in, out); 
		} else {
			out.addMember(new Member(VSTokens.ResponseCode.toString(), 0));
		}
		return out;
	}
}
