/*
 * IAccountDao.java
 * 
 * Created on Aug 9, 2007, 1:33:27 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.account.dao;

import java.util.Date;
import java.util.List;
import mcel.tump.account.domain.Account;
import mcel.tump.account.domain.DailyPayments;
import mcel.tump.account.domain.FailedAdjustment;
import mcel.tump.account.domain.TotalDailySalesByVoucherType;
import mcel.tump.account.domain.TotalHourlySalesByVoucherType;
import mcel.tump.account.domain.TotalMonthlySalesByDealer;
import mcel.tump.util.persistence.IBaseDao;

/**
 *
 * @author db2admin
 */
public interface IAccountDao extends IBaseDao {
    public Account findAccountOfDealer(Long dealerId);
    public Account findAccountById(Long accountId);
    public Account findAccountByIdForUpdate(Long accountId);
    public RegisterPaymentDBResponse makePayment(String username, String accpacDealerId, Date accpacTimestamp, String erpReference, String erpOrderId, Long amount, String erpUser);
    public List<FailedAdjustment> getFailedAdjustments();
    public TotalDailySalesByVoucherType getDailySalesByVoucherType(Date date);
    public TotalHourlySalesByVoucherType getHourlySalesByVoucherType();
    public DailyPayments getDailyPayments(final Date date);
    public TotalMonthlySalesByDealer getMonthlySalesByDealer(final Date date);
}
