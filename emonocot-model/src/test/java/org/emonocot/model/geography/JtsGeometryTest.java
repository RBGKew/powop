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
package org.emonocot.model.geography;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import com.spatial4j.core.context.jts.JtsSpatialContext;
import com.spatial4j.core.shape.Shape;
import com.spatial4j.core.shape.jts.JtsGeometry;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;
import com.vividsolutions.jts.simplify.TopologyPreservingSimplifier;

public class JtsGeometryTest {

	private static Logger logger = LoggerFactory.getLogger(JtsGeometryTest.class);

	private ClassPathResource countriesResource = new ClassPathResource("org/emonocot/model/level3_simplified.txt");

	private ClassPathResource regionsResource = new ClassPathResource("org/emonocot/model/level2_simplified.txt");

	private ClassPathResource continentsResource = new ClassPathResource("org/emonocot/model/level1.txt");

	private ClassPathResource level4Resource = new ClassPathResource("org/emonocot/model/level4.txt");

	/**
	 * Simplfied AND, KRA, NFL, NOR and SCI using
	 * TopologyPreservingSimplifier.simplify(geom, 0.01)
	 *
	 * Skipped NGA
	 * @throws Exception
	 */
	@Test
	public void testCountries() throws Exception {
		JtsSpatialContext ctx = new JtsSpatialContext(false);
		SampleDataReader sampleDataReader = new SampleDataReader(countriesResource.getInputStream());
		WKTReader wktreader = new WKTReader();
		WKTWriter wktWriter = new WKTWriter();

		while(sampleDataReader.hasNext()) {
			SampleData sampleData = sampleDataReader.next();

			Geometry geom = wktreader.read(sampleData.shape);
			if(!geom.isValid()) {
				logger.warn(sampleData.name + " is not valid");
			}


			Shape shape = new JtsGeometry(geom, ctx, true);
		}
	}

	/**
	 * Simplfied 22, 30, 61 and 73 using
	 * TopologyPreservingSimplifier.simplify(geom, 0.01)
	 *
	 * Skipped 10, 41 and 72
	 * @throws Exception
	 */
	@Test
	public void testRegions() throws Exception {
		JtsSpatialContext ctx = new JtsSpatialContext(false);
		SampleDataReader sampleDataReader = new SampleDataReader(regionsResource.getInputStream());
		WKTReader wktreader = new WKTReader();
		WKTWriter wktwriter = new WKTWriter();

		while(sampleDataReader.hasNext()) {
			SampleData sampleData = sampleDataReader.next();

			Geometry geom = wktreader.read(sampleData.shape);
			if(!geom.isValid()) {
				logger.warn(sampleData.name + " is not valid");
			}

			Shape shape = new JtsGeometry(geom, ctx, true);
		}
	}

	@Ignore
	@Test
	public void testContinents() throws Exception {
		JtsSpatialContext ctx = new JtsSpatialContext(false);
		SampleDataReader sampleDataReader = new SampleDataReader(continentsResource.getInputStream());
		WKTReader wktreader = new WKTReader();
		WKTWriter wktwriter = new WKTWriter();

		while(sampleDataReader.hasNext()) {
			SampleData sampleData = sampleDataReader.next();

			Geometry geom = wktreader.read(sampleData.shape);
			if(!geom.isValid()) {
				logger.warn(sampleData.name + " is not valid");
			}

			if(false) {
				Shape shape = new JtsGeometry(TopologyPreservingSimplifier.simplify(geom,0.1), ctx, true);
				logger.debug(sampleData.code + " " + wktwriter.write(TopologyPreservingSimplifier.simplify(geom,0.1)));
			} else if (sampleData.code.equals("1")) {

			} else {
				logger.debug(sampleData.code);
				Shape shape = new JtsGeometry(geom, ctx, true);
			}
		}
	}

	@Test
	@Ignore
	public void testSimplifyLevel4() throws Exception {
		JtsSpatialContext ctx = new JtsSpatialContext(false);
		SampleDataReader sampleDataReader = new SampleDataReader(level4Resource.getInputStream());
		PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter("level4_simplified.txt")));
		WKTReader wktreader = new WKTReader();
		WKTWriter wktwriter = new WKTWriter();

		while(sampleDataReader.hasNext()) {
			SampleData sampleData = sampleDataReader.next();

			Geometry geom = wktreader.read(sampleData.shape);
			if(!geom.isValid()) {
				logger.warn(sampleData.name + " is not valid");
			}

			printWriter.print(sampleData.id +"\t" + sampleData.code + "\t" + sampleData.name +"\t" + wktwriter.write(TopologyPreservingSimplifier.simplify(geom,0.2)) + "\n");

		}
		printWriter.flush();
	}
}
