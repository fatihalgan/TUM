/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.xmlrpc;

import java.io.IOException;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.protocol.BasicHttpContext;


/**
 *
 * @author db2admin
 */
public class RetryHttpMethodExecutor implements HttpMethodExecutor {

    private HttpClient httpClient;
    private int retryCount;
    private int retryPeriod;
    
    public RetryHttpMethodExecutor(HttpClient client, int retryCount, int retryPeriod) {
        this.httpClient = client;
        this.retryCount = retryCount;
        this.retryPeriod = retryPeriod;
        
    }
    
    public HttpResponse executeMethod(HttpPost method) throws IOException {
        boolean retry = true;
        HttpResponse response = null;
        while(retry) {
            try {
            	response = httpClient.execute(method);
                if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) retry = false;
                else {
                    if(retryCount == 0) return response;
                    retryCount--;
                }
            } catch(IOException ioe) {
                if(retryCount == 0) throw ioe;
                retryCount--;
                try {
                    Thread.sleep(retryPeriod);
                } catch(InterruptedException ie) {
                    throw new IOException("Connection reset by XML-RPC Client...");
                }   
            } 
        }
        return response;
    }
    
}
