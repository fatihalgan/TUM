/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.gateway.handler;

import mcel.tump.gateway.util.TUMResponseCodes;

/**
 *
 * @author db2admin
 */
public class TUMRequestHandlerException extends Exception {

    private String requestTransactionId;
    private Integer responseCode;
    
    public TUMRequestHandlerException(Integer responseCode, String requestTransactionId) {
        this.requestTransactionId = requestTransactionId;
        this.responseCode = responseCode;
    }

    public String getRequestTransactionId() {
        return requestTransactionId;
    }

    public Integer getResponseCode() {
        return responseCode;
    }
    
    public String getMessage() {
    	return TUMResponseCodes.getResponseCode(responseCode).toString();
    }
    
}
