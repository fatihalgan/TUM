/*
 * AccountDaoImpl.java
 * 
 * Created on Aug 9, 2007, 1:36:22 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.account.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import mcel.tump.account.domain.Account;
import mcel.tump.account.domain.AccpacPayment;
import mcel.tump.account.domain.FailedAdjustment;
import mcel.tump.account.domain.SaleByDealer;
import mcel.tump.account.domain.SaleByVoucherType;
import mcel.tump.account.domain.DailyPayments;
import mcel.tump.account.domain.TotalDailySalesByVoucherType;
import mcel.tump.account.domain.TotalHourlySalesByVoucherType;
import mcel.tump.account.domain.TotalMonthlySalesByDealer;
import mcel.tump.util.persistence.BaseDao;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

/**
 *
 * @author db2admin
 */
public class AccountDaoImpl extends BaseDao implements IAccountDao {

    public Account findAccountOfDealer(Long dealerId) {
        List<Account> accounts = getHibernateTemplate().find("from mcel.tump.account.domain.Account as account where " +
            "account.dealer.id = ?", dealerId);
        Iterator<Account> it = accounts.iterator();
        if(it.hasNext()) return it.next();
        return null;
    }

    public Account findAccountById(Long accountId) {
        return (Account)getHibernateTemplate().get(Account.class, accountId);
    }

    public Account findAccountByIdForUpdate(Long accountId) {
        return (Account)getHibernateTemplate().get(Account.class, accountId, LockMode.UPGRADE);
    }
    
    public TotalDailySalesByVoucherType getDailySalesByVoucherType(final Date date) {
    	return (TotalDailySalesByVoucherType)getHibernateTemplate().execute(new HibernateCallback() {
    		public Object doInHibernate(Session session) throws HibernateException, SQLException {
    			@SuppressWarnings("deprecation")
    			Connection con = session.connection();
    			PreparedStatement stmt = con.prepareStatement("select count(*), trans_amount, edge_dealer_id from tum_trans_tbl " + 
    					"group by trans_amount, edge_dealer_id order by trans_amount");
    			ResultSet rs = stmt.executeQuery();
    			TotalDailySalesByVoucherType total = new TotalDailySalesByVoucherType();
    			total.setDate(date);
    			while(rs.next()) {
    				SaleByVoucherType sale = new SaleByVoucherType();
    				sale.setNumberSold(rs.getInt(1));
    				sale.setTransAmount(rs.getInt(2));
    				sale.setDealerCode(rs.getString(3));
    				total.getSales().add(sale);
    			}
    			return total;
    		}
    	});
    }
    
    public TotalHourlySalesByVoucherType getHourlySalesByVoucherType() {
    	return (TotalHourlySalesByVoucherType)getHibernateTemplate().execute(new HibernateCallback() {
    		public Object doInHibernate(Session session) throws HibernateException, SQLException {
    			@SuppressWarnings("deprecation")
    			Connection con = session.connection();
    			PreparedStatement stmt = con.prepareStatement("select count(*), trans_amount, edge_dealer_id from tum_trans_tbl " + 
    					"group by trans_amount, edge_dealer_id order by trans_amount");
    			ResultSet rs = stmt.executeQuery();
    			TotalHourlySalesByVoucherType total = new TotalHourlySalesByVoucherType();
    			while(rs.next()) {
    				SaleByVoucherType sale = new SaleByVoucherType();
    				sale.setNumberSold(rs.getInt(1));
    				sale.setTransAmount(rs.getInt(2));
    				sale.setDealerCode(rs.getString(3));
    				total.getSales().add(sale);
    			}
    			return total;
    		}
    	});
    }
    
    public TotalMonthlySalesByDealer getMonthlySalesByDealer(final Date date) {
    	return (TotalMonthlySalesByDealer)getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				@SuppressWarnings("deprecation")
				Connection con = session.connection();
				PreparedStatement stmt = con.prepareStatement("select sum(trans_amount), edge_dealer_id from tum_trans_hist_cs_unverified " +
					"where edge_timestamp > ? and edge_timestamp  < ? " + "group by edge_dealer_id order by sum(trans_amount)");
				Timestamp start = new Timestamp(date.getYear(), date.getMonth(), date.getDate(), 0, 0, 0, 0);
    			stmt.setTimestamp(1, start);
    			Calendar cal = Calendar.getInstance();
    			cal.setTime(start);
    			cal.add(Calendar.MONTH, 1);
    			stmt.setTimestamp(2, new Timestamp(cal.getTimeInMillis()));
    			ResultSet rs = stmt.executeQuery();
    			TotalMonthlySalesByDealer total = new TotalMonthlySalesByDealer();
    			total.setDate(date);
    			while(rs.next()) {
    				SaleByDealer sale = new SaleByDealer();
    				sale.setTransAmount(rs.getInt(1));
    				sale.setDealerCode(rs.getString(2));
    				total.getSales().add(sale);
    			}
    			return total;
			}
    		
    	});
    }
    
    public DailyPayments getDailyPayments(final Date date) {
    	return (DailyPayments)getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				@SuppressWarnings("deprecation")
    			Connection con = session.connection();
    			PreparedStatement stmt = con.prepareStatement("select amount, accpac_ref_no, edge_dealer_id, accpac_timestamp, " + 
    				"accpac_dealer_id, accpac_order_id from tum_accpac_payments_tbl " +
    				" where status = 'Registered' and accpac_timestamp > ? and accpac_timestamp < ? " +
    				"order by accpac_timestamp");
    			Timestamp start = new Timestamp(date.getYear(), date.getMonth(), date.getDate(), 0, 0, 0, 0);
    			stmt.setTimestamp(1, start);
    			Calendar cal = Calendar.getInstance();
    			cal.setTime(start);
    			cal.roll(Calendar.DATE, true);
    			stmt.setTimestamp(2, new Timestamp(cal.getTimeInMillis()));
    			ResultSet rs = stmt.executeQuery();
    			DailyPayments total = new DailyPayments();
    			total.setDate(date);
    			while(rs.next()) {
    				AccpacPayment payment = new AccpacPayment();
    				payment.setAmount(rs.getLong(1));
    				payment.setInvoiceNumber(rs.getString(2));
    				payment.setShopName(rs.getString(3));
    				payment.setPaymentTime(rs.getDate(4));
    				payment.setSubDealer(rs.getString(5));
    				payment.setShipmentNumber(rs.getString(6));
    				total.getPayments().add(payment);
    			}
    			return total;
			}
    	
    	});
    	
    }
   
    public RegisterPaymentDBResponse makePayment(final String username, final String accpacDealerId, final Date accpacTimestamp, final String erpReference, final String erpOrderId, final Long amount, final String erpUser) {
        return (RegisterPaymentDBResponse)getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                @SuppressWarnings("deprecation")
                Connection con = session.connection();
                CallableStatement stmt = con.prepareCall("{call TUMT_MAIN.sp_PaymentRegisterShop(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
                if(username != null) {
                    //This payment is entered from the TUMManager screen
                    stmt.setString("P_USERNAME", username);
                    stmt.setString("P_ACCPAC_USER", "");
                } else {
                    //This payment is coming from ACCPAC
                    stmt.setString("P_USERNAME", "appadmin");
                    stmt.setString("P_ACCPAC_USER", erpUser);
                }
                stmt.setString("P_ACCPAC_DEALER_ID", accpacDealerId);
                stmt.setTimestamp("P_ACCPAC_TIMESTAMP", new java.sql.Timestamp(accpacTimestamp.getTime()));
                System.out.println(new java.sql.Date(accpacTimestamp.getTime()).toLocaleString());
                stmt.setString("P_ERP_REFERENCE", erpReference);
                stmt.setString("P_ERP_ORDER_ID", erpOrderId);
                stmt.setInt("P_ERP_AMOUNT", amount.intValue());
                stmt.registerOutParameter("P_TUM_TRANSACTION_ID", Types.BIGINT);
                stmt.registerOutParameter("P_DB_RESULTCODE", Types.INTEGER);
                stmt.registerOutParameter("P_BALANCE_BEFORE", Types.INTEGER);
                stmt.registerOutParameter("P_BALANCE_AFTER", Types.INTEGER);
                stmt.registerOutParameter("P_TUM_DEALER_ID", Types.INTEGER);
                stmt.executeUpdate();
                Long tumTransactionId = stmt.getLong("P_TUM_TRANSACTION_ID");
                Integer dbResult = stmt.getInt("P_DB_RESULTCODE");
                Integer balanceBefore = stmt.getInt("P_BALANCE_BEFORE");
                Integer balanceAfter = stmt.getInt("P_BALANCE_AFTER");
                Integer tumDealerID = stmt.getInt("P_TUM_DEALER_ID");
                RegisterPaymentDBResponse response = new RegisterPaymentDBResponse();
                response.setTumTransactionId(tumTransactionId);
                response.setDbResult(dbResult);
                response.setBalanceBefore(balanceBefore);
                response.setBalanceAfter(balanceAfter);
                response.setTumDealerID(tumDealerID);
                return response;
            }
        });
    }

    public List<FailedAdjustment> getFailedAdjustments() {
        List<FailedAdjustment> failedAdjustments = getHibernateTemplate().find("from mcel.tump.account.domain.FailedAdjustment as a where " +
            "a.refillFlag = ? and a.adjustmentFlag > ?", new Object[] {0, 100});
        return failedAdjustments;
    }
}
