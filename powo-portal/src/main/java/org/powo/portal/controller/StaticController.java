package org.powo.portal.controller;

import org.powo.site.Site;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class StaticController {

	@Autowired
	@Qualifier("currentSite")
	Site site;

	@RequestMapping(value = "/generic-error", method = RequestMethod.GET, produces = {"text/html"})
	public String error(Model model) {
		site.populateStaticModel(model);
		return "/generic_error";
	}

	@RequestMapping(value = "/not-found-error", method = RequestMethod.GET, produces = {"text/html"})
	public String notFoundError(Model model) {
		site.populateStaticModel(model);
		return "/not_found_error";
	}

	@RequestMapping(value = "/privacy", method = RequestMethod.GET, produces = {"text/html"})
	public String privacyPolicy(Model model) {
		site.populateStaticModel(model);
		return "static/privacy";
	}

	@RequestMapping(value = "/terms-and-conditions", method = RequestMethod.GET, produces = {"text/html"})
	public String termsAndConditions(Model model) {
		site.populateStaticModel(model);
		return "static/terms_and_conditions";
	}

	@RequestMapping(value = "/about", method = RequestMethod.GET, produces = {"text/html"})
	public String about(Model model) {
		site.populateStaticModel(model);
		return "static/about";
	}

	@RequestMapping(value = "/search-help", method = RequestMethod.GET, produces = {"text/html"})
	public String searchHelp(Model model) {
		site.populateStaticModel(model);
		return "static/search_help";
	}

}
