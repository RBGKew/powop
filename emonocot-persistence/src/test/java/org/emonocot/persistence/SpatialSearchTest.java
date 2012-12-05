package org.emonocot.persistence;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.solr.common.SolrInputDocument;
import org.emonocot.model.Taxon;
import org.emonocot.model.constants.DescriptionType;
import org.emonocot.model.geography.Continent;
import org.emonocot.model.geography.Country;
import org.emonocot.model.geography.GeographicalRegion;
import org.emonocot.model.geography.Region;
import org.emonocot.pager.Page;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.io.WKTWriter;

/**
 *
 * @author ben
 *
 */
public class SpatialSearchTest extends AbstractPersistenceTest {
	
	WKTWriter wktWriter = new WKTWriter();
	
    /**
     * @throws java.lang.Exception
     *             if there is a problem
     */
    @Before
    public final void setUp() throws Exception {
        super.doSetUp();
        Collection<SolrInputDocument> geographicalRegions = new HashSet<SolrInputDocument>();
        
        geographicalRegions.add(addRegion(Continent.AUSTRALASIA));
        geographicalRegions.add(addRegion(Region.BRAZIL));
        geographicalRegions.add(addRegion(Region.CARIBBEAN));
        geographicalRegions.add(addRegion(Region.NEW_ZEALAND));
        geographicalRegions.add(addRegion(Country.NSW));
        solrServer.add(geographicalRegions);
        solrServer.commit();
    }
    
    private SolrInputDocument addRegion(GeographicalRegion region) {
    	SolrInputDocument sid = new SolrInputDocument();
    	sid.addField("id", "Location_" + region.getCode());
        sid.addField("location.tdwg_code_s", region.getCode());
        sid.addField("location.name_s", region.name());
        sid.addField("geo", wktWriter.write(region.getEnvelope()));
        return sid;
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
        createDescription(taxon1, DescriptionType.habitat, "Lorem ipsum", null);
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
    public final void testSpatialSearch() {
        //testSpatialSearch() should return Aus bus but not Aus ceus
        Page<Taxon> page = getTaxonDao().search(
        null, "Intersects(150.00 -40.0 160.0 -20.0)", null, null, null,
                null, null, null);
        Set<String> names = new HashSet<String>();
        for (Taxon t : page.getRecords()) {
           names.add(t.getScientificName());
        }
        
        assertThat(names, hasItems("Aus bus"));
        assertThat(names, hasItems(not("Aus ceus")));
    }
}
