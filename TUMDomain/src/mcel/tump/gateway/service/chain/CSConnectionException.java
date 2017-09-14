/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.gateway.service.chain;

/**
 *
 * @author db2admin
 */
public class CSConnectionException extends RuntimeException {
    
    private int httpStatusCode;
    
    public CSConnectionException(int statusCode) {
        this.httpStatusCode = statusCode;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }
    
}
