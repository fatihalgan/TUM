/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.gateway.service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author db2admin
 */
public class TUMMobileGatewayServlet extends AbstractTUMGatewayServlet {

	private static final Log logger = LogFactory.getLog(TUMMobileGatewayServlet.class);
    
    public void init() throws ServletException {
        super.init();
        ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
    	gateway = (ITUMPGatewayService)ctx.getBean("tumpGatewayService");
    }
    
    public void processResponseHeaders(HttpServletResponse response, String responseBody) {
    	return;
    }
}
