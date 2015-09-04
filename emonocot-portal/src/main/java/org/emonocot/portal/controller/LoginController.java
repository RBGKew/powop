/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
package org.emonocot.portal.controller;

import org.emonocot.api.UserService;
import org.emonocot.model.auth.User;
import org.emonocot.portal.controller.form.LoginForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author ben
 *
 */
@Controller
public class LoginController {

	private UserService userService;

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String setupForm(Model model) {
		model.addAttribute(new LoginForm());
		return "login";
	}

	@RequestMapping(value = "/activate/{nonce}", method = RequestMethod.GET)
	public String verify(@PathVariable("nonce") String nonce,
			@RequestParam String username,
			Model model,
			RedirectAttributes redirectAttributes) {

		if(userService.verifyNonce(username, nonce)) {
			User user = userService.load(username);
			user.setEnabled(true);
			userService.update(user);
			String[] codes = new String[] { "account.activated.successfully" };
			Object[] args = new Object[] {  };
			DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(codes, args);
			redirectAttributes.addFlashAttribute("info",message);
			return "redirect:/login";
		} else {
			String[] codes = new String[] { "account.activation.failed" };
			Object[] args = new Object[] {  };
			DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(codes, args);
			redirectAttributes.addFlashAttribute("error",message);
			return "redirect:/login";
		}


	}
}
