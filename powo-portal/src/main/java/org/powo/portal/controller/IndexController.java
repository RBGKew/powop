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

import com.google.common.base.Strings;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class IndexController extends LayoutController {

	@RequestMapping(method = RequestMethod.GET, produces = "text/html")
	public String index(
		Model model,
		@RequestParam(name = "q", required = false) String query,
		@RequestParam(name = "f", required = false) String filterQuery
	) {
		if (!Strings.isNullOrEmpty(query) || !Strings.isNullOrEmpty(filterQuery)) {
			model.asMap().clear();
			model.addAttribute("q", query);
			model.addAttribute("f", filterQuery);
			return "redirect:/results";			
		}

		model.addAttribute("title", site.indexPageTitle());
		model.addAttribute("cross-site-link", site.crossSiteLink());
		model.addAttribute("cross-site-type", site.crossSiteType());
		model.addAttribute("featured-taxa-sections", site.featuredTaxaSections());
		var taxonCounts = site.getFormattedTaxonCounts();
		for (var key : taxonCounts.keySet()) {
			model.addAttribute(key, taxonCounts.get(key));
		}
		return "index";
	}
}
