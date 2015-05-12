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
