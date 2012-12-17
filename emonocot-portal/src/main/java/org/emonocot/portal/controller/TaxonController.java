package org.emonocot.portal.controller;

import org.emonocot.api.TaxonService;
import org.emonocot.model.Taxon;
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
    public final void setTaxonService(final TaxonService taxonService) {
        super.setService(taxonService);
    }

    /**
     * @param identifier
     *            Set the identifier of the taxon
     * @param model Set the model
     * @return A model and view containing a taxon
     */
    @RequestMapping(value = "/{identifier}", method = RequestMethod.GET, produces = {"text/html", "*/*"})
    public final String show(@PathVariable final String identifier, final Model model) {
        model.addAttribute(getService().load(identifier, "taxon-page"));
        return "taxon/show";
    }
}
