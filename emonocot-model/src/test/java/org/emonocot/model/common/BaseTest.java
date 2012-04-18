package org.emonocot.model.common;

import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author ben
 *
 */
public class BaseTest {

    /**
     *
     */
    private Base b1;

    /**
     *
     */
    private Base b2;

    /**
     *
     */
    @Before
    public final void setUp() {
        b1 = new Annotation();
        b2 = new Annotation();
    }

    /**
     * Root Cause of http://build.e-monocot.org/bugzilla/show_bug.cgi?id=262
     * Unexpected Taxon Exception in DwC Harvesting even though the taxon is
     * expected. If both identifiers are null (and if both primary keys are
     * null) equals is false.
     */
    @Test
    public final void testEqualsWithNullIdentifiers() {
        assertFalse("Equals should return false", b1.equals(b2));
    }

}
