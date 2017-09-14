package mcel.tump.gateway.service.chain;

import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Locale;

import mcel.tump.dealer.domain.AbstractDealer;
import mcel.tump.dealer.domain.Notifiable;
import mcel.tump.dealer.service.IDealerService;
import mcel.tump.gateway.util.TUMGWTokens;
import mcel.tump.gateway.util.TUMRechargeRequest;
import mcel.tump.gateway.util.TUMRechargeResponse;
import mcel.tump.notification.service.INotificationService;
import mcel.tump.util.Messages;
import mcel.tump.util.value.ShopBalanceAlertType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TUMSuccessfulRechargeConfirmationProcessor implements RechargeRequestProcessor {

	
	private RechargeRequestProcessor nextHandler;
    private RechargeRequestProcessor onFaultHandler;
    private INotificationService notificationService;
    private IDealerService dealerService;
    private static final Log logger = LogFactory.getLog(TUMSuccessfulRechargeConfirmationProcessor.class);
    
    
    public void setNextHandler(RechargeRequestProcessor processor) {
        this.nextHandler = processor;
    }
    
    public RechargeRequestProcessor getNextHandler() {
        return this.nextHandler;
    }

    public INotificationService getNotificationService() {
        return notificationService;
    }

    public void setNotificationService(INotificationService notificationService) {
        this.notificationService = notificationService;
    }
    
    public void process(TUMRechargeRequest request, TUMRechargeResponse response) {
        logger.debug("TUMSuccessfulRechargeNotification processor started...");
        if(!response.isFault()) {
            try {
            	if((response.getSenderMSISDN() != null) && (!response.getSenderMSISDN().equals(response.getSubscriberMSISDN()))) { 
            		logger.debug("This is a money transfer, Recharge Confirmation processor will skip execution to next handler..");
            		if(nextHandler != null) nextHandler.process(request, response);
            		return;
            	}
                logger.debug("Trying to send notifications..");
                if(request.getMethodName().equals(TUMGWTokens.RechargeSubscriberPinRequest.toString())) {
                	sendPinnedRechargeSubsConfirmationSMS(request, response);
                } else if(request.getMethodName().equals(TUMGWTokens.RechargeSubscriberRequest.toString())) {
                	int adjustmentFlag = response.getAdjustmentResultCode();
                    int refillFlag = response.getRefillResultCode();
                    //Means that the refill was successful, however, adjustment has failed
                    sendRechargeSubsConfirmationSMS(request, response);
                    sendBalanceAlertMessage(request, response);
                } else if(request.getMethodName().equals(TUMGWTokens.AdjustSubscriberAccountRequest.toString())) {
                	sendAdjustSubsConfirmationSMS(request, response);
                	sendBalanceAlertMessage(request, response);
                } else if(request.getMethodName().equals(TUMGWTokens.RechargeSubscriberSMSRequest.toString())) {
                	sendRechargeSubsSMSConfirmationSMS(request, response);
                	sendBalanceAlertMessage(request, response);
                }
                
            } catch(Exception e) {
                logger.error("Error while trying to send notification messages: " + e.getMessage());
            }
        } else {
           logger.debug("An error response has been passed to handler. Will not process this request."); 
        }
        if(nextHandler != null) nextHandler.process(request, response);
    }
    
    private void sendPinnedRechargeSubsConfirmationSMS(TUMRechargeRequest request, TUMRechargeResponse response) {
    	String message = null;
        String voucherProfileName = response.getVoucherProfileName();        
        if(response.getSubscriberLanguage().equals(Locale.ENGLISH)) {
        	message = MessageFormat.format(Messages.getString("recharge_pinned_subs_confirm_sms"), voucherProfileName);
        } else { 
        	message = MessageFormat.format(Messages.getString("recharge_pinned_subs_confirm_sms_por"), voucherProfileName);
        }
        String from = "mCel";
        logger.debug("Sending Recharge Confirmation SMS Message: " + message);
        getNotificationService().sendSMS(message, response.getSubscriberMSISDN(), from);
    }
    
    private void sendRechargeSubsConfirmationSMS(TUMRechargeRequest request, TUMRechargeResponse response) {
        String message = null;
        Float beforeBalance = Float.parseFloat(response.getSubscriberBalanceBefore());
        Float afterBalance = Float.parseFloat(response.getSubscriberBalanceAfter());
        Float transferAmount = afterBalance - beforeBalance;
        
        NumberFormat nf = NumberFormat.getIntegerInstance();
        String strTransferAmount = nf.format(transferAmount);
        if(response.getSubscriberLanguage().equals(Locale.ENGLISH)) {
        	message = MessageFormat.format(Messages.getString("recharge_subs_confirm_sms"), strTransferAmount);
        } else { 
        	message = MessageFormat.format(Messages.getString("recharge_subs_confirm_sms_por"), strTransferAmount);
        }
        String from = "mCel";
        logger.debug("Sending Recharge Confirmation SMS Message: " + message);
        getNotificationService().sendSMS(message, response.getSubscriberMSISDN(), from);
    }
    
    private void sendRechargeSubsSMSConfirmationSMS(TUMRechargeRequest request, TUMRechargeResponse response) {
    	String message = null;
        Float beforeBalance = Float.parseFloat(response.getSubscriberBalanceBefore());
        Float afterBalance = Float.parseFloat(response.getSubscriberBalanceAfter());
        Float transferAmount = afterBalance - beforeBalance;
        
        NumberFormat nf = NumberFormat.getIntegerInstance();
        String strTransferAmount = nf.format(transferAmount);
        if(response.getSubscriberLanguage().equals(Locale.ENGLISH)) {
        	message = MessageFormat.format(Messages.getString("recharge_subs_sms_confirm_sms"), strTransferAmount, response.getVoucherProfileName(), response.getRequestTransactionID());
        } else { 
        	message = MessageFormat.format(Messages.getString("recharge_subs_sms_confirm_sms_por"), strTransferAmount, response.getVoucherProfileName(), response.getRequestTransactionID());
        }
        String from = "mCel";
        logger.debug("Sending Recharge Confirmation SMS Message: " + message);
        getNotificationService().sendSMS(message, response.getSubscriberMSISDN(), from);
    }
    
    private void sendAdjustSubsConfirmationSMS(TUMRechargeRequest request, TUMRechargeResponse response) {
    	String message = null;
        Float beforeBalance = Float.parseFloat(response.getSubscriberBalanceBefore());
        Float afterBalance = Float.parseFloat(response.getSubscriberBalanceAfter());
        Float transferAmount = afterBalance - beforeBalance;
        
        NumberFormat nf = NumberFormat.getIntegerInstance();
        String strTransferAmount = nf.format(transferAmount);
        if(response.getSubscriberLanguage().equals(Locale.ENGLISH)) {
        	message = MessageFormat.format(Messages.getString("adjust_subs_confirm_sms"), strTransferAmount, response.getRequestTransactionID());
        } else { 
        	message = MessageFormat.format(Messages.getString("adjust_subs_confirm_sms_por"), strTransferAmount, response.getRequestTransactionID());
        }
        String from = "mCel";
        logger.debug("Sending Recharge Confirmation SMS Message: " + message);
        getNotificationService().sendSMS(message, response.getSubscriberMSISDN(), from);
    }
    
    private void sendBalanceAlertMessage(TUMRechargeRequest request, TUMRechargeResponse response) {
        AbstractDealer dealer = getDealerService().getDealerByDealerCode(request.getRequestDealerID());
        ShopBalanceAlertType alert = dealer.getAccount().getAlertFlag(response.getDealerBalanceBefore(), response.getDealerBalanceAfter());
        if(alert.equals(alert.CriticalBalanceAlert) || alert.equals(alert.LowBalanceAlert)) {
            getNotificationService().sendDealerBalanceAlertMail((Notifiable)dealer, response.getDealerBalanceAfter(), alert);
        }
    }

    public IDealerService getDealerService() {
        return dealerService;
    }

    public void setDealerService(IDealerService dealerService) {
        this.dealerService = dealerService;
    }

    public RechargeRequestProcessor getOnFaultHandler() {
        return onFaultHandler;
    }

    public void setOnFaultHandler(RechargeRequestProcessor onFaultHandler) {
        this.onFaultHandler = onFaultHandler;
    }
}
