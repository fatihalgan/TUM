package mcel.tump.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import mcel.tump.dealer.domain.DealerTypes;
import mcel.tump.security.domain.UserStatus;

public class BaseManagedBean {

	protected FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }
    
    protected javax.faces.application.Application getApplication() {
        return getFacesContext().getApplication();
    }
    
    public void addWarningMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg);
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.addMessage(null, facesMsg);
    }
    
    protected void addErrorMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.addMessage(null, facesMsg);
    }
    
    protected void addSuccessMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.addMessage(null, facesMsg);
    }
    
    protected void addMessageToComponent(String component, String msg, Severity severity) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        FacesMessage facesMessage = new FacesMessage(severity, msg, msg);
        facesContext.addMessage(component, facesMessage);
    }
    
    protected HttpSession getSession() {
        return (HttpSession) getFacesContext().getExternalContext().getSession(false);
    }
    
    protected HttpServletRequest getServletRequest() {
        return (HttpServletRequest) getFacesContext().getExternalContext().getRequest();
    }
    
    protected HttpServletResponse getServletResponse() {
        return (HttpServletResponse) getFacesContext().getExternalContext().getResponse();
    }
    
    protected UIViewRoot getViewRoot() {
        return getFacesContext().getViewRoot();
    }
    
    protected UIComponent findComponent(String componentName) {
        return getViewRoot().findComponent(componentName);
    }
    
    protected Object getManagedBean(String name) {
    	FacesContext ctx = FacesContext.getCurrentInstance();
    	return ctx.getApplication().createValueBinding("#{" + name + "}").getValue(ctx);
    }
    
    protected void setResponseContentToExcel() {
    	HttpServletResponse response = getServletResponse();
        response.setContentType("application/vnd.ms-excel");
		response.setHeader("Expires","0");
		response.setHeader("Pragma", "cache");
		response.setHeader("Cache-Control", "cache");
		response.setHeader("Content-disposition","attachment;filename="+System.currentTimeMillis()+".xls");
    }
    
    public List<SelectItem> getUserStatusOptions() {
    	List<SelectItem> options = new ArrayList<SelectItem>();
    	for(UserStatus status : UserStatus.values()) {
    		options.add(new SelectItem(status, status.toString()));
    	}
    	return options;
    }
    
    public List<SelectItem> getDealerTypeOptions() {
    	List<SelectItem> options = new ArrayList<SelectItem>();
    	for(DealerTypes type : DealerTypes.values()) {
    		options.add(new SelectItem(type, type.toString()));
    	}
    	return options;
    }
    
}
