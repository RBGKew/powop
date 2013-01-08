package org.emonocot.model.geography;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        assertEquals("[AFRICA, MACARONESIA, ALG, CHINA, EUROPE, GRB, IRE, FRA, EASTERN_CANADA, ABT]",
                list.toString());
    }

}
