package mcel.tump.util.acegi;

import mcel.tump.security.domain.Authorization;
import mcel.tump.security.service.ISecurityService;
import java.util.Iterator;
import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.SecurityConfig;
import org.acegisecurity.intercept.web.AbstractFilterInvocationDefinitionSource;

public class UrlToUserFilterInvocationDefinitionSource extends AbstractFilterInvocationDefinitionSource {
    
    private ISecurityService securityService;
    
    public UrlToUserFilterInvocationDefinitionSource() {
        super();
    }

    public ConfigAttributeDefinition lookupAttributes(String url) {
        if((url == null) || (url.length() == 0)) 
            throw new NullPointerException("UrlToUserFilterInvocationDefinitionSource.lookupAttributes: " +
            "Parameter of url is null..");
        String myUrl = null;
        myUrl = prepareUrl(String.valueOf(url.toCharArray()));
        Authorization auth = getSecurityService().getAuthorizationOfPath(myUrl);
        ConfigAttributeDefinition cad = new ConfigAttributeDefinition();
        if(auth != null) {
            SecurityConfig cfg = new SecurityConfig(auth.getAuthorizationURI());
            cad.addConfigAttribute(cfg);
        }
        return cad;
    }
    
    private String prepareUrl(String url) {
        url = url.toLowerCase();
        if (url.contains("&")) url = url.substring(0, url.indexOf("&"));
        int i = -1;
        do {
        	i = url.indexOf("/faces");
        	if(i != -1) url = url.substring(i + 6, (url.length()- (i)));
        } while(i != -1);
        return url;
    }
    
    public Iterator getConfigAttributeDefinitions() {
        return null;
    }


    public void setSecurityService(ISecurityService securityService) {
        this.securityService = securityService;
    }

    public ISecurityService getSecurityService() {
        return securityService;
    }
}
