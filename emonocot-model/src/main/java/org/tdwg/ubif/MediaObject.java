package org.tdwg.ubif;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 *
 * @author ben
 *
 */
public class MediaObject {

    /**
     *
     */
    @XStreamAsAttribute
    private String id;

    /**
     *
     */
    @XStreamAsAttribute
    private String debuglabel;

    /**
     *
     */
    @XStreamAlias("Representation")
    private Representation representation;

    /**
    *
    */
    @XStreamAlias("Type")
    private String type;

    /**
     *
     */
    @XStreamAlias("Source")
    private Source source;

    /**
     * @return the representation
     */
    public final Representation getRepresentation() {
        return representation;
    }

    /**
     * @param newRepresentation
     *            the representation to set
     */
    public final void setRepresentation(final Representation newRepresentation) {
        this.representation = newRepresentation;
    }

    /**
     *
     * @param newId
     *            Set the id
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

    /**
     * @return the debuglabel
     */
    public final String getDebuglabel() {
        return debuglabel;
    }

    /**
     * @param newDebugLabel
     *            the debuglabel to set
     */
    public final void setDebuglabel(final String newDebugLabel) {
        this.debuglabel = newDebugLabel;
    }

    /**
     * @return the type
     */
    public final String getType() {
        return type;
    }

    /**
     * @param newType the type to set
     */
    public final void setType(final String newType) {
        this.type = newType;
    }

    /**
     * @return the source
     */
    public final Source getSource() {
        return source;
    }

    /**
     * @param newSource the source to set
     */
    public final void setSource(final Source newSource) {
        this.source = newSource;
    }

}
