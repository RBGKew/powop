package org.powo.site;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.ImmutableMap;

import org.powo.model.Taxon;
import org.powo.model.solr.DefaultQueryOption;
import org.powo.persistence.solr.SourceFilter;
import org.powo.portal.view.components.Link;
import org.springframework.stereotype.Component;

@Component("ColFungiSite")
public class ColFungiSite extends PowoSite {

	private static final List<String> suggesters = Arrays.asList("scientific-name", "common-name");

	@Override
	public Map<String, String> getFormattedTaxonCounts() {
		var taxonCounts = taxonCountsService.get(defaultQuery());
		return new ImmutableMap.Builder<String, String>()
			.put("taxon-counts-total", format(taxonCounts.getTotalCount(), 100))
			.put("taxon-counts-species", format(taxonCounts.getSpeciesCount(), 1))
			.put("taxon-counts-genus", format(taxonCounts.getGenusCount(), 1))
			.put("taxon-counts-family", format(taxonCounts.getFamilyCount(), 1))
			.build();
	}

	@Override
	public String siteId() {
		return "colfungi";
	}

	@Override
	public String kewLogoPath() {
		return "svg/kew-colfungi-logo.svg";
	}

	@Override
	public DefaultQueryOption defaultQuery() {
		return new SourceFilter("CatalogodeHongosUtilesdeColombia");
	}

	@Override
	public String suggesterFilter() {
		return "CatalogodeHongosUtilesdeColombia";
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
		Link link = new Link("http://colplanta.org", "Looking for a Colombian plant?");
		return Optional.of(link);
	}

	@Override
	public String canonicalUrl() {
		return "http://colfungi.org";
	}
}
