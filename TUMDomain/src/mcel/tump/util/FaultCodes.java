/*
 * FaultCodes.java
 * 
 * Created on Sep 27, 2007, 12:57:43 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 *
 * @author db2admin
 */
public class FaultCodes {

    private static final String BUNDLE_NAME = "faultcodes"; //$NON-NLS-1$

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    private FaultCodes() {
    
    }

    public static String getString(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return '!' + "KeyNotFound:" + key + '!';
        }
    }
}
