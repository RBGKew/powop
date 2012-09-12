package org.tdwg.ubif;

import java.util.Set;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 *
 * @author ben
 *
 */
public class Representation {

    /**
     *
     */
    @XStreamAlias("Label")
    private String label;

    /**
     *
     */
    @XStreamAlias("Detail")
    private String detail;

    /**
     *
     */
    @XStreamImplicit(itemFieldName="MediaObject")
    private Set<MediaObjectRef> mediaObjects;

    /**
     *
     * @param newLabel Set the label
     */
    public final void setLabel(final String newLabel) {
        this.label = newLabel;
    }

    /**
     *
     * @return the label
     */
    public final String getLabel() {
        return label;
    }

    /**
     * @return the mediaObjects
     */
    public final Set<MediaObjectRef> getMediaObjects() {
        return mediaObjects;
    }

    /**
     * @param newMediaObjects the mediaObject to set
     */
    public final void setMediaObjects(final Set<MediaObjectRef> newMediaObjects) {
        this.mediaObjects = newMediaObjects;
    }

    /**
     * @return the detail
     */
    public final String getDetail() {
        return detail;
    }

    /**
     * @param newDetail the detail to set
     */
    public final void setDetail(final String newDetail) {
        this.detail = newDetail;
    }
}
