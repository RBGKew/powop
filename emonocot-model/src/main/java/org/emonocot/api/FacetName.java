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
    CLASS,
    /**
     * Facet on the taxonomic family.
     */
    FAMILY,
    /**
     * Facet on the continent.
     */
    CONTINENT,
    /**
     * Facet on the source system.
     */
    AUTHORITY,
    /**
     * The rank of a plant name.
     */
    RANK,
    /**
     * The taxonomic status of the taxon.
     */
    TAXONOMIC_STATUS,
    /**
     * The date the taxon was published.
     */
    DATE_PUBLISHED,
    /**
     * The type of object annotated.
     */
    RECORD_TYPE,
    /**
     * The type of issue.
     */
    ISSUE_TYPE,
    /**
     * The error code.
     */
    ERROR_CODE,
    /**
     * The job id.
     */
    JOB_INSTANCE
}
