/*
 * Messages.java
 *
 * Created on November 20, 2006, 10:41 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mcel.tump.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 *
 * @author db2admin
 */
public class Messages {
    
    private static final String BUNDLE_NAME = "Messages"; //$NON-NLS-1$

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    private Messages() {
    
    }

    public static String getString(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return '!' + "KeyNotFound:" + key + '!';
        }
    }
    
}
