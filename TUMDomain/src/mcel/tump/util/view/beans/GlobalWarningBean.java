/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.util.view.beans;

/**
 *
 * @author db2admin
 */
public class GlobalWarningBean extends GlobalMessageBean {
    
    /** Creates a new instance of GlobalInfoBean */
    public GlobalWarningBean(String summary, String detail, String returnPage) {
        super("warning", "", summary, detail, returnPage);
    }
    
    public GlobalWarningBean(String title, String summary, String detail, String returnPage) {
        super("warning", title, summary, detail, returnPage);
    }
}
