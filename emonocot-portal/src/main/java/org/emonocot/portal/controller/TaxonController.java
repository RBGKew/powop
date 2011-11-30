package org.emonocot.portal.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.emonocot.api.TaxonService;
import org.emonocot.model.taxon.AlphabeticalTaxonComparator;
import org.emonocot.model.taxon.Taxon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.hibernate3.HibernateObjectRetrievalFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.rc.retroweaver.runtime.Collections;

/**
 *
 * @author ben
 *
 */
@Controller
public class TaxonController {

    /**
    *
    */
    private static Logger logger = LoggerFactory
            .getLogger(TaxonController.class);
    /**
    *
    */
    private TaxonService service;

    /**
    *
    */
    private String baseUrl;

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
     * @param taxonService
     *            Set the taxon service
     */
    @Autowired
    public final void setTaxonService(final TaxonService taxonService) {
        this.service = taxonService;
    }

    /**
     * @return the model and view
     */
    @RequestMapping(value = "/classify", method = RequestMethod.GET)
    public final ModelAndView getClassification() {
        ModelAndView modelAndView = new ModelAndView("classificationPage");
        return modelAndView;
    }

    /**
     * @return the list of nodes
     */
    @RequestMapping(value = "/taxonTree",
                    method = RequestMethod.GET,
                    headers = "Accept=application/json")
    public final @ResponseBody
    List<Node> getTaxonTreeRoots() {
        List<Taxon> results = service.loadChildren(null, 10, 0, "taxon-with-children");
        List<Node> nodes = new ArrayList<Node>();
        for (Taxon result : results) {
            nodes.add(new Node(result));
        }
        return nodes;
    }

    /**
     * @param identifier set the identifier
     * @return the list of nodes
     */
    @RequestMapping(value = "/taxonTree/{identifier}",
                    method = RequestMethod.GET,
                    headers = "Accept=application/json")
    public final @ResponseBody
    List<Node> getTaxonTreeNode(@PathVariable final String identifier) {
        List<Taxon> results = service.loadChildren(identifier, null, null, "taxon-with-children");
        List<Node> nodes = new ArrayList<Node>();
        for (Taxon result : results) {
            nodes.add(new Node(result));
        }
        return nodes;
    }

    /**
     * @param identifier
     *            Set the identifier of the taxon
     * @return A model and view containing a taxon
     */
    @RequestMapping(value = "/taxon/{identifier}", method = RequestMethod.GET)
    public final ModelAndView getTaxonPage(@PathVariable final String identifier) {
        ModelAndView modelAndView = new ModelAndView("taxonPage");
        modelAndView.addObject(service.load(identifier, "taxon-page"));
        return modelAndView;
    }

    /**
     * @param identifier
     *            Set the identifier of the taxon
     * @return A model and view containing a taxon
     */
    @RequestMapping(value = "/taxon/{identifier}", method = RequestMethod.GET, headers = "Accept=application/json")
    public final ResponseEntity<Taxon> get(@PathVariable final String identifier) {
        return new ResponseEntity<Taxon>(service.find(identifier),
                HttpStatus.OK);
    }

    /**
     * @param taxon
     *            the taxon to save
     * @return A response entity containing a newly created taxon
     */
    @RequestMapping(value = "/taxon", method = RequestMethod.POST)
    public final ResponseEntity<Taxon> create(@RequestBody final Taxon taxon) {
        HttpHeaders httpHeaders = new HttpHeaders();
        try {
            httpHeaders.setLocation(new URI(baseUrl + "taxon/"
                    + taxon.getIdentifier()));
        } catch (URISyntaxException e) {
            logger.error(e.getMessage());
        }
        ResponseEntity<Taxon> response = new ResponseEntity<Taxon>(
                service.save(taxon), httpHeaders, HttpStatus.CREATED);
        return response;
    }

    /**
     * @param identifier
     *            Set the identifier of the taxon
     * @return A response entity containing the status
     */
    @RequestMapping(value = "/taxon/{identifier}", method = RequestMethod.DELETE)
    public final ResponseEntity<Taxon> delete(
            @PathVariable final String identifier) {
        service.delete(identifier);
        return new ResponseEntity<Taxon>(HttpStatus.OK);
    }

    /**
     * Used to return the taxon tree nodes.
     * @author ben
     *
     */
    class Node {
        /**
         *
         */
        private Map<String, Object> data = new HashMap<String, Object>();

        /**
         *
         */
        private String state = "closed";

        /**
         *
         */
        private Map<String, String> attr = new HashMap<String, String>();

        /**
         *
         */
        private List<Node> children = new ArrayList<Node>();

        /**
         *
         * @param taxon Set the taxon
         */
        public Node(final Taxon taxon) {
            data.put("title", taxon.getName());
            Map<String, String> dataAttr = new HashMap<String, String>();
            dataAttr.put("href", "taxon/" + taxon.getIdentifier());
            data.put("attr", dataAttr);
            attr.put("id", taxon.getIdentifier());
            if (!taxon.getChildren().isEmpty()) {
                List<Taxon> sortedChildren = new ArrayList<Taxon>(
                        taxon.getChildren());
                Collections.sort(sortedChildren,
                        new AlphabeticalTaxonComparator());
                for (Taxon child : sortedChildren) {
                    children.add(new Node(child.getName(), child
                            .getIdentifier()));
                }
            } else {
                state = null;
            }
        }

        /**
         * @return the state
         */
        public final String getState() {
            return state;
        }

        /**
         * @param newState the state to set
         */
        public final void setState(final String newState) {
            this.state = newState;
        }

        /**
        *
        * @param name Set the name
        * @param identifier Set the identifier
        */
       public Node(final String name, final String identifier) {
           data.put("title", name);
           Map<String, String> dataAttr = new HashMap<String, String>();
           dataAttr.put("href", "taxon/" + identifier);
           data.put("attr", dataAttr);
           attr.put("id", identifier);
       }

        /**
         * @return the data
         */
        public final Map<String, Object> getData() {
            return data;
        }

        /**
         * @param newData the data to set
         */
        public final void setData(final Map<String, Object> newData) {
            this.data = newData;
        }

        /**
         * @return the attr
         */
        public final Map<String, String> getAttr() {
            return attr;
        }

        /**
         * @param newAttr the attr to set
         */
        public final void setAttr(final Map<String, String> newAttr) {
            this.attr = newAttr;
        }

        /**
         * @return the children
         */
        public final List<Node> getChildren() {
            return children;
        }

        /**
         * @param newChildren the children to set
         */
        public final void setChildren(final List<Node> newChildren) {
            this.children = newChildren;
        }
    }

   /**
    *
    * @param ex The exception
    * @param request The httpServletRequest
    * @return a model and view containing the exception
    */
   @ResponseStatus(HttpStatus.NOT_FOUND)
   @ExceptionHandler({ HibernateObjectRetrievalFailureException.class })
   public final ModelAndView handleBadArgument(final Exception ex,
           final HttpServletRequest request) {
       ModelAndView modelAndView = new ModelAndView("404");
       modelAndView.addObject("exception", ex);
       return modelAndView;
   }
}
