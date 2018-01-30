package org.powo.site;

import org.powo.model.Taxon;
import org.powo.model.solr.QueryOption;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component("PubSite")
public class PubSite extends PowoSite implements Site {

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
	public QueryOption defaultQuery() {
		return null;
	}

}
