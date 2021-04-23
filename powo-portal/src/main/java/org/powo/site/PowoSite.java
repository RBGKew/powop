package org.powo.site;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.powo.api.DescriptionService;
import org.powo.api.ImageService;
import org.powo.model.Taxon;
import org.powo.model.solr.DefaultQueryOption;
import org.powo.persistence.solr.PowoDefaultQuery;
import org.powo.portal.service.TaxonCountsService;
import org.powo.portal.view.components.Link;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component("PowoSite")
public class PowoSite implements Site {

	@Autowired
	ImageService imageService;

	@Autowired
	DescriptionService descriptionService;

	@Autowired
	TaxonCountsService taxonCountsService;

	private static final List<String> suggesters = Arrays.asList("location", "characteristic", "scientific-name", "common-name");

	@Override
	public void addTaxonCountsToModel(Model model) {
		var taxonCounts = taxonCountsService.get(defaultQuery());
		model.addAttribute("names", format(taxonCounts.getTotalCount(), 1000));
		model.addAttribute("images", format(imageService.count(), 100));
		model.addAttribute("descriptions", format(descriptionService.countAccounts(), 100));
	}

	@Override
	public String siteId() {
		return "powo";
	}

	@Override
	public String kewLogoPath() {
		return "svg/kew-science-big-logo.svg";
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
