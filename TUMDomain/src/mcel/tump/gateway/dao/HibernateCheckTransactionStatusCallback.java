package mcel.tump.gateway.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import mcel.tump.gateway.util.TUMPRequest;
import mcel.tump.gateway.util.TUMPResponse;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

public class HibernateCheckTransactionStatusCallback implements HibernateCallback {

	private static final String CHECK_STATUS_SQL = "select ID, TUM_TIMESTAMP, TRANS_AMOUNT, EDGE_TRANSACTION_ID, EDGE_TIMESTAMP from TUM_TRANS_TBL where EDGE_TRANSACTION_ID = ?";
	
	private TUMPRequest request;
    private TUMPResponse response;
    
    public HibernateCheckTransactionStatusCallback(TUMPRequest request, TUMPResponse response) {
    	this.request = request;
    	this.response = response; 
    }

	@Override
	public Object doInHibernate(Session session) throws HibernateException, SQLException {
		Connection con = session.connection();
		PreparedStatement pstmt = con.prepareStatement(CHECK_STATUS_SQL);
		pstmt.setLong(1, Long.parseLong(request.getRequestTransactionID()));
		ResultSet rs = pstmt.executeQuery();
		boolean status = false;
		if(rs.next()) status = true;
		response.generateCheckTransactionStatusResponse(request.getRequestTransactionID(), new Date(), status, 0);
		return response;
	}
	
	public TUMPRequest getRequest() {
        return request;
    }

    public TUMPResponse getResponse() {
        return response;
    }
}
