/*
 * GlobalMessageBean.java
 *
 * Created on March 16, 2007, 11:21 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mcel.tump.util.view.beans;

import java.io.Serializable;

/**
 *
 * @author db2admin
 */
public class GlobalMessageBean implements Serializable {
    
    protected String type;
    protected String detail;
    protected String summary;
    protected String title;
    private String returnPage;
    
    public GlobalMessageBean() {
        super();
    }
    
    /**
     * Creates a new instance of GlobalMessageBean
     */
    public GlobalMessageBean(String type, String title, String summary, String detail, String returnPage) {
        this.setType(type);
        this.setTitle(title);
        this.setSummary(summary);
        this.setDetail(detail);
        this.setReturnPage(returnPage);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReturnPage() {
        return returnPage;
    }

    public void setReturnPage(String returnPage) {
        this.returnPage = returnPage;
    }
    
}
