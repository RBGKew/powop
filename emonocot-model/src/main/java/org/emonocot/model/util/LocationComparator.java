package org.emonocot.model.util;

import java.util.Comparator;

import org.emonocot.model.constants.Location;

/**
 *
 * @author ben
 *
 */
public class LocationComparator implements
        Comparator<Location> {

    /**
     * @param o1 Set the first region
     * @param o2 Set the second region
     * @return -1 if o1 comes before o2, 1 if o1 comes after o2 and 0 if the two
     *         regions are equal
     */
    public final int compare(final Location o1,
            final Location o2) {
    	switch(o1.getLevel()) {
    	case 0:
    		switch(o2.getLevel()) {
        	case 0:
        		return o1.compareNames(o2);
        	case 1:
        		return o1.compareNames(o2.getParent());
        	case 2:
        		o1.compareNames(o2.getParent().getParent());
        	default:
        		return 0;    	
        	}
    	case 1:
    		switch(o2.getLevel()) {
        	case 0:
        		return o1.getParent().compareNames(o2);
        	case 1:
        		if (o1.getParent().compareNames(o2.getParent()) == 0) {
                    return o1.compareNames(o2);
                } else {
                    return o1.getParent().compareNames(o2.getParent());
                }
        	case 2:
                if (o1.getParent().compareNames(o2.getParent().getParent()) == 0) {
                    return o1.compareNames(o2.getParent());
                } else {
                    return o1.getParent().compareNames(o2.getParent().getParent());
                }
        	default:
        		return 0;    	
        	}
    	case 2:
    		switch(o2.getLevel()) {
        	case 0:
        		return o1.getParent().getParent().compareNames(o2);
        	case 1:
        		if (o1.getParent().getParent().compareNames(o2.getParent()) == 0) {
                    return o1.getParent().compareNames(o2);
                } else {
                    return o1.getParent().getParent().compareNames(o2.getParent());
                }
        	case 2:
        		if (o1.getParent().getParent().compareNames(o2.getParent().getParent()) == 0) {
                    if (o1.getParent().compareNames(o2.getParent()) == 0) {
                        return o1.compareNames(o2);
                    } else {
                        return o1.getParent().compareNames(o2.getParent());
                    }
                } else {
                    return o1.getParent().getParent().compareNames(o2.getParent().getParent());
                }
        	default:
        		return 0;    	
        	}
    	default:
    		return 0;    	
    	}
    }

}
