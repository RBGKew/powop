package org.emonocot.portal.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.emonocot.api.OrganisationService;
import org.emonocot.api.ResourceService;
import org.emonocot.api.job.JobLauncher;
import org.emonocot.model.registry.Organisation;
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

/**
 * 
 * @author ben
 * 
 */
@Controller
@RequestMapping("/organisation")
public class OrganisationController extends GenericController<Organisation, OrganisationService> {	
	
	private static final Logger logger = LoggerFactory.getLogger(OrganisationController.class);

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
	public final void setResourceService(final ResourceService resourceService) {
		this.resourceService = resourceService;
	}

	/**
	 * 
	 * @param organisationService
	 *            Set the source service
	 */
	@Autowired
	public final void setOrganisationService(final OrganisationService organisationService) {
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
	@RequestMapping(method = RequestMethod.GET, params = "!form", produces = "text/html")
	public final String list(
			final Model model,
			@RequestParam(value = "start", defaultValue = "0", required = false) final Integer start,
			@RequestParam(value = "size", defaultValue = "10", required = false) final Integer size) {
		model.addAttribute("result", getService().list(start, size, null));
		return "organisation/list";
	}

	/**
	 * 
	 * @param model
	 *            Set the model
	 * @return the name of the view
	 */
	@RequestMapping(method = RequestMethod.GET, params = "form", produces = "text/html")
	public final String create(final Model model) {
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
	public final String post(@Valid final Organisation organisation,
			final BindingResult result, final HttpSession session) {
		if (result.hasErrors()) {
			return "organisation/create";
		}

		getService().saveOrUpdate(organisation);
		String[] codes = new String[] { "organisation.was.created" };
		Object[] args = new Object[] { organisation.getTitle() };
		DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(
				codes, args);
		session.setAttribute("info", message);
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
	public final String show(@PathVariable final String organisationId,
			final Model uiModel) {
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
	public final String update(@PathVariable final String organisationId,
			final Model model) {
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
	public final String post(
			@PathVariable final String organisationId,
			@Valid final Organisation organisation, final BindingResult result,
			final HttpSession session) {
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
		persistedSource.setSubject(organisation.getSubject());
		persistedSource.setBibliographicCitation(organisation.getBibliographicCitation());
		persistedSource.setLogoUrl(organisation.getLogoUrl());
		getService().saveOrUpdate(persistedSource);
		String[] codes = new String[] { "organisation.updated" };
		Object[] args = new Object[] { organisation.getTitle() };
		DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(
				codes, args);
		session.setAttribute("info", message);
		return "redirect:/organisation/" + organisationId;
	}
}
