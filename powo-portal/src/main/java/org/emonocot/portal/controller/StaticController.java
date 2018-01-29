package org.emonocot.portal.controller;

import org.joda.time.DateTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class StaticController {

	@RequestMapping(value = "/generic-error", method = RequestMethod.GET, produces = {"text/html"})
	public String error() {
		return "/generic_error";
	}

	@RequestMapping(value = "/not-found-error", method = RequestMethod.GET, produces = {"text/html"})
	public String notFoundError() {
		return "/not_found_error";
	}

	@RequestMapping(value = "/privacy", method = RequestMethod.GET, produces = {"text/html"})
	public String privacyPolicy() {
		return "static/privacy";
	}

	@RequestMapping(value = "/terms-and-conditions", method = RequestMethod.GET, produces = {"text/html"})
	public String termsAndConditions(Model model) {
		model.addAttribute("date", new DateTime().toString("d MMMM y"));
		model.addAttribute("year", new DateTime().getYear());
		return "static/terms_and_conditions";
	}

	@RequestMapping(value = "/about", method = RequestMethod.GET, produces = {"text/html"})
	public String about() {
		return "static/about";
	}

	@RequestMapping(value = "/search-help", method = RequestMethod.GET, produces = {"text/html"})
	public String searchHelp() {
		return "static/search_help";
	}

}
