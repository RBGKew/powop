package org.emonocot.pager;

import java.util.Comparator;

import org.apache.commons.collections.comparators.NullComparator;

public class Member implements Comparable<Member> { 
	
  private static NullComparator nullSafeStringComparator;
  static {
	  Comparator<String> stringComparator = new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}

	  };
	  nullSafeStringComparator = new NullComparator(stringComparator, false);
  }
  
  private int ordinal;
  
  private Level level;
  
  private String value;
  
  private String field;
  
  public Member(Level level, int ordinal, String field, String value) {
	  this.level = level;
	  this.ordinal = ordinal;
	  this.value = value;
	  this.field = field;
  }
  
  public int compareTo(Member o) {
	  return nullSafeStringComparator.compare(this.value, o.value);
  }

  /**
   * Get the ordinal of this member
   */
  public int getOrdinal() {
	  return ordinal;
  }

  /**
   * Get the facet value (calculated) which is the name of this facet
   */
  public String getValue() {
	  return value;
  }
  
  public String getField() {
	  return field;
  }
}
