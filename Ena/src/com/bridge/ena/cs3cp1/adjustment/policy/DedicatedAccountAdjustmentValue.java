/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.cs3cp1.adjustment.policy;

import com.bridge.ena.cs3cp1.adjustment.policy.serializer.TypeFactory;
import com.bridge.ena.cs3cp1.command.CS3CP1Tokens;
import com.bridge.ena.xml.NonterminalNode;
import com.bridge.ena.xmlrpc.serializer.Member;
import com.bridge.ena.xmlrpc.serializer.Struct;
import java.util.Iterator;
import org.dom4j.Element;

/**
 *
 * @author db2admin
 */
public class DedicatedAccountAdjustmentValue implements NonterminalNode{

    private AdjustmentBase adjustmentBase;
    private float value;
        
    public DedicatedAccountAdjustmentValue() {
        super();
    }
    
    public DedicatedAccountAdjustmentValue(AdjustmentBase adjustmentBase, float value) {
        this.adjustmentBase = adjustmentBase;
        this.value = value;
    }
    
    public DedicatedAccountAdjustmentValue(String adjustmentBase, float value) {
        this.adjustmentBase = AdjustmentBase.valueOf(adjustmentBase);
        this.value = value;
    }
    
    public DedicatedAccountAdjustmentValue(Element element) {
        parseChildren(element);
    }

    public AdjustmentBase getAdjustmentBase() {
        return adjustmentBase;
    }

    public void setAdjustmentBase(AdjustmentBase adjustmentBase) {
        this.adjustmentBase = adjustmentBase;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String toXML(StringBuffer buf) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void parseChildren(Element element) {
        Iterator<Element> it = element.elementIterator();
        while(it.hasNext()) {
            Element child = it.next();
            if(TypeFactory.isTerminalNode(child)) {
                if(child.getName().equals("adjustmentBase")) {
                    setAdjustmentBase(AdjustmentBase.valueOf(child.getText().trim()));
                } else if(child.getName().equals("value")) {
                    setValue(new Float(child.getText().trim()));
                }
            }
        }
    }
    
    public void applyAdjustment(Struct dedicatedAccount, Float mainAdjustment, Float decimalDenominator) {
        if(adjustmentBase.equals(AdjustmentBase.FreeAmount)) {
            Float amount = value * decimalDenominator;
            dedicatedAccount.addMember(new Member(CS3CP1Tokens.AdjustmentAmount.toString(), amount.toString()));
        } else if(adjustmentBase.equals(AdjustmentBase.MainAdjustmentPercentage)) {
            Float adjustmentAmount = ((mainAdjustment * decimalDenominator) * value) / 100;
            dedicatedAccount.addMember(new Member(CS3CP1Tokens.AdjustmentAmount.toString(), adjustmentAmount.toString()));
        }
    }
    
}
