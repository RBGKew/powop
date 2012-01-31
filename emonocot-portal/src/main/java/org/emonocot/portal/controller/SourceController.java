package org.emonocot.portal.controller;

import java.net.URI;
import java.net.URISyntaxException;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
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
    private JobDataService jobDataService;

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
     * @return A model and view containing a source
     */
    @RequestMapping(value = "/source/{identifier}", method = RequestMethod.GET)
    public final ModelAndView getSourcePage(
            @PathVariable final String identifier) {
        ModelAndView modelAndView = new ModelAndView("sourcePage");
        modelAndView.addObject(service.find(identifier));
        return modelAndView;
    }

    /**
     * @param identifier
     *            Set the identifier of the source
     * @return A model and view containing a source
     */
    @RequestMapping(value = "/admin/source/{identifier}", method = RequestMethod.GET, params = "!form")
    public final ModelAndView getPage(
            @PathVariable final String identifier,
            @RequestParam(value = "limit", required = false, defaultValue = "10") final Integer limit,
            @RequestParam(value = "start", required = false, defaultValue = "0") final Integer start) {
        ModelAndView modelAndView = new ModelAndView("sourceAdminPage");
        modelAndView.addObject(service.load(identifier));
        List<JobExecution> jobExecutions = jobDataService.listJobExecutions(
                identifier, limit, start);
        modelAndView.addObject("jobExecutions", jobExecutions);
        return modelAndView;
    }

    /**
    *
    * @param modelMap
    *            Set the model map
    * @return the name of the view
    */
   @RequestMapping(value = "/admin/source/{identifier}", method = RequestMethod.GET, params="form")
   public final String createForm(@PathVariable final String identifier, final ModelMap modelMap) {
       modelMap.addAttribute(service.load(identifier));
       return "sourceAdminForm";
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
        modelAndView.addObject("job", jobDataService.find(jobId));

        if (recordType == null) {
            modelAndView.setViewName("sourceJobPage");
            modelAndView.addObject("results",
                    jobDataService.countObjects(jobId));
        } else {
            modelAndView.addObject("recordType", recordType);
            modelAndView.setViewName("sourceJobDetails");
            modelAndView.addObject("results",
                    jobDataService.countErrors(jobId, recordType));
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
        modelAndView.addObject("job", jobDataService.find(jobId));
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
     * 
     * @param source
     *            Set the source
     * @param result
     *            Set the binding results
     * @return a model and view
     */
    @RequestMapping(value = "/admin/source/{identifier}", method = RequestMethod.POST, headers = "Accept=text/html")
    public final String post(@PathVariable final String identifier,
            @Valid final Source source, final BindingResult result,
            final HttpSession session) {

        if (result.hasErrors()) {
            return "sourceAdminForm";
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
