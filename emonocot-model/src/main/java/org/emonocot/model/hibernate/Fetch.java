package org.emonocot.model.hibernate;

import org.hibernate.FetchMode;

/**
 *
 * @author ben
 *
 */
public class Fetch {

    /**
     *
     */
    private String association;

    /**
     *
     */
    private FetchMode mode;

    /**
     *
     * @return the association
     */
    public final String getAssociation() {
        return association;
    }

    /**
     *
     * @param newAssociation Set the association
     */
    public final void setAssociation(final String newAssociation) {
        this.association = newAssociation;
    }

    /**
     *
     * @return the fetch mode
     */
    public final FetchMode getMode() {
        return mode;
    }

    /**
     *
     * @param newMode Set the fetch mode
     */
    public final void setMode(final FetchMode newMode) {
        this.mode = newMode;
    }

    /**
     *
     * @param newAssociation Set the association
     * @param newMode Set the mode
     */
    public Fetch(final String newAssociation, final FetchMode newMode) {
        this.association = newAssociation;
        this.mode = newMode;
    }
}
