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

import org.emonocot.api.GroupService;
import org.emonocot.api.UserService;
import org.emonocot.model.SecuredObject;
import org.emonocot.model.auth.Group;
import org.emonocot.model.auth.User;
import org.emonocot.model.registry.Organisation;
import org.emonocot.portal.controller.form.AceDto;
import org.emonocot.portal.format.annotation.PermissionFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.Permission;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/group")
public class GroupController extends GenericController<Group, GroupService> {

	public GroupController() {
		super("group", Group.class);
	}

	private static Logger logger = LoggerFactory.getLogger(GroupController.class);

	private UserService userService;

	private ConversionService conversionService;

	@Autowired
	public void setGroupService(GroupService groupService) {
		super.setService(groupService);
	}

	@Autowired
	public void setUserService(UserService newUserService) {
		this.userService = newUserService;
	}

	@Autowired
	public void setConversionService(ConversionService conversionService) {
		this.conversionService = conversionService;
	}

	@RequestMapping(value = "/{identifier}/permission", params = "!delete", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public ResponseEntity<AceDto> addPermission(
			@PathVariable String identifier, @RequestBody AceDto ace) {
		SecuredObject object = conversionService.convert(ace, SecuredObject.class);
		getService().addPermission(object, identifier, ace.getPermission(), ace.getClazz());
		ResponseEntity<AceDto> responseEntity = new ResponseEntity<AceDto>(ace, HttpStatus.CREATED);
		return responseEntity;
	}

	@RequestMapping(value = "/{identifier}/permission", params = "delete", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public ResponseEntity<AceDto> deletePermission(@PathVariable String identifier, @RequestBody AceDto ace) {
		SecuredObject object = conversionService.convert(ace, SecuredObject.class);
		userService.deletePermission(object, identifier, ace.getPermission(), ace.getClazz());
		return new ResponseEntity<AceDto>(ace, HttpStatus.OK);
	}

	@RequestMapping(value = "/{groupIdentifier}", params = { "members", "!delete" }, method = RequestMethod.POST, produces = "text/html")
	public String addMember(@PathVariable String groupIdentifier,
			@ModelAttribute("user") User user,
			RedirectAttributes redirectAttributes) {
		userService.addUserToGroup(user.getUsername(), groupIdentifier);
		String[] codes = new String[] {"user.added.to.group" };
		Object[] args = new Object[] {user.getUsername() };
		DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(codes, args);
		redirectAttributes.addFlashAttribute("info", message);
		return "redirect:/group/" + groupIdentifier + "?form";
	}

	@RequestMapping(value = "/{groupIdentifier}", params = { "members",
	"delete" }, method = RequestMethod.GET, produces = "text/html")
	public String removeMember(@PathVariable String groupIdentifier,
			@RequestParam String user, RedirectAttributes redirectAttributes) {
		userService.removeUserFromGroup(user, groupIdentifier);
		String[] codes = new String[] {"user.removed.from.group"};
		Object[] args = new Object[] {user};
		DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable( codes, args);
		redirectAttributes.addFlashAttribute("info", message);
		return "redirect:/group/" + groupIdentifier + "?form";
	}

	@RequestMapping(params = "!form", method = RequestMethod.GET, produces = "text/html")
	public String list(
			@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
			@RequestParam(value = "size", defaultValue = "10", required = false) Integer size,
			Model model) {
		model.addAttribute("result", getService().list(page, size, null));
		return "group/list";
	}

	@RequestMapping(params = "form", method = RequestMethod.GET, produces = "text/html")
	public String create(Model model) {
		model.addAttribute(new Group());
		return "group/create";
	}

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String create(@Valid Group group,
			BindingResult result,
			Model model,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return "group/create";
		}
		getService().save(group);
		String[] codes = new String[] {"group.created" };
		Object[] args = new Object[] {group.getIdentifier()};
		DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(codes, args);
		redirectAttributes.addFlashAttribute("info", message);
		return "redirect:/group";
	}

	@RequestMapping(value = "/{identifier}", params = {"!form","!delete"}, method = RequestMethod.GET, produces = "text/html")
	public String show(
			@PathVariable("identifier") String identifier,
			Model model) {
		model.addAttribute("group", getService().load(identifier, "group-page"));
		model.addAttribute("aces", getService().listAces(identifier));
		return "group/show";
	}

	@RequestMapping(value = "/{identifier}", params = "form", method = RequestMethod.GET, produces = "text/html")
	public String updateForm(
			@PathVariable("identifier") String identifier,
			Model model) {
		model.addAttribute("group", getService().load(identifier, "group-page"));
		model.addAttribute("aces", getService().listAces(identifier));
		model.addAttribute("user", new User());
		AceDto aceDto  = new AceDto();
		aceDto.setClazz(Organisation.class);
		aceDto.setPermission(BasePermission.READ);
		model.addAttribute("ace", aceDto);
		return "group/update";
	}

	public String addAce(@PathVariable String identifier,
			@ModelAttribute("ace") AceDto ace,
			RedirectAttributes redirectAttributes) {
		SecuredObject object = conversionService.convert(ace, SecuredObject.class);
		getService().addPermission(object, identifier, ace.getPermission(), ace.getClazz());
		String[] codes = new String[] {"ace.added.to.group" };
		Object[] args = new Object[] {
				conversionService.convert(ace.getPermission(), String.class),
				ace.getClazz().getSimpleName(), ace.getObject() };
		DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(codes, args);
		redirectAttributes.addFlashAttribute("info", message);
		return "redirect:/group/" + identifier + "?form";
	}

	@RequestMapping(value = "/{identifier}", params = { "aces",
	"delete" }, method = RequestMethod.GET, produces = "text/html")
	public String removeAce(@PathVariable String identifier,
			@RequestParam String object,
			@RequestParam Class clazz,
			@RequestParam @PermissionFormat Permission permission,
			RedirectAttributes redirectAttributes) {
		AceDto ace = new AceDto();
		ace.setClazz(clazz);
		ace.setObject(object);
		ace.setPrincipal(identifier);
		SecuredObject securedObject = conversionService.convert(ace, SecuredObject.class);
		getService().deletePermission(securedObject, identifier, permission, clazz);
		String[] codes = new String[] {"ace.removed.from.group" };
		Object[] args = new Object[] {
				conversionService.convert(permission, String.class),
				clazz.getSimpleName(), ace.getObject() };
		DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(codes, args);
		redirectAttributes.addFlashAttribute("info", message);
		return "redirect:/group/" + identifier + "?form";
	}

	@RequestMapping(value = "/{identifier}",  method = RequestMethod.GET, params = "delete", produces = "text/html")
	public String delete(@PathVariable String identifier, RedirectAttributes redirectAttributes) {
		Group group = getService().find(identifier);
		userService.deleteGroup(identifier);
		String[] codes = new String[] { "group.deleted" };
		Object[] args = new Object[] { group.getName() };
		DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(codes, args);
		redirectAttributes.addFlashAttribute("info", message);
		return "redirect:/group";
	}
}
