package org.emonocot.portal.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.emonocot.model.constants.SchedulingPeriod;
import org.emonocot.model.registry.Resource;
import org.emonocot.pager.Page;
import org.emonocot.portal.controller.form.ResourceParameterDto;
import org.emonocot.portal.format.annotation.FacetRequestFormat;
import org.joda.time.DateTime;
import org.joda.time.base.BaseDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author ben
 *
 */
@Controller
@RequestMapping("/resource")
public class ResourceController extends GenericController<Resource, ResourceService> {
	private static Logger logger = LoggerFactory.getLogger(ResourceController.class);
	
	private static BaseDateTime PAST_DATETIME = new DateTime(2010, 11, 1,	9, 0, 0, 0);
	
	private AnnotationService annotationService;
	
	private OrganisationService organisationService;
	
	private JobLauncher jobLauncher;
	
	private JobExplorer jobExplorer;

	@Autowired
	public void setResourceService(ResourceService resourceService) {
		super.setService(resourceService);
	}
	
	@Autowired
	@Qualifier("readWriteJobLauncher")
	public void setJobLauncher(JobLauncher newJobLauncher) {
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
    
    @Autowired
    public void setJobExplorer(JobExplorer jobExplorer) {
    	this.jobExplorer = jobExplorer;
    }

	/**
     *
     */
    public ResourceController() {
        super("resource");
    }
    
    private void populateForm(Model model, Resource resource, ResourceParameterDto parameter) {
		model.addAttribute("resource", resource);
		model.addAttribute("parameter", parameter);
		model.addAttribute("resourceTypes", Arrays.asList(ResourceType.values()));
		model.addAttribute("schedulingPeriods",Arrays.asList(SchedulingPeriod.values()));
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
	public String search(
			@PathVariable Long resourceId,
			@RequestParam(value = "query", required = false) String query,
		    @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
		    @RequestParam(value = "start", required = false, defaultValue = "0") Integer start,
		    @RequestParam(value = "facet", required = false) @FacetRequestFormat List<FacetRequest> facets,
		    @RequestParam(value = "sort", required = false) String sort,
		    @RequestParam(value = "view", required = false) String view,
		    Model model) {
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
				"annotation.job_id_l" }, null, selectedFacets,
				null, "annotated-obj");
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
	@RequestMapping(value = "/{resourceId}", method = RequestMethod.POST, produces = "text/html", params = {"!parameters"})
	public String update(
			@PathVariable Long resourceId, Model model,
			@Valid Resource resource, BindingResult result,
			RedirectAttributes redirectAttributes) {
		Resource persistedResource = getService().load(resourceId);

		if (result.hasErrors()) {
			for(ObjectError objectError : result.getAllErrors()) {
				logger.error(objectError.getDefaultMessage());
			}
			populateForm(model, resource, new ResourceParameterDto());
			return "resource/update";
		}
		
		persistedResource.setUri(resource.getUri());
		persistedResource.setTitle(resource.getTitle());
		persistedResource.setResourceType(resource.getResourceType());
		persistedResource.setLastHarvested(resource.getLastHarvested());
		persistedResource.setJobId(resource.getJobId());
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

		getService().saveOrUpdate(persistedResource);
		String[] codes = new String[] { "resource.was.updated" };
		Object[] args = new Object[] { resource.getTitle() };
		DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(
				codes, args);
		redirectAttributes.addFlashAttribute("info", message);
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
	public String list(Model model,
			@RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
			@RequestParam(value = "start", required = false, defaultValue = "0") Integer start) {
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
	public String create(
			@RequestParam(required = true) String organisation,
			Model model) {
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
	public String create(			
			Model model, @Valid Resource resource,
			BindingResult result, RedirectAttributes redirectAttributes) {		
		if (result.hasErrors()) {
			populateForm(model, resource, new ResourceParameterDto());
			return "resource/create";
		}
		getService().saveOrUpdate(resource);
		String[] codes = new String[] { "resource.was.created" };
		Object[] args = new Object[] { resource.getTitle() };
		DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(
				codes, args);
		redirectAttributes.addFlashAttribute("info", message);
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
	@RequestMapping(value = "/{resourceId}", method = RequestMethod.GET, produces = "text/html", params = {"!run", "!form", "!parameters"})
	public String show(@PathVariable Long resourceId, Model uiModel) {
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
	public String update(
			@PathVariable Long resourceId,
			Model model) {
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
    public String addParameter(@PathVariable Long resourceId,
    		@ModelAttribute("parameter") ResourceParameterDto parameter,
            RedirectAttributes redirectAttributes) {
        Resource resource = getService().load(resourceId);
        resource.getParameters().put(parameter.getName(), "");
        getService().saveOrUpdate(resource);
        String[] codes = new String[] {"parameter.added.to.resource" };
        Object[] args = new Object[] { parameter.getName() };
        DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(
                codes, args);
        redirectAttributes.addFlashAttribute("info", message);
        return "redirect:/resource/" + resourceId + "?form=true";
    }

    /**
     * @param organisationId
     *            Set the identifier of the source
     * @param resourceId Set the job identifier
     * @param name the name of the parameter to delete
     * @param session Set the session
     * @return the view name
     */
    @RequestMapping(value = "/{resourceId}", params = { "parameters", "delete" }, method = RequestMethod.GET)
    public String removeParameter(@PathVariable Long resourceId,
    		@RequestParam("name") String name, RedirectAttributes redirectAttributes) {
        Resource resource = getService().load(resourceId);
        resource.getParameters().remove(name);
        getService().saveOrUpdate(resource);
        String[] codes = new String[] {"parameter.removed.from.resource" };
        Object[] args = new Object[] { name };
        DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(
                codes, args);
        redirectAttributes.addFlashAttribute("info", message);
        return "redirect:/resource/" + resourceId + "?form=true";
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
	@RequestMapping(value = "/{resourceId}", method = RequestMethod.GET, produces = "text/html", params = "run")
	public String run(
			@PathVariable Long resourceId,
			@RequestParam(required = false, defaultValue = "true") Boolean ifModified,
			Model model,
			RedirectAttributes redirectAttributes) {

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
				redirectAttributes.addFlashAttribute("error", message);
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
		jobParametersMap.put("resource.identifier", resource.getIdentifier());

		if (resource.getStatus() == null || !ifModified) {
			jobParametersMap.put("timestamp", Long.toString(System.currentTimeMillis()));
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
			jobLauncher.launch(jobLaunchRequest);
			resource.setStartTime(null);
			resource.setDuration(null);
			resource.setExitCode(null);
			resource.setExitDescription(null);
			resource.setJobId(null);
			resource.setStatus(BatchStatus.UNKNOWN);
			resource.setRecordsRead(0);
			resource.setReadSkip(0);
			resource.setProcessSkip(0);
			resource.setWriteSkip(0);
			resource.setWritten(0);
			getService().saveOrUpdate(resource);
			String[] codes = new String[] { "job.scheduled" };
			Object[] args = new Object[] {};
			DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(
					codes, args);
			redirectAttributes.addFlashAttribute("info", message);
		} catch (JobExecutionException e) {
			String[] codes = new String[] { "job.failed" };
			Object[] args = new Object[] { e.getMessage() };
			DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(
					codes, args);
			redirectAttributes.addFlashAttribute("error", message);
		}
		return "redirect:/resource/" + resourceId;
	}
	
	@RequestMapping(value = "/{resourceId}/progress", method = RequestMethod.GET, produces="application/json")
    @ResponseBody
	public JobExecutionInfo getProgress(@PathVariable("resourceId") Long resourceId) throws Exception {
		JobExecutionInfo jobExecutionInfo = new JobExecutionInfo();
		Resource resource = getService().load(resourceId);
		
		JobExecution jobExecution = jobExplorer.getJobExecution(resource.getJobId());
		if(jobExecution != null) {			
			jobExecutionInfo.setStatus(jobExecution.getStatus());
			if(jobExecution.getExitStatus() != null) {
				ExitStatus exitStatus = jobExecution.getExitStatus();
			    jobExecutionInfo.setExitCode(exitStatus.getExitCode());
			    jobExecutionInfo.setExitDescription(exitStatus.getExitDescription());
			    
			    Integer recordsRead = 0;
				Integer readSkip = 0;
				Integer processSkip = 0;
				Integer writeSkip = 0;
				Integer written = 0;
				for(StepExecution stepExecution : jobExecution.getStepExecutions()) {
					recordsRead += stepExecution.getReadCount();
					readSkip += stepExecution.getReadSkipCount();
					processSkip += stepExecution.getProcessSkipCount();
					writeSkip += stepExecution.getWriteSkipCount();
					written += stepExecution.getWriteCount();
				}
				jobExecutionInfo.setRecordsRead(recordsRead);
				jobExecutionInfo.setReadSkip(readSkip);
				jobExecutionInfo.setProcessSkip(processSkip);
				jobExecutionInfo.setWriteSkip(writeSkip);
				jobExecutionInfo.setWritten(written);
			}			
			Float total = new Float(0);
			
			switch(jobExecution.getJobInstance().getJobName()) {
			case "DarwinCoreArchiveHarvesting":
				total = new Float(43);
				break;
			case "IdentificationKeyHarvesting":
				total = new Float(10);
				break;
			case "IUCNImport":
				total = new Float(10);
				break;
			case "GBIFImport":
				total = new Float(10);
				break;
			default:
				break;
			}
			
			Float steps = new Float(jobExecution.getStepExecutions().size());
			jobExecutionInfo.setProgress(Math.round((steps/ total) * 100f));
			
		}
		return jobExecutionInfo;
	}  


}
