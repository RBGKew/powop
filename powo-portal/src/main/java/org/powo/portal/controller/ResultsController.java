package org.powo.portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/results")
public class ResultsController extends LayoutController {
	@Autowired
	private MessageSource messages;

	@RequestMapping(method = RequestMethod.GET, produces = "text/html")
	public String results(Model model) {
		model.addAttribute("title", messages.getMessage("site.results.title", null, site.defaultLocale()));
		var taxonCounts = site.getFormattedTaxonCounts();
		for (var key : taxonCounts.keySet()) {
			model.addAttribute(key, taxonCounts.get(key));
		}
		return "results";
	}
}
