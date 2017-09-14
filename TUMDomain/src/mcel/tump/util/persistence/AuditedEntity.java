/*
 * AuditedEntity.java
 * 
 * Created on Aug 8, 2007, 5:19:00 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.util.persistence;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.type.Type;

/**
 *
 * @author db2admin
 */
public class AuditedEntity implements Serializable {

    private Long id;
    private Long entityId;
    private String entityClass;
    private String message;
    private Set<AuditedProperty> properties = new HashSet<AuditedProperty>();
    private AuditLogRecord owningLogRecord = new AuditLogRecord();
    
    public AuditedEntity() {
        super();
    }
    
    public AuditedEntity(Class entityClass, Long entityId, String message) {
        this.entityClass = entityClass.getName();
        this.message = message;
        this.entityId = entityId;
    }

    public String getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(String entityClass) {
        this.entityClass = entityClass;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Set<AuditedProperty> getProperties() {
        return properties;
    }

    public void setProperties(Set<AuditedProperty> properties) {
        this.properties = properties;
    }
    
    public void addProperty(AuditedProperty prop) {
        getProperties().add(prop);
        prop.setOwningEntity(this);
    }

    public AuditLogRecord getOwningLogRecord() {
        return owningLogRecord;
    }

    public void setOwningLogRecord(AuditLogRecord owningLogRecord) {
        this.owningLogRecord = owningLogRecord;
    }
    
    public void addProperties(Object entity, Serializable id, Object[] currentState,
        Object[] previousState, String[] propertyNames, Type[] types) {
        for(int i= 0; i < propertyNames.length; i++) {
            AuditedProperty auditProp = new AuditedProperty();
            auditProp.setOwningEntity(this);
            auditProp.setPropertyName(propertyNames[i]);
            if(currentState[i] == null) auditProp.setNewValue("null");
            else if(containsAuditableCollection(currentState[i])) auditProp.setNewValue("entityCollection");
            else auditProp.setNewValue(currentState[i].toString());
            addProperty(auditProp);
        }
    }
    
    private boolean containsAuditableCollection(Object o) {
        if(o instanceof Collection) {
            Collection col = (Collection)o;
            Iterator it = col.iterator();
            while(it.hasNext()) {
                if(it.next() instanceof Auditable) return true;
            }
        }
        return false;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }
    
    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof AuditedEntity)) {
            return false;
        }
        AuditedEntity castOther = (AuditedEntity) other;
        return new EqualsBuilder().append(entityId, castOther.entityId).append(entityClass, castOther.entityClass).isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder(-1300479015, 1517648211).append(entityId).append(entityClass).toHashCode();
    }

}
