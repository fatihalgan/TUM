package mcel.tump.util.jsf;

import javax.faces.application.FacesMessage;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;


public class JSFUtils {
    
    public static void refreshPage() {
        FacesContext context = FacesContext.getCurrentInstance();
        String currentView = context.getViewRoot().getViewId();
        ViewHandler vh = context.getApplication().getViewHandler();
        UIViewRoot x = vh.createView(context, currentView);
        x.setViewId(currentView);
        context.setViewRoot(x);
    }
    
    public static Object resolveVariable(String name) {
        return FacesContext.getCurrentInstance().getApplication()
            .getVariableResolver().resolveVariable(FacesContext.getCurrentInstance(),
                name);
    }
    
    public static void setRequestAttribute(String name, Object value) {
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put(name, value);
    }
    
    public static void handleException(Exception e) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
        e.getMessage(), e.getMessage()));
    }
    
    public static String handleExceptionWithReturn(Exception e) {
        handleException(e);
        return null;
    }
}
