/*
 * IClassLoadStrategy.java
 *
 * Created on November 20, 2006, 6:13 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mcel.tump.util;

/**
 *
 * @author db2admin
 */
public interface IClassLoadStrategy {
    ClassLoader getClassLoader (ClassLoadContext ctx);
} // End of interface

