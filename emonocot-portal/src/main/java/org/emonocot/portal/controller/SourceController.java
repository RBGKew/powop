package org.emonocot.portal.controller;

import java.net.URI;
import java.net.URISyntaxException;

import org.emonocot.model.source.Source;
import org.emonocot.api.SourceService;

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
public class SourceController {

   /**
    *
    */
   private static Logger logger = LoggerFactory
           .getLogger(SourceController.class);
   /**
    *
    */
   private SourceService service;

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
 * @param sourceService
 *            Set the source service
 */
@Autowired
public final void setSourceService(final SourceService sourceService) {
    this.service = sourceService;
}

/**
 * @param identifier
 *            Set the identifier of the source
 * @return A model and view containing a source
 */
@RequestMapping(value = "/source/{identifier}", method = RequestMethod.GET)
public final ModelAndView getSourcePage(@PathVariable final String identifier) {
    ModelAndView modelAndView = new ModelAndView("sourcePage");
    modelAndView.addObject(service.load(identifier, "source-page"));
    return modelAndView;
}

/**
 * @param identifier
 *            Set the identifier of the source
 * @return A model and view containing a source
 */
@RequestMapping(value = "/source/{identifier}",
                method = RequestMethod.GET,
                headers = "Accept=application/json")
public final ResponseEntity<Source> get(
        @PathVariable final String identifier) {
    return new ResponseEntity<Source>(service.find(identifier),
            HttpStatus.OK);
}

/**
 * @param source
 *            the source to save
 * @return A response entity containing a newly created source
 */
  @RequestMapping(value = "/source",
                  method = RequestMethod.POST)
  public final ResponseEntity<Source> create(
          @RequestBody final Source source) {
      HttpHeaders httpHeaders = new HttpHeaders();
      try {
          httpHeaders.setLocation(new URI(baseUrl + "source/" + source.getIdentifier()));
      } catch (URISyntaxException e) {
          logger.error(e.getMessage());
      }
      ResponseEntity<Source> response = new ResponseEntity<Source>(
              service.save(source), httpHeaders, HttpStatus.CREATED);
      return response;
  }

  /**
   * @param identifier
   *            Set the identifier of the source
   * @return A response entity containing the status
   */
    @RequestMapping(value = "/source/{identifier}",
                    method = RequestMethod.DELETE)
    public final ResponseEntity<Source> delete(
            @PathVariable final String identifier) {
        service.delete(identifier);
        return new ResponseEntity<Source>(HttpStatus.OK);
    }
}
