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
