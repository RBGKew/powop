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
