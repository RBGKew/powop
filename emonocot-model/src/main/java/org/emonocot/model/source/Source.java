package org.emonocot.model.source;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.emonocot.model.common.BaseData;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

/**
 * Class that represents the authority an object is harvested from.
 *
 * @author ben
 *
 */
@Entity
@Indexed
public class Source extends BaseData {

    /**
     *
     */
    private static final long serialVersionUID = -2463044801110563816L;

    /**
    *
    */
    private String uri;

    /**
    *
    */
    private Long id;

    /**
    *
    */
    private String creatorEmail;

    /**
    *
    */
    private String description;

    /**
    *
    */
    private String logoUrl;

    /**
    *
    */
    private String publisherName;

    /**
    *
    */
    private String publisherEmail;

    /**
    *
    */
    private String subject;

    /**
    *
    */
    private String title;

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
     * @return the uri
     */
    @URL
    public String getUri() {
        return uri;
    }

    /**
     * @param uri
     *            the uri to set
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

    /**
     *
     * @return the class name
     */
    @Transient
    @JsonIgnore
    public final String getClassName() {
        return "Source";
    }

    /**
     * @return the creatorEmail
     */
    public final String getCreatorEmail() {
        return creatorEmail;
    }

    /**
     * @param creatorEmail the creatorEmail to set
     */
    @Email
    public final void setCreatorEmail(String creatorEmail) {
        this.creatorEmail = creatorEmail;
    }

    /**
     * @return the description
     */
    @Lob
    @Field
    @Length(max = 1431655761)
    public final String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public final void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the logoUrl
     */
    @URL
    public final String getLogoUrl() {
        return logoUrl;
    }

    /**
     * @param logoUrl the logoUrl to set
     */
    public final void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    /**
     * @return the publisherName
     */
    @Field
    public final String getPublisherName() {
        return publisherName;
    }

    /**
     * @param publisherName the publisherName to set
     */
    public final void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    /**
     * @return the publisherEmail
     */
    public final String getPublisherEmail() {
        return publisherEmail;
    }

    /**
     * @param publisherEmail the publisherEmail to set
     */
    public final void setPublisherEmail(String publisherEmail) {
        this.publisherEmail = publisherEmail;
    }

    /**
     * @return the subject
     */
    @Field
    public final String getSubject() {
        return subject;
    }

    /**
     * @param subject the subject to set
     */
    public final void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * @return the title
     */
    @Field
    public final String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public final void setTitle(String title) {
        this.title = title;
    }

}
