package com.bridge.ena.xmlrpc;

import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class EnaKeyStoreTrustManager implements X509TrustManager {

	X509TrustManager sunJSSEX509TrustManager;
	
	public EnaKeyStoreTrustManager() {
		KeyStore ks = null;
		try {
			ks = KeyStore.getInstance("JKS");  
			ks.load(EnaKeyStoreTrustManager.class.getResourceAsStream("tumcacerts"), "changeit".toCharArray());  
		} catch(Exception e) {
			throw new RuntimeException("Could not load keystore cacerts for SSL trust manager: " + e.getMessage());
		}
		try {
			TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509", "SunJSSE");  
			tmf.init(ks);  
			TrustManager tms [] = tmf.getTrustManagers();
			for (int i = 0; i < tms.length; i++) {  
				if (tms[i] instanceof X509TrustManager) {  
					sunJSSEX509TrustManager = (X509TrustManager) tms[i];  
	                return;  
	            }  
	        }
		} catch(Exception e) {
			throw new RuntimeException("Could not initialize Trust Manager: " + e.getMessage());
		}
		throw new RuntimeException("Could not initialize Trust Manager: No X509 Trust Manager found..");
	}
	
	 /* 
     * Delegate to the default trust manager. 
     */  
    public void checkClientTrusted(X509Certificate[] chain, String authType)  
                throws CertificateException {  
        sunJSSEX509TrustManager.checkClientTrusted(chain, authType);    
    }  
  
    /* 
     * Delegate to the default trust manager. 
     */  
    public void checkServerTrusted(X509Certificate[] chain, String authType)  
                throws CertificateException {  
        try {
        	sunJSSEX509TrustManager.checkServerTrusted(chain, authType);
        } catch(CertificateException ce) {
        	for(X509Certificate cer : chain) {
        		if(cer.getIssuerDN().getName().equals("EMAILADDRESS=falgan@mcel.co.mz, CN=tum.mcel.co.mz, OU=tum, O=mcel, L=Maputo, ST=Maputo, C=MZ")) {
        			return;
        		}
        	}
        	throw new CertificateException(ce);
        }
    }  
  
    /* 
     * Merely pass this through. 
     */  
    public X509Certificate[] getAcceptedIssuers() {  
        return sunJSSEX509TrustManager.getAcceptedIssuers();  
    }  
}


