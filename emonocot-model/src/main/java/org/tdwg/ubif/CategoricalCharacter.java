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
