/*
 * IAccountService.java
 * 
 * Created on Aug 9, 2007, 1:43:25 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.account.service;

import java.util.Date;
import mcel.tump.account.dao.RegisterPaymentDBResponse;
import mcel.tump.account.domain.Account;
import mcel.tump.account.domain.DailyPayments;
import mcel.tump.account.domain.TotalDailySalesByVoucherType;
import mcel.tump.account.domain.TotalHourlySalesByVoucherType;
import mcel.tump.account.domain.TotalMonthlySalesByDealer;
import mcel.tump.util.value.Money;

/**
 *
 * @author db2admin
 */
public interface IAccountService {
    public Account getAccountOfDealer(Long dealerId);
    public Account getAccount(Long accountId);
    public void saveAccount(Account account);
    public void deleteAccount(Account account);
    public void creditAmount(Money amount, String dealerCode, String accpacRefno, String accpacOrderId);
    public RegisterPaymentDBResponse paymentArrive(String accpacDealerId, String accpacRefNo, String accpacOrderId,
        Date accpacTimestamp, String accpacUsername, Long amount);
    public void processFailedAdjustments();
    public TotalDailySalesByVoucherType getDailySalesByVoucherType(Date date);
    public TotalHourlySalesByVoucherType getHourlySalesByVoucherType();
    public DailyPayments getDailyPayments(Date date);
    public TotalMonthlySalesByDealer getMonthlySalesByDealer(Date date);
}
