/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.cs.adjustment.policy.test;

import com.bridge.ena.cs3cp1.adjustment.policy.DedicatedAccountAdjustment;
import com.bridge.ena.cs3cp1.adjustment.policy.DedicatedAccountAdjustmentPolicyLoader;
import com.bridge.ena.cs3cp1.adjustment.policy.DedicatedAccountAdjustmentValue;
import com.bridge.ena.cs3cp1.adjustment.policy.DedicatedAccountAdjustments;
import java.util.Iterator;
import junit.framework.TestCase;

/**
 *
 * @author db2admin
 */
public class DedicatedAccountAdjustmentPolicyLoaderTestCase extends TestCase {
    
    public void setUp() {
        
    }
    
    public void testLoadDedicatedAccountAdjustmentPolicy() {
        DedicatedAccountAdjustments adjustments = DedicatedAccountAdjustmentPolicyLoader.instance().getDedicatedAccountAdjustments(100f, 100f);
        assertNotNull(adjustments);
        assertNotSame(0, adjustments.getAdjustments().size());
    }
    
    public void testDedicatedAccountAdjustmentValues() {
        DedicatedAccountAdjustments adjustments = DedicatedAccountAdjustmentPolicyLoader.instance().getDedicatedAccountAdjustments(100f, 100f);
        Iterator<DedicatedAccountAdjustment> it = adjustments.getAdjustments().iterator();
        while(it.hasNext()) {
            DedicatedAccountAdjustment adjustment = it.next();
            assertNotSame(0, adjustment.getDedicatedAccountID());
            assertNotNull(adjustment.getStartDate());
            assertNotNull(adjustment.getEndDate());
            DedicatedAccountAdjustmentValue value = adjustment.getValue();
            assertNotNull(value.getAdjustmentBase());
            assertNotNull(value.getValue());
        }
    }
}
