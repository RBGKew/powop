package org.emonocot.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AdminController {
	
	@RequestMapping(value = "/login", method = RequestMethod.GET, produces = {"text/html"})
	public String login() {
		return("login");
	}
	
	@RequestMapping(value = "/resource", method = RequestMethod.GET, produces = {"text/html"})
	public String resource(){
		return "resource";
		
	}
	@RequestMapping(value = "/organisation", method = RequestMethod.GET, produces = {"text/html"})
	public String organisation(){
		return "organisation";
		
	}

}
