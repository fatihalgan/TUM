/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.gateway.vtu.adapter;


/**
 *
 * @author db2admin
 */
public class GenericVTUFaultResponseException extends VTUFaultResponseException {
    
    public GenericVTUFaultResponseException(String faultMessage) {
        super(1005, faultMessage);
    }
}
