/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
package org.emonocot.portal.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.emonocot.api.TaxonService;
import org.emonocot.model.IdentificationKey;
import org.emonocot.model.Taxon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ClassificationController {

	private TaxonService taxonService;
	private static final int NUM_CHILDREN=70;

	@Autowired
	public final void setTaxonService(final TaxonService service) {
		this.taxonService = service;
	}

	@RequestMapping(value = "/classification" , method = RequestMethod.GET)
	public final String classification(final Model model){
		List<Taxon> results = taxonService.loadChildren(null, NUM_CHILDREN, 0, "classification-tree");
		model.addAttribute("result", results);
		return "classification";
	}
	@RequestMapping(value = "/taxonTree", method = RequestMethod.GET, produces = "application/json")
	public final @ResponseBody
	List<Node> getTaxonTreeRoots() {
		List<Taxon> results = taxonService.loadChildren(null, NUM_CHILDREN, 0, "classification-tree");
		List<Node> nodes = new ArrayList<Node>();
		for (Taxon result : results) {
			nodes.add(new Node(result));
		}
		return nodes;
	}

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

	class Node {
		private Map<String, Object> data = new HashMap<String, Object>();

		private String state = "closed";

		private Map<String, String> attr = new HashMap<String, String>();

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
					keyInfo.append(prepender + key.getId());
					first = false;
					keyCount++;
				}
				dataAttr.put("data-key-link", keyInfo.toString());
			}
			data.put("attr", dataAttr);
			attr.put("id", taxon.getIdentifier());
		}

		public final String getState() {
			return state;
		}

		public final void setState(final String newState) {
			this.state = newState;
		}

		public Node(final String name, final String identifier) {
			data.put("title", name);
			Map<String, String> dataAttr = new HashMap<String, String>();
			dataAttr.put("href", "taxon/" + identifier);
			data.put("attr", dataAttr);
			attr.put("id", identifier);
		}

		public final Map<String, Object> getData() {
			return data;
		}

		public final void setData(final Map<String, Object> newData) {
			this.data = newData;
		}

		public final Map<String, String> getAttr() {
			return attr;
		}

		public final void setAttr(final Map<String, String> newAttr) {
			this.attr = newAttr;
		}
	}
}
