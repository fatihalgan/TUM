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

public class HibernateReserveRechargeSubscriberCallback implements HibernateCallback {

	private TUMRechargeRequest request;
    private TUMRechargeResponse response;

    public HibernateReserveRechargeSubscriberCallback(TUMRechargeRequest request, TUMRechargeResponse response) {
        this.request = request;
        this.response = response;
    }
    
    public Object doInHibernate(Session session) throws HibernateException, SQLException {
        Connection con = session.connection();
        CallableStatement stmt = con.prepareCall("{call TUMT_MAIN.sp_ReserveRechargeSubscriber(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
        stmt.setString("P_USERNAME", request.getUsername());
        stmt.setString("P_EDGE_TRANSACTION_ID", request.getRequestTransactionID());
        stmt.setString("P_EDGE_DEALER_ID", request.getRequestDealerID());
        stmt.setString("P_EDGE_SUBDEALER_ID", request.getRequestSubDealerID());
        stmt.setTimestamp("P_EDGE_TIMESTAMP", new java.sql.Timestamp(request.getRequestTimeStamp().getTime()));
        stmt.setString("P_SUBSCRIBER_MSISDN", request.getSubscriberMSISDN());
        stmt.setInt("P_TRANS_AMOUNT", request.getTransferAmount());
        stmt.setString("P_SENDER_MSISDN", request.getSenderMSISDN());
        stmt.registerOutParameter("P_RESULT_CODE", Types.INTEGER);
        stmt.registerOutParameter("P_ALERT", Types.INTEGER);
        stmt.executeUpdate();
        Integer resultCode = stmt.getInt("P_RESULT_CODE");
        Integer alertCode = stmt.getInt("P_ALERT");
        ((TUMPResponse) getResponse()).generateReserveRechargeSubscriberResponse(request.getRequestTransactionID(), request.getRequestDealerID(), request.getRequestSubDealerID(), request.getSubscriberMSISDN(), request.getTransferAmount(), new Date(), resultCode);
        return response;
    }
    
    public TUMRechargeRequest getRequest() {
        return request;
    }

    public TUMRechargeResponse getResponse() {
        return response;
    }

}
