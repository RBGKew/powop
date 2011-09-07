package org.emonocot.model.common;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.apache.commons.lang.ObjectUtils;
import org.emonocot.model.authority.Authority;
import org.emonocot.model.hibernate.DateTimeBridge;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Parameter;
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
     * As Jo pointed out, having a map of AuthorityType -> Authority
     * didn't allow for more than one secondary authority.
     */
    private Set<Authority> authorities = new HashSet<Authority>();

    /**
     *
     */
    private Authority authority;

    /**
     *
     * @return the primary authority
     */
    @ManyToOne(fetch = FetchType.LAZY)
    public Authority getAuthority() {
        return authority;
    }

    /**
     *
     * @param authority Set the authority
     */
    public void setAuthority(Authority authority) {
        this.authority = authority;
    }

    /**
     * @return the authorities, including the primary authority
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @IndexedEmbedded
    public Set<Authority> getAuthorities() {
        return authorities;
    }

    /**
     * @param authorities the authorities to set
     */
    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    /**
     *
     * @return Get the license of this object.
     */
    @Enumerated(value = EnumType.STRING)
    @Field
    public License getLicense() {
        return license;
    }

    /**
     *
     * @return Get the time this object was created.
     */
    @Type(type="dateTimeUserType")
    @FieldBridge(impl = DateTimeBridge.class, params = {
        @Parameter(name = "resolution", value = "MILLISECOND")
    })
    public DateTime getCreated() {
        return created;
    }

    /**
     *
     * @return Get the time this object was last modified.
     */
    @Type(type="dateTimeUserType")
    @FieldBridge(impl = DateTimeBridge.class, params = {
        @Parameter(name = "resolution", value = "MILLISECOND")
    })
    public DateTime getModified() {
        return modified;
    }

    /**
     *
     * @return Get the source of this object.
     */
    @Field
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
    @DocumentId
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
    @Field
    public String getCreator() {
        return creator;
    }

    /**
     *
     * @return The unique identifier of the object
     */
    @Field
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
        return ObjectUtils.equals(this.identifier, base.identifier);
    }

    @Override
    public int hashCode() {
        return ObjectUtils.hashCode(this.identifier);
    }
}
