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
