package org.powo.site;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.powo.model.Taxon;
import org.powo.model.solr.DefaultQueryOption;
import org.powo.persistence.solr.SourceFilter;
import org.powo.portal.view.components.Link;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component("ColFungiSite")
public class ColFungiSite extends PowoSite {

	private static final List<String> suggesters = Arrays.asList("scientific-name", "common-name");

	@Override
	public void populateTaxonModel(Taxon taxon, Model model) {
		super.populateTaxonModel(taxon, model);
		model.addAttribute("siteClass", "s-colfungi");
		model.addAttribute("kew-logo", "svg/kew-colfungi-logo.svg");
		model.addAttribute("site-logo", "partials/logo/colfungi");
		model.addAttribute("site-logo-svg", "svg/colfungi.svg");
	}

	@Override
	public void populateIndexModel(Model model) {
		model.addAttribute("siteClass", "s-colfungi");
		model.addAttribute("intro", "partials/intro/colfungi");
		model.addAttribute("names", format(taxaCount(), 100));
		model.addAttribute("kew-logo", "svg/kew-colfungi-logo.svg");
		model.addAttribute("site-logo", "partials/logo/colfungi");
		model.addAttribute("site-logo-svg", "svg/colfungi.svg");
	}

	@Override
	public void populateStaticModel(Model model) {
		model.addAttribute("siteClass", "s-colfungi");
		model.addAttribute("kew-logo", "svg/kew-colfungi-logo.svg");
		model.addAttribute("site-logo-svg", "svg/colfungi.svg");
		model.addAttribute("site-logo", "partials/logo/colfungi");
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
}
