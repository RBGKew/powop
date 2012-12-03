/**
 * 
 */
package org.emonocot.portal.controller;

import java.util.List;

import org.emonocot.api.OrganisationService;
import org.emonocot.model.registry.Organisation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author jk00kg
 *
 */
@Controller
public class ProjectPageController {
	
	/**
	 * A list of identifiers for 'sources' not to be listed on the page
	 */
	private String[] excludeIdentifiers = {"WCS", "e-monocot.org"};

    /**
     *
     */
    private OrganisationService sourceService;

    /**
     * @param service Set the Source service
     */
    @Autowired
    public void setUserService(final OrganisationService service) {
        sourceService = service;
    }

    /**
     * @param model Set the model
     * @return A model and view containing a user
     */
    @RequestMapping(value = "/about")
    public final String show(final Model model) {
    	List<Organisation> sources = sourceService.list(null, null, null).getRecords();
    	
    	//Page specific remove
    	for (String identifier : excludeIdentifiers) {
			for (Organisation source : sources) {
				if(identifier.equals(source.getIdentifier())){
					sources.remove(source);
					break;
				}
			}
		}
        
    	model.addAttribute("sources", sources);
        return "about";
    }

}
