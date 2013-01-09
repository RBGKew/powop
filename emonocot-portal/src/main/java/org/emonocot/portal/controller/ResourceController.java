package org.emonocot.portal.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.emonocot.api.AnnotationService;
import org.emonocot.api.OrganisationService;
import org.emonocot.api.ResourceService;
import org.emonocot.api.job.JobExecutionException;
import org.emonocot.api.job.JobExecutionInfo;
import org.emonocot.api.job.JobLaunchRequest;
import org.emonocot.api.job.JobLauncher;
import org.emonocot.model.Annotation;
import org.emonocot.model.constants.ResourceType;
import org.emonocot.model.registry.Resource;
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
import org.springframework.validation.ObjectError;
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
@RequestMapping("/resource")
public class ResourceController extends GenericController<Resource, ResourceService> {
	private static Logger logger = LoggerFactory.getLogger(ResourceController.class);
	
	private static final BaseDateTime PAST_DATETIME = new DateTime(2010, 11, 1,	9, 0, 0, 0);
	
	private AnnotationService annotationService;
	
	private OrganisationService organisationService;
	
	private JobLauncher jobLauncher;

	@Autowired
	public final void setResourceService(final ResourceService resourceService) {
		super.setService(resourceService);
	}
	
	@Autowired
	public final void setJobLauncher(final JobLauncher newJobLauncher) {
	   this.jobLauncher = newJobLauncher;
	}
	
	@Autowired
	public void setAnnotationService(AnnotationService annotationService) {
	       this.annotationService = annotationService;
	}
	
    @Autowired
	public void setOrganisationService(OrganisationService organisationService) {
		this.organisationService = organisationService;
	}

	/**
     *
     */
    public ResourceController() {
        super("resource");
    }
    
    private void populateForm(final Model model, final Resource resource, final ResourceParameterDto parameter) {
		model.addAttribute("resource", resource);
		model.addAttribute("parameter", parameter);
		model.addAttribute("resourceTypes", Arrays.asList(ResourceType.values()));
	}
    
    /**
	 * @param organisationId
	 *            Set the identifier of the source
	 * @param query
	 *            Set the query
	 * @param resourceId
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
	@RequestMapping(value = "/{resourceId}/output", method = RequestMethod.GET)
	public final String search(
			@PathVariable final Long resourceId,
			@RequestParam(value = "query", required = false) final String query,
		    @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
		    @RequestParam(value = "start", required = false, defaultValue = "0") final Integer start,
		    @RequestParam(value = "facet", required = false) @FacetRequestFormat final List<FacetRequest> facets,
		    @RequestParam(value = "sort", required = false) String sort,
		    @RequestParam(value = "view", required = false) String view,
		    final Model model) {
		Resource resource = getService().load(resourceId);
		organisationService.load(resource.getOrganisation().getIdentifier());
		model.addAttribute("resource", resource);
		Map<String, String> selectedFacets = new HashMap<String, String>();
		if (facets != null && !facets.isEmpty()) {
			for (FacetRequest facetRequest : facets) {
				selectedFacets.put(facetRequest.getFacet(),
						facetRequest.getSelected());
			}
		}
		selectedFacets.put("annotation.job_id_l", resource.getJobId().toString());
		Page<Annotation> result = annotationService.search(query, null, limit,
				start, new String[] { "annotation.code_s",
				"annotation.type_s", "annotation.record_type_s",
				"annotation.job_id_l" }, selectedFacets, null,
				"annotated-obj");
		result.putParam("query", query);
		model.addAttribute("jobId",resource.getJobId());
		model.addAttribute("result", result);

		return "resource/output";
	}
	
	/**
	 * @param organisationId
	 *            Set the source identifier
	 * @param resourceId
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
	@RequestMapping(value = "/{resourceId}", method = RequestMethod.POST, produces = "text/html", params = {"!run","!parameters"})
	public final String post(
			@PathVariable final Long resourceId, final Model model,
			@Valid final Resource resource, final BindingResult result,
			final HttpSession session) {
		Resource persistedJob = getService().load(resourceId);

		if (result.hasErrors()) {
			for(ObjectError objectError : result.getAllErrors()) {
				logger.error(objectError.getDefaultMessage());
			}
			populateForm(model, resource, new ResourceParameterDto());
			return "resource/update";
		}
		
		persistedJob.setUri(resource.getUri());
		persistedJob.setTitle(resource.getTitle());
		persistedJob.setResourceType(resource.getResourceType());
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

		getService().saveOrUpdate(persistedJob);
		String[] codes = new String[] { "resource.was.updated" };
		Object[] args = new Object[] { resource.getTitle() };
		DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(
				codes, args);
		session.setAttribute("info", message);
		return "redirect:/resource/" + resourceId;
	}
	/**
	 * 
	 * @param model
	 *            Set the model
	 * @param organisationId
	 *            Set the source identifier,
	 * @param limit
	 *            Set the maximum number of objects to return
	 * @param start
	 *            Set the offset
	 * @return the name of the view
	 */
	@RequestMapping(method = RequestMethod.GET, params = "!form")
	public final String list(final Model model,
			@RequestParam(value = "limit", required = false, defaultValue = "10") final Integer limit,
			@RequestParam(value = "start", required = false, defaultValue = "0") final Integer start) {
		model.addAttribute("resources", getService().list(start, limit, null));
		return "resource/list";
	}

	/**
	 * @param organisationId
	 *            Set the source id
	 * @param model
	 *            Set the model
	 * @return the name of the view
	 */
	@RequestMapping(method = RequestMethod.GET, params = "form")
	public final String create(
			@RequestParam(required = true) String organisation,
			final Model model) {
		Resource resource = new Resource();
		resource.setOrganisation(organisationService.load(organisation));
		populateForm(model, resource, new ResourceParameterDto());
		
		return "resource/create";
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
	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public final String post(			
			final Model model, @Valid final Resource resource,
			final BindingResult result, final HttpSession session) {		
		if (result.hasErrors()) {
			populateForm(model, resource, new ResourceParameterDto());
			return "resource/create";
		}
		getService().saveOrUpdate(resource);
		String[] codes = new String[] { "resource.was.created" };
		Object[] args = new Object[] { resource.getTitle() };
		DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(
				codes, args);
		session.setAttribute("info", message);
		return "redirect:/resource";
	}

	/**
	 * @param organisationId
	 *            Set the source identifier
	 * @param resourceId
	 *            Set the job identifier
	 * @param uiModel
	 *            Set the model
	 * @return the view name
	 */
	@RequestMapping(value = "/{resourceId}", method = RequestMethod.GET, 
			produces = "text/html",
			params = {"!output", "!details", "!form", "!parameters"})
	public final String show(@PathVariable final Long resourceId, final Model uiModel) {
		Resource resource = getService().load(resourceId);
		uiModel.addAttribute("resource", resource);
		return "resource/show";
	}

	/**
	 * @param organisationId
	 *            Set the source identifier
	 * @param model
	 *            Set the model
	 * @param resourceId
	 *            Set the job identifier
	 * @return the name of the view
	 */
	@RequestMapping(value = "/{resourceId}", method = RequestMethod.GET, params = "form")
	public final String update(
			@PathVariable final Long resourceId,
			final Model model) {
		Resource resource = getService().load(resourceId);
		populateForm(model, resource, new ResourceParameterDto());
		return "resource/update";
	}
	
	/**
     * @param organisationId
     *            Set the identifier of the source
     * @param resourceId the identifier of the job
     * @param name the name of the parameter to add
     * @param session Set the session
     * @return the view name
     */
    @RequestMapping(value = "/{resourceId}", params = { "parameters", "!delete" }, method = RequestMethod.POST)
    public final String addParameter(@PathVariable final Long resourceId,
    		@ModelAttribute("parameter") final ResourceParameterDto parameter,
            final HttpSession session) {
        Resource resource = getService().load(resourceId);
        resource.getParameters().put(parameter.getName(), "");
        getService().saveOrUpdate(resource);
        String[] codes = new String[] {"parameter.added.to.resource" };
        Object[] args = new Object[] { parameter.getName() };
        DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(
                codes, args);
        session.setAttribute("info", message);
        return "redirect:/resource/" + resourceId + "?form";
    }

    /**
     * @param organisationId
     *            Set the identifier of the source
     * @param resourceId Set the job identifier
     * @param name the name of the parameter to delete
     * @param session Set the session
     * @return the view name
     */
    @RequestMapping(value = "/{resourceId}", params = { "parameters",
            "delete" }, method = RequestMethod.GET)
    public final String removeMember(@PathVariable final Long resourceId,
    		@RequestParam("name") final String name, final HttpSession session) {
        Resource resource = getService().load(resourceId);
        resource.getParameters().remove(name);
        getService().saveOrUpdate(resource);
        String[] codes = new String[] {"parameter.removed.from.resource" };
        Object[] args = new Object[] { name };
        DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(
                codes, args);
        session.setAttribute("info", message);
        return "redirect:/resource/" + resourceId + "?form";
    }

	/**
	 * @param organisationId
	 *            Set the source identifier
	 * @param resourceId
	 *            Set the job identifier
	 * @param model
	 *            Set the model
	 * @param session
	 *            Set the session
	 * 
	 * @return the view name
	 */
	@RequestMapping(value = "/{resourceId}", method = RequestMethod.POST, produces = "text/html", params = "run")
	public final String run(
			@PathVariable final Long resourceId, final Model model,
			final HttpSession session) {

		Resource resource = getService().load(resourceId);
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
				return "redirect:/resource/" + resourceId;
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
		jobLaunchRequest.setJob(resource.getResourceType().getJobName());		
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
			getService().saveOrUpdate(resource);
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
		return "redirect:/resource/" + resourceId;
	}

}
