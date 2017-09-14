/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.cs3cp1.adjustment.policy;

import com.bridge.ena.cs3cp1.adjustment.policy.serializer.TypeFactory;
import com.bridge.ena.xml.Node;
import com.bridge.ena.xml.NonterminalNode;
import com.bridge.ena.xmlrpc.serializer.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.dom4j.Element;

/**
 *
 * @author db2admin
 */
public class DedicatedAccountAdjustments implements NonterminalNode {

    private List<DedicatedAccountAdjustment> adjustments = new ArrayList<DedicatedAccountAdjustment>();
    private float mainAdjustmentAmonut = 0f;
    private float decimalDenominator = 0f;
    private String currentServiceClass = null;
    
    public DedicatedAccountAdjustments() {
        super();
    }
    
    public DedicatedAccountAdjustments(List<DedicatedAccountAdjustment> adjustments) {
        this();
        this.setAdjustments(adjustments);
    }
    
    public DedicatedAccountAdjustments(List<DedicatedAccountAdjustment> adjustments, float mainAdjustmentAmount,
            float decimalDenominator, String currentServiceClass) {
        this(adjustments);
        this.mainAdjustmentAmonut = mainAdjustmentAmount;
        this.decimalDenominator = decimalDenominator;
        this.currentServiceClass = currentServiceClass;
    }
    
    public DedicatedAccountAdjustments(Element element) {
        this();
        parseChildren(element);
    }
    
    public void addAdjustment(DedicatedAccountAdjustment adjustment) {
        getAdjustments().add(adjustment);
    }

    public List<DedicatedAccountAdjustment> getAdjustments() {
        return adjustments;
    }

    public void setAdjustments(List<DedicatedAccountAdjustment> adjustments) {
        this.adjustments = adjustments;
    }

    public String toXML(StringBuffer buf) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void parseChildren(Element element) {
       Iterator<Element> it = element.elementIterator();
       while(it.hasNext()) {
           Element child = it.next();
           Node node = TypeFactory.getNode(child);
           if(node instanceof DedicatedAccountAdjustment) {
               addAdjustment((DedicatedAccountAdjustment)node);
           }
       }
    }
    
    public void applyAdjustments(Array dedicatedAccount) {
        Iterator<DedicatedAccountAdjustment> it = adjustments.iterator();
        while(it.hasNext()) {
            DedicatedAccountAdjustment adjustment = it.next();
            adjustment.applyAdjustment(dedicatedAccount, mainAdjustmentAmonut,getDecimalDenominator(),
                  currentServiceClass);
        }
    }

    public float getMainAdjustmentAmonut() {
        return mainAdjustmentAmonut;
    }

    public void setMainAdjustmentAmonut(float mainAdjustmentAmonut) {
        this.mainAdjustmentAmonut = mainAdjustmentAmonut;
    }

    public float getDecimalDenominator() {
        return decimalDenominator;
    }

    public void setDecimalDenominator(float decimalDenominator) {
        this.decimalDenominator = decimalDenominator;
    }

    public String getCurrentServiceClass() {
        return currentServiceClass;
    }

    public void setCurrentServiceClass(String currentServiceClass) {
        this.currentServiceClass = currentServiceClass;
    }
    
}
