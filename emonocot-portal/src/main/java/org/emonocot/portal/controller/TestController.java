package org.emonocot.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {

	@RequestMapping(value = "/blarg")
	public final String blarg(Model model) {
		return "index";
	}

}
