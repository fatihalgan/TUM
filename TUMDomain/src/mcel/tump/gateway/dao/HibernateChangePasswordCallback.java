/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.gateway.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import mcel.tump.gateway.util.TUMPRequest;
import mcel.tump.gateway.util.TUMPResponse;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

/**
 *
 * @author db2admin
 */
public class HibernateChangePasswordCallback implements HibernateCallback {
    
    private TUMPRequest request;
    private TUMPResponse response;
    
    public HibernateChangePasswordCallback(TUMPRequest request, TUMPResponse response) {
        this.request = request;
        this.response = response;
    }

    public Object doInHibernate(Session session) throws HibernateException, SQLException {
        Connection con = session.connection();
        CallableStatement stmt = con.prepareCall("{call TUMT_MAIN.sp_ChangePassword1(?, ?, ?, ?)}");
        stmt.setString("PUSERNAME", request.getUsername());
        stmt.setString("PREQTRANSACTIONID", request.getRequestTransactionID());
        //stmt.setTimestamp("P_REQ_TIMESTAMP", new Timestamp(requestTimestamp.getTime()));
        stmt.registerOutParameter("PDBRESULTCODE", Types.INTEGER);
        stmt.registerOutParameter("PTUMTRANSACTIONID", Types.BIGINT);
        stmt.executeUpdate();
        Long tumTransactionId = stmt.getLong("PTUMTRANSACTIONID");
        Integer dbResult = stmt.getInt("PDBRESULTCODE");
        response.generateChangePasswordResponse(String.valueOf(tumTransactionId), dbResult);
        return response;
    }

    public TUMPRequest getRequest() {
        return request;
    }

    public TUMPResponse getResponse() {
        return response;
    }
    
}
