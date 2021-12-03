package org.powo.portal.controller;

import org.powo.site.Site;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
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

	@Autowired
	private MessageSource messages;
	
	@RequestMapping(value = "/what-we-do", method = RequestMethod.GET, produces = {"text/html"})
  public String whatWeDo(Model model) {
		model.addAttribute("title", messages.getMessage("site.static.title", new Object[] { "What we do" }, site.defaultLocale()));
    return "static/what_we_do";
  }
	
	@RequestMapping(value = "/cite-us", method = RequestMethod.GET, produces = {"text/html"})
  public String citeUs(Model model) {
		var yearFormat = new SimpleDateFormat("YYYY");
		var dateFormat = new SimpleDateFormat("dd MMMMM YYYY");
		var date = new Date();
		model.addAttribute("citationYear", yearFormat.format(date));
		model.addAttribute("citationDate", dateFormat.format(date));
		model.addAttribute("title", messages.getMessage("site.static.title", new Object[] { "Cite us" }, site.defaultLocale()));
    return "static/cite_us";
  }

	@RequestMapping(value = "/about", method = RequestMethod.GET, produces = {"text/html"})
	public String about(Model model) {
		model.addAttribute("title", messages.getMessage("site.static.title", new Object[] { "About" }, site.defaultLocale()));
		return "static/about";
	}

	@RequestMapping(value = "/search-help", method = RequestMethod.GET, produces = {"text/html"})
	public String searchHelp(Model model) {
		model.addAttribute("title", messages.getMessage("site.static.title", new Object[] { "Help" }, site.defaultLocale()));
		return "static/search_help";
	}
	@RequestMapping(value = "/contact", method = RequestMethod.GET, produces = {"text/html"})
	public String contact(Model model) {
		model.addAttribute("title", messages.getMessage("site.static.title", new Object[] { "Contact" }, site.defaultLocale()));
		return "static/contact";
	}

}
