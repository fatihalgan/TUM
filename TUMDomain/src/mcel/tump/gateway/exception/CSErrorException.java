/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.gateway.exception;

/**
 *
 * @author db2admin
 */
public class CSErrorException extends RuntimeException {

    private String requestTransactionId;
    private Integer responseCode;
    
    public CSErrorException(String requestTransactionId, Integer responseCode) {
        this.requestTransactionId = requestTransactionId;
        this.responseCode = responseCode;
    }

    public String getRequestTransactionId() {
        return requestTransactionId;
    }

    public Integer getResponseCode() {
        return responseCode;
    }
    
}
