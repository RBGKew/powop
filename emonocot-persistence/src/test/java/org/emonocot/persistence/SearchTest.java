package org.emonocot.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.emonocot.api.FacetName;
import org.emonocot.api.Sorting;
import org.emonocot.api.Sorting.SortDirection;
import org.emonocot.model.common.SearchableObject;
import org.emonocot.model.description.Feature;
import org.emonocot.model.geography.Continent;
import org.emonocot.model.geography.Country;
import org.emonocot.model.geography.GeographicalRegion;
import org.emonocot.model.geography.Region;
import org.emonocot.model.hibernate.DistributionBridge;
import org.emonocot.model.pager.Page;
import org.emonocot.model.taxon.Taxon;
import org.hibernate.search.query.facet.Facet;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author ben
 *
 */
public class SearchTest extends AbstractPersistenceTest {

    /**
     *
     */
    @BeforeClass
    public static void doSetupRegions() throws IOException {
       DistributionBridge distributionBridge = new DistributionBridge();
       distributionBridge.setupRegions();
    }
    /**
     * @throws java.lang.Exception
     *             if there is a problem
     */
    @Before
    public final void setUp() throws Exception {
        super.doSetUp();
    }

    /**
     * @throws java.lang.Exception
     *             if there is a problem
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
        Taxon taxon1 = createTaxon("Aus", "1", null, null, "Aaceae", null, null,
                null, null, null, null, new GeographicalRegion[] {}, null);
        createTextContent(taxon1, Feature.habitat, "Lorem ipsum", null);
        Taxon taxon2 = createTaxon("Aus bus", "2", taxon1, null, "Aaceae", null,
                null, null, null, null, null,
                new GeographicalRegion[] {Continent.AUSTRALASIA,
                        Region.BRAZIL, Region.CARIBBEAN }, null);
        Taxon taxon3 = createTaxon("Aus ceus", "3", taxon1, null, null, null,
                null, null, null, null, null,
                new GeographicalRegion[] {Region.NEW_ZEALAND }, null);
        createTaxon("Aus deus", "4", null, taxon2, "Aaceae", null, null, null,
                null, null, null, new GeographicalRegion[] {}, null);
        createTaxon("Aus eus", "5", null, taxon3, null, null, null, null, null,
                null, null, new GeographicalRegion[] {}, null);
        createTaxon("Alania", "urn:kew.org:wcs:taxon:294463", null, null, null, null, null, null, null,
                null, null, new GeographicalRegion[] {Country.NSW}, null);
        createTaxon(null, "6", null, null, null, null, null, null, null,
                null, null, new GeographicalRegion[] {}, null);
    }

    /**
     *
     */
    @Test
    public final void testSearch() {
        Page<Taxon> page = getTaxonDao().search("name:Aus", null, null, null,
                new FacetName[] {FacetName.CONTINENT }, null, null, null);
    }

    /**
    *
    */
    @Test
    public final void testRestrictedSearch() {
        Map<FacetName, String> selectedFacets = new HashMap<FacetName, String>();

        selectedFacets.put(FacetName.CONTINENT, "AUSTRALASIA");

        Page<Taxon> page = getTaxonDao().search("name:Aus", null, null, null,
                new FacetName[] {FacetName.CONTINENT , FacetName.REGION}, selectedFacets, null, null);
    }

    /**
     *
     */
    @Test
    public final void testSpatialSearch() {
        //testSpatialSearch() should return Aus bus but not Aus ceus
        Page<Taxon> page = getTaxonDao().search(
        null, "Intersects(150.00 -40.0 160.0 -20.0)", null, null, null,
                null, null, null);
        for (Taxon t : page.getRecords()) {
            System.out.println(t.getName());
        }
    }

    /**
     *
     */
    @Test
    public final void testSearchEmbeddedContent() {
        Page<Taxon> page = getTaxonDao().search("Lorem", null, null, null,
                null, null, null, null);

        assertFalse(page.getSize() == 0);
    }
    
    @Test
    public final void testSearchByHigherName() {
        Page<SearchableObject> results = searchableObjectDao.search("Aaceae", null, null, null, null, null, null, null);

        assertEquals("There should be 3 results", 3, results.getSize().intValue());
    }
    
    @Test
    public final void testSearchBySynonym() {
        Page<SearchableObject> results = searchableObjectDao.search("deus", null, null, null, null, null, null, null);


        assertEquals("There should be 2 results, the synonym and accepted name", 2, results.getSize().intValue());
    }

    /**
     * Test method for {@link org.emonocot.persistence.dao.hibernate.TaxonDaoImpl#findByExample(org.emonocot.model.taxon.Taxon)}.
     */
    @Test
    public void testFindByExample() {
        Taxon example = new Taxon();
        example.setFamily("Aaceae");
        Page<Taxon> results = getTaxonDao().searchByExample(example, false, false);
        assertEquals("There should be 3 results", new Integer(3), results.getSize());
        
    }

	/**
	 * BUG #308 As an eMonocot user when I search results by A to Z I do not
	 * understand the order of the results page.
	 */
    @Test
    public final void testSearchWithNulls() {
        Page<SearchableObject> results = searchableObjectDao.search("", null, null, null, null, null, new Sorting("label", SortDirection.FORWARD), null);

        assertEquals("There should be 7 results", 7, results.getSize().intValue());
        for(SearchableObject s : results.getRecords()) {
        	System.out.println(s.getIdentifier());
        }
        assertEquals("The first results should be urn:kew.org:wcs:taxon:294463", "urn:kew.org:wcs:taxon:294463", results.getRecords().get(0).getIdentifier());
        
    }
}
