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

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.emonocot.model.auth.User;
import org.emonocot.model.marshall.json.AnnotatableObjectDeserializer;
import org.emonocot.model.marshall.json.AnnotatableObjectSerializer;
import org.emonocot.model.marshall.json.DateTimeDeserializer;
import org.emonocot.model.marshall.json.DateTimeSerializer;
import org.emonocot.model.marshall.json.UserDeserializer;
import org.emonocot.model.marshall.json.UserSerializer;
import org.hibernate.annotations.Any;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

/**
 * @author jk00kg
 * A comment provided by a portal {@link User} about some item of Data
 */
@Entity
public class Comment extends Base {

    private Long id;

    private String comment;
    
    /**
     * The object which this comment is about
     */
    private Base aboutData;

    private DateTime created;
    
    private Status status;
    
    private User user;
    
    /**
     * If this comment is a response to another comment, the immediate parent comment
     */
    private Comment inResponseTo;
    
    /**
     * The object (page) on which this comment should appear
     */
    private BaseData commentPage;
    

    /**
	 * @return the inResponseTo
	 */
    @ManyToOne(fetch = FetchType.LAZY)
	@Cascade({ CascadeType.SAVE_UPDATE })
	@JsonIgnore
	public Comment getInResponseTo() {
		return inResponseTo;
	}

	/**
	 * @param inResponseTo the inResponseTo to set
	 */
    @JsonIgnore
	public void setInResponseTo(Comment inResponseTo) {
		this.inResponseTo = inResponseTo;
	}

	/**
	 * @return the commentPage
	 */
    @Any(metaColumn = @Column(name = "commentPage_type"),
            fetch = FetchType.LAZY, metaDef = "CommentMetaDef")
    @JoinColumn(name = "commentPage_id")
    @JsonSerialize(using = AnnotatableObjectSerializer.class)
	public BaseData getCommentPage() {
		return commentPage;
	}

	/**
	 * @param commentPage the commentPage to set
	 */
    @JsonDeserialize(using = AnnotatableObjectDeserializer.class)
	public void setCommentPage(BaseData commentPage) {
		this.commentPage = commentPage;
	}

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
    
    @Any(metaColumn = @Column(name = "aboutData_type"),
        fetch = FetchType.LAZY, metaDef = "CommentMetaDef")
    @JoinColumn(name = "aboutData_id")
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
