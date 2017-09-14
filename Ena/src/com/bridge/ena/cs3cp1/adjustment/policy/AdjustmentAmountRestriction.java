/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.cs3cp1.adjustment.policy;

import com.bridge.ena.cs3cp1.adjustment.policy.serializer.TypeFactory;
import com.bridge.ena.xml.NonterminalNode;
import java.util.Iterator;
import org.dom4j.Element;

/**
 *
 * @author db2admin
 */
public class AdjustmentAmountRestriction implements AdjustmentRestriction, NonterminalNode {

    private Float minValue;
    private Float maxValue;
    
    public AdjustmentAmountRestriction() {
        super();
    }
    
    public AdjustmentAmountRestriction(Float minAmount, Float maxAmount) {
        this.minValue = minAmount;
        this.maxValue = maxAmount;
    } 
    
    public AdjustmentAmountRestriction(Element element) {
        parseChildren(element);
    }
    public boolean adjustmentAllowed(String currentServiceClassID, Float mainAdjustmentAmount) {
        if((mainAdjustmentAmount >= minValue) && (mainAdjustmentAmount <= maxValue)) return true;
        return false;
    }

    public String toXML(StringBuffer buf) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void parseChildren(Element element) {
        Iterator<Element> it = element.elementIterator();
        while(it.hasNext()) {
            Element child = it.next();
            if(TypeFactory.isTerminalNode(child)) {
                if(child.getName().equals("minValue")) {
                    setMinValue(new Float(child.getText().trim()));
                } else if(child.getName().equals("maxValue")) {
                    setMaxValue(new Float(child.getText().trim()));
                }
            }
        }
    }

    public Float getMinValue() {
        return minValue;
    }

    public void setMinValue(Float minValue) {
        this.minValue = minValue;
    }

    public Float getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Float maxValue) {
        this.maxValue = maxValue;
    }
}
