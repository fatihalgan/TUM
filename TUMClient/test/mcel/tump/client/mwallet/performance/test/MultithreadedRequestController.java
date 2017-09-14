package mcel.tump.client.mwallet.performance.test;

import java.util.concurrent.atomic.AtomicLong;

import mcel.tump.util.cert.ISignatureVerifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.bridge.ena.xmlrpc.IXMLRPCClient;

public class MultithreadedRequestController {

	public static final AtomicLong originTransIdCounter = new AtomicLong(300000001400473L);
	
	ApplicationContext ctx;
	IXMLRPCClient xmlRpcClient;
	ISignatureVerifier signatureVerifier;
	int numOfThreads = 10800;
	int interleaveChunk = 10;
	int interleavePeriod = 1000;
	
	public MultithreadedRequestController() {
		ctx = new FileSystemXmlApplicationContext("D:/RHDSWorkspaces/MCel/TUMP/TUMClient/test/mcel/tump/client/mwallet/performance/test/applicationContext.xml");
		xmlRpcClient = (IXMLRPCClient)ctx.getBean("xmlRpcClient");
		signatureVerifier = (ISignatureVerifier)ctx.getBean("signatureVerifier");
	}
	
	public static void main(String[] args) {
		MultithreadedRequestController controller = new MultithreadedRequestController();
		Thread[] threads = new Thread[controller.numOfThreads];
		
		for(int i = 0; i < controller.numOfThreads; i++) {
			MWalletRequestThread mt = new MWalletRequestThread(controller.xmlRpcClient, controller, controller.signatureVerifier);
			Thread t = new Thread(mt);
			threads[i] = t;
		}
		
		for(int i = 0; i < controller.numOfThreads; i++) {
			threads[i].start();
			try {
				if((i % controller.interleaveChunk) == 0) Thread.sleep(controller.interleavePeriod);
			} catch(InterruptedException ie) {
				
			}
		}
	}
	
}
