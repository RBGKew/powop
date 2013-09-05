package org.emonocot.model.compare;

import java.util.Comparator;

import org.emonocot.model.Distribution;
import org.emonocot.model.constants.Location;

public class DistributionComparator  implements Comparator<Distribution> {
	
	Comparator<Location> locationComparator = new LocationComparator();

	@Override
	public int compare(Distribution o1, Distribution o2) {
		if(o1.getLocation() == null) {
			if(o2.getLocation() == null) {
				return 0;
			} else {
				return -1;
			}
		} else {
			if(o2.getLocation() == null) {
				return 1;
			} else {
				return locationComparator.compare(o1.getLocation(), o2.getLocation()); 
			}
		}
	}

}
