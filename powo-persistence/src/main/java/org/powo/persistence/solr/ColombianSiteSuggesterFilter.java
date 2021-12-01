package org.powo.persistence.solr;

/**
 * Used by ColPlantASite and ColFungiSite to create a Solr suggester filter that includes taxa
 * that are associated to the appropriate organisation (ColPlantA or ColFungi),
 * or are Plantae/Fungi found in Colombia.
 */
public class ColombianSiteSuggesterFilter {
    
    String filterQuery;

    public ColombianSiteSuggesterFilter(String organisationIdentifier, String kingdom, String location) {
        filterQuery = organisationIdentifier + " OR (" + kingdom + " AND distribution:" + location + ")";
    }

    @Override
    public String toString() {
        return filterQuery;
    }

}
