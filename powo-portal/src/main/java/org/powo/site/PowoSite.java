package org.powo.site;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.ImmutableMap;

import org.powo.api.DescriptionService;
import org.powo.api.ImageService;
import org.powo.api.TaxonService;
import org.powo.model.Taxon;
import org.powo.model.registry.Organisation;
import org.powo.model.solr.DefaultQueryOption;
import org.powo.persistence.solr.PowoDefaultQuery;
import org.powo.portal.service.TaxonCountsService;
import org.powo.portal.view.FeaturedTaxaSection;
import org.powo.portal.view.FeaturedTaxon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component("PowoSite")
public class PowoSite implements Site {

	@Autowired
	ImageService imageService;

	@Autowired
	DescriptionService descriptionService;

	@Autowired
	TaxonCountsService taxonCountsService;

	@Autowired
	TaxonService taxonService;

	@Autowired
	MessageSource messageSource;

	private static final List<String> suggesters = Arrays.asList("location", "characteristic", "scientific-name", "common-name");

	@Override
	public Map<String, String> getFormattedTaxonCounts() {
		var taxonCounts = taxonCountsService.get(defaultQuery());
		return new ImmutableMap.Builder<String, String>()
			.put("names", format(taxonCounts.getTotalCount(), 1000))
			.put("images", format(imageService.count(), 100))
			.put("descriptions", format(descriptionService.countAccounts(), 100))
			.build();
	}

	@Override
	public String siteId() {
		return "powo";
	}

	@Override
	public String oneTrustID() {
		return "powo";
	}

	@Override
	public String siteIdCapitlized() {
		return "POWO";
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
	public String taxonPageTitle(Taxon taxon) {
		return String.format("%s %s | Plants of the World Online | Kew Science", taxon.getScientificName(),
				taxon.getScientificNameAuthorship());
	}

	@Override
	public String favicon() {
		return "powo-favicon.ico";
	}

	@Override
	public String canonicalUrl() {
		return "http://powo.science.kew.org";
	}
	
	public List<FeaturedTaxaSection> featuredTaxaSections() {
		// TODO: move this into application.properties when we have Spring profiles per site
		var passifloraLindeniana = new FeaturedTaxon(taxonService.find("urn:lsid:ipni.org:names:164286-1"), messageSource);
		var delonixRegia = new FeaturedTaxon(taxonService.find("urn:lsid:ipni.org:names:491231-1"), messageSource);
		var digitalisPurpurea = new FeaturedTaxon(taxonService.find("urn:lsid:ipni.org:names:802077-1"), messageSource);

		return List.of(new FeaturedTaxaSection("Featured plants", List.of(passifloraLindeniana, delonixRegia, digitalisPurpurea)));
	}

	@Override
	public Organisation primarySource() {
		return null;
	}

	@Override
	public boolean hasTaxon(Taxon taxon) {
		return taxon.getKingdom().toLowerCase().equals("plantae");
	}

}
