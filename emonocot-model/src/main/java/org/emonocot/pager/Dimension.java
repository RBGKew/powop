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

public class Dimension implements Comparable<Dimension> {

	public String name;
	
    private List<Level> levels = new ArrayList<Level>();
  
    public Dimension(String name) {
		this.name = name;
	}
	
	public Level getLevel(String facet) {
		for(Level level : levels) {
			if(level.getFacet().equals(facet)) {
				return level;
			}
		}
		return null;
	}
	
	public boolean containsLevel(String facet) {
		for(Level level : levels) {
			if(level.getFacet().equals(facet)) {
				return true;
			}
		}
		return false;
	}
	
	public void addLevel(String facet, boolean multiValued) {
		this.levels.add(new Level(facet,this,multiValued));
	}
  
    public Level getLowerLevel(Level level) {
	  int i = levels.indexOf(level);
	  if((i + 1) == levels.size()) {
		  return null;
	  } else {
		  return levels.get(i + 1);
	  }

    }
  
  public Level getHigherLevel(Level level) {
	  int i = levels.indexOf(level);
	  if(i == 0) {
		  return null;
	  } else {
		  return levels.get(i - 1);
	  }
  }
  
  public String getName() {
		return name;
	}

	public List<Level> getLevels() {
		return levels;
	}
	
	public int compareTo(Dimension o) {
		return this.name.compareTo(o.name);
	} 
}
