/*
 * MenuItem.java
 *
 * Created on November 20, 2006, 12:59 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mcel.tump.util.view.xmlmenu;

import mcel.tump.security.domain.Role;
import mcel.tump.security.domain.User;
import mcel.tump.security.service.ISecurityService;
import mcel.tump.util.Messages;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContextHolder;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author db2admin
 */
public class MenuItem {
    
    public static final String COMMA = ",";
    private String labelKey;
    private String label;
    private String outcome;
    private String target;
    private List<String> roles;
    private boolean displayIfUnauthorized;
    private List<MenuItem> submenus = null;
    
    /** Creates a new instance of MenuItem */
    public MenuItem() {
        super();
    }
    
        /**
     * Add a submenu to this menu item.
     * @param i MenuItem to add as a submenu
     */
    public void addMenuItem(MenuItem i) {
        if (submenus == null) {
            submenus = new ArrayList<MenuItem>();
        }
        submenus.add(i);
    }
    
    /**
     * container property indicates if this menu item has submenus.
     * @return true if this menu item is a container of other submenus.
     */
    public boolean isContainer() {
        return submenus != null && submenus.size() > 0;
    }
    
    /**
     * labelKey property is the resource bundle key for menu item label.
     * @param labelKey resource bundle key for menu item label
     */
    public void setLabelKey(String labelKey) {
        this.labelKey = labelKey;
    }
    
    /**
     * labelKey property is the resource bundle key for this menu item label.
     * @return resource bundle key for this menu item's label
     */
    public String getLabelKey() {
        return labelKey;
    }
    
    /**
     * outcome property is the JSF navigation outcome for this menu item.
     * @param outcome JSF navigation outcome for this menu item
     */
    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    /**
     * outcome property is the JSF navigation outcome for this menu item.
     * @return JSF navigation outcome for this menu item
     */
    public String getOutcome() {
        return outcome;
    }
    
    /**
     * label property is the non-translateable label for this menu item.
     * @param label non-translateable label for this menu item
     *
     * To use a translatable label, set the labelKey property instead.
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * label property is the non-translateable label for this menu item.
     * @return non-translateable label for this menu item
     */
    public String getLabel() {
        return label;
    }
    
    /**
     * List of submenus for this menu item if any.
     * @return list of submenus for this menu item if any
     */
    public List<MenuItem> getItems() {
        return submenus != null ? submenus : new ArrayList<MenuItem>(0);
    }
    
    /**
     * Security roles required to use this menu item.
     * @param roles comma-separated list of role names
     */
    public void setRoles(String roles) {
        if (roles != null) {
            this.roles = new ArrayList<String>();
            if (roles.indexOf(',') >= 0) {
                StringTokenizer s = new StringTokenizer(roles, COMMA);
                while (s.hasMoreTokens()) {
                    this.roles.add(s.nextToken());
                }
            } else {
                this.roles.add(roles);
            }
        }
    }
    
    /**
     * displayIfUnauthorized property controls whether menu item will appear anyway.
     * (as display-only) if the user is not in the list of roles)
     * @param display true if you want the menu item to display anyway
     */
    public void setDisplayIfUnauthorized(boolean display) {
        this.displayIfUnauthorized = display;
    }

    /**
     * rendered property indicates whether the menu item should be rendered.
     * @return true if the menu item should be displayed
     */
    public boolean isRendered() {
        return displayIfUnauthorized || isUserAuthorized();
    }
    
    private boolean requiresAuthorization() {
        return roles != null && roles.size() > 0;
    }

    private ExternalContext getExternalContext() {
        return FacesContext.getCurrentInstance().getExternalContext();
    }

    private boolean isSecurityEnabled() {
        //return getExternalContext().getAuthType() != null;
        return true;
    }

    private boolean isUserAuthorized() {
        if (!isSecurityEnabled() || !requiresAuthorization()) {
            return true;
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User)getSecurityService().loadUserByUsername(auth.getName());
        if(user == null) return false;
        Set<Role> userRoles = user.getUserRoles();
        for(Role userRole : userRoles) {
            if(isUserInRole(userRole.getRoleName())) return true;
        }
        return false;
    }
    
    private boolean isUserInRole(String roleName) {
        for(String role : roles) {
            if(roleName.trim().equals(role.trim())) return true;
        }
        return false;
    }
    
    public String getTarget(String target) {
        return target;
    }
    
    public void setTarget(String target) {
        this.target = target;
    }
    
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("[");
        if(labelKey != null) buf.append("'" + Messages.getString(labelKey) + "'" + COMMA);
        else buf.append("'" + label + "'" + COMMA);
        if(outcome != null) {
            buf.append("'" + outcome + "'");
        } else {
            buf.append("null");
        }
        buf.append(COMMA);
        if(target != null) {
            buf.append("{'tw' :");
            buf.append("'" + target + "'");
            buf.append("}");
        } else {
            buf.append("null");
        }
        buf.append(COMMA);
        if(submenus != null && submenus.size() > 0) {
            for(MenuItem item : submenus) {
                if(item.isRendered())
                    buf.append(item.toString());
            }
        }
        buf.append("]" + COMMA);
        return buf.toString();
    }
    
    private ISecurityService getSecurityService() {
        ServletContext servletCtx = (ServletContext)getExternalContext().getContext();
        ApplicationContext springCtx = WebApplicationContextUtils.getWebApplicationContext(servletCtx);
        return (ISecurityService)springCtx.getBean("securityService");
    }
}
