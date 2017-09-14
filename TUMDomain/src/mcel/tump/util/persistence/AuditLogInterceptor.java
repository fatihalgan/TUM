/*
 * AuditLogInterceptor.java
 *
 * Created on March 15, 2007, 5:00 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mcel.tump.util.persistence;

import java.io.Serializable;
import java.util.Iterator;
import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.Session;
import org.hibernate.type.Type;

/**
 *
 * @author db2admin
 */
public class AuditLogInterceptor extends EmptyInterceptor {
    
    private static ThreadLocal auditCtx = new ThreadLocal();    
    private AuditLog auditLog;
    
    /** Creates a new instance of AuditLogInterceptor */
    public AuditLogInterceptor() {
        super();
    }

    private PerSessionAuditContext getAuditCtx() {
        PerSessionAuditContext ctx = (PerSessionAuditContext)auditCtx.get();
        if(ctx == null) {
            ctx = new PerSessionAuditContext(getAuditLog());
            auditCtx.set(ctx);
        }
        return ctx;
    }
    
    public boolean onSave(Object entity, Serializable id, Object[] state,
        String[] propertyNames, Type[] types) throws CallbackException {
        if(!(entity instanceof Auditable)) return false;
        return getAuditCtx().onSave(entity, id, state, propertyNames, types);
    }
    
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState,
        Object[] previousState, String[] propertyNames, Type[] types)
        throws CallbackException {
        if(!(entity instanceof Auditable)) return false;
        return getAuditCtx().onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
    }
    
    public void postFlush(Iterator iterator) throws CallbackException {
        getAuditCtx().postFlush(iterator);
        auditCtx.remove();
    }
    
    public AuditLog getAuditLog() {
        return auditLog;
    }

    public void setAuditLog(AuditLog auditLog) {
        this.auditLog = auditLog;
    }
}
