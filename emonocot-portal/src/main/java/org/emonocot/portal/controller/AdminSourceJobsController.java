package org.emonocot.portal.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.emonocot.api.JobService;
import org.emonocot.api.SourceService;
import org.emonocot.model.job.Job;
import org.emonocot.model.source.Source;

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
@RequestMapping("/admin/source/{sourceId}/job")
public class AdminSourceJobsController {

    /**
    *
    */
    private JobService service;

    /**
     *
     * @param jobService
     *            Set the source service
     */
    @Autowired
    public final void setJobService(final JobService jobService) {
        this.service = jobService;
    }

    /**
   *
   */
    private SourceService sourceService;

    /**
     *
     * @param newSourceService
     *            Set the source service
     */
    @Autowired
    public final void setSourceService(final SourceService newSourceService) {
        this.sourceService = newSourceService;
    }

    /**
     *
     * @param model
     *            Set the model
     * @param sourceId
     *            Set the source identifier,
     * @param limit
     *            Set the maximum number of objects to return
     * @param start
     *            Set the offset
     * @return the name of the view
     */
    @RequestMapping(method = RequestMethod.GET, params = "!form")
    public final String list(
            final Model model,
            @PathVariable("sourceId") final String sourceId,
            @RequestParam(value = "limit", required = false, defaultValue = "10") final Integer limit,
            @RequestParam(value = "start", required = false, defaultValue = "0") final Integer start) {
        model.addAttribute("result", service.list(sourceId, start, limit));
        return "admin/source/job/list";
    }

    /**
     *
     * @param model
     *            Set the model
     * @return the name of the view
     */
    @RequestMapping(method = RequestMethod.GET, params = "form")
    public final String create(final Model model) {
        model.addAttribute(new Job());
        return "admin/source/job/create";
    }

    /**
     * @param sourceId
     *            Set the sourceId
     * @param session
     *            Set the session
     * @param job
     *            Set the job
     * @param result
     *            Set the binding results
     * @return a model and view
     */
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=text/html")
    public final String post(@PathVariable("sourceId") final String sourceId,
            @Valid final Job job, final BindingResult result,
            final HttpSession session) {
        if (result.hasErrors()) {
            return "admin/source/job/create";
        }

        Source source = sourceService.find(sourceId, "source-with-jobs");
        source.getJobs().add(job);
        job.setSource(source);
        service.saveOrUpdate(job);
        String[] codes = new String[] {"job.was.created" };
        Object[] args = new Object[] {source.getTitle() };
        DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(
                codes, args);
        session.setAttribute("info", message);
        return "redirect:/admin/source/" + source.getIdentifier() + "/job";
    }

    /**
     * @param sourceId
     *            Set the source identifier
     * @param identifier
     *            Set the identifier
     * @param uiModel
     *            Set the model
     * @return the view name
     */
    @RequestMapping(value = "/{identifier}", produces = "text/html")
    public final String show(@PathVariable("sourceId") final String sourceId,
            @PathVariable("identifier") final String identifier,
            final Model uiModel) {
        Job job = service.load(identifier, "job-with-source");
        assert job.getSource().getIdentifier().equals(sourceId);
        uiModel.addAttribute("job", job);
        return "admin/source/job/show";
    }

    /**
     * @param sourceId Set the sourceId
     * @param model
     *            Set the model
     * @param identifier
     *            Set the identifier
     * @return the name of the view
     */
    @RequestMapping(value = "/{identifier}", method = RequestMethod.GET, params = "form")
    public final String update(@PathVariable("sourceId") final String sourceId,
            @PathVariable final String identifier, final Model model) {
        Job job = service.load(identifier, "job-with-source");
        assert job.getSource().getIdentifier().equals(sourceId);
        model.addAttribute("job", job);
        return "admin/source/job/update";
    }

    /**
     * @param sourceId Set the source identifier
     * @param identifier
     *            Set the identifier
     * @param session
     *            Set the session
     * @param job
     *            Set the job
     * @param result
     *            Set the binding results
     * @return a model and view
     */
    @RequestMapping(value = "/{identifier}", method = RequestMethod.POST, headers = "Accept=text/html")
    public final String post(@PathVariable("sourceId") final String sourceId,
            @PathVariable final String identifier,
            @Valid final Job job, final BindingResult result,
            final HttpSession session) {

        if (result.hasErrors()) {
            return "admin/source/job/update";
        }
        Job persistedJob = service.load(identifier, "job-with-source");
        assert persistedJob.getSource().getIdentifier().equals(sourceId);

        service.saveOrUpdate(persistedJob);
        String[] codes = new String[] {"job.updated" };
        Object[] args = new Object[] {job.getIdentifier() };
        DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(
                codes, args);
        session.setAttribute("info", message);
        return "redirect:/admin/source/" + sourceId + "/job/" + identifier
                + "?form=true";
    }
}
