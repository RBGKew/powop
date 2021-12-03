package org.powo.site;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.powo.model.Taxon;
import org.powo.model.solr.DefaultQueryOption;
import org.powo.persistence.solr.SourceFilter;
import org.springframework.stereotype.Component;

@Component("UPBSite")
public class UPBSite extends PowoSite {

	private static final List<String> suggesters = Arrays.asList("scientific-name", "common-name");

	private String organisationIdentifier = "UsefulPlantsofBoyacaProject";

	@Override
	public Map<String, String> getFormattedTaxonCounts() {
		return new HashMap<>();
	}

	@Override
	public String siteId() {
		return "upb";
	}

	@Override
	public String oneTrustID() {
		return "upb";
	}

	@Override
	public String siteIdCapitlized() {
		return "UPB";
	}
	
	@Override
	public String kewLogoPath() {
		return "svg/kew-upb-logo.svg";
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
	public String taxonPageTitle(Taxon taxon) {
		return String.format("%s %s | Useful Plants of Boyacá", taxon.getScientificName(),
				taxon.getScientificNameAuthorship());
	}

	@Override
	public String favicon() {
		return "upfc-favicon.ico";
	}

	@Override
	public boolean hasTaxon(Taxon taxon) {
		return taxon.getAcceptedNameAuthorities().stream().anyMatch(org -> org.getIdentifier().equals(organisationIdentifier));
	}

	@Override
	public Locale defaultLocale() {
		return new Locale("en", "uk", "upb");
	}
}
