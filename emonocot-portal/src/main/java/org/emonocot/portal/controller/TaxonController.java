package org.emonocot.portal.controller;

import org.emonocot.api.TaxonService;
import org.emonocot.model.Taxon;
import org.gbif.ecat.voc.EstablishmentMeans;
import org.gbif.ecat.voc.OccurrenceStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author ben
 *
 */
@Controller
@RequestMapping("/taxon")
public class TaxonController extends GenericController<Taxon, TaxonService> {
	
	private static Logger queryLog = LoggerFactory.getLogger("query");

    /**
     *
     */
    public TaxonController() {
        super("taxon");
    }

    /**
     *
     * @param taxonService
     *            Set the taxon service
     */
    @Autowired
    public void setTaxonService(TaxonService taxonService) {
        super.setService(taxonService);
    }

    /**
     * @param identifier
     *            Set the identifier of the taxon
     * @param model Set the model
     * @return A model and view containing a taxon
     */
    @RequestMapping(value = "/{identifier}", method = RequestMethod.GET, produces = {"text/html", "*/*"})
    public String show(@PathVariable String identifier, Model model) {
        model.addAttribute(getService().load(identifier,"taxon-page"));
        model.addAttribute("present", OccurrenceStatus.Present);
        model.addAttribute("absent", OccurrenceStatus.Absent);
        model.addAttribute("nativ", EstablishmentMeans.Native); // native is a keyword in java so we can't use it as a JSP variable, at least in tomcat 
        model.addAttribute("introduced", EstablishmentMeans.Introduced);
        queryLog.info("Taxon: \'{}\'", new Object[] {identifier});
        return "taxon/show";
    }
    
    /**
     * Many users visit a taxon page and then navigate to the document above, then bounce
     */
    @RequestMapping(method = RequestMethod.GET, produces = {"text/html", "*/*"})
    public String list(Model model) {
    	return "redirect:/search?facet=base.class_s%3aorg.emonocot.model.Taxon";
    }
}
