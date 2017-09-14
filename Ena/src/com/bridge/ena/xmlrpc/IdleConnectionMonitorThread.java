package com.bridge.ena.xmlrpc;

import java.util.concurrent.TimeUnit;

import org.apache.http.conn.ClientConnectionManager;

public class IdleConnectionMonitorThread extends Thread {

	private final ClientConnectionManager connMgr;
	private volatile boolean shutdown;
	    
	public IdleConnectionMonitorThread(ClientConnectionManager connMgr) {
		super();
	    this.connMgr = connMgr;
	}

	@Override
	public void run() {
		try {
			while (!shutdown) {
				synchronized (this) {
					wait(20000);
	                // Close expired connections
	                connMgr.closeExpiredConnections();
	                // Optionally, close connections
	                // that have been idle longer than 30 sec
	                connMgr.closeIdleConnections(30, TimeUnit.SECONDS);
	            }
	        }
	    } catch (InterruptedException ex) {
	    	// terminate
	    }
	}
	    
	public void shutdown() {
		shutdown = true;
	    synchronized (this) {
	    	notifyAll();
	    }
	}
}