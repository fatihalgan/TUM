/*
 * Auditable.java
 *
 * Created on March 15, 2007, 10:29 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mcel.tump.util.persistence;

/**
 *
 * @author db2admin
 */
public interface Auditable {
    public Long getId();
    public String getAuditMessage();
    public void setAuditMessage(String message);
}
