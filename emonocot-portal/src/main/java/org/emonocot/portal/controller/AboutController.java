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

/**
 * @author jk00kg
 *
 */
@Controller
@RequestMapping(value = "/about")
public class AboutController {
	
	/**
	 * A list of identifiers for 'sources' not to be listed on the page
	 */
	private String[] excludeIdentifiers = {"WCS", "e-monocot.org"};

    /**
     *
     */
    private OrganisationService organisationService;

    /**
     * @param service Set the Source service
     */
    @Autowired
    public void setOrganisationService(final OrganisationService service) {
        organisationService = service;
    }

    /**
     * @param model Set the model
     * @return A model and view containing a user
     */
    @RequestMapping
    public final String show(final Model model) {
    	List<Organisation> organisations = organisationService.list(null, null, null).getRecords();
    	
    	//Page specific remove
    	for (String identifier : excludeIdentifiers) {
			for (Organisation source : organisations) {
				if(identifier.equals(source.getIdentifier())){
					organisations.remove(source);
					break;
				}
			}
		}
        
    	model.addAttribute("organisations", organisations);
        return "about";
    }

}
