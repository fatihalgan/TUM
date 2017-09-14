package mcel.tump.gateway.service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import mcel.tump.gateway.handler.TUMRequestHandler;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class EzeTopGatewayServlet extends AbstractTUMGatewayServlet {

	private static final long serialVersionUID = -6789576455980568204L;
		
	@Override
	public void init() throws ServletException {
		super.init();
		ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        //requestHandler = (TUMRequestHandler)ctx.getBean("signatureVerificationHandler");
        //responseHandler = (TUMRequestHandler)ctx.getBean("signatureGenerationHandler");
	}
	
	@Override
	public void processResponseHeaders(HttpServletResponse response, String responseBody) {
		if(responseBody != null)
			response.setHeader("Content-Length", String.valueOf(responseBody.getBytes().length));		
	}
}
