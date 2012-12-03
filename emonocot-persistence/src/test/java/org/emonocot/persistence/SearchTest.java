package org.emonocot.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.HashMap;
import java.util.Map;

import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.emonocot.model.SearchableObject;
import org.emonocot.model.Taxon;
import org.emonocot.model.constants.DescriptionType;
import org.emonocot.model.geography.Continent;
import org.emonocot.model.geography.Country;
import org.emonocot.model.geography.Location;
import org.emonocot.model.geography.Region;
import org.emonocot.pager.Page;
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
        Taxon taxon1 = createTaxon("Aus", "1", null, null, "Aaceae", null, null,
                null, null, null, null, new Location[] {}, null);
        createDescription(taxon1, DescriptionType.habitat, "Lorem ipsum", null);
        Taxon taxon2 = createTaxon("Aus bus", "2", taxon1, null, "Aaceae", null,
                null, null, null, null, null,
                new Location[] {Continent.AUSTRALASIA,
                        Region.BRAZIL, Region.CARIBBEAN }, null);
        Taxon taxon3 = createTaxon("Aus ceus", "3", taxon1, null, null, null,
                null, null, null, null, null,
                new Location[] {Region.NEW_ZEALAND }, null);
        createTaxon("Aus deus", "4", null, taxon2, "Aaceae", null, null, null,
                null, null, null, new Location[] {}, null);
        createTaxon("Aus eus", "5", null, taxon3, null, null, null, null, null,
                null, null, new Location[] {}, null);
        createTaxon("Alania", "urn:kew.org:wcs:taxon:294463", null, null, null, null, null, null, null,
                null, null, new Location[] {Country.NSW}, null);
        createTaxon(null, "6", null, null, null, null, null, null, null,
                null, null, new Location[] {}, null);
    }

    /**
     *
     */
    @Test
    public final void testSearch() {
        Page<Taxon> results = getTaxonDao().search("taxon.scientific_name_t:Aus", null, null, null,
                new String[] {"taxon.distribution_TDWG_0_ss" }, null, null, null);
        assertEquals("There should be 5 taxa matching Aus", new Integer(5), (Integer)results.getSize());
        
    }

    /**
    *
    */
    @Test
    public final void testRestrictedSearch() {
        Map<String, String> selectedFacets = new HashMap<String, String>();

        selectedFacets.put("taxon.distribution_TDWG_0_ss", "AUSTRALASIA");

        Page<Taxon> results = getTaxonDao().search("taxon.scientific_name_t:Aus", null, null, null,
                new String[] {"taxon.distribution_TDWG_0_ss" , "taxon.distribution_TDWG_1_ss"}, selectedFacets, null, null);
        assertEquals("There should be 2 taxa matching Aus found in AUSTRALASIA", new Integer(2), (Integer)results.getSize());
        for(String facetName : results.getFacetNames()) {
      	   System.out.println(facetName);
      	   FacetField facet = results.getFacetField(facetName);
      	   for(Count count : facet.getValues()) {
      		   System.out.println("\t" + count.getName() + " " + count.getCount());
      	   }
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
     * Test method for {@link org.emonocot.persistence.dao.hibernate.TaxonDaoImpl#findByExample(org.emonocot.model.Taxon)}.
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
        Page<SearchableObject> results = searchableObjectDao.search("", null, null, null, null, null, "searchable.label_sort_asc", null);

        assertEquals("There should be 7 results", 7, results.getSize().intValue());
        
        assertEquals("The first results should be urn:kew.org:wcs:taxon:294463", "urn:kew.org:wcs:taxon:294463", results.getRecords().get(0).getIdentifier());
        
    }
}
