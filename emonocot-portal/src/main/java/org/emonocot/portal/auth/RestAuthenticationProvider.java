package org.emonocot.portal.auth;

import org.emonocot.api.UserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

public class RestAuthenticationProvider implements AuthenticationProvider {
	
	private UserService userService;
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	@Override
	public boolean supports(Class<? extends Object> authentication) {
	    return authentication.equals(RestAuthenticationToken.class);
	}

	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		UserDetails userDetails;

        try {
            userDetails = userService.getUserByApiKey((String)authentication.getPrincipal());
            if(userDetails != null) {
                return new RestAuthenticationToken(authentication.getPrincipal(),authentication.getCredentials(),userDetails);
            } else {
            	throw new BadCredentialsException("Invalid API Key");
            }
        }  catch (Exception e) {
            throw new AuthenticationServiceException(e.getMessage(), e);
        }
	}

}
