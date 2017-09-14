/*
 * CertificateGenerator.java
 * 
 * Created on Aug 23, 2007, 1:17:22 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.util.cert;

import java.io.FileInputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.Certificate;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 *
 * @author db2admin
 */
public class SignatureVerifier implements ISignatureVerifier {

    private KeyStore keystore = null;
    private String keystorePath = null;
    private String keyPass = null;
    private String keyAlias = null;
    private String certAlias = null;
    private String algorithm = "MD5withRSA";
    private PrivateKey priv = null;
    private Certificate certificate;
    private static Log logger = LogFactory.getLog(SignatureVerifier.class);
    
    public SignatureVerifier(String keystorePath, String keyPass, String keyAlias, String certAlias)  {
        this.keystorePath = keystorePath;
        this.keyPass = keyPass;
        this.keyAlias = keyAlias;
        this.certAlias = certAlias;
        try {
            FileInputStream fisk = new FileInputStream(keystorePath);
            logger.debug("Loading keystore..");
            keystore = KeyStore.getInstance("JKS", "SUN");
            keystore.load(fisk, keyPass.toCharArray());
            priv = (PrivateKey)keystore.getKey(keyAlias, keyPass.toCharArray());
        } catch(Exception e) {
            throw new RuntimeException("Could not load the private key for alias " + keyAlias + ": " + e.getMessage());
        }
        try {
        	certificate = keystore.getCertificate(certAlias);
        } catch(Exception e) {
        	throw new RuntimeException("Could not load the certificate for alias " + certAlias + ": " + e.getMessage());
        }	
    }
    
    public SignatureVerifier(String keystorePath, String keyPass, String keyAlias, String certAlias, String algorithm) {
    	this(keystorePath, keyPass, keyAlias, certAlias);
    	this.algorithm = algorithm;
    }
    
    private Certificate getCertificate() {
    	return certificate;
    }
        
    public boolean verify(String alias, String signature, String data) {
        try {
            logger.debug("Verifying signature");
            Certificate cert = getCertificate();
            PublicKey pubkey = cert.getPublicKey();
            byte[] sigToVerify = signature.getBytes();
            byte[] decodedSignature = Base64.decodeBase64(sigToVerify);
            logger.debug("Signature is: " + new String(decodedSignature));
            Signature sig = Signature.getInstance(algorithm);
            sig.initVerify(pubkey);
            logger.debug("Data to be verified is: " + data);
            sig.update(data.getBytes());
            return sig.verify(decodedSignature);
        } catch(Exception e) {
            logger.error("Could not verify signature");
            return false;
        }
    }
    
    public byte[] sign(String data) {
        try { 
            //Initialize the signature
            logger.debug("Data to be signed is: " + data);
            logger.debug("Signing the message");
            Signature sig = Signature.getInstance(algorithm);
            sig.initSign(priv);
            sig.update(data.getBytes());
            byte[] signedData = sig.sign();
            logger.debug("Signature data is: " + new String(signedData));
            byte[] encodedSignature = Base64.encodeBase64(signedData);
            logger.debug("Encoded signature is: " + new String(encodedSignature));
            return encodedSignature;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}
