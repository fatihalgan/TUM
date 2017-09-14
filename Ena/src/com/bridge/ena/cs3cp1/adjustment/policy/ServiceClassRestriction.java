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
public class ServiceClassRestriction implements AdjustmentRestriction, NonterminalNode {

    private String serviceClassID;
    
    public ServiceClassRestriction() {
        super();
    }
    
    public ServiceClassRestriction(String serviceClassID) {
        this.serviceClassID = serviceClassID;
    }
    
    public ServiceClassRestriction(Element element) {
        parseChildren(element);
    }
    
    public boolean adjustmentAllowed(String currentServiceClassID, Float mainAdjustmentAmount) {
        if(currentServiceClassID.equals(serviceClassID)) return true;
        return false;
    }

    public String getServiceClassID() {
        return serviceClassID;
    }

    public void setServiceClassID(String serviceClassID) {
        this.serviceClassID = serviceClassID;
    }

    public String toXML(StringBuffer buf) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void parseChildren(Element element) {
        Iterator<Element> it = element.elementIterator();
        while(it.hasNext()) {
            Element child = it.next();
            if(TypeFactory.isTerminalNode(child)) {
                if(child.getName().equals("serviceClassID")) {
                    setServiceClassID(child.getText().trim());
                }
            }
        }
    }

}
