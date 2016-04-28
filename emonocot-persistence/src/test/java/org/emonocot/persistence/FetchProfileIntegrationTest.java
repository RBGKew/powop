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
package org.emonocot.persistence;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.beanutils.BeanUtils;
import org.emonocot.model.Comment;
import org.emonocot.model.Description;
import org.emonocot.model.Image;
import org.emonocot.model.Reference;
import org.emonocot.model.Taxon;
import org.emonocot.model.auth.User;
import org.emonocot.model.constants.DescriptionType;
import org.emonocot.model.constants.Location;
import org.emonocot.model.registry.Organisation;
import org.hibernate.Hibernate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

/**
 *
 * @author ben
 *
 */

public class FetchProfileIntegrationTest extends AbstractPersistenceTest {

	/**
	 * @throws java.lang.Exception if there is a problem
	 */
	@Before
	public final void setUp() throws Exception {
		super.doSetUp();
	}

	/**
	 * @throws java.lang.Exception if there is a problem
	 */
	@After
	public final void tearDown() throws Exception {
		super.doTearDown();
	}

	/**
	 *
	 */
	@Override
	public final void setUpTestData() {
		Organisation organisation = createSource("testOrg1", "http://example.org", "Test Organisation", "test@example.com");
		Reference reference = createReference(
				"urn:lsid:example.com:reference:1", "Test title",
				"Test author");

		Taxon taxon1 = createTaxon("Aus", "urn:lsid:example.com:taxon:1", null,
				null, null, null, null, null, null, null,
				organisation, new Location[] {}, null);
		createDescription(taxon1, DescriptionType.associations, "Lorem ipsum",
				reference);
		Taxon taxon2 = createTaxon("Aus bus", "urn:lsid:example.com:taxon:2",
				taxon1, null, null, null, null, null, null, null,
				null, new Location[] {Location.AUSTRALASIA,
				Location.BRAZIL, Location.CARIBBEAN }, null);
		Taxon taxon3 = createTaxon("Aus ceus", "urn:lsid:example.com:taxon:3",
				taxon1, null, null, null, null, null, null, null,
				null, new Location[] {Location.NEW_ZEALAND }, null);
		Taxon taxon4 = createTaxon("Aus deus", "urn:lsid:example.com:taxon:4",
				null, taxon2, null, null, null, null, null, null,
				null, new Location[] {}, null);
		Taxon taxon5 = createTaxon("Aus eus", "urn:lsid:example.com:taxon:5",
				null, taxon3, null, null, null, null, null, null,
				null, new Location[] {}, null);
		Image image = createImage("Aus aus", "image1", null, taxon1, null);
		User user = createUser("test@emonocot.org", "test", "user");
		createComment("testComment1", "This is a comment", taxon1, user);
	}

	/**
	 *
	 */
	@Test
	public final void testFetchProfile() {
		Taxon taxon = getTaxonDao().load("urn:lsid:example.com:taxon:1",
				"taxon-page");
		assertTrue("Images should be initialized",
				Hibernate.isInitialized(taxon.getImages()));
		assertTrue("Content should be initialized",
				Hibernate.isInitialized(taxon.getDescriptions()));
		Description description = null;
		for(Description d : taxon.getDescriptions()) {
			if(d.getType().equals(DescriptionType.associations)) {
				description =  d;
				break;
			}
		}

		assertNotNull("Description should not be null", description);
		assertTrue("References should be initialized",
				Hibernate.isInitialized(description.getReferences()));
	}

	/**
	 *
	 */
	@Test
	public final void testSearchableObjectFetchProfile() {
		Image image = (Image) getSearchableObjectDao().load("image1",
				"taxon-with-image");
		assertTrue("Taxon should be initialized",
				Hibernate.isInitialized(image.getTaxon()));
		Taxon taxon = (Taxon) getSearchableObjectDao().load(
				"urn:lsid:example.com:taxon:5", "taxon-with-image");
	}

	@Test
	public final void testNestedAssociation() {
		Comment c = commentDao.load("testComment1", "aboutData");
		assertTrue("The 'aboutData' hibernate proxy should have been initialized ", Hibernate.isInitialized(c.getAboutData()));
		Object authority = null;
		Object organisation = null;
		try {
			authority = BeanUtils.getProperty(c.getAboutData(), "authority");
			organisation = BeanUtils.getProperty(c.getAboutData(), "organisation");
		} catch (Exception e) {}
		assertTrue("Their should be an Organisation that is initialized", (authority != null && Hibernate.isInitialized(authority))
				|| (organisation != null && Hibernate.isInitialized(organisation)));
	}
}
