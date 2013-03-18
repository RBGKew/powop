package org.emonocot.portal.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.emonocot.api.OrganisationService;
import org.emonocot.api.ResourceService;
import org.emonocot.api.autocomplete.Match;
import org.emonocot.model.registry.Organisation;
import org.emonocot.pager.Page;
import org.emonocot.portal.format.annotation.FacetRequestFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 
 * @author ben
 * 
 */
@Controller
@RequestMapping("/organisation")
public class OrganisationController extends GenericController<Organisation, OrganisationService> {	
	
	private static Logger logger = LoggerFactory.getLogger(OrganisationController.class);

	/**
     *
     */
	public OrganisationController() {
		super("organisation");
	}

	/**
    *
    */
	private ResourceService resourceService;

	/**
	 * 
	 * @param resourceService
	 *            Set the source service
	 */
	@Autowired
	public void setResourceService(ResourceService resourceService) {
		this.resourceService = resourceService;
	}

	/**
	 * 
	 * @param organisationService
	 *            Set the source service
	 */
	@Autowired
	public void setOrganisationService(OrganisationService organisationService) {
		super.setService(organisationService);
	}

	/**
	 * 
	 * @param model
	 *            Set the model
	 * @param limit
	 *            Set the maximum number of objects to return
	 * @param start
	 *            Set the offset
	 * @return the name of the view
	 */
	@RequestMapping(method = RequestMethod.GET, params = {"!form","!autocomplete"}, produces = "text/html")
	public String list(
			Model model,
			@RequestParam(value = "query", required = false) String query,
		    @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
		    @RequestParam(value = "start", required = false, defaultValue = "0") Integer start,
		    @RequestParam(value = "facet", required = false) @FacetRequestFormat List<FacetRequest> facets,
		    @RequestParam(value = "sort", required = false) String sort,
		    @RequestParam(value = "view", required = false) String view) {
		
		Map<String, String> selectedFacets = new HashMap<String, String>();
		if (facets != null && !facets.isEmpty()) {
			for (FacetRequest facetRequest : facets) {
				selectedFacets.put(facetRequest.getFacet(),
						facetRequest.getSelected());
			}
		}
		selectedFacets.put("base.class_s", "org.emonocot.model.registry.Organisation");
		Page<Organisation> result = getService().search(query, null, limit, start, 
				new String[] { "organisation.subject_t" }, null, selectedFacets, sort, null);
		model.addAttribute("result", result);
		result.putParam("query", query);
		return "organisation/list";
	}
	
	@RequestMapping(params = "autocomplete", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<Match> autocomplete(@RequestParam(required = true) String term) {    	
        return getService().autocomplete(term, 10, null);
    }

	/**
	 * 
	 * @param model
	 *            Set the model
	 * @return the name of the view
	 */
	@RequestMapping(method = RequestMethod.GET, params = "form", produces = "text/html")
	public String create(Model model) {
		model.addAttribute(new Organisation());
		return "organisation/create";
	}

	/**
	 * @param session
	 *            Set the session
	 * @param organisation
	 *            Set the source
	 * @param result
	 *            Set the binding results
	 * @return a model and view
	 */
	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String post(@Valid Organisation organisation,
			BindingResult result, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return "organisation/create";
		}

		getService().saveOrUpdate(organisation);
		String[] codes = new String[] { "organisation.was.created" };
		Object[] args = new Object[] { organisation.getTitle() };
		DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(
				codes, args);
		redirectAttributes.addFlashAttribute("info", message);
		return "redirect:/organisation";
	}

	/**
	 * @param organisationId
	 *            Set the identifier of the source
	 * @param limit
	 *            Set the maximum number of results
	 * @param start
	 *            Set the offset
	 * @param uiModel
	 *            Set the model
	 * @return the view name
	 */
	@RequestMapping(value = "/{organisationId}", produces = "text/html")
	public String show(@PathVariable String organisationId,
			Model uiModel) {
		uiModel.addAttribute(getService().find(organisationId));
		uiModel.addAttribute("resources", resourceService.list(organisationId, 0, 10));
		return "organisation/show";
	}

	/**
	 * 
	 * @param model
	 *            Set the model
	 * @param organisationId
	 *            Set the identifier
	 * @return the name of the view
	 */
	@RequestMapping(value = "/{organisationId}", method = RequestMethod.GET, params = "form", produces = "text/html")
	public String update(@PathVariable String organisationId,
			Model model) {
		model.addAttribute(getService().load(organisationId));
		return "organisation/update";
	}

	/**
	 * @param organisationId
	 *            Set the identifier
	 * @param session
	 *            Set the session
	 * @param organisation
	 *            Set the source
	 * @param result
	 *            Set the binding results
	 * @return the model name
	 */
	@RequestMapping(value = "/{organisationId}", method = RequestMethod.POST, produces = "text/html")
	public String post(
			@PathVariable String organisationId,
			@Valid Organisation organisation, BindingResult result,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return "organisation/update";
		}
		Organisation persistedSource = getService().load(organisationId);
		persistedSource.setTitle(organisation.getTitle());
		persistedSource.setUri(organisation.getUri());
		persistedSource.setCreator(organisation.getCreator());
		persistedSource.setCreatorEmail(organisation.getCreatorEmail());
		persistedSource.setCreated(organisation.getCreated());
		persistedSource.setDescription(organisation.getDescription());
		persistedSource.setPublisherName(organisation.getPublisherName());
		persistedSource.setPublisherEmail(organisation.getPublisherEmail());
		String emailCommentsTo = organisation.getCommentsEmailedTo();
		if(emailCommentsTo == null) {
		    emailCommentsTo = organisation.getPublisherEmail() != null ?
		                organisation.getPublisherEmail() :organisation.getCreatorEmail();
		}
		persistedSource.setCommentsEmailedTo(emailCommentsTo);
		persistedSource.setSubject(organisation.getSubject());
		persistedSource.setBibliographicCitation(organisation.getBibliographicCitation());
		persistedSource.setLogoUrl(organisation.getLogoUrl());
		getService().saveOrUpdate(persistedSource);
		String[] codes = new String[] { "organisation.updated" };
		Object[] args = new Object[] { organisation.getTitle() };
		DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(
				codes, args);
		redirectAttributes.addFlashAttribute("info", message);
		return "redirect:/organisation/" + organisationId;
	}
}
