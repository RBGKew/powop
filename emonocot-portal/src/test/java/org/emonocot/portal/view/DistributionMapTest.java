package org.emonocot.portal.view;

import static org.junit.Assert.assertEquals;

import org.emonocot.model.Distribution;
import org.emonocot.model.Taxon;
import org.emonocot.model.geography.Country;
import org.emonocot.model.geography.GeographicalRegion;
import org.junit.Test;

/**
 *
 * @author ben
 *
 */
public class DistributionMapTest {

    /**
     *
     */
    @Test
    public final void testMapUrlConstruction() {
        Taxon taxon = new Taxon();
        createDistribution(taxon, Country.REU);

        assertEquals("The map service url fragment should be correct",
                "tdwg3:present:REU",
                Functions.map(taxon));
    }

    /**
     *
     * @param taxon
     *            Set the taxon
     * @param region
     *            Set the Geographical Region
     */
    private void createDistribution(final Taxon taxon,
                                    final GeographicalRegion region) {
        Distribution distribution = new Distribution();
        distribution.setTaxon(taxon);
        distribution.setLocation(region);
        taxon.getDistribution().add(distribution);
    }

}
