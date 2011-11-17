package org.emonocot.model.common;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.emonocot.model.hibernate.DateTimeBridge;
import org.emonocot.model.marshall.json.DateTimeDeserializer;
import org.emonocot.model.marshall.json.DateTimeSerializer;
import org.emonocot.model.marshall.json.SourceDeserializer;
import org.emonocot.model.marshall.json.SourceSerializer;
import org.emonocot.model.source.Source;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Index;
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
     * @return the primary authority
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SourceSerializer.class)
    public Source getAuthority() {
        return authority;
    }

    /**
     *
     * @param authority Set the authority
     */
    @JsonDeserialize(using = SourceDeserializer.class)
    public void setAuthority(Source authority) {
        this.authority = authority;
    }

    /**
     * @return the authorities, including the primary authority
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @IndexedEmbedded(depth = 1)
    @JsonSerialize(contentUsing = SourceSerializer.class)
    public Set<Source> getSources() {
        return sources;
    }

    /**
     * @param sources the authorities to set
     */
    @JsonDeserialize(contentUsing = SourceDeserializer.class)
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
    @Field(index = Index.UN_TOKENIZED)
    @FieldBridge(impl = DateTimeBridge.class, params = {
        @Parameter(name = "resolution", value = "MILLISECOND")
    })
    @JsonSerialize(using = DateTimeSerializer.class)
    public DateTime getCreated() {
        return created;
    }

    /**
     *
     * @return Get the time this object was last modified.
     */
    @Type(type="dateTimeUserType")
    @Field(index = Index.UN_TOKENIZED)
    @FieldBridge(impl = DateTimeBridge.class, params = {
        @Parameter(name = "resolution", value = "MILLISECOND")
    })
    @JsonSerialize(using = DateTimeSerializer.class)
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
    @JsonDeserialize(using = DateTimeDeserializer.class)
    public void setCreated(DateTime newCreated) {
        this.created = newCreated;
    }

    /**
     *
     * @param newModified
     *            Set the modified time for this object.
     */
    @JsonDeserialize(using = DateTimeDeserializer.class)
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
