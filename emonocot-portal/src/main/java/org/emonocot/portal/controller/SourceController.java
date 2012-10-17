package org.emonocot.portal.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.emonocot.api.AnnotationService;
import org.emonocot.api.FacetName;
import org.emonocot.api.JobService;
import org.emonocot.api.Sorting;
import org.emonocot.api.SourceService;
import org.emonocot.api.job.JobExecutionException;
import org.emonocot.api.job.JobExecutionInfo;
import org.emonocot.api.job.JobLaunchRequest;
import org.emonocot.api.job.JobLauncher;
import org.emonocot.model.Annotation;
import org.emonocot.model.Job;
import org.emonocot.model.Source;
import org.emonocot.model.constants.JobType;
import org.emonocot.pager.Page;
import org.emonocot.portal.format.annotation.FacetRequestFormat;
import org.emonocot.portal.format.annotation.SortingFormat;
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
@RequestMapping("/source")
public class SourceController extends GenericController<Source, SourceService> {

	/**
	 * 1288569600 in unix time.
	 */
	private static final BaseDateTime PAST_DATETIME = new DateTime(2010, 11, 1,
			9, 0, 0, 0);
	
	private static final Logger logger = LoggerFactory.getLogger(SourceController.class);

	/**
     *
     */
	public SourceController() {
		super("source");
	}

	/**
    *
    */
	private JobService jobService;

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
	 * @param newJobService
	 *            Set the source service
	 */
	@Autowired
	public final void setJobService(final JobService newJobService) {
		this.jobService = newJobService;
	}

	/**
	 * 
	 * @param sourceService
	 *            Set the source service
	 */
	@Autowired
	public final void setSourceService(final SourceService sourceService) {
		super.setService(sourceService);
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
			@RequestParam(value = "page", defaultValue = "0", required = false) final Integer page,
			@RequestParam(value = "size", defaultValue = "10", required = false) final Integer size) {
		model.addAttribute("result", getService().list(page, size));
		return "source/list";
	}

	/**
	 * 
	 * @param model
	 *            Set the model
	 * @return the name of the view
	 */
	@RequestMapping(method = RequestMethod.GET, params = "form", produces = "text/html")
	public final String create(final Model model) {
		model.addAttribute(new Source());
		return "source/create";
	}

	/**
	 * @param session
	 *            Set the session
	 * @param source
	 *            Set the source
	 * @param result
	 *            Set the binding results
	 * @return a model and view
	 */
	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public final String post(@Valid final Source source,
			final BindingResult result, final HttpSession session) {
		if (result.hasErrors()) {
			return "source/create";
		}

		getService().saveOrUpdate(source);
		String[] codes = new String[] { "source.was.created" };
		Object[] args = new Object[] { source.getTitle() };
		DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(
				codes, args);
		session.setAttribute("info", message);
		return "redirect:/source/" + source.getIdentifier() + "?form=true";
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
		uiModel.addAttribute("jobs", jobService.list(identifier, 0, 10));
		return "source/show";
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
		return "source/update";
	}

	/**
	 * @param identifier
	 *            Set the identifier
	 * @param session
	 *            Set the session
	 * @param source
	 *            Set the source
	 * @param result
	 *            Set the binding results
	 * @return the model name
	 */
	@RequestMapping(value = "/{identifier}", method = RequestMethod.POST, produces = "text/html")
	public final String post(
			@PathVariable("identifier") final String identifier,
			@Valid final Source source, final BindingResult result,
			final HttpSession session) {
		if (result.hasErrors()) {
			return "source/update";
		}
		Source persistedSource = getService().load(identifier);
		persistedSource.setTitle(source.getTitle());
		persistedSource.setUri(source.getUri());
		persistedSource.setCreator(source.getCreator());
		persistedSource.setCreatorEmail(source.getCreatorEmail());
		persistedSource.setCreated(source.getCreated());
		persistedSource.setDescription(source.getDescription());
		persistedSource.setPublisherName(source.getPublisherName());
		persistedSource.setPublisherEmail(source.getPublisherEmail());
		persistedSource.setSubject(source.getSubject());
		persistedSource.setBibliographicCitation(source.getBibliographicCitation());
		persistedSource.setLogoUrl(source.getLogoUrl());
		getService().saveOrUpdate(persistedSource);
		String[] codes = new String[] { "source.updated" };
		Object[] args = new Object[] { source.getTitle() };
		DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(
				codes, args);
		session.setAttribute("info", message);
		return "redirect:/source/" + identifier + "?form=true";
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
	@RequestMapping(value = "/{identifier}/job", method = RequestMethod.GET, params = "!form")
	public final String list(
			final Model model,
			@PathVariable("identifier") final String identifier,
			@RequestParam(value = "limit", required = false, defaultValue = "10") final Integer limit,
			@RequestParam(value = "start", required = false, defaultValue = "0") final Integer start) {
		model.addAttribute("source", getService().load(identifier));
		model.addAttribute("jobs", jobService.list(identifier, start, limit));
		return "source/job/list";
	}

	/**
	 * @param identifier
	 *            Set the source id
	 * @param model
	 *            Set the model
	 * @return the name of the view
	 */
	@RequestMapping(value = "/{identifier}/job", method = RequestMethod.GET, params = "form")
	public final String create(
			@PathVariable("identifier") final String identifier,
			final Model model) {
		populateForm(model, new Job(), new JobParameterDto());
		model.addAttribute("source", getService().load(identifier));
		return "source/job/create";
	}

	/**
	 * 
	 * @param model
	 *            Set the model
	 * @param job
	 *            Seth the job
	 */
	private void populateForm(final Model model, final Job job, final JobParameterDto parameter) {
		model.addAttribute("job", job);
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
	 * @param job
	 *            Set the job
	 * @param result
	 *            Set the binding results
	 * @return a model and view
	 */
	@RequestMapping(value = "/{sourceIdentifier}/job", method = RequestMethod.POST, produces = "text/html")
	public final String post(
			@PathVariable("sourceIdentifier") final String sourceIdentifier,
			final Model model, @Valid final Job job,
			final BindingResult result, final HttpSession session) {
		Source source = getService().find(sourceIdentifier, "source-with-jobs");
		if (result.hasErrors()) {
			model.addAttribute("source", source);
			populateForm(model, job, new JobParameterDto());
			return "source/job/create";
		}

		source.getJobs().add(job);
		job.setSource(source);
		jobService.saveOrUpdate(job);
		String[] codes = new String[] { "job.was.created" };
		Object[] args = new Object[] { job.getIdentifier() };
		DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(
				codes, args);
		session.setAttribute("info", message);
		return "redirect:/source/" + sourceIdentifier + "/job";
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
	@RequestMapping(value = "/{identifier}/job/{jobId}", method = RequestMethod.GET, 
			produces = "text/html",
			params = {"!output", "!details", "!form", "!parameters"})
	public final String show(
			@PathVariable("identifier") final String identifier,
			@PathVariable("jobId") final String jobId, final Model uiModel) {
		Job job = jobService.load(jobId, "job-with-source");
		assert job.getSource().getIdentifier().equals(identifier);
		uiModel.addAttribute("job", job);
		uiModel.addAttribute("source", job.getSource());
		return "source/job/show";
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
	@RequestMapping(value = "/{identifier}/job/{jobId}", method = RequestMethod.GET, params = "form")
	public final String update(
			@PathVariable("identifier") final String identifier,
			@PathVariable("jobId") final String jobId,
			final Model model) {
		Job job = jobService.load(jobId, "job-with-source");
		assert job.getSource().getIdentifier().equals(identifier);
		populateForm(model, job, new JobParameterDto());
		model.addAttribute("source", job.getSource());
		return "source/job/update";
	}
	
	/**
     * @param identifier
     *            Set the identifier of the source
     * @param jobId the identifier of the job
     * @param name the name of the parameter to add
     * @param session Set the session
     * @return the view name
     */
    @RequestMapping(value = "/{identifier}/job/{jobId}", params = { "parameters", "!delete" }, method = RequestMethod.POST)
    public final String addParameter(@PathVariable final String identifier,
    		@PathVariable final String jobId,
    		@ModelAttribute("parameter") final JobParameterDto parameter,
            final HttpSession session) {
        Job job = jobService.load(jobId, "job-with-source");
        job.getParameters().put(parameter.getName(), "");
        jobService.saveOrUpdate(job);
        String[] codes = new String[] {"parameter.added.to.job" };
        Object[] args = new Object[] { parameter.getName() };
        DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(
                codes, args);
        session.setAttribute("info", message);
        return "redirect:/source/" + identifier + "/job/" + jobId + "?form";
    }

    /**
     * @param identifier
     *            Set the identifier of the source
     * @param jobId Set the job identifier
     * @param name the name of the parameter to delete
     * @param session Set the session
     * @return the view name
     */
    @RequestMapping(value = "/{identifier}/job/{jobId}", params = { "parameters",
            "delete" }, method = RequestMethod.GET)
    public final String removeMember(@PathVariable final String identifier,
    		@PathVariable final String jobId,
    		@RequestParam("name") final String name, final HttpSession session) {
        Job job = jobService.load(jobId, "job-with-source");
        job.getParameters().remove(name);
        jobService.saveOrUpdate(job);
        String[] codes = new String[] {"parameter.removed.from.job" };
        Object[] args = new Object[] { name };
        DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(
                codes, args);
        session.setAttribute("info", message);
        return "redirect:/source/" + identifier + "/job/" + jobId + "?form";
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
	@RequestMapping(value = "/{identifier}/job/{jobId}", method = RequestMethod.POST, produces = "text/html", params = "run")
	public final String run(
			@PathVariable("identifier") final String identifier,
			@PathVariable("jobId") final String jobId, final Model model,
			final HttpSession session) {

		Job job = jobService.load(jobId, "job-with-source");
		assert job.getSource().getIdentifier().equals(identifier);
		if (job.getStatus() != null) {
			switch (job.getStatus()) {
			case STARTED:
			case STARTING:
			case STOPPING:
			case UNKNOWN:
				String[] codes = new String[] { "job.running" };
				Object[] args = new Object[] {};
				DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(
						codes, args);
				session.setAttribute("error", message);
				return "redirect:/source/" + identifier + "/job/"
						+ job.getIdentifier();
			case COMPLETED:
			case FAILED:
			case STOPPED:
			default:
				break;
			}
		}
		Map<String, String> jobParametersMap = new HashMap<String, String>();
		jobParametersMap.put("authority.name", job.getSource().getIdentifier());
		jobParametersMap.put("authority.uri", job.getUri());

		if (job.getStatus() == null) {
			jobParametersMap.put("authority.last.harvested",
					Long.toString((PAST_DATETIME.getMillis())));
		} else {
			jobParametersMap.put("authority.last.harvested",
					Long.toString((job.getStartTime().getMillis())));
		}
		
		jobParametersMap.putAll(job.getParameters());

		JobLaunchRequest jobLaunchRequest = new JobLaunchRequest();
		switch (job.getJobType()) {
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
					new IllegalArgumentException("The jobType " + job.getJobType() + " is not configured"));
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
			job.setStartTime(jobExecutionInfo.getStartTime());
			job.setDuration(jobExecutionInfo.getDuration());
			job.setExitCode(jobExecutionInfo.getExitCode());
			job.setExitDescription(jobExecutionInfo.getExitDescription());
			job.setJobId(jobExecutionInfo.getId());
			if (jobExecutionInfo.getJobInstance() != null) {
				job.setJobInstance(jobExecutionInfo.getJobInstance()
						.getResource());
			}
			job.setLastHarvested(new DateTime());
			job.setResource(jobExecutionInfo.getResource());
			job.setStatus(jobExecutionInfo.getStatus());
			job.setRecordsRead(0);
			job.setReadSkip(0);
			job.setProcessSkip(0);
			job.setWriteSkip(0);
			job.setWritten(0);
			jobService.saveOrUpdate(job);
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
		return "redirect:/source/" + identifier + "/job/" + job.getIdentifier();
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
	 * @param job
	 *            Set the job
	 * @param result
	 *            Set the binding results
	 * @return the view name
	 */
	@RequestMapping(value = "/{identifier}/job/{jobId}", method = RequestMethod.POST, produces = "text/html", params = {"!run","!parameters"})
	public final String post(
			@PathVariable("identifier") final String identifier,
			@PathVariable("jobId") final String jobId, final Model model,
			@Valid final Job job, final BindingResult result,
			final HttpSession session) {
		Job persistedJob = jobService.load(jobId, "job-with-source");

		if (result.hasErrors()) {
			populateForm(model, job, new JobParameterDto());
			model.addAttribute("source", persistedJob.getSource());
			return "source/job/update";
		}

		assert persistedJob.getSource().getIdentifier().equals(identifier);
		persistedJob.setUri(job.getUri());
		persistedJob.setJobType(job.getJobType());
		persistedJob.setLastHarvested(job.getLastHarvested());
		persistedJob.setJobId(job.getJobId());
		persistedJob.setStatus(job.getStatus());
		persistedJob.setStartTime(job.getStartTime());
		persistedJob.setDuration(job.getDuration());
		persistedJob.setExitCode(job.getExitCode());
		persistedJob.setExitDescription(job.getExitDescription());
		persistedJob.setRecordsRead(job.getRecordsRead());
		persistedJob.setReadSkip(job.getReadSkip());
		persistedJob.setProcessSkip(job.getProcessSkip());
		persistedJob.setWriteSkip(job.getWriteSkip());
		persistedJob.setWritten(job.getWritten());
		persistedJob.setParameters(job.getParameters());

		jobService.saveOrUpdate(persistedJob);
		String[] codes = new String[] { "job.was.updated" };
		Object[] args = new Object[] { job.getIdentifier() };
		DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(
				codes, args);
		session.setAttribute("info", message);
		return "redirect:/source/" + identifier + "/job/" + jobId;
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
	@RequestMapping(value = "/{identifier}/job/{jobId}/output", method = RequestMethod.GET)
	public final String search(
			@PathVariable final String identifier,
			@PathVariable final Long jobId,
			@RequestParam(value = "query", required = false) final String query,
		    @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
		    @RequestParam(value = "start", required = false, defaultValue = "0") final Integer start,
		    @RequestParam(value = "facet", required = false) @FacetRequestFormat final List<FacetRequest> facets,
		    @RequestParam(value = "sort", required = false) @SortingFormat final Sorting sort,
		    @RequestParam(value = "view", required = false) String view,
		    final Model model) {
		model.addAttribute(getService().load(identifier));
		Map<FacetName, String> selectedFacets = new HashMap<FacetName, String>();
		if (facets != null && !facets.isEmpty()) {
			for (FacetRequest facetRequest : facets) {
				selectedFacets.put(facetRequest.getFacet(),
						facetRequest.getSelected());
			}
		}
		selectedFacets.put(FacetName.JOB_INSTANCE, jobId.toString());
		Page<Annotation> result = annotationService.search(query, null, limit,
				start, new FacetName[] { FacetName.ERROR_CODE,
						FacetName.ISSUE_TYPE, FacetName.RECORD_TYPE,
						FacetName.JOB_INSTANCE }, selectedFacets, null,
				"annotated-obj");
		result.putParam("query", query);
		model.addAttribute("jobId",jobId);
		model.addAttribute("result", result);

		return "source/job/output";
	}
}
