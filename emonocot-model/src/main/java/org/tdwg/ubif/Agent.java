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
package org.tdwg.ubif;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 *
 * @author ben
 *
 */
public class Agent {
    /**
    *
    */
    @XStreamAsAttribute
    private String ref;

    /**
     *
     */
    @XStreamAsAttribute
    private String id;

    /**
     *
     */
    @XStreamAsAttribute
    private String role;

    /**
     *
     */
    @XStreamAlias("Representation")
    private Representation representation;

    /**
     * @return the ref
     */
    public final String getRef() {
        return ref;
    }

    /**
     * @param newRef
     *            the ref to set
     */
    public final void setRef(final String newRef) {
        this.ref = newRef;
    }

    /**
     * @return the role
     */
    public final String getRole() {
        return role;
    }

    /**
     * @param newRole
     *            the role to set
     */
    public final void setRole(final String newRole) {
        this.role = newRole;
    }

    /**
     * @return the representation
     */
    public final Representation getRepresentation() {
        return representation;
    }

    /**
     * @param newRepresentation the representation to set
     */
    public final void setRepresentation(
            final Representation newRepresentation) {
        this.representation = newRepresentation;
    }

    /**
     *
     * @param newId Set the id
     */
    public final void setId(final String newId) {
        this.id = newId;
    }

    /**
     *
     * @return the id
     */
    public final String getId() {
        return id;
    }
}
