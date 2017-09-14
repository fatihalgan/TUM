package mcel.tump.gateway.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;

import mcel.tump.gateway.util.TUMPResponse;
import mcel.tump.gateway.util.TUMRechargeRequest;
import mcel.tump.gateway.util.TUMRechargeResponse;
import mcel.tump.reservations.domain.VoucherReservation;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

public class HibernateCommitVoucherCallback implements HibernateCallback {

	private TUMRechargeRequest request;
    private TUMRechargeResponse response;

    public HibernateCommitVoucherCallback(TUMRechargeRequest request, TUMRechargeResponse response) {
        this.request = request;
        this.response = response;
    }
    
    public Object doInHibernate(Session session) throws HibernateException, SQLException {
        Connection con = session.connection();
        CallableStatement stmt = con.prepareCall("{call TUMT_MAIN.SP_IROAMER_COMMIT(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
        stmt.setString("P_USERNAME", request.getUsername());
        stmt.setString("P_EDGE_DEALER_ID", request.getRequestDealerID());
        stmt.setString("P_EDGE_SUBDEALER_ID", request.getRequestSubDealerID());
        stmt.setString("P_EDGE_TRANSACTION_ID", request.getRequestTransactionID());
        stmt.setString("P_NEW_STATUS", VoucherReservation.STATUS_COMMIT_REQUESTED);
        stmt.registerOutParameter("P_ID", Types.BIGINT);
        stmt.registerOutParameter("P_RESULT_CODE", Types.INTEGER);
        stmt.registerOutParameter("P_SUBSCRIBER_MSISDN", Types.VARCHAR);
        stmt.registerOutParameter("P_VOUCHER_SERIAL_NUMBER", Types.VARCHAR);
        stmt.registerOutParameter("P_VOUCHER_ACTIVATION_CODE", Types.VARCHAR);
        stmt.registerOutParameter("P_VOUCHER_VALUE", Types.VARCHAR);
        stmt.executeUpdate();
        Integer resultCode = stmt.getInt("P_RESULT_CODE");
        Long tumTransactionId = stmt.getLong("P_ID");
        String subscriberMsisdn = stmt.getString("P_SUBSCRIBER_MSISDN");
        String voucherSerialNumber = stmt.getString("P_VOUCHER_SERIAL_NUMBER");
        String voucherActivationCode = stmt.getString("P_VOUCHER_ACTIVATION_CODE");
        String voucherValue = stmt.getString("P_VOUCHER_VALUE");
        ((TUMPResponse)getResponse()).generateCommitVoucherResponse(tumTransactionId.toString(), request.getRequestTransactionID(), request.getRequestDealerID(), request.getRequestSubDealerID(), subscriberMsisdn, voucherSerialNumber, voucherActivationCode, voucherValue, new Date(), resultCode);
        return response;    
    }
    
    public TUMRechargeRequest getRequest() {
        return request;
    }

    public TUMRechargeResponse getResponse() {
        return response;
    }
   
}
