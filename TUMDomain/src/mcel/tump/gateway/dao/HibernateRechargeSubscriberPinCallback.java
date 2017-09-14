package mcel.tump.gateway.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;

import mcel.tump.gateway.util.TUMPResponse;
import mcel.tump.gateway.util.TUMRechargeRequest;
import mcel.tump.gateway.util.TUMRechargeResponse;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

public class HibernateRechargeSubscriberPinCallback implements HibernateCallback {

	private TUMRechargeRequest request;
    private TUMRechargeResponse response;
    
    public HibernateRechargeSubscriberPinCallback(TUMRechargeRequest request, TUMRechargeResponse response) {
    	this.request = request;
    	this.response = response;
    }
    
    public Object doInHibernate(Session session) throws HibernateException, SQLException {
    	Connection con = session.connection();
        CallableStatement stmt = con.prepareCall("{call TUMT_MAIN.sp_rechargeSubscriberpin(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
        stmt.setTimestamp("P_TRANS_TIME", new java.sql.Timestamp(getRequest().getRequestTimeStamp().getTime()));
        stmt.setLong("P_TRANS_AMOUNT", 0);
        stmt.setString("P_SUBSCRIBER_MSISDN", getRequest().getSubscriberMSISDN());
        stmt.setString("P_USERNAME", getRequest().getUsername());
        stmt.setString("P_EDGE_DEALER_ID", getRequest().getRequestDealerID());
        stmt.setString("P_EDGE_SUBDEALER_ID", getRequest().getRequestSubDealerID());
        stmt.setString("P_EDGE_TRANSACTION_ID", getRequest().getRequestTransactionID());
        stmt.setTimestamp("P_EDGE_TIMESTAMP", new java.sql.Timestamp(getRequest().getRequestTimeStamp().getTime()));
        stmt.setString("P_ACTIVATION_CODE", getRequest().getVoucherActivationCode());
        stmt.setString("P_SERIAL_NUMBER", getRequest().getVoucherSerialNumber());
        stmt.setString("P_SIGNATURE", getRequest().getSignature());
        //Set two more input params, Voucher Serial No and Voucher Activation Code
        stmt.registerOutParameter("P_ID", Types.BIGINT);
        stmt.registerOutParameter("P_RESULT_CODE", Types.INTEGER);
        stmt.registerOutParameter("P_ALERT", Types.INTEGER);
        stmt.executeUpdate();
        Long tumTransactionId = stmt.getLong("P_ID");
        Integer resultCode = stmt.getInt("P_RESULT_CODE");
        Integer alertCode = stmt.getInt("P_ALERT");
        ((TUMPResponse) getResponse()).generateRechargeSubscriberPinResponse(tumTransactionId.toString(), getRequest().getRequestTransactionID(),
        		getRequest().getRequestDealerID(), getRequest().getRequestSubDealerID(),
                getRequest().getSubscriberMSISDN(), getRequest().getVoucherSerialNumber(), new Date(), resultCode, alertCode);
        return response;
    }

	public TUMRechargeRequest getRequest() {
		return request;
	}

	public TUMRechargeResponse getResponse() {
		return response;
	}
    
}
