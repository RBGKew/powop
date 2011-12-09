package org.emonocot.portal.controller;

import java.net.URI;
import java.net.URISyntaxException;

import org.emonocot.api.GroupService;
import org.emonocot.api.UserService;
import org.emonocot.model.user.Group;
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

/**
 *
 * @author ben
 *
 */
@Controller
public class GroupController {

    /**
    *
    */
   private static Logger logger = LoggerFactory
           .getLogger(GroupController.class);

    /**
     *
     */
    private GroupService service;
    
    /**
     *
     */
    private UserService userService;

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
     * @param groupService set the group service
     */
    @Autowired
    public final void setGroupService(GroupService service) {
        this.service = service;
    }
    
    /**
     * @param userService set the user service
     */
    @Autowired
    public final void setUserService(UserService userService) {
        this.userService = userService;
    }

      /**
       * @param identifier
       *            Set the identifier of the group
       * @return A model and view containing a group
       */
      @RequestMapping(value = "/group/{identifier}",
                      method = RequestMethod.GET,
                      headers = "Accept=application/json")
    public final ResponseEntity<Group> get(
            @PathVariable final String identifier) {
        return new ResponseEntity<Group>(service.find(identifier),
                HttpStatus.OK);
    }

      /**
       * @param group
       *            the group to save
       * @return A response entity containing a newly created group
       */
        @RequestMapping(value = "/group",
                        method = RequestMethod.POST)
      public final ResponseEntity<Group> create(@RequestBody final Group group) {
          HttpHeaders httpHeaders = new HttpHeaders();
          try {
              httpHeaders.setLocation(new URI(baseUrl + "group/"
                      + group.getIdentifier()));
          } catch (URISyntaxException e) {
              logger.error(e.getMessage());
          }
          service.save(group);
          ResponseEntity<Group> response = new ResponseEntity<Group>(
                  group, httpHeaders, HttpStatus.CREATED);
          return response;
      }

      /**
       * @param identifier
       *            Set the identifier of the group
       * @return A response entity containing the status
       */
        @RequestMapping(value = "/group/{identifier}",
                        method = RequestMethod.DELETE)
        public final ResponseEntity<Group> delete(
                @PathVariable final String identifier) {
            service.delete(identifier);
            return new ResponseEntity<Group>(HttpStatus.OK);
        }
        
        /**
         * @param identifier
         *            Set the identifier of the group
         * @return A response entity containing the status
         */
          @RequestMapping(value = "/group/{identifier}/permission",
                          params = "!delete",
                          method = RequestMethod.POST)
          public final ResponseEntity<AceDto> addPermission(
                  @PathVariable final String identifier, @RequestBody final AceDto ace) {
              Group group = service.find(identifier);
              userService.addPermission(ace.getObject(), group, ace.getPermission(), ace.getObject().getClass());
              ResponseEntity<AceDto> responseEntity = new ResponseEntity<AceDto>(ace, HttpStatus.CREATED);
              return responseEntity;
          }
          
          /**
           * @param identifier
           *            Set the identifier of the group
           * @return A response entity containing the status
           */
            @RequestMapping(value = "/group/{identifier}/permission",
                            params = "delete",
                            method = RequestMethod.POST)
            public final ResponseEntity<AceDto> deletePermission(
                    @PathVariable final String identifier, @RequestBody final AceDto ace) {
                Group group = service.find(identifier);
                userService.deletePermission(ace.getObject(), group, ace.getPermission(), ace.getObject().getClass());
                return new ResponseEntity<AceDto>(ace, HttpStatus.OK);
            }
}
