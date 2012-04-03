package org.tdwg.ubif;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 *
 * @author ben
 *
 */
public class MediaObjectRef {

    /**
     *
     */
    @XStreamAsAttribute
    private String ref;

    /**
     * @return the ref
     */
    public final String getRef() {
        return ref;
    }

    /**
     * @param newRef the ref to set
     */
    public final void setRef(final String newRef) {
        this.ref = newRef;
    }
}
