/*
 * MSISDNValidator.java
 * 
 * Created on Aug 22, 2007, 10:43:53 AM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.util.jsf;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author db2admin
 */
public class MSISDNValidator implements Validator {

    public void validate(FacesContext facesContext, UIComponent component, Object object) throws ValidatorException {
        String msisdn = (String)object;
        msisdn = msisdn.trim();
        if(msisdn.length() == 0) return;
        if((msisdn.length() == 9)) return;
        FacesMessage msg = new FacesMessage();
        msg.setSummary("MSISDN number must start with 82 - 83 and should be 9 digits long.");
        msg.setDetail("MSISDN number must start with 82 - 83 and should be 9 digits long.");
        msg.setSeverity(FacesMessage.SEVERITY_ERROR);
        throw new ValidatorException(msg);
    }
}
