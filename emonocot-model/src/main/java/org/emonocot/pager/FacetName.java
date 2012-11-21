package org.emonocot.pager;

/**
 *
 * @author ben
 *
 */
public enum FacetName {
    /**
     * The type (class) of object.
     */
    CLASS("base.class_s"),
    /**
     * Facet on the taxonomic family.
     */
    FAMILY("taxon.family_s"),
    /**
     * Facet on the continent.
     */
    CONTINENT("taxon.distribution_TDWG_0_ss"),
    /**
     * The region for a Taxon's distibution to include.
     */
    REGION("taxon.distribution_TDWG_1_ss"),
    /**
     * Facet on the source system.
     */
    AUTHORITY("base.authority_s"),
    /**
     * The rank of a plant name.
     */
    RANK("taxon.taxon_rank_s"),
    /**
     * The taxonomic status of the taxon.
     */
    TAXONOMIC_STATUS("taxon.taxonomic_status_s"),
    /**
     * The date the taxon was published.
     */
    NAME_PUBLISHED_IN_YEAR("taxon.name_published_in_year_i"),
    /**
     * The type of object annotated.
     */
    RECORD_TYPE("annotation.record_type_s"),
    /**
     * The type of issue.
     */
    TYPE("annotation.type_s"),
    /**
     * The error code.
     */
    CODE("annotation.code_s"),
    /**
     * The job id.
     */
    JOB_ID("annotation.job_id_l");

    private FacetName(String solrField) {
        this.solrField = solrField;
    }

    private String solrField;

	public static FacetName fromString(String string) {
		for(FacetName facetName : FacetName.values()) {
			if(facetName.solrField.equals(string)) {
				return facetName;
			}
		}
		throw new IllegalArgumentException(string + " is not a valid value for a facet");
	}

}
