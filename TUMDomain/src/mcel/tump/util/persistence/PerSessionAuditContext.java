/*
 * PerSessionAuditContext.java
 *
 * Created on March 15, 2007, 10:26 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mcel.tump.util.persistence;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.acegisecurity.context.SecurityContextHolder;
import org.hibernate.CallbackException;
import org.hibernate.type.Type;

/**
 *
 * @author db2admin
 */
public class PerSessionAuditContext {
    
    private Set<AuditedEntity> entities = new HashSet<AuditedEntity>();
    private AuditLogRecord logRecord = new AuditLogRecord();
    private AuditLog log;
    
    /** Creates a new instance of PerSessionAuditContext */
    public PerSessionAuditContext(AuditLog auditLog) {
        super();
        if(SecurityContextHolder.getContext().getAuthentication() != null)
            logRecord.setUserName(SecurityContextHolder.getContext().getAuthentication().getName());    
        if(logRecord.getUserName() == null) logRecord.setUserName("System");
        logRecord.setCreated(new Date());
        this.log = auditLog;
    }
    
    public boolean onSave(Object entity, Serializable id, Object[] state,
        String[] propertyNames, Type[] types) throws CallbackException {
        if(!(entity instanceof Auditable)) return false;
        setLogMessage(entity);
        AuditedEntity auditedEntity = new AuditedEntity(entity.getClass(), (Long)id,"Create");
        entities.add(auditedEntity);            
        return false;
    }
    
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState,
        Object[] previousState, String[] propertyNames, Type[] types) throws CallbackException {
        if(!(entity instanceof Auditable)) return false;
        setLogMessage(entity);
        AuditedEntity auditedEntity = new AuditedEntity(entity.getClass(), (Long)id, "Update");
        auditedEntity.addProperties(entity, id, currentState, previousState, propertyNames, types);
        entities.add(auditedEntity);
        return false;
    }
    
    public void postFlush(Iterator iterator) throws CallbackException {
        try {
            for(AuditedEntity entity : entities) {
                logRecord.addEntity(entity);
            }
            log.logEvent(logRecord);
            
        } finally {
            entities.clear();
        }
    }
    
    private void setLogMessage(Object entity) {
        Auditable auditable = (Auditable)entity;
        if(auditable.getAuditMessage() != null && auditable.getAuditMessage().length() > 0) {
            logRecord.setExplanation(auditable.getAuditMessage());
            auditable.setAuditMessage(null);
        }
    }
}