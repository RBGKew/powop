package org.emonocot.model.geography;

import org.apache.lucene.spatial.base.shape.Shape;

/**
 *
 * @author ben
 *
 */
public enum Continent implements GeographicalRegion {
    /**
     *
     */
    EUROPE(1, "Europe"),
    /**
     *
     */
    AFRICA(2, "Africa"),
    /**
     *
     */
    ASIA_TEMPERATE(3, "Asia-Temperate"),
    /**
     *
     */
    ASIA_TROPICAL(4, "Asia-Tropical"),
    /**
     *
     */
    AUSTRALASIA(5, "Australasia"),
    /**
     *
     */
    PACIFIC(6, "Pacific"),
    /**
     *
     */
    NORTHERN_AMERICA(7, "Northern America"),
    /**
     *
     */
    SOUTHERN_AMERICA(8, "Southern America"),
    /**
     *
     */
    ANTARCTICA(9, "Antarctic");

    /**
     * The TDWG Code.
     */
    private int code;

    /**
     * The human-readable name.
     */
    private String name;

    /**
     * The geographic region as a Shape.
     */
    private Shape shape;

    /**
     *
     * @param newCode Set the code of this continent
     * @param newName Set the name of this continent
     */
    private Continent(final int newCode, final String newName) {
        this.code = newCode;
        this.name = newName;
    }

    /**
     *
     * @return The TDWG code of this continent.
     */
    public int getCode() {
        return code;
    }

    /**
     *
     * @return The human-readable name of this continent.
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param code the code of the continent
     * @return a valid continent
     */
    public static Continent fromString(final String code) {
        int c = Integer.parseInt(code);
        for (Continent continent : Continent.values()) {
            if (continent.code == c) {
                return continent;
            }
        }
        throw new IllegalArgumentException(code
                + " is not a valid Continent code");
    }

    @Override
    public String toString() {
        return Integer.toString(code);
    }

    @Override
    public Shape getShape() {
        return shape;
    }

    @Override
    public void setShape(final Shape newShape) {
        this.shape = newShape;
    }
}
