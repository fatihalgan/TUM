/*
 * TUMClientTestCase.java
 * JUnit based test
 *
 * Created on September 14, 2007, 4:12 PM
 */

package mcel.tump.client.test;

import mcel.tump.client.vtu.test.*;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author db2admin
 */
public class AllTests extends TestCase {
    
    public static Test suite() {
        TestSuite suite = new TestSuite("TUMClientTestCase");
        suite.addTestSuite(TUMVTUClientTestCase.class);
        return suite;
    }
    
}
