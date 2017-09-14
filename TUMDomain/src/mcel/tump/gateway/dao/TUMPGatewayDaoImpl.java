/*
 * TUMPGatewayDaoImpl.java
 * 
 * Created on Aug 20, 2007, 11:46:53 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.gateway.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import mcel.tump.gateway.exception.TUMApplicationException;
import mcel.tump.gateway.service.chain.TUMFinalLogDaoProcessor;
import mcel.tump.gateway.util.TUMGWTokens;
import mcel.tump.gateway.util.TUMRechargeRequest;
import mcel.tump.gateway.util.TUMRechargeResponse;
import mcel.tump.gateway.util.TUMPRequest;
import mcel.tump.gateway.util.TUMPResponse;
import mcel.tump.gateway.util.TUMResponseCodes;
import mcel.tump.reservations.domain.Reservation;
import mcel.tump.reservations.domain.VoucherReservation;
import mcel.tump.util.persistence.BaseDao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.exception.LockAcquisitionException;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.bridge.ena.vs.command.VoucherState;

/**
 *
 * @author db2admin
 */
public class TUMPGatewayDaoImpl extends BaseDao implements ITUMPGatewayDao {

	private static final Log logger = LogFactory.getLog(TUMPGatewayDaoImpl.class);
	
    public void balanceCheck(TUMPRequest request, TUMPResponse response) {
        HibernateBalanceCheckCallback cb = new HibernateBalanceCheckCallback(request, response);    
        response = (TUMPResponse)getHibernateTemplate().execute(cb);
        request = cb.getRequest();
    }
    
    public void rechargeSubscriber(TUMRechargeRequest request, TUMRechargeResponse response) {
        HibernateRechargeSubscriberCallback cb = new HibernateRechargeSubscriberCallback(request, response);    
        response = (TUMRechargeResponse)getHibernateTemplate().execute(cb);
        request = cb.getRequest();
    }
    
    public void rechargeSubscriberPinned(TUMRechargeRequest request, TUMRechargeResponse response) {
    	HibernateRechargeSubscriberPinCallback cb = new HibernateRechargeSubscriberPinCallback(request, response);
    	response = (TUMRechargeResponse)getHibernateTemplate().execute(cb);
    	request = cb.getRequest();
    }

    public void writeFinalLog(final Long tumTransactionId, final Integer csResult, final Date csTimestamp, final String balanceBefore, final String balanceAfter,
        final Integer serviceClassOld, final Float transactionAmount, final Float refillPortion, final Float adjustmentPortion, final String voucherProfileID,
        final String voucherProfileName, final Date newServiceClassExpiryDate, final Integer serviceClassNew, final String subscriberMSISDN,
        final Integer updateServiceClassFlag, final Integer refillFlag, final Integer adjustmentFlag, final Integer balanceEnquiryFlag) {
        getHibernateTemplate().execute(
                new HibernateCallback() {
                    
                    public Object doInHibernate(Session session) throws HibernateException, SQLException {
                    	logger.debug("Retrieving connection for writeFinalLog for tum transaction id " + tumTransactionId.toString());
                        Connection con = session.connection();
                        logger.debug("Retrieved connection for writeFinalLog for tum transaction id " + tumTransactionId.toString());
                        CallableStatement stmt = con.prepareCall("{call TUMT_MAIN.sp_FinalCSResult(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
                        stmt.setLong("P_ID", tumTransactionId);
                        stmt.setInt("P_CS_RESULT", csResult);
                        stmt.setTimestamp("P_CS_TIMESTAMP", new java.sql.Timestamp(csTimestamp.getTime()));
                        if(balanceBefore != null) stmt.setFloat("P_BEFORE_BALANCE", Float.parseFloat(balanceBefore));
                        else stmt.setNull("P_BEFORE_BALANCE", Types.FLOAT);
                        if(balanceAfter != null) stmt.setFloat("P_AFTER_BALANCE", Float.parseFloat(balanceAfter));
                        else stmt.setNull("P_AFTER_BALANCE", Types.FLOAT);
                        //service class old
                        if(serviceClassOld != null) stmt.setInt("P_SC_OLD", serviceClassOld);
                        else stmt.setNull("P_SC_OLD", Types.INTEGER);
                        //transactionAmountRefill
                        stmt.setFloat("P_TR_RFL_AMO", transactionAmount);
                        //Refill portion
                        if(refillPortion != null) stmt.setFloat("P_RFL_AMO", refillPortion);
                        else stmt.setNull("P_RFL_AMO", Types.FLOAT);
                        //AdjustmentPortion
                        if(adjustmentPortion != null) stmt.setFloat("P_ADJ_AMO", adjustmentPortion);
                        else stmt.setNull("P_ADJ_AMO", Types.FLOAT);
                        //Voucher Profile ID
                        if(voucherProfileID != null) stmt.setString("P_PROFILE_ID", voucherProfileID);
                        else stmt.setNull("P_PROFILE_ID", Types.VARCHAR);
                        //Vocuher Profile Name
                        if(voucherProfileName != null) stmt.setString("P_PROFILE_NAME", voucherProfileName);
                        else stmt.setNull("P_PROFILE_NAME", Types.VARCHAR);
                        //New Service Class Expiry Date
                        if(newServiceClassExpiryDate != null) stmt.setTimestamp("P_SC_NEW_EXP_DATE", new Timestamp(newServiceClassExpiryDate.getTime()));
                        else stmt.setNull("P_SC_NEW_EXP_DATE", Types.TIMESTAMP);
                        //Service Class New
                        if(serviceClassNew != null) stmt.setInt("P_SC_NEW", serviceClassNew);
                        else stmt.setNull("P_SC_NEW", Types.INTEGER);
                        //Subscriber MSISDN
                        stmt.setString("P_SUBSCRIBER_MSISDN", subscriberMSISDN);
                        //Update Service Class Flag
                        if(updateServiceClassFlag != null) stmt.setInt("P_FLG_UPD_SC", updateServiceClassFlag);
                        else stmt.setNull("P_FLG_UPD_SC", Types.INTEGER);
                        //Refill Flag
                        stmt.setInt("P_FLG_RFL", refillFlag);
                        //Adjustment Flag
                        stmt.setInt("P_FLG_ADJ", adjustmentFlag);
                        //BalanceEnquiryFlag
                        if(balanceEnquiryFlag != null) stmt.setInt("P_FLG_BE", balanceEnquiryFlag);
                        else stmt.setNull("P_FLG_BE", Types.INTEGER);
                        logger.debug("Will do executeUpdate for writeFinalLog for tum transaction id " + tumTransactionId.toString());
                        stmt.executeUpdate();
                        logger.debug("Retrieved result of executeUpdate for writeFinalLog for tum transaction id " + tumTransactionId.toString());
                        return null;
                    }
        });
    }

    
    
    public void getRechargeLogs(TUMPRequest request, TUMPResponse response) {
        HibernateRechargeLogsCallback cb = new HibernateRechargeLogsCallback(request, response);    
        response = (TUMPResponse)getHibernateTemplate().execute(cb);
        request = cb.getRequest();
    }

    public void changePassword(TUMPRequest request, TUMPResponse response) {
        HibernateChangePasswordCallback cb = new HibernateChangePasswordCallback(request, response);    
        response = (TUMPResponse)getHibernateTemplate().execute(cb);
        request = cb.getRequest();
    }

    public void getTotalDailySalesReport(TUMPRequest request, TUMPResponse response) {
        HibernateTotalDailySalesReportCallback cb = new HibernateTotalDailySalesReportCallback(request, response);    
        response = (TUMPResponse)getHibernateTemplate().execute(cb);
        request = cb.getRequest();
    }

	@Override
	public void checkTransactionStatus(TUMPRequest request, TUMPResponse response) {
		HibernateCheckTransactionStatusCallback cb = new HibernateCheckTransactionStatusCallback(request, response);
		response = (TUMPResponse)getHibernateTemplate().execute(cb);
		request = cb.getRequest();
		
	}
	
	private Long getNextTUMTransactionID() {
        return (Long)getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Long returnVal = null;
                Connection con = session.connection();
                PreparedStatement pstmt = con.prepareStatement("select seq_recharge_sub.nextval from dual");
                ResultSet rs = pstmt.executeQuery();
                while(rs.next()) {
                    returnVal = rs.getLong(1);
                }
                rs.close();
                pstmt.close();
                return returnVal;
            }
        });
    }


	@Override
	public void reserveRechargeSubscriber(TUMRechargeRequest request, TUMRechargeResponse response) {
		 HibernateReserveRechargeSubscriberCallback cb = new HibernateReserveRechargeSubscriberCallback(request, response);    
	     response = (TUMPResponse)getHibernateTemplate().execute(cb);
	     request = cb.getRequest();
	}
	
	public Reservation lockRechargeReservation(String requestTransactionId, String newStatus) {
		Reservation reservation = null;
    	try {
    		reservation = checkoutReservationById(requestTransactionId);
    		if(!reservation.getStatus().equals(Reservation.STATUS_RESERVED)) {
    			if(reservation.getStatus().equals(Reservation.STATUS_COMMITTED))
    				throw new TUMApplicationException(requestTransactionId, TUMResponseCodes.RESERVATION_ALREADY_COMMITTED.getResponseCode());
    			else if(reservation.getStatus().equals(Reservation.STATUS_CANCELLED)) {
    				throw new TUMApplicationException(requestTransactionId, TUMResponseCodes.RESERVATION_ALREADY_CANCELLED.getResponseCode());
    			} else {
    				throw new TUMApplicationException(requestTransactionId, TUMResponseCodes.RESERVATION_LOCKED.getResponseCode());
    			}
    		}
    		reservation.setStatus(newStatus);
    		update(reservation);
    		flush();
    		return reservation;
    	} catch(CannotAcquireLockException ce) {
    		throw new TUMApplicationException(requestTransactionId, TUMResponseCodes.RESERVATION_LOCKED.getResponseCode());
    	} catch(HibernateException he) {
    		throw new TUMApplicationException(requestTransactionId, TUMResponseCodes.RESERVATION_NOT_FOUND.getResponseCode());
    	}
	}
	
	public void cancelReserveRechargeSubscriber(Reservation reservation, TUMRechargeRequest request, TUMRechargeResponse response) {
		try {
			getHibernateTemplate().lock(reservation, LockMode.UPGRADE);
			if(!reservation.getStatus().equals(Reservation.STATUS_CANCELLING)) {
				if(reservation.getStatus().equals("COMMITTED")) {
					((TUMPResponse)response).generateFaultResponse(TUMResponseCodes.RESERVATION_ALREADY_COMMITTED.getResponseCode(), reservation.getRequestTransactionId().toString());
				} else if(reservation.getStatus().equals("CANCELLED")) {
					((TUMPResponse)response).generateFaultResponse(TUMResponseCodes.RESERVATION_ALREADY_CANCELLED.getResponseCode(), reservation.getRequestTransactionId().toString());
				}
			} else {
				reservation.setStatus("CANCELLED");
				reservation.setRequestTimestamp(request.getRequestTimeStamp());
				reservation.setTumTimestamp(new Date());
				getHibernateTemplate().update(reservation);
				((TUMPResponse)response).generateCancelReservationResponse(request.getRequestTransactionID(), reservation.getRequestDealerId(), reservation.getRequestSubDealerId(), reservation.getSubscriberMSISDN(), reservation.getTransAmount().intValue(), reservation.getTumTimestamp(), TUMResponseCodes.SUCCESS.getResponseCode());
			}
		}  
		catch(HibernateException he) {
			((TUMPResponse)response).generateFaultResponse(TUMResponseCodes.RESERVATION_NOT_FOUND.getResponseCode(), request.getRequestTransactionID());
		}
	}
	
	public void commitReserveRechargeSubscriber(Reservation reservation, TUMRechargeRequest request, TUMRechargeResponse response) {
		try {
			getHibernateTemplate().lock(reservation, LockMode.UPGRADE);
			if(!response.isFault()) {
				reservation.setStatus(Reservation.STATUS_COMMITTED);
				reservation.setTumTimestamp(new Date());
			} else {
				reservation.setStatus(Reservation.STATUS_ERROR);
				reservation.setTumTimestamp(new Date());
			}
			getHibernateTemplate().update(reservation);
		} catch(HibernateException he) {
			logger.error("Could not commit reservation with id " + reservation.getRequestTransactionId().toString() + ": " + he.getMessage());
		}
	}
	
	@Override
	public void checkoutReservations() {
		HibernateCheckoutReservationsCallback cb = new HibernateCheckoutReservationsCallback();
		getHibernateTemplate().execute(cb);
	}
	
	public List<Reservation> findReservationsByStatus(String status) {
		List<Reservation> returnVal = getHibernateTemplate().find("from mcel.tump.reservations.domain.Reservation r where r.status = ?", status);
		return returnVal;	
	}
	
	public Reservation checkoutReservationById(String requestTransactionId) {
		return (Reservation)getHibernateTemplate().load(Reservation.class, Long.parseLong(requestTransactionId), LockMode.UPGRADE_NOWAIT);
	}
	
	@Override
	public void checkoutReservationForCancel(TUMRechargeRequest request, TUMRechargeResponse response) {
		HibernateCheckoutReservationForCancelCallback cb = new HibernateCheckoutReservationForCancelCallback(request, response);
		getHibernateTemplate().execute(cb);
		
	}

	@Override
	public void reserveVoucher(TUMRechargeRequest request, TUMRechargeResponse response) {
		HibernateReserveVoucherCallback cb = new HibernateReserveVoucherCallback(request, response);
		response = (TUMPResponse)getHibernateTemplate().execute(cb);
		request = cb.getRequest(); 
	}
	
	public void commitReserveVoucher(TUMRechargeRequest request, TUMRechargeResponse response) {
		HibernateCommitVoucherCallback cb = new HibernateCommitVoucherCallback(request, response);
		response = (TUMPResponse)getHibernateTemplate().execute(cb);
		request = cb.getRequest();
	}
	
	public void cancelReserveVoucher(TUMRechargeRequest request, TUMRechargeResponse response) {
		HibernateCancelVoucherCallback cb = new HibernateCancelVoucherCallback(request, response);
		response = (TUMPResponse)getHibernateTemplate().execute(cb);
		request = cb.getRequest();
	}
	
	public void writeVoucherFinalLog(TUMRechargeRequest request, TUMRechargeResponse response) {
		HibernateWriteVoucherFinalLogCallback cb = new HibernateWriteVoucherFinalLogCallback(request, response);
		response = (TUMPResponse)getHibernateTemplate().execute(cb);
		request = cb.getRequest(); 
	}
}
