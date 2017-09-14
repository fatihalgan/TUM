/*
 * GlobalInfoBean.java
 * 
 * Created on Aug 2, 2007, 5:01:12 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.util.view.beans;

/**
 *
 * @author db2admin
 */
public class GlobalInfoBean extends GlobalMessageBean {

   /** Creates a new instance of GlobalInfoBean */
    public GlobalInfoBean(String summary, String detail, String returnPage) {
        super("info", "", summary, detail, returnPage);
    }
    
    public GlobalInfoBean(String title, String summary, String detail, String returnPage) {
        super("info", title, summary, detail, returnPage);
    }
}
