package org.emonocot.checklist.model;

/**
 *
 * @author jk00kg
 *
 */
public enum Rank {
    /**
     *
     */
    FAMILY ("fam", "Family"),
    /**
     *
     */
    GENUS ("gen", "Genus"),
    /**
     *
     */
    INFRASPECIFIC ("infrasp", "Infraspecies"),
    /**
     *
     */
    SPECIES ("sp", "Species"),
    /**
     *
     */
    SUBSPECIES ("ssp", "Subspecies"),
    /**
     *
     */
    VARIETY ("var", "Variety"),
    /**
     *
     */
    UNKNOWN ("?", "Unknown");

    /**
     *
     */
    private String abbreviation;

    /**
     *
     */
    private String label;

    /**
     *
     * @param newAbbreviation Set the abbreviation of the rank
     * @param newLabel Set the label of the rank
     */
    private Rank(final String newAbbreviation,
            final String newLabel) {
        this.abbreviation = newAbbreviation;
        this.label = newLabel;
    }

    /**
     * @return the abbreviation
     */
    public String getAbbreviation() {
        return abbreviation;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }
}
