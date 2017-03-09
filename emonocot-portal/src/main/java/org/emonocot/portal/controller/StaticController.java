package org.emonocot.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class StaticController {

	@RequestMapping(value = "/generic_error", method = RequestMethod.GET, produces = {"text/html"})
	public String error(){
		return "generic_error";
	}
	
	@RequestMapping(value = "/not_found_error", method = RequestMethod.GET, produces = {"text/html"})
	public String notFoundError(){
		return "not_found_error";
	}
	
	@RequestMapping(value = "/cookie_policy", method = RequestMethod.GET, produces = {"text/html"})
	public String cookie_policy(){
		return "cookie_policy";
	}
	
	@RequestMapping(value = "/terms_and_conditions", method = RequestMethod.GET, produces = {"text/html"})
	public String terms_and_conditions(){
		return "terms_and_conditions";
	}
	
	@RequestMapping(value = "/about", method = RequestMethod.GET, produces = {"text/html"})
	public String about(){
		return "about";
	}
	
	@RequestMapping(value = "/search_help", method = RequestMethod.GET, produces = {"text/html"})
	public String search_help(){
		return "search_help";
	}
}
