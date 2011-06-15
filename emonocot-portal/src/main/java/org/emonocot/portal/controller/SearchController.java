package org.emonocot.portal.controller;

import org.emonocot.service.TaxonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author ben
 *
 */
@Controller
@RequestMapping("/search")
public class SearchController {
    /**
     *
     */
    private TaxonService taxonService;

    /**
     *
     * @param taxonService Set the taxon service
     */
    @Autowired
    public final void setTaxonService(final TaxonService taxonService) {
        this.taxonService = taxonService;
    }

    /**
     *
     * @param query Set the query
     * @param limit Limit the number of returned results
     * @param start Set the offset
     * @return a model and view
     */
    public final ModelAndView search(
            @RequestParam(value = "query", required = false) final String query,
            @RequestParam(value = "limit",
                    required = false, defaultValue = "10") final Integer limit,
            @RequestParam(value = "start",
                    required = false, defaultValue = "0") final Integer start) {
        ModelAndView modelAndView = new ModelAndView("searchResponse");
        modelAndView.addObject("result",
                taxonService.search(query, null, limit, start, null, null));
        return modelAndView;
    }
}
