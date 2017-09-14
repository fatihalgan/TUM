/*
 * SMPPSender.java
 *
 * Created on Aug 29, 2007, 3:54:12 PM
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.smpp.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;

import com.bridge.smpp.SMSCFactory;
import com.bridge.smpp.SMSCSender;
import com.bridge.smpp.TextMessage;

/**
 *
 * @author db2admin
 */
public class SMPPSender extends SMSCSender<TextMessage> implements ApplicationListener {

    private static final Log logger = LogFactory.getLog(SMPPSender.class);
        
    public SMPPSender() {
    	super();
    }
    
    public SMPPSender(SMSCFactory smscFactory, String connectionName) {
        this.smscFactory = smscFactory;
        this.connectionName = connectionName;
    }

    public TextMessage[] convertMessage(TextMessage src) {
    	return new TextMessage[] {src};
    }
    
    public void sendSMS(String messageText, String to, String from) {
    	TextMessage msg = new TextMessage();
    	msg.setMessageText(messageText);
    	msg.setDestAddress("258" + to);
    	msg.setSourceAddress(from);
    	smscFactory.getConnection(connectionName).send(msg);
    }

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if(event instanceof ContextRefreshedEvent) {
			try {
				logger.info("Starting SMPP Service");
	            smscFactory.bind();
	        } catch(Exception e) {
	            logger.error("SMPP Service could not be started: " + e);
	        }
	    } else if(event instanceof ContextClosedEvent) {
	            smscFactory.unbind();
	    }
	}
}
