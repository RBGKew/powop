package org.emonocot.persistence.dao.hibernate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.Group;
import org.apache.solr.client.solrj.response.GroupCommand;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.params.FacetParams;
import org.emonocot.api.autocomplete.Match;
import org.emonocot.model.Base;
import org.emonocot.pager.CellSet;
import org.emonocot.pager.Cube;
import org.emonocot.pager.DefaultPageImpl;
import org.emonocot.pager.Level;
import org.emonocot.pager.Page;
import org.emonocot.persistence.dao.SearchableDao;
import org.hibernate.ObjectNotFoundException;
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
     * @throws SolrServerException 
     */
    public final Page<T> search(final String query, final String spatialQuery,
            final Integer pageSize, final Integer pageNumber,
            final String[] facets,
            Map<String, String> facetPrefixes, final Map<String, String> selectedFacets,
            final String sort, final String fetch) throws SolrServerException {
        SolrQuery solrQuery = prepareQuery(query, sort, pageSize, pageNumber, selectedFacets);
        solrQuery.set("spellcheck", "true");
        solrQuery.set("spellcheck.collate", "true");
        solrQuery.set("spellcheck.count", "1");
        
        
        // Filter the searchable objects out
        solrQuery.addFilterQuery("base.class_searchable_b:" + isSearchableObject());
        
        if (spatialQuery != null && spatialQuery.trim().length() != 0) {
                solrQuery.addFilterQuery("{!join to=taxon.distribution_ss from=location.tdwg_code_s}geo:\"" + spatialQuery + "\"");
        }

        if (facets != null && facets.length != 0) {
        	solrQuery.setFacet(true);
        	solrQuery.setFacetMinCount(1);
        	solrQuery.setFacetSort(FacetParams.FACET_SORT_INDEX);
        	for(String facet : facets) {
        		if(facet.endsWith("_dt")) {
        			/**
        			 * Is a date facet. Once Solr 4.2 is released, we can implement variable length buckets, but for now
        			 * stick with fixed buckets https://issues.apache.org/jira/browse/SOLR-2366
        			 */
        			
        			solrQuery.add("facet.range",facet);
        			solrQuery.add("f." + facet + ".facet.range.start","NOW/DAY-1YEARS");
        			solrQuery.add("f." + facet + ".facet.range.end","NOW/DAY");
        			solrQuery.add("f." + facet + ".facet.range.gap","+1MONTH");        			
        		} else {
                    solrQuery.addFacetField(facet);
        		}
        	}
            if(facetPrefixes != null) {
            	for(String facet : facetPrefixes.keySet()) {
            		solrQuery.add("f." + facet + ".facet.prefix",facetPrefixes.get(facet));
            	}
            }
        }
        
        QueryResponse queryResponse = solrServer.query(solrQuery);		
        
        List<T> results = new ArrayList<T>();
        for(SolrDocument solrDocument : queryResponse.getResults()) {
            T object = loadObjectForDocument(solrDocument);
            enableProfilePostQuery(object, fetch);
			results.add(object);
        }
        
        Long totalResults = new Long(queryResponse.getResults().getNumFound());
        Page<T> page = new DefaultPageImpl<T>(totalResults.intValue(), pageNumber, pageSize, results, queryResponse);
        if(selectedFacets != null) {
        	page.setSelectedFacets(selectedFacets);
        }
        page.setSort(sort);

        return page;
    }
    
    public List<Match> autocomplete(String query, Integer pageSize, Map<String, String> selectedFacets) throws SolrServerException {
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
        solrQuery.set("group","true");
        solrQuery.set("group.field", "autocomplete");
        
        QueryResponse queryResponse = solrServer.query(solrQuery);		
        
        List<Match> results = new ArrayList<Match>();
        Map<String,Match> matchMap = new HashMap<String,Match>();
        for(GroupCommand groupCommand : queryResponse.getGroupResponse().getValues()) {
			for (Group group : groupCommand.getValues()) {
				for (SolrDocument solrDocument : group.getResult()) {
					Match match = new Match();
					match.setLabel((String) solrDocument.get("autocomplete"));
					match.setValue((String) solrDocument.get("autocomplete"));
					matchMap.put((String) solrDocument.get("id"), match);
					results.add(match);
				}
			}
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

	@Override
    public Page<SolrDocument> searchForDocuments(String query, Integer pageSize, Integer pageNumber, Map<String, String> selectedFacets, String sort) throws SolrServerException {
        SolrQuery solrQuery = prepareQuery(query, sort, pageSize, pageNumber, selectedFacets);
        
        QueryResponse queryResponse = solrServer.query(solrQuery);		
		
		Long totalResults = new Long(queryResponse.getResults().getNumFound());
        Page<SolrDocument> page = new DefaultPageImpl<SolrDocument>(totalResults.intValue(), pageNumber, pageSize, queryResponse.getResults(), queryResponse);
        if(selectedFacets != null) {
        	page.setSelectedFacets(selectedFacets);
        }
        page.setSort(sort);
        
        return page;
	}


	@Override
    public T loadObjectForDocument(SolrDocument solrDocument) {
		try {
			Class clazz = Class.forName((String)solrDocument.getFieldValue("base.class_s"));
        	Long id = (Long) solrDocument.getFieldValue("base.id_l");
        	T t = (T) getSession().load(clazz, id);
        	t.getIdentifier();
        	return t;
		} catch (ClassNotFoundException cnfe) {
			throw new RuntimeException("Could not instantiate search result", cnfe);
		} catch (ObjectNotFoundException onfe) {
			return null;
		}
	}
	
    public CellSet analyse(String rows, String cols, Integer firstCol, Integer maxCols, Integer firstRow, Integer maxRows,	Map<String, String> selectedFacets, String[] facets, Cube cube) throws SolrServerException {
		SolrQuery query = new SolrQuery();
	    query.setQuery("*:*");
	    SolrQuery totalQuery = new SolrQuery();
	    totalQuery.setQuery("*:*");	    
	    
	    // We're not interested in the results themselves
	    query.setRows(1);
		query.setStart(0);
		totalQuery.setRows(1);
	    totalQuery.setStart(0);
		
		if(rows == null) {
		    query.setFacet(true);
            query.setFacetMinCount(1);
            query.setFacetSort(FacetParams.FACET_SORT_INDEX);
            query.addFacetField(cube.getDefaultLevel());
            if (maxRows != null) {
            	totalQuery.setFacet(true);
            	totalQuery.setFacetMinCount(1);
            	totalQuery.addFacetField("{!key=totalRows}" + cube.getDefaultLevel());
    			query.add("f." + cube.getDefaultLevel() + ".facet.limit", maxRows.toString()); 
    			query.add("f." + cube.getDefaultLevel() + ".facet.mincount", "1");
    			if (firstRow != null) {
    				query.add("f." + cube.getDefaultLevel() + ".facet.offset", firstRow.toString());
    			}
    		}
		} else if(cols == null) {
		    query.setFacet(true);
            query.setFacetMinCount(1);
            query.setFacetSort(FacetParams.FACET_SORT_INDEX);
            query.addFacetField(rows);
            if (maxRows != null) {
            	totalQuery.setFacet(true);
            	totalQuery.setFacetMinCount(1);
            	totalQuery.addFacetField("{!key=totalRows}"+rows);
    			query.add("f." + rows + ".facet.limit", maxRows.toString());
    			query.add("f." + rows + ".facet.mincount", "1");
    			if (firstRow != null) {
    				query.add("f." + rows + ".facet.offset", firstRow.toString());
    			}
    		}
            if(cube.getLevel(rows).isMultiValued() && cube.getLevel(rows).getHigher() != null) {
            	Level higher = cube.getLevel(rows).getHigher();
            	totalQuery.add("f." + rows + ".facet.prefix",selectedFacets.get(higher.getFacet()) + "_");
            	query.add("f." + rows + ".facet.prefix",selectedFacets.get(higher.getFacet()) + "_");
            }
		} else {
		    query.setFacet(true);
            query.setFacetMinCount(1);
            query.setFacetSort(FacetParams.FACET_SORT_INDEX);
            query.addFacetField(rows);
            if (maxRows != null) {
            	totalQuery.setFacet(true);
            	totalQuery.setFacetMinCount(1);
            	totalQuery.addFacetField("{!key=totalRows}"+rows);
    			query.add("f." + rows + ".facet.limit", maxRows.toString());
    			query.add("f." + rows + ".facet.mincount", "1");
    			if (firstRow != null) {
    				query.add("f." + rows + ".facet.offset", firstRow.toString());
    			}
    		}
            if(cube.getLevel(rows).isMultiValued() && cube.getLevel(rows).getHigher() != null) {
            	Level higher = cube.getLevel(rows).getHigher();
            	totalQuery.add("f." + rows + ".facet.prefix",selectedFacets.get(higher.getFacet()) + "_");
            	query.add("f." + rows + ".facet.prefix",selectedFacets.get(higher.getFacet()) + "_");
            }
            query.addFacetField(cols);
            if (maxCols != null) {
            	totalQuery.setFacet(true);
            	totalQuery.setFacetMinCount(1);
            	totalQuery.addFacetField("{!key=totalCols}"+cols);
            	/**
            	 * Facet pivot does not behave the same way on columns - the limit is 
            	 */
    			//query.add("f." + cols + ".facet.limit", maxCols.toString());
    			//query.add("f." + cols + ".facet.mincount", "1");
    			//if (firstCol != null) {
    			//	query.add("f." + cols + ".facet.offset", firstCol.toString());
    			//}
    		}
            if(cube.getLevel(cols).isMultiValued() && cube.getLevel(cols).getHigher() != null) {
            	Level higher = cube.getLevel(cols).getHigher();
            	totalQuery.add("f." + cols + ".facet.prefix",selectedFacets.get(higher.getFacet()) + "_");
            	query.add("f." + cols + ".facet.prefix",selectedFacets.get(higher.getFacet()) + "_");
            }
		    query.addFacetPivotField(rows + "," + cols);
		}
	    
	    if (selectedFacets != null && !selectedFacets.isEmpty()) {
            for (String facetName : selectedFacets.keySet()) {
            	totalQuery.addFilterQuery(facetName + ":" + selectedFacets.get(facetName));
                query.addFilterQuery(facetName + ":" + selectedFacets.get(facetName));  
            }
        }
        
        if (facets != null && facets.length != 0) {
            query.setFacet(true);
            query.setFacetMinCount(1);
            query.setFacetSort(FacetParams.FACET_SORT_INDEX);
            
            for (String facetName : facets) {
                if(rows != null && rows.equals(facetName)) {
                } else if(cols != null && cols.equals(facetName)) {
                } else if(rows == null && facetName.equals(cube.getDefaultLevel())) {
                } else {
            	  query.addFacetField(facetName);
            	}
            }
        }
	    

		QueryResponse response = solrServer.query(query);
		QueryResponse totalResponse = solrServer.query(totalQuery);			
		FacetField totalRows = null;
		FacetField totalCols = null;
		if (totalResponse.getFacetField("totalRows") != null) {
			totalRows = totalResponse.getFacetField("totalRows");
		}

		if (totalResponse.getFacetField("totalCols") != null) {
			totalCols = totalResponse.getFacetField("totalCols");
		}

		CellSet cellSet = new CellSet(response, selectedFacets, query,
				rows, cols, firstRow, maxRows, firstCol, maxCols,
				totalRows, totalCols, cube);

		return cellSet;
	}
	
	/**
	 * Prepares a {@link SolrQuery} with the parameters passed in 
	 * @param query
	 * @param sort
	 * @param pageSize
	 * @param pageNumber
	 * @param selectedFacets
	 * @return A {@link SolrQuery} that can be customised before passing to a {@link SolrServer}  
	 */
    protected SolrQuery prepareQuery(String query, String sort, Integer pageSize, Integer pageNumber, Map<String,String> selectedFacets){
        SolrQuery solrQuery = new SolrQuery();
        
        if (query != null && !query.trim().equals("")) {
            String searchString = null;
            if (query.indexOf(":") != -1) {
                searchString = query;
            } else {
                // replace spaces with '+' so that we search on terms
                searchString = query.trim().replace(" ", "+");
                solrQuery.set("defType","edismax");
                solrQuery.set("qf", "searchable.label_sort searchable.solrsummary_t");
            }
            solrQuery.setQuery(searchString);

        } else {
            solrQuery.setQuery("*:*");
        }
        
        if (sort != null && sort.length() != 0) {
            for(String singleSort : sort.split(",")) {
                if(singleSort.equals("_asc")) {
                    //Do nothing
                } else if(singleSort.endsWith("_asc")) { 
                    String sortField = singleSort.substring(0,singleSort.length() - 4);
                    solrQuery.addSortField(sortField, SolrQuery.ORDER.asc);
                } else if(singleSort.endsWith("_desc")) {
                    String sortField = singleSort.substring(0,singleSort.length() - 5);
                    solrQuery.addSortField(sortField, SolrQuery.ORDER.desc);
                }
            }
        }
        
        if (pageSize != null) {
            solrQuery.setRows(pageSize);
            if (pageNumber != null) {
                solrQuery.setStart(pageSize * pageNumber);
            }
        }
        
        if(selectedFacets != null && !selectedFacets.isEmpty()) {
            for(String facetName : selectedFacets.keySet()) {
                solrQuery.addFilterQuery(facetName + ":" + selectedFacets.get(facetName));
            }
        }
        
	    return solrQuery;
	}
    
}
