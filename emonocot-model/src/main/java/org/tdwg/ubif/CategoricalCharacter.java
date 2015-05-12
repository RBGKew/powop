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

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 *
 * @author ben
 *
 */
public class CategoricalCharacter {

    /**
    *
    */
    @XStreamAsAttribute
    private String id;

    /**
    *
    */
    @XStreamAlias("Representation")
    private Representation representation;

    /**
     *
     */
    @XStreamAlias("States")
    private List<StateDefinition> states;

    /**
     * @return the id
     */
    public final String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public final void setId(final String id) {
        this.id = id;
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
    public final void setRepresentation(final Representation newRepresentation) {
        this.representation = newRepresentation;
    }

    /**
     * @return the states
     */
    public final List<StateDefinition> getStates() {
        return states;
    }

    /**
     * @param newStates the states to set
     */
    public final void setStates(final List<StateDefinition> newStates) {
        this.states = newStates;
    }
}
