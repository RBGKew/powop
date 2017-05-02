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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.groups.Default;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.emonocot.api.AnnotationService;
import org.emonocot.api.OrganisationService;
import org.emonocot.api.ResourceService;
import org.emonocot.model.constants.ResourceType;
import org.emonocot.model.registry.Resource;
import org.emonocot.model.registry.Resource.ReadResource;
import org.emonocot.pager.Page;
import org.emonocot.portal.format.annotation.FacetRequestFormat;
import org.emonocot.portal.legacy.OldSearchBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/resource")
public class ResourceController extends GenericController<Resource, ResourceService> {
	private static Logger logger = LoggerFactory.getLogger(ResourceController.class);

	private OrganisationService organisationService;

	@Autowired
	public void setResourceService(ResourceService resourceService) {
		super.setService(resourceService);
	}

	@Autowired
	public void setAnnotationService(AnnotationService annotationService) {
	}

	@Autowired
	public void setOrganisationService(OrganisationService organisationService) {
		this.organisationService = organisationService;
	}

	public ResourceController() {
		super("resource", Resource.class);
	}

	private void populateForm(Model model, Resource resource, HttpServletRequest request) {
		model.addAttribute("resource", resource);
		model.addAttribute("resourceTypes", Arrays.asList(new ResourceType[] {ResourceType.DwC_Archive, ResourceType.GBIF, ResourceType.IUCN}));
		populateCsrfToken(request, model);
	}

	@RequestMapping(produces = "text/html", method = RequestMethod.GET, params = {"!form"})
	public String list(Model model,
			@RequestParam(value = "query", required = false) String query,
			@RequestParam(value = "limit", required = false, defaultValue = "50") Integer limit,
			@RequestParam(value = "start", required = false, defaultValue = "0") Integer start,
			@RequestParam(value = "facet", required = false) @FacetRequestFormat List<FacetRequest> facets,
			@RequestParam(value = "sort", required = false) String sort,
			@RequestParam(value = "view", required = false) String view) throws SolrServerException, IOException {
		Map<String, String> selectedFacets = new HashMap<String, String>();
		if (facets != null && !facets.isEmpty()) {
			for (FacetRequest facetRequest : facets) {
				selectedFacets.put(facetRequest.getFacet(),
						facetRequest.getSelected());
			}
		}
		selectedFacets.put("base.class_searchable_b", "false");
		selectedFacets.put("base.class_s", "org.emonocot.model.registry.Resource");
		SolrQuery solrQuery = new OldSearchBuilder().oldSearchBuilder
				(query, null, limit, start,
						new String[] { "resource.exit_code_s",
								"resource.resource_type_s",
								"resource.scheduled_b",
								"resource.scheduling_period_s",
								"resource.status_s",
								"resource.last_harvested_dt",
								"resource.organisation_s"
				}, null, selectedFacets, sort, null);
		Page<Resource> result = getService().search(solrQuery, null);
		result.putParam("query", query);
		model.addAttribute("result", result);
		return "resource/list";
	}

	@RequestMapping(method = RequestMethod.GET, params = "form")
	public String create(
			@RequestParam(required = true) String organisation,
			Model model,
			HttpServletRequest request) {
		Resource resource = new Resource();
		resource.setOrganisation(organisationService.load(organisation));
		populateForm(model, resource, request);

		return "resource/form";
	}

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String create(Model model,
			@Validated({Default.class, ReadResource.class}) Resource resource,
			BindingResult result,
			RedirectAttributes redirectAttributes,
			HttpServletRequest request) {

		if (result.hasErrors()) {
			populateForm(model, resource, request);
			model.addAttribute("errors", result.getAllErrors());
			return "resource/form";
		}

		logger.error("Creating Resource " + resource + " with organisation " + resource.getOrganisation());
		getService().saveOrUpdate(resource);
		String[] codes = new String[] { "resource.was.created" };
		Object[] args = new Object[] { resource.getTitle() };
		DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(codes, args);
		redirectAttributes.addFlashAttribute("info", message);

		return "redirect:/resource/" + resource.getId();
	}

	@RequestMapping(value = "/{resourceId}", method = RequestMethod.GET, produces = "text/html", params = {"!run", "!form", "!parameters", "!delete"})
	public String show(@PathVariable Long resourceId, Model uiModel) {
		Resource resource = getService().load(resourceId,"job-with-source");
		uiModel.addAttribute("resource", resource);
		return "resource/show";
	}

	@RequestMapping(value = "/{resourceId}", method = RequestMethod.GET, params = "form")
	public String update(
			@PathVariable Long resourceId,
			Model model,
			HttpServletRequest request) {
		Resource resource = getService().load(resourceId,"job-with-source");
		populateForm(model, resource, request);
		return "resource/form";
	}

	@RequestMapping(value = "/{resourceId}", method = RequestMethod.POST, produces = "text/html", params = {"!parameters"})
	public String update(
			@PathVariable Long resourceId, Model model,
			@Validated({Default.class, ReadResource.class}) Resource resource, BindingResult result,
			RedirectAttributes redirectAttributes,
			HttpServletRequest request) {
		Resource persistedResource = getService().load(resourceId);

		if (result.hasErrors()) {
			for(ObjectError objectError : result.getAllErrors()) {
				logger.error(objectError.getDefaultMessage());
			}
			populateForm(model, resource, request);
			model.addAttribute("errors", result.getAllErrors());
			return "resource/update";
		}

		persistedResource.setUri(resource.getUri());
		persistedResource.setTitle(resource.getTitle());
		persistedResource.setResourceType(resource.getResourceType());
		persistedResource.setLastHarvested(resource.getLastHarvested());
		persistedResource.setJobId(resource.getJobId());
		persistedResource.setLastHarvestedJobId(resource.getLastHarvestedJobId());
		persistedResource.setStatus(resource.getStatus());
		persistedResource.setStartTime(resource.getStartTime());
		persistedResource.setDuration(resource.getDuration());
		persistedResource.setExitCode(resource.getExitCode());
		persistedResource.setExitDescription(resource.getExitDescription());
		persistedResource.setRecordsRead(resource.getRecordsRead());
		persistedResource.setReadSkip(resource.getReadSkip());
		persistedResource.setProcessSkip(resource.getProcessSkip());
		persistedResource.setWriteSkip(resource.getWriteSkip());
		persistedResource.setWritten(resource.getWritten());
		persistedResource.setParameters(resource.getParameters());
		persistedResource.setScheduled(resource.getScheduled());
		persistedResource.setSchedulingPeriod(resource.getSchedulingPeriod());
		persistedResource.updateNextAvailableDate();

		getService().saveOrUpdate(persistedResource);
		String[] codes = new String[] { "resource.was.updated" };
		Object[] args = new Object[] { resource.getTitle() };
		DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(codes, args);
		redirectAttributes.addFlashAttribute("info", message);
		return "redirect:/resource/{resourceId}";
	}
}