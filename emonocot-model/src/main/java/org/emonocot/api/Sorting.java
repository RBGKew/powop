package org.emonocot.api;

/**
 * @author jk00kg
 *
 */
public class Sorting {

    /**
     *
     * @param newFieldName Set the field name
     */
    public Sorting(final String newFieldName) {
        this.fieldName = newFieldName;
        direction = SortDirection.FORWARD;
    }

   /**
    *
    * @param newFieldName Set the field name
    * @param newDirection Set the direction
    */
   public Sorting(final String newFieldName, final SortDirection newDirection) {
       this.fieldName = newFieldName;
       direction = newDirection;
   }

   /**
    * @return the sorting as a string
    */
   @Override
   public final String toString() {
        return this.fieldName == null ? "." + this.direction : this.fieldName
                + "." + this.direction;
   }

    /**
     * @author jk00kg
     *
     */
    public enum SortDirection {
        /**
         * Commonly 'ascending', the more usual order for searching.
         */
        FORWARD,
        /**
         * Commonly 'descending'.
         */
        REVERSE
    }

    /**
     *
     */
    private SortDirection direction;

    /**
     *
     */
    private String fieldName;

    /**
     * @return the direction to sort in
     */
    public final SortDirection getDirection() {
        return direction;
    }

    /**
     * @param newDirection
     *            the direction to sort in
     */
    public final void setDirection(final SortDirection newDirection) {
        this.direction = newDirection;
    }

    /**
     * @return the name of the index field in this sort
     */
    public final String getFieldName() {
        return fieldName;
    }

    /**
     * @param newFieldName
     *            the name of the index field in this sort
     */
    public final void setFieldName(final String newFieldName) {
        this.fieldName = newFieldName;
    }

    /**
     *
     * @param other Set the other object
     * @return true if the objects are equal
     */
    public final boolean equals(final Sorting other) {
        if (other == null) {
            return false;
        } else {
            if (this.fieldName == null && this.fieldName == other.fieldName) {
                return true;
            } else {
                if (this.fieldName == null) {
                    return false;
                } else if (this.fieldName.equals(other.fieldName)) {
                    if (this.direction == null && this.direction == other.direction) {
                        return true;
                    } else {
                        return this.direction.equals(other.direction);
                    }
                } else {
                     return false;
                }
            }
        }
    }

    @Override
    public final int hashCode() {
        if (this.fieldName == null) {
            return 0;
        } else {
            return this.fieldName.hashCode() * 29 + this.direction.hashCode();
        }
    }

}
