package com.bridge.ena.xmlrpc;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;

public interface HttpMethodExecutor {

	public HttpResponse executeMethod(HttpPost method) throws IOException;
}
