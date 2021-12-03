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


	@RequestMapping(value = "/what-we-do", method = RequestMethod.GET, produces = {"text/html"})
  public String whatWeDo(Model model) {
		model.addAttribute("title", messages.getMessage("site.privacy.title", null, site.defaultLocale()));
    return "static/what_we_do";
  }

	@RequestMapping(value = "/cite-us", method = RequestMethod.GET, produces = {"text/html"})
  public String citeUs(Model model) {
		var yearFormat = new SimpleDateFormat("YYYY");
		var dateFormat = new SimpleDateFormat("dd MMMMM YYYY");
		var date = new Date();
		model.addAttribute("citationYear", yearFormat.format(date));
		model.addAttribute("citationDate", dateFormat.format(date));
    return "static/cite_us";
  }

	@RequestMapping(value = "/about", method = RequestMethod.GET, produces = {"text/html"})
	public String about(Model model) {
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
