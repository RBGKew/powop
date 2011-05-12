package org.openarchives.pmh;

/**
 *
 * @author ben
 *
 */
public enum Granularity {

    /**
     *
     */
    YYYY_MM_DD("YYYY-MM-DD"),
    /**
     *
     */
    YYYY_MM_DD_THH_MM_SS_Z("YYYY-MM-DDThh:mm:ssZ");

    /**
     *
     */
    private final String value;

    /**
     *
     * @param newValue Set the value of this term
     */
    Granularity(final String newValue) {
        value = newValue;
    }

    /**
     *
     * @return the value of this enumeration
     */
    public final String value() {
        return value;
    }

    /**
     *
     * @param value The string value to convert to a term
     * @return A term or an IllegalArgumentException if no terms match
     */
    public static Granularity fromValue(final String value) {
        for (Granularity c : Granularity.values()) {
            if (c.value.equals(value)) {
                return c;
            }
        }
        throw new IllegalArgumentException(value
                + " is not a valid value for Granularity");
    }

}

