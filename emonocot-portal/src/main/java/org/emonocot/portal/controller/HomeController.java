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

import javax.validation.Valid;

import org.emonocot.api.UserService;
import org.emonocot.model.auth.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author ben
 *
 */
@Controller
@RequestMapping("/home")
public class HomeController {

	private UserService userService;

	@Autowired
	public void setUserService(UserService service) {
		userService = service;
	}

	/**
	 * @param model Set the model
	 * @return A model and view containing a user
	 */
	@RequestMapping(method = RequestMethod.GET, params = "!form")
	public String show(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = (User) authentication.getPrincipal();
		model.addAttribute(userService.load(user.getUsername()));
		return "user/show";
	}

	@RequestMapping(method = RequestMethod.GET, params = "form")
	public String update(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User user = (User) authentication.getPrincipal();
		model.addAttribute(userService.load(user.getUsername()));
		return "home/update";
	}

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String update(@Valid User user,
			BindingResult result,
			Model model,
			RedirectAttributes redirectAttributes) throws Exception {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User authorizedUser = (User)authentication.getPrincipal();
		assert authorizedUser.equals(user);

		if (result.hasErrors()) {
			return "home/update";
		}
		authorizedUser.setAccountName(user.getAccountName());
		authorizedUser.setFamilyName(user.getFamilyName());
		authorizedUser.setFirstName(user.getFirstName());
		authorizedUser.setHomepage(user.getHomepage());
		authorizedUser.setName(user.getName());
		authorizedUser.setOrganization(user.getOrganization());
		authorizedUser.setTopicInterest(user.getTopicInterest());
		authorizedUser.setNotifyByEmail(user.isNotifyByEmail());
		try {
			String img = userService.makeProfileThumbnail(user.getImgFile(),authorizedUser.getImg());
			if(img != null) {
				authorizedUser.setImg(img);
			}
		} catch(UnsupportedOperationException uoe) {
			String[] codes = new String[] {"unsupported.image.mimetype" };
			Object[] args = new Object[] {uoe.getMessage()};
			DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(codes, args);
			model.addAttribute("error", message);
			return "home/update";
		}

		userService.saveOrUpdate(authorizedUser);
		String[] codes = new String[] {"profile.updated" };
		Object[] args = new Object[] {user.getAccountName()};
		DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(
				codes, args);
		redirectAttributes.addFlashAttribute("info", message);
		return "redirect:/home";
	}

}
