package org.powo.portal.controller;

import javax.servlet.http.HttpServletResponse;

import org.powo.site.Site;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ExceptionHandlerController {

	@Autowired
	@Qualifier("currentSite")
	Site site;

	@RequestMapping(value = "/not-found-error", method = RequestMethod.GET, produces = {"text/html"})
	public String notFoundError(HttpServletResponse response, Model model) {
		site.populateStaticModel(model);
		response.setStatus(404);
		return "/not_found_error";
	}

	@RequestMapping(value = "/generic-error", method = RequestMethod.GET, produces = {"text/html"})
	public String error(HttpServletResponse response, Model model) {
		site.populateStaticModel(model);
		response.setStatus(500);
		return "/generic_error";
	}
}
