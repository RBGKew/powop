package org.powo.site;

import java.util.Arrays;
import java.util.List;

import org.powo.model.Taxon;
import org.powo.model.solr.DefaultQueryOption;
import org.powo.persistence.solr.SourceFilter;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component("UPBSite")
public class UPBSite extends PowoSite {

	private static final List<String> suggesters = Arrays.asList("scientific-name", "common-name");

	@Override
	public void populateTaxonModel(Taxon taxon, Model model) {
		super.populateTaxonModel(taxon, model);
		model.addAttribute("site-logo", "partials/logo/upb");
		model.addAttribute("site-logo-svg", "svg/upb.svg");
	}

	@Override
	public void populateIndexModel(Model model) {
		model.addAttribute("siteClass", "s-upb");
		model.addAttribute("intro", "partials/intro/upb");
		model.addAttribute("site-logo", "partials/logo/upb");
		model.addAttribute("site-logo-svg", "svg/upb.svg");
	}

	@Override
	public void populateStaticModel(Model model) {
		model.addAttribute("site-logo-svg", "svg/upb.svg");
		model.addAttribute("site-logo", "partials/logo/upb");
		model.addAttribute("siteClass", "s-upb");
	}

	@Override
	public String indexPageTitle() {
		return "Useful Plants of Boyacá";
	}

	@Override
	public DefaultQueryOption defaultQuery() {
		return new SourceFilter("UsefulPlantsofBoyacaProject");
	}

	@Override
	public String suggesterFilter() {
		return "UsefulPlantsofBoyacaProject";
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
		return null;
	}
}
