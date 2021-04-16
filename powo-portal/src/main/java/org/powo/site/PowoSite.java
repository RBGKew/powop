package org.powo.site;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.apache.commons.lang3.text.WordUtils;
import org.powo.api.DescriptionService;
import org.powo.api.ImageService;
import org.powo.api.SearchableObjectService;
import org.powo.api.TaxonService;
import org.powo.model.Taxon;
import org.powo.model.TaxonCounts;
import org.powo.model.solr.DefaultQueryOption;
import org.powo.persistence.solr.PowoDefaultQuery;
import org.powo.portal.service.TaxonCountsService;
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
import org.powo.portal.view.components.Link;
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
	TaxonCountsService taxonCountsService;

	@Autowired
	DescriptionService descriptionService;

	@Autowired
	SearchableObjectService searchableObjectService;

	private List<String> suggesters = Arrays.asList("location", "characteristic", "scientific-name", "common-name");

	@Override
	public void populateTaxonModel(Taxon taxon, Model model) {
		model.addAttribute(taxon);
		model.addAttribute(new Sources(taxon));
		Bibliography bibliography = new Bibliography(taxon);
		Descriptions descriptions = new Descriptions(taxon);
		Descriptions uses = new Descriptions(taxon, true);
		Images images = new Images(taxon, imageService);
		Identifications identifications = new Identifications(taxon);
		VernacularNames vernacularNames = new VernacularNames(taxon);
		if (!descriptions.getBySource().isEmpty()) {
			model.addAttribute("descriptions", descriptions);
		}
		if (!bibliography.isEmpty()) {
			model.addAttribute(bibliography);
		}
		if (!uses.getBySource().isEmpty()) {
			model.addAttribute("uses", uses);
		}
		if (!taxon.getSynonymNameUsages().isEmpty()) {
			model.addAttribute("synonyms", new ScientificNames(taxon.getSynonymNameUsages()));
		}
		if (!taxon.getChildNameUsages().isEmpty()) {
			model.addAttribute("children", new ScientificNames(taxon.getChildNameUsages()));
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
		if (!images.getAll().isEmpty()) {
			model.addAttribute(images);
		}

		model.addAttribute("color-theme", bodyClass(taxon));
		model.addAttribute("title", pageTitle(taxon));
		model.addAttribute("summary", new Summary(taxon, messageSource).build());
		model.addAttribute("kew-logo", "svg/kew-science-big-logo.svg");
		model.addAttribute("site-logo", "partials/logo/powo");
		model.addAttribute("site-logo-svg", "svg/powo-logo.svg");
	}

	@Override
	public void populateIndexModel(Model model) {
		var taxonCounts = taxonCountsService.get(defaultQuery());
		model.addAttribute("names", format(taxonCounts.getTotalCount(), 1000));
		model.addAttribute("images", format(imageService.count(), 100));
		model.addAttribute("descriptions", format(descriptionService.countAccounts(), 100));
		model.addAttribute("intro", "partials/intro/powo");
		model.addAttribute("kew-logo", "svg/kew-science-big-logo.svg");
		model.addAttribute("site-logo", "partials/logo/powo");
		model.addAttribute("site-logo-svg", "svg/powo-logo.svg");
	}

	@Override
	public void populateStaticModel(Model model) {
		model.addAttribute("kew-logo", "svg/kew-science-big-logo.svg");
		model.addAttribute("site-logo", "partials/logo/powo");
		model.addAttribute("site-logo-svg", "svg/powo-logo.svg");
		model.addAttribute("aboutTemplate", "partials/about/powo");
	}

	@Override
	public DefaultQueryOption defaultQuery() {
		return new PowoDefaultQuery();
	}

	@Override
	public String suggesterFilter() {
		return "Plantae";
	}

	@Override
	public List<String> getSuggesters() {
		return suggesters;
	}

	private String pageTitle(Taxon taxon) {
		return String.format("%s %s | Plants of the World Online | Kew Science", taxon.getScientificName(),
				taxon.getScientificNameAuthorship());
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

	protected String format(long n, int ceilTo) {
		return NumberFormat.getNumberInstance(Locale.UK).format(((n + (ceilTo - 1)) / ceilTo) * ceilTo);
	}

	@Override
	public Locale defaultLocale() {
		return new Locale("en", "uk", "powo");
	}

	@Override
	public String indexPageTitle() {
		return "Plants of the World Online | Kew Science";
	}

	@Override
	public String taxonPageTitle(Taxon taxon) {
		return String.format("%s %s | Plants of the World Online | Kew Science", taxon.getScientificName(),
				taxon.getScientificNameAuthorship());
	}

	@Override
	public String favicon() {
		return "powo-favicon.ico";
	}

	@Override
	public Optional<Link> crossSiteLink() {
		return Optional.empty();
	}

}
