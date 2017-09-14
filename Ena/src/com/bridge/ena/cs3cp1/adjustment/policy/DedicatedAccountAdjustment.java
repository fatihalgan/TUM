/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.cs3cp1.adjustment.policy;

import com.bridge.ena.cs3cp1.adjustment.policy.serializer.TypeFactory;
import com.bridge.ena.cs.command.CSTokens;
import com.bridge.ena.util.DateUtils;
import com.bridge.ena.xml.Node;
import com.bridge.ena.xml.NonterminalNode;
import com.bridge.ena.xmlrpc.serializer.Array;
import com.bridge.ena.xmlrpc.serializer.Member;
import com.bridge.ena.xmlrpc.serializer.Struct;
import java.util.Date;
import java.util.Iterator;
import org.dom4j.Element;

/**
 *
 * @author db2admin
 */
public class DedicatedAccountAdjustment implements NonterminalNode {

    private int dedicatedAccountID;
    private Date startDate;
    private Date endDate;
    private DedicatedAccountAdjustmentValue value;
    private AdjustmentRestrictions restrictions;
    
    public DedicatedAccountAdjustment() {
        super();
    }
    
    public DedicatedAccountAdjustment(int dedicatedAccountID, Date startDate, Date endDate, DedicatedAccountAdjustmentValue value,
            AdjustmentRestrictions restrictions) {
        this.dedicatedAccountID = dedicatedAccountID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.value = value;
        this.restrictions = restrictions;
    }
    
    public DedicatedAccountAdjustment(Element element) {
        parseChildren(element);
    }

    public int getDedicatedAccountID() {
        return dedicatedAccountID;
    }

    public void setDedicatedAccountID(int dedicatedAccountID) {
        this.dedicatedAccountID = dedicatedAccountID;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public DedicatedAccountAdjustmentValue getValue() {
        return value;
    }

    public void setValue(DedicatedAccountAdjustmentValue value) {
        this.value = value;
    }
    
    public void parseChildren(Element element) {
        Iterator<Element> it = element.elementIterator();
        while(it.hasNext()) {
            Element child = it.next();
            if(TypeFactory.isTerminalNode(child)) {
                if(child.getName().equals("dedicatedAccountID")) {
                    setDedicatedAccountID(new Integer(child.getText().trim()));
                } else if(child.getName().equals("startDate")) {
                    setStartDate(DateUtils.convertXSDDate(child.getText().trim()));
                } else if(child.getName().equals("endDate")) {
                    setEndDate(DateUtils.convertXSDDate(child.getText().trim()));
                } 
            } else {
                Node node = TypeFactory.getNode(child);
                if(node instanceof DedicatedAccountAdjustmentValue) {
                    setValue((DedicatedAccountAdjustmentValue)node);
                } else if(node instanceof AdjustmentRestrictions) {
                    setRestrictions((AdjustmentRestrictions)node);
                }
            }
        }
    }

    public String toXML(StringBuffer buf) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void applyAdjustment(Array dedicatedAccount, float mainAdjustment, float decimalDenominator,
            String currentServiceClass) {
        if (!getRestrictions().allowAdjstment(currentServiceClass, mainAdjustment)) return;
        Date now = new Date(System.currentTimeMillis());
        if(now.after(startDate) && now.before(endDate)) {
            Struct da = new Struct();
            da.addMember(new Member(CSTokens.DedicatedAccountID.toString(), dedicatedAccountID));
            value.applyAdjustment(da, mainAdjustment, decimalDenominator);
            dedicatedAccount.addStruct(da);
        }
    }

    public AdjustmentRestrictions getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(AdjustmentRestrictions restrictions) {
        this.restrictions = restrictions;
    }
    
}
