/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.gateway.util;

/**
 *
 * @author db2admin
 */
public interface TUMResponseMessage {

    public void generateFaultResponse(Integer resultCode, String requestTransactionId);
    public String toXML();
}
