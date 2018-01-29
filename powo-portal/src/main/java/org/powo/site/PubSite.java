package org.powo.site;

import org.powo.model.Taxon;
import org.powo.model.solr.QueryOption;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component("PubSite")
public class PubSite implements Site {

	@Override
	public String sitePageClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void populateTaxonModel(Taxon taxon, Model model) {
		// TODO Auto-generated method stub

	}

	@Override
	public void populateIndexModel(Model model) {
		// TODO Auto-generated method stub

	}

	@Override
	public QueryOption defaultQuery() {
		// TODO Auto-generated method stub
		return null;
	}

}
