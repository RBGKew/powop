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
import org.emonocot.model.constants.Location;
import org.emonocot.pager.Page;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.io.WKTWriter;

public class SpatialSearchTest extends AbstractPersistenceTest {

	WKTWriter wktWriter = new WKTWriter();

	Logger logger = LoggerFactory.getLogger(SpatialSearchTest.class);

	@Before
	public final void setUp() throws Exception {
		super.doSetUp();
		Collection<SolrInputDocument> geographicalRegions = new HashSet<SolrInputDocument>();

		geographicalRegions.add(addRegion(Location.BRAZIL));
		geographicalRegions.add(addRegion(Location.PAR));
		solrServer.add(geographicalRegions);
		solrServer.commit(true,true);
	}

	private SolrInputDocument addRegion(Location location) {
		SolrInputDocument sid = new SolrInputDocument();
		sid.addField("id", "Location_" + location.getCode());
		sid.addField("location.tdwg_code_s", location.getCode());
		sid.addField("location.name_s", location.name());
		sid.addField("geo", wktWriter.write(location.getEnvelopeForSolr()));
		return sid;
	}

	@After
	public final void tearDown() throws Exception {
		super.doTearDown();
	}

	@Override
	public final void setUpTestData() {
		Taxon taxon1 = createTaxon("Aus", "1", null, null, "Aaceae", null, null, null, null, null, null, new Location[] {}, null);
		createTaxon("Aus bus", "2", taxon1, null, "Aaceae", null, null, null, null, null, null, new Location[] {Location.BRAZIL}, null);
		createTaxon("Aus ceus", "3", taxon1, null, null, null, null, null, null, null, null, new Location[] {Location.PAR}, null);
	}

	@Test
	public final void testSpatialSearch() throws Exception {
		// should return Aus bus but not Aus ceus
		Set<String> names = resultsInRegion("ENVELOPE(-50.00, -40.0, 0.0, -15.0)");
		assertThat(names, hasItems("Aus bus"));
		assertThat(names, not(hasItems("Aus ceus")));

		// should return both Aus bus and Aus ceus
		names = resultsInRegion("ENVELOPE(-60.0, -40.0, -15.0, -25.0)");
		assertThat(names, hasItems("Aus bus", "Aus ceus"));
	}

	private final Set<String> resultsInRegion(String envelope) throws Exception {
		String query = "{!join to=taxon.distribution_ss from=location.tdwg_code_s}geo:\"Intersects(" + envelope + ")\"";
		Set<String> names = new HashSet<String>();
		Page<SearchableObject> page = getSearchableObjectDao().search(null, query, null, null, null, null, null, null, null);
		for (SearchableObject t : page.getRecords()) {
			names.add(((Taxon)t).getScientificName());
		}
		logger.warn("Results in {} = {}", envelope, names.toString());

		return names;
	}
}
