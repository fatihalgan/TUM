/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.cs3cp1.adjustment.policy;

import com.bridge.ena.cs3cp1.adjustment.policy.serializer.TypeFactory;
import com.bridge.ena.xml.Node;
import com.bridge.ena.xml.NonterminalNode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.dom4j.Element;

/**
 *
 * @author db2admin
 */
public class AdjustmentRestrictions implements NonterminalNode {
    
    private List<AdjustmentRestriction> serviceClassRestrictions = new ArrayList<AdjustmentRestriction>();
    private List<AdjustmentRestriction> adjustmentAmountRestrictions = new ArrayList<AdjustmentRestriction>();
    
    public AdjustmentRestrictions() {
        super();
    }
    
    public AdjustmentRestrictions(List<AdjustmentRestriction> serviceClassRestrictions) {
        this();
        setServiceClassRestrictions(serviceClassRestrictions);
    }
    
    public AdjustmentRestrictions(Element element) {
        this();
        parseChildren(element);
    }

    public List<AdjustmentRestriction> getServiceClassRestrictions() {
        return serviceClassRestrictions;
    }

    public void setServiceClassRestrictions(List<AdjustmentRestriction> restrictions) {
        this.serviceClassRestrictions = restrictions;
    }
    
    public void addServiceClassRestriction(AdjustmentRestriction restriction) {
        getServiceClassRestrictions().add(restriction);
    }
    
    public void addAdjustmentAmountRestriction(AdjustmentRestriction restriction) {
        getAdjustmentAmountRestrictions().add(restriction);
    }

    public String toXML(StringBuffer buf) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void parseChildren(Element element) {
        Iterator<Element> it = element.elementIterator();
       while(it.hasNext()) {
           Element child = it.next();
           Node node = TypeFactory.getNode(child);
           if(node instanceof ServiceClassRestriction) {
               addServiceClassRestriction((AdjustmentRestriction)node);
           } else if(node instanceof AdjustmentAmountRestriction) {
               addAdjustmentAmountRestriction((AdjustmentRestriction)node);
           }
       }
    }
    
    public boolean allowAdjstment(String currentServiceClass, float mainAdjustment) {
        boolean serviceClass = false;
        boolean adjustmentValue = false;
        if(getServiceClassRestrictions().size() == 0) serviceClass = true;
        for(AdjustmentRestriction restriction : getServiceClassRestrictions()) {
            if(restriction.adjustmentAllowed(currentServiceClass, mainAdjustment)) {
                serviceClass = true;
                break;
            }
        }
        if(getAdjustmentAmountRestrictions().size() == 0) adjustmentValue = true;
        for(AdjustmentRestriction restriction : getAdjustmentAmountRestrictions()) {
            if(restriction.adjustmentAllowed(currentServiceClass, mainAdjustment)) {
                adjustmentValue = true;
                break;
            }
        }
        return serviceClass && adjustmentValue;
    }

    public List<AdjustmentRestriction> getAdjustmentAmountRestrictions() {
        return adjustmentAmountRestrictions;
    }

    public void setAdjustmentAmountRestrictions(List<AdjustmentRestriction> adjustmentAmountRestrictions) {
        this.adjustmentAmountRestrictions = adjustmentAmountRestrictions;
    }
}
