package org.emonocot.model.registry;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.emonocot.model.Annotation;
import org.emonocot.model.BaseData;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Where;
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
public class Organisation extends BaseData implements Comparable<Organisation> {

    /**
     *
     */
    private static long serialVersionUID = -2463044801110563816L;

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
     */
    private String bibliographicCitation;
    
    /**
     *
     */
    private String creator;

    /**
     *
     */
    private Set<Resource> resources;
    
    private Set<Annotation> annotations = new HashSet<Annotation>();

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
     * @param newUri
     *            the uri to set
     */
    public void setUri(String newUri) {
        this.uri = newUri;
    }

    /**
     *
     * @return the class name
     */
    @Transient
    @JsonIgnore
    public String getClassName() {
        return "Source";
    }

    /**
     * @return the creatorEmail
     */
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
}
