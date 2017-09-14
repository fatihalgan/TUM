/*
 * ClassLoadContext.java
 *
 * Created on November 20, 2006, 6:14 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mcel.tump.util;

/**
 *
 * @author db2admin
 */

public class ClassLoadContext {
    private final Class m_caller;
    
    public final Class getCallerClass () {
        return m_caller;
    }
    
    ClassLoadContext (final Class caller) {
        m_caller = caller;
    }
} // End of class
