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
package org.powo.portal.controller;

import org.powo.api.TaxonService;
import org.powo.common.IdUtil;
import org.powo.model.Taxon;
import org.powo.site.Site;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/taxon")
public class TaxonController {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(TaxonController.class);

	@Autowired
	@Qualifier("currentSite")
	Site site;

	@Autowired
	TaxonService service;

	@RequestMapping(path = {"/urn:lsid:ipni.org:names:{identifier}", "/{identifier}"}, method = RequestMethod.GET, produces = {"text/html", "*/*"})
	public String show(@PathVariable String identifier, Model model) {
		Taxon taxon = service.load(IdUtil.fqName(identifier), "object-page");
		site.populateTaxonModel(taxon, model);
		model.addAttribute("title", site.taxonPageTitle(taxon));
		return "taxon";
	}
}
