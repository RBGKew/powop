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

import org.emonocot.api.PhylogeneticTreeService;
import org.emonocot.model.IdentificationKey;
import org.emonocot.model.PhylogeneticTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author jk00kg
 */
@Controller
@RequestMapping("/phylo")
public class PhylogeneticTreeController extends GenericController<PhylogeneticTree, PhylogeneticTreeService> {

	private static Logger queryLog = LoggerFactory.getLogger("query");

	public PhylogeneticTreeController() {
		super("phylo", PhylogeneticTree.class);
	}

	/**
	 * @param newIdentificationKeyService
	 *            Set the identification key service
	 */
	@Autowired
	public void setPhylogeneticTreeService(PhylogeneticTreeService newPhylogeneticTreeService) {
		super.setService(newPhylogeneticTreeService);
	}

	/**
	 * @param identifier
	 *            The identifier of the identification key
	 * @param model
	 *            The model
	 * @return The name of the view
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, params = "!delete", produces = {"text/html", "*/*"})
	public String getPage(@PathVariable Long id,
			Model model) {
		PhylogeneticTree tree = getService().load(id,"object-page");
		model.addAttribute(tree);
		queryLog.info("PhylogeneticTree: \'{}\'", new Object[] {id});
		return "phylo/show";
	}

	/**
	 * Many users visit a taxon page and then navigate to the document above, then bounce
	 */
	@RequestMapping(method = RequestMethod.GET, produces = {"text/html", "*/*"})
	public String list(Model model) {
		return "redirect:/search?facet=base.class_s%3aorg.emonocot.model.PhylogeneticTree";
	}

	@RequestMapping(value = "/{id}",  method = RequestMethod.GET, params = "delete", produces = "text/html")
	public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		PhylogeneticTree tree = getService().load(id);
		getService().deleteById(id);
		String[] codes = new String[] { "phylogeny.deleted" };
		Object[] args = new Object[] { tree.getTitle() };
		DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(codes, args);
		redirectAttributes.addFlashAttribute("info", message);
		return "redirect:/search?facet=base.class_s%3aorg.emonocot.model.PhylogeneticTree";
	}
}
