/*
 * AllTests.java
 * JUnit 4.x based test
 *
 * Created on August 21, 2007, 1:19 AM
 */

package mcel.tump.test;

import junit.framework.TestSuite;
import mcel.tump.gateway.handler.test.TUMPHandlerTestCase;

/**
 *
 * @author db2admin
 */
public class AllTests {

    public static TestSuite suite() {
        TestSuite suite = new TestSuite("Test for mcel.tump.gateway.test");
        //$JUnit-BEGIN$
        suite.addTestSuite(TUMPHandlerTestCase.class);
        //suite.addTestSuite(SMPPTestCase.class);
        //$JUnit-END$
        return suite;
    }
}
