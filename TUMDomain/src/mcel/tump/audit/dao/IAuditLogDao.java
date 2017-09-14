/*
 * IAuditLogDao.java
 * 
 * Created on Aug 21, 2007, 4:12:00 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.audit.dao;

import java.util.List;
import mcel.tump.util.persistence.AuditedEntity;
import mcel.tump.util.persistence.AuditedProperty;
import mcel.tump.util.persistence.IBaseDao;

/**
 *
 * @author db2admin
 */
public interface IAuditLogDao extends IBaseDao {

    public List<AuditedEntity> findEntityLogs(String entityName);
    public List<AuditedProperty> findPropertyLogs(String entityName);
}
