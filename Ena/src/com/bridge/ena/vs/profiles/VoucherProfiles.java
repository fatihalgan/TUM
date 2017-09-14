/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bridge.ena.vs.profiles;

import com.bridge.ena.vs.profiles.serializer.TypeFactory;
import com.bridge.ena.xml.Node;
import com.bridge.ena.xml.NonterminalNode;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import org.dom4j.Element;

/**
 *
 * @author db2admin
 */
public class VoucherProfiles implements NonterminalNode {

    private Set<VoucherProfile> profiles = new TreeSet<VoucherProfile>();

    public VoucherProfiles() {
        super();
    }

    public VoucherProfiles(Set<VoucherProfile> profiles) {
        this.profiles = profiles;
    }

    public VoucherProfiles(Element element) {
        this();
        parseChildren(element);
    }

    public void addProfile(VoucherProfile profile) {
        profiles.add(profile);
    }

    public void removeProfile(VoucherProfile profile) {
        profiles.remove(profile);
    }

    public VoucherProfile getByNominalValue(Float nominalValue) {
        for (VoucherProfile pf : profiles) {
            if (pf.getMainAccountValue().equals(nominalValue)) {
                return pf;
            }
        }
        return null;
    }

    public VoucherProfile getByClosestNominalValue(Float nominalValue) {
        VoucherProfile chosen = null;
        for (VoucherProfile pf : profiles) {
            chosen = pf;
            if(nominalValue >= pf.getMainAccountValue()) return chosen;
        }
        return chosen;
    }

    public VoucherProfile getByProfileID(String profileID) {
        for (VoucherProfile pf : profiles) {
            if (pf.getProfileID().equals(profileID)) {
                return pf;
            }
        }
        return null;
    }

    public VoucherProfile getByProfileName(String profileName) {
        for (VoucherProfile pf : profiles) {
            if (pf.getProfileName().equals(profileName)) {
                return pf;
            }
        }
        return null;
    }

    public String toXML(StringBuffer buf) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void parseChildren(Element element) {
        Iterator<Element> it = element.elementIterator();
        while (it.hasNext()) {
            Element child = it.next();
            Node node = TypeFactory.getNode(child);
            if (node instanceof VoucherProfile) {
                addProfile((VoucherProfile) node);
            }
        }
    }
}
