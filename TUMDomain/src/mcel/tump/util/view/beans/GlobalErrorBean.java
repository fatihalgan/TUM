/*
 * GlobalErrorBean.java
 *
 * Created on March 16, 2007, 11:26 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mcel.tump.util.view.beans;

/**
 *
 * @author db2admin
 */
public class GlobalErrorBean extends GlobalMessageBean {
    
    /** Creates a new instance of GlobalErrorBean */
    public GlobalErrorBean(String title, String summary, String detail, String returnPage) {
        super("error", title, summary, detail, returnPage);
    }
}
