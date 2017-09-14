package mcel.tump.gateway.service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import mcel.tump.gateway.handler.TUMRequestHandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class TUMGatewayServlet extends AbstractTUMGatewayServlet {

	private static final long serialVersionUID = -461062113556126115L;
	private static final Log logger = LogFactory.getLog(TUMGatewayServlet.class);
	
	@Override
	public void init() throws ServletException {
		super.init();
		ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        requestHandler = (TUMRequestHandler)ctx.getBean("signatureVerificationHandler");
        responseHandler = (TUMRequestHandler)ctx.getBean("signatureGenerationHandler");
	}
	
	@Override
	public void processResponseHeaders(HttpServletResponse response, String responseBody) {
		if(responseBody != null)
			response.setHeader("Content-Length", String.valueOf(responseBody.getBytes().length));		
	}

}
