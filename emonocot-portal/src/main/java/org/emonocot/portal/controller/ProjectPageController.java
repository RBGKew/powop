/**
 * 
 */
package org.emonocot.portal.controller;

import org.emonocot.api.SourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author jk00kg
 *
 */
@Controller
public class ProjectPageController {

    /**
     *
     */
    private SourceService sourceService;

    /**
     * @param service Set the Source service
     */
    @Autowired
    public void setUserService(final SourceService service) {
        sourceService = service;
    }

    /**
     * @param model Set the model
     * @return A model and view containing a user
     */
    @RequestMapping(value = "/about")
    public final String show(final Model model) {
        model.addAttribute(sourceService.list(null, null));
        return "about";
    }

}
