package com.bridge.ena.xmlrpc;

import java.net.Socket;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.X509ExtendedTrustManager;
import javax.net.ssl.X509TrustManager;

public class EnaTrustManager implements X509TrustManager {

	@Override
	public void checkClientTrusted(X509Certificate[] arg0, String arg1)
			throws CertificateException {
		
		
	}

	@Override
	public void checkServerTrusted(X509Certificate[] arg0, String arg1)
			throws CertificateException {
		for(X509Certificate cert : arg0) {
			try {
				cert.verify(cert.getPublicKey());
			} catch(Exception e) {
				throw new CertificateException("Could not verify the public key of the certificate: " + e.getMessage());
			}
		}
	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return null;
	}

	
}
