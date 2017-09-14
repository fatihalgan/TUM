/*
 * XMLRPCServlet.java
 * 
 * Created on Sep 10, 2007, 12:25:28 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.gateway.service;

import javax.servlet.*;
import javax.servlet.http.*;

import mcel.tump.gateway.handler.TUMRequestHandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author db2admin
 */
public class TUMGatewayServlet extends AbstractTUMGatewayServlet {
   
   	private static final long serialVersionUID = 3177998143268186227L;
	private static final Log logger = LogFactory.getLog(TUMGatewayServlet.class);
    
    public void init() throws ServletException {
        super.init();
    	ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        requestHandler = (TUMRequestHandler)ctx.getBean("signatureVerificationHandler");
        responseHandler = (TUMRequestHandler)ctx.getBean("signatureGenerationHandler");
    }
    
    public void processResponseHeaders(HttpServletResponse response, String responseBody) {
    	return;
    }
       
}
