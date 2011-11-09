package org.emonocot.persistence.dao.olap;

import org.emonocot.persistence.dao.OlapResult;

/**
 *
 * @author ben
 *
 */
public class OlapResultImpl implements OlapResult {

    /**
     *
     */
    private String value;

    /**
     *
     */
    private String label;

    /**
     *
     * @param newValue Set the value
     * @param newLabel Set the label
     */
    public OlapResultImpl(final String newValue, final String newLabel) {
        this.value = newValue;
        this.label = newLabel;
    }

    /**
     * @return the value
     */
    public final String getValue() {
        return value;
    }

    /**
     * @return the label
     */
    public final String getLabel() {
        return label;
    }

}
