/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.gateway.service;

import com.bridge.ena.cs3cp1.command.CS3CP1Tokens;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import com.bridge.ena.xmlrpc.exception.XmlRpcConnectionException;
import com.bridge.ena.xmlrpc.serializer.MethodCall;
import com.bridge.ena.xmlrpc.serializer.MethodResponse;

import mcel.tump.gateway.handler.TUMRequestHandlerException;
import mcel.tump.gateway.vtu.adapter.IVTUGatewayService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author db2admin
 */
public class VTUGatewayServlet extends AbstractCSGatewayServlet {

    private static final Log logger = LogFactory.getLog(VTUGatewayServlet.class);
        
    public void init() throws ServletException {
        super.init();
        gateway = (IVTUGatewayService)ctx.getBean("vtuGatewayService");
    }
    
    protected MethodResponse invokeOperation(MethodCall vtuRequest) throws XmlRpcConnectionException, TUMRequestHandlerException {
        String opName = vtuRequest.getMethodName();
        if(opName.equals(CS3CP1Tokens.BalanceEnquiryMethodCallName.toString()))
            return gateway.balanceCheck(vtuRequest);
        else if(opName.equals(CS3CP1Tokens.RefillMethodCallName.toString()))
            return gateway.rechargeSubscriber(vtuRequest);
        else return null;
    }
    
    public void processResponseHeaders(HttpServletResponse response, String responseBody) {
		return;
	}
    
}
