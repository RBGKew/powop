package org.tdwg.ubif;

import com.thoughtworks.xstream.annotations.XStreamAlias;

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
    @XStreamAlias("MediaObject")
    private MediaObjectRef mediaObject;

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
     * @return the mediaObject
     */
    public final MediaObjectRef getMediaObject() {
        return mediaObject;
    }

    /**
     * @param newMediaObject the mediaObject to set
     */
    public final void setMediaObject(final MediaObjectRef newMediaObject) {
        this.mediaObject = newMediaObject;
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
