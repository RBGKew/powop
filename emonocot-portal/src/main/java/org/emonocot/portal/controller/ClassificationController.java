package org.emonocot.portal.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.lang.StringBuilder;

import org.emonocot.api.TaxonService;
import org.emonocot.model.IdentificationKey;
import org.emonocot.model.Taxon;
import org.emonocot.model.util.AlphabeticalTaxonComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rc.retroweaver.runtime.Collections;

/**
 *
 * @author ben
 *
 */
@Controller
public class ClassificationController {

    /**
     *
     */
    private TaxonService taxonService;

    /**
     * @param service Set the taxon service
     */
    @Autowired
    public final void setTaxonService(final TaxonService service) {
        this.taxonService = service;
    }
    
    @RequestMapping(value = "/classification" , method = RequestMethod.GET)
    public final String classification(final Model model){
    	List<Taxon> results = taxonService.loadChildren(null, 20, 0, "classification-tree");
    	model.addAttribute("result", results);
    	return "classification";
    }
    /**
     * @return the list of nodes
     */
    @RequestMapping(value = "/taxonTree", method = RequestMethod.GET,
                    produces = "application/json")
    public final @ResponseBody
    List<Node> getTaxonTreeRoots() {
        List<Taxon> results = taxonService.loadChildren(null, 20, 0, "classification-tree");
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
                    produces = "application/json")
    public final @ResponseBody
    List<Node> getTaxonTreeNode(@PathVariable final String identifier) {
        List<Taxon> results = taxonService.loadChildren(identifier, null, null, "classification-tree");
        List<Node> nodes = new ArrayList<Node>();
        for (Taxon result : results) {
            nodes.add(new Node(result));
        }
        return nodes;
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
         * @param taxon Set the taxon
         */
        public Node(final Taxon taxon) {
            data.put("title", taxon.getScientificName());
            Map<String, Object> dataAttr = new HashMap<String, Object>();
            dataAttr.put("href", "taxon/" + taxon.getIdentifier());
            Set<IdentificationKey> keys = taxon.getKeys();
            if (keys != null && keys.size() > 0) {
                dataAttr.put("class", "key");

            	String prepender = "key/";

            	StringBuilder keyInfo = new StringBuilder();
            	int keyCount = 0;
            	boolean first = true;
                for (IdentificationKey key : keys) {
                    if (!first) {
                        keyInfo.append(",");
                    }
                    keyInfo.append(key.getTitle()).append(":::");
                    keyInfo.append(prepender + key.getIdentifier());
                    first = false;
                    keyCount++;
                }
                dataAttr.put("data-key-link", keyInfo.toString());
            }
            data.put("attr", dataAttr);
            attr.put("id", taxon.getIdentifier());

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
    }
}
