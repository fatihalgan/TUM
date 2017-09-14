package mcel.tump.util.acegi;
import mcel.tump.security.domain.Authorization;
import mcel.tump.security.domain.Role;
import mcel.tump.security.service.ISecurityService;
import java.util.Iterator;
import java.util.Set;
import org.acegisecurity.Authentication;
import org.acegisecurity.ConfigAttribute;
import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.vote.AccessDecisionVoter;

public class AnonymousUserAuthorizationVoter implements AccessDecisionVoter {
    
    private ISecurityService securityService;
    public static final String ANONYMOUS_ROLE = "ROLE_ANONYMOUS";
    
    public AnonymousUserAuthorizationVoter() {
        super();
    }

    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    public boolean supports(Class clazz) {
        return true;
    }

    public int vote(Authentication authentication, Object object, 
                    ConfigAttributeDefinition config) {
        int result = ACCESS_ABSTAIN;
        Iterator iter = config.getConfigAttributes();
        while (iter.hasNext()) {
            ConfigAttribute attribute = (ConfigAttribute)iter.next();
            if (this.supports(attribute)) {
                // Attempt to find a matching granted authority
                for (int i = 0; i < authentication.getAuthorities().length; i++) {
                    if(authentication.getAuthorities()[i].getAuthority().equals(ANONYMOUS_ROLE)) {
                        Role role = getSecurityService().getRoleByName(ANONYMOUS_ROLE);
                        return voteForRole(role, attribute);    
                    }
                }
            }
        }
        return result;
    }
    
    private int voteForRole(Role role, ConfigAttribute attribute) {
        Set<Authorization> authorizations = role.getRoleAuthorizations();
        for(Authorization auth : authorizations) {
            if(attribute.getAttribute().equals(auth.getAuthorizationURI())) return ACCESS_GRANTED;    
        }
        return ACCESS_ABSTAIN;
    }


    public void setSecurityService(ISecurityService securityService) {
        this.securityService = securityService;
    }

    public ISecurityService getSecurityService() {
        return securityService;
    }
}
