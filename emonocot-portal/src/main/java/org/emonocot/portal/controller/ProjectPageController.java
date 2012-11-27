/**
 * 
 */
package org.emonocot.portal.controller;

import java.util.List;

import org.emonocot.api.SourceService;
import org.emonocot.model.Source;
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
    private SourceService sourceService;

    /**
     * @param service Set the Source service
     */
    @Autowired
    public void setUserService(final SourceService service) {
        sourceService = service;
    }

    /**
     * @param model Set the model
     * @return A model and view containing a user
     */
    @RequestMapping(value = "/about")
    public final String show(final Model model) {
    	List<Source> sources = sourceService.list(null, null, null).getRecords();
    	
    	//Page specific remove
    	for (String identifier : excludeIdentifiers) {
			for (Source source : sources) {
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
