package org.emonocot.portal.controller;


import org.emonocot.api.ConceptService;
import org.emonocot.model.Concept;
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
@RequestMapping("/term")
public class ConceptController extends GenericController<Concept, ConceptService> {
	
	private static Logger queryLog = LoggerFactory.getLogger("query");

    /**
     *
     * @param imageService
     *            Set the image service
     */
    @Autowired
    public final void setConceptService(ConceptService conceptService) {
        super.setService(conceptService);
    }

    /**
     *
     */
    public ConceptController() {
       super("term");
    }

    /**
     * @param identifier
     *            Set the identifier of the image
     * @param model Set the model
     * @return the name of the view
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = {"text/html", "*/*"})
    public final String show(@PathVariable final Long id, final Model model) {
        Concept concept = getService().load(id,"concept-page");
        model.addAttribute(concept);
        queryLog.info("Concept: \'{}\'", new Object[] {id});
        return "concept/show";
    }
    
    /**
     * Many users visit a taxon page and then navigate to the document above, then bounce
     */
    @RequestMapping(method = RequestMethod.GET, produces = {"text/html", "*/*"})
    public String list(Model model) {
    	return "redirect:/search?facet=base.class_s%3aorg.emonocot.model.Concept";
    }
}
