package org.openarchives.pmh;

/**
 *
 * @author ben
 *
 */
public enum DeletedRecord {

    /**
     *
     */
    NO("no"),
    /**
     *
     */
    PERSISTENT("persistent"),
    /**
     *
     */
    TRANSIENT("transient");

    /**
     *
     */
    private final String value;

    /**
     *
     * @param newValue Set the value of the enum
     */
    DeletedRecord(final String newValue) {
        value = newValue;
    }

    /**
     *
     * @return the value of the enum
     */
    public String value() {
        return value;
    }

    /**
     *
     * @param value The value which should be converted into a deleted record
     * @return a deleted record.
     */
    public static DeletedRecord fromValue(final String value) {
        for (DeletedRecord c : DeletedRecord.values()) {
            if (c.value.equals(value)) {
                return c;
            }
        }
        throw new IllegalArgumentException(value);
    }
}
