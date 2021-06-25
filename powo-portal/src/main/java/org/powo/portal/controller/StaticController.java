package org.powo.portal.controller;

import org.powo.site.Site;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class StaticController extends LayoutController {

	@Autowired
	@Qualifier("currentSite")
	Site site;

	@RequestMapping(value = "/privacy", method = RequestMethod.GET, produces = {"text/html"})
	public String privacyPolicy(Model model) {
		return "static/privacy";
	}

	@RequestMapping(value = "/terms-and-conditions", method = RequestMethod.GET, produces = {"text/html"})
	public String termsAndConditions(Model model) {
		return "static/terms_and_conditions";
	}

	@RequestMapping(value = "/what-we-do", method = RequestMethod.GET, produces = {"text/html"})
  public String whatWeDo(Model model) {
    return "static/what_we_do";
  }

	@RequestMapping(value = "/about", method = RequestMethod.GET, produces = {"text/html"})
	public String about(Model model) {
		var yearFormat = new SimpleDateFormat("YYYY");
		var dateFormat = new SimpleDateFormat("dd MM YYYY");
		var date = new Date();
		model.addAttribute("citationYear", yearFormat.format(date));
		model.addAttribute("citationDate", dateFormat.format(date));
		return "static/about";
	}

	@RequestMapping(value = "/search-help", method = RequestMethod.GET, produces = {"text/html"})
	public String searchHelp(Model model) {
		return "static/search_help";
	}
	@RequestMapping(value = "/contact", method = RequestMethod.GET, produces = {"text/html"})
	public String contact(Model model) {
		return "static/contact";
	}

}
