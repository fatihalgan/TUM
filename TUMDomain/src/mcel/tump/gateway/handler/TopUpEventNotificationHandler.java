package mcel.tump.gateway.handler;

import java.util.Date;

import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import mcel.tump.gateway.util.TUMPRequest;
import mcel.tump.gateway.util.TUMPResponse;
import mcel.tump.gateway.util.TopUpEvent;

public class TopUpEventNotificationHandler extends AbstractTUMRequestHandler implements InitializingBean {

	private static Log logger = LogFactory.getLog(TopUpEventNotificationHandler.class);
	
	private JmsTemplate jmsTemplate;
	
	public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}


	@Override
	public void processRequest(final TUMPRequest request, final TUMPResponse response)
			throws TUMRequestHandlerException {
		logger.debug("Started TopUpEventNotificationHandler for transaction: " + request.getRequestTransactionID());
		try {
			if(request.getTransferAmount() <= 0) {
				logger.debug("Transaction amount was not bigger than 0 for Transaction ID: " + request.getRequestTransactionID() + ". Will not send Top Up notification for this event..");
				 if(getNext() != null) getNext().processRequest(request, response);
				return;
			}
			if(response.isFault()) {
				logger.debug("TopUp has returned with a Fault for transaction ID: " + request.getRequestTransactionID() + ". Will not send Top Up notification for this event..");
				 if(getNext() != null) getNext().processRequest(request, response);
				 return;
			}
			jmsTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					TopUpEvent evt = createTopUpEvent(request, response);
					return session.createTextMessage(evt.asXML());
				}
			});
			logger.debug("TopUpEvent notification sent for transaction: " + request.getRequestTransactionID() + " , msisdn: " + request.getSubscriberMSISDN());
		} catch(Exception e) {
			logger.error("TopUpEvent notification could not be sent for transaction: " + request.getRequestTransactionID() + " , msisdn: " + request.getSubscriberMSISDN());
		}
		 if(getNext() != null) getNext().processRequest(request, response);
	}
	
	private TopUpEvent createTopUpEvent(TUMPRequest request, TUMPResponse response) {
		String msisdn = request.getSubscriberMSISDN();
		Integer transAmount = response.getTransferAmount();
		Date edgeTimestamp = request.getRequestTimeStamp();
		String edgeDealerId = request.getRequestDealerID();
		TopUpEvent evt = new TopUpEvent(msisdn, transAmount, edgeTimestamp, edgeDealerId);
		return evt;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		jmsTemplate.setDeliveryMode(DeliveryMode.PERSISTENT);		
	}

}
