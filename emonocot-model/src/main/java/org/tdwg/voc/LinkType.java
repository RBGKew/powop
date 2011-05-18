package org.tdwg.voc;

import java.io.Serializable;
import java.net.URI;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 *
 * @author ben
 *
 */
public abstract class LinkType {

    /**
     *
     */
    @XStreamAlias("rdf:resource")
    @XStreamAsAttribute
    private URI resource;

    /**
     *
     * @return the resource
     */
    public final Serializable getResource() {
        return resource;
    }

    /**
     *
     * @param newResource Set the resource
     */
    public final void setResource(final URI newResource) {
        this.resource = newResource;
    }
}
