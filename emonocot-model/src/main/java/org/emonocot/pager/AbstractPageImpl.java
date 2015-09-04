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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.RangeFacet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author ben
 *
 * @param <T>
 */
public abstract class AbstractPageImpl<T> implements Page<T>, Serializable {

	private static long serialVersionUID = 3235700796905201107L;

	protected static Integer MAX_PAGE_LABELS = 3;

	protected static String LABEL_DIVIDER = " - ";

	private static Logger logger = LoggerFactory
			.getLogger(AbstractPageImpl.class);

	private Map<String, Object> parameters = new HashMap<String, Object>();

	private Integer pagesAvailable;

	private Integer prevIndex;

	private Integer nextIndex;

	private Integer currentIndex;

	private Integer firstRecord;

	private Integer lastRecord;

	private Integer size;

	private List<T> records;

	private Integer pageSize;

	private QueryResponse queryResponse = null;

	private Map<Integer, String> pageNumbers;

	private ArrayList<Integer> indices;

	private Map<String, String> selectedFacets
	= new HashMap<String, String>();

	private String sort;

	private String suggestedSpelling = null;

	private boolean correctlySpelled = true;

	/**
	 * Constructor.
	 *
	 * @param newCurrentIndex
	 *            the page of this result set (0-based), can be null
	 * @param count
	 *            the total number of results available for this query
	 * @param newPageSize
	 *            The size of pages (can be null if all results should be
	 *            returned if available)
	 * @param newRecords
	 *            A list of objects in this page (can be empty if there were no
	 *            results)
	 */
	public AbstractPageImpl(Integer count, Integer newCurrentIndex,
			Integer newPageSize, List<T> newRecords, QueryResponse queryResponse) {
		if (newCurrentIndex != null) {
			this.currentIndex = newCurrentIndex;
		} else {
			this.currentIndex = 0;
		}

		this.queryResponse = queryResponse;
		this.pageSize = newPageSize;
		this.pageNumbers = new HashMap<Integer, String>();
		indices = new ArrayList<Integer>();
		if (count == 0) {
			pagesAvailable = 1;
		} else if (newPageSize != null) {
			if (0 == count % newPageSize) {
				pagesAvailable = count / newPageSize;

				Integer labelsStart = 0;
				if (this.currentIndex > AbstractPageImpl.MAX_PAGE_LABELS) {
					labelsStart = this.currentIndex
							- AbstractPageImpl.MAX_PAGE_LABELS;
				}

				Integer labelsEnd = pagesAvailable.intValue();
				if ((pagesAvailable - this.currentIndex)
						> AbstractPageImpl.MAX_PAGE_LABELS) {
					labelsEnd = this.currentIndex
							+ AbstractPageImpl.MAX_PAGE_LABELS;
				}

				for (int index = labelsStart; index < labelsEnd; index++) {
					indices.add(index);
					String startLabel = getLabel(index * pageSize);
					String endLabel = getLabel(((index + 1) * pageSize) - 1);
					pageNumbers.put(index, createLabel(startLabel, endLabel));
				}
			} else {
				pagesAvailable = (count / newPageSize) + 1;

				Integer labelsStart = 0;
				if (this.currentIndex > AbstractPageImpl.MAX_PAGE_LABELS) {
					labelsStart = this.currentIndex
							- AbstractPageImpl.MAX_PAGE_LABELS;
				}

				Integer labelsEnd = pagesAvailable.intValue();
				if ((pagesAvailable - this.currentIndex)
						> AbstractPageImpl.MAX_PAGE_LABELS) {
					labelsEnd = this.currentIndex
							+ AbstractPageImpl.MAX_PAGE_LABELS;
					for (int index = labelsStart; index < labelsEnd; index++) {
						indices.add(index);

						String startLabel = getLabel(index * pageSize);
						String endLabel =
								getLabel(((index + 1) * pageSize) - 1);
						pageNumbers.put(index,
								createLabel(startLabel, endLabel));
					}

				} else {
					for (int index = labelsStart;
							index < (labelsEnd - 1); index++) {
						indices.add(index);
						String startLabel = getLabel(index * pageSize);
						String endLabel
						= getLabel(((index + 1) * pageSize) - 1);
						pageNumbers.put(index,
								createLabel(startLabel, endLabel));
					}
					indices.add(pagesAvailable.intValue() - 1);
					String startLabel = getLabel((pagesAvailable.intValue() - 1)
							* pageSize);
					String endLabel = getLabel(count.intValue() - 1);
					pageNumbers.put(pagesAvailable.intValue() - 1,
							createLabel(startLabel, endLabel));
				}

			}
		} else {
			pagesAvailable = 1;
		}

		if (pagesAvailable == 1) {
			nextIndex = null;
			prevIndex = null;
		} else {
			if (0 < this.currentIndex) {
				prevIndex = this.currentIndex - 1;
			}
			if (this.currentIndex < (pagesAvailable - 1)) {
				nextIndex = this.currentIndex + 1;
			}
		}
		if (newPageSize == null) {
			this.firstRecord = 1;
			this.lastRecord = newRecords.size();
		} else {
			this.firstRecord = (this.currentIndex * newPageSize) + 1;
			this.lastRecord = (this.currentIndex * newPageSize)
					+ newRecords.size();
		}

		this.size = count;
		this.records = newRecords;

		if(this.queryResponse != null && this.queryResponse.getSpellCheckResponse() != null) {
			if(this.queryResponse.getSpellCheckResponse().getCollatedResults() != null &&
					this.queryResponse.getSpellCheckResponse().getCollatedResults().size() != 0) {
				this.suggestedSpelling = this.queryResponse.getSpellCheckResponse().getCollatedResults().get(0).getCollationQueryString();
			}
		}

		if(this.queryResponse != null && this.queryResponse.getSpellCheckResponse() != null) {
			this.correctlySpelled = this.queryResponse.getSpellCheckResponse().isCorrectlySpelled();
		}
	}

	/**
	 *
	 * @return the number of pages available
	 */
	public Integer getPagesAvailable() {
		return pagesAvailable;
	}

	/**
	 *
	 * @return the index of the next page
	 */
	public Integer getNextIndex() {
		return nextIndex;
	}

	/**
	 *
	 * @return the index of the previous page
	 */
	public Integer getPrevIndex() {
		return prevIndex;
	}

	/**
	 *
	 * @return the index of this page
	 */
	public Integer getCurrentIndex() {
		return currentIndex;
	}

	@JsonIgnore
	public String getCurrentPageNumber() {
		return this.pageNumbers.get(currentIndex);
	}

	/**
	 *
	 * @return the total number of objects available.
	 */
	public Integer getSize() {
		return size;
	}

	/**
	 *
	 * @return the index of the first record in this result set
	 */
	public Integer getFirstRecord() {
		return firstRecord;
	}

	/**
	 *
	 * @return the index of the last record in this result set
	 */
	public Integer getLastRecord() {
		return lastRecord;
	}

	/**
	 *
	 * @return the records in this page
	 */
	public List<T> getRecords() {
		return records;
	}

	/**
	 *
	 * @return the page size
	 */
	public Integer getPageSize() {
		return pageSize;
	}

	/**
	 * @return a map of the calculated facets
	 */
	@JsonIgnore
	public FacetField getFacetField(String facetName) {
		return queryResponse.getFacetField(facetName);
	}

	@JsonIgnore
	public RangeFacet getRangeFacet(String facetName) {
		for(RangeFacet rangeFacet : queryResponse.getFacetRanges()) {
			if(rangeFacet.getName().equals(facetName)) {
				return rangeFacet;
			}
		}
		return null;
	}

	/**
	 * @return a list of the calculated facet names
	 */
	@JsonIgnore
	public List<String> getFacetNames() {
		List<String> facetNames = new ArrayList<String>();
		for(FacetField facetField : queryResponse.getFacetFields()) {
			facetNames.add(facetField.getName());
		}
		for(RangeFacet facetRange : queryResponse.getFacetRanges()) {
			facetNames.add(facetRange.getName());
		}
		Collections.sort(facetNames, new FacetNameComparator());
		return facetNames;
	}

	/**
	 * @param name Set the parameter name
	 * @param value Set the parameter value
	 */
	@JsonIgnore
	public void putParam(String name, Object value) {
		this.parameters.put(name, value);
	}

	/**
	 * @return a map of the parameters
	 */
	public Map<String, Object> getParams() {
		return parameters;
	}

	/**
	 * @return the parameter names
	 */
	@JsonIgnore
	public Set<String> getParamNames() {
		return parameters.keySet();
	}

	/**
	 * @param index set the page number
	 * @return the label for a given page
	 */
	@JsonIgnore
	public String getPageNumber(int index) {
		return pageNumbers.get(index);
	}

	/**
	 *
	 * @param i set the index
	 * @return the label
	 */
	@JsonIgnore
	protected String getLabel(Integer i) {
		Integer label = new Integer(i + 1);
		return label.toString();
	}

	/**
	 *
	 * @param startLabel Set the start label
	 * @param endLabel Set the end label
	 * @return a page label
	 */
	protected abstract String createLabel(String startLabel, String endLabel);

	/**
	 *
	 * @return the list of indices for which we have a page label
	 */
	public List<Integer> getIndices() {
		return indices;
	}

	/**
	 *
	 * @return a list of the names of selected facets
	 */
	@JsonIgnore
	public Set<String> getSelectedFacetNames() {
		return selectedFacets.keySet();
	}

	/**
	 *
	 * @return The index of the selected facet, or null if the facet is not
	 *         selected
	 */
	public Map<String, String> getSelectedFacets() {
		return selectedFacets;
	}

	/**
	 *
	 * @param facetName
	 *            Set the facet name
	 * @return true if the facet is selected, false otherwise
	 */
	@JsonIgnore
	public boolean isFacetSelected(String facetName) {
		return selectedFacets.containsKey(facetName);
	}

	/**
	 * @param facetName
	 *            Set the facet name
	 * @param selected
	 *            Set the index of the selected facet
	 */
	public void setSelectedFacets(Map<String,String> selectedFacets) {
		this.selectedFacets = selectedFacets;
	}

	/**
	 * @return the sort
	 */
	public String getSort() {
		return sort;
	}

	/**
	 * @param newSort set the sorting
	 */
	public void setSort(String newSort) {
		this.sort = newSort;
	}

	/**
	 * @return the parameters
	 */
	public Map<String, Object> getParameters() {
		return parameters;
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	/**
	 * @param pagesAvailable the pagesAvailable to set
	 */
	public void setPagesAvailable(Integer pagesAvailable) {
		this.pagesAvailable = pagesAvailable;
	}

	/**
	 * @param prevIndex the prevIndex to set
	 */
	public void setPrevIndex(Integer prevIndex) {
		this.prevIndex = prevIndex;
	}

	/**
	 * @param nextIndex the nextIndex to set
	 */
	public void setNextIndex(Integer nextIndex) {
		this.nextIndex = nextIndex;
	}

	/**
	 * @param currentIndex the currentIndex to set
	 */
	public void setCurrentIndex(Integer currentIndex) {
		this.currentIndex = currentIndex;
	}

	/**
	 * @param firstRecord the firstRecord to set
	 */
	public void setFirstRecord(Integer firstRecord) {
		this.firstRecord = firstRecord;
	}

	/**
	 * @param lastRecord the lastRecord to set
	 */
	public void setLastRecord(Integer lastRecord) {
		this.lastRecord = lastRecord;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(Integer size) {
		this.size = size;
	}

	/**
	 * @param records the records to set
	 */
	public void setRecords(List<T> records) {
		this.records = records;
	}

	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @param indices the indices to set
	 */
	public void setIndices(ArrayList<Integer> indices) {
		this.indices = indices;
	}

	public String getSuggestedSpelling() {
		return this.suggestedSpelling;
	}

	public boolean getCorrectlySpelled() {
		return this.correctlySpelled;
	}

	public void setCorrectlySpelled(boolean correctlySpelled) {
		this.correctlySpelled = correctlySpelled;
	}

	public void setSuggestedSpelling(String suggestedSpelling) {
		this.suggestedSpelling = suggestedSpelling;
	}

}
