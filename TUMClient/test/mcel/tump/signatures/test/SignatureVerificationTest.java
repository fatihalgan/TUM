package mcel.tump.signatures.test;

import org.junit.Before;
import org.junit.Test;

import mcel.tump.util.cert.ISignatureVerifier;
import mcel.tump.util.cert.SignatureVerifier;
import junit.framework.TestCase;

public class SignatureVerificationTest extends TestCase {

	private ISignatureVerifier clientSignatureVerifier;
	private ISignatureVerifier serverSignatureVerifier;
	
	private String data = "testTUM";
	//private String data = "Hello World!";
		
	private String givenEncodedSignature = "lFadSCcrMeqtydJHPeQqtoaeyuzakiaG4uSHPVD2cYJfrovXWthutI5pu/sqOOCzhqqY19yMl7wg1M6uyHuK9+28ksJLt1+oX5S+koa/bMzgZljb7m4Bh3PwkY1kFMzmJJEsw7V7NxzAlGpVjK05szxkLQg4wMk1nzWFTjphUNNN3QhDwQmpdGtqtZqvDdRnk9Y/JCM6RtmqtOdgpK1628ZZzjacumZ/y/x2oukODNLbA0PDj2wfjkkxDZpmFS9CcJ8+8oNo7XgRL+3osNccTmk7dTSABDcaFv6KSiRL5AHXii+w4T0b3wUNCtygS5XwcxfAb56bdXjoq/MFz99n5g==";	
	//private String givenEncodedSignature = "if1/sS9RARtPj2P5aQZ6mNzafaB0b0oAWxd251yYQJgaSSthSLpCUTzDFntl7BtqKvkG+nkT8FcU/MOUUutzZ5Sa6VsJmm//7Yo43Poipjnad4oyF2hxMJvlOL+QO/zd/7vikEATd28exzFg51/IbQc3k3Zd7CIFXsgrAU9Yq7o=";
	
	private String clientKeystorePath = "C://TUMKeystore//.keystore";
	private String serverKeystorePath = "C://TUMKeystore//.keystore";
	
	private String clientAlias = "mbim";
	private String serverAlias = "tump2014";
	
	private String clientPassword = "madcoder";
	private String serverPassword = "madcoder";
	
	@Before
	public void setUp() throws Exception {
		clientSignatureVerifier = new SignatureVerifier(clientKeystorePath, clientPassword, clientAlias, serverAlias, "MD5withRSA");
		serverSignatureVerifier = new SignatureVerifier(serverKeystorePath, serverPassword, serverAlias, clientAlias, "MD5withRSA");
	}
	
	/**
	@Test
	public void testRequestFromClientToServer() throws Exception {
		byte[] signature = clientSignatureVerifier.sign(data);
		System.out.println("Encoded signature from client to server is: " + new String(signature));
		System.out.print("Binary format of signature is: "); 
		writeBinrayContent(Base64.decodeBase64(signature));
		System.out.print("\n");
		boolean result = serverSignatureVerifier.verify(null, new String(signature), data);
		assertTrue(result);
	}
	**/
	
	
	@Test
	public void testRequestFromClientToServerWithGivenEncodedSignature() throws Exception {
		boolean result = serverSignatureVerifier.verify(serverAlias, givenEncodedSignature, data);
		assertTrue(result);
	}
	
	
	/**
	@Test
	public void testResponseFromServerToClient() throws Exception {
		byte[] signature = serverSignatureVerifier.sign(data);
		System.out.println("Encoded signature from server to client is: " + new String(signature));
		System.out.print("Binary format of signature is: "); 
		writeBinrayContent(Base64.decodeBase64(signature));
		System.out.print("\n");
		boolean result = clientSignatureVerifier.verify(null, new String(signature), data);
		assertTrue(result);
	}
	**/
	
	private void writeBinrayContent(byte[] content) {
		for(int i = 0; i < content.length; i++) {
			System.out.print(content[i] + " | ");
		}
	}
}
