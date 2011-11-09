package org.emonocot.portal.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.emonocot.model.common.Annotation;
import org.emonocot.model.common.SearchableObject;
import org.emonocot.model.pager.Page;
import org.emonocot.model.source.Source;
import org.emonocot.portal.format.annotation.FacetRequestFormat;
import org.emonocot.service.JobService;
import org.emonocot.api.AnnotationService;
import org.emonocot.api.FacetName;
import org.emonocot.api.SourceService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author ben
 *
 */
@Controller
public class SourceController {

    /**
     *
     */
    private static Logger logger = LoggerFactory
            .getLogger(SourceController.class);
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
    private String baseUrl;

    /**
     *
     */
    private JobService jobService;

    /**
     *
     * @param newBaseUrl
     *            Set the base url
     */
    public final void setBaseUrl(final String newBaseUrl) {
        this.baseUrl = newBaseUrl;
    }

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
    * @param jobService
    *            Set the job service
    */
   @Autowired
   public final void setSourceService(final JobService jobService) {
       this.jobService = jobService;
   }

    /**
     * @param identifier
     *            Set the identifier of the source
     * @return A model and view containing a source
     */
    @RequestMapping(value = "/source/{identifier}", method = RequestMethod.GET)
    public final ModelAndView getSourcePage(
            @PathVariable final String identifier) {
        ModelAndView modelAndView = new ModelAndView("sourcePage");
        modelAndView.addObject(service.load(identifier));
        return modelAndView;
    }

    /**
     * @param identifier
     *            Set the identifier of the source
     * @return A model and view containing a source
     */
    @RequestMapping(value = "/admin/source/{identifier}", method = RequestMethod.GET)
    public final ModelAndView getSourceAdminPage(
            @PathVariable final String identifier,
            @RequestParam(value = "limit", required = false, defaultValue = "10") final Integer limit,
            @RequestParam(value = "start", required = false, defaultValue = "0") final Integer start) {
        ModelAndView modelAndView = new ModelAndView("sourceAdminPage");
        modelAndView.addObject(service.load(identifier));
        List<JobExecution> jobExecutions = jobService.listJobExecutions(identifier, limit, start);
        modelAndView.addObject("jobExecutions", jobExecutions);
        return modelAndView;
    }

    /**
     * @param identifier
     *            Set the identifier of the source
     * @param jobId set the job Id
     * @return A model and view containing a source
     */
    @RequestMapping(value = "/admin/source/{identifier}/jobs/{jobId}", method = RequestMethod.GET)
    public final ModelAndView getSourceJobPage(
            @PathVariable final String identifier,
            @PathVariable final Long jobId,
            @RequestParam(value = "recordType", required = false) final String recordType) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(service.load(identifier));
        modelAndView.addObject("job", jobService.load(jobId));

        if (recordType == null) {
           modelAndView.setViewName("sourceJobPage");
           modelAndView.addObject("results", jobService.countObjects(jobId));
        } else {
            modelAndView.addObject("recordType", recordType);
            modelAndView.setViewName("sourceJobDetails");
            modelAndView.addObject("results", jobService.countErrors(jobId, recordType));
        }
        return modelAndView;
    }

    /**
     * @param identifier
     *            Set the identifier of the source
     * @param jobId set the job Id
     * @return A model and view containing a source
     */
    @RequestMapping(value = "/admin/source/{identifier}/jobs/{jobId}/messages", method = RequestMethod.GET)
    public final ModelAndView getSourceJobMessages(
            @PathVariable final String identifier,
            @PathVariable final Long jobId,
            @RequestParam(value = "query", required = false) final String query,
            @RequestParam(value = "facet", required = false) @FacetRequestFormat final List<FacetRequest> facets,
            @RequestParam(value = "limit", required = false, defaultValue = "10") final Integer limit,
            @RequestParam(value = "start", required = false, defaultValue = "0") final Integer start) {
        ModelAndView modelAndView = new ModelAndView("jobAnnotations");
        modelAndView.addObject(service.load(identifier));
        modelAndView.addObject("job", jobService.load(jobId));
        Map<FacetName, String> selectedFacets = new HashMap<FacetName, String>();
        if (facets != null && !facets.isEmpty()) {
            for (FacetRequest facetRequest : facets) {
                selectedFacets.put(facetRequest.getFacet(),
                        facetRequest.getSelected());
            }
        }
        selectedFacets.put(FacetName.JOB_INSTANCE, jobId.toString());
        Page<Annotation> result = annotationService.search(
                query, null, limit, start, new FacetName[] {
                        FacetName.ERROR_CODE, FacetName.ISSUE_TYPE,
                        FacetName.RECORD_TYPE, FacetName.JOB_INSTANCE },
                selectedFacets, null, "annotated-obj");
        result.putParam("query", query);
        modelAndView.addObject("result", result);

        return modelAndView;
    }

    /**
     * @param identifier
     *            Set the identifier of the source
     * @return A model and view containing a source
     */
    @RequestMapping(value = "/source/{identifier}", method = RequestMethod.GET, headers = "Accept=application/json")
    public final ResponseEntity<Source> get(
            @PathVariable final String identifier) {
        return new ResponseEntity<Source>(service.find(identifier),
                HttpStatus.OK);
    }

    /**
     * @param source
     *            the source to save
     * @return A response entity containing a newly created source
     */
    @RequestMapping(value = "/source", method = RequestMethod.POST)
    public final ResponseEntity<Source> create(@RequestBody final Source source) {
        HttpHeaders httpHeaders = new HttpHeaders();
        try {
            httpHeaders.setLocation(new URI(baseUrl + "source/"
                    + source.getIdentifier()));
        } catch (URISyntaxException e) {
            logger.error(e.getMessage());
        }
        ResponseEntity<Source> response = new ResponseEntity<Source>(
                service.save(source), httpHeaders, HttpStatus.CREATED);
        return response;
    }

    /**
     * @param identifier
     *            Set the identifier of the source
     * @return A response entity containing the status
     */
    @RequestMapping(value = "/source/{identifier}", method = RequestMethod.DELETE)
    public final ResponseEntity<Source> delete(
            @PathVariable final String identifier) {
        service.delete(identifier);
        return new ResponseEntity<Source>(HttpStatus.OK);
    }
}
