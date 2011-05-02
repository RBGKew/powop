package org.emonocot.job.scratchpads.model;

/**
 *
 * @author ben
 *
 */
public enum DataObjectType {
    /**
     *
     */
    TEXT("http://purl.org/dc/dcmitype/Text"),
    /**
     *
     */
    STILL_IMAGE("http://purl.org/dc/dcmitype/StillImage"),
    /**
     *
     */
    MOVING_IMAGE("http://purl.org/dc/dcmitype/MovingImage"),
    /**
     *
     */
    SOUND("http://purl.org/dc/dcmitype/Sound");

    /**
     *
     */
    private String uri;

    /**
     *
     * @param newUri The uri of this data object type.
     */
    private DataObjectType(final String newUri) {
        this.uri = newUri;
    }

    @Override
    public String toString() {
        return uri;
    }

    /**
     *
     * @param value The string value to convert
     * @return A data object type
     */
    public static DataObjectType fromString(final String value) {
        for (DataObjectType d : DataObjectType.values()) {
            if (d.uri.equals(value)) {
                return d;
            }
        }

        throw new IllegalArgumentException(value
                + " is not a valid string representation of DataObjectType");
    }
}
