package org.powo.site;

import java.util.Arrays;
import java.util.List;

import org.powo.model.Taxon;
import org.powo.model.solr.DefaultQueryOption;
import org.powo.persistence.solr.ColPlantADefaultQuery;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component("ColPlantASite")
public class ColPlantASite extends PowoSite implements Site {

	private List<String> suggesters = Arrays.asList("scientific-name", "common-name");

	@Override
	public void populateTaxonModel(Taxon taxon, Model model) {
		super.populateTaxonModel(taxon, model);
		model.addAttribute("site-logo", "partials/logo/colplanta");
		model.addAttribute("site-logo-svg", "svg/colplanta.svg");
	}

	@Override
	public void populateIndexModel(Model model) {
		model.addAttribute("siteClass", "s-colplanta");
		model.addAttribute("intro", "partials/intro/colplanta");
		model.addAttribute("site-logo", "partials/logo/colplanta");
		model.addAttribute("site-logo-svg", "svg/colplanta.svg");
	}

	@Override
	public void populateStaticModel(Model model) {
		model.addAttribute("site-logo-svg", "svg/colplanta.svg");
		model.addAttribute("site-logo", "partials/logo/colplanta");
		model.addAttribute("siteClass", "s-colplanta");
	}

	@Override
	public DefaultQueryOption defaultQuery() {
		return new ColPlantADefaultQuery();
	}

	@Override
	public String suggesterFilter() {
		return "ColPlantA";
	}

	@Override
	public List<String> getSuggesters() {
		return suggesters;
	}
}