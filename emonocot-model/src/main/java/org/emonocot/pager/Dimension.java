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
