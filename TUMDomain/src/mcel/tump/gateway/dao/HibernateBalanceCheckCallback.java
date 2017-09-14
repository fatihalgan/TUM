/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.gateway.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import mcel.tump.gateway.util.TUMPRequest;
import mcel.tump.gateway.util.TUMPResponse;
import oracle.jdbc.OracleTypes;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

/**
 *
 * @author db2admin
 */
public class HibernateBalanceCheckCallback implements HibernateCallback{
    
    private TUMPRequest request;
    private TUMPResponse response;
    
    public HibernateBalanceCheckCallback(TUMPRequest request, TUMPResponse response) {
        this.request = request;
        this.response = response;
    }

    public Object doInHibernate(Session session) throws HibernateException, SQLException {
        Connection con = session.connection();
        CallableStatement stmt = con.prepareCall("{call TUMT_MAIN.sp_BalanceCheck(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
        stmt.setString("P_USERNAME", request.getUsername());
        stmt.setString("P_EDGE_DEALER_ID", request.getRequestDealerID());
        stmt.setString("P_EDGE_TRANSACTION_ID", request.getRequestTransactionID());
        stmt.setTimestamp("P_EDGE_TIMESTAMP", new java.sql.Timestamp(request.getRequestTimeStamp().getTime()));
        stmt.registerOutParameter("P_TUM_BALANCE", Types.BIGINT);
        stmt.registerOutParameter("P_TUM_TIMESTAMP", Types.TIMESTAMP);
        stmt.registerOutParameter("P_TUM_TRANSACTION_ID", Types.BIGINT);
        stmt.registerOutParameter("P_TUM_DB_RESULT", Types.INTEGER);
        stmt.registerOutParameter("P_PENDING_COUNT", Types.INTEGER);
        stmt.registerOutParameter("P_PENDING_REQUESTS", OracleTypes.CURSOR);
        stmt.executeUpdate();                
        Long balance = stmt.getLong("P_TUM_BALANCE");
        Date timestamp = new Date(stmt.getTimestamp("P_TUM_TIMESTAMP").getTime());
        Long tumTransactionId = stmt.getLong("P_TUM_TRANSACTION_ID");
        Integer result = stmt.getInt("P_TUM_DB_RESULT");
        response.generateBalanceCheckResponse(tumTransactionId.toString(), 
            request.getRequestTransactionID(), request.getRequestDealerID(), balance.intValue(), 
            timestamp, result);
        return response;
    }

    public TUMPRequest getRequest() {
        return request;
    }

    public TUMPResponse getResponse() {
        return response;
    }
    
    
    
}
