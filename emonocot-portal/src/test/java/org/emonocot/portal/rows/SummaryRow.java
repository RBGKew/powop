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
    public String label;

    /**
     *
     */
    public String value;

    /**
     *
     * @return the object as an array
     */
    public final Object[] toArray() {
        return new String[] {this.label, this.value};
    }
}
