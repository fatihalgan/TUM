/*
 * XMLRPCClient.java
 *
 * Created on Aug 27, 2007, 1:48:21 PM
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.xmlrpc;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.SecureRandom;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRoute;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;

import com.bridge.ena.xmlrpc.exception.XmlRpcConnectionException;

/**
 *
 * @author db2admin
 */
public class XMLRPCClient implements IXMLRPCClient {

    private static final Log logger = LogFactory.getLog(XMLRPCClient.class);
    private String targetUrl;
    private int maxConnections;
    private String userAgentRequestHeader;
    private String username = null;
    private String password = null;
    private int retryCount = 0;
    private int retryPeriod = 0;
    private static final int defaultConTimeout = 5000;
    private static final int defaultSockTimeout = 10000;
    private String authorizationString;
    private DefaultHttpClient httpClient;

    public XMLRPCClient(String targetUrl, int maxConnections, String userAgentRequestHeader, int retryCount, int retryPeriod, int connectionTimeout, int soTimeout) {
    	this.targetUrl = targetUrl;
        this.maxConnections = maxConnections;
        this.userAgentRequestHeader = userAgentRequestHeader;
        this.retryCount = retryCount;
        this.retryPeriod = retryPeriod;
    	URI uri = null;
        try {
         uri = new URI(targetUrl);
        } catch(URISyntaxException e) {
        	logger.error("Invalid URI syntax: " + targetUrl);
        	throw new RuntimeException(e.getMessage());        
        }
        // Create and initialize scheme registry 
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), uri.getPort()));
        try {
        	SSLContext sslContext = SSLContext.getInstance("TLS");
        	sslContext.init(null, new TrustManager[] {new EnaKeyStoreTrustManager()}, new SecureRandom());
        	SSLSocketFactory sf = new SSLSocketFactory(sslContext);
        	sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        	Scheme httpsScheme = new Scheme("https", sf, 443);
        	schemeRegistry.register(httpsScheme);
        } catch(Exception e) {
        	logger.error("Failed to register HTTPS scheme: " + e.getMessage());
        }
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, connectionTimeout);
        HttpConnectionParams.setStaleCheckingEnabled(params, true);
        HttpConnectionParams.setSoTimeout(params, soTimeout);
        ConnManagerParams.setMaxTotalConnections(params, this.maxConnections);
        ConnManagerParams.setMaxConnectionsPerRoute(params, new ConnPerRouteBean(this.maxConnections));
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);
        this.httpClient = new DefaultHttpClient(cm, params);
        this.httpClient.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(0, false));
        //this.httpClient.setKeepAliveStrategy(new AIRConnectionKeepAliveStrategy());
    }
    
    
    public XMLRPCClient(String targetUrl, int maxConnections, String userAgentRequestHeader, int retryCount, int retryPeriod) {
        this(targetUrl, maxConnections, userAgentRequestHeader, retryCount, retryPeriod, defaultConTimeout, defaultSockTimeout);
    }
    
    public XMLRPCClient(String targetUrl, int maxConnections, String userAgentRequestHeader, String authorizationString, int retryCount, int retryPeriod, int connectionTimeout, int soTimeout) {
        this(targetUrl, maxConnections, userAgentRequestHeader, retryCount, retryPeriod, connectionTimeout, soTimeout);
    	this.authorizationString = authorizationString;
    }
    
    public XMLRPCClient(String targetUrl, int maxConnections, String userAgentRequestHeader, String authorizationString, int retryCount, int retryPeriod) {
        this(targetUrl, maxConnections, userAgentRequestHeader, retryCount, retryPeriod);
    	this.authorizationString = authorizationString;
    }
    
    public XMLRPCClient(String targetUrl, int maxConnections, String userAgentRequestHeader,
        String username, String password, int retryCount, int retryPeriod, int connectionTimeout, int soTimeout) {
    	this(targetUrl, maxConnections, userAgentRequestHeader, retryCount, retryPeriod, connectionTimeout, soTimeout);
        this.username = username;
        this.password = password;
        if(username != null && password != null) {
        	httpClient.getCredentialsProvider().setCredentials(AuthScope.ANY,
            new UsernamePasswordCredentials(username, password));
        }
    }
    
    public XMLRPCClient(String targetUrl, int maxConnections, String userAgentRequestHeader,
    		String username, String password, int retryCount, int retryPeriod) {
    	this(targetUrl, maxConnections, userAgentRequestHeader, username, password, retryCount, retryCount, defaultConTimeout, defaultSockTimeout);
    }

    public String sendMessage(String xmlMessage) throws XmlRpcConnectionException {
        HttpResponse response = null;
        String strResponse = null;
        HttpPost postMethod = new HttpPost(targetUrl);
        postMethod.setHeader("User-Agent", userAgentRequestHeader);
        postMethod.setHeader("Content-type", "text/xml");
        if(authorizationString != null) postMethod.setHeader("Authorization", authorizationString);
        try {
        	HttpEntity entity = new StringEntity(xmlMessage);
        	postMethod.setEntity(entity);
        } catch(UnsupportedEncodingException e) {
        	logger.error("Unsupporting encoding type in XMLRPC Client: " + e.getMessage());
        	throw new RuntimeException(e.getMessage());
        }
        int status = 0;
        try {
            logger.debug("Sending XML-RPC Message...");
            logger.trace(xmlMessage);
            HttpMethodExecutor executor = new DefaultHttpMethodExecutor(httpClient);
            response = executor.executeMethod(postMethod);
            logger.debug("Successfully sent XML-RPC Message...");
        } catch(IOException e) {
            postMethod.abort();
            throw new XmlRpcConnectionException("Could not execute XMLRPC call to host: " + targetUrl + ".  Exception detail is: " + e.getMessage(), HttpStatus.SC_REQUEST_TIMEOUT);
        }
        if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
        	postMethod.abort();
            throw new XmlRpcConnectionException("Http Status error " + status + " returned from XML-RPC call to host: " + targetUrl, status);
        }
        try {
        	HttpEntity entity = response.getEntity();
            if (entity != null) {
                byte[] bytes = EntityUtils.toByteArray(entity);
                strResponse = new String(bytes);
            }
        	logger.debug("Received Response Message...");
            logger.trace(strResponse);
        } catch(IOException e) {
            postMethod.abort();
        	throw new XmlRpcConnectionException("Error in getting XMLRPC call response from host: " + targetUrl + ".  Exception detail is: " + e.getMessage(), HttpStatus.SC_SERVICE_UNAVAILABLE);
        } 
        return strResponse;
    }

    public void setAuthentication(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
