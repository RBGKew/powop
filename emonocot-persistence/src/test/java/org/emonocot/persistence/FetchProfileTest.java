package org.emonocot.persistence;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.emonocot.model.Image;
import org.emonocot.model.Reference;
import org.emonocot.model.Taxon;
import org.emonocot.model.Description;
import org.emonocot.model.constants.DescriptionType;
import org.emonocot.model.geography.Location;
import org.hibernate.Hibernate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author ben
 *
 */
public class FetchProfileTest extends AbstractPersistenceTest {

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
        Reference reference = createReference(
                "urn:lsid:example.com:reference:1", "Test title",
                "Test author");

        Taxon taxon1 = createTaxon("Aus", "urn:lsid:example.com:taxon:1", null,
                null, null, null, null, null, null, null,
                null, new Location[] {}, null);
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
}
