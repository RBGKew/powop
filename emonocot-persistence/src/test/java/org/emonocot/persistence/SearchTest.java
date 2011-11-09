package org.emonocot.persistence;

import static org.junit.Assert.assertFalse;

import java.util.HashMap;
import java.util.Map;

import org.emonocot.api.FacetName;
import org.emonocot.model.description.Feature;
import org.emonocot.model.geography.Continent;
import org.emonocot.model.geography.GeographicalRegion;
import org.emonocot.model.geography.Region;
import org.emonocot.model.pager.Page;
import org.emonocot.model.taxon.Taxon;
import org.hibernate.search.query.facet.Facet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author ben
 *
 */
public class SearchTest extends AbstractPersistenceTest {
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
        Taxon taxon1 = createTaxon("Aus", "1", null, null, null, null, null,
                null, null, null, null, new GeographicalRegion[] {});
        createTextContent(taxon1, Feature.habitat, "Lorem ipsum");
        Taxon taxon2 = createTaxon("Aus bus", "2", taxon1, null, null, null,
                null, null, null, null, null,
                new GeographicalRegion[] {Continent.AUSTRALASIA,
                        Region.BRAZIL, Region.CARIBBEAN });
        Taxon taxon3 = createTaxon("Aus ceus", "3", taxon1, null, null, null,
                null, null, null, null, null,
                new GeographicalRegion[] {Region.NEW_ZEALAND });
        createTaxon("Aus deus", "4", null, taxon2, null, null, null, null,
                null, null, null, new GeographicalRegion[] {});
        createTaxon("Aus eus", "5", null, taxon3, null, null, null, null, null,
                null, null, new GeographicalRegion[] {});
    }

    /**
     *
     */
    @Test
    public final void testSearch() {
        Page<Taxon> page = getTaxonDao().search("name:Aus", null, null, null,
                new FacetName[] {FacetName.CONTINENT }, null, null, null);
        for (Taxon t : page.getRecords()) {
            System.out.println(t.getName());
        }

        for (Facet facet : page.getFacets().get(FacetName.CONTINENT.name())) {
            System.out.println(facet.getValue() + " " + facet.getCount());
        }
    }

    /**
    *
    */
    @Test
    public final void testRestrictedSearch() {
        Map<FacetName, String> selectedFacets = new HashMap<FacetName, String>();

        selectedFacets.put(FacetName.CONTINENT, "AUSTRALASIA");

        Page<Taxon> page = getTaxonDao().search("name:Aus", null, null, null,
                new FacetName[] {FacetName.CONTINENT }, selectedFacets, null, null);
        for (Taxon t : page.getRecords()) {
            System.out.println(t.getName());
        }

        for (Facet facet : page.getFacets().get(FacetName.CONTINENT.name())) {
            System.out.println(facet.getValue() + " " + facet.getCount());
        }
    }

    /**
   *
   */
    @Test
    public final void testSpatialSearch() {
        System.out
                .println("testSpatialSearch() should return Aus bus but not Aus ceus");
        Page<Taxon> page = getTaxonDao().search(

        "name:Aus", "Intersects(100.0 -40.0 155.0 -5.0)", null, null, null,
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
        for (Taxon t : page.getRecords()) {
            System.out.println(t.getName());
        }
        assertFalse(page.getSize() == 0);
    }
}
