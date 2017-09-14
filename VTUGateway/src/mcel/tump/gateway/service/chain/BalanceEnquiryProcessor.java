/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.gateway.service.chain;

import com.bridge.ena.cs3cp1.command.BalanceEnquiryTCommand;
import com.bridge.ena.cs.command.CSTokens;
import com.bridge.ena.cs3cp6.command.CommandFactory;
import com.bridge.ena.cs3cp6.command.GetBalanceAndDateCommand;
import com.bridge.ena.xmlrpc.exception.XmlRpcConnectionException;
import com.bridge.ena.xmlrpc.serializer.MethodCall;
import com.bridge.ena.xmlrpc.serializer.MethodResponse;
import java.util.Date;
import mcel.tump.gateway.util.TUMResponseCodes;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author db2admin
 */
public class BalanceEnquiryProcessor implements VTURequestProcessor {

    private CommandFactory commandFactory;
    private static Log logger = LogFactory.getLog(BalanceEnquiryProcessor.class);
    
    public MethodResponse process(MethodCall request) {
    	logger.debug("Started BalanceEnquiry processor...");
        MethodResponse response = new MethodResponse();
        String subscriberNumber = (String)request.getMemberValue(CSTokens.SubscriberNumber.toString());
        Date originTimeStamp = (Date)request.getMemberValue(CSTokens.OriginTimeStamp.toString());
        String originTransactionID = (String)request.getMemberValue(CSTokens.OriginTransactionID.toString());
        if((subscriberNumber == null) || (originTimeStamp == null) || (originTransactionID == null)) {
            response.generateFaultResponse(1000, "Illegal Request Message");
            return response;
        }
        logger.info("Received a BalanceEnquiry request from VTU with OriginTransactionID: " + originTransactionID);
        GetBalanceAndDateCommand cmd = commandFactory.getBalanceAndDateCommand(subscriberNumber, originTimeStamp, originTransactionID, 1);
        try {
            return cmd.execute();
        } catch(XmlRpcConnectionException e) {
            logger.error("Could not connect to Charging System: " + e.getMessage());
            response.generateFaultResponse(TUMResponseCodes.CS_UNAVAILABLE.getResponseCode(), "CS Unavailable");
        }
        return response;
    }

    public void setCommandFactory(CommandFactory factory) {
        this.commandFactory = factory;
    }
    
}
