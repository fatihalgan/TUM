/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.gateway.service.chain;

import com.bridge.ena.cs.command.CSTokens;
import com.bridge.ena.cs3cp1.command.CS3CP1Tokens;
import com.bridge.ena.xmlrpc.serializer.Member;
import com.bridge.ena.xmlrpc.serializer.MethodCall;
import com.bridge.ena.xmlrpc.serializer.MethodResponse;
import java.util.Date;
import mcel.tump.gateway.service.ITUMPGatewayService;
import mcel.tump.gateway.util.TUMPRequest;
import mcel.tump.gateway.util.TUMPResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.bridge.ena.xmlrpc.command.XMLRPCTokens;

/**
 *
 * @author db2admin
 */
public class RefillProcessor implements VTURequestProcessor {
    
    private ITUMPGatewayService tumGatewayService;
    private static Log logger = LogFactory.getLog(RefillProcessor.class);
    
    public MethodResponse process(MethodCall request) {
        logger.debug("Refill Processor started..");
        String originTransactionID = (String)request.getMemberValue(CSTokens.OriginTransactionID.toString());
        logger.info("OriginTransactionID of the message is: " + originTransactionID);
        Date originTimeStamp = (Date)request.getMemberValue(CSTokens.OriginTimeStamp.toString());
        String subscriberNumber = (String)request.getMemberValue(CSTokens.SubscriberNumber.toString());
        String transactionAmountRefill = (String)request.getMemberValue(CS3CP1Tokens.TransactionAmountRefill.toString());
        String requestDealerID = (String)request.getMember(CSTokens.ExternalData2.toString()).getValue();
        if((originTransactionID == null) || (originTimeStamp == null) || (subscriberNumber == null)
                || (transactionAmountRefill == null) || (requestDealerID == null)) {
            MethodResponse response = new MethodResponse();
            response.generateFaultResponse(1001, "Mandatory field missing.");
            return response;
        }
        Float amount = Float.parseFloat(transactionAmountRefill);
        Integer refillAmount = (int)(amount / 100);
        TUMPRequest tumpRequest = new TUMPRequest();
        tumpRequest.generateRechargeSubscriberRequest(null, null, originTransactionID, requestDealerID, null, subscriberNumber, refillAmount, originTimeStamp);
        TUMPResponse tumpResponse = new TUMPResponse();
        try {
            getTumGatewayService().rechargeSubscriber(tumpRequest, tumpResponse);
        } catch(TUMRechargeFaultResponseException e) {
            //No need to do anything, because the fault details are already in tumpResponse...
            logger.error("Fault occured during recharge operation..Rolled back the recharge operation..");
        }
        logger.debug("Response received from TUM system. Converting adjustment result to a refill");
        return convertAdjustmentResponseToRefillResponse(tumpResponse);
    }

    public ITUMPGatewayService getTumGatewayService() {
        return tumGatewayService;
    }

    public void setTumGatewayService(ITUMPGatewayService tumGatewayService) {
        this.tumGatewayService = tumGatewayService;
    }
    
    private MethodResponse convertAdjustmentResponseToRefillResponse(TUMPResponse tumpResponse) {
        MethodResponse response = new MethodResponse();
        if(tumpResponse.isFault()) {
            logger.debug("Found a fault response..");
            if(tumpResponse.getFaultCode() >= 1000) {
                logger.debug("Charging System Error With FaultCode: " + tumpResponse.getFaultCode());
                response.generateFaultResponse(tumpResponse.getFaultCode(), "Charging System Error");
            } if(tumpResponse.getFaultCode() <= 100) {
                response.generateFaultResponse(tumpResponse.getFaultCode(), "TUM System Error");
            } else {
                logger.debug("TUM System Error With FaultCode: " + tumpResponse.getFaultCode());
                //return response;
                response.generateFaultResponse(tumpResponse.getFaultCode(), "TUM System Error");
            }
        } else {
            response.addMember(new Member(CSTokens.ServiceClassCurrent.toString(), tumpResponse.getServiceClassCurrent()));
            response.addMember(new Member(CSTokens.Currency1.toString(), "MZM"));
            Integer balanceAfter = new Float(Float.valueOf(tumpResponse.getSubscriberBalanceAfter()) * 100).intValue();
            response.addMember(new Member(CS3CP1Tokens.AccountValueAfter1.toString(), balanceAfter.toString()));
            response.addMember(new Member(CS3CP1Tokens.SupervisionDateAfter.toString(), tumpResponse.getSupervisionDate()));
            response.addMember(new Member(CS3CP1Tokens.ServiceFeeDateAfter.toString(), tumpResponse.getServiceFeeDate()));
            response.addMember(new Member(XMLRPCTokens.ResponseCode.toString(), tumpResponse.getResponseCode()));
        }
        return response;
    }
}
