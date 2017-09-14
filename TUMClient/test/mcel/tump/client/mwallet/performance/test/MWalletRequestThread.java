package mcel.tump.client.mwallet.performance.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import mcel.tump.gateway.util.TUMPRequest;
import mcel.tump.gateway.util.TUMPResponse;
import mcel.tump.util.cert.ISignatureVerifier;

import com.bridge.ena.util.DateUtils;
import com.bridge.ena.xmlrpc.IXMLRPCClient;
import com.bridge.ena.xmlrpc.exception.XmlRpcConnectionException;

public class MWalletRequestThread implements Runnable {

	private static Log log = LogFactory.getLog(MWalletRequestThread.class);
	private IXMLRPCClient xmlRpcClient;
	private ISignatureVerifier signatureVerifier = null;
	private MultithreadedRequestController controller;
	private Date requestTime;
	private Date responseTime;
	private TUMPResponse tumResponse;
	private TUMPRequest request;
	
	public MWalletRequestThread(IXMLRPCClient client, MultithreadedRequestController controller,
			ISignatureVerifier signatureVerifier) {
		this.xmlRpcClient = client;
		this.controller = controller;
		this.signatureVerifier = signatureVerifier;
	}
	
	private String SQL = "INSERT INTO TUM_TEST_RESULT (EDGE_TRANSACTION_ID, TUM_TRANSACTION_ID, SUBSCRIBER_MSISDN, TRANSFER_AMOUNT, TUM_TIMESTAMP, DEALER_BALANCE_BEFORE, DEALER_BALANCE_AFTER, SUBSCRIBER_BALANCE_BEFORE, SUBSCRIBER_BALANCE_AFTER, REQUEST_TIME, RESPONSE_TIME, RESULT_CODE)" +
			" values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	public void run() {
		request = prepareRequest();	
		String message = request.toXML();
		String response = null;
		try {
			requestTime = new Date();
			response = xmlRpcClient.sendMessage(message);
			responseTime = new Date();
			parseResponse(response);
			writeResult(tumResponse);
		} catch(XmlRpcConnectionException ce) {
			log.error("Timeout from TUM: " + ce.getMessage());
			responseTime = new Date();
			tumResponse = new TUMPResponse();
			tumResponse.generateFaultResponse(404, request.getRequestTransactionID());
		} catch(Exception e) {
			log.error(e.getMessage());
			responseTime = new Date();
			tumResponse = new TUMPResponse();
			tumResponse.generateFaultResponse(404, request.getRequestTransactionID());
		}
		
		
	}
	
	private void generateSignature(TUMPRequest request) {
    	String signatureData = generateSignatureData(request);
        byte[] signature = signatureVerifier.sign(signatureData);
        request.setSignature(signature);
    }
    
    private String generateSignatureData(TUMPRequest request) {
    	String signatureData = "";
        if(request.getUsername() != null) signatureData = signatureData + request.getUsername();
        if(request.getPassword() != null) signatureData = signatureData + request.getPassword();
        if(request.getRequestTransactionID() != null) signatureData = signatureData + request.getRequestTransactionID();
        if(request.getRequestDealerID() != null) signatureData = signatureData + request.getRequestDealerID();
        if(request.getRequestSubDealerID() != null) signatureData = signatureData + request.getRequestSubDealerID();
        if(request.getSubscriberMSISDN() != null) signatureData = signatureData + request.getSubscriberMSISDN();
        if(request.getTransferAmount() != null) signatureData = signatureData + request.getTransferAmount();
        if(request.getRequestTimeStamp() != null) signatureData = signatureData + DateUtils.convertISO8601String(request.getRequestTimeStamp());
        return signatureData;
    }
	
	private TUMPRequest prepareRequest() {
		TUMPRequest request = new TUMPRequest();
		request.generateRechargeSubscriberRequest("mwalletuser", "mwallet", String.valueOf(controller.originTransIdCounter.incrementAndGet()), "CLI00295", null, "826307248", 9, new Date());
		generateSignature(request);
		return request;
	}
	
	private void parseResponse(String response) {
		tumResponse = new TUMPResponse(response);
	}
	
	private void writeResult(TUMPResponse response) {
		try {
			DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());
			//Connect to the Tomala database
			Connection con = DriverManager.getConnection ("jdbc:oracle:thin:@(DESCRIPTION=(FAILOVER=on)(ADDRESS_LIST = (ADDRESS = (PROTOCOL = TCP)(HOST = 10.100.49.82)(PORT = 1521))(ADDRESS = (PROTOCOL = TCP)(HOST = 10.100.49.85)(PORT = 1521)))(CONNECT_DATA = (SERVICE_NAME = tumdb_racone)(FAILOVER_MODE=(TYPE=SELECT)(METHOD=BASIC))))", "tumd", "welcome1");
			//Connection con = DriverManager.getConnection ("jdbc:oracle:thin:@(DESCRIPTION=(FAILOVER=on)(ADDRESS_LIST = (ADDRESS = (PROTOCOL = TCP)(HOST = 10.100.49.91)(PORT = 1521))(ADDRESS = (PROTOCOL = TCP)(HOST = 10.100.49.92)(PORT = 1521)))(CONNECT_DATA = (SERVICE_NAME = tumdb_racone)(FAILOVER_MODE=(TYPE=SELECT)(METHOD=BASIC))))", "tumd", "welcome1");
			insertResultRow(response, con);
			con.commit();
			con.close();
		} catch(SQLException se) {
			log.error("Error in DB Connection: " + se.getMessage());
		}
	}
	
	private void insertResultRow(TUMPResponse response, Connection con) throws SQLException {
		PreparedStatement stmt = con.prepareStatement(SQL);
		if(!response.isFault()) {
			stmt.setString(1, response.getRequestTransactionID());
			stmt.setString(2, response.getTUMTransactionID());
			stmt.setString(3, response.getSubscriberMSISDN());
			stmt.setInt(4, response.getTransferAmount());
			stmt.setTimestamp(5, new Timestamp(response.getTUMTimeStamp().getTime()));
			stmt.setInt(6, response.getDealerBalanceBefore());
			stmt.setInt(7, response.getDealerBalanceAfter());
			stmt.setString(8, response.getSubscriberBalanceBefore());
			stmt.setString(9, response.getSubscriberBalanceAfter());
			stmt.setTimestamp(10, new Timestamp(requestTime.getTime()));
			stmt.setTimestamp(11, new Timestamp(responseTime.getTime()));
			stmt.setInt(12, 0);
		} else {
			stmt.setString(1, request.getRequestTransactionID());
			stmt.setNull(2, Types.VARCHAR);
			stmt.setString(3, request.getSubscriberMSISDN());
			stmt.setInt(4, request.getTransferAmount());
			stmt.setNull(5, Types.DATE);
			stmt.setNull(6, Types.INTEGER);
			stmt.setNull(7, Types.INTEGER);
			stmt.setNull(8, Types.VARCHAR);
			stmt.setNull(9, Types.VARCHAR);
			stmt.setTimestamp(10, new Timestamp(requestTime.getTime()));
			stmt.setNull(11, Types.DATE);
			stmt.setInt(12, response.getFaultCode());
		}
		stmt.executeUpdate();
		stmt.close();
	}
	
	
}
