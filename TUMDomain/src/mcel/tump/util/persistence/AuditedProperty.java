/*
 * AuditedProperty.java
 * 
 * Created on Aug 8, 2007, 5:15:37 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.util.persistence;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *
 * @author db2admin
 */
public class AuditedProperty implements Serializable {
    
    private String propertyName;
    private String oldValue;
    private String newValue;
    private AuditedEntity owningEntity = new AuditedEntity();
    
    public AuditedProperty() {
        super();
    }
    
    public AuditedProperty(String propertyName, Object oldValue, Object newValue) {
        this.propertyName = propertyName;
        if(oldValue != null) this.oldValue = oldValue.toString();
        if(newValue != null) this.newValue = newValue.toString();
    }
    
    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public AuditedEntity getOwningEntity() {
        return owningEntity;
    }

    public void setOwningEntity(AuditedEntity owningEntity) {
        this.owningEntity = owningEntity;
    }
    
    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof AuditedProperty)) {
            return false;
        }
        AuditedProperty castOther = (AuditedProperty) other;
        return new EqualsBuilder().append(propertyName, castOther.propertyName).isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder(-1300479075, 1516648211).append(propertyName).toHashCode();
    }
    
}
