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

import java.text.NumberFormat;
import java.util.Locale;

import org.powo.api.DescriptionService;
import org.powo.api.ImageService;
import org.powo.api.TaxonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class IndexController {

	@Autowired
	TaxonService taxonService;

	@Autowired
	ImageService imageService;

	@Autowired
	DescriptionService descriptionService;

	@RequestMapping(method = RequestMethod.GET, produces = "text/html")
	public String index(Model model) {
		model.addAttribute("names", format(taxonService.count(), 1000));
		model.addAttribute("images", format(imageService.count(), 100));
		model.addAttribute("descriptions", format(descriptionService.countAccounts(), 100));
		return "index";
	}

	private String format(long n, int ceilTo) {
		return NumberFormat.getNumberInstance(Locale.UK).format(((n + (ceilTo-1)) / ceilTo) * ceilTo);
	}
}
