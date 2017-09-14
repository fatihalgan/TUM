/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.cs3cp1.adjustment.policy;

import com.bridge.ena.cs3cp1.adjustment.policy.serializer.Serializer;
import java.io.InputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author db2admin
 */
public class DedicatedAccountAdjustmentPolicyLoader {

    private static DedicatedAccountAdjustmentPolicyLoader instance = null;
    private static final Log logger = LogFactory.getLog(DedicatedAccountAdjustmentPolicyLoader.class);
    private Serializer serializer = null;
    
    private DedicatedAccountAdjustmentPolicyLoader() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream input = loader.getResourceAsStream("daadjustmentpolicy.xml");
        if(input == null) {
            logger.warn("Could not located daadjustmentpolicy.xml file in classpath. No DedicatedAccount adjustments will be done.");
            return;
        }
        try {    
            serializer = new Serializer(input);
            logger.info("Loaded DedicatedAccount adjustments...");
        } catch(Exception e) {
            logger.error("BadXmlFormat...Could not parse DedicatedAccount adjustments information..");
            throw new RuntimeException(e);
        }
    }
    
    public static DedicatedAccountAdjustmentPolicyLoader instance() {
        if(instance == null) {
            instance = new DedicatedAccountAdjustmentPolicyLoader();
        }
        return instance;
    }
    
    public DedicatedAccountAdjustments getDedicatedAccountAdjustments(Float mainAdjustmentAmount, Float decimalDenominator) {
        if(serializer == null) return null;
        DedicatedAccountAdjustments dedicatedAccountAdjustments = (DedicatedAccountAdjustments)serializer.parse();
        dedicatedAccountAdjustments.setMainAdjustmentAmonut(mainAdjustmentAmount);
        dedicatedAccountAdjustments.setDecimalDenominator(decimalDenominator);
        return dedicatedAccountAdjustments;
    }
}
