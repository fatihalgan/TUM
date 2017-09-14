/*
 * Created on 22.Mar.2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package iyte.idmanager.providers.encoding;

import org.acegisecurity.providers.encoding.PasswordEncoder;
import org.springframework.dao.DataAccessException;

/**
 * @author db2admin
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class AnonymousPasswordEncoder implements PasswordEncoder {

    public boolean isPasswordValid(String encPass, String rawPass, Object salt) {
        try {
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String encodePassword(String rawPass, Object salt) throws DataAccessException {
        return null;
    }
}
