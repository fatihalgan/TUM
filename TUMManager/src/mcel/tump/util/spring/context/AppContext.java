package mcel.tump.util.spring.context;

import mcel.tump.dealer.dao.IDealerDao;
import mcel.tump.dealer.service.IDealerService;
import mcel.tump.security.dao.IAuthorizationDao;
import mcel.tump.security.service.ISecurityService;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import mcel.tump.account.dao.IAccountDao;
import mcel.tump.account.service.IAccountService;
import mcel.tump.reference.dao.IParametersDao;
import mcel.tump.reference.service.IParametersService;
import mcel.tump.smpp.util.SMPPSender;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class AppContext {
    
    private static AppContext appCtx = null;
    ServletContext servletCtx = null;    
    ApplicationContext springCtx = null;
    
    public static AppContext instance() {
        if(appCtx == null) appCtx = new AppContext();
        return appCtx;
    }
    
    private AppContext() {
        ExternalContext extCtx = FacesContext.getCurrentInstance().getExternalContext();
        servletCtx = (ServletContext)extCtx.getContext();
        springCtx = WebApplicationContextUtils.getWebApplicationContext(servletCtx);
    }
    
    public IAuthorizationDao getAuthorizationDao() {
        return (IAuthorizationDao)springCtx.getBean("authorizationDao");
    }
    
    public ISecurityService getSecurityService() {
        return (ISecurityService)springCtx.getBean("securityService");
    }
    
    public IDealerDao getDealerDao() {
    	return (IDealerDao)springCtx.getBean("dealerDao");
    }
    
    public IDealerService getDealerService() {
    	return (IDealerService)springCtx.getBean("dealerService");
    }
    
    public IParametersService getParametersService() {
        return (IParametersService)springCtx.getBean("parametersService");
    }
    
    public IParametersDao getParametersDao() {
        return (IParametersDao)springCtx.getBean("paramsDao");
    }
    
    public IAccountDao getAccountDao() {
        return (IAccountDao)springCtx.getBean("accountDao");
    }
    
    public IAccountService getAccountService() {
        return (IAccountService)springCtx.getBean("accountService");
    }
    
    public SMPPSender getSmppSender() {
        return (SMPPSender)springCtx.getBean("smppSender");
    }
}
