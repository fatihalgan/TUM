/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mcel.tump.gateway.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;

import mcel.tump.gateway.service.chain.TUMRechargeDaoProcessor;
import mcel.tump.gateway.util.TUMRechargeMessage;
import mcel.tump.gateway.util.TUMRechargeRequest;
import mcel.tump.gateway.util.TUMRechargeResponse;
import mcel.tump.gateway.util.TUMPResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

/**
 *
 * @author db2admin
 */
public class HibernateRechargeSubscriberCallback implements HibernateCallback {

    private TUMRechargeRequest request;
    private TUMRechargeResponse response;
    
    private static final Log logger = LogFactory.getLog(HibernateRechargeSubscriberCallback.class);

    public HibernateRechargeSubscriberCallback(TUMRechargeRequest request, TUMRechargeResponse response) {
        this.request = request;
        this.response = response;
    }

    public Object doInHibernate(Session session) throws HibernateException, SQLException {
        try {
        	Connection con = session.connection();
        	CallableStatement stmt = con.prepareCall("{call TUMT_MAIN.sp_RechargeSubscriber(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
        	Timestamp timestamp = new java.sql.Timestamp(getRequest().getRequestTimeStamp().getTime());
        	long transferAmount = getTransferAmount(getRequest());
        	String msisdn = getRequest().getSubscriberMSISDN();
        	String username = getRequest().getUsername();
        	stmt.setTimestamp("P_TRANS_TIME", timestamp);
        	stmt.setLong("P_TRANS_AMOUNT", transferAmount);
        	stmt.setString("P_SUBSCRIBER_MSISDN", msisdn);
        	stmt.setString("P_USERNAME", username);
        	stmt.setString("P_EDGE_DEALER_ID", getRequest().getRequestDealerID());
        	stmt.setString("P_EDGE_SUBDEALER_ID", getRequest().getRequestSubDealerID());
        	stmt.setString("P_EDGE_TRANSACTION_ID", getRequest().getRequestTransactionID());
        	stmt.setTimestamp("P_EDGE_TIMESTAMP", new java.sql.Timestamp(getRequest().getRequestTimeStamp().getTime()));
        	stmt.setString("P_SIGNATURE", getRequest().getSignature());
        	stmt.registerOutParameter("P_ID", Types.BIGINT);
        	stmt.registerOutParameter("P_BEFORE_BALANCE", Types.BIGINT);
        	stmt.registerOutParameter("P_AFTER_BALANCE", Types.BIGINT);
        	stmt.registerOutParameter("P_RESULT_CODE", Types.INTEGER);
        	stmt.registerOutParameter("P_ALERT", Types.INTEGER);
        	stmt.executeUpdate();
        	Long tumTransactionId = stmt.getLong("P_ID");
        	Integer beforeBalance = stmt.getInt("P_BEFORE_BALANCE");
        	Integer afterBalance = stmt.getInt("P_AFTER_BALANCE");
        	Integer resultCode = stmt.getInt("P_RESULT_CODE");
        	Integer alertCode = stmt.getInt("P_ALERT");
        	((TUMPResponse) getResponse()).generateRechargeSubscriberResponse(tumTransactionId.toString(), getRequest().getRequestTransactionID(),
                getRequest().getRequestDealerID(), getRequest().getRequestSubDealerID(),
                getRequest().getSubscriberMSISDN(), getRequest().getTransferAmount(), new Date(), beforeBalance, afterBalance, resultCode, alertCode);
        	logger.debug("ResultCode from TUMT_MAIN.sp_RechargeSubscriber for transaction " + request.getRequestTransactionID() + ": " + resultCode.toString());
        } catch(Exception e) {
        	logger.error("Exception while trying to recharge subscriber in TUM DB for transaction " + request.getRequestTransactionID() +  ": " + e.getMessage());
        }
        return response;
    }
    
    private Integer getTransferAmount(TUMRechargeRequest request) {
    	Integer transactionAmountCommission = request.getTransactionAmountCommission();
    	if(transactionAmountCommission == null || transactionAmountCommission == 0) {
    		return request.getTransferAmount();
    	} else {
    		return request.getTransferAmount() - transactionAmountCommission;
    	}
    }

    public TUMRechargeRequest getRequest() {
        return request;
    }

    public TUMRechargeResponse getResponse() {
        return response;
    }
}
