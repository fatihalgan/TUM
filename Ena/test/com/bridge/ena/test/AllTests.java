/*
 * AllTests.java
 * JUnit 4.x based test
 *
 * Created on August 21, 2007, 1:19 AM
 */

package com.bridge.ena.test;

import com.bridge.ena.cs.adjustment.policy.test.DedicatedAccountAdjustmentPolicyLoaderTestCase;
import junit.framework.TestSuite;
import com.bridge.ena.cs3cp1.test.AIRMessageTestCase;
import com.bridge.ena.cs3cp1.test.AirRefillMessageTestCase;
import com.bridge.ena.vs.test.VSMessageTestCase;
import com.bridge.ena.xmlrpc.serializer.test.MethodCallTestCase;
import com.bridge.ena.xmlrpc.serializer.test.MethodResponseTestCase;
import com.bridge.ena.xmlrpc.serializer.test.XMLRPCClientTestCase;
import static org.junit.Assert.*;

/**
 *
 * @author db2admin
 */
public class AllTests {

    public static TestSuite suite() {
        TestSuite suite = new TestSuite("Tests for Ena class library");
        //$JUnit-BEGIN$
        suite.addTestSuite(MethodCallTestCase.class);
        suite.addTestSuite(MethodResponseTestCase.class);
        suite.addTestSuite(XMLRPCClientTestCase.class);
        suite.addTestSuite(DedicatedAccountAdjustmentPolicyLoaderTestCase.class);
        suite.addTestSuite(AIRMessageTestCase.class);
        suite.addTestSuite(VSMessageTestCase.class);
        suite.addTestSuite(AirRefillMessageTestCase.class);
        return suite;
    }
}
