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

public class HibernateReserveVoucherCallback implements HibernateCallback {

	private TUMRechargeRequest request;
    private TUMRechargeResponse response;

    public HibernateReserveVoucherCallback(TUMRechargeRequest request, TUMRechargeResponse response) {
        this.request = request;
        this.response = response;
    }
    
    public Object doInHibernate(Session session) throws HibernateException, SQLException {
        Connection con = session.connection();
        CallableStatement stmt = con.prepareCall("{call TUMT_MAIN.SP_IROAMER_RESERVE(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
        stmt.setString("P_USERNAME", request.getUsername());
        stmt.setString("P_EDGE_DEALER_ID", request.getRequestDealerID());
        stmt.setString("P_EDGE_SUBDEALER_ID", request.getRequestSubDealerID());
        stmt.setTimestamp("P_EDGE_TIMESTAMP",  new java.sql.Timestamp(getRequest().getRequestTimeStamp().getTime()));
        stmt.setString("P_EDGE_TRANSACTION_ID", request.getRequestTransactionID());
        stmt.setString("P_SUBSCRIBER_MSISDN", request.getSubscriberMSISDN());
        stmt.setString("P_SENDER_MSISDN", request.getSenderMSISDN());
        stmt.setString("P_ACTIVATION_CODE", request.getVoucherActivationCode());
        stmt.registerOutParameter("P_TUM_TIMESTAMP", Types.TIMESTAMP);
        stmt.registerOutParameter("P_RESULT_CODE", Types.INTEGER);
        stmt.executeUpdate();
        Date tumTimestamp = new Date(stmt.getTimestamp("P_TUM_TIMESTAMP").getTime());
        int resultCode = stmt.getInt("P_RESULT_CODE");
        ((TUMPResponse)getResponse()).generateReserveVoucherResponse(request.getRequestTransactionID(), request.getRequestDealerID(), request.getRequestSubDealerID(), request.getSubscriberMSISDN(), tumTimestamp, resultCode);
        return response;
    }
        
    public TUMRechargeRequest getRequest() {
        return request;
    }

    public TUMRechargeResponse getResponse() {
        return response;
    }
   
}
