package org.powo.model;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.gbif.ecat.voc.TaxonomicStatus;
import org.junit.Test;
import org.powo.model.Taxon;

public class TaxonTest {
	
	TaxonomicStatus[] synonyms = new TaxonomicStatus[]{ 
			TaxonomicStatus.Synonym,
			TaxonomicStatus.Heterotypic_Synonym,
			TaxonomicStatus.Homotypic_Synonym,
			TaxonomicStatus.IntermediateRankSynonym,
			TaxonomicStatus.Proparte_Synonym,
			TaxonomicStatus.DeterminationSynonym};
	
	TaxonomicStatus[] appearsAccepted = new TaxonomicStatus[]{
			TaxonomicStatus.Accepted,
			TaxonomicStatus.Doubtful
	};
	
	@Test
	public void testIsTaxonSynonym(){
		Taxon taxon = new Taxon();
		assertFalse("Taxon with blank taxonomic status should not be a synonym", taxon.isSynonym());
		List<TaxonomicStatus> synonymList = Arrays.asList(synonyms);
		for(TaxonomicStatus status : TaxonomicStatus.values()){
			taxon.setTaxonomicStatus(status);
			if(synonymList.contains(status)){
			assertTrue("Taxon with " + status.toString() + " status should be a synonym", taxon.isSynonym());
			}else{
				assertFalse("Taxon with " + status.toString() + " status should not be a synonym", taxon.isSynonym());
			}
		}
		
	}
	
	@Test
	public void testIsTaxonAccepted(){
		Taxon taxon = new Taxon();
		assertFalse("Taxon with blank taxonomic status should not be accepted", taxon.isAccepted());
		for(TaxonomicStatus status : TaxonomicStatus.values()){
			taxon.setTaxonomicStatus(status);
			if(status == TaxonomicStatus.Accepted){
			assertTrue("Taxon with " + status.toString() + " status should be accepted", taxon.isAccepted());
			}else{
				assertFalse("Taxon with " + status.toString() + " status should not be accepted", taxon.isAccepted());
			}
		}
		
	}
	
	@Test
	public void testDoesTaxonLookAccepted(){
		Taxon taxon = new Taxon();
		assertTrue("Taxon with blank taxonomic status should look accepted", taxon.looksAccepted());
		List<TaxonomicStatus> acceptedList = Arrays.asList(appearsAccepted);
		for(TaxonomicStatus status : TaxonomicStatus.values()){
			taxon.setTaxonomicStatus(status);
			if(acceptedList.contains(status)){
			assertTrue("Taxon with " + status.toString() + " status should appear accepted", taxon.looksAccepted());
			}else{
				assertFalse("Taxon with " + status.toString() + " status should not appear accepted", taxon.looksAccepted());
			}
		}
		
	}
}
