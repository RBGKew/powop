package org.emonocot.persistence;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.emonocot.model.description.Feature;
import org.emonocot.model.description.TextContent;
import org.emonocot.model.geography.Continent;
import org.emonocot.model.geography.GeographicalRegion;
import org.emonocot.model.geography.Region;
import org.emonocot.model.media.Image;
import org.emonocot.model.reference.Reference;
import org.emonocot.model.taxon.Taxon;
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
                null, new GeographicalRegion[] {}, null);
        createTextContent(taxon1, Feature.associations, "Lorem ipsum",
                reference);
        Taxon taxon2 = createTaxon("Aus bus", "urn:lsid:example.com:taxon:2",
                taxon1, null, null, null, null, null, null, null,
                null, new GeographicalRegion[] {Continent.AUSTRALASIA,
                        Region.BRAZIL, Region.CARIBBEAN }, null);
        Taxon taxon3 = createTaxon("Aus ceus", "urn:lsid:example.com:taxon:3",
                taxon1, null, null, null, null, null, null, null,
                null, new GeographicalRegion[] {Region.NEW_ZEALAND }, null);
        Taxon taxon4 = createTaxon("Aus deus", "urn:lsid:example.com:taxon:4",
                null, taxon2, null, null, null, null, null, null,
                null, new GeographicalRegion[] {}, null);
        Taxon taxon5 = createTaxon("Aus eus", "urn:lsid:example.com:taxon:5",
                null, taxon3, null, null, null, null, null, null,
                null, new GeographicalRegion[] {}, null);
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
                Hibernate.isInitialized(taxon.getContent()));
        TextContent textContent = taxon.getContent(Feature.associations);
        assertNotNull("TextContent should not be null", textContent);
        assertTrue("References should be initialized",
                Hibernate.isInitialized(textContent.getReferences()));
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
