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

@Component("ColPlantASite")
public class ColPlantASite extends PowoSite {

	private static final List<String> suggesters = Arrays.asList("scientific-name", "common-name");

	@Override
	public void populateTaxonModel(Taxon taxon, Model model) {
		super.populateTaxonModel(taxon, model);
		model.addAttribute("siteClass", "s-colplanta");
		model.addAttribute("kew-logo", "svg/kew-colplanta-logo.svg");
		model.addAttribute("site-logo", "partials/logo/colplanta");
		model.addAttribute("site-logo-svg", "svg/colplanta.svg");
	}

	@Override
	public void populateIndexModel(Model model) {
		var taxonCounts = taxonCountsService.get(defaultQuery());
		model.addAttribute("siteClass", "s-colplanta");
		model.addAttribute("intro", "partials/intro/colplanta");
		model.addAttribute("taxon-counts-total", format(taxonCounts.getTotalCount(), 100));
		model.addAttribute("taxon-counts-species", format(taxonCounts.getSpeciesCount(), 1));
		model.addAttribute("kew-logo", "svg/kew-colplanta-logo.svg");
		model.addAttribute("site-logo", "partials/logo/colplanta");
		model.addAttribute("site-logo-svg", "svg/colplanta.svg");
	}

	@Override
	public void populateStaticModel(Model model) {
		model.addAttribute("siteClass", "s-colplanta");
		model.addAttribute("kew-logo", "svg/kew-colplanta-logo.svg");
		model.addAttribute("site-logo-svg", "svg/colplanta.svg");
		model.addAttribute("site-logo", "partials/logo/colplanta");
	}

	@Override
	public DefaultQueryOption defaultQuery() {
		return new SourceFilter("CatalogodePlantasyLiquenesdeColombia");
	}

	@Override
	public String suggesterFilter() {
		return "CatalogodePlantasyLiquenesdeColombia";
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
		Link link = new Link("http://colfungi.org", "Looking for Colombian fungi?");
		return Optional.of(link);
	}
}
