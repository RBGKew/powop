package org.emonocot.portal.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.emonocot.api.AnnotationService;
import org.emonocot.api.ResourceService;
import org.emonocot.api.OrganisationService;
import org.emonocot.api.job.JobExecutionException;
import org.emonocot.api.job.JobExecutionInfo;
import org.emonocot.api.job.JobLaunchRequest;
import org.emonocot.api.job.JobLauncher;
import org.emonocot.model.Annotation;
import org.emonocot.model.constants.JobType;
import org.emonocot.model.registry.Resource;
import org.emonocot.model.registry.Organisation;
import org.emonocot.pager.Page;
import org.emonocot.portal.format.annotation.FacetRequestFormat;
import org.joda.time.DateTime;
import org.joda.time.base.BaseDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
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

	/**
	 * 1288569600 in unix time.
	 */
	private static final BaseDateTime PAST_DATETIME = new DateTime(2010, 11, 1,
			9, 0, 0, 0);
	
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
    */
	private JobLauncher jobLauncher;

	/**
     *
     */
	private AnnotationService annotationService;

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
    * @param newJobLauncher
    *            Set the job launcher
    */
   @Autowired
   public final void setJobLauncher(final JobLauncher newJobLauncher) {
       this.jobLauncher = newJobLauncher;
   }
   
   /**
    * @param newAnnotationService
    *            Set the annotation service
    */
   @Autowired
   public final void setAnnotationService(
           final AnnotationService newAnnotationService) {
       this.annotationService = newAnnotationService;
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
		return "redirect:/organisation/" + organisation.getIdentifier() + "?form=true";
	}

	/**
	 * @param identifier
	 *            Set the identifier of the source
	 * @param limit
	 *            Set the maximum number of results
	 * @param start
	 *            Set the offset
	 * @param uiModel
	 *            Set the model
	 * @return the view name
	 */
	@RequestMapping(value = "/{identifier}", produces = "text/html")
	public final String show(@PathVariable final String identifier,
			final Model uiModel) {
		uiModel.addAttribute(getService().find(identifier));
		uiModel.addAttribute("resources", resourceService.list(identifier, 0, 10));
		return "organisation/show";
	}

	/**
	 * 
	 * @param model
	 *            Set the model
	 * @param identifier
	 *            Set the identifier
	 * @return the name of the view
	 */
	@RequestMapping(value = "/{identifier}", method = RequestMethod.GET, params = "form", produces = "text/html")
	public final String update(@PathVariable final String identifier,
			final Model model) {
		model.addAttribute(getService().load(identifier));
		return "organisation/update";
	}

	/**
	 * @param identifier
	 *            Set the identifier
	 * @param session
	 *            Set the session
	 * @param organisation
	 *            Set the source
	 * @param result
	 *            Set the binding results
	 * @return the model name
	 */
	@RequestMapping(value = "/{identifier}", method = RequestMethod.POST, produces = "text/html")
	public final String post(
			@PathVariable("identifier") final String identifier,
			@Valid final Organisation organisation, final BindingResult result,
			final HttpSession session) {
		if (result.hasErrors()) {
			return "organisation/update";
		}
		Organisation persistedSource = getService().load(identifier);
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
		return "redirect:/organisation/" + identifier + "?form=true";
	}

	/**
	 * 
	 * @param model
	 *            Set the model
	 * @param identifier
	 *            Set the source identifier,
	 * @param limit
	 *            Set the maximum number of objects to return
	 * @param start
	 *            Set the offset
	 * @return the name of the view
	 */
	@RequestMapping(value = "/{identifier}/resource", method = RequestMethod.GET, params = "!form")
	public final String list(
			final Model model,
			@PathVariable("identifier") final String identifier,
			@RequestParam(value = "limit", required = false, defaultValue = "10") final Integer limit,
			@RequestParam(value = "start", required = false, defaultValue = "0") final Integer start) {
		model.addAttribute("organisation", getService().load(identifier));
		model.addAttribute("resources", resourceService.list(identifier, start, limit));
		return "organisation/resource/list";
	}

	/**
	 * @param identifier
	 *            Set the source id
	 * @param model
	 *            Set the model
	 * @return the name of the view
	 */
	@RequestMapping(value = "/{identifier}/resource", method = RequestMethod.GET, params = "form")
	public final String create(
			@PathVariable("identifier") final String identifier,
			final Model model) {
		populateForm(model, new Resource(), new ResourceParameterDto());
		model.addAttribute("organisation", getService().load(identifier));
		return "organisation/resource/create";
	}

	/**
	 * 
	 * @param model
	 *            Set the model
	 * @param resource
	 *            Seth the job
	 */
	private void populateForm(final Model model, final Resource resource, final ResourceParameterDto parameter) {
		model.addAttribute("resource", resource);
		model.addAttribute("parameter", parameter);
		model.addAttribute("jobTypes", Arrays.asList(JobType.values()));
	}

	/**
	 * @param identifier
	 *            Set the sourceId
	 * @param model
	 *            Set the model
	 * @param session
	 *            Set the session
	 * @param resource
	 *            Set the job
	 * @param result
	 *            Set the binding results
	 * @return a model and view
	 */
	@RequestMapping(value = "/{organisationIdentifier}/resource", method = RequestMethod.POST, produces = "text/html")
	public final String post(
			@PathVariable("organisationIdentifier") final String sourceIdentifier,
			final Model model, @Valid final Resource resource,
			final BindingResult result, final HttpSession session) {
		Organisation organisation = getService().find(sourceIdentifier, "source-with-jobs");
		if (result.hasErrors()) {
			model.addAttribute("source", organisation);
			populateForm(model, resource, new ResourceParameterDto());
			return "organisation/resource/create";
		}

		organisation.getResources().add(resource);
		resource.setOrganisation(organisation);
		resourceService.saveOrUpdate(resource);
		String[] codes = new String[] { "resource.was.created" };
		Object[] args = new Object[] { resource.getIdentifier() };
		DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(
				codes, args);
		session.setAttribute("info", message);
		return "redirect:/organisation/" + sourceIdentifier + "/resource";
	}

	/**
	 * @param identifier
	 *            Set the source identifier
	 * @param jobId
	 *            Set the job identifier
	 * @param uiModel
	 *            Set the model
	 * @return the view name
	 */
	@RequestMapping(value = "/{identifier}/resource/{jobId}", method = RequestMethod.GET, 
			produces = "text/html",
			params = {"!output", "!details", "!form", "!parameters"})
	public final String show(
			@PathVariable("identifier") final String identifier,
			@PathVariable("jobId") final String jobId, final Model uiModel) {
		Resource resource = resourceService.load(jobId, "job-with-source");
		assert resource.getOrganisation().getIdentifier().equals(identifier);
		uiModel.addAttribute("resource", resource);
		uiModel.addAttribute("organisation", resource.getOrganisation());
		return "organisation/resource/show";
	}

	/**
	 * @param identifier
	 *            Set the source identifier
	 * @param model
	 *            Set the model
	 * @param jobId
	 *            Set the job identifier
	 * @return the name of the view
	 */
	@RequestMapping(value = "/{identifier}/resource/{jobId}", method = RequestMethod.GET, params = "form")
	public final String update(
			@PathVariable("identifier") final String identifier,
			@PathVariable("jobId") final String jobId,
			final Model model) {
		Resource resource = resourceService.load(jobId, "job-with-source");
		assert resource.getOrganisation().getIdentifier().equals(identifier);
		populateForm(model, resource, new ResourceParameterDto());
		model.addAttribute("organisation", resource.getOrganisation());
		return "organisation/resource/update";
	}
	
	/**
     * @param identifier
     *            Set the identifier of the source
     * @param jobId the identifier of the job
     * @param name the name of the parameter to add
     * @param session Set the session
     * @return the view name
     */
    @RequestMapping(value = "/{identifier}/resource/{jobId}", params = { "parameters", "!delete" }, method = RequestMethod.POST)
    public final String addParameter(@PathVariable final String identifier,
    		@PathVariable final String jobId,
    		@ModelAttribute("parameter") final ResourceParameterDto parameter,
            final HttpSession session) {
        Resource resource = resourceService.load(jobId, "job-with-source");
        resource.getParameters().put(parameter.getName(), "");
        resourceService.saveOrUpdate(resource);
        String[] codes = new String[] {"parameter.added.to.job" };
        Object[] args = new Object[] { parameter.getName() };
        DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(
                codes, args);
        session.setAttribute("info", message);
        return "redirect:/organisation/" + identifier + "/resource/" + jobId + "?form";
    }

    /**
     * @param identifier
     *            Set the identifier of the source
     * @param jobId Set the job identifier
     * @param name the name of the parameter to delete
     * @param session Set the session
     * @return the view name
     */
    @RequestMapping(value = "/{identifier}/resource/{jobId}", params = { "parameters",
            "delete" }, method = RequestMethod.GET)
    public final String removeMember(@PathVariable final String identifier,
    		@PathVariable final String jobId,
    		@RequestParam("name") final String name, final HttpSession session) {
        Resource resource = resourceService.load(jobId, "job-with-source");
        resource.getParameters().remove(name);
        resourceService.saveOrUpdate(resource);
        String[] codes = new String[] {"parameter.removed.from.job" };
        Object[] args = new Object[] { name };
        DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(
                codes, args);
        session.setAttribute("info", message);
        return "redirect:/organisation/" + identifier + "/resource/" + jobId + "?form";
    }

	/**
	 * @param identifier
	 *            Set the source identifier
	 * @param jobId
	 *            Set the job identifier
	 * @param model
	 *            Set the model
	 * @param session
	 *            Set the session
	 * 
	 * @return the view name
	 */
	@RequestMapping(value = "/{identifier}/resource/{jobId}", method = RequestMethod.POST, produces = "text/html", params = "run")
	public final String run(
			@PathVariable("identifier") final String identifier,
			@PathVariable("jobId") final String jobId, final Model model,
			final HttpSession session) {

		Resource resource = resourceService.load(jobId, "job-with-source");
		assert resource.getOrganisation().getIdentifier().equals(identifier);
		if (resource.getStatus() != null) {
			switch (resource.getStatus()) {
			case STARTED:
			case STARTING:
			case STOPPING:
			case UNKNOWN:
				String[] codes = new String[] { "job.running" };
				Object[] args = new Object[] {};
				DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(
						codes, args);
				session.setAttribute("error", message);
				return "redirect:/organisation/" + identifier + "/resource/"
						+ resource.getIdentifier();
			case COMPLETED:
			case FAILED:
			case STOPPED:
			default:
				break;
			}
		}
		Map<String, String> jobParametersMap = new HashMap<String, String>();
		jobParametersMap.put("authority.name", resource.getOrganisation().getIdentifier());
		jobParametersMap.put("authority.uri", resource.getUri());

		if (resource.getStatus() == null) {
			jobParametersMap.put("authority.last.harvested",
					Long.toString((PAST_DATETIME.getMillis())));
		} else {
			jobParametersMap.put("authority.last.harvested",
					Long.toString((resource.getStartTime().getMillis())));
		}
		
		jobParametersMap.putAll(resource.getParameters());

		JobLaunchRequest jobLaunchRequest = new JobLaunchRequest();
		switch (resource.getJobType()) {
		case REINDEX:
			jobLaunchRequest.setJob("ReIndex");
			break;
		case SITEMAP:
			jobLaunchRequest.setJob("SitemapGeneration");
			break;
		case IDENTIFICATION_KEY:
			jobLaunchRequest.setJob("IdentificationKeyHarvesting");
			break;
		case OAI_PMH:

			jobLaunchRequest.setJob("OaiPmhTaxonHarvesting");
			break;
		case DwC_Archive:
			jobLaunchRequest.setJob("DarwinCoreArchiveHarvesting");
			break;
		default:
			logger.error("Attempted to launch a job of unknown type, assuming it's a DwC job",
					new IllegalArgumentException("The jobType " + resource.getJobType() + " is not configured"));
			// TODO Return to some appropriate page with an error?
			break;
		}
		jobLaunchRequest.setParameters(jobParametersMap);

		try {
			jobLaunchRequest = jobLauncher.launch(jobLaunchRequest);
			JobExecutionInfo jobExecutionInfo = jobLaunchRequest.getExecution();
			if (jobExecutionInfo == null) {
				throw jobLaunchRequest.getException();
			}
			resource.setStartTime(jobExecutionInfo.getStartTime());
			resource.setDuration(jobExecutionInfo.getDuration());
			resource.setExitCode(jobExecutionInfo.getExitCode());
			resource.setExitDescription(jobExecutionInfo.getExitDescription());
			resource.setJobId(jobExecutionInfo.getId());
			if (jobExecutionInfo.getJobInstance() != null) {
				resource.setJobInstance(jobExecutionInfo.getJobInstance()
						.getResource());
			}
			resource.setLastHarvested(new DateTime());
			resource.setResource(jobExecutionInfo.getResource());
			resource.setStatus(jobExecutionInfo.getStatus());
			resource.setRecordsRead(0);
			resource.setReadSkip(0);
			resource.setProcessSkip(0);
			resource.setWriteSkip(0);
			resource.setWritten(0);
			resourceService.saveOrUpdate(resource);
			String[] codes = new String[] { "job.started" };
			Object[] args = new Object[] {};
			DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(
					codes, args);
			session.setAttribute("info", message);
		} catch (JobExecutionException e) {
			String[] codes = new String[] { "job.failed" };
			Object[] args = new Object[] { e.getMessage() };
			DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(
					codes, args);
			session.setAttribute("error", message);
		}
		return "redirect:/organisation/" + identifier + "/resource/" + resource.getIdentifier();
	}

	/**
	 * @param identifier
	 *            Set the source identifier
	 * @param jobId
	 *            Set the job identifier
	 * @param model
	 *            Set the model
	 * @param session
	 *            Set the session
	 * @param resource
	 *            Set the job
	 * @param result
	 *            Set the binding results
	 * @return the view name
	 */
	@RequestMapping(value = "/{identifier}/resource/{jobId}", method = RequestMethod.POST, produces = "text/html", params = {"!run","!parameters"})
	public final String post(
			@PathVariable("identifier") final String identifier,
			@PathVariable("jobId") final String jobId, final Model model,
			@Valid final Resource resource, final BindingResult result,
			final HttpSession session) {
		Resource persistedJob = resourceService.load(jobId, "job-with-source");

		if (result.hasErrors()) {
			populateForm(model, resource, new ResourceParameterDto());
			model.addAttribute("organisation", persistedJob.getOrganisation());
			return "organisation/resource/update";
		}

		assert persistedJob.getOrganisation().getIdentifier().equals(identifier);
		persistedJob.setUri(resource.getUri());
		persistedJob.setJobType(resource.getJobType());
		persistedJob.setLastHarvested(resource.getLastHarvested());
		persistedJob.setJobId(resource.getJobId());
		persistedJob.setStatus(resource.getStatus());
		persistedJob.setStartTime(resource.getStartTime());
		persistedJob.setDuration(resource.getDuration());
		persistedJob.setExitCode(resource.getExitCode());
		persistedJob.setExitDescription(resource.getExitDescription());
		persistedJob.setRecordsRead(resource.getRecordsRead());
		persistedJob.setReadSkip(resource.getReadSkip());
		persistedJob.setProcessSkip(resource.getProcessSkip());
		persistedJob.setWriteSkip(resource.getWriteSkip());
		persistedJob.setWritten(resource.getWritten());
		persistedJob.setParameters(resource.getParameters());

		resourceService.saveOrUpdate(persistedJob);
		String[] codes = new String[] { "job.was.updated" };
		Object[] args = new Object[] { resource.getIdentifier() };
		DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(
				codes, args);
		session.setAttribute("info", message);
		return "redirect:/organisation/" + identifier + "/resource/" + jobId;
	}

	/**
	 * @param identifier
	 *            Set the identifier of the source
	 * @param query
	 *            Set the query
	 * @param jobId
	 *            Set the job Id
	 * @param facets
	 *            Set the facets
	 * @param limit
	 *            Set the page size
	 * @param start
	 *            Set the page offset
	 * @param model
	 *            Set the model
	 * @return A model and view containing a source
	 */
	@RequestMapping(value = "/{identifier}/resource/{jobId}/output", method = RequestMethod.GET)
	public final String search(
			@PathVariable final String identifier,
			@PathVariable final Long jobId,
			@RequestParam(value = "query", required = false) final String query,
		    @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
		    @RequestParam(value = "start", required = false, defaultValue = "0") final Integer start,
		    @RequestParam(value = "facet", required = false) @FacetRequestFormat final List<FacetRequest> facets,
		    @RequestParam(value = "sort", required = false) String sort,
		    @RequestParam(value = "view", required = false) String view,
		    final Model model) {
		model.addAttribute(getService().load(identifier));
		Map<String, String> selectedFacets = new HashMap<String, String>();
		if (facets != null && !facets.isEmpty()) {
			for (FacetRequest facetRequest : facets) {
				selectedFacets.put(facetRequest.getFacet(),
						facetRequest.getSelected());
			}
		}
		selectedFacets.put("annotation.job_id_l", jobId.toString());
		Page<Annotation> result = annotationService.search(query, null, limit,
				start, new String[] { "annotation.code_s",
				"annotation.type_s", "annotation.record_type_s",
				"annotation.job_id_l" }, selectedFacets, null,
				"annotated-obj");
		result.putParam("query", query);
		model.addAttribute("jobId",jobId);
		model.addAttribute("result", result);

		return "organisation/resource/output";
	}
}
