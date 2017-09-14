/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bridge.ena.vs.profiles;

import com.bridge.ena.vs.profiles.serializer.TypeFactory;
import com.bridge.ena.xml.Node;
import com.bridge.ena.xml.NonterminalNode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.dom4j.Attribute;
import org.dom4j.Element;

/**
 *
 * @author db2admin
 */
public class VoucherProfile implements NonterminalNode, Comparable<VoucherProfile> {

    private String profileID;
    private String profileName;
    private Float mainAccountValue;
    private Integer newTemporaryServiceClass;
    private Integer serviceClassExtension;
    private String profileDescription;
    private List<Integer> fromServiceClasses = new ArrayList<Integer>();

    public VoucherProfile() {
        super();
    }

    public VoucherProfile(String profileID, String profileName, Float mainAccountValue, Integer newTemporaryServiceClass, Integer serviceClassExtension,
    	String profileDescription) {
        this.profileID = profileID;
        this.profileName = profileName;
        this.mainAccountValue = mainAccountValue;
        this.newTemporaryServiceClass = newTemporaryServiceClass;
        this.serviceClassExtension = serviceClassExtension;
        this.profileDescription = profileDescription;
    }

    public VoucherProfile(Element element) {
        parseChildren(element);
    }
    /**
     * @return the profileID
     */
    public String getProfileID() {
        return profileID;
    }

    /**
     * @return the mainAccountValue
     */
    public Float getMainAccountValue() {
        return mainAccountValue;
    }

    public String getProfileName() {
        return profileName;
    }
    
    public String getProfileDescription() {
		return profileDescription;
	}

	public boolean equals(Object o) {
        if(!(o instanceof VoucherProfile)) return false;
        VoucherProfile other = (VoucherProfile)o;
        if(!(other.profileID.equals(this.profileID))) return false;
        if(!(other.profileName.equals(this.profileName))) return false;
        if(!(other.mainAccountValue.equals(this.mainAccountValue))) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return 17 * (profileID.hashCode() + profileName.hashCode() + mainAccountValue.hashCode());
    }

    public String toXML(StringBuffer buf) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void parseChildren(Element element) {
        Iterator<Attribute> it = element.attributeIterator();
        while(it.hasNext()) {
            Attribute child = it.next();
            if(child.getName().equals("profileID")) this.profileID = child.getText().trim();
            else if(child.getName().equals("profileName")) this.profileName = child.getText().trim();
            else if(child.getName().equals("profileDescription")) this.profileDescription = child.getText().trim();
            else if(child.getName().equals("mainAccountValue")) {
                try {
                    this.mainAccountValue = Float.parseFloat(child.getText().trim());
                } catch(Exception e) {
                    this.mainAccountValue = 0f;
                }
            }
            else if(child.getName().equals("newTemporaryServiceClass")) {
                try {
                    this.newTemporaryServiceClass = Integer.parseInt(child.getText().trim());
                } catch(Exception e) {
                    throw new RuntimeException("newTemporaryServiceClass attribute must be an integer value in VoucherProfile");
                }
            }
            else if(child.getName().equals("serviceClassExtension")) {
                try {
                    this.serviceClassExtension = Integer.parseInt(child.getText().trim());
                } catch(Exception e) {
                    throw new RuntimeException("serviceClassExtension attribute must be an integer value in VoucherProfile");
                }
            }
        }
        Iterator<Element> it2 = element.elementIterator();
        while(it2.hasNext()) {
            Element child = it2.next();
            if(child.getName().equals("FromServiceClass")) {
                fromServiceClasses.add(Integer.parseInt(child.getText()));
            }
        }
    }

    /**
     * @return the newTemporaryServiceClass
     */
    public Integer getNewTemporaryServiceClass() {
        return newTemporaryServiceClass;
    }

    /**
     * @param newTemporaryServiceClass the newTemporaryServiceClass to set
     */
    public void setNewTemporaryServiceClass(Integer newTemporaryServiceClass) {
        this.newTemporaryServiceClass = newTemporaryServiceClass;
    }

    /**
     * @return the serviceClassExtension
     */
    public Integer getServiceClassExtension() {
        return serviceClassExtension;
    }

    /**
     * @param serviceClassExtension the serviceClassExtension to set
     */
    public void setServiceClassExtension(Integer serviceClassExtension) {
        this.serviceClassExtension = serviceClassExtension;
    }

    public int compareTo(VoucherProfile o) {
        return o.getMainAccountValue().compareTo(this.getMainAccountValue());
    }

    public boolean hasInFromServiceClasses(Integer serviceClassId) {
        return fromServiceClasses.contains(serviceClassId);
    }
}
