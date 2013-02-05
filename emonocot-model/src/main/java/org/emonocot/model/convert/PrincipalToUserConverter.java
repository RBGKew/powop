/**
 * 
 */
package org.emonocot.model.convert;

import java.security.Principal;

import org.emonocot.api.UserService;
import org.emonocot.model.auth.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

/**
 * @author jk00kg
 *
 */
public class PrincipalToUserConverter implements Converter<Principal, User> {
    
    UserService service;

    /**
     * @param service the service to set
     */
    @Autowired
    public void setService(UserService service) {
        this.service = service;
    }

    /* (non-Javadoc)
     * @see org.springframework.core.convert.converter.Converter#convert(java.lang.Object)
     */
    @Override
    public User convert(Principal source) {
        User user = service.find(source.getName());
        if(user != null) {
            return user;
        } else {
            throw new IllegalArgumentException("Unable to find a user with an identifier of " + source.getName());
        }
    }

}
