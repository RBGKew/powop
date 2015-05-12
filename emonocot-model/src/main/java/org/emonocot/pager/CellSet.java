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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.PivotField;
import org.apache.solr.client.solrj.response.QueryResponse;



public class CellSet {
	
	public static Level count = null;
	
	static {
		CellSet.count = new Level("count",null, false);
		CellSet.count.addMember(0, "count", "count");
	}

	private Cube cube;
	
	private Level rows;
	
	private Level columns;
	
	private Integer nRows = 0;
	
	private Integer nCols = 0;
	
	private Integer firstRow;
	
	private Integer maxRows;
	
	private Integer firstCol;
	
	private Integer maxCols;
	
	private Integer totalRowsCount;
	
	private Integer totalColsCount;
	
	private Number[][] cells = new Number[0][0];
	
	private Map<String,String> selectedFacets;
	
	private QueryResponse queryResponse;
	
	private FacetField totalRows;
	private FacetField totalCols;
	
	public CellSet(QueryResponse response, Map<String,String> selectedFacets, SolrQuery query, String rows, String columns, Integer firstRow, Integer maxRows, Integer firstCol, Integer maxCols,FacetField totalRows, FacetField totalCols, Cube cube) {
		this.cube = cube;
		this.totalRows = totalRows;
		this.totalCols = totalCols;
		this.firstCol = firstCol;
		this.maxCols = maxCols;
		this.firstRow = firstRow;
		this.maxRows = maxRows;
		this.queryResponse = response;
		this.selectedFacets = selectedFacets;		
		    
		this.rows = cube.getLevel(rows);		
		if(columns != null) {
			this.columns = cube.getLevel(columns);			
		    FacetField rowField = response.getFacetField(rows);
			nRows = rowField.getValues().size();
			for(int i = 0; i < nRows; i++) {
				this.rows.addMember(i, rows, rowField.getValues().get(i).getName());
			}
			FacetField colField = response.getFacetField(columns);
			if(colField.getValueCount() < firstCol) {
			    nCols = 0;	
			} else if(colField.getValueCount() < firstCol + maxCols) {
			    nCols = colField.getValueCount() - firstCol;
			} else {
				nCols = maxCols;
			}
			for(int i = 0; i < nCols; i++) {				
				this.columns.addMember(i, columns, colField.getValues().get(firstCol + i).getName());
			}
	        cells = new Number[nRows][nCols];
	        for(PivotField rField : response.getFacetPivot().get(rows + "," + columns)) {
	        	Member rMember = null;
	        	if(rField.getValue() != null) {
	        		rMember = this.rows.getMember(rField.getValue().toString());
	        	} else {
	        		rMember = this.rows.getMember((String)rField.getValue());
	        	}
			    int i = rMember.getOrdinal();
				for(PivotField cField : rField.getPivot()) {
					Member cMember = null;
					if(cField.getValue() != null) {
					    cMember = this.columns.getMember(cField.getValue().toString());
					} else {
						cMember = this.columns.getMember((String)cField.getValue());
					}
					if(cMember != null) {
					    int j = cMember.getOrdinal();
					    cells[i][j] = cField.getCount();
				    }
				}
	        }
		} else if(rows != null) {
			this.columns = CellSet.count;
			this.totalColsCount = 1;
			// no cols, rows calculated through standard facet
			FacetField rowField = response.getFacetField(rows);
			nRows = rowField.getValues().size();
			nCols = 1;
			cells = new Number[nRows][nCols];
			for(int i = 0; i < nRows; i++) {
				this.rows.addMember(i, rows, rowField.getValues().get(i).getName());				
			    cells[i][0] = rowField.getValues().get(i).getCount();
			}			
		} else {
			this.columns = CellSet.count;
			this.totalColsCount = 1;			
			this.rows = cube.getLevel(cube.getDefaultLevel());
			// no cols or rows rows calculated through default facet
			FacetField rowField = response.getFacetField(cube.getDefaultLevel());
			nRows = rowField.getValues().size();
			nCols = 1;
			cells = new Number[nRows][nCols];
			for(int i = 0; i < nRows; i++) {
				this.rows.addMember(i, rows, rowField.getValues().get(i).getName());
			    cells[i][0] = rowField.getValues().get(i).getCount();
			}
		}
	}

    /**
     * The member which represents the columns
     */
    public Level getColumns() {
		return columns;
	}

    /**
     * The member which represents the rows
     */
    public Level getRows() {
		return rows;
	}

    /**
     * The cell value at ordinal i,j where i is the row and j is the column
     */
    public Number getCellValue(int row, int col) {
		return cells[row][col];
	}
	
    /**
     * The list of ${member.facet}:${member.value} filters to apply to the data - slices
     */
    public List<String> getFilters() {
		List<String> filters = new ArrayList<String>();
		if (selectedFacets != null && !selectedFacets.isEmpty()) {
            for (String facetName : selectedFacets.keySet()) {
            	filters.add(facetName + ":" + selectedFacets.get(facetName));
            }
        }
		return filters;
	}
	
	public List<String> getEmptyFilters() {
		return new ArrayList<String>();
	}
    
    public String getValue(Level level) {
    	for(String filter : getFilters()) {
    		if(filter.startsWith(level.getFacet())) {
    			return filter.substring(level.getFacet().length() + 1);
    		}
    	}
    	return null;
    }
    
	public Cube getCube() {
		return cube;
	}
	
	public Integer getFirstRow() {
		return firstRow;
	}

	public Integer getMaxRows() {
		return maxRows;
	}

	public Integer getFirstCol() {
		return firstCol;
	}

	public Integer getMaxCols() {
		return maxCols;
	}
	
	public FacetField getTotalCols() {
		return totalCols;
	}
	
	public FacetField getTotalRows() {
		return totalRows;
	}
	
	public Level getCount() {
		return CellSet.count;
	}
	
	public FacetField getFacetField(String facetName) {
        return queryResponse.getFacetField(facetName);
    }

    public List<String> getFacetNames() {
        List<String> facetNames = new ArrayList<String>();
        for(FacetField facetField : queryResponse.getFacetFields()) {
			if(columns.isRelatedFacet(facetField.getName())) {
			} else if(rows.isRelatedFacet(facetField.getName())) {
			} else {
			    facetNames.add(facetField.getName());
			}
		}
        Collections.sort(facetNames, new FacetNameComparator());
        return facetNames;
    }
    
    public Map<String, String> getSelectedFacets() {
		if(selectedFacets == null) {
			return new HashMap<String,String>();
		}
        return selectedFacets;
    }

    public boolean isFacetSelected(String facetName) {
        if(selectedFacets != null && selectedFacets.containsKey(facetName)) {
			return true;
		} else if(columns.getFacet().equals(facetName)) {
			return true;
		} else if(rows.getFacet().equals(facetName)) {
		    return true;
		} else {
			return false;
		}
    }
    
    public void setSelectedFacets(Map<String,String> selectedFacets) {
        this.selectedFacets = selectedFacets;
    }

    public void setSelectedFacet(String facetName, String selected) {
        selectedFacets.put(facetName, selected);
    }
    
    public Long getRowTotal(Member row) {
    	FacetField.Count count = null;
    	for(FacetField.Count c : totalRows.getValues()) {
    		if(c.getName() == null) {
    			if(row.getValue() == null){
    				count = c;
        			break;
    			}
    		} else if(c.getName().equals(row.getValue())) {
    			count = c;
    			break;
    		}
    	}
    	if(count == null) {
    		return null;
    	} else {
    		return count.getCount();
    	}
    }
    
    public Long getColumnTotal(Member col) {
    	FacetField.Count count = null;
    	for(FacetField.Count c : totalCols.getValues()) {
    		if(c.getName() == null) {
    			if(col.getValue() == null) {
    			    count = c;
    			    break;
    			}
    		} else if(c.getName().equals(col.getValue())) {
    			count = c;
    			break;
    		}
    	}
    	if(count == null) {
    		return null;
    	} else {
    		return count.getCount();
    	}
	}
    
    public Long getRemainingCols(int from) {
    	if((totalCols.getValueCount() -1) < from) {
    		return 0L;
    	} else {
    		long remainder = 0;
    		for(int i = from; i < totalCols.getValueCount(); i++) {
    			remainder += totalCols.getValues().get(i).getCount();
    		}
    		return remainder;
    	}
    }
    
    public Long getRemainingRows(int from) {
    	if((totalRows.getValueCount() -1) < from) {
    		return 0L;
    	} else {
    		long remainder = 0;
    		for(int i = from; i < totalRows.getValueCount(); i++) {
    			remainder += totalRows.getValues().get(i).getCount();
    		}
    		return remainder;
    	}
    }
}
