package org.emonocot.api;

import java.util.List;
import java.util.Map;

import org.apache.solr.common.SolrDocument;
import org.emonocot.api.autocomplete.Match;
import org.emonocot.model.Base;
import org.emonocot.pager.Page;

/**
 * @author ben
 * @param <T>
 */
public interface SearchableService<T extends Base> extends Service<T> {
    /**
     * @param query
     *            A lucene query
     * @param spatialQuery
     *            A spatial query to filter the results by
     * @param pageSize
     *            The maximum number of results to return
     * @param pageNumber
     *            The offset (in pageSize chunks, 0-based) from the beginning of
     *            the recordset
     * @param facets
     *            The names of the facets you want to calculate
     * @param facetPrefixes TODO
     * @param selectedFacets
     *            A map of facets which you would like to restrict the search by
     * @param sort
     *            a parameter indicating how the results should be sorted
     * @param fetch
     *            Set the fetch profile
     * @return a Page from the resultset
     */
    Page<T> search(String query, String spatialQuery, Integer pageSize,
            Integer pageNumber, String[] facets,
            Map<String, String> facetPrefixes, Map<String, String> selectedFacets, String sort, String fetch);
    
    /**
     * 
     * @param query The query to autocomplete on
     * @param pageSize The number of matches to return
     * @param selectedFacets any restrictions on the search
     * @return a list of match objects
     */
    List<Match> autocomplete(String query, Integer pageSize, Map<String, String> selectedFacets);
    
    /**
     *
     * @param query
     * @param pageSize
     * @param pageNumber
     * @param selectedFacets
     * @param sort
     * @return
     */
    Page<SolrDocument> searchForDocuments(String query, Integer pageSize, Integer pageNumber, Map<String, String> selectedFacets, String sort);
    
    /**
     *
     * @param solrDocument
     * @return
     */
    T loadObjectForDocument(SolrDocument solrDocument);
}
