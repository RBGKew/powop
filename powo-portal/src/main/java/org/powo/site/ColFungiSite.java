package org.powo.site;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.ImmutableMap;

import org.powo.api.OrganisationService;
import org.powo.model.Taxon;
import org.powo.model.constants.DescriptionType;
import org.powo.model.registry.Organisation;
import org.powo.model.solr.DefaultQueryOption;
import org.powo.persistence.solr.SourceFilter;
import org.powo.portal.view.FeaturedTaxaSection;
import org.powo.portal.view.FeaturedTaxon;
import org.powo.portal.view.components.Link;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("ColFungiSite")
public class ColFungiSite extends PowoSite {

	private static final List<String> suggesters = Arrays.asList("scientific-name", "common-name");

	private String organisationIdentifier = "CatalogodeHongosUtilesdeColombia";

	@Autowired
	public OrganisationService organisationService;

	@Override
	public Map<String, String> getFormattedTaxonCounts() {
		var taxonCounts = taxonCountsService.get(defaultQuery());
		return new ImmutableMap.Builder<String, String>()
				.put("taxon-counts-total", format(taxonCounts.getTotalCount(), 100))
				.put("taxon-counts-species", format(taxonCounts.getSpeciesCount(), 1)).build();
	}

	@Override
	public String siteId() {
		return "colfungi";
	}

	@Override
	public String oneTrustID() {
		return "04b96383-6665-429f-8fb0-fc62e2c4fbe2";
	}

	@Override
	public String siteIdCapitlized() {
		return "ColFungi";
	}

	@Override
	public String kewLogoPath() {
		return "svg/kew-colfungi-logo.svg";
	}

	@Override
	public DefaultQueryOption defaultQuery() {
		return new SourceFilter(organisationIdentifier);
	}

	@Override
	public String suggesterFilter() {
		return organisationIdentifier;
	}

	@Override
	public List<String> getSuggesters() {
		return suggesters;
	}

	@Override
	public Locale defaultLocale() {
		return new Locale("en", "uk", "colfungi");
	}

	@Override
	public String indexPageTitle() {
		return "Colombian Fungi made accessible";
	}

	@Override
	public String taxonPageTitle(Taxon taxon) {
		return String.format("%s %s | Colombian Fungi made accessible", taxon.getScientificName(),
				taxon.getScientificNameAuthorship());
	}

	@Override
	public String favicon() {
		return "upfc-favicon.ico";
	}

	@Override
	public Optional<Link> crossSiteLink() {
		Link link = new Link("http://colplanta.org", "Visit ColPlantA");
		return Optional.of(link);
	}

	@Override
	public String crossSiteType() {
		return "plant";
	}

	@Override
	public String canonicalUrl() {
		return "http://colfungi.org";
	}

	public List<FeaturedTaxaSection> featuredTaxaSections() {
		var lobariellaPallida = new FeaturedTaxon(taxonService.find("urn:lsid:indexfungorum.org:names:548106"), messageSource);
		var auriculariaFuscosuccinea = new FeaturedTaxon(taxonService.find("urn:lsid:indexfungorum.org:names:309392"), messageSource);
		var macrolepiotaColombiana = new FeaturedTaxon(taxonService.find("urn:lsid:indexfungorum.org:names:318604"), messageSource);

		return List.of(new FeaturedTaxaSection("Featured fungi",
				List.of(lobariellaPallida, auriculariaFuscosuccinea, macrolepiotaColombiana)));
	}

	@Override
	public Organisation primarySource() {
		return organisationService.find(organisationIdentifier);
	}
}
