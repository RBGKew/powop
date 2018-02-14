package org.powo.site;

import java.util.Arrays;
import java.util.List;

import org.powo.model.Taxon;
import org.powo.model.solr.DefaultQueryOption;
import org.powo.persistence.solr.PubDefaultQuery;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component("PubSite")
public class PubSite extends PowoSite implements Site {

	private List<String> suggesters = Arrays.asList("scientific-name", "common-name");

	@Override
	public String sitePageClass() {
		return "s-pub";
	}

	@Override
	public void populateTaxonModel(Taxon taxon, Model model) {
		super.populateTaxonModel(taxon, model);
	}

	@Override
	public void populateIndexModel(Model model) {
		model.addAttribute("siteClass", "s-pub");
		model.addAttribute("intro", "partials/intro/pub");
	}

	@Override
	public DefaultQueryOption defaultQuery() {
		return new PubDefaultQuery();
	}

	@Override
	public String suggesterFilter() {
		return "pub";
	}

	@Override
	public List<String> getSuggesters() {
		return suggesters;
	}
}
