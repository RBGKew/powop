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
