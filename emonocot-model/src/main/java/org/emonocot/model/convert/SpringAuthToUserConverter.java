/**
 * 
 */
package org.emonocot.model.convert;

import org.emonocot.api.UserService;
import org.emonocot.model.auth.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * @author jk00kg
 *
 */
public class SpringAuthToUserConverter implements Converter<UsernamePasswordAuthenticationToken, User> {
    
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
    public User convert(UsernamePasswordAuthenticationToken source) {
        User user = service.find(source.getName());
        if(user != null) {
            return user;
        } else {
            throw new IllegalArgumentException("Unable to find a user with an identifier of " + source.getName());
        }
    }

}
