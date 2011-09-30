/**
 * 
 */
package org.emonocot.model.comms;

/**
 * @author jk00kg
 *
 */
public class Sorting {
            
    public Sorting(String fieldName){
        this.fieldName = fieldName;
        direction = SortDirection.FORWARD; 
    }

    /**
     * @author jk00kg
     *
     */
    public enum SortDirection {
        /**
         * Commonly 'ascending', the more usual order for searching
         */
        FORWARD,
        /**
         * Commonly 'descending'
         */
        REVERSE
    }

    private SortDirection direction;

    private String fieldName;

    /**
     * @return the direction to sort in
     */
    public SortDirection getDirection() {
        return direction;
    }

    /**
     * @param direction the direction to sort in
     */
    public void setDirection(SortDirection direction) {
        this.direction = direction;
    }

    /**
     * @return  the name of the index field in this sort
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * @param fieldName the name of the index field in this sort
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
        
        
}
