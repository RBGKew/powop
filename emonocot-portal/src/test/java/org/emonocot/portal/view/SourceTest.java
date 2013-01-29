package org.emonocot.portal.view;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;

import java.util.HashSet;

import org.emonocot.model.BaseData;
import org.emonocot.model.Taxon;
import org.emonocot.model.Description;
import org.emonocot.model.registry.Organisation;
import org.emonocot.portal.view.provenance.ProvenanceManagerImpl;
import org.emonocot.portal.view.provenance.ProvenanceHolderImpl;
import org.emonocot.portal.view.provenance.ProvenanceManager;

import org.junit.Test;


public class SourceTest {
	
	private BaseData createData(BaseData data, String organisation, String license, String rights) {
		Organisation org = new Organisation();
		org.setIdentifier(organisation);
		org.setTitle(organisation);
		data.setAuthority(org);
		data.setLicense(license);
		data.setRights(rights);
		return data;
	}

	/**
	 * BUG #197 As a user of eMonocot, 
	 * I would like to know the source of each part of a taxon page.
	 */
	@Test
	public void testSource() {
		Organisation organisation = new Organisation();
		organisation.setIdentifier("test");
		organisation.setTitle("test");
		Organisation organisation2 = new Organisation();
		organisation2.setIdentifier("test2");
		organisation2.setTitle("test2");
		
		Description description1 = new Description();
		createData(description1,"test","this is my license","these are my rights");
		
		Description description2 = new Description();
		createData(description2,"test","this is my other license","these are my other rights");
		
		Description description3 = new Description();
		createData(description3,"test2",null,"these are my rights 2");
		
		Description description4 = new Description();
		createData(description4,"test","this is my license","these are my rights");
		Description description5 = new Description();
		createData(description5,"test2",null,null);
		Description description6 = new Description();
		createData(description6,"test2",null,null);
		Description description7 = new Description();
		createData(description7,"test2","",null);
		
		Taxon taxon = new Taxon();
		createData(taxon,"test","this is my license","these are my rights");
		taxon.getDescriptions().add(description1);
		description1.setTaxon(taxon);
		taxon.getDescriptions().add(description2);
		description2.setTaxon(taxon);
		taxon.getDescriptions().add(description3);
		description3.setTaxon(taxon);
		taxon.getDescriptions().add(description4);
		description4.setTaxon(taxon);
		taxon.getDescriptions().add(description5);
		description5.setTaxon(taxon);
		taxon.getDescriptions().add(description6);
		description6.setTaxon(taxon);
		taxon.getDescriptions().add(description7);
		description7.setTaxon(taxon);
		
		ProvenanceManager provenanceManager = new ProvenanceManagerImpl();
		
		provenanceManager.setProvenance(taxon);
		
		assertEquals("The number of provenance holder for organisation should be 2", provenanceManager.getProvenanceData(organisation).size(), 2 );
		assertEquals("The provenance key for taxon should be A",provenanceManager.getKey(taxon),"A");
		assertEquals("The provenance key for description1 should be A",provenanceManager.getKey(description1),"A");
		assertEquals("The provenance key for description2 should be B",provenanceManager.getKey(description2),"B");
		assertEquals("The provenance key for description3 should be D",provenanceManager.getKey(description3),"D");
		assertEquals("The number of sources for taxon page should be 2",provenanceManager.getSources().size(), 2);
		assertEquals("The number of provenance holder for organisation2 should be 2", provenanceManager.getProvenanceData(organisation2).size(), 2 );
		assertEquals("The provenance key for description1 and description4 should be the same",provenanceManager.getKey(description1),provenanceManager.getKey(description4));
		
	}
	
}
