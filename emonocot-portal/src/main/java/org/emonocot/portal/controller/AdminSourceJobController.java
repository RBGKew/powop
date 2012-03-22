package org.emonocot.portal.controller;

import java.util.Arrays;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.emonocot.api.JobService;
import org.emonocot.api.SourceService;
import org.emonocot.model.job.Job;
import org.emonocot.model.job.JobType;
import org.emonocot.model.source.Source;
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
@RequestMapping("/admin/source/{identifier}/job")
public class AdminSourceJobController {

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
   private JobService jobService;

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
       this.service = sourceService;
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
   @RequestMapping(method = RequestMethod.GET, params = "!form")
   public final String list(
           final Model model,
           @PathVariable("identifier") final String identifier,
           @RequestParam(value = "limit", required = false, defaultValue = "10") final Integer limit,
           @RequestParam(value = "start", required = false, defaultValue = "0") final Integer start) {
       model.addAttribute("source", service.load(identifier));
       model.addAttribute("jobs", jobService.list(identifier, start, limit));
       return "admin/source/job/list";
   }

   /**
    * @param identifier
    *            Set the source id
    * @param model
    *            Set the model
    * @return the name of the view
    */
   @RequestMapping(method = RequestMethod.GET, params = "form")
   public final String create(
           @PathVariable("identifier") final String identifier,
           final Model model) {
       populateForm(model, new Job());
       model.addAttribute("source", service.load(identifier));
       return "admin/source/job/create";
   }

   /**
    *
    * @param model Set the model
    * @param job Seth the job
    */
   private void populateForm(final Model model, final Job job) {
       model.addAttribute("job", job);
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
   @RequestMapping(method = RequestMethod.POST, headers = "Accept=text/html")
   public final String post(
           @PathVariable("identifier") final String identifier,
           final Model model, @Valid final Job job,
           final BindingResult result, final HttpSession session) {
       Source source = service.find(identifier, "source-with-jobs");
       if (result.hasErrors()) {
           model.addAttribute("source", source);
           populateForm(model, job);
           return "admin/source/job/create";
       }

       source.getJobs().add(job);
       job.setSource(source);
       jobService.saveOrUpdate(job);
       String[] codes = new String[] {"job.was.created" };
       Object[] args = new Object[] {job.getIdentifier() };
       DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(
               codes, args);
       session.setAttribute("info", message);
       return "redirect:/admin/source/" + identifier + "/job";
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
   @RequestMapping(value = "/{jobId}", produces = "text/html")
   public final String show(@PathVariable("identifier") final String identifier,
           @PathVariable("jobId") final String jobId,
           final Model uiModel) {
       Job job = jobService.load(jobId, "job-with-source");
       assert job.getSource().getIdentifier().equals(identifier);
       uiModel.addAttribute("job", job);
       uiModel.addAttribute("source", job.getSource());
       return "admin/source/job/show";
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
   @RequestMapping(value = "/{jobId}", method = RequestMethod.GET, params = "form")
   public final String update(@PathVariable("identifier") final String identifier,
           @PathVariable("jobId") final String jobId, final Model model) {
       Job job = jobService.load(jobId, "job-with-source");
       assert job.getSource().getIdentifier().equals(identifier);
       populateForm(model, job);
       model.addAttribute("source", job.getSource());
       return "admin/source/job/update";
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
   @RequestMapping(value = "/{jobId}", method = RequestMethod.POST, headers = "Accept=text/html")
   public final String post(@PathVariable("identifier") final String identifier,
           @PathVariable("jobId") final String jobId, final Model model,
           @Valid final Job job, final BindingResult result,
           final HttpSession session) {
       Job persistedJob = jobService.load(jobId, "job-with-source");

       if (result.hasErrors()) {
           populateForm(model, job);
           model.addAttribute("source", persistedJob.getSource());
           return "admin/source/job/update";
       }

       assert persistedJob.getSource().getIdentifier().equals(identifier);
       persistedJob.setFamily(job.getFamily());
       persistedJob.setUri(job.getUri());
       persistedJob.setJobType(job.getJobType());

       jobService.saveOrUpdate(persistedJob);
       String[] codes = new String[] {"job.was.updated" };
       Object[] args = new Object[] {job.getIdentifier() };
       DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(
               codes, args);
       session.setAttribute("info", message);
       return "redirect:/admin/source/" + identifier + "/job/" + jobId;
   }

}
