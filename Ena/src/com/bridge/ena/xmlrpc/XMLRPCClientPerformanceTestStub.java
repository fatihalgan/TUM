/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.xmlrpc;

import com.bridge.ena.xmlrpc.exception.XmlRpcConnectionException;
import java.io.BufferedReader;
import java.io.FileReader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author db2admin
 */
public class XMLRPCClientPerformanceTestStub implements IXMLRPCClient {

    private static final Log logger = LogFactory.getLog(XMLRPCClientPerformanceTestStub.class);
    private String adjustmentResponse = "";
    private String balanceEnquiryResponse = "";
    private String accountDetailsResponse = "";
            
    public XMLRPCClientPerformanceTestStub(String adjustmentRequestResponseFilePath,
            String balanceEnquiryRequestResponseFilePath, String accountDetailsResponseFilePath) {
        super();
        initAdjustmentResponse(adjustmentRequestResponseFilePath);
        initBalanceEnquiryResponse(balanceEnquiryRequestResponseFilePath);
        initAccountDetailsResponse(accountDetailsResponseFilePath);
    }
    
    private void initAdjustmentResponse(String adjustmentRequestResponseFilePath) {
        try {
            FileReader adjustmentReader = new FileReader(adjustmentRequestResponseFilePath);
            BufferedReader buf = new BufferedReader(adjustmentReader);
            String line = "";
            while((line = buf.readLine()) != null) {
                adjustmentResponse = adjustmentResponse + line;
            }
        } catch(Exception e) {
            logger.error("Could not initialize AdjustmentResponse");
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
    
    private void initBalanceEnquiryResponse(String balanceEnquiryRequestResponseFilePath) {
        try {
            FileReader balanceEnquiryReader = new FileReader(balanceEnquiryRequestResponseFilePath);
            BufferedReader buf = new BufferedReader(balanceEnquiryReader);
            String line = "";
            while((line = buf.readLine()) != null) {
                balanceEnquiryResponse = balanceEnquiryResponse + line;
            }
        } catch(Exception e) {
            logger.error("Could not initialize BalanceEnquiryResponse");
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
    
    private void initAccountDetailsResponse(String accountDetailsResponseFilePath) {
        try {
            FileReader accountDetailsReader = new FileReader(accountDetailsResponseFilePath);
            BufferedReader buf = new BufferedReader(accountDetailsReader);
            String line = "";
            while((line = buf.readLine()) != null) {
                accountDetailsResponse = accountDetailsResponse + line;
            }
        } catch(Exception e) {
            logger.error("Could not initialize BalanceEnquiryResponse");
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
    
    public String sendMessage(String xmlMessage) throws XmlRpcConnectionException {
        if(xmlMessage.contains("BalanceEnquiryTRequest")) {
            return balanceEnquiryResponse;
        } else if(xmlMessage.contains("AdjustmentTRequest")) {
            return adjustmentResponse;
        } else if(xmlMessage.contains("GetAccountDetailsTRequest")) {
            return accountDetailsResponse;
        } else {
            throw new RuntimeException("Invalid XML-RPC Message..");
        }
    }

    public void setAuthentication(String username, String password) {
        return;
    }

}
