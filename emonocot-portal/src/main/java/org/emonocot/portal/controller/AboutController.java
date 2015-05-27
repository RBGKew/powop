/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
package org.emonocot.portal.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;
import org.emonocot.api.OrganisationService;
import org.emonocot.api.SearchableObjectService;
import org.emonocot.model.SearchableObject;
import org.emonocot.model.registry.Organisation;
import org.emonocot.pager.Page;
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

	private SearchableObjectService searchableObjectService;

	
	@Autowired
	public void setSearchableObjectService(SearchableObjectService searchableObjectService) {
		this.searchableObjectService = searchableObjectService;
	}


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
    	
    	// Cope with solr unavailability
    			try {
    			    List<String> responseFacets = new ArrayList<String>();
    			    responseFacets.add("base.class_s");
    			    Page<SearchableObject> stats = searchableObjectService.search("", null, 1, 0, responseFacets.toArray(new String[1]), null, null, null, null);
    			    model.addAttribute("stats", stats);
    			} catch (SolrServerException sse) {
    				
    			}	
        return "about";
    }

}
