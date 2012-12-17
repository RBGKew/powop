package org.emonocot.persistence.dao.hibernate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.params.FacetParams;
import org.emonocot.api.autocomplete.Match;
import org.emonocot.model.Base;
import org.emonocot.model.Taxon;
import org.emonocot.pager.DefaultPageImpl;
import org.emonocot.pager.Page;
import org.emonocot.persistence.dao.SearchableDao;
import org.hibernate.Criteria;
import org.hibernate.criterion.Example;
import org.springframework.beans.factory.annotation.Autowired;


/**
 *
 * @author ben
 *
 * @param <T>
 */
public abstract class SearchableDaoImpl<T extends Base> extends DaoImpl<T>
        implements SearchableDao<T> {
    
	private SolrServer solrServer = null;
	
	@Autowired	
	public void setSolrServer(SolrServer solrServer) {
		this.solrServer = solrServer;
	}
	
	/**
	 * Does this DAO search for SearchableObjects?
	 * @return
	 */
	protected boolean isSearchableObject() {
		return true;
	}

    /**
     *
     * @param newType
     *            Set the type of object handled by this class
     * @param searchTypes
     *            Set the subclasses of T to be searched for
     */
    public SearchableDaoImpl(final Class<T> newType) {
        super(newType);
    }

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
     * @param selectedFacets
     *            A map of facets which you would like to restrict the search by
     * @param sort
     *            A representation for the order results should be returned in
     * @param fetch
     *            Set the fetch profile
     * @return a Page from the resultset
     */
    public final Page<T> search(final String query, final String spatialQuery,
            final Integer pageSize, final Integer pageNumber,
            final String[] facets,
            final Map<String, String> selectedFacets, final String sort,
            final String fetch) {
        SolrQuery solrQuery = new SolrQuery();        

        if (query != null && !query.trim().equals("")) {
        	String searchString = null;
            if (query.indexOf(":") != -1) {
                searchString = query;
            } else {
            	// replace spaces with '+' so that we search on terms
                searchString = query.trim().replace(" ", "+");
                solrQuery.set("defType","edismax");
                solrQuery.set("qf", "searchable.solrsummary_t");
                solrQuery.set("pf", "searchable.label_sort^100");
            }
            solrQuery.setQuery(searchString);

        } else {
            solrQuery.setQuery("*:*");
        }
        
        // Filter the searchable objects out
        solrQuery.addFilterQuery("base.class_searchable_b:" + isSearchableObject());
        
        if (spatialQuery != null && spatialQuery.trim().length() != 0) {
                solrQuery.addFilterQuery("{!join to=taxon.distribution_ss from=location.tdwg_code_s}geo:\"" + spatialQuery + "\"");
        }

        // Set additional result parameters
        if (pageSize != null) {
            solrQuery.setRows(pageSize);
            if (pageNumber != null) {
                solrQuery.setStart(pageSize * pageNumber);
            }
        }
        
        if (facets != null && facets.length != 0) {
        	solrQuery.setFacet(true);
        	solrQuery.setFacetMinCount(1);
        	solrQuery.setFacetSort(FacetParams.FACET_SORT_INDEX);
            solrQuery.addFacetField(facets);
        }

        if (sort != null && sort.length() != 0) {
            if(sort.equals("_asc")) {
            	
            } else if(sort.endsWith("_asc")) { 
                String sortField = sort.substring(0,sort.length() - 4);
                solrQuery.addSortField(sortField, SolrQuery.ORDER.asc);
            } else if(sort.endsWith("_desc")) {
            	String sortField = sort.substring(0,sort.length() - 5);
                solrQuery.addSortField(sortField, SolrQuery.ORDER.desc);
            }
        }
        
        if(selectedFacets != null && !selectedFacets.isEmpty()) {
        	for(String facetName : selectedFacets.keySet()) {
        		solrQuery.addFilterQuery(facetName + ":" + selectedFacets.get(facetName));
        	}
        }
        
        QueryResponse queryResponse = null;
		try {
			queryResponse = solrServer.query(solrQuery);
		} catch (SolrServerException sse) {
			throw new RuntimeException("Exception querying solr server",sse);
		}
        
        List<T> results = new ArrayList<T>();
        for(SolrDocument solrDocument : queryResponse.getResults()) {
			try {
				Class clazz = Class.forName((String)solrDocument.getFieldValue("base.class_s"));
	        	Long id = (Long)solrDocument.getFieldValue("base.id_l");
	        	T t = (T)getSession().load(clazz, id);
	        	t.getIdentifier();
	        	results.add(t);
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("Could not instantiate search result", cnfe);
			}
        }
        
        Long totalResults = new Long(queryResponse.getResults().getNumFound());
        Page<T> page = new DefaultPageImpl<T>(totalResults.intValue(), pageNumber, pageSize, results, queryResponse);
        if(selectedFacets != null) {
        	page.setSelectedFacets(selectedFacets);
        }
        page.setSort(sort);

        return page;
    }
    
    public List<Match> autocomplete(String query, Integer pageSize, Map<String, String> selectedFacets) {
    	SolrQuery solrQuery = new SolrQuery();        

        if (query != null && !query.trim().equals("")) {
        	String searchString = query.trim().replace(" ", "+");            
            solrQuery.setQuery(searchString);
        } else {
            return new ArrayList<Match>();
        }
        
        // Filter the searchable objects out
        solrQuery.addFilterQuery("base.class_searchable_b:" + isSearchableObject());

        // Set additional result parameters
        solrQuery.setRows(pageSize);
        
        if(selectedFacets != null && !selectedFacets.isEmpty()) {
        	for(String facetName : selectedFacets.keySet()) {
        		solrQuery.addFilterQuery(facetName + ":" + selectedFacets.get(facetName));
        	}
        }
        
        solrQuery.set("defType","edismax");
        solrQuery.set("qf", "autocomplete^3 autocompleteng");
        solrQuery.set("pf", "autocompletenge");
        solrQuery.set("fl","autocomplete,id");
        solrQuery.setHighlight(true);
        solrQuery.set("hl.fl", "autocomplete");
        solrQuery.set("hl.snippets",3);
        solrQuery.setHighlightSimplePre("<b>");
        solrQuery.setHighlightSimplePost("</b>");       
        
        QueryResponse queryResponse = null;
		try {
			queryResponse = solrServer.query(solrQuery);
		} catch (SolrServerException sse) {
			throw new RuntimeException("Exception querying solr server",sse);
		}
        
        List<Match> results = new ArrayList<Match>();
        Map<String,Match> matchMap = new HashMap<String,Match>();
        for(SolrDocument solrDocument : queryResponse.getResults()) {
        	Match match = new Match();
        	match.setLabel((String)solrDocument.get("autocomplete"));
        	match.setValue((String)solrDocument.get("autocomplete"));
        	matchMap.put((String)solrDocument.get("id"), match);
        	results.add(match);
        }
        for(String documentId : matchMap.keySet()) {
        	if(queryResponse.getHighlighting().containsKey(documentId)) {
        		Map<String, List<String>> highlightedTerms = queryResponse.getHighlighting().get(documentId);
        		if(highlightedTerms.containsKey("autocomplete")) {
        		    matchMap.get(documentId).setLabel(highlightedTerms.get("autocomplete").get(0));
        		}
        	} 
        }

        return results;
    }

	/* (non-Javadoc)
	 * @see org.emonocot.persistence.dao.SearchableDao#searchByExample(org.emonocot.model.Base, boolean, boolean)
	 */
	@Override
	public Page<T> searchByExample(T example, boolean ignoreCase,
			boolean useLike) {
		Example criterion = Example.create(example);
        if(ignoreCase) {
            criterion.ignoreCase();
        }
        if(useLike) {
            criterion.enableLike();
        }
        Criteria criteria = getSession().createCriteria(Taxon.class);
        criteria.add(criterion);
        List<T> results = (List<T>) criteria.list();
        Page<T> page = new DefaultPageImpl<T>(results.size(), null, null, results, null);
        return page;
	}


}
