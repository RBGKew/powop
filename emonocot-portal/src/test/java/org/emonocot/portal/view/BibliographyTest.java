/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
package org.emonocot.portal.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashSet;

import org.emonocot.model.Description;
import org.emonocot.model.Reference;
import org.emonocot.model.Taxon;
import org.emonocot.portal.view.bibliography.Bibliography;
import org.emonocot.portal.view.bibliography.SimpleBibliographyImpl;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BibliographyTest {
	
	private static Logger logger = LoggerFactory.getLogger(BibliographyTest.class);
	
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
		reference1 = createReference("1753", null, "Species Plantarum", null, "1");
		taxon.getReferences().add(reference1);
		
		reference2 = createReference(null, "Zebras are wonderful creatures", "Zebras are wonderful creatures", null, "2");
		taxon.getReferences().add(reference2);
		
		reference3 = createReference(null, "Aardvarks are horrible", "Aardvarks are horrible", null, "3");
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
		reference.setBibliographicCitation(bibliographicCitation);
		reference.setCreator(creator);
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
		for(Reference ref : bibliography.getReferences()) {
			logger.info(bibliography.getKey(ref) + " " + ref.getDate() + " " +ref.getBibliographicCitation());
		}
		assertEquals("References with earlier dates should come first", bibliography.getKey(duplicateReference4),"1");
		assertEquals("References with dates should before other references", bibliography.getKey(reference1),"2");
		assertEquals("References without dates should be sorted by author, then title", bibliography.getKey(reference2),"4");
		assertEquals("Duplicate references should return the same key", bibliography.getKey(duplicateReference4),"1");
		assertEquals("Duplicate references should return the same key", bibliography.getKey(duplicateReference5),"1");
	}

}
