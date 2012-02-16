package org.emonocot.portal.controller;

import org.emonocot.api.SourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
     * @param model Set the model
     * @param limit Set the maximum number of objects to return
     * @param start Set the offset
     * @return the name of the view
     */
    @RequestMapping(method = RequestMethod.GET)
    public final String list(final Model model,
            @RequestParam(value = "limit", required = false, defaultValue = "10") final Integer limit,
            @RequestParam(value = "start", required = false, defaultValue = "0") final Integer start) {
        model.addAttribute("result", service.list(limit, start));
        return "admin/source/list";
    }
}
