package mcel.tump.gateway.service;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpStatus;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.bridge.ena.xml.BadXmlFormatException;
import com.bridge.ena.xmlrpc.exception.XmlRpcConnectionException;
import com.bridge.ena.xmlrpc.serializer.MethodCall;
import com.bridge.ena.xmlrpc.serializer.MethodResponse;
import com.bridge.ena.xmlrpc.serializer.Serializer;

import mcel.tump.gateway.handler.AbstractRequestHandler;
import mcel.tump.gateway.handler.RequestHandler;
import mcel.tump.gateway.handler.TUMRequestHandlerException;
import mcel.tump.gateway.service.chain.TUMRechargeFaultResponseException;
import mcel.tump.gateway.vtu.adapter.IVTUGatewayService;

public abstract class AbstractCSGatewayServlet extends HttpServlet {

	private static final Log logger = LogFactory.getLog(AbstractCSGatewayServlet.class);
	
	protected IVTUGatewayService gateway;
	protected RequestHandler requestHandler;
	protected ApplicationContext ctx;
	
	
	public void init() throws ServletException {
		ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        requestHandler = (AbstractRequestHandler)ctx.getBean("internationalNumberHandler");
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    public abstract void processResponseHeaders(HttpServletResponse response, String responseBody);
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	ServletInputStream in = request.getInputStream();
    	response.setContentType("text/xml");
    	PrintWriter pw = response.getWriter();
    	logger.debug("Received an XML-RPC Request, now trying to parse request");
    	MethodResponse vtuResponse = new MethodResponse();
    	try {
    		MethodCall vtuRequest = parseRequest(in, request, response);
    		logger.debug(vtuRequest.toXML(new StringBuffer()));
    		logger.debug("Processing CSGateway message: ");
    		logger.debug("Executing Request Handler Chain");
    		requestHandler.processRequest(vtuRequest, vtuResponse);
    		logger.debug("Request Handler Chain executed successfully. Now processing the request");
    		vtuResponse = invokeOperation(vtuRequest);
    		logger.debug("TUMP Request has been processed. Sending back the response.");
    	} catch(BadXmlFormatException be) {
    		logger.error("Bad Xml request format..");
    		vtuResponse.generateFaultResponse(HttpStatus.SC_BAD_REQUEST, "Bad XML-RPC request message");
    	} catch(TUMRequestHandlerException rhe) {
    		logger.error("Request handler generated an error...");
    		vtuResponse.generateFaultResponse(rhe.getResponseCode(), rhe.getMessage());
    	} catch(XmlRpcConnectionException ce) {
    		logger.error("Cannot connect to server: " + ce.getMessage());
    		response.sendError(HttpStatus.SC_NOT_FOUND);
    		in.close();
    		return;
    	} catch(Exception e) {
    		logger.error("Exception in sending Response: " + e.getMessage());
    		response.sendError(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    		in.close();
    		return;
    	} 
    	String responseText = vtuResponse.toXML(new StringBuffer("<?xml version=\"1.0\"?>"));
    	processResponseHeaders(response, responseText);
    	logger.debug("Response Message Sent ToClient: " + responseText.toString());
    	pw.print(responseText);
    	response.getWriter().flush();
    	in.close();
    }
    
    private MethodCall parseRequest(ServletInputStream in, HttpServletRequest request, HttpServletResponse response) throws BadXmlFormatException {
        MethodCall vtuRequest = null;
        // get stream to read from the XML being posted
        Serializer serializer = new Serializer(in);
        vtuRequest = (MethodCall)serializer.parse();
        logger.debug("Successfully parsed incoming XML-RPC request");
        return vtuRequest;
    }
    
    protected abstract MethodResponse invokeOperation(MethodCall vtuRequest) throws XmlRpcConnectionException, TUMRequestHandlerException;
}
