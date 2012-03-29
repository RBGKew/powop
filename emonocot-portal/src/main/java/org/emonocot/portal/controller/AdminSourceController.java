package org.emonocot.portal.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.emonocot.api.JobService;
import org.emonocot.api.SourceService;
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
     * @param sourceService
     *            Set the source service
     */
    @Autowired
    public final void setSourceService(final SourceService sourceService) {
        this.service = sourceService;
    }

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
     * @param model
     *            Set the model
     * @param limit
     *            Set the maximum number of objects to return
     * @param start
     *            Set the offset
     * @return the name of the view
     */
    @RequestMapping(method = RequestMethod.GET, params = "!form")
    public final String list(
            final Model model,
            @RequestParam(value = "page", defaultValue = "0", required = false) final Integer page,
            @RequestParam(value = "size", defaultValue = "10", required = false) final Integer size) {
        model.addAttribute("result", service.list(page, size));
        return "admin/source/list";
    }

    /**
     *
     * @param identifier
     *            Set the identifier
     * @param uiModel
     *            Set the model
     * @return the view name
     */
    @RequestMapping(value = "/{identifier}", produces = "text/html")
    public final String show(
            @PathVariable("identifier") final String identifier,
            final Model uiModel) {
        uiModel.addAttribute("source", service.load(identifier));
        uiModel.addAttribute("jobs", jobService.list(identifier, 0, 10));
        return "admin/source/show";
    }

    /**
     *
     * @param model
     *            Set the model
     * @return the name of the view
     */
    @RequestMapping(method = RequestMethod.GET, params = "form")
    public final String create(final Model model) {
        model.addAttribute(new Source());
        return "admin/source/create";
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
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=text/html")
    public final String post(@Valid final Source source,
            final BindingResult result, final HttpSession session) {
        if (result.hasErrors()) {
            return "admin/source/create";
        }

        service.saveOrUpdate(source);
        String[] codes = new String[] {"source.was.created" };
        Object[] args = new Object[] {source.getTitle() };
        DefaultMessageSourceResolvable message = new DefaultMessageSourceResolvable(
                codes, args);
        session.setAttribute("info", message);
        return "redirect:/source/" + source.getIdentifier() + "?form=true";
    }
}
