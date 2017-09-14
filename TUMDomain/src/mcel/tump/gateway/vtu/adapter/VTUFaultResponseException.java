/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.gateway.vtu.adapter;

/**
 *
 * @author db2admin
 */
public abstract class VTUFaultResponseException extends RuntimeException {

    protected Integer faultCode;
    protected String faultMessage;
    
    public VTUFaultResponseException(Integer faultCode, String faultMessage) {
        this.faultCode = faultCode;
        this.faultMessage = faultMessage;
    }
    
    public Integer getFaultCode() {
        return faultCode;
    }

    public void setFaultCode(Integer faultCode) {
        this.faultCode = faultCode;
    }

    public String getFaultMessage() {
        return faultMessage;
    }

    public void setFaultMessage(String faultMessage) {
        this.faultMessage = faultMessage;
    }
        
}
