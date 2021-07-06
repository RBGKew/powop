package org.powo.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/results")
public class ResultsController extends LayoutController {

	@RequestMapping(method = RequestMethod.GET, produces = "text/html")
	public String results(Model model) {
		model.addAttribute("title", site.indexPageTitle());
		model.addAttribute("cross-site-link", site.crossSiteLink());
		model.addAttribute("cross-site-type", site.crossSiteType());
		model.addAttribute("featured-taxa-sections", site.featuredTaxaSections());
		var taxonCounts = site.getFormattedTaxonCounts();
		for (var key : taxonCounts.keySet()) {
			model.addAttribute(key, taxonCounts.get(key));
		}
		return "results";
	}
}
