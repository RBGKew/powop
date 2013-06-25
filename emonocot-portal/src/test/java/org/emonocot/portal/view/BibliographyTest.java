package org.emonocot.portal.view;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.emonocot.model.Reference;
import org.emonocot.model.Taxon;
import org.emonocot.model.Description;
import org.emonocot.model.constants.DescriptionType;
import org.emonocot.portal.view.bibliography.Bibliography;
import org.emonocot.portal.view.bibliography.SimpleBibliographyImpl;
import org.junit.Before;
import org.junit.Test;

public class BibliographyTest {
	
	private Bibliography bibliography;
	
	private Reference reference1;
	
	private Reference reference2;
	
	private Reference reference3;
	
	private Reference duplicateReference4;
	
	private Reference duplicateReference5;
	
	@Before
	public void setUp() {
		Taxon taxon = new Taxon();
		taxon.setDescriptions(new HashSet<Description>());
		reference1 = createReference("1753", null, null, null, "1");
		taxon.getReferences().add(reference1);
		
		reference2 = createReference(null, "Zebras are wonderful creatures", null, null, "2");
		taxon.getReferences().add(reference2);
		
		reference3 = createReference(null, "A", null, null, "3");
		taxon.getReferences().add(reference3);
		
		duplicateReference4 = createReference("2005","The flowering plants of Mt. Popa, central Myanmar - Results of Myanmar-Japanese joint expeditions, 2000-2004","Tanaka, N., Koyama, T. & Murata, J. (2005). The flowering plants of Mt. Popa, central Myanmar - Results of Myanmar-Japanese joint expeditions, 2000-2004. Makinoa 5: 1-102.","Tanaka, N., Koyama, T. & Murata, J.","4");
		duplicateReference5 = createReference("2005","The flowering plants of Mt. Popa, central Myanmar - Results of Myanmar-Japanese joint expeditions, 2000-2004","Tanaka, N., Koyama, T. & Murata, J. (2005). The flowering plants of Mt. Popa, central Myanmar - Results of Myanmar-Japanese joint expeditions, 2000-2004. Makinoa 5: 1-102.","Tanaka, N., Koyama, T. & Murata, J.","5");
		taxon.getReferences().add(duplicateReference4);
		taxon.getReferences().add(duplicateReference5);
		
		bibliography = new SimpleBibliographyImpl();
		bibliography.setReferences(taxon);
		
	}

	private Reference createReference(String datePublished, String title, String bibliographicCitation, String creator, String identifier) {
		Reference reference = new Reference();
		reference.setDate(datePublished);
		reference.setTitle(title);
		reference.setIdentifier(identifier);
		return reference;
	}

	/**
	 * BUG #311 As a User of emonocot I expect to understand how the numbers
	 * next to the description text matches to the references
	 */
	@Test
	public void testBibliography() {
		assertNotNull("Bibliography should not be null", bibliography);
		assertEquals("Bibliography should contain four items", bibliography.getReferences().size(),4);
		assertEquals("References with dates should come first", bibliography.getKey(reference1),"1");
		assertEquals("References without dates should be sorted by author, then title", bibliography.getKey(reference2),"4");
		assertEquals("Duplicate references should return the same key", bibliography.getKey(duplicateReference4),"2");
		assertEquals("Duplicate references should return the same key", bibliography.getKey(duplicateReference5),"2");
	}

}
