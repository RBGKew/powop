package org.emonocot.model.geography;

/**
 *
 * @author ben
 *
 */
public enum Continent {
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
}
