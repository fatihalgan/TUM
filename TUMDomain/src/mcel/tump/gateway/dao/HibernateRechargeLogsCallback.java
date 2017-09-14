/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.gateway.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import mcel.tump.gateway.util.RechargeLogRecord;
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
public class HibernateRechargeLogsCallback implements HibernateCallback {
    
    private TUMPRequest request;
    private TUMPResponse response;
    
    public HibernateRechargeLogsCallback(TUMPRequest request, TUMPResponse response) {
        this.request = request;
        this.response = response;
    }

    public Object doInHibernate(Session session) throws HibernateException, SQLException {
        Connection con = session.connection();
        CallableStatement stmt = con.prepareCall("{call TUMT_MAIN.sp_GetLast5Today(?, ?, ?)}");
        stmt.setString("P_USERNAME", request.getUsername());
        stmt.registerOutParameter("P_DBRESULTCODE", Types.INTEGER);
        stmt.registerOutParameter("P_LAST5_CURSOR", OracleTypes.CURSOR);
        stmt.executeUpdate();                
        Integer result = stmt.getInt("P_DBRESULTCODE");
        ResultSet rs = (ResultSet)stmt.getObject("P_LAST5_CURSOR");
        List<RechargeLogRecord> logs = new ArrayList<RechargeLogRecord>();
        while(rs.next()) {
            RechargeLogRecord rec = new RechargeLogRecord();
            rec.setAmount(rs.getInt("TRANS_AMOUNT"));
            rec.setSubscriberBalanceBefore(String.valueOf(rs.getInt("BEFORE_BALANCE")));
            rec.setSubscriberBalanceAfter(String.valueOf(rs.getInt("AFTER_BALANCE")));
            rec.setSubscriberMSISDN(rs.getString("SUBSCRIBER_MSISDN"));
            rec.setTumDealerId(String.valueOf(rs.getString("TUM_DEALER_ID")));
            rec.setTumTimestamp(new Date(rs.getTimestamp("TUM_TIMESTAMP").getTime()));
            logs.add(rec);
        }
        response.generateRechargeLogResponse(request.getRequestTransactionID(), logs, result); 
        return response;
    }

    public TUMPRequest getRequest() {
        return request;
    }

    public TUMPResponse getResponse() {
        return response;
    }
    
}
