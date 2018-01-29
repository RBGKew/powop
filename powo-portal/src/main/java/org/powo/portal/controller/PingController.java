package org.powo.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/ping")
public class PingController {
	@RequestMapping(method = RequestMethod.GET, produces = "text/plain")
	@ResponseBody
	public String ping() {
		return "Ok";
	}
}
