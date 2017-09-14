/*
 * MethodResponseTestCase.java
 * JUnit 4.x based test
 *
 * Created on September 3, 2007, 6:25 PM
 */

package com.bridge.ena.xmlrpc.serializer.test;

import java.io.FileInputStream;
import junit.framework.TestCase;
import com.bridge.ena.xmlrpc.serializer.Fault;
import com.bridge.ena.xmlrpc.serializer.MethodResponse;
import com.bridge.ena.xmlrpc.serializer.Serializer;
import com.bridge.ena.xmlrpc.serializer.Struct;
import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 *
 * @author db2admin
 */
public class MethodResponseTestCase extends TestCase {
    
    String path = "D://NetBeansWorkspaces//MCel//TUMP//Ena//test//com//bridge//ena//xmlrpc//serializer//test//";
    
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    public void testSimpleFaultResponse() throws Exception {
        FileInputStream file = new FileInputStream(path + "faultResponse.xml");
        Serializer ser = new Serializer(file);
        MethodResponse response = (MethodResponse)ser.parse();
        Fault fault = response.getFault();
        assertEquals(fault.getFault().getValue("faultCode"), new Integer(1001));
        assertNull(response.getMembers());
    }

    public void testComplexMethodResponse() throws Exception {
        FileInputStream file = new FileInputStream(path + "complexMethodResponse.xml");
        Serializer ser = new Serializer(file);
        MethodResponse response = (MethodResponse)ser.parse();
        assertEquals(response.getMembers().getValue("responseCode"), new Integer(0));
        Struct members = response.getMembers();
        assertEquals(members.getValue("currentLanguageID"), new Integer(3));
        members = response.getMembers().getValue("AccumulatorInformation.accumulatorID", new Integer(2));
        assertNotNull(members);
        assertEquals(members.getValue("accumulatorValue"), new Integer(3));
    }
    
    public void testGetAccountDetailsResponse() throws Exception {
        FileInputStream file = new FileInputStream(path + "accountDetailsResponse.xml");
        Serializer ser = new Serializer(file);
        MethodResponse response = (MethodResponse)ser.parse();
        assertEquals(response.getMembers().getValue("responseCode"), new Integer(0));
        Struct members = response.getMembers();
        assertNotNull(members);
        assertEquals(members.getValue("accountValue1"), "25000");
    }
    
    public void testGetBase64SimpleResponse() throws Exception {
        FileInputStream file = new FileInputStream(path + "simpleResponseWithBase64.xml");
        Serializer ser = new Serializer(file);
        MethodResponse response = (MethodResponse)ser.parse();
        byte[] signature = (byte[])response.getMembers().getValue("Signature");
        assertEquals(new String(signature), "eJTk9/HCpmQkoan5PPAcELThfIZKxEPyRbZyb6FxqDT5N0PvEsEo3bwV/RXC6Fip4wdrQGhy9/68+8gHjunNG91FENEpmxns4LmHBsg1LdHNN+xy/pMttFkSdzEG3/2+bAA6UX4L/90hIwLCLIR1L1fTjOvmGTSuyqtEylHvddU=");
    }
    
    public void testUpdateVoucherStateResponse() throws Exception {
    	FileInputStream file = new FileInputStream(path + "updateVoucherStateResponse.xml");
        Serializer ser = new Serializer(file);
        MethodResponse response = (MethodResponse)ser.parse();
        assertEquals(response.getMembers().getValue("responseCode"), new Integer(0));
        Struct members = response.getMembers();
        assertNotNull(members);
    }
}
