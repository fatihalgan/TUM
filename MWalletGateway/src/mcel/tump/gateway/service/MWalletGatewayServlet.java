package mcel.tump.gateway.service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import mcel.tump.gateway.handler.TUMRequestHandlerException;
import mcel.tump.gateway.vtu.adapter.IVTUGatewayService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.bridge.ena.cs3cp6.command.CS3CP6Tokens;
import com.bridge.ena.xmlrpc.exception.XmlRpcConnectionException;
import com.bridge.ena.xmlrpc.serializer.MethodCall;
import com.bridge.ena.xmlrpc.serializer.MethodResponse;

public class MWalletGatewayServlet extends AbstractCSGatewayServlet {
	
	private static final Log logger = LogFactory.getLog(MWalletGatewayServlet.class);
	
	public void init() throws ServletException {
		super.init();
		gateway = (IVTUGatewayService)ctx.getBean("mwalletGatewayService");
	}
	
	protected MethodResponse invokeOperation(MethodCall vtuRequest) throws XmlRpcConnectionException, TUMRequestHandlerException {
        String opName = vtuRequest.getMethodName();
        if(opName.equals(CS3CP6Tokens.RefillMethodCallName.toString()))
            return gateway.rechargeSubscriber(vtuRequest);
        else return null;
    }
	
	public void processResponseHeaders(HttpServletResponse response, String responseBody) {
		if(responseBody != null)
		response.setHeader("Content-Length", String.valueOf(responseBody.getBytes().length));
	}
}
