/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.cs3cp1.adjustment.policy.serializer;

import com.bridge.ena.cs3cp1.adjustment.policy.AdjustmentAmountRestriction;
import com.bridge.ena.cs3cp1.adjustment.policy.AdjustmentRestrictions;
import com.bridge.ena.cs3cp1.adjustment.policy.DedicatedAccountAdjustment;
import com.bridge.ena.cs3cp1.adjustment.policy.DedicatedAccountAdjustmentValue;
import com.bridge.ena.cs3cp1.adjustment.policy.DedicatedAccountAdjustments;
import com.bridge.ena.cs3cp1.adjustment.policy.ServiceClassRestriction;
import com.bridge.ena.xml.Node;
import org.dom4j.Element;

/**
 *
 * @author db2admin
 */
public class TypeFactory {

    public static Node getNode(Element element) {
        if(element.getName().equals("DedicatedAccountAdjustments")) {
            return new DedicatedAccountAdjustments(element);
        }
        if(element.getName().equals("DedicatedAccountAdjustment")) {
            return new DedicatedAccountAdjustment(element);
        } 
        if(element.getName().equals("DedicatedAccountAdjustmentValue")) {
            return new DedicatedAccountAdjustmentValue(element);
        }
        if(element.getName().equals("AdjustmentRestrictions")) {
            return new AdjustmentRestrictions(element);
        }
        if(element.getName().equals("ServiceClassRestriction")) {
            return new ServiceClassRestriction(element);
        }
        if(element.getName().equals("AdjustmentAmountRestriction")) {
            return new AdjustmentAmountRestriction(element);
        }
        return null;
    }
    
    public static boolean isTerminalNode(Element element) {
        if(element.getName().equals("dedicatedAccountID")) return true;
        if(element.getName().equals("startDate")) return true;
        if(element.getName().equals("endDate")) return true;
        if(element.getName().equals("adjustmentBase")) return true;
        if(element.getName().equals("value")) return true;
        if(element.getName().equals("serviceClassID")) return true;
        if(element.getName().equals("minValue")) return true;
        if(element.getName().equals("maxValue")) return true;
        return false;
    }
}
