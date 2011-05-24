package org.emonocot.model.common;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang.ObjectUtils;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

/**
 *
 * @author ben
 *
 */
@MappedSuperclass
public abstract class Base {

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
     */
    private String identifier;

    /**
     *
     * @return Get the license of this object.
     */
    @Enumerated(value = EnumType.STRING)
    public License getLicense() {
        return license;
    }

    /**
     *
     * @return Get the time this object was created.
     */
    @Type(type="dateTimeUserType")
    public DateTime getCreated() {
        return created;
    }

    /**
     *
     * @return Get the time this object was last modified.
     */
    @Type(type="dateTimeUserType")
    public DateTime getModified() {
        return modified;
    }

    /**
     *
     * @return Get the source of this object.
     */
    public String getSource() {
        return source;
    }

    /**
     *
     * @param newId
     *            Set the identifier of this object.
     */
    public void setId(Long newId) {
        this.id = newId;
    }

    /**
     *
     * @param newCreated
     *            Set the created time for this object.
     */
    public void setCreated(DateTime newCreated) {
        this.created = newCreated;
    }

    /**
     *
     * @param newModified
     *            Set the modified time for this object.
     */
    public void setModified(DateTime newModified) {
        this.modified = newModified;
    }

    /**
     *
     * @param newLicense
     *            Set the license for this object.
     */
    public void setLicense(License newLicense) {
        this.license = newLicense;
    }

    /**
     *
     * @param newSource
     *            Set the source for this object.
     */
    public void setSource(String newSource) {
        this.source = newSource;
    }

    /**
     *
     * @return Get the identifier for this object.
     */
    @Id
    @GeneratedValue(generator = "system-increment")
    public Long getId() {
        return id;
    }

    /**
     *
     * @param newCreator
     *            Set the creator of this object
     */
    public void setCreator(String newCreator) {
        this.creator = newCreator;
    }

    /**
     *
     * @return Get the creator of this object.
     */
    public String getCreator() {
        return creator;
    }

    /**
     *
     * @return The unique identifier of the object
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     *
     * @param identifier Set the unique identifier of the object
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
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
