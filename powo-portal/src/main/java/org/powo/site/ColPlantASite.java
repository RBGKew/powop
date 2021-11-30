package org.powo.site;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.ImmutableMap;

import org.powo.model.Taxon;
import org.powo.model.solr.DefaultQueryOption;
import org.powo.persistence.solr.ColombianSiteQuery;
import org.powo.persistence.solr.ColombianSiteSuggesterFilter;
import org.powo.portal.view.FeaturedTaxaSection;
import org.powo.portal.view.FeaturedTaxon;
import org.powo.portal.view.components.Link;
import org.springframework.stereotype.Component;

@Component("ColPlantASite")
public class ColPlantASite extends PowoSite {

	private static final List<String> suggesters = Arrays.asList("scientific-name", "common-name");

	private String organisationIdentifier = "CatalogodePlantasyLiquenesdeColombia";

	@Override
	public Map<String, String> getFormattedTaxonCounts() {
		var taxonCounts = taxonCountsService.get(defaultQuery());
		return new ImmutableMap.Builder<String, String>()
				.put("taxon-counts-total", format(taxonCounts.getTotalCount(), 1000))
				.put("taxon-counts-species", format(taxonCounts.getSpeciesCount(), 100)).build();
	}

	@Override
	public String siteId() {
		return "colplanta";
	}

	@Override
	public String oneTrustID() {
		return "d96f3461-475d-4f71-8bd6-d80deb50e359";
	}

	@Override
	public String siteIdCapitlized() {
		return "ColPlantA";
	}

	@Override
	public String kewLogoPath() {
		return "svg/kew-colplanta-logo.svg";
	}

	@Override
	public DefaultQueryOption defaultQuery() {
		return new ColombianSiteQuery(
				organisationIdentifier, "Plantae", "Colombia");
	}

	@Override
	public String suggesterFilter() {
		return new ColombianSiteSuggesterFilter(organisationIdentifier, "Fungi", "Colombia").toString();
	}

	@Override
	public List<String> getSuggesters() {
		return suggesters;
	}

	@Override
	public Locale defaultLocale() {
		return new Locale("en", "uk", "colplanta");
	}

	@Override
	public String indexPageTitle() {
		return "Colombian Plants made Accessible";
	}

	@Override
	public String taxonPageTitle(Taxon taxon) {
		return String.format("%s %s | Colombian Plants made Accessible", taxon.getScientificName(),
				taxon.getScientificNameAuthorship());
	}

	@Override
	public String favicon() {
		return "upfc-favicon.ico";
	}

	@Override
	public Optional<Link> crossSiteLink() {
		Link link = new Link("http://colfungi.org", "Visit ColFungi");
		return Optional.of(link);
	}

	@Override
	public String crossSiteType() {
		return "fungi";
	}

	@Override
	public List<FeaturedTaxaSection> featuredTaxaSections() {
		var cochlospermumOrinocense = new FeaturedTaxon(taxonService.find("urn:lsid:ipni.org:names:111532-1"),
				messageSource);
		var passifloraEdulis = new FeaturedTaxon(taxonService.find("urn:lsid:ipni.org:names:321964-2"), messageSource);
		var epidendrumRadicans = new FeaturedTaxon(taxonService.find("urn:lsid:ipni.org:names:632612-1"),
				messageSource);

		return List.of(new FeaturedTaxaSection("Featured plants",
				List.of(cochlospermumOrinocense, passifloraEdulis, epidendrumRadicans)));
	}

	@Override
	public boolean hasTaxon(Taxon taxon) {
		// Return true if the ColPlantA organisation is in the taxon authorities
		var organisationMatch = taxon.getAcceptedNameAuthorities().stream()
				.anyMatch(org -> org.getIdentifier().equals(organisationIdentifier));
		if (organisationMatch) {
			return true;
		}
		// Return false if the taxon is not a plant
		if (!(taxon.getKingdom().toLowerCase().equals("plantae"))) {
			return false;
		}
		// Return true if the taxon is a plant and is found in Colombia
		return taxon.getLocations().stream().anyMatch(l -> l.getName().equals("Colombia"));
	}
}
