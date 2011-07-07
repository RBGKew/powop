/**
 * 
 */
package org.emonocot.checklist.model;

/**
 * 
 * @author jk00kg
 *
 */
public enum Rank {
    FAMILY ("fam"),
    GENUS ("gen"),
    INFRASPECIFIC ("infrasp"),
    SPECIES ("sp"),
    SUBSPECIES ("ssp"),
    VARIETY ("var"),
    UNKNOWN ("?");
    
    /**
     * 
     */
    private String abbreviation;
    
    private Rank(String abbreviation) {
        this.abbreviation = abbreviation;
    }
    
    /**
     * @return the abbreviation
     */
    public String getAbbreviation() {
        return abbreviation;
    }
    
}
