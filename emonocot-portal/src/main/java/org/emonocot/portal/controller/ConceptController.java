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

@Controller
@RequestMapping("/term")
public class ConceptController extends GenericController<Concept, ConceptService> {

	private static Logger queryLog = LoggerFactory.getLogger("query");

	@Autowired
	public final void setConceptService(ConceptService conceptService) {
		super.setService(conceptService);
	}

	public ConceptController() {
		super("term", Concept.class);
	}

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
