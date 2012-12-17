package org.emonocot.portal.view;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;

import java.util.HashSet;

import org.emonocot.model.Taxon;
import org.emonocot.model.Description;
import org.emonocot.model.registry.Organisation;
import org.emonocot.portal.view.provenance.ProvenanceManagerImpl;
import org.emonocot.portal.view.provenance.ProvenanceHolderImpl;
import org.emonocot.portal.view.provenance.ProvenanceManager;

import org.junit.Test;


public class SourceTest {

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
		description1.setAuthority(organisation);
		description1.setRights("these are my rights");
		description1.setLicense("this is my license");
		Description description2 = new Description();
		description2.setAuthority(organisation);
		description2.setRights("these are my other rights");
		description2.setLicense("this is my other license");
		Description description3 = new Description();
		description3.setAuthority(organisation2);
		description3.setRights("these are my rights 2");
		description3.setLicense(null);
		Taxon taxon = new Taxon();
		taxon.setAuthority(organisation);
		taxon.setRights("these are my rights");
		taxon.setLicense("this is my license");
		taxon.getDescriptions().add(description1);
		description1.setTaxon(taxon);
		taxon.getDescriptions().add(description2);
		description2.setTaxon(taxon);
		taxon.getDescriptions().add(description3);
		description3.setTaxon(taxon);
		
		ProvenanceManager provenanceManager = new ProvenanceManagerImpl();
		
		provenanceManager.setProvenance(taxon);
		
		assertEquals("The provenance key for taxon should be A",provenanceManager.getKey(taxon),"A");
		assertEquals("The provenance key for description1 should be A",provenanceManager.getKey(description1),"A");
		assertEquals("The provenance key for description2 should be B",provenanceManager.getKey(description2),"B");
		assertEquals("The provenance key for description3 should be C",provenanceManager.getKey(description3),"C");
		assertEquals("The number of sources for taxon page should be 2",provenanceManager.getSources().size(), 2);
		
	}
	
}
