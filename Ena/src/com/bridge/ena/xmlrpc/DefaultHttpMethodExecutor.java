package com.bridge.ena.xmlrpc;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;

public class DefaultHttpMethodExecutor implements HttpMethodExecutor {

	private HttpClient httpClient;
	
	public DefaultHttpMethodExecutor(HttpClient httpClient) {
		this.httpClient = httpClient;
	}
	
	public HttpResponse executeMethod(HttpPost method) throws IOException {
		HttpResponse response = null;
		try {
			response = httpClient.execute(method);
		} catch(IOException ioe) {
			throw new IOException("Timeout during HTTP POST operation: " + ioe.getMessage());
		}
		return response;
	}
}
