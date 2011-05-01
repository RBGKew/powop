package org.emonocot.model.common;

import org.apache.commons.lang.ObjectUtils;
import org.joda.time.DateTime;

/**
 * 
 * @author ben
 * 
 */
public class Base {

    /**
     *
     */
    private Long id;

    /**
     *
     */
    private License license;

    /**
     *
     */
    private DateTime created;

    /**
     *
     */
    private DateTime modified;

    /**
     *
     */
    private String creator;

    /**
     *
     */
    private String source;

    /**
     * 
     * @return Get the license of this object.
     */
    public final License getLicense() {
        return license;
    }

    /**
     * 
     * @return Get the time this object was created.
     */
    public final DateTime getCreated() {
        return created;
    }

    /**
     * 
     * @return Get the time this object was last modified.
     */
    public final DateTime getModified() {
        return modified;
    }

    /**
     * 
     * @return Get the source of this object.
     */
    public final String getSource() {
        return source;
    }

    /**
     * 
     * @param newId
     *            Set the identifier of this object.
     */
    public final void setId(final Long newId) {
        this.id = newId;
    }

    /**
     * 
     * @param newCreated
     *            Set the created time for this object.
     */
    public final void setCreated(final DateTime newCreated) {
        this.created = newCreated;
    }

    /**
     * 
     * @param newModified
     *            Set the modified time for this object.
     */
    public final void setModified(final DateTime newModified) {
        this.modified = newModified;
    }

    /**
     * 
     * @param newLicense
     *            Set the license for this object.
     */
    public final void setLicense(final License newLicense) {
        this.license = newLicense;
    }

    /**
     * 
     * @param newSource
     *            Set the source for this object.
     */
    public final void setSource(final String newSource) {
        this.source = newSource;
    }

    /**
     * 
     * @return Get the identifier for this object.
     */
    public final Long getId() {
        return id;
    }

    /**
     * 
     * @param newCreator
     *            Set the creator of this object
     */
    public final void setCreator(final String newCreator) {
        this.creator = newCreator;
    }

    /**
     * 
     * @return Get the creator of this object.
     */
    public final String getCreator() {
        return creator;
    }

    @Override
    public boolean equals(Object other) {
        // check for self-comparison
        if (this == other) {
            return true;
        }
        if (other == null || other.getClass() != this.getClass()) {
            return false;
        }
        Base base = (Base) other;
        return ObjectUtils.equals(this.created, base.created)
                && ObjectUtils.equals(this.modified, base.modified)
                && ObjectUtils.equals(this.license, base.license)
                && ObjectUtils.equals(this.creator, base.creator);
    }

    @Override
    public int hashCode() {
        return super.hashCode() + this.created.hashCode()
                + this.modified.hashCode() + this.license.hashCode()
                + this.creator.hashCode();
    }
}
