package org.emonocot.model.geography;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.apache.solr.common.SolrInputDocument;
import org.emonocot.model.Distribution;
import org.emonocot.model.Taxon;
import org.junit.Test;

public class GeographicalRegionFactoryTest {
	
    @Test
	public void test() throws Exception {
    	GeographicalRegionFactoryImpl factory = new GeographicalRegionFactoryImpl();
    	factory.setBaseDirectory(new File("target/test-classes/META-INF/org/tdwg/geography/"));
    	factory.afterPropertiesSet();
    	Distribution d = new Distribution();
    	d.setLocation(Continent.AFRICA);
    	Set<Distribution> distribution = new HashSet<Distribution>();
    	distribution.add(d);
    	Taxon t = new Taxon();
    	t.getDistribution().add(d);
    	factory.indexSpatial(t, new SolrInputDocument());
    	
    	factory.destroy();
		
	}

}
