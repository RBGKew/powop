package org.emonocot.portal.controller;

import org.emonocot.api.SourceService;
import org.emonocot.model.source.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author ben
 *
 */
@Controller
@RequestMapping("/source")
public class SourceController extends GenericController<Source, SourceService> {

    /**
     *
     */
    public SourceController() {
        super("source");
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
     * @param identifier
     *            Set the identifier of the source
     * @param model Set the model
     * @return the view name
     */
    @RequestMapping(value = "/{identifier}", method = RequestMethod.GET)
    public final String show(@PathVariable final String identifier,
            final Model model) {
        model.addAttribute(getService().find(identifier));
        return "source/show";
    }
}
