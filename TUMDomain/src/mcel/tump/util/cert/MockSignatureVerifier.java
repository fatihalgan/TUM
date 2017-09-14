/*
 * MockSignatureVerifier.java
 * 
 * Created on Sep 4, 2007, 12:37:42 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.util.cert;

import mcel.tump.util.cert.ISignatureVerifier;

/**
 *
 * @author db2admin
 */
public class MockSignatureVerifier implements ISignatureVerifier {

    public MockSignatureVerifier() {
        super();
    }

    public boolean verify(String alias, String signature, String data) {
        return true;
    }

    public byte[] sign(String data) {
        return data.getBytes();
    }

}
