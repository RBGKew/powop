package org.emonocot.portal.controller;

import java.net.URI;
import java.net.URISyntaxException;

import org.emonocot.api.GroupService;
import org.emonocot.api.UserService;
import org.emonocot.model.user.Group;
import org.emonocot.model.user.User;
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
}
