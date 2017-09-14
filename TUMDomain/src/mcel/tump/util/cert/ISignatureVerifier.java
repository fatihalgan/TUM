/*
 * ISignatureVerifier.java
 * 
 * Created on Aug 23, 2007, 1:50:13 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.util.cert;

/**
 *
 * @author db2admin
 */
public interface ISignatureVerifier {
    public boolean verify(String alias, String signature, String data);
    public byte[] sign(String data);
}
