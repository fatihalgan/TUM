/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.integration.accpac;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import mcel.tump.account.dao.RegisterPaymentDBResponse;
import mcel.tump.account.service.IAccountService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sadun.util.polling.BasePollManager;
import org.sadun.util.polling.DirectoryPoller;
import org.sadun.util.polling.FileFoundEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 *
 * @author db2admin
 */
public class AccpacMessagePoller extends BasePollManager implements ApplicationListener {

    private static final Log logger = LogFactory.getLog(AccpacMessagePoller.class);
    private DirectoryPoller poller;
    private String directoryName;
    private IAccountService accountService;
        
    public AccpacMessagePoller() {
        super();
    }
    
    public void init() {
        poller = new DirectoryPoller(new File(getDirectoryName()));
        poller.setPollInterval(10000);
        poller.setAutoMove(true);
        poller.setDebugExceptions(true);
        poller.addPollManager(this);
        poller.setSendSingleFileEvent(true);
        poller.start();
    }
    
    public void onApplicationEvent(ApplicationEvent event) {
        if(event instanceof ContextRefreshedEvent) {
            try {
                logger.info("Starting Accpac Message Poller Service");
                init();
            } catch(Exception e) {
                logger.error("Accpac Message Poller Service could not be started: " + e);
            }
        } else if(event instanceof ContextClosedEvent) {
            poller.shutdown();
        }
    }
    
    @Override
    public void fileFound(FileFoundEvent event) {
        logger.info("Received new Accpac Message: ...Processing...");
        super.fileFound(event);
        //Start to process the file
         File file = event.getFile();
         String fileContent = "";
         try {
            FileReader reader = new FileReader(file);
            BufferedReader buf = new BufferedReader(reader);
            String line = null;
            while((line = buf.readLine()) != null) {
                fileContent = fileContent + line;
            }
         } catch(Exception e) {
             logger.error("Error while reading file content: ");
             e.printStackTrace();
             throw new RuntimeException(e.getMessage());
         }
         boolean success = sendMessage(fileContent);
         if(success) writeSuccessLogFile(file); 
    }
    
    private void writeSuccessLogFile(File processedFile) {
        String prefix = processedFile.getName().substring(0, processedFile.getName().indexOf("."));
        File file = new File(processedFile.getParent() + File.separatorChar + prefix + ".prc");
        try {
            file.createNewFile();
        } catch(IOException ioe) {
            logger.error("Could not create the file processed log after successful processing of Accpac Message");
            ioe.printStackTrace();
        }
    }
    private boolean sendMessage(String message) {
        RegisterPaymentDBResponse response = null;
        try {
            StringTokenizer tok = new StringTokenizer(message, ",");
            String username = tok.nextToken().trim();
            String dealerCode = tok.nextToken().trim();
            String erpReferenceNumber = tok.nextToken().trim();
            String erpOrderId = tok.nextToken().trim();
            String strAmount = tok.nextToken().trim();
            Long amount = new BigDecimal(strAmount).longValue();
            DateFormat format = new SimpleDateFormat("ddMMyyyyHHmmss");
            Date timestamp = format.parse(tok.nextToken().trim());
            response = getAccountService().paymentArrive(dealerCode, erpReferenceNumber, erpOrderId, timestamp, username, amount);
        } catch(Exception e) {
            logger.debug("Error processing Accpac Message:");
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        if(response.getDbResult() == 0) return true;
        return false;
    }

    public String getDirectoryName() {
        return directoryName;
    }

    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }

    public IAccountService getAccountService() {
        return accountService;
    }

    public void setAccountService(IAccountService accountService) {
        this.accountService = accountService;
    }
    
}

    