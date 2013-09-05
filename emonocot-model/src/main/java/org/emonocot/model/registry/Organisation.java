package org.emonocot.model.registry;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

import org.apache.solr.common.SolrInputDocument;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.emonocot.model.Annotation;
import org.emonocot.model.BaseData;
import org.emonocot.model.Searchable;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

/**
 * Class that represents the authority an object is harvested from.
 *
 * @author ben
 *
 */
@Entity
public class Organisation extends BaseData implements Comparable<Organisation>, Searchable {

    private static final long serialVersionUID = -2463044801110563816L;

    private String uri;

    private Long id;

    private String creatorEmail;

    private String description;

    private String logoUrl;

    private String publisherName;

    private String publisherEmail;

    private String subject;

    private String title;
    
    private String bibliographicCitation;
    
    private String creator;

    private Set<Resource> resources;
    
    private Set<Annotation> annotations = new HashSet<Annotation>();

    /**
     * 
     */
    private String commentsEmailedTo;

	private boolean insertCommentsIntoScratchpad;

    public void setId(Long newId) {
        this.id = newId;
    }

    @Id
    @GeneratedValue(generator = "table-hilo", strategy = GenerationType.TABLE)
    public Long getId() {
        return id;
    }

    @URL
    public String getUri() {
        return uri;
    }

    public void setUri(String newUri) {
        this.uri = newUri;
    }

    public boolean getInsertCommentsIntoScratchpad() {
		return insertCommentsIntoScratchpad;
	}

	public void setInsertCommentsIntoScratchpad(boolean insertCommentsIntoScratchpad) {
		this.insertCommentsIntoScratchpad = insertCommentsIntoScratchpad;
	}

	@Transient
    @JsonIgnore
    public String getClassName() {
        return "Organisation";
    }

    public String getCreatorEmail() {
        return creatorEmail;
    }

    /**
     * @param newCreatorEmail the creatorEmail to set
     */
    @Email
    public void setCreatorEmail(String newCreatorEmail) {
        this.creatorEmail = newCreatorEmail;
    }

    /**
     * @return the description
     */
    @Lob
    @Length(max = 1431655761)
    public String getDescription() {
        return description;
    }

    /**
     * @param newDescription the description to set
     */
    public void setDescription(String newDescription) {
        this.description = newDescription;
    }

    /**
     * @return the logoUrl
     */
    @URL
    public String getLogoUrl() {
        return logoUrl;
    }

    /**
     * @param logoUrl the logoUrl to set
     */
    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    /**
     * @return the publisherName
     */
    public String getPublisherName() {
        return publisherName;
    }

    /**
     * @param newPublisherName the publisherName to set
     */
    public void setPublisherName(String newPublisherName) {
        this.publisherName = newPublisherName;
    }

    /**
     * @return the publisherEmail
     */
    public String getPublisherEmail() {
        return publisherEmail;
    }

    /**
     * @param newPublisherEmail the publisherEmail to set
     */
    public void setPublisherEmail(String newPublisherEmail) {
        this.publisherEmail = newPublisherEmail;
    }

    /**
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param newSubject the subject to set
     */
    public void setSubject(String newSubject) {
        this.subject = newSubject;
    }

    /**
     * @return the title
     */
    @NotEmpty
    public String getTitle() {
        return title;
    }

    /**
     * @param newTitle the title to set
     */
    public void setTitle(String newTitle) {
        this.title = newTitle;
    }

    /**
	 * @return the bibliographicCitation
	 */
	public String getBibliographicCitation() {
		return bibliographicCitation;
	}

	/**
	 * @param bibliographicCitation the bibliographicCitation to set
	 */
	public void setBibliographicCitation(String bibliographicCitation) {
		this.bibliographicCitation = bibliographicCitation;
	}

	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}

	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}

	/**
     * @return the jobs
     */
	@JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organisation")
    @Cascade(CascadeType.DELETE)
    @OrderBy("lastHarvested DESC")
    public Set<Resource> getResources() {
        return resources;
    }

    /**
     * @param resources the jobs to set
     */
	@JsonIgnore
    public void setResources(Set<Resource> resources) {
        this.resources = resources;
    }
	
	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "annotatedObjId")
    @Where(clause = "annotatedObjType = 'Organisation'")
    @Cascade({ CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.DELETE })
    @JsonIgnore
    public Set<Annotation> getAnnotations() {
        return annotations;
    }

    /**
     * @param annotations
     *            the annotations to set
     */
    public void setAnnotations(Set<Annotation> annotations) {
        this.annotations = annotations;
    }
    
    /**
     * @return the commentsEmailedTo
     */
    public String getCommentsEmailedTo() {
        return commentsEmailedTo;
    }

    /**
     * @param commentsEmailedTo the commentsEmailedTo to set
     */
    @NotBlank
    public void setCommentsEmailedTo(String commentsEmailedTo) {
        this.commentsEmailedTo = commentsEmailedTo;
    }

    public static int nullSafeStringComparator(final String one, final String two) {
        if (one == null ^ two == null) {
            return (one == null) ? -1 : 1;
        }

        if (one == null && two == null) {
            return 0;
        }

        return one.compareToIgnoreCase(two);
    }


	@Override
	public int compareTo(Organisation o) {
		
		return nullSafeStringComparator(this.title, o.title);
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
    	if(getAuthority() != null) {
			sid.addField("base.authority_s", getAuthority().getIdentifier());
		}
    	sid.addField("organisation.bibliographic_citation_s",getBibliographicCitation());
    	sid.addField("organisation.creator_t",getCreator());
    	sid.addField("organisation.description_t",getDescription());
    	sid.addField("organisation.publisher_name_t",getPublisherName());
    	sid.addField("organisation.subject_t",getSubject());
    	sid.addField("organisation.title_t",getTitle());
    	sid.addField("searchable.label_sort", getTitle());
    	StringBuilder summary = new StringBuilder().append(getBibliographicCitation()).append(" ")
    	.append(getCreator()).append(" ").append(getDescription()).append(" ")
    	.append(getPublisherName()).append(" ").append(getSubject()).append(" ").append(getTitle());
    	sid.addField("searchable.solrsummary_t", summary);
		return sid;
	}

	@Transient
    @JsonIgnore
	public Collection<String> getCommentDestinations() {
		Set<String> destinations = new HashSet<String>();
		if(this.commentsEmailedTo != null && !this.commentsEmailedTo.isEmpty()) {
			destinations.add(commentsEmailedTo);
		}
		if(this.insertCommentsIntoScratchpad) {
			destinations.add(this.identifier);
		}
		return destinations;
	}
}
