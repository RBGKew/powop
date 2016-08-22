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
package org.emonocot.persistence.dao.hibernate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.charfilter.HTMLStripCharFilter;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SuggesterResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.params.FacetParams;
import org.emonocot.model.Base;
import org.emonocot.pager.CellSet;
import org.emonocot.pager.Cube;
import org.emonocot.pager.DefaultPageImpl;
import org.emonocot.pager.FacetName;
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

	private SolrClient solrClient = null;

	@Autowired
	public void setSolrServer(SolrClient solrClient) {
		this.solrClient = solrClient;
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


	public final QueryResponse search(SolrQuery solrQuery){ 
		QueryResponse queryResponse = null;
		if(solrQuery != null){
		try {
			queryResponse = solrClient.query(solrQuery);
		} catch (IOException e) {
			logger.error("Error querying solr server: ", e);
			return null;
			
		} catch (SolrServerException e) {
			logger.error("Error querying solr server: ", e);
			return null;	
		}
		
		}
		return queryResponse;
	}
	
	public final Page<T> search(SolrQuery solrQuery, String fetch) throws SolrServerException, IOException {
		QueryResponse queryResponse;
		try {
			queryResponse = solrClient.query(solrQuery);
		} catch (IOException e) {
			logger.error("Error querying solr server: ", e);
			throw new SolrServerException(e);
		}
		List<T> results = new ArrayList<T>();
		for(SolrDocument solrDocument : queryResponse.getResults()) {
			T object = loadObjectForDocument(solrDocument);
			enableProfilePostQuery(object, fetch);
			results.add(object);
		}

		Integer pageSize = solrQuery.getRows();
		Integer pageNumber = solrQuery.getStart() / pageSize ;
		Long totalResults = new Long(queryResponse.getResults().getNumFound());
		
		Page<T> page = new DefaultPageImpl<T>(totalResults.intValue(), pageNumber, pageSize, results, queryResponse);
		
		page.setSort(solrQuery.getSortField());

		return page;
	}

	private void includeMissing(SolrQuery solrQuery, String facet) {
		try {
			FacetName fn = FacetName.fromString(facet);
			if(fn != null && fn.isIncludeMissing()) {
				solrQuery.set("f." + fn.getSolrField() + ".facet.missing", true);
			}
		} catch (IllegalArgumentException e) {
			logger.debug("Unable to find a facet for " + facet);
		}
	}

	public SuggesterResponse autocomplete(SolrQuery query) throws SolrServerException, IOException{
		QueryResponse queryResponse = solrClient.query(query);
		return queryResponse.getSuggesterResponse();
	}

	private String filter(String value) {
		StringBuilder out = new StringBuilder();
		StringReader strReader = new StringReader(value);
		try {
			HTMLStripCharFilter html = new HTMLStripCharFilter(new BufferedReader(strReader));
			char[] cbuf = new char[1024 * 10];
			while (true) {
				int count = html.read(cbuf);
				if (count == -1)
					break; // end of stream mark is -1
				if (count > 0)
					out.append(cbuf, 0, count);
			}
			html.close();
		} catch (IOException e) {
			throw new RuntimeException("Failed stripping HTML for value: "
					+ value, e);
		}
		return out.toString();
	}

	@Override
	public Page<SolrDocument> searchForDocuments(SolrQuery solrQuery) throws SolrServerException, IOException {
		QueryResponse queryResponse = solrClient.query(solrQuery);
		Integer pageSize = solrQuery.getRows();
		Integer pageNumber = solrQuery.getStart() / pageSize ;
		Long totalResults = new Long(queryResponse.getResults().getNumFound());
		Page<SolrDocument> page = new DefaultPageImpl<SolrDocument>(totalResults.intValue(), pageNumber, pageSize, queryResponse.getResults(), queryResponse);

		page.setSort(solrQuery.getSortField());

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

	public CellSet analyse(String rows, String cols, Integer firstCol, Integer maxCols, Integer firstRow, Integer maxRows,	Map<String, String> selectedFacets, String[] facets, Cube cube) throws SolrServerException, IOException {
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
			includeMissing(query,cube.getDefaultLevel());
			includeMissing(totalQuery,cube.getDefaultLevel());
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
			includeMissing(query,rows);
			includeMissing(totalQuery,rows);
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
			includeMissing(query,rows);
			includeMissing(totalQuery,rows);
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
			includeMissing(query,cols);
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
				String facetValue = selectedFacets.get(facetName);
				if(StringUtils.isNotEmpty(facetValue)) {
					totalQuery.addFilterQuery(facetName + ":" + selectedFacets.get(facetName));
					query.addFilterQuery(facetName + ":" + selectedFacets.get(facetName));
				} else {//Subtract/Exclude documents with any value for the facet
					totalQuery.addFilterQuery("-" + facetName + ":[* TO *]");
					query.addFilterQuery("-" + facetName + ":[* TO *]");
				}
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
					includeMissing(query,facetName);
					query.addFacetField(facetName);
				}
			}
		}

		QueryResponse response = solrClient.query(query);
		QueryResponse totalResponse = solrClient.query(totalQuery);
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



}
