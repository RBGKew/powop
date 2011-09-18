package org.emonocot.portal.controller;

import org.emonocot.model.taxon.Taxon;
import org.emonocot.service.TaxonService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

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
   private TaxonService taxonService;
   
   /**
   *
   * @param taxonService
   *            Set the taxon service
   */
  @Autowired
  public final void setTaxonService(final TaxonService taxonService) {
      this.taxonService = taxonService;
  }

  /**
   * @param identifier
   *            Set the identifier of the taxon
   * @return A model and view containing a taxon
   */
  @RequestMapping(value = "/taxon/{identifier}", method = RequestMethod.GET)
  public final ModelAndView getTaxon(@PathVariable final String identifier) {
      ModelAndView modelAndView = new ModelAndView("taxonPage");
      modelAndView.addObject(taxonService.load(identifier, "taxon-page"));
      return modelAndView;
  }
  
  /**
   * @param taxon
   *            the taxon to save
   * @return A response entity containing a newly created taxon
   */
  @RequestMapping(value = "/taxon", method = RequestMethod.POST, headers="Accept=application/json")
  public final ResponseEntity<Taxon> postTaxon(@RequestBody Taxon taxon) {
      return new ResponseEntity<Taxon>(taxonService.save(taxon),HttpStatus.CREATED);
  }
  
  /**
   * @param identifier
   *            Set the identifier of the taxon
   * @return A response entity containing the status
   */
  @RequestMapping(value = "/taxon/{identifier}", method = RequestMethod.DELETE, headers="Accept=application/json")
  public final ResponseEntity<Taxon> deleteTaxon(@PathVariable final String identifier) {
      taxonService.delete(identifier);
      return new ResponseEntity<Taxon>(HttpStatus.OK);
  }

}
