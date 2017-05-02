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

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.emonocot.api.UserService;
import org.emonocot.model.SecuredObject;
import org.emonocot.model.auth.User;
import org.emonocot.pager.Page;
import org.emonocot.portal.controller.form.AceDto;
import org.emonocot.portal.format.annotation.FacetRequestFormat;
import org.emonocot.portal.legacy.OldSearchBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user")
public class UserController extends GenericController<User, UserService> {

	private static Logger logger = LoggerFactory.getLogger(UserController.class);

	public UserController() {
		super("user", User.class);
	}

	@Autowired
	public final void setUserService(final UserService userService) {
		super.setService(userService);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "text/html", params = {"!form", "!delete"})
	public String show(@PathVariable Long id, Model uiModel) {
		uiModel.addAttribute(getService().find(id));
		return "user/show";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, params = "form", produces = "text/html")
	public String update(@PathVariable Long id, Model uiModel) {
		uiModel.addAttribute(getService().find(id));
		return "user/update";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.POST, produces = "text/html")
	public String update(@Valid User user, @PathVariable Long id,
			BindingResult result,
			Model model,
			RedirectAttributes redirectAttributes) throws Exception {
		logger.error("POST /user/" + id);
		User persistedUser = getService().load(id);

		if (result.hasErrors()) {
			logger.error("result.hasErrors()");
			return "user/update";
		}
		persistedUser.setAccountName(user.getAccountName());
		persistedUser.setFamilyName(user.getFamilyName());
		persistedUser.setFirstName(user.getFirstName());
		persistedUser.setHomepage(user.getHomepage());
		persistedUser.setName(user.getName());
		persistedUser.setOrganization(user.getOrganization());
		persistedUser.setTopicInterest(user.getTopicInterest());
		persistedUser.setNotifyByEmail(user.isNotifyByEmail());
		persistedUser.setEnabled(user.isEnabled());
		persistedUser.setAccountNonLocked(user.isAccountNonLocked());
		logger.error("accountNonLocked " + user.isAccountNonLocked());
		logger.error("enabled " + user.isEnabled());
		logger.error("saving");
		getService().saveOrUpdate(persistedUser);
		String[] codes = new String[] {"user.updated" };
		Object[] args = new Object[] {user.getAccountName()};
		DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(codes, args);
		redirectAttributes.addFlashAttribute("info", message);
		logger.error("saved");
		return "redirect:/user/{id}";
	}

	@RequestMapping(produces = "text/html", method = RequestMethod.GET)
	public String list(Model model,
			@RequestParam(value = "query", required = false) String query,
			@RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
			@RequestParam(value = "start", required = false, defaultValue = "0") Integer start,
			@RequestParam(value = "facet", required = false) @FacetRequestFormat List<FacetRequest> facets,
			@RequestParam(value = "sort", required = false) String sort,
			@RequestParam(value = "view", required = false) String view) throws SolrServerException, IOException {
		Map<String, String> selectedFacets = new HashMap<String, String>();
		if (facets != null && !facets.isEmpty()) {
			for (FacetRequest facetRequest : facets) {
				selectedFacets.put(facetRequest.getFacet(), facetRequest.getSelected());
			}
		}
		selectedFacets.put("base.class_s", "org.emonocot.model.auth.User");
		SolrQuery solrQuery = new OldSearchBuilder().oldSearchBuilder
		(query, null, limit, start, null, null, selectedFacets, sort, null);
		Page<User> result = getService().search(solrQuery, null);
		result.putParam("query", query);
		model.addAttribute("result", result);
		return "user/list";
	}

	@RequestMapping(value = "/{id}",  method = RequestMethod.GET, params = "delete", produces = "text/html")
	public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		User user = getService().find(id);
		getService().deleteUser(user.getUsername());
		String[] codes = new String[] { "user.deleted" };
		Object[] args = new Object[] { user.getUsername() };
		DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(codes, args);
		redirectAttributes.addFlashAttribute("info", message);
		return "redirect:/user";
	}
}
