package org.emonocot.pager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Level {
	Logger logger = LoggerFactory.getLogger(Level.class);
	
	private Dimension dimension;
	
	private String facet;
	
	private boolean multiValued;
	
	private Member nullMember = null;
	
    private Map<String,Member> members = new HashMap<String,Member>();
    
    public Level(String facet, Dimension dimension, boolean multiValued) {
		this.facet = facet;	
		this.dimension = dimension;
		this.multiValued = multiValued;
	}
	
	public boolean isRelatedFacet(String facet) {
		if(dimension != null) {
		  for(Level level : dimension.getLevels()) {
			if(level.getFacet().equals(facet)) {
				return true;
			}
		  }
	    }
		return false;
	}
	
	public void addMember(int ordinal, String field, String value) {
		Member member = new Member(this, ordinal, field, value);
		this.members.put(value, member);
		if(value == null) {
			this.nullMember = member;
		}
	}
	
	public Member getMember(String value) {
		if(value == null) {
		    return nullMember;	
		} else {
		    return members.get(value);
		}
	}

  /**
   * Return the members of this dimension
   */
  public Collection<Member> getMembers() {
	  SortedSet<Member> sortedMembers = new TreeSet<Member>();
	  sortedMembers.addAll(members.values());
	  return sortedMembers;
  }
  
  /**
   * Get this string value of the solr field which this dimension represents
   */
  public String getFacet() {
	  return facet;
  }
  
  public Level getLower() {
	  if(dimension != null) {
	      return dimension.getLowerLevel(this);
	  } else {
		  return null;
	  }
  }
  
  public Level getHigher() {
	  if(dimension != null) {
	      return dimension.getHigherLevel(this);
	  } else {
		  return null;
	  }
  }
  
  public boolean isMultiValued() {
	  return multiValued;
  }
  
  public Dimension getDimension() {
	  return dimension;
  }

}
