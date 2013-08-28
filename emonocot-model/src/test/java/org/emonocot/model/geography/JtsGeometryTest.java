package org.emonocot.model.geography;

import static junit.framework.Assert.assertTrue;

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

}
