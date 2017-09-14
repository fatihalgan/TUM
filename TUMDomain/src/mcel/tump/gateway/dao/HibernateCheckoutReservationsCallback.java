package mcel.tump.gateway.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import mcel.tump.gateway.util.TUMPRequest;
import mcel.tump.gateway.util.TUMPResponse;
import mcel.tump.reservations.domain.Reservation;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

public class HibernateCheckoutReservationsCallback implements HibernateCallback {

	public HibernateCheckoutReservationsCallback() {
        super();
    }
    
    public Object doInHibernate(Session session) throws HibernateException, SQLException {
    	Connection con = session.connection();
    	CallableStatement stmt = con.prepareCall("{call TUMT_MAIN.sp_ChangeResStatus_Time(?, ?, ?, ?)}");
    	stmt.setString("P_OLD_STATUS", Reservation.STATUS_RESERVED);
    	stmt.setString("P_NEW_STATUS", Reservation.STATUS_COMMITTING);
    	stmt.setInt("P_INTERVAL_IN_SECONDS", 300);
    	stmt.registerOutParameter("P_RESULT", Types.INTEGER);
    	stmt.executeUpdate();
    	Integer result = stmt.getInt("P_RESULT");
    	if(result != 0) throw new HibernateException("Call to TUMT_MAIN.sp_ChangeReservationStatus returned with error code: " + result);
    	return result;
    }
    
}
