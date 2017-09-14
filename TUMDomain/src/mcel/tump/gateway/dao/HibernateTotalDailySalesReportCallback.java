/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.gateway.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import mcel.tump.gateway.util.TUMPRequest;
import mcel.tump.gateway.util.TUMPResponse;
import mcel.tump.gateway.util.UserSalesRecord;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

/**
 *
 * @author db2admin
 */
public class HibernateTotalDailySalesReportCallback implements HibernateCallback {
    
    private TUMPRequest request;
    private TUMPResponse response;
    
    public HibernateTotalDailySalesReportCallback(TUMPRequest request, TUMPResponse response) {
        this.request = request;
        this.response = response;
    }

    public Object doInHibernate(Session session) throws HibernateException, SQLException {
        Connection con = session.connection();
        PreparedStatement pstmt = con.prepareStatement("select SUM(TRANS_AMOUNT), USERNAME, EDGE_DEALER_ID from tum_trans_tbl " +
            "where TRUNC(TUM_TIMESTAMP) = ? and EDGE_DEALER_ID = (select EDGE_DEALER_ID from TUM_DEALERS_TBL t1, " +
            "TUM_USER_TBL t2 where t1.TUM_DEALER_ID = t2.DEALER_ID and t2.USERNAME = ?) group by USERNAME, EDGE_DEALER_ID");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        pstmt.setTimestamp(1, new Timestamp(cal.getTimeInMillis()));
        pstmt.setString(2, request.getUsername());
        ResultSet rs = pstmt.executeQuery();
        List<UserSalesRecord> sales = new ArrayList<UserSalesRecord>();
        while(rs.next()) {
            Integer amount = rs.getInt(1);
            String username = rs.getString(2);
            String edgeDealerId = rs.getString(3);
            Date date = new Date(cal.getTimeInMillis());
            UserSalesRecord rec = new UserSalesRecord();
            rec.setAmount(amount);
            rec.setDate(date);
            rec.setUsername(username);
            rec.setEdgeDealerId(edgeDealerId);
            sales.add(rec);
        }
        response.generateTotalDailySalesReportResponse(request.getRequestTransactionID(), sales);
        return response;
    }

    public TUMPRequest getRequest() {
        return request;
    }

    public TUMPResponse getResponse() {
        return response;
    }
    
}
