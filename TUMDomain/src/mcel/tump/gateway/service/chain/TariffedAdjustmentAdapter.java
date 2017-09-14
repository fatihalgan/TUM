/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.gateway.service.chain;

import mcel.tump.gateway.util.TUMRechargeResponse;
import mcel.tump.gateway.util.TUMRechargeRequest;
import com.bridge.ena.cs3cp1.command.CommandFactory;
import com.bridge.ena.cs3cp1.command.TariffedAdjustmentCommand;
import com.bridge.ena.xml.BadXmlFormatException;
import com.bridge.ena.xmlrpc.exception.XmlRpcConnectionException;
import mcel.tump.gateway.util.TUMResponseCodes;
import mcel.tump.reference.domain.SubscriberBenefit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpStatus;

/**
 *
 * @author db2admin
 */
public class TariffedAdjustmentAdapter implements CSCommandAdapter {

    private CommandFactory commandFactory;
    private TUMRechargeRequest request;
    private TUMRechargeResponse response;
    private SubscriberBenefit benefit;
    private static final Log logger = LogFactory.getLog(TariffedAdjustmentAdapter.class);
    
    public TariffedAdjustmentAdapter(CommandFactory facotry, TUMRechargeRequest request, TUMRechargeResponse response, SubscriberBenefit benefit) {
        this.request = request;
        this.response = response;
        this.commandFactory = facotry;
        this.benefit = benefit;
    }
    
    public TUMRechargeRequest getRequest() {
        return request;
    }

    public TUMRechargeResponse getResponse() {
        return response;
    }
    
    public void execute() {
        TariffedAdjustmentCommand cmd = commandFactory.getTariffedAdjustmentCommand(
                    request.getRequestTransactionID(), 
                    request.getRequestTimeStamp(), request.getSubscriberMSISDN(), 
                    request.getTransferAmount().floatValue(), 
                    request.getTransferAmount().toString(), 
                    response.getTUMTransactionID(), benefit.getFreeSMS(), 
                    benefit.getFreeMMS(), 
                    benefit.getInstantBonus().getAmount().floatValue(), 
                    benefit.getMakeCallTimeFrame(), 
                    benefit.getReceiveCallTimeFrame(), 100);
        try {
            logger.debug("Sending Tariffed Adjustment request to Charging System...");
            cmd.execute();
            response.setHttpStatusCode(HttpStatus.SC_OK);
            if(cmd.isFault()) {
                logger.warn("Received Fault response from Charging System: " + cmd.getFaultCode());
                response.generateFaultResponse(cmd.getFaultCode(), request.getRequestTransactionID());
            } else if(cmd.isErrorOrFaultResponse()) {
                logger.warn("Received Error response from Charging System: " + cmd.getResponseCode());
                response.generateFaultResponse(cmd.getResponseCode(), request.getRequestTransactionID());
            } else {
                logger.debug("Received Success response from Charging System: " + cmd.getResponseCode());
            }
            response.setSubscriberBalanceBefore(cmd.getSubscriberInfo().getBalanceBefore().toString());
            response.setSubscriberBalanceAfter(cmd.getSubscriberInfo().getAccountValue().toString());
            response.setSubscriberLanguage(cmd.getSubscriberInfo().getLanguageLocale());
            response.setServiceClassCurrent(cmd.getSubscriberInfo().getServiceClassCurrent());
            response.setServiceClassBefore(cmd.getSubscriberInfo().getServiceClassBefore());
            response.setSupervisionDate(cmd.getSubscriberInfo().getSupervisionDate());
            response.setServiceFeeDate(cmd.getSubscriberInfo().getServiceFeeDate());
            response.setFreeMMS(cmd.getSubscriberInfo().getSmsBalance() - cmd.getSubscriberInfo().getSmsBalanceBefore());
            response.setFreeMMS(cmd.getSubscriberInfo().getMmsBalance() - cmd.getSubscriberInfo().getMmsBalanceBefore());
        } catch(XmlRpcConnectionException e) {
            logger.error("Could not connect to Charging System: " + e.getMessage());
            response.generateFaultResponse(TUMResponseCodes.CS_UNAVAILABLE.getResponseCode(), request.getRequestTransactionID());
            response.setHttpStatusCode(HttpStatus.SC_NOT_FOUND);
            
        } catch(BadXmlFormatException e) {
            logger.error("Bad XML Format in message to Charging System: " + e.getMessage());
            response.generateFaultResponse(TUMResponseCodes.CS_ILLEGAL_REQUEST_MESSAGE.getResponseCode(), request.getRequestTransactionID());
            response.setHttpStatusCode(HttpStatus.SC_BAD_REQUEST);
        }       
    }
}
