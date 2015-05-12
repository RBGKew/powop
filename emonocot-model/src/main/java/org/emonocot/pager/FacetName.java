/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
package org.emonocot.pager;

/**
 *
 * @author ben
 *
 */
public enum FacetName {

    CLASS("base.class_s", false),
    ORDER("taxon.order_s", false),
    FAMILY("taxon.family_ss", false),
    SUBFAMILY("taxon.subfamily_ss", true),
    TRIBE("taxon.tribe_ss", true),
    SUBTRIBE("taxon.subtribe_ss", true),
    GENUS("taxon.genus_ss", false),
    SPECIES("taxon.specific_epithet_s", false),
    CONTINENT("taxon.distribution_TDWG_0_ss", false),
    REGION("taxon.distribution_TDWG_1_ss", false, CONTINENT),
    SOURCE("searchable.sources_ss", false),
    AUTHORITY("base.authority_s", false),
    RANK("taxon.taxon_rank_s", false),
    TAXONOMIC_STATUS("taxon.taxonomic_status_s", false),
    CONSERVATION_STATUS("taxon.measurement_or_fact_threatStatus_txt", false),
    LIFE_FORM("taxon.measurement_or_fact_Lifeform_txt", false),
    HABITAT("taxon.measurement_or_fact_Habitat_txt", false),
    NAME_PUBLISHED_IN_YEAR("taxon.name_published_in_year_i", false),
    RECORD_TYPE("annotation.record_type_s", false),
    TYPE("annotation.type_s", false),
    CODE("annotation.code_s", false),
    JOB_ID("annotation.job_id_l", false),
    EXIT_CODE("resource.exit_code_s", false),
    RESOURCE_TYPE("resource.resource_type_s", false),
    SCHEDULED("resource.scheduled_b", false),
    SCHEDULING_PERIOD("resource.scheduling_period_s", false),
    RESOURCE_STATUS("resource.status_s", false),
    RESOURCE_ORGANISATION("resource.organisation_s", false),
    LAST_HARVESTED("resource.last_harvested_dt", false),
    COMMENT_SUBJECT("comment.subject_s", false),
    COMMENT_PAGE_TYPE("comment.comment_page_class_s", false);
    
    public static final FacetName[] taxonomyFacets = {ORDER, FAMILY, SUBFAMILY, TRIBE, SUBTRIBE, GENUS}; 

    private FacetName(String solrField, boolean includeMissing) {
        this.solrField = solrField;
        this.includeMissing = includeMissing;
    }
    
    private FacetName(String solrField, boolean includeMissing, FacetName parent) {
        this.solrField = solrField;
        this.includeMissing = includeMissing;
        this.parent = parent;
        this.parent.child = this;
    }

    private String solrField;
    
    private boolean includeMissing;
    
    private FacetName parent;
    
    private FacetName child;
    
	/**
     * @return the solrField
     */
    public String getSolrField() {
        return solrField;
    }

    /**
     * @return the includeMissing
     */
    public boolean isIncludeMissing() {
        return includeMissing;
    }

    public static FacetName fromString(String string) {
		for(FacetName facetName : FacetName.values()) {
			if(facetName.solrField.equals(string)) {
				return facetName;
			}
		}
		throw new IllegalArgumentException(string + " is not a valid value for a facet");
	}

	public FacetName getChild() {
		return child;
	}

}
