package org.emonocot.pager;

/**
 *
 * @author ben
 *
 */
public enum FacetName {

    CLASS("base.class_s"),    
    FAMILY("taxon.family_s"),
    CONTINENT("taxon.distribution_TDWG_0_ss"),
    REGION("taxon.distribution_TDWG_1_ss"),
    SOURCE("searchable.sources_ss"),
    AUTHORITY("base.authority_s"),
    RANK("taxon.taxon_rank_s"),
    TAXONOMIC_STATUS("taxon.taxonomic_status_s"),
    CONSERVATION_STATUS("taxon.measurement_or_fact_IUCNConservationStatus_txt"),
    LIFE_FORM("taxon.measurement_or_fact_Lifeform_txt"),
    HABITAT("taxon.measurement_or_fact_Habitat_txt"),
    NAME_PUBLISHED_IN_YEAR("taxon.name_published_in_year_i"),
    RECORD_TYPE("annotation.record_type_s"),
    TYPE("annotation.type_s"),
    CODE("annotation.code_s"),
    JOB_ID("annotation.job_id_l"),
    EXIT_CODE("resource.exit_code_s"),
    RESOURCE_TYPE("resource.resource_type_s"),
    SCHEDULED("resource.scheduled_b"),
    SCHEDULING_PERIOD("resource.scheduling_period_s"),
    RESOURCE_STATUS("resource.status_s"),
    RESOURCE_ORGANISATION("resource.organisation_s"),
    LAST_HARVESTED("resource.last_harvested_dt"),
    COMMENT_SUBJECT("comment.subject_s"),
    COMMENT_PAGE_TYPE("comment.comment_page_class_s");

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
