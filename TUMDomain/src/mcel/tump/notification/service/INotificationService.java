/*
 * INotificationService.java
 * 
 * Created on Aug 14, 2007, 11:01:07 AM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.notification.service;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.mail.javamail.MimeMessagePreparator;

import mcel.tump.account.domain.FailedAdjustment;
import mcel.tump.account.domain.TotalHourlySalesByVoucherType;
import mcel.tump.dealer.domain.AccpacAccountable;
import mcel.tump.dealer.domain.Notifiable;
import mcel.tump.dealer.domain.ShopDailySaleInfo;
import mcel.tump.gateway.util.TUMRechargeRequest;
import mcel.tump.gateway.util.TUMRechargeResponse;
import mcel.tump.security.domain.User;
import mcel.tump.util.value.ShopBalanceAlertType;

/**
 *
 * @author db2admin
 */
public interface INotificationService {
    public void sendDealerCreatedInformationMail(final Notifiable notifiable, final User user);
    public void sendDealerCreatedInformationSMS(final Notifiable dealer);
    public void sendUserCreatedInformationMail(User user);
    public void sendDealerBalanceAlertMail(final Notifiable dealer, final Integer balance, ShopBalanceAlertType alertType);
    public void sendDealerDailySaleInfoMail(final ShopDailySaleInfo saleInfo, final User user);
    public void userPasswordResetNotificationMail(final User user);
    public void sendPaymentArrivedInfoMail(final AccpacAccountable dealer, final String accpacRefNo, final String accpqacOrderId, Long amount, Integer balanceAfter);
    public void sendFailedAdjustmentCorrectedNotificationSMS(final FailedAdjustment adjustment, Locale locale);
    public void sendDailySalesReportMail(byte[] attachment, Date date, List<String> recipients);
    public void sendMonthlySalesReportMail(byte[] attachment, Date date, Date endDate, List<String> recipients);
    public void sendHourlySalesReportMail(final TotalHourlySalesByVoucherType salesData, final List<String> recipients);
    public void sendSMS(String message, String to, String from);
    public void sendMail(MimeMessagePreparator preparator);
}
