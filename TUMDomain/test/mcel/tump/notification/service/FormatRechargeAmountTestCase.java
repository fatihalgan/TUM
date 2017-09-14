/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.notification.service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author db2admin
 */
public class FormatRechargeAmountTestCase {

    public FormatRechargeAmountTestCase() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}

    @Test
    public void testFormatRechargeAmount() {
        Float transferAmount = 9.999996f;
        NumberFormat nf = NumberFormat.getIntegerInstance();
        String str = nf.format(transferAmount);
        System.out.println(str);
    }

}