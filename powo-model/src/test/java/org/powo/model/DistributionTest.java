package org.powo.model;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.Test;
import org.powo.model.constants.Location;

public class DistributionTest {

    @Test
    public void testGetLocationTree() {
        var distribution = new Distribution();
        distribution.setLocation(Location.CLM);
        assertEquals(Set.of(Location.SOUTHERN_AMERICA, Location.WESTERN_SOUTH_AMERICA, Location.CLM, Location.CLM_OO),
                distribution.getLocationTree());
    }

}
