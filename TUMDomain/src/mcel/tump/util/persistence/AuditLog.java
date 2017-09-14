/*
 * AuditLog.java
 *
 * Created on March 15, 2007, 10:50 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mcel.tump.util.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *
 * @author db2admin
 */
public class AuditLog {
    
    private SessionFactory sessionFactory;
    
    public void logEvent(AuditLogRecord logRecord) {
        Session tempSession = getSessionFactory().openSession();
        Transaction tx = tempSession.beginTransaction();
        try {
            tempSession.save(logRecord);
            tempSession.flush();
        } finally {
            tx.commit();
            tempSession.close();
        }
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
