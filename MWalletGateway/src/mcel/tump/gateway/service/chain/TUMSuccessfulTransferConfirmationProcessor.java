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

public class TUMSuccessfulTransferConfirmationProcessor implements RechargeRequestProcessor {

	private RechargeRequestProcessor nextHandler;
    private RechargeRequestProcessor onFaultHandler;
    private INotificationService notificationService;
    private IDealerService dealerService;
    private static final Log logger = LogFactory.getLog(TUMSuccessfulTransferConfirmationProcessor.class);
    
    
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
            	if((response.getSenderMSISDN() == null) || response.getSenderMSISDN().equals(response.getSubscriberMSISDN())) {
            		logger.debug("This is not a money transfer, Transfer confirmation processor will skip execution to next handler");
            		if(nextHandler != null) nextHandler.process(request, response);
            		return;
            	}
                logger.debug("Tring to send notifications..");
                int adjustmentFlag = response.getAdjustmentResultCode();
                int refillFlag = response.getRefillResultCode();
                //Means that the refill was successful, however, adjustment has failed
                sendTransferConfirmationSMS(request, response);
                sendBalanceAlertMessage(request, response);
            } catch(Exception e) {
                logger.error("Error while trying to send notification messages: " + e.getMessage());
            }
        } else {
           logger.debug("An error response has been passed to handler. Will not process this request."); 
        }
        if(nextHandler != null) nextHandler.process(request, response);
    }
    
    public void sendTransferConfirmationSMS(TUMRechargeRequest request, TUMRechargeResponse response) {
        String message = null;
        Float beforeBalance = Float.parseFloat(response.getSubscriberBalanceBefore());
        Float afterBalance = Float.parseFloat(response.getSubscriberBalanceAfter());
        Float transferAmount = afterBalance - beforeBalance;
        
        NumberFormat nf = NumberFormat.getIntegerInstance();
        String strTransferAmount = nf.format(transferAmount);
        if(response.getSubscriberLanguage().equals(Locale.ENGLISH)) {
        	if(request.getMethodName().equals(TUMGWTokens.RechargeSubscriberRequest.toString()))
        		message = MessageFormat.format(Messages.getString("transfer_subs_confirm_sms"), strTransferAmount, response.getSubscriberMSISDN(), response.getRequestTransactionID());
        	else if(request.getMethodName().equals(TUMGWTokens.RechargeSubscriberSMSRequest.toString()))
        		message = MessageFormat.format(Messages.getString("transfer_subs_sms_confirm_sms"), response.getFreeSMS(), response.getSubscriberMSISDN(), response.getRequestTransactionID());
        } else { 
        	if(request.getMethodName().equals(TUMGWTokens.RechargeSubscriberRequest.toString()))
        		message = MessageFormat.format(Messages.getString("transfer_subs_confirm_sms_por"), strTransferAmount, response.getSubscriberMSISDN(), response.getRequestTransactionID());
        	else if(request.getMethodName().equals(TUMGWTokens.RechargeSubscriberSMSRequest.toString()))
        		message = MessageFormat.format(Messages.getString("transfer_subs_sms_confirm_sms_por"), response.getFreeSMS(), response.getSubscriberMSISDN(), response.getRequestTransactionID());
        }
        String from = "mCel";
        logger.debug("Sending Recharge Confirmation SMS Message: " + message);
        getNotificationService().sendSMS(message, response.getSenderMSISDN(), from);
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
