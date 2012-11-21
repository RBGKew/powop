package org.emonocot.model.geography;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.apache.solr.common.SolrInputDocument;
import org.emonocot.model.Distribution;
import org.emonocot.model.Taxon;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.spatial4j.core.context.jts.JtsSpatialContext;
import com.spatial4j.core.io.JtsShapeReadWriter;
import com.vividsolutions.jts.io.WKTWriter;

public class GeographicalRegionFactoryTest {

	private GeographicalRegionFactoryImpl factory = new GeographicalRegionFactoryImpl();

	private JtsSpatialContext spatialContext = new JtsSpatialContext(false);
	private JtsShapeReadWriter jtsShapeReadWriter = new JtsShapeReadWriter(
			spatialContext);
	private WKTWriter wktWriter = new WKTWriter();

	@Before
	public void setUp() throws Exception {
		factory.setBaseDirectory(new File(
				"target/test-classes/META-INF/org/tdwg/geography/"));
		factory.afterPropertiesSet();
	}

	@After
	public void tearDown() {
		factory.destroy();
	}

	@Test
	public void testIndexSpatial() throws Exception {
		Distribution d = new Distribution();
		d.setLocation(Continent.AFRICA);
		Set<Distribution> distribution = new HashSet<Distribution>();
		distribution.add(d);
		Taxon t = new Taxon();
		t.getDistribution().add(d);
		factory.indexSpatial(t, new SolrInputDocument());
	}

	/**
	 * Bug where we were using the entire polygon which leads to
	 * self-intersection etc due to interior rings and so on. We only want to
	 * use the boundary
	 * 
	 * @throws Exception
	 */
	@Test
	public void readAllTdwgShapes() throws Exception {
		for (Continent continent : Continent.values()) {
			jtsShapeReadWriter.readShape(wktWriter.write(factory.getGeometry(continent).getBoundary()));
		}
		
		for (Region region : Region.values()) {
			jtsShapeReadWriter.readShape(wktWriter.write(factory.getGeometry(region).getBoundary()));
		}
		
		for (Country country : Country.values()) {
			jtsShapeReadWriter.readShape(wktWriter.write(factory.getGeometry(country).getBoundary()));
		}
	}

}
