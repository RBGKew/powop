package org.powo.portal.view;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.powo.model.Distribution;
import org.powo.model.Taxon;
import org.powo.model.constants.Location;
import org.powo.model.constants.TaxonomicStatus;
import org.junit.Test;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

public class SummaryTest {

	MessageSource messageSource = new ReloadableResourceBundleMessageSource();
	
	
	private Set<Distribution> oneDistribution = new HashSet<Distribution>(Arrays.asList(createDist(Location.ABT)));
	
	private Set<Distribution> manyDistributions(){
		Set<Distribution> distributions = new HashSet<Distribution>();

		distributions.addAll(Arrays.asList(
				createDist(Location.NORTHERN_EUROPE),
				createDist(Location.EASTERN_EUROPE),
				createDist(Location.SOUTHWESTERN_EUROPE),
				createDist(Location.SOUTHERN_AFRICA),
				createDist(Location.NORTHERN_AFRICA),
				createDist(Location.ABT)	
				));
		return distributions;
		
	}
	
	//private Set<Uses
	
	private Distribution createDist(Location location){
		Distribution dist = new Distribution();
		dist.setLocation(location);
		return dist;
	}
	
	@Test
	public void taxonAcceptedSummary(){
		Taxon tax = new Taxon();
		tax.setTaxonomicStatus(TaxonomicStatus.Accepted);
		assertEquals("This plant is accepted.", new Summary(tax, messageSource).build());
	}
	
	@Test
	public void taxonSynonoymSummary(){
		Taxon tax = new Taxon();
		tax.setTaxonomicStatus(TaxonomicStatus.Synonym);
		assertEquals("This is a synonym", new Summary(tax, messageSource).build());
	}
	
	@Test
	public void taxonArtificialHybridSummary(){
		Taxon tax = new Taxon();
		tax.setTaxonomicStatus(TaxonomicStatus.Artifical_Hybrid);
		assertEquals("This plant is an artifical hybrid.", new Summary(tax, messageSource).build());
	}
	
	@Test
	public void taxonAcceptedSummaryWithDistribution(){
		Taxon tax = new Taxon();
		tax.setTaxonomicStatus(TaxonomicStatus.Accepted);
		tax.setDistribution(oneDistribution);
		System.out.println(new Summary(tax, messageSource).build());
		tax.setDistribution(manyDistributions());
		System.out.println(new Summary(tax, messageSource).build());
	}
	
	@Test
	public void taxonAcceptedSummaryWithUses(){
		Taxon tax = new Taxon();
		tax.setTaxonomicStatus(TaxonomicStatus.Accepted);
		//tax.setDescriptions(newDescriptions);
		System.out.println(new Summary(tax, messageSource).build());
	}
	
	@Test
	public void taxonAcceptedSummaryWithGeographicArea(){
		Taxon tax = new Taxon();
		tax.setTaxonomicStatus(TaxonomicStatus.Accepted);
		//tax.setTaxonRemarks();
		System.out.println(new Summary(tax, messageSource).build());
	}
	
	
	@Test
	public void taxonAcceptedSummaryWithGeographicAreaDistributionAndUses(){
		Taxon tax = new Taxon();
		tax.setTaxonomicStatus(TaxonomicStatus.Accepted);
		System.out.println(new Summary(tax, messageSource).build());
	}
	
	@Test
	public void taxonWCSDistribution(){
		Taxon tax = new Taxon();
		tax.setTaxonomicStatus(TaxonomicStatus.Accepted);
		tax.setTaxonRemarks("Andaman Is., Thailand to W. Malesia.");
		assertEquals("This plant is accepted, and its native range is Andaman Islands, Thailand to W. Malesia." ,new Summary(tax, messageSource).build());
	}
	
	public void taxonWCSUncertainDistribution(){
		Taxon tax = new Taxon();
		tax.setTaxonomicStatus(TaxonomicStatus.Accepted);
		tax.setTaxonRemarks("Guatamala?) ? (?)");
		assertEquals("This plant is accepted, and its native range is likely to be Guatamala.", new Summary(tax, messageSource).build());
	}

}
