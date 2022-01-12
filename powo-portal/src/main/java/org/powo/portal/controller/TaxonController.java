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

import java.util.Map;

import org.apache.commons.text.WordUtils;
import org.powo.api.ImageService;
import org.powo.common.IdUtil;
import org.powo.model.Taxon;
import org.powo.portal.service.SiteTaxonService;
import org.powo.portal.service.TaxonImageService;
import org.powo.portal.view.Bibliography;
import org.powo.portal.view.Descriptions;
import org.powo.portal.view.Distributions;
import org.powo.portal.view.Identifications;
import org.powo.portal.view.MeasurementOrFacts;
import org.powo.portal.view.ScientificNames;
import org.powo.portal.view.Sources;
import org.powo.portal.view.Summary;
import org.powo.portal.view.VernacularNames;
import org.powo.site.Site;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/taxon")
public class TaxonController extends LayoutController {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(TaxonController.class);

	@Autowired
	@Qualifier("currentSite")
	Site site;

	@Autowired
	SiteTaxonService service;

	@Autowired
	ImageService imageService;

	@Autowired
	TaxonImageService taxonImageService;

	@Autowired
	MessageSource messageSource;

	@Value("#{${site.redirectkeys}}")
	Map<String, String> siteRedirectKeys;

	@GetMapping(path = { "/{identifier}" })
	public String show(@PathVariable String identifier,
			@RequestParam(required = false, value = "site") String siteRedirectKey, Model model) {
		if (siteRedirectKey != null) {
			var target = siteRedirectKeys.get(siteRedirectKey);
			if (target != null) {
				// Clear the model, as otherwise the attributes are added to the HTTP query parameters
				model.asMap().clear();
				return "redirect:" + target + "/taxon/" + identifier;
			}
		}
		var taxon = service.load(IdUtil.fqName(identifier));

		model.addAttribute(taxon);
		model.addAttribute(
			"title", 
			messageSource.getMessage(
				"site.taxon.title", 
				new Object[] { taxon.getScientificName(), taxon.getScientificNameAuthorship() }, 
				site.defaultLocale()
			)
		);
		model.addAttribute("color-theme", bodyClass(taxon));
		model.addAttribute("summary", new Summary(taxon, messageSource).build());
		model.addAttribute(new Sources(taxon));

		var bibliography = new Bibliography(taxon);
		var descriptions = new Descriptions(taxon, site.primarySource());
		var uses = new Descriptions(taxon, site.primarySource(), true);
		var imageSet = taxonImageService.getTaxonImageSet(taxon);
		var identifications = new Identifications(taxon);
		var vernacularNames = new VernacularNames(taxon);
		if (!descriptions.getBySource().isEmpty()) {
			model.addAttribute("descriptions", descriptions);
		}
		if (!bibliography.isEmpty()) {
			model.addAttribute(bibliography);
		}
		if (!uses.getBySource().isEmpty()) {
			model.addAttribute("uses", uses);
		}
		var synonymNameUsages = service.getSynonymNameUsages(taxon);
		if (!synonymNameUsages.isEmpty()) {
			model.addAttribute("synonyms", new ScientificNames(synonymNameUsages));
		}
		var childNameUsages = service.getChildNameUsages(taxon);
		if (!childNameUsages.isEmpty()) {
			model.addAttribute("children", new ScientificNames(childNameUsages));
		}
		if (!taxon.getMeasurementsOrFacts().isEmpty()) {
			model.addAttribute(new MeasurementOrFacts(taxon));
		}
		if (!taxon.getDistribution().isEmpty()) {
			model.addAttribute(new Distributions(taxon));
		}
		if (!vernacularNames.getNames().isEmpty()) {
			model.addAttribute(vernacularNames);
		}
		if (!identifications.getIdentifications().isEmpty()) {
			model.addAttribute(identifications);
		}
		if (!imageSet.getImages().isEmpty()) {
			model.addAttribute("imageSet", imageSet);
		}

		return "taxon";
	}

	private String bodyClass(Taxon taxon) {
		if (taxon.looksAccepted()) {
			if (taxon.getTaxonRank() == null || taxon.getTaxonRank().isInfraspecific()) {
				return "s-theme-Infraspecific";
			} else {
				return String.format("s-theme-%s", WordUtils.capitalizeFully(taxon.getTaxonRank().toString()));
			}
		} else {
			return "s-theme-Synonym";
		}
	}
}
