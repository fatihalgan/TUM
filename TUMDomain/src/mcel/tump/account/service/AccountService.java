/*
 * AccountService.java
 * 
 * Created on Aug 9, 2007, 1:46:23 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.account.service;

import com.bridge.ena.cs3cp6.command.AbstractCS3CP6Command;
import com.bridge.ena.cs3cp6.command.CommandFactory;
import com.bridge.ena.xmlrpc.exception.XmlRpcConnectionException;
import java.util.Date;
import java.util.List;
import mcel.tump.account.dao.IAccountDao;
import mcel.tump.account.dao.RegisterPaymentDBResponse;
import mcel.tump.account.domain.Account;
import mcel.tump.account.domain.DailyPayments;
import mcel.tump.account.domain.FailedAdjustment;
import mcel.tump.account.domain.SaleByDealer;
import mcel.tump.account.domain.TotalDailySalesByVoucherType;
import mcel.tump.account.domain.TotalHourlySalesByVoucherType;
import mcel.tump.account.domain.TotalMonthlySalesByDealer;
import mcel.tump.dealer.domain.AccpacAccountable;
import mcel.tump.dealer.service.IDealerService;
import mcel.tump.notification.service.INotificationService;
import mcel.tump.reference.service.IParametersService;
import mcel.tump.util.value.Money;
import mcel.tump.util.value.TresholdValues;
import org.acegisecurity.context.SecurityContextHolder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author db2admin
 */
public class AccountService implements IAccountService {

    private static final Log logger = LogFactory.getLog(AccountService.class);

    private IAccountDao accountDao;
    private IDealerService dealerService;
    private IParametersService parametersService;
    private INotificationService notificationService;
    private CommandFactory commandFactory;
    
    public IAccountDao getAccountDao() {
        return accountDao;
    }

    public void setAccountDao(IAccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public IDealerService getDealerService() {
        return dealerService;
    }

    public void setDealerService(IDealerService dealerService) {
        this.dealerService = dealerService;
    }

    public IParametersService getParametersService() {
        return parametersService;
    }

    public void setParametersService(IParametersService parametersService) {
        this.parametersService = parametersService;
    }

    public INotificationService getNotificationService() {
        return notificationService;
    }

    public void setNotificationService(INotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public CommandFactory getCommandFactory() {
        return commandFactory;
    }

    public void setCommandFactory(CommandFactory commandFactory) {
        this.commandFactory = commandFactory;
    }
    

    public Account getAccountOfDealer(Long dealerId) {
        return getAccountDao().findAccountOfDealer(dealerId);
    }

    public Account getAccount(Long accountId) {
        return getAccountDao().findAccountById(accountId);
    }

    public void saveAccount(Account account) {
        TresholdValues oldValues = getAccountDao().findAccountById(account.getId()).getTresholdValues();
        account = (Account)getAccountDao().merge(account);
        TresholdValues cumulativeTresholds = getDealerService().getCumulativeTresholdValuesExcept(account.getDealer());
        getDealerService().validateTresholds(cumulativeTresholds, account.getDealer());
    }

    public void deleteAccount(Account account) {
        getAccountDao().delete(account);
    }

    public void creditAmount(Money amount, String dealerCode, String accpacRefNo, String accpacOrderId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        RegisterPaymentDBResponse response = null;
        response = getAccountDao().makePayment(username, dealerCode,
            new Date(), accpacRefNo, accpacOrderId, amount.getAmount().longValue(), null);
        if(response.getDbResult() != 0) throw new RuntimeException("Could not credit amount, ErrorCode: " + response.getDbResult());
        AccpacAccountable dealer = (AccpacAccountable)getDealerService().getDealer(Long.parseLong(response.getTumDealerID().toString()));
        notifyPaymentArrive(dealer, accpacRefNo, accpacOrderId, amount.getAmount().longValue(), response.getBalanceAfter());
    }
    
    public RegisterPaymentDBResponse paymentArrive(String accpacDealerId, String accpacRefNo, String accpacOrderId,
        Date accpacTimestamp, String accpacUsername, Long amount) {
        RegisterPaymentDBResponse response = getAccountDao().makePayment(null, accpacDealerId, accpacTimestamp, accpacRefNo, accpacOrderId, amount, accpacUsername);
        if(response.getDbResult() == 0) {
            AccpacAccountable dealer = (AccpacAccountable)getDealerService().getDealer(response.getTumDealerID().longValue());
            if(dealer != null)notifyPaymentArrive(dealer, accpacRefNo, accpacOrderId, amount, response.getBalanceAfter());
        }
        return response;
    }

    private void notifyPaymentArrive(AccpacAccountable dealer, String accpacRefNo, String accpacOrderId, Long amount, Integer balanceAfter) {
        getNotificationService().sendPaymentArrivedInfoMail(dealer, accpacRefNo, accpacOrderId, amount, balanceAfter);
    }

    public void processFailedAdjustments() {
        List<FailedAdjustment> failedAdjustments = getAccountDao().getFailedAdjustments();
        logger.debug("Found " + failedAdjustments.size() + " failed adjustments..");
        for(FailedAdjustment fa : failedAdjustments) {
            if(!(fa.getCorrectionAmount() > 0)) continue;
            logger.debug("Edge Timestamp is: " + fa.getEdgeTimestamp().toLocaleString());
            AbstractCS3CP6Command cmd = commandFactory.getUpdateBalanceAndDateCommand(fa.getEdgeTransactionId().toString(),
                fa.getEdgeTimestamp(), fa.getSubscriberMSISDN(), fa.getCorrectionAmount(), "TUM Adjustment Correction", fa.getTumTransactionId().toString(), 0, 0, 0, 0, 100f);
            try {
                logger.debug("Trying to correct the failed adjustment for transaction: " + fa.getEdgeTransactionId());
                cmd.execute();
            } catch(XmlRpcConnectionException e) {
                logger.error("Could not correct the failed adjustment for MSISDN: " + fa.getSubscriberMSISDN() + " for TUM Transaction: " + fa.getTumTransactionId());
                continue;
            }
            if(cmd.isErrorOrFaultResponse()) {
                logger.error("Could not correct the failed adjustment for MSISDN: " + fa.getSubscriberMSISDN() + " for TUM Transaction: " + fa.getTumTransactionId() + ". Error response is: " + cmd.getFullResultCode());
                continue;
            }
            fa.setAdjustmentFlag(0);
            cmd = commandFactory.getBalanceAndDateCommand(fa.getSubscriberMSISDN(), new Date(), fa.getEdgeTransactionId().toString(), 100f);
            try {
                cmd.execute();
            } catch(XmlRpcConnectionException e) {
                logger.error("Could not get Balance to send SMS for the failed adjustment for MSISDN: " + fa.getSubscriberMSISDN() + " for TUM Transaction: " + fa.getTumTransactionId());
            }
            getNotificationService().sendFailedAdjustmentCorrectedNotificationSMS(fa, cmd.getSubscriberInfo().getLanguageLocale());
        }
        logger.debug("Finished correcting failed adjustments...");
    }
    
    public TotalDailySalesByVoucherType getDailySalesByVoucherType(Date date) {
    	return getAccountDao().getDailySalesByVoucherType(date);
    }
    
    public TotalHourlySalesByVoucherType getHourlySalesByVoucherType() {
    	return getAccountDao().getHourlySalesByVoucherType();
    }
    
    public DailyPayments getDailyPayments(Date date) {
    	return getAccountDao().getDailyPayments(date);
    }
    
    public TotalMonthlySalesByDealer getMonthlySalesByDealer(Date date) {
    	return getAccountDao().getMonthlySalesByDealer(date);
    }
}