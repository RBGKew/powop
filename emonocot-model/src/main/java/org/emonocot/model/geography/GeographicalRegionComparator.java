package org.emonocot.model.geography;

import java.util.Comparator;

/**
 *
 * @author ben
 *
 */
public class GeographicalRegionComparator implements
        Comparator<GeographicalRegion> {

    /**
     * @param o1 Set the first region
     * @param o2 Set the second region
     * @return -1 if o1 comes before o2, 1 if o1 comes after o2 and 0 if the two
     *         regions are equal
     */
    public final int compare(final GeographicalRegion o1,
            final GeographicalRegion o2) {
        if (o1.getClass().equals(Continent.class)) {
            Continent c1 = (Continent) o1;
            if (o2.getClass().equals(Continent.class)) {
                Continent c2 = (Continent) o2;
                return c1.compareNames(c2);
            } else if (o2.getClass().equals(Region.class)) {
                Region r2 = (Region) o2;
                return c1.compareNames(r2.getContinent());
            } else {
                Country c2 = (Country) o2;
                return c1.compareNames(c2.getRegion().getContinent());
            }
        } else if (o1.getClass().equals(Region.class)) {
            Region r1 = (Region) o1;
            if (o2.getClass().equals(Continent.class)) {
                Continent c2 = (Continent) o2;
                return r1.getContinent().compareNames(c2);
            } else if (o2.getClass().equals(Region.class)) {
                Region r2 = (Region) o2;
                if (r1.getContinent().compareNames(r2.getContinent()) == 0) {
                    return r1.compareNames(r2);
                } else {
                    return r1.getContinent().compareNames(r2.getContinent());
                }
            } else {
                Country c2 = (Country) o2;
                if (r1.getContinent().compareNames(c2.getRegion().getContinent()) == 0) {
                    return r1.compareNames(c2.getRegion());
                } else {
                    return r1.getContinent().compareNames(
                            c2.getRegion().getContinent());
                }
            }
        } else {
            Country c1 = (Country) o1;
            if (o2.getClass().equals(Continent.class)) {
                Continent c2 = (Continent) o2;
                return c1.getRegion().getContinent().compareNames(c2);
            } else if (o2.getClass().equals(Region.class)) {
                Region r2 = (Region) o2;
                if (c1.getRegion().getContinent().compareNames(r2.getContinent()) == 0) {
                    return c1.getRegion().compareNames(r2);
                } else {
                    return c1.getRegion().getContinent()
                            .compareNames(r2.getContinent());
                }
            } else {
                Country c2 = (Country) o2;
                if (c1.getRegion().getContinent()
                        .compareNames(c2.getRegion().getContinent()) == 0) {
                    if (c1.getRegion().compareNames(c2.getRegion()) == 0) {
                        return c1.compareNames(c2);
                    } else {
                        return c1.getRegion().compareNames(c2.getRegion());
                    }
                } else {
                    return c1.getRegion().getContinent()
                            .compareNames(c2.getRegion().getContinent());
                }
            }
        }
    }

}
