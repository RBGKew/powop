package org.emonocot.portal.controller;

import java.net.URI;
import java.net.URISyntaxException;

import org.emonocot.model.taxon.Taxon;
import org.emonocot.api.TaxonService;

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
public class TaxonController {

   /**
    *
    */
   private static Logger logger = LoggerFactory
           .getLogger(TaxonController.class);
   /**
    *
    */
   private TaxonService service;

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
   * @param taxonService
   *            Set the taxon service
   */
  @Autowired
  public final void setTaxonService(final TaxonService taxonService) {
      this.service = taxonService;
  }

  /**
   * @param identifier
   *            Set the identifier of the taxon
   * @return A model and view containing a taxon
   */
  @RequestMapping(value = "/taxon/{identifier}", method = RequestMethod.GET)
  public final ModelAndView getTaxonPage(@PathVariable final String identifier) {
      ModelAndView modelAndView = new ModelAndView("taxonPage");
      modelAndView.addObject(service.load(identifier, "taxon-page"));
      return modelAndView;
  }

    /**
     * @param identifier
     *            Set the identifier of the taxon
     * @return A model and view containing a taxon
     */
    @RequestMapping(value = "/taxon/{identifier}",
                    method = RequestMethod.GET,
                    headers = "Accept=application/json")
    public final ResponseEntity<Taxon> get(
            @PathVariable final String identifier) {
        return new ResponseEntity<Taxon>(service.find(identifier),
                HttpStatus.OK);
    }

  /**
   * @param taxon
   *            the taxon to save
   * @return A response entity containing a newly created taxon
   */
    @RequestMapping(value = "/taxon",
                    method = RequestMethod.POST)
    public final ResponseEntity<Taxon> create(
            @RequestBody final Taxon taxon) {
        HttpHeaders httpHeaders = new HttpHeaders();
        try {
            httpHeaders.setLocation(new URI(baseUrl + "taxon/" + taxon.getIdentifier()));
        } catch (URISyntaxException e) {
            logger.error(e.getMessage());
        }
        ResponseEntity<Taxon> response = new ResponseEntity<Taxon>(
                service.save(taxon), httpHeaders, HttpStatus.CREATED);
        return response;
    }

  /**
   * @param identifier
   *            Set the identifier of the taxon
   * @return A response entity containing the status
   */
    @RequestMapping(value = "/taxon/{identifier}",
                    method = RequestMethod.DELETE)
    public final ResponseEntity<Taxon> delete(
            @PathVariable final String identifier) {
        service.delete(identifier);
        return new ResponseEntity<Taxon>(HttpStatus.OK);
    }

}
