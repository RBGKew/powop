package org.emonocot.portal.controller;

import java.net.URI;
import java.net.URISyntaxException;

import org.emonocot.model.reference.Reference;
import org.emonocot.api.ReferenceService;
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
public class ReferenceController {

   /**
    *
    */
   private static Logger logger = LoggerFactory
           .getLogger(ReferenceController.class);
   /**
    *
    */
   private ReferenceService service;

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
   *
   * @param referenceService
   *            Set the reference service
   */
  @Autowired
    public final void setReferenceService(
            final ReferenceService referenceService) {
      this.service = referenceService;
  }

  /**
   * @param identifier
   *            Set the identifier of the reference
   * @return A model and view containing a reference
   */
  @RequestMapping(value = "/reference/{identifier}",
                  method = RequestMethod.GET,
                  headers = "Accept=application/json")
  public final ResponseEntity<Reference> get(
          @PathVariable final String identifier) {
      return new ResponseEntity<Reference>(service.find(identifier),
              HttpStatus.OK);
  }

  /**
   * @param reference
   *            the reference to save
   * @return A response entity containing a newly created reference
   */
    @RequestMapping(value = "/reference",
                    method = RequestMethod.POST)
    public final ResponseEntity<Reference> create(
            @RequestBody final Reference reference) {
        HttpHeaders httpHeaders = new HttpHeaders();
        try {
            httpHeaders.setLocation(new URI(baseUrl + "reference/" + reference.getIdentifier()));
        } catch (URISyntaxException e) {
            logger.error(e.getMessage());
        }
        ResponseEntity<Reference> response = new ResponseEntity<Reference>(
                service.save(reference), httpHeaders, HttpStatus.CREATED);
        return response;
    }

  /**
   * @param identifier
   *            Set the identifier of the reference
   * @return A response entity containing the status
   */
    @RequestMapping(value = "/reference/{identifier}",
                    method = RequestMethod.DELETE)
    public final ResponseEntity<Reference> delete(
            @PathVariable final String identifier) {
        service.delete(identifier);
        return new ResponseEntity<Reference>(HttpStatus.OK);
    }
}
