/*
 * NotificationService.java
 * 
 * Created on Aug 14, 2007, 11:01:43 AM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.notification.service;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import mcel.tump.account.domain.FailedAdjustment;
import mcel.tump.account.domain.TotalHourlySalesByVoucherType;
import mcel.tump.dealer.domain.AccpacAccountable;
import mcel.tump.dealer.domain.Notifiable;
import mcel.tump.dealer.domain.ShopDailySaleInfo;
import mcel.tump.gateway.util.TUMRechargeRequest;
import mcel.tump.gateway.util.TUMRechargeResponse;
import mcel.tump.security.domain.User;
import mcel.tump.smpp.util.SMPPSender;
import mcel.tump.util.Messages;
import mcel.tump.util.value.ContactPerson;
import mcel.tump.util.value.NotificationInfo;
import mcel.tump.util.value.ShopBalanceAlertType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

/**
 *
 * @author db2admin
 */
public class NotificationService implements INotificationService {

    public NotificationService() {
        super();
    }
    
    private JavaMailSender mailSender;
    private SMPPSender smppSender;
    private static final Log logger = LogFactory.getLog(NotificationService.class);
    
    public JavaMailSender getMailSender() {
        return mailSender;
    }

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public SMPPSender getSmppSender() {
        return smppSender;
    }

    public void setSmppSender(SMPPSender smppSender) {
        this.smppSender = smppSender;
    }
    
    
    
    public void sendDealerCreatedInformationMail(final Notifiable dealer, final User user) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                String recipient = "";
                if(dealer.getNotificationInfo() == null) return;
                if(dealer.getNotificationInfo().getEmail1() != null && dealer.getNotificationInfo().getEmail1().length() > 0) {
                    recipient = recipient + dealer.getNotificationInfo().getEmail1();
                }
                if(dealer.getNotificationInfo().getEmail2() != null && dealer.getNotificationInfo().getEmail2().length() > 0) {
                    recipient = recipient + ", " + dealer.getNotificationInfo().getEmail2();
                }
                if(dealer.getNotificationInfo().getEmail3() != null && dealer.getNotificationInfo().getEmail3().length() > 0) {
                    recipient = recipient + ", " + dealer.getNotificationInfo().getEmail3();
                }
                mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
                mimeMessage.setText(MessageFormat.format(Messages.getString("dealer_created_notification_msg"), dealer.getDealerCode(), dealer.getDealerName()));
            }
        };
        sendMail(preparator);
        preparator = new MimeMessagePreparator() {

            public void prepare(MimeMessage mimeMessage) throws Exception {
                mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
                mimeMessage.setText(MessageFormat.format(Messages.getString("dealer_created_admin_msg"), dealer.getDealerCode(), dealer.getDealerName()));
            }
        };
        if(user.getEmail() != null && user.getEmail().length() > 0) sendMail(preparator);
    }
    
    public void sendDailySalesReportMail(final byte[] attachment, final Date date, final List<String> recipients) {
    	MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);
				message.setTo(recipients.toArray(new String[recipients.size()]));
				message.setSubject("TUM Daily Sales: " + date.toLocaleString());
				message.setText("Please find attached the daily sales data for the Topup Mediation Platform for " + date.toLocaleString());
				SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yy");
				String strDate = format.format(date);
				DataSource ds = new ByteArrayDataSource("TUMSales - " + strDate + ".xls", "application/ms-excel", attachment);
				message.addAttachment(ds.getName(), ds);
			}    	
    	};
    	sendMail(preparator);
    }
    
    public void sendDealerCreatedInformationSMS(final Notifiable dealer) {
        String message = MessageFormat.format(Messages.getString("dealer_created_sms"), dealer.getDealerCode(), dealer.getDealerName());
        String from = "mCel";
        if(dealer.getNotificationInfo() != null) {
            if(dealer.getNotificationInfo().getMsisdn1() != null && dealer.getNotificationInfo().getMsisdn1().length() > 0) sendSMS(message, dealer.getNotificationInfo().getMsisdn1(), from);
            if(dealer.getNotificationInfo().getMsisdn2() != null && dealer.getNotificationInfo().getMsisdn2().length() > 0) sendSMS(message, dealer.getNotificationInfo().getMsisdn2(), from);
            if(dealer.getNotificationInfo().getMsisdn3() != null && dealer.getNotificationInfo().getMsisdn3().length() > 0) sendSMS(message, dealer.getNotificationInfo().getMsisdn3(), from);
        }
    }
    
    public void sendMail(MimeMessagePreparator preparator) {
        try {
            getMailSender().send(preparator);
        } catch(MailException me) {
            me.printStackTrace();
            System.err.println(me.getMessage());
        }
    }
    
    public void sendSMS(String message, String to, String from) {
        logger.debug("Will try to send the following SMS message: " + message);
        if(getSmppSender() == null) return;
        getSmppSender().sendSMS(message, to, from);
    }

    public void sendUserCreatedInformationMail(final User user) {
        MimeMessagePreparator preperator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                String recipient = user.getEmail();
                mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
                mimeMessage.setText(MessageFormat.format(Messages.getString("user_created_mail_msg"), user.getUsername(), user.getPassword()));
            }
        };
        sendMail(preperator);
    }
    
    public void sendDealerDailySaleInfoMail(final ShopDailySaleInfo saleInfo, final User user) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                String recipient = user.getEmail();
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
                mimeMessage.setText(MessageFormat.format(Messages.getString("shop_daily_sale_info_mail_msg"), format.format(saleInfo.getDate()), saleInfo.getOwningDealer().getDealerName(), saleInfo.getNumberOfSales(), saleInfo.getSalesAmount()));
            }
        };
        sendMail(preparator);
    }

    public void sendDealerBalanceAlertMail(final Notifiable dealer, final Integer balance, final ShopBalanceAlertType alertType) {
        MimeMessagePreparator preperator = new MimeMessagePreparator() {

            public void prepare(MimeMessage mimeMessage) throws Exception {
                String recipient = "";
                ContactPerson contact = dealer.getContactPerson();
                NotificationInfo notifyInfo = dealer.getNotificationInfo();
                if((contact != null) && (contact.getEmail() != null))
                    recipient = recipient + contact.getEmail();
                if((notifyInfo != null) && (notifyInfo.getEmail1() != null))
                    recipient = recipient + "," + notifyInfo.getEmail1();
                if((notifyInfo != null) && (notifyInfo.getEmail2() != null))
                    recipient = recipient + "," + notifyInfo.getEmail2();
                if((notifyInfo != null) && (notifyInfo.getEmail3() != null))
                    recipient = recipient + "," + notifyInfo.getEmail3();
                
                mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
                if(alertType.equals(ShopBalanceAlertType.LowBalanceAlert))
                    mimeMessage.setText(MessageFormat.format(Messages.getString("low_balance_alert_mail_msg"), balance.toString(), dealer.getDealerName()));
                else if(alertType.equals(ShopBalanceAlertType.CriticalBalanceAlert))
                    mimeMessage.setText(MessageFormat.format(Messages.getString("critical_balance_alert_mail_msg"), balance.toString(), dealer.getDealerName()));
            }
        };
        sendMail(preperator);
    }

    public void userPasswordResetNotificationMail(final User user) {
        MimeMessagePreparator preperator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                String recipient = user.getEmail();
                mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
                mimeMessage.setText(MessageFormat.format(Messages.getString("password_reset_mail_msg"), user.getPassword()));
            }
        };
        sendMail(preperator);
    }

    public void sendPaymentArrivedInfoMail(final AccpacAccountable dealer, final String accpacRefNo, final String accpqacOrderId, final Long amount, final Integer balanceAfter) {
        MimeMessagePreparator preperator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                String recipient = "";
                ContactPerson contact = dealer.getContactPerson();
                NotificationInfo notifyInfo = dealer.getNotificationInfo();
                if((contact != null) && (contact.getEmail() != null))
                    recipient = recipient + contact.getEmail();
                if((notifyInfo != null) && (notifyInfo.getEmail1() != null))
                    recipient = recipient + "," + notifyInfo.getEmail1();
                if((notifyInfo != null) && (notifyInfo.getEmail2() != null))
                    recipient = recipient + "," + notifyInfo.getEmail2();
                if((notifyInfo != null) && (notifyInfo.getEmail3() != null))
                    recipient = recipient + "," + notifyInfo.getEmail3();
                mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
                mimeMessage.setText(MessageFormat.format(Messages.getString("payment_arrived_mail_msg"), accpqacOrderId, accpacRefNo, amount.toString(), balanceAfter.toString()));
            }
        };
        sendMail(preperator);
    }

    public void sendFailedAdjustmentCorrectedNotificationSMS(FailedAdjustment adjustment, Locale locale) {
        String message = null;
        if(locale.equals(Locale.ENGLISH)) message = MessageFormat.format(Messages.getString("adjustment_correction_notification_sms"), adjustment.toString());
        else message = MessageFormat.format(Messages.getString("adjustment_correction_notification_sms_por"), adjustment.toString());
        String from = "mCel";
        sendSMS(message, adjustment.getSubscriberMSISDN(), from);
    }
    
    class ByteArrayDataSource implements DataSource {
    	byte[] bytes;
	    String contentType;
	    String name;
	 
	    ByteArrayDataSource( String name, String contentType, byte[] bytes ) {
	      this.name = name;
	      this.bytes = bytes;
	      this.contentType = contentType;
	    }
	 
	    public String getContentType() {
	      return contentType;
	    }
	 
	    public InputStream getInputStream() {
	      return new ByteArrayInputStream(bytes);
	    }
	 
	    public String getName() {
	      return name;
	    }
	 
	    public OutputStream getOutputStream() throws IOException {
	      throw new FileNotFoundException();
	    }
    }

	@Override
	public void sendMonthlySalesReportMail(final byte[] attachment, final Date date, final Date endDate, final List<String> recipients) {
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);
				message.setTo(recipients.toArray(new String[recipients.size()]));
				message.setSubject("TUM Monthly Sales: " + date.toLocaleString() + " - " + endDate.toLocaleString());
				message.setText("Please find attached the daily sales data for the Topup Mediation Platform for " + date.toLocaleString() + " and " + endDate.toLocaleString());
				SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yy");
				String strDate = format.format(date);
				String strEndDate = format.format(endDate);
				DataSource ds = new ByteArrayDataSource("TUMSales - " + strDate + " - " + strEndDate + ".xls", "application/ms-excel", attachment);
				message.addAttachment(ds.getName(), ds);
			}    	
    	};
    	sendMail(preparator);
		
	}
	
	public void sendHourlySalesReportMail(final TotalHourlySalesByVoucherType salesData, final List<String> recipients) {
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);
				message.setTo(recipients.toArray(new String[recipients.size()]));
				message.setSubject("TUM Hourly Sales as of: " + new Date().toLocaleString());
				message.setText(salesData.toString());
			}
		};
		sendMail(preparator);
	}
}
