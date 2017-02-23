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
	public String not_found_error(){
		return "not_found_error";
	}
}
