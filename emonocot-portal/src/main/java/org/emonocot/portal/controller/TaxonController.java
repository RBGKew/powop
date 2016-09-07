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

import org.apache.commons.lang.WordUtils;
import org.emonocot.api.TaxonService;
import org.emonocot.model.Taxon;
import org.emonocot.portal.view.Bibliography;
import org.emonocot.portal.view.Descriptions;
import org.emonocot.portal.view.Distributions;
import org.emonocot.portal.view.Images;
import org.emonocot.portal.view.MeasurementOrFacts;
import org.emonocot.portal.view.Sources;
import org.emonocot.portal.view.VernacularNames;
import org.gbif.ecat.voc.TaxonomicStatus;
import org.emonocot.portal.view.ScientificNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/taxon")
public class TaxonController extends GenericController<Taxon, TaxonService> {

	private static Logger logger = LoggerFactory.getLogger(TaxonController.class);

	public TaxonController() {
		super("taxon", Taxon.class);
	}

	@Autowired
	public void setTaxonService(TaxonService taxonService) {
		super.setService(taxonService);
	}

	@RequestMapping(value = "/{identifier}", method = RequestMethod.GET, produces = {"text/html", "*/*"})
	public String show(@PathVariable String identifier, Model model) {
		Taxon taxon = getService().load(identifier, "object-page");
		model.addAttribute(taxon);
		model.addAttribute(new Sources(taxon));
		model.addAttribute(new Bibliography(taxon));
		if(!taxon.getDescriptions().isEmpty()) {
			model.addAttribute(new Descriptions(taxon));
		}
		if(!taxon.getSynonymNameUsages().isEmpty()) {
			model.addAttribute("synonyms", new ScientificNames(taxon.getSynonymNameUsages()));
		}
		if(!taxon.getChildNameUsages().isEmpty()) {
			model.addAttribute("children", new ScientificNames(taxon.getChildNameUsages()));
		}
		if(!taxon.getMeasurementsOrFacts().isEmpty()) {
			model.addAttribute(new MeasurementOrFacts(taxon));
		}
		if(!taxon.getDistribution().isEmpty()) {
			model.addAttribute(new Distributions(taxon));
		}
		if(!taxon.getImages().isEmpty()) {
			model.addAttribute(new Images(taxon));
		}
		if(!taxon.getVernacularNames().isEmpty()) {
			model.addAttribute(new VernacularNames(taxon));
		}
		model.addAttribute("theme", bodyClass(taxon));

		return "taxon";
	}

	/**
	 * Many users visit a taxon page and then navigate to the document above, then bounce
	 */
	@RequestMapping(method = RequestMethod.GET, produces = {"text/html", "*/*"})
	public String list(Model model) {
		return "redirect:/search?facet=base.class_s%3aorg.emonocot.model.Taxon";
	}

	private String bodyClass(Taxon taxon) {
		if(taxon.getTaxonomicStatus() != null && taxon.getTaxonomicStatus().equals(TaxonomicStatus.Synonym)) {
			return "s-theme-Synonym";
		} else {
			return String.format("s-theme-%s", WordUtils.capitalizeFully(taxon.getTaxonRank().toString()));
		}
	}
}
