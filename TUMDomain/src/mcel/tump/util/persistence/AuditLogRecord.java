/*
 * AuditLogRecord.java
 *
 * Created on March 15, 2007, 4:31 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mcel.tump.util.persistence;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *
 * @author db2admin
 */
public class AuditLogRecord implements Serializable {
    
    private Long id;
    private String userName;
    private Date created;
    private String explanation;
    private Set<AuditedEntity> entities = new HashSet<AuditedEntity>();
    
    public AuditLogRecord() { 
        super(); 
    }
    
    public AuditLogRecord(String userName, String explanation) {
        this.userName = userName;
        this.created = new Date();
        this.explanation = explanation;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Set<AuditedEntity> getEntities() {
        return entities;
    }

    public void setEntities(Set<AuditedEntity> entities) {
        this.entities = entities;
    }
    
    public void addEntity(AuditedEntity entity) {
        getEntities().add(entity);
        entity.setOwningLogRecord(this);
    }
    
    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof AuditLogRecord)) {
            return false;
        }
        AuditLogRecord castOther = (AuditLogRecord) other;
        return new EqualsBuilder().append(userName, castOther.userName).append(created, castOther.created).isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder(-1300409015, 1507648211).append(userName).append(created).toHashCode();
    }
}
