package org.emonocot.pager;

import java.util.ArrayList;
import java.util.List;

public class Member implements Comparable<Member> {  
  
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
	  return this.value.compareTo(o.value);
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
