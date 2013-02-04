package org.emonocot.model.compare;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.emonocot.model.constants.Location;
import org.emonocot.model.compare.LocationComparator;
import org.junit.Test;

/**
 *
 * @author ben
 *
 */
public class LocationComparatorTest {

    /**
     *
     */
    private LocationComparator comparator = new LocationComparator();

    /**
     *
     */
    @Test
    public final void testCompareGeography() {
        List<Location> list = new ArrayList<Location>();
        list.add(Location.EUROPE);
        list.add(Location.AFRICA);
        list.add(Location.CHINA);
        list.add(Location.MACARONESIA);
        list.add(Location.EASTERN_CANADA);
        list.add(Location.FRA);
        list.add(Location.ABT);
        list.add(Location.GRB);
        list.add(Location.IRE);
        list.add(Location.ALG);

        Collections.sort(list, comparator);
        assertEquals("[AFRICA, AFRICA_MACARONESIA, AFRICA_NORTHERN_AFRICA_ALG, ASIA_TEMPERATE_CHINA, EUROPE, EUROPE_NORTHERN_EUROPE_GRB, EUROPE_NORTHERN_EUROPE_IRE, EUROPE_SOUTHWESTERN_EUROPE_FRA, NORTHERN_AMERICA_EASTERN_CANADA, NORTHERN_AMERICA_WESTERN_CANADA_ABT]",
                list.toString());
    }

}
