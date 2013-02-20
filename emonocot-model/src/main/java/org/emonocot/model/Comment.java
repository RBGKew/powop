/**
 * 
 */
package org.emonocot.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.emonocot.model.auth.User;
import org.emonocot.model.marshall.json.AnnotatableObjectDeserializer;
import org.emonocot.model.marshall.json.AnnotatableObjectSerializer;
import org.emonocot.model.marshall.json.DateTimeDeserializer;
import org.emonocot.model.marshall.json.DateTimeSerializer;
import org.emonocot.model.marshall.json.UserDeserializer;
import org.emonocot.model.marshall.json.UserSerializer;
import org.emonocot.model.registry.Organisation;
import org.emonocot.model.registry.Resource;
import org.hibernate.annotations.Any;
import org.hibernate.annotations.AnyMetaDef;
import org.hibernate.annotations.MetaValue;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

/**
 * @author jk00kg
 * A comment provided by a portal {@link User} about some item of Data
 */
@Entity
public class Comment extends Base {

    /**
     * 
     */
    private Long id;
    
    /**
     * 
     */
    private String comment;
    
    /**
     * 
     */
    private Base aboutData;

    /**
     * 
     */
    private DateTime created;
    
    /**
     * 
     */
    private Status status;
    
    /**
     * 
     */
    private User user;

    /* (non-Javadoc)
     * @see org.emonocot.model.Identifiable#getIdentifier()
     */
    @Override
    public String getIdentifier() {
        return identifier;
    }

    /* (non-Javadoc)
     * @see org.emonocot.model.Identifiable#setIdentifier(java.lang.String)
     */
    @Override
    public void setIdentifier(String identifier) {
        this.identifier = identifier;

    }

    /**
     * @return the id
     */
    @Id
    @GeneratedValue(generator = "system-increment")
    public Long getId() {
        return id;
    }
    
    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the comment
     */
    @Lob
    public String getComment() {
        return comment;
    }

    /**
     * @param comment the comment to set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * @return the aboutData
     */
    @JoinColumn(name = "aboutData_id")
    @Any(metaColumn = @Column(name = "aboutData_type"),
        fetch = FetchType.LAZY)
    @AnyMetaDef(idType = "long", metaType = "string", metaValues = {
            @MetaValue(targetEntity = Comment.class, value = "Comment"),
        @MetaValue(targetEntity = Description.class, value = "Description"),
        @MetaValue(targetEntity = Distribution.class, value = "Distribution"),
        @MetaValue(targetEntity = Identifier.class, value = "Identifier"),
        @MetaValue(targetEntity = IdentificationKey.class, value = "IdentificationKey"),
        @MetaValue(targetEntity = Image.class, value = "Image"),
        @MetaValue(targetEntity = MeasurementOrFact.class, value = "MeasurementOrFact"),
        @MetaValue(targetEntity = Organisation.class, value = "Organisation"),
        @MetaValue(targetEntity = Resource.class, value = "Resource"),
        @MetaValue(targetEntity = Reference.class, value = "Reference"),
        @MetaValue(targetEntity = Taxon.class, value = "Taxon"),
        @MetaValue(targetEntity = TypeAndSpecimen.class, value = "TypeAndSpecimen"),
        @MetaValue(targetEntity = VernacularName.class, value = "VernacularName")
    })
    @JsonSerialize(using = AnnotatableObjectSerializer.class)
    public Base getAboutData() {
        return aboutData;
    }

    /**
     * @param aboutData the aboutData to set
     */
    @JsonDeserialize(using = AnnotatableObjectDeserializer.class)
    public void setAboutData(Base aboutData) {
        this.aboutData = aboutData;
    }

    /**
     *
     * @return Get the time this object was created.
     */
    @Type(type="dateTimeUserType")
    @JsonSerialize(using = DateTimeSerializer.class)
    public DateTime getCreated() {
        return created;
    }

    /**
     *
     * @param created
     *            Set the created time for this object.
     */
    @JsonDeserialize(using = DateTimeDeserializer.class)
    public void setCreated(DateTime created) {
        this.created = created;
    }

    /**
     * @return the status
     */
    @Enumerated(EnumType.STRING)
    public Status getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * @return the user
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonSerialize(using = UserSerializer.class)
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    @JsonDeserialize(using = UserDeserializer.class)
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @author jk00kg
     * The sending status of a comment
     */
    public enum Status {
        PENDING,
        REFUSED,
        SENT;
    }

}
