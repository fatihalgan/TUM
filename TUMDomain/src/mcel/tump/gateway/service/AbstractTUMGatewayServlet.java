package mcel.tump.gateway.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mcel.tump.gateway.handler.TUMRequestHandler;
import mcel.tump.gateway.handler.TUMRequestHandlerException;
import mcel.tump.gateway.service.chain.TUMRechargeFaultResponseException;
import mcel.tump.gateway.util.TUMGWTokens;
import mcel.tump.gateway.util.TUMPRequest;
import mcel.tump.gateway.util.TUMPResponse;
import mcel.tump.gateway.util.TUMResponseCodes;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpStatus;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.bridge.ena.xml.BadXmlFormatException;
import com.bridge.ena.xmlrpc.serializer.MethodCall;
import com.bridge.ena.xmlrpc.serializer.Serializer;

public abstract class AbstractTUMGatewayServlet extends HttpServlet {

	private static final long serialVersionUID = -569472002578585240L;

	private static final Log logger = LogFactory.getLog(AbstractTUMGatewayServlet.class);
	
	protected TUMRequestHandler requestHandler;
    protected TUMRequestHandler responseHandler;
    protected ITUMPGatewayService gateway;
    protected IVoucherGatewayService vsgateway;
    protected ApplicationContext ctx;
    
    
    public void init() throws ServletException {
        ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        gateway = (ITUMPGatewayService)ctx.getBean("tumpGatewayService");
        vsgateway = (IVoucherGatewayService)ctx.getBean("voucherGatewayService");
    }
    
    public abstract void processResponseHeaders(HttpServletResponse response, String responseBody);
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	ServletInputStream in = request.getInputStream();
        response.setContentType("text/xml");
        logger.info("Received an XML-RPC Request, now trying to parse request");
        TUMPRequest tumpRequest = null;
        TUMPResponse tumpResponse = new TUMPResponse();
        try {
        	tumpRequest = parseRequest(in, request, response);
        	logger.info("Processing TUMPGateway message");
            if(requestHandler != null) {
            	logger.info("Executing Request Handler Chain");
            	requestHandler.processRequest(tumpRequest, tumpResponse);
            	logger.info("Request Handler Chain executed successfully. Now will process the request");
            } else {
            	logger.info("Request Handler is null. Will skip execution of Request Handler Chain..");
            }
            invokeOperation(tumpRequest, tumpResponse);
            logger.info("TUMP Request has been processed. Sending back the response.");
        } catch(BadXmlFormatException be) {
        	response.sendError(HttpStatus.SC_BAD_REQUEST);
        	response.getWriter().flush();
            in.close();
            return;
        } catch(TUMRechargeFaultResponseException e) {
            //No need to do anything because the error details are already in TUMResponse...
            logger.error("Fault Response from recharge operation.. Rolled back the recharge operation...");
        } catch(TUMRequestHandlerException e) {
            logger.error("TUM Request Handler Error: " + e.getMessage());
            tumpResponse.generateFaultResponse(e.getResponseCode(), e.getRequestTransactionId());
        } catch(Exception e) {
            e.printStackTrace();
            tumpResponse.generateFaultResponse(TUMResponseCodes.SYSTEM_ERROR.getResponseCode(), tumpRequest.getRequestTransactionID());
        }
        try {
            if(responseHandler != null) {
            	logger.info("Executing Response Handler Chain");
            	responseHandler.processRequest(tumpRequest, tumpResponse);
            } else {
            	logger.info("Response Handler is null. Will skip execution of Request Handler Chain..");
            }
        } catch(TUMRequestHandlerException e) {
            tumpResponse.generateFaultResponse(e.getResponseCode(), e.getRequestTransactionId());
        } catch(Exception e) {
            tumpResponse.generateFaultResponse(TUMResponseCodes.SYSTEM_ERROR.getResponseCode(), tumpRequest.getRequestTransactionID());
        }
        logger.debug("TUMPGateway Response message is: " + tumpResponse.toXML());
        String responseText = tumpResponse.toXML(new StringBuffer("<?xml version=\"1.0\"?>"));
        processResponseHeaders(response, responseText);
        response.getWriter().print(responseText);
        response.getWriter().flush();
        in.close();
    }
    
    private TUMPRequest parseRequest(ServletInputStream in, HttpServletRequest request, HttpServletResponse response) throws BadXmlFormatException {
    	TUMPRequest tumpRequest = null;
    	try {
        	// get stream to read from the XML being posted
        	Serializer serializer = new Serializer(in);
        	tumpRequest = new TUMPRequest((MethodCall)serializer.parse());
        	logger.debug("Successfully parsed incoming XML-RPC request");
        	logger.debug(tumpRequest.toXML());
    	} catch(Throwable e) {
    		throw new BadXmlFormatException();
    	}
        return tumpRequest;
    }
    
    protected void invokeOperation(TUMPRequest tumRequest, TUMPResponse tumResponse) {
        String opName = tumRequest.getMethodName();
        logger.debug("Requested Operation is: " + opName);
        if(opName.equals(TUMGWTokens.BalanceCheckRequest.toString()))
            gateway.balanceCheck(tumRequest, tumResponse);
        else if(opName.equals(TUMGWTokens.RechargeSubscriberRequest.toString()))
            gateway.rechargeSubscriber(tumRequest, tumResponse);
        else if(opName.equals(TUMGWTokens.ReserveRechargeSubscriberRequest.toString()))
        	gateway.reserveRechargeSubscriber(tumRequest, tumResponse);
        else if(opName.equals(TUMGWTokens.CommitReservationRequest.toString())) 
        	gateway.commitReserveRechargeSubscriber(tumRequest, tumResponse);
        else if(opName.equals(TUMGWTokens.RechargeSubscriberPinRequest.toString()))        	
        	gateway.rechargeSubscriberPin(tumRequest, tumResponse);
        else if(opName.equals(TUMGWTokens.RechargeSubscriberSMSRequest.toString()))
        	gateway.rechargeSubscriberSMS(tumRequest, tumResponse);
        else if(opName.equals(TUMGWTokens.AdjustSubscriberAccountRequest.toString())) 
        	gateway.adjustSubscriber(tumRequest, tumResponse);
        else if(opName.equals(TUMGWTokens.CheckSubscriberValidRequest.toString())) 
        	gateway.checkSubscriberValid(tumRequest, tumResponse);
        else if(opName.equals(TUMGWTokens.CheckTransactionStatusRequest.toString()))
        	gateway.checkTransactionStatus(tumRequest, tumResponse);
        else if(opName.equals(TUMGWTokens.ChangePasswordRequest.toString()))
            gateway.changePassword(tumRequest, tumResponse);        
        else if(opName.equals(TUMGWTokens.RechargeLogsRequest.toString()))
            gateway.rechargeLogs(tumRequest, tumResponse);
        else if(opName.equals(TUMGWTokens.TotalDeailySalesReportRequest.toString()))
            gateway.dailySalesReport(tumRequest, tumResponse);
        else if(opName.equals(TUMGWTokens.CancelReservationRequest.toString()))
        	gateway.cancelReserveRechargeSubscriber(tumRequest, tumResponse);
        else if(opName.equals(TUMGWTokens.ReserveVoucherRequest.toString())) 
        	vsgateway.reserveVoucher(tumRequest, tumResponse);
        else if(opName.equals(TUMGWTokens.CancelVoucherRequest.toString())) 
        	vsgateway.cancelReserveVoucher(tumRequest, tumResponse);
        else if(opName.equals(TUMGWTokens.CommitVoucherRequest.toString()))
        	vsgateway.commitReserveVoucher(tumRequest, tumResponse);
        
        logger.debug("TUM Transaction has complated for Transaction ID: " + tumRequest.getRequestTransactionID());
    }
}
