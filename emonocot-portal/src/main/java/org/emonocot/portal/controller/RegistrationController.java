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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import org.emonocot.api.UserService;
import org.emonocot.model.auth.User;
import org.emonocot.portal.controller.form.RegistrationForm;
import org.emonocot.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author ben
 *
 */
@Controller
public class RegistrationController {

	private UserService service;

	private EmailService emailService;

	private String baseUrl;

	@Autowired
	public void setUserService(UserService userService) {
		this.service = userService;
	}

	@Autowired
	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	/**
	 *
	 * @return a model and view containing a registration form object
	 */
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String setupForm(Model model) {
		model.addAttribute(new RegistrationForm());
		return "register";
	}

	/**
	 *
	 * @param form
	 *            Set the registration form
	 * @param result
	 *            Set the binding results
	 * @param session
	 *            Set the http session
	 * @param model
	 *            Set the model
	 * @return the view name
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String post(
			@Valid @ModelAttribute("registrationForm") RegistrationForm form,
			BindingResult result, Model model, RedirectAttributes redirectAttributes) throws Exception {
		if (result.hasErrors()) {
			return "register";
		}

		User user = new User();
		user.setUsername(form.getUsername());
		user.setPassword(form.getPassword());
		user.setAccountName(form.getAccountName());
		user.setFamilyName(form.getFamilyName());
		user.setFirstName(form.getFirstName());
		user.setName(form.getName());
		user.setHomepage(form.getHomepage());
		user.setOrganization(form.getOrganization());
		user.setTopicInterest(form.getTopicInterest());
		user.setApiKey(UUID.randomUUID().toString());
		try {
			user.setImg(service.makeProfileThumbnail(form.getImg(), null));
		} catch(UnsupportedOperationException uoe) {
			String[] codes = new String[] {"unsupported.image.mimetype" };
			Object[] args = new Object[] {uoe.getMessage()};
			DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(codes, args);
			model.addAttribute("error", message);
			return "register";
		}
		user.setAccountNonLocked(true);
		user.setAccountNonExpired(true);
		user.setCredentialsNonExpired(true);
		user.setEnabled(false);

		service.createUser(user);
		String nonce = service.createNonce(user.getUsername());

		Map<String,Object> map = new HashMap<String,Object>();
		map.put("user", user);
		map.put("nonce", nonce);
		map.put("baseUrl", baseUrl);
		emailService.sendEmail("org/emonocot/portal/controller/ActivateAccountRequest.vm", map, user.getUsername(), "Welcome! Your account requires activation");

		String[] codes = new String[] {"registration.successful" };
		Object[] args = new Object[] {};
		DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(codes, args);
		redirectAttributes.addFlashAttribute("info", message);
		return "redirect:/login";
	}
}
