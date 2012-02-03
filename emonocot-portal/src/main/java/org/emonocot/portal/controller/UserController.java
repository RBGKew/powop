package org.emonocot.portal.controller;

import java.net.URI;
import java.net.URISyntaxException;

import javax.validation.Valid;

import org.emonocot.api.UserService;
import org.emonocot.model.common.SecuredObject;
import org.emonocot.model.user.User;
import org.emonocot.portal.model.AceDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author ben
 *
 */
@Controller
public class UserController {

    /**
    *
    */
    private static Logger logger = LoggerFactory
            .getLogger(UserController.class);

    /**
     *
     */
    private UserService service;

    /**
     *
     */
    private ConversionService conversionService;

    /**
    *
    */
    private String baseUrl;

    /**
     *
     * @param newBaseUrl
     *            Set the base url
     */
    public final void setBaseUrl(final String newBaseUrl) {
        this.baseUrl = newBaseUrl;
    }

    /**
     *
     */
    @Autowired
    public final void setConversionService(
            final ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    /**
     * @param userService
     *            set the user service
     */
    @Autowired
    public final void setUserService(final UserService userService) {
        this.service = userService;
    }

    /**
     * @return A model and view containing a user
     */
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public final ModelAndView getUserPage() {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        User user = (User) authentication.getPrincipal();
        ModelAndView modelAndView = new ModelAndView("user/show");
        modelAndView.addObject(service.load(user.getUsername()));
        return modelAndView;
    }

    /**
     * @param identifier
     *            Set the identifier of the user
     * @return A model and view containing a user
     */
    @RequestMapping(value = "/user/{identifier}", method = RequestMethod.GET, headers = "Accept=application/json")
    public final ResponseEntity<User> get(@PathVariable final String identifier) {
        return new ResponseEntity<User>(service.find(identifier), HttpStatus.OK);
    }

    /**
     * @param user
     *            the user to save
     * @return A response entity containing a newly created user
     */
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public final ResponseEntity<User> create(@RequestBody @Valid final User user) {
        HttpHeaders httpHeaders = new HttpHeaders();
        try {
            httpHeaders.setLocation(new URI(baseUrl + "user/"
                    + user.getIdentifier()));
        } catch (URISyntaxException e) {
            logger.error(e.getMessage());
        }
        ResponseEntity<User> response = new ResponseEntity<User>(
                service.save(user), httpHeaders, HttpStatus.CREATED);
        return response;
    }

    /**
     * @param identifier
     *            Set the identifier of the user
     * @return A response entity containing the status
     */
    @RequestMapping(value = "/user/{identifier}", method = RequestMethod.DELETE)
    public final ResponseEntity<User> delete(
            @PathVariable final String identifier) {
        service.delete(identifier);
        return new ResponseEntity<User>(HttpStatus.OK);
    }

    /**
     * @param identifier
     *            Set the identifier of the user
     * @return A response entity containing the status
     */
    @RequestMapping(value = "/user/{identifier}/permission", params = "!delete", method = RequestMethod.POST)
    public final ResponseEntity<AceDto> addPermission(
            @PathVariable final String identifier, @RequestBody final AceDto ace) {
        SecuredObject object = conversionService.convert(ace,
                SecuredObject.class);
        service.addPermission(object, identifier, ace.getPermission(), ace.getClazz());
        ResponseEntity<AceDto> responseEntity = new ResponseEntity<AceDto>(ace,
                HttpStatus.CREATED);
        return responseEntity;
    }

    /**
     * @param identifier
     *            Set the identifier of the user
     * @return A response entity containing the status
     */
    @RequestMapping(value = "/user/{identifier}/permission", params = "delete", method = RequestMethod.POST)
    public final ResponseEntity<AceDto> deletePermission(
            @PathVariable final String identifier, @RequestBody final AceDto ace) {
        SecuredObject object = conversionService.convert(ace,
                SecuredObject.class);
        service.deletePermission(object, identifier, ace.getPermission(),
                ace.getClazz());
        return new ResponseEntity<AceDto>(ace, HttpStatus.OK);
    }
}
