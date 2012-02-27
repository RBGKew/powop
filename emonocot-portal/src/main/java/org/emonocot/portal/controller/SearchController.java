package org.emonocot.portal.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.emonocot.api.FacetName;
import org.emonocot.api.ImageService;
import org.emonocot.api.SearchableObjectService;
import org.emonocot.api.Sorting;
import org.emonocot.api.TaxonService;
import org.emonocot.model.common.SearchableObject;
import org.emonocot.model.media.Image;
import org.emonocot.model.pager.Page;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.portal.format.annotation.FacetRequestFormat;
import org.emonocot.portal.format.annotation.SortingFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author ben
 *
 */
@Controller
public class SearchController {

    /**
    *
    */
    private static Logger queryLog = LoggerFactory.getLogger("query");

    /**
     *
     */
    private static Logger logger = LoggerFactory
            .getLogger(SearchController.class);
    /**
     *
     */
    private TaxonService taxonService;

    /**
     *
     */
    private SearchableObjectService searchableObjectService;

    /**
     *
     */
    private ImageService imageService;

    /**
     *
     * @param newTaxonService
     *            Set the taxon service
     */
    @Autowired
    public final void setTaxonService(final TaxonService newTaxonService) {
        this.taxonService = newTaxonService;
    }

    /**
     *
     * @param newSearchableObjectService
     *            set the service to search across all 'searchable' objects
     */
    @Autowired
    public final void setSearchableObjectService(
            final SearchableObjectService newSearchableObjectService) {
        this.searchableObjectService = newSearchableObjectService;
    }

    /**
     *
     * @param newImageService
     *            set the image service
     */
    @Autowired
    public final void setImageService(final ImageService newImageService) {
        this.imageService = newImageService;
    }

    /**
     *
     * @param query
     *            Set the query
     * @param limit
     *            Limit the number of returned results
     * @param start
     *            Set the offset
     * @param facets
     *            The facets to set
     * @param view Set the view
     * @param model Set the model
     *
     * @return a model and view
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public final String search(
        @RequestParam(value = "query", required = false) final String query,
        @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
        @RequestParam(value = "start", required = false, defaultValue = "0") final Integer start,
        @RequestParam(value = "facet", required = false) @FacetRequestFormat final List<FacetRequest> facets,
        @RequestParam(value = "sort", required = false) @SortingFormat final Sorting sort,
        @RequestParam(value = "view", required = false, defaultValue = "list") final String view,
        final Model model) {

        Map<FacetName, String> selectedFacets = null;
        if (facets != null && !facets.isEmpty()) {
            selectedFacets = new HashMap<FacetName, String>();
            for (FacetRequest facetRequest : facets) {
                selectedFacets.put(facetRequest.getFacet(),
                        facetRequest.getSelected());
            }
            logger.debug(selectedFacets.size()
                    + " facets have been selected from " + facets.size()
                    + " available");
        } else {
            logger.debug("There were no facets available to select from");
        }

        //Decide which facets to return
        List<FacetName> responseFacetList = new ArrayList<FacetName>();
        responseFacetList.add(FacetName.CLASS);
        responseFacetList.add(FacetName.FAMILY);
        responseFacetList.add(FacetName.CONTINENT);
        responseFacetList.add(FacetName.AUTHORITY);
        if (selectedFacets == null) {
            logger.debug("No selected facets, setting default response facets");
        } else {
            if (selectedFacets.containsKey(FacetName.CLASS)) {
                if (selectedFacets.get(FacetName.CLASS).equals(
                        "org.emonocot.model.taxon.Taxon")) {
                    logger.debug("Adding taxon specific facets");
                    responseFacetList.add(FacetName.RANK);
                    responseFacetList.add(FacetName.TAXONOMIC_STATUS);
                }
            }
            if (selectedFacets.containsKey(FacetName.CONTINENT)) {
                logger.debug("Adding region facet");
                responseFacetList.add(FacetName.REGION);
            } else {
                selectedFacets.remove(FacetName.REGION);
            }
        }
        FacetName[] responseFacets = new FacetName[]{};
        responseFacets = responseFacetList.toArray(responseFacets);

        if (view == null || view.equals("list")) {
            limit = 10;
        } else if (view.equals("grid")) {
            limit = 20;
        } else {
            limit = 20;
        }
        //Run the search
        Page<? extends SearchableObject> result = null;
        if (selectedFacets == null
                || !selectedFacets.containsKey(FacetName.CLASS)) {
            result = searchableObjectService.search(
                    query, null, limit, start, responseFacets,
                    selectedFacets, sort, "taxon-with-image");
        } else {
            if (selectedFacets.get(FacetName.CLASS).equals(
                    "org.emonocot.model.media.Image")) {
                logger.debug("Using the image service for " + query);
                result = imageService.search(query, null, limit, start,
                        responseFacets,
                        selectedFacets, sort, "image-taxon");
            } else if (selectedFacets.get(FacetName.CLASS).equals(
                    "org.emonocot.model.taxon.Taxon")) {
                logger.debug("Using the taxon service for " + query);
                result = taxonService.search(query, null, limit, start,
                        responseFacets,
                        selectedFacets, sort, "taxon-with-image");
            } else {
                logger.error("We can't search by an object of FacetName.CLASS idx="
                        + selectedFacets.get(FacetName.CLASS));
            }
        }
        queryLog.info("Query: \'{}\', start: {}, limit: {},"
                + "facet: [{}], {} results", new Object[] {query,
                start, limit, selectedFacets, result.getSize() });
        result.putParam("query", query);

        result.putParam("view", view);

        result.setSort(sort);
        model.addAttribute("result", result);
        return "search";
    }

    /**
     * @param term
     *            The term to search for
     * @return A list of terms to serialize
     */
    @RequestMapping(value = "/autocomplete",
                    method = RequestMethod.GET,
                    headers = "Accept=application/json")
    public final @ResponseBody List<Match> get(
            @RequestParam(required = true) final String term) {
        Page<SearchableObject> result = searchableObjectService.search(term
                + "*", null, 10, 0, null, null, null, null);
        List<Match> matches = new ArrayList<Match>();
        for (SearchableObject object : result.getRecords()) {
            if (object.getClass().equals(Taxon.class)) {
                matches.add(new Match(((Taxon) object).getName()));
            } else {
                matches.add(new Match(((Image) object).getCaption()));
            }
        }
        return matches;
    }

    /**
     * Used to return the autocomplete matches.
     *
     * @author ben
     *
     */
    class Match {

        /**
         *
         */
        private String label;

        /**
         * @return the label
         */
        public final String getLabel() {
            return label;
        }

        /**
         * @param newLabel the label to set
         */
        public final void setLabel(final String newLabel) {
            this.label = newLabel;
        }

        /**
         *
         * @param newLabel Set the label
         */
        public Match(final String newLabel) {
            this.label = newLabel;
        }
    }
}
