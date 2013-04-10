package org.emonocot.pager;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class Cube {
	
	SortedSet<Dimension> dimensions = new TreeSet<Dimension>();
	
	Map<String,String> selectedFacets = new HashMap<String,String>();

	private String defaultLevel;
	
	public Cube(Map<String,String> selectedFacets) {
		this.selectedFacets = selectedFacets;
	}
	
	public Level getLevel(String field) {
		for(Dimension d : dimensions) {
			if(d.containsLevel(field)) {
				return d.getLevel(field);
			}
		}
		return null;
	}
	
	public void addDimension(Dimension dimension) {
		dimensions.add(dimension);
	}
	
	public Set<Dimension> getDimensions() {
		return dimensions;
	}

	public String getDefaultLevel() {
		return defaultLevel;
	}

	public void setDefaultLevel(String defaultLevel) {
		this.defaultLevel = defaultLevel;
	}	
}
