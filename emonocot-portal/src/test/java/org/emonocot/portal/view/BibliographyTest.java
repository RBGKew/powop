package org.emonocot.portal.view;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.emonocot.model.description.Feature;
import org.emonocot.model.description.TextContent;
import org.emonocot.model.reference.Reference;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.portal.view.bibliography.Bibliography;
import org.emonocot.portal.view.bibliography.SimpleBibliographyImpl;
import org.junit.Before;
import org.junit.Test;

public class BibliographyTest {
	
	private Bibliography bibliography;
	
	private Reference reference1;
	
	private Reference reference2;
	
	private Reference reference3;
	
	@Before
	public void setUp() {
		Taxon taxon = new Taxon();
		taxon.setContent(new HashMap<Feature,TextContent>());
		reference1 = createReference("1753", null);
		taxon.getReferences().add(reference1);
		
		reference2 = createReference(null, "Zebras are wonderful creatures");
		taxon.getReferences().add(reference2);
		
		reference3 = createReference(null, "A");
		taxon.getReferences().add(reference3);
		
		bibliography = new SimpleBibliographyImpl();
		bibliography.setReferences(taxon);
		
	}

	private Reference createReference(String datePublished, String title) {
		Reference reference = new Reference();
		reference.setDatePublished(datePublished);
		reference.setTitle(title);
		return reference;
	}

	/**
	 * BUG #311 As a User of emonocot I expect to understand how the numbers
	 * next to the description text matches to the references
	 */
	@Test
	public void testBibliography() {
		assertNotNull("Bibliography should not be null", bibliography);
		assertEquals("Bibliography should contain three items", bibliography.getReferences().size(),3);
		assertEquals("References with dates should come first", bibliography.getKey(reference1),"1");
		assertEquals("References without dates should be sorted by author, then title", bibliography.getKey(reference2),"3");
	}

}
