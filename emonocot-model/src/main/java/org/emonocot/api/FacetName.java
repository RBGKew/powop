package org.emonocot.api;

/**
 *
 * @author ben
 *
 */
public enum FacetName {
    /**
     * The type (class) of object.
     */
    CLASS(false),
    /**
     * Facet on the taxonomic family.
     */
    FAMILY(false),
    /**
     * Facet on the continent.
     */
    CONTINENT(true),
    /**
     * The region for a Taxon's distibution to include.
     */
    REGION(true),
    /**
     * Facet on the source system.
     */
    AUTHORITY(false),
    /**
     * The rank of a plant name.
     */
    RANK(false),
    /**
     * The taxonomic status of the taxon.
     */
    TAXONOMIC_STATUS(false),
    /**
     * The date the taxon was published.
     */
    DATE_PUBLISHED(false),
    /**
     * The type of object annotated.
     */
    RECORD_TYPE(false),
    /**
     * The type of issue.
     */
    ISSUE_TYPE(false),
    /**
     * The error code.
     */
    ERROR_CODE(false),
    /**
     * The job id.
     */
    JOB_INSTANCE(false);

    /**
     * @param multi is the facet multivalued
     */
    private FacetName(final boolean multi) {
        this.multivalued = multi;
    }

    /**
     * Is the facet multivalued?
     */
    private boolean multivalued;

    /**
     *
     * @return true if the facet is multivalued
     */
    public boolean isMultivalued() {
        return multivalued;
    }
}
