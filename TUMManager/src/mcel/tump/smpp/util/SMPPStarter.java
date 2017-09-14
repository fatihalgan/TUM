/*
 * SMPPStarter.java
 * 
 * Created on Aug 29, 2007, 4:54:45 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.smpp.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import mcel.tump.util.spring.context.AppContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bridge.smpp.SMSCFactory;

/**
 *
 * @author db2admin
 */
public class SMPPStarter implements ServletContextListener {

    private static final Log logger = LogFactory.getLog(SMPPStarter.class);
    
    public void contextInitialized(ServletContextEvent sce) {
        try {
            logger.info("Starting SMPP Service");
            SMSCFactory smscFactory = AppContext.instance().getSmppSender().getSmscFactory();
            smscFactory.bind();
        } catch(Exception e) {
            logger.error("SMPP Service could not be started: " + e);
        }
    }

    public void contextDestroyed(ServletContextEvent sce) {
        SMPPSender sender = AppContext.instance().getSmppSender();
        sender.getSmscFactory().unbind();
    }

}
