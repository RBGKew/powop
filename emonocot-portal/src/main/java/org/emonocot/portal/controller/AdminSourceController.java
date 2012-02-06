package org.emonocot.portal.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.emonocot.api.AnnotationService;
import org.emonocot.api.FacetName;
import org.emonocot.api.SourceService;
import org.emonocot.model.common.Annotation;
import org.emonocot.model.pager.Page;
import org.emonocot.model.source.Source;
import org.emonocot.portal.format.annotation.FacetRequestFormat;
import org.emonocot.service.JobDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
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
@RequestMapping("/admin/source")
public class AdminSourceController {

    /**
     *
     */
    private static Logger logger = LoggerFactory
            .getLogger(AdminSourceController.class);
    /**
     *
     */
    private SourceService service;

    /**
     *
     */
    private AnnotationService annotationService;

    /**
     *
     */
    private JobDataService jobDataService;

    /**
     *
     * @param sourceService
     *            Set the source service
     */
    @Autowired
    public final void setSourceService(final SourceService sourceService) {
        this.service = sourceService;
    }

    /**
     * @param annotationService Set the annotation service
     */
    @Autowired
    public final void setAnnotationService(final AnnotationService annotationService) {
        this.annotationService = annotationService;
    }

   /**
    *
    * @param newJobDataService
    *            Set the job service
    */
   @Autowired
   public final void setSourceService(final JobDataService newJobDataService) {
       this.jobDataService = newJobDataService;
   }

    /**
     * @param identifier
     *            Set the identifier of the source
     * @param limit Set the maximum number of results
     * @param start Set the offset
     * @param model Set the model
     * @return the view name
     */
    @RequestMapping(value = "/{identifier}", method = RequestMethod.GET, params = "!form")
    public final String show(
            @PathVariable final String identifier,
            @RequestParam(value = "limit", required = false, defaultValue = "10") final Integer limit,
            @RequestParam(value = "start", required = false, defaultValue = "0") final Integer start,
            final Model model) {
        model.addAttribute(service.load(identifier));
        List<JobExecution> jobExecutions = jobDataService.listJobExecutions(
                identifier, limit, start);
        model.addAttribute("jobExecutions", jobExecutions);
        return "admin/source/show";
    }

    /**
    *
    * @param model
    *            Set the model
    * @return the name of the view
    */
   @RequestMapping(value = "/{identifier}", method = RequestMethod.GET, params="form")
   public final String update(@PathVariable final String identifier, final Model model) {
       model.addAttribute(service.load(identifier));
       return "admin/source/update";
   }

    /**
     * @param identifier
     *            Set the identifier of the source
     * @param jobId set the job Id
     * @param model Set the model
     * @return the view name
     */
    @RequestMapping(value = "/{identifier}/jobs/{jobId}", method = RequestMethod.GET)
    public final String getSourceJobPage(
            @PathVariable final String identifier,
            @PathVariable final Long jobId,
            @RequestParam(value = "recordType", required = false) final String recordType,
            final Model model) {
        model.addAttribute(service.load(identifier));
        model.addAttribute("job", jobDataService.find(jobId));

        if (recordType == null) {
            model.addAttribute("results",
                    jobDataService.countObjects(jobId));
            return "admin/source/job";
        } else {
            model.addAttribute("recordType", recordType);
            model.addAttribute("results",
                    jobDataService.countErrors(jobId, recordType));
            return "admin/source/jobDetails";
        }
    }

    /**
     * @param identifier
     *            Set the identifier of the source
     * @param query Set the query
     * @param jobId Set the job Id
     * @param facets Set the facets
     * @param limit Set the page size
     * @param start Set the page offset
     * @param model Set the model
     * @return A model and view containing a source
     */
    @RequestMapping(value = "/{identifier}/jobs/{jobId}/messages", method = RequestMethod.GET)
    public final String getSourceJobMessages(
            @PathVariable final String identifier,
            @PathVariable final Long jobId,
            @RequestParam(value = "query", required = false) final String query,
            @RequestParam(value = "facet", required = false) @FacetRequestFormat final List<FacetRequest> facets,
            @RequestParam(value = "limit", required = false, defaultValue = "10") final Integer limit,
            @RequestParam(value = "start", required = false, defaultValue = "0") final Integer start,
            final Model model) {
        model.addAttribute(service.load(identifier));
        model.addAttribute("job", jobDataService.find(jobId));
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
        model.addAttribute("result", result);

        return "admin/source/jobAnnotations";
    }

    /**
     * @param identifier Set the identifier
     * @param session Set the session
     * @param source
     *            Set the source
     * @param result
     *            Set the binding results
     * @return a model and view
     */
    @RequestMapping(value = "/{identifier}", method = RequestMethod.POST, headers = "Accept=text/html")
    public final String post(@PathVariable final String identifier,
            @Valid final Source source, final BindingResult result,
            final HttpSession session) {

        if (result.hasErrors()) {
            return "admin/source/update";
        }
        Source persistedSource = service.load(identifier);
        persistedSource.setTitle(source.getTitle());
        persistedSource.setUri(source.getUri());
        persistedSource.setCreator(source.getCreator());
        persistedSource.setCreatorEmail(source.getCreatorEmail());
        persistedSource.setCreated(source.getCreated());
        persistedSource.setDescription(source.getDescription());
        persistedSource.setPublisherName(source.getPublisherName());
        persistedSource.setPublisherEmail(source.getPublisherEmail());
        persistedSource.setSubject(source.getSubject());
        persistedSource.setSource(source.getSource());
        persistedSource.setLogoUrl(source.getLogoUrl());
        service.saveOrUpdate(persistedSource);
        String[] codes = new String[] {"source.updated" };
        Object[] args = new Object[] {source.getTitle() };
        DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(
                codes, args);
        session.putValue("info", message);
        return "redirect:/admin/source/" + identifier + "?form=true";
    }
}
