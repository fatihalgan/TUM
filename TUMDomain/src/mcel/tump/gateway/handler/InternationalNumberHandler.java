/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.gateway.handler;

import com.bridge.ena.xmlrpc.serializer.Member;
import com.bridge.ena.xmlrpc.serializer.MethodCall;
import com.bridge.ena.xmlrpc.serializer.MethodResponse;
import mcel.tump.gateway.util.TUMPResponse;
import mcel.tump.gateway.util.TUMResponseCodes;

/**
 *
 * @author db2admin
 */
public class InternationalNumberHandler extends AbstractRequestHandler {

    public void processRequest(MethodCall request, MethodResponse response) throws TUMRequestHandlerException {
        try {
            Member member = request.getMember("subscriberNumber");
            String subscriberNumber = (String)member.getValue();
            if(subscriberNumber.startsWith("258")) subscriberNumber = subscriberNumber.substring(3);
            member.setValue(subscriberNumber);
            if(getNext() != null) getNext().processRequest(request, response);
        } catch(Exception e) {
            throw new TUMRequestHandlerException(TUMResponseCodes.CS_SUBSCRIBER_NOT_FOUND.getResponseCode(), "Subscriber Not Found");
        }
    }
}
