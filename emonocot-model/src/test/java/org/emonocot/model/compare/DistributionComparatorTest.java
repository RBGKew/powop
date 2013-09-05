package org.emonocot.model.compare;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.emonocot.model.Distribution;
import org.emonocot.model.constants.Location;
import org.junit.Before;
import org.junit.Test;

public class DistributionComparatorTest {
	
	private Comparator<Distribution> distributionComparator;
	
	private Comparator<Location> locationComparator;
	
	private List<Distribution> distributions;
	
	private List<Location> locations;
	
	@Before
	public void setUp() {
		 distributionComparator = new DistributionComparator();
		 distributions = new ArrayList<Distribution>();
		 locationComparator = new LocationComparator();
		 locations = new ArrayList<Location>();
		 distributions.add(createDistribution("distribution1", Location.EUROPE, null));
	     distributions.add(createDistribution("distribution2", Location.ABT, null));
	     distributions.add(createDistribution("distribution3", Location.EUROPE, "Europe"));
	     distributions.add(createDistribution("distribution4", Location.ABT, "Alberta"));
	     locations.add(Location.SUD);
	     locations.add(Location.SUL);
	     locations.add(Location.SUD);
	     locations.add(Location.SUL);
	}
	
	@Test
	public void testTransiativeComparison() {
		Collections.sort(distributions, distributionComparator);
		
	}
	
	@Test
	public void testTransiativeComparisonLocations() {

		Collections.sort(locations, locationComparator);
		
	}

	private Distribution createDistribution(String identifier, Location location, String locality) {
		Distribution d = new Distribution();
		d.setIdentifier(identifier);
		d.setLocation(location);
		d.setLocality(locality);
		return d;
	}

}
