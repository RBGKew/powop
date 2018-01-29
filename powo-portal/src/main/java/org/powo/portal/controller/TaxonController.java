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
package org.powo.portal.controller;

import org.apache.commons.lang3.text.WordUtils;
import org.powo.api.ImageService;
import org.powo.api.TaxonService;
import org.powo.common.IdUtil;
import org.powo.model.Taxon;
import org.powo.portal.view.Bibliography;
import org.powo.portal.view.Descriptions;
import org.powo.portal.view.Distributions;
import org.powo.portal.view.Identifications;
import org.powo.portal.view.Images;
import org.powo.portal.view.MeasurementOrFacts;
import org.powo.portal.view.ScientificNames;
import org.powo.portal.view.Sources;
import org.powo.portal.view.Summary;
import org.powo.portal.view.VernacularNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/taxon")
public class TaxonController extends GenericController<Taxon, TaxonService> {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(TaxonController.class);

	public TaxonController() {
		super("taxon", Taxon.class);
	}
	
	@Autowired
	MessageSource messageSource;

	@Autowired
	ImageService imageService;

	@Autowired
	public void setTaxonService(TaxonService taxonService) {
		super.setService(taxonService);
	}

	@RequestMapping(path = {"/urn:lsid:ipni.org:names:{identifier}", "/{identifier}"}, method = RequestMethod.GET, produces = {"text/html", "*/*"})
	public String show(@PathVariable String identifier, Model model) {
		Taxon taxon = getService().load(IdUtil.fqName(identifier), "object-page");
		model.addAttribute(taxon);
		model.addAttribute(new Sources(taxon));
		Bibliography bibliography = new Bibliography(taxon);
		Descriptions descriptions = new Descriptions(taxon);
		Descriptions uses = new Descriptions(taxon, true);
		Images images = new Images(taxon, imageService);
		if(!descriptions.getBySource().isEmpty()) {
			model.addAttribute("descriptions", descriptions);
		}
		if(!bibliography.isEmpty()) {
			model.addAttribute(bibliography);
		}
		if(!uses.getBySource().isEmpty()) {
			model.addAttribute("uses", uses);
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
		if(!taxon.getVernacularNames().isEmpty()) {
			model.addAttribute(new VernacularNames(taxon));
		}
		if(!taxon.getIdentifications().isEmpty()) {
			model.addAttribute(new Identifications(taxon));
		}
		if(!images.getAll().isEmpty()) {
			model.addAttribute(images);
		}

		model.addAttribute("color-theme", bodyClass(taxon));
		model.addAttribute("title", pageTitle(taxon));
		model.addAttribute("summary", new Summary(taxon, messageSource).build());
		return "taxon";
	}

	/**
	 * Many users visit a taxon page and then navigate to the document above, then bounce
	 */
	@RequestMapping(method = RequestMethod.GET, produces = {"text/html", "*/*"})
	public String list(Model model) {
		return "redirect:/search";
	}

	private String pageTitle(Taxon taxon) {
		return String.format("%s %s | Plants of the World Online | Kew Science", taxon.getScientificName(), taxon.getScientificNameAuthorship());
	}

	private String bodyClass(Taxon taxon) {
		if(taxon.looksAccepted()) {
			if(taxon.getTaxonRank() == null || taxon.getTaxonRank().isInfraspecific()) {
				return "s-theme-Infraspecific";
			} else {
				return String.format("s-theme-%s", WordUtils.capitalizeFully(taxon.getTaxonRank().toString()));
			}
		} else {
			return "s-theme-Synonym";
		}
	}
}
