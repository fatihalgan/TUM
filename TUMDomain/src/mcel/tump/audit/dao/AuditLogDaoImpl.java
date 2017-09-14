/*
 * AuditLogDaoImpl.java
 * 
 * Created on Aug 21, 2007, 4:24:12 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.audit.dao;

import java.util.List;
import mcel.tump.util.persistence.AuditedEntity;
import mcel.tump.util.persistence.AuditedProperty;
import mcel.tump.util.persistence.BaseDao;

/**
 *
 * @author db2admin
 */
public class AuditLogDaoImpl extends BaseDao implements IAuditLogDao {

    public AuditLogDaoImpl() {
        super();
    }

    public List<AuditedEntity> findEntityLogs(String entityName) {
        List<AuditedEntity> entities = getHibernateTemplate().find("from mcel.tump.util.persistence.AuditedEntity as entity where entity.entityClass = ? order by entity.owningLogRecord.created desc", new Object[] {entityName});
        return entities;
    }
    
    public List<AuditedProperty> findPropertyLogs(String entityName) {
        List<AuditedProperty> properties = getHibernateTemplate().find("from mcel.tump.util.persistence.AuditedProperty as prop where prop.owningEntity.entityClass = ? order by prop.owningEntity.owningLogRecord.created desc", new Object[] {entityName});
        return properties;
    }
}
