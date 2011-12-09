package org.emonocot.portal.controller;

import java.net.URI;
import java.net.URISyntaxException;

import javax.validation.Valid;

import org.emonocot.api.GroupService;
import org.emonocot.api.UserService;
import org.emonocot.model.user.Group;
import org.emonocot.model.user.Principal;
import org.emonocot.model.user.User;
import org.emonocot.portal.model.AceDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.security.acls.model.Permission;

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
   private String baseUrl;

   /**
    *
    * @param newBaseUrl Set the base url
    */
   public final void setBaseUrl(final String newBaseUrl) {
       this.baseUrl = newBaseUrl;
   }

    /**
     * @param userService set the user service
     */
    @Autowired
    public final void setUserService(UserService userService) {
        this.service = userService;
    }

    /**
     * @param identifier
     *            Set the identifier of the user
     * @return A model and view containing a user
     */
    @RequestMapping(value = "/user/{identifier}", method = RequestMethod.GET)
    public final ModelAndView getUserPage(@PathVariable final String identifier) {
        ModelAndView modelAndView = new ModelAndView("userPage");
        modelAndView.addObject(service.load(identifier));
        return modelAndView;
    }

    /**
     * @param identifier
     *            Set the identifier of the user
     * @return A model and view containing a user
     */
    @RequestMapping(value = "/user/{identifier}",
                    method = RequestMethod.GET,
                    headers = "Accept=application/json")
    public final ResponseEntity<User> get(@PathVariable final String identifier) {
        return new ResponseEntity<User>(service.find(identifier), HttpStatus.OK);
    }

    /**
     * @param user
     *            the user to save
     * @return A response entity containing a newly created user
     */
      @RequestMapping(value = "/user",
                      method = RequestMethod.POST)
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
      @RequestMapping(value = "/user/{identifier}",
                      method = RequestMethod.DELETE)
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
        @RequestMapping(value = "/user/{identifier}/permission",
                        params = "!delete",
                        method = RequestMethod.POST)
        public final ResponseEntity<AceDto> addPermission(
                @PathVariable final String identifier, 
                @RequestBody final AceDto ace) {
            User user = service.find(identifier);
            service.addPermission(ace.getObject(), user, ace.getPermission(), ace.getObject().getClass());
            ResponseEntity<AceDto> responseEntity = new ResponseEntity<AceDto>(ace, HttpStatus.CREATED);
            return responseEntity;
        }
        
        /**
         * @param identifier
         *            Set the identifier of the user
         * @return A response entity containing the status
         */
          @RequestMapping(value = "/user/{identifier}/permission",
                          params ="delete",
                          method = RequestMethod.POST)
          public final ResponseEntity<AceDto> deletePermission(
                  @PathVariable final String identifier, 
                  @RequestBody final AceDto ace) {
              User user = service.find(identifier);
              service.deletePermission(ace.getObject(), user, ace.getPermission(), ace.getObject().getClass());
              return new ResponseEntity<AceDto>(ace, HttpStatus.OK);
          }
}
