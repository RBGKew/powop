package org.emonocot.portal.rows;

/**
 *
 * @author ben
 *
 */
public class SummaryRow {

    /**
     *
     */
    public String category;

    /**
     *
     */
    public String subcategory;
    
    /**
     *
     */
    public String details;
    
    /**
     *
     */
    public String record;

    /**
     *
     * @return the object as an array
     */
    public final Object[] toArray() {
        return new String[] {this.category, this.subcategory, this.details, this.record};
    }
}
