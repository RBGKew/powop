package org.powo.site.powo;

import java.text.NumberFormat;
import java.util.Locale;

import org.apache.commons.lang3.text.WordUtils;
import org.powo.api.DescriptionService;
import org.powo.api.ImageService;
import org.powo.api.TaxonService;
import org.powo.model.Taxon;
import org.powo.model.solr.QueryOption;
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
import org.powo.site.Site;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component("PowoSite")
public class PowoSite implements Site {

	@Autowired
	MessageSource messageSource;

	@Autowired
	ImageService imageService;

	@Autowired
	TaxonService taxonService;

	@Autowired
	DescriptionService descriptionService;

	@Override
	public String sitePageClass() {
		return null;
	}

	@Override
	public void populateTaxonModel(Taxon taxon, Model model) {
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
	}

	@Override
	public void populateIndexModel(Model model) {
		model.addAttribute("names", format(taxonService.count(), 1000));
		model.addAttribute("images", format(imageService.count(), 100));
		model.addAttribute("descriptions", format(descriptionService.countAccounts(), 100));
	}

	@Override
	public QueryOption defaultQuery() {
		return null;
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

	private String format(long n, int ceilTo) {
		return NumberFormat.getNumberInstance(Locale.UK).format(((n + (ceilTo-1)) / ceilTo) * ceilTo);
	}
}
