/**
 * 
 */
package org.emonocot.model;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.apache.solr.common.SolrInputDocument;
import org.emonocot.model.auth.User;
import org.emonocot.model.marshall.json.AnnotatableObjectDeserializer;
import org.emonocot.model.marshall.json.AnnotatableObjectSerializer;
import org.emonocot.model.marshall.json.DateTimeDeserializer;
import org.emonocot.model.marshall.json.DateTimeSerializer;
import org.emonocot.model.marshall.json.OrganisationDeserialiser;
import org.emonocot.model.marshall.json.OrganisationSerializer;
import org.emonocot.model.marshall.json.UserDeserializer;
import org.emonocot.model.marshall.json.UserSerializer;
import org.emonocot.model.registry.Organisation;
import org.hibernate.annotations.Any;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author jk00kg
 * A comment provided by a portal {@link User} about some item of Data
 */
@Entity
public class Comment extends Base implements Searchable {
	
    private static DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    /**
	 * 
	 */
	private static final long serialVersionUID = -5773904824251895404L;

	private Long id;

    private String comment;
    
    private String subject;

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
    private Base commentPage;
    
    private Organisation authority;
    
    private Map<String,String> alternativeIdentifiers = new HashMap<String,String>();

    public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	/**
    *
    * @return the primary authority
    */
   @ManyToOne(fetch = FetchType.LAZY)
   @JsonSerialize(using = OrganisationSerializer.class)
   public Organisation getAuthority() {
       return authority;
   }

   /**
    *
    * @param authority Set the authority
    */
   @JsonDeserialize(using = OrganisationDeserialiser.class)
   public void setAuthority(Organisation authority) {
       this.authority = authority;
   }
    
    /**
	 * @return the inResponseTo
	 */
    @ManyToOne(fetch = FetchType.LAZY)
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
    @JoinColumn(name = "commentPage_id", nullable = true)
    @JsonSerialize(using = AnnotatableObjectSerializer.class)
	public Base getCommentPage() {
		return commentPage;
	}

	/**
	 * @param commentPage the commentPage to set
	 */
    @JsonDeserialize(using = AnnotatableObjectDeserializer.class)
	public void setCommentPage(Base commentPage) {
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
    @GeneratedValue(generator = "table-hilo", strategy = GenerationType.TABLE)
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
    @JoinColumn(name = "aboutData_id", nullable = true)
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
    
    @ElementCollection
    public Map<String,String> getAlternativeIdentifiers() {
		return alternativeIdentifiers;
	}

	public void setAlternativeIdentifiers(Map<String,String> alternativeIdentifiers) {
		this.alternativeIdentifiers = alternativeIdentifiers;
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

    @Transient
	@JsonIgnore
	public String getClassName() {
		return "Comment";
	}
	
	@Override
	@Transient
    @JsonIgnore
	public String getDocumentId() {
		return getClassName() + "_" + getId();
	}

	@Override
	public SolrInputDocument toSolrInputDocument() {
		SolrInputDocument sid = new SolrInputDocument();
		sid.addField("id", getClassName() + "_" + getId());
    	sid.addField("base.id_l", getId());
    	sid.addField("base.class_searchable_b", false);
    	sid.addField("base.class_s", getClass().getName());
    	if(getAboutData() != null) {
    		sid.addField("comment.about_class_s",getAboutData().getClass().getName());   
		}
    	StringBuilder summary = new StringBuilder().append(getComment());
    	if(getCommentPage() != null) {
    		if(getCommentPage() instanceof Taxon) {
    			sid.addField("comment.comment_page_class_s","org.emonocot.model.Taxon");
    			Taxon taxon = (Taxon)getCommentPage();
    			summary.append(" ").append(" ").append(taxon.getClazz()).append(" ").append(taxon.getFamily()).append(" ")
    			.append(taxon.getGenus()).append(" ").append(taxon.getKingdom()).append(" ").append(taxon.getOrder()).append(" ")
    			.append(taxon.getPhylum()).append(" ").append(taxon.getScientificName()).append(" ")
    			.append(taxon.getScientificNameAuthorship()).append(" ").append(taxon.getSpecificEpithet()).append(" ")
    			.append(taxon.getSubfamily()).append(" ").append(taxon.getSubgenus()).append(" ")
    			.append(taxon.getSubtribe()).append(" ").append(taxon.getTaxonomicStatus()).append(" ")
    			.append(taxon.getTribe());
    			sid.addField("taxon.family_ss", taxon.getFamily());
    		} else if(getCommentPage() instanceof IdentificationKey) {
    			sid.addField("comment.comment_page_class_s","org.emonocot.model.IdentificationKey");
    			IdentificationKey identificationKey = (IdentificationKey)getCommentPage();
    			summary.append(" ").append(identificationKey.getTitle());
    		} else if(getCommentPage() instanceof Image) {
    			Image image = (Image)getCommentPage();
    			summary.append(" ").append(image.getTitle());
    			sid.addField("comment.comment_page_class_s","org.emonocot.model.Image");
    		}
		}
    	//sid.addField("comment.comment_t",getComment());
    	sid.addField("comment.created_dt",dateTimeFormatter.print(getCreated()));
    	sid.addField("comment.status_t",getStatus());
    	sid.addField("comment.subject_s",getSubject());
    	sid.addField("searchable.solrsummary_t", summary.toString());
		return sid;
	}

}
