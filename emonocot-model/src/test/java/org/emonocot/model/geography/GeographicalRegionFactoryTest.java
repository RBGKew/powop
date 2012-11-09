package org.emonocot.model.geography;

import java.io.File;

import org.junit.Test;

public class GeographicalRegionFactoryTest {
	
    @Test
	public void test() throws Exception {
    	GeographicalRegionFactory factory = new GeographicalRegionFactory();
    	factory.setBaseDirectory(new File("target/test-classes/META-INF/org/tdwg/geography/"));
    	factory.afterPropertiesSet();
    	
    	System.out.println(factory.getWKT(Continent.AFRICA));
    	
    	factory.destroy();
		
	}

}
