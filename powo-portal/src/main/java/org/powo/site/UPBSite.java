package org.powo.site;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.powo.model.Taxon;
import org.powo.model.solr.DefaultQueryOption;
import org.powo.persistence.solr.SourceFilter;
import org.powo.portal.view.components.Link;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component("UPBSite")
public class UPBSite extends PowoSite {

	private static final List<String> suggesters = Arrays.asList("scientific-name", "common-name");

	@Override
	public void populateTaxonModel(Taxon taxon, Model model) {
		super.populateTaxonModel(taxon, model);
		model.addAttribute("siteClass", "s-upb");
		model.addAttribute("kew-logo", "svg/kew-science-big-logo.svg");
		model.addAttribute("kew-logo-link", "http://www.kew.org/science-conservation");
		model.addAttribute("site-logo", "partials/logo/upb");
		model.addAttribute("site-logo-svg", "svg/upb.svg");
	}

	@Override
	public void populateIndexModel(Model model) {
		model.addAttribute("siteClass", "s-upb");
		model.addAttribute("intro", "partials/intro/upb");
		model.addAttribute("kew-logo", "svg/kew-science-big-logo.svg");
		model.addAttribute("kew-logo-link", "http://www.kew.org/science-conservation");
		model.addAttribute("site-logo", "partials/logo/upb");
		model.addAttribute("site-logo-svg", "svg/upb.svg");
	}

	@Override
	public void populateStaticModel(Model model) {
		model.addAttribute("siteClass", "s-upb");
		model.addAttribute("kew-logo", "svg/kew-science-big-logo.svg");
		model.addAttribute("kew-logo-link", "http://www.kew.org/science-conservation");
		model.addAttribute("site-logo-svg", "svg/upb.svg");
		model.addAttribute("site-logo", "partials/logo/upb");
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
	public String indexPageTitle() {
		return "Useful Plants of Boyacá";
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

	@Override
	public Optional<Link> crossSiteLink() {
		return Optional.empty();
	}
}
