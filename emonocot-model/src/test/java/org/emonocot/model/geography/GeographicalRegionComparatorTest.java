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
public class GeographicalRegionComparatorTest {

    /**
     *
     */
    private GeographicalRegionComparator comparator
        = new GeographicalRegionComparator();

    /**
     *
     */
    @Test
    public final void testCompareGeography() {
        List<GeographicalRegion> list = new ArrayList<GeographicalRegion>();
        list.add(Continent.EUROPE);
        list.add(Continent.AFRICA);
        list.add(Region.CHINA);
        list.add(Region.MACRONESIA);
        list.add(Region.EASTERN_CANADA);
        list.add(Country.FRA);
        list.add(Country.ABT);
        list.add(Country.GRB);
        list.add(Country.IRE);
        list.add(Country.ALG);

        Collections.sort(list, comparator);
        assertEquals("[2, 21, ALG, 36, 1, GRB, IRE, FRA, 72, ABT]",
                list.toString());
    }

}
