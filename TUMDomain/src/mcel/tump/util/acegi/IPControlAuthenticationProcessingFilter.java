/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mcel.tump.util.acegi;

import javax.servlet.http.HttpServletRequest;
import mcel.tump.dealer.domain.AbstractDealer;
import mcel.tump.dealer.domain.DealerShop;
import mcel.tump.dealer.domain.IPAddress;
import mcel.tump.dealer.service.IDealerService;
import mcel.tump.security.domain.User;
import mcel.tump.security.service.ISecurityService;
import org.acegisecurity.Authentication;
import org.acegisecurity.AuthenticationException;
import org.acegisecurity.InsufficientAuthenticationException;
import org.acegisecurity.ui.webapp.AuthenticationProcessingFilter;

/**
 *
 * @author db2admin
 */
public class IPControlAuthenticationProcessingFilter extends AuthenticationProcessingFilter {

    private ISecurityService securityService;
    private IDealerService dealerService;
    private String allowedAddress1;
    private String allowedAddress2;
    
    public Authentication attemptAuthentication(HttpServletRequest request) throws AuthenticationException {
        Authentication auth = super.attemptAuthentication(request);
        User user = getSecurityService().loadUserByUsername(auth.getName()); 
        AbstractDealer dealer = user.getOwningDealer();
        //TODO: Check IP 
        boolean valid = getDealerService().checkIPValidity(request.getRemoteAddr(), dealer);
        if(valid || request.getRemoteAddr().equals(getAllowedAddress1()) || request.getRemoteAddr().equals(getAllowedAddress2())) return auth;
        else
            throw new InsufficientAuthenticationException("The IP address is not in the range defined for the shop this user belongs to.");
    }

    public ISecurityService getSecurityService() {
        return securityService;
    }

    public void setSecurityService(ISecurityService securityService) {
        this.securityService = securityService;
    }
    
    public IDealerService getDealerService() {
        return dealerService;
    }

    public void setDealerService(IDealerService dealerService) {
        this.dealerService = dealerService;
    }

    public String getAllowedAddress1() {
        return allowedAddress1;
    }

    public void setAllowedAddress1(String allowedAddress1) {
        this.allowedAddress1 = allowedAddress1;
    }

    public String getAllowedAddress2() {
        return allowedAddress2;
    }

    public void setAllowedAddress2(String allowedAddress2) {
        this.allowedAddress2 = allowedAddress2;
    }
    
}
