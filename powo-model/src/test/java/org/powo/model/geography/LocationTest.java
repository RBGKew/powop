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
package org.powo.model.geography;

import static org.junit.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.Ignore;
import org.powo.model.constants.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTReader;

public class LocationTest {

	private static Logger logger = LoggerFactory.getLogger(LocationTest.class);

	public final void testCompareGeography() throws Exception {
		List<Geometry> list = new ArrayList<Geometry>();
		list.add(Location.EUROPE.getEnvelope());
		list.add(Location.AFRICA.getEnvelope());
		list.add(Location.CHINA.getEnvelope());
		list.add(Location.MACARONESIA.getEnvelope());
		list.add(Location.EASTERN_CANADA.getEnvelope());
		list.add(Location.FRA.getEnvelope());
		list.add(Location.ABT.getEnvelope());
		list.add(Location.GRB.getEnvelope());
		list.add(Location.IRE.getEnvelope());
		list.add(Location.ALG.getEnvelope());

		GeometryCollection geometryCollection = new GeometryCollection(
				list.toArray(new Geometry[list.size()]), new GeometryFactory());

		Coordinate[] envelope = geometryCollection.getEnvelope().getCoordinates();
		for (Coordinate c : envelope) {
			logger.debug(Math.round(c.x) + " " + Math.round(c.y));
		}
	}

	@Test
	@Ignore("Re-enabling tests")
	public final void testGetEnvelopeForSolr() throws Exception {
		String paraguayWKT = "POLYGON ((-62.64372600000005 -27.588293504882838, -54.24392600000006 -27.588293504882838, -54.24392600000006 -19.296693504882825, -62.64372600000005 -19.296693504882825, -62.64372600000005 -27.588293504882838))";
		Polygon paraguay = (Polygon) (new WKTReader().read(paraguayWKT));
		assertThat(Location.PAR.getEnvelopeForSolr(), is(paraguay));
	}

	@Test
	public void testGetAllParents() {
		assertEquals(Set.of(Location.WESTERN_SOUTH_AMERICA, Location.SOUTHERN_AMERICA), Location.CLM.getAllParents());
		assertEquals(Set.of(Location.SOUTHERN_AMERICA), Location.WESTERN_SOUTH_AMERICA.getAllParents());
		assertEquals(Set.of(), Location.SOUTHERN_AMERICA.getAllParents());
	}

	@Test
	public void testGetAllChildren() {
		assertEquals(Set.of(), Location.CLM_OO.getAllChildren());
		assertEquals(Set.of(Location.CLM_OO), Location.CLM.getAllChildren());
		assertEquals(
				Set.of(Location.BOL, Location.BOL_OO, Location.CLM, Location.CLM_OO, Location.ECU,
						Location.ECU_OO, Location.GAL, Location.GAL_OO, Location.PER, Location.PER_OO),
				Location.WESTERN_SOUTH_AMERICA.getAllChildren());
	}
}
