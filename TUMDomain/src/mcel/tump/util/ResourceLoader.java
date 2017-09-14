/*
 * ResourceLoader.java
 *
 * Created on November 20, 2006, 6:12 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mcel.tump.util;

import java.net.URL;

/**
 *
 * @author db2admin
 */

public abstract class ResourceLoader {
    /**
     * @see java.lang.ClassLoader#loadClass(java.lang.String)
     */
    public static Class loadClass (final String name)
        throws ClassNotFoundException
    {
        final ClassLoader loader = ClassLoaderResolver.getClassLoader();
        
        return Class.forName (name, false, loader);
    }
    /**
     * @see java.lang.ClassLoader#getResource(java.lang.String)
     */    
    public static URL getResource (final String name)
    {
        final ClassLoader loader = ClassLoaderResolver.getClassLoader();
        
        if (loader != null)
            return loader.getResource (name);
        else
            return ClassLoader.getSystemResource (name);
    }
} // End of class
