package mcel.tump.gateway.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import mcel.tump.gateway.util.TUMGWTokens;
import mcel.tump.gateway.util.TUMRechargeRequest;
import mcel.tump.gateway.util.TUMRechargeResponse;
import mcel.tump.reservations.domain.VoucherReservation;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

public class HibernateWriteVoucherFinalLogCallback implements HibernateCallback {

	private TUMRechargeRequest request;
    private TUMRechargeResponse response;

    public HibernateWriteVoucherFinalLogCallback(TUMRechargeRequest request, TUMRechargeResponse response) {
        this.request = request;
        this.response = response;
    }
    
    public Object doInHibernate(Session session) throws HibernateException, SQLException {
    	Connection con = session.connection();
    	CallableStatement stmt = con.prepareCall("{call TUMT_MAIN.SP_IROAMER_FINAL_LOG(?, ?, ?, ?, ?)}");
    	stmt.setString("P_EDGE_TRANSACTION_ID", request.getRequestTransactionID());
    	String status = null;
    	if(request.getMethodName().equals(TUMGWTokens.ReserveVoucherRequest.toString())) {
    		if(response.isFault()) status = VoucherReservation.STATUS_RESERVE_ERROR;
    		else status = VoucherReservation.STATUS_RESERVED;
    	} else if(request.getMethodName().equals(TUMGWTokens.CommitVoucherRequest.toString())) {
    		if(response.isFault()) status = VoucherReservation.STATUS_COMMIT_ERROR;
    		else status = VoucherReservation.STATUS_COMMITTED;
    	} else if(request.getMethodName().equals(TUMGWTokens.CancelVoucherRequest.toString())) {
    		if(response.isFault()) status = VoucherReservation.STATUS_CANCEL_ERROR;
    		else status = VoucherReservation.STATUS_CANCELLED;
    	}
    	stmt.setString("P_NEW_STATUS", status);
    	if(request.getMethodName().equals(TUMGWTokens.ReserveVoucherRequest.toString())) {
    		stmt.setString("P_VOUCHER_SERIAL_NUMBER", response.getVoucherSerialNumber());
    		stmt.setString("P_VOUCHER_VALUE", response.getVoucherValue());
    	} else {
    		stmt.setNull("P_VOUCHER_SERIAL_NUMBER", Types.VARCHAR);
    		stmt.setNull("P_VOUCHER_VALUE", Types.VARCHAR);
    	}
    	stmt.registerOutParameter("P_RESULT_CODE", Types.INTEGER);
    	stmt.executeUpdate();
    	return response;
    }
    
    public TUMRechargeRequest getRequest() {
        return request;
    }

    public TUMRechargeResponse getResponse() {
        return response;
    }
   
}
