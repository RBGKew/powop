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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.solr.common.SolrInputDocument;
import org.emonocot.model.SearchableObject;
import org.emonocot.model.Taxon;
import org.emonocot.model.constants.DescriptionType;
import org.emonocot.model.constants.Location;
import org.emonocot.pager.Page;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.vividsolutions.jts.io.WKTWriter;

/**
 *
 * @author ben
 *
 */

public class SpatialSearchIntegrationTest extends AbstractPersistenceTest {

	WKTWriter wktWriter = new WKTWriter();

	/**
	 * @throws java.lang.Exception
	 *             if there is a problem
	 */
	@Before
	public final void setUp() throws Exception {
		super.doSetUp();
		Collection<SolrInputDocument> geographicalRegions = new HashSet<SolrInputDocument>();

		geographicalRegions.add(addRegion(Location.AUSTRALASIA));
		geographicalRegions.add(addRegion(Location.BRAZIL));
		geographicalRegions.add(addRegion(Location.CARIBBEAN));
		geographicalRegions.add(addRegion(Location.NEW_ZEALAND));
		geographicalRegions.add(addRegion(Location.NSW));
		solrServer.add(geographicalRegions);
		solrServer.commit(true,true);
	}

	private SolrInputDocument addRegion(Location location) {
		SolrInputDocument sid = new SolrInputDocument();
		sid.addField("id", "Location_" + location.getCode());
		sid.addField("location.tdwg_code_s", location.getCode());
		sid.addField("location.name_s", location.name());
		sid.addField("geo", wktWriter.write(location.getEnvelope()));
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
				null, null, null, null, new Location[] {}, null);
		createDescription(taxon1, DescriptionType.habitat, "Lorem ipsum", null);
		Taxon taxon2 = createTaxon("Aus bus", "2", taxon1, null, "Aaceae", null,
				null, null, null, null, null,
				new Location[] {Location.AUSTRALASIA,
				Location.BRAZIL, Location.CARIBBEAN }, null);
		Taxon taxon3 = createTaxon("Aus ceus", "3", taxon1, null, null, null,
				null, null, null, null, null,
				new Location[] {Location.NEW_ZEALAND }, null);
		createTaxon("Aus deus", "4", null, taxon2, "Aaceae", null, null, null,
				null, null, null, new Location[] {}, null);
		createTaxon("Aus eus", "5", null, taxon3, null, null, null, null, null,
				null, null, new Location[] {}, null);
		createTaxon("Alania", "urn:kew.org:wcs:taxon:294463", null, null, null, null, null, null, null,
				null, null, new Location[] {Location.NSW}, null);
		createTaxon(null, "6", null, null, null, null, null, null, null,
				null, null, new Location[] {}, null);
	}

	/**
	 *
	 */
	@Test
	public final void testSpatialSearch() throws Exception {
		//testSpatialSearch() should return Aus bus but not Aus ceus
		Page<SearchableObject> page = getSearchableObjectDao().search(
				null, "{!join to=taxon.distribution_ss from=location.tdwg_code_s}geo:\"Intersects(150.00 -40.0 160.0 -20.0)\"", null, null, null,
				null, null, null, null);
		Set<String> names = new HashSet<String>();
		for (SearchableObject t : page.getRecords()) {
			names.add(((Taxon)t).getScientificName());
		}

		assertThat(names, hasItems("Aus bus"));
		assertThat(names, hasItems(not("Aus ceus")));
	}
}
