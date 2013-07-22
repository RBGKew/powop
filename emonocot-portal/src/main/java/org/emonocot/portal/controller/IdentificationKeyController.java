package org.emonocot.portal.controller;

import org.emonocot.api.IdentificationKeyService;
import org.emonocot.model.IdentificationKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author jk00kg
 */
@Controller
@RequestMapping("/key")
public class IdentificationKeyController extends GenericController<IdentificationKey, IdentificationKeyService> {

    private static Logger queryLog = LoggerFactory.getLogger("query");

    public IdentificationKeyController() {
        super("key");
    }

    /**
     * @param newIdentificationKeyService
     *            Set the identification key service
     */
    @Autowired
    public void setIdentificationKeyService(IdentificationKeyService newIdentificationKeyService) {
        super.setService(newIdentificationKeyService);
    }

    /**
     * @param identifier
     *            The identifier of the identification key
     * @param model
     *            The model
     * @return The name of the view
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = {"text/html", "*/*"})
    public String getPage(@PathVariable Long id,
            Model model) {
        IdentificationKey key = getService().load(id,"object-page");
        model.addAttribute(key); 
        queryLog.info("IdentificationKey: \'{}\'", new Object[] {id});
        return "key/show";
    }
    
    /**
     * Many users visit a taxon page and then navigate to the document above, then bounce
     */
    @RequestMapping(method = RequestMethod.GET, produces = {"text/html", "*/*"})
    public String list(Model model) {
    	return "redirect:/search?facet=base.class_s%3aorg.emonocot.model.IdentificationKey";
    }

}
