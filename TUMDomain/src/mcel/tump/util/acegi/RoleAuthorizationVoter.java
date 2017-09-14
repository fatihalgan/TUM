package mcel.tump.util.acegi;

import java.util.Iterator;

import org.acegisecurity.Authentication;
import org.acegisecurity.ConfigAttribute;
import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.vote.AccessDecisionVoter;

public class RoleAuthorizationVoter implements AccessDecisionVoter {

    public RoleAuthorizationVoter() {
        super();
    }

    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    public boolean supports(Class class1) {
        return true;
    }

    public int vote(Authentication authentication, Object object, 
                    ConfigAttributeDefinition config) {
        int result = ACCESS_ABSTAIN;
        Iterator iter = config.getConfigAttributes();
        while (iter.hasNext()) {
            ConfigAttribute attribute = (ConfigAttribute)iter.next();
            if (this.supports(attribute)) {
                result = ACCESS_DENIED;
                // Attempt to find a matching granted authority
                for (int i = 0; i < authentication.getAuthorities().length; 
                     i++) {
                    if (attribute.getAttribute().equals(authentication.getAuthorities()[i].getAuthority())) {
                        return ACCESS_GRANTED;
                    }
                }
            }
        }
        return result;
    }
}
