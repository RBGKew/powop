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

import org.codehaus.jackson.annotate.JsonIgnore;
import org.emonocot.model.hibernate.DateTimeBridge;
import org.emonocot.model.source.Source;
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
public abstract class BaseData extends Base {

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
     * As Jo pointed out, having a map of AuthorityType -> Authority
     * didn't allow for more than one secondary authority.
     */
    private Set<Source> sources = new HashSet<Source>();

    /**
     *
     */
    private Source authority;

    /**
     *
     */
    private Long id;

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
     * @return the primary authority
     */
    @ManyToOne(fetch = FetchType.LAZY)
    public Source getAuthority() {
        return authority;
    }

    /**
     *
     * @param authority Set the authority
     */
    public void setAuthority(Source authority) {
        this.authority = authority;
    }

    /**
     * @return the authorities, including the primary authority
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @IndexedEmbedded
    @JsonIgnore
    public Set<Source> getSources() {
        return sources;
    }

    /**
     * @param sources the authorities to set
     */
    public void setSources(Set<Source> sources) {
        this.sources = sources;
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
}
