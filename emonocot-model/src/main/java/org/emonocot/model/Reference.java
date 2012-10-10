package org.emonocot.model;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.emonocot.model.constants.ReferenceType;
import org.emonocot.model.hibernate.DatePublishedBridge;
import org.emonocot.model.marshall.json.TaxonDeserializer;
import org.emonocot.model.marshall.json.TaxonSerializer;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Where;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;

/**
 *
 * @author ben
 *
 */
@Entity
@Indexed
public class Reference extends BaseData {

    /**
     *
     */
    private static long serialVersionUID = -5928234699377084008L;

    /**
     *
     */
    private String title;

    /**
     *
     */
    private String date;

    /**
     *
     */
    private ReferenceType type;

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
    private String bibliographicCitation;
    
    /**
     *
     */
    private Locale language;

    /**
     *
     */
    private Set<Taxon> taxa = new HashSet<Taxon>();

    /**
    *
    */
    private Set<Annotation> annotations = new HashSet<Annotation>();

    /**
     *
     */
    private Long id;

    /**
     *
     */
    private String description;

    /**
     *
     */
    private String subject;
    
    /**
     *
     */
    private String taxonRemarks;

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
     * @return the author
     */
    @Field
    public String getCreator() {
        return creator;
    }

    /**
     *
     * @param newCreator
     *            Set the author
     */
    public void setCreator(String newCreator) {
        this.creator = newCreator;
    }

    /**
     *
     * @return the publication this reference was published in
     */
    @Field
    public String getSource() {
        return source;
    }

    /**
     *
     * @param newSource
     *            Set the publication this reference was published in
     */
    public void setSource(String newSource) {
        this.source = newSource;
    }

    /**
     *
     * @return the full citation
     */
    @Field
    public String getBibliographicCitation() {
        return bibliographicCitation;
    }

    /**
     *
     * @param newCitation
     *            Set the full citation
     */
    public void setBibliographicCitation(String newCitation) {
        this.bibliographicCitation = newCitation;
    }

    /**
     *
     * @param newTitle
     *            set the title
     */
    public void setTitle(String newTitle) {
        this.title = newTitle;
    }

    /**
     *
     * @param newReferenceType
     *            set the reference type
     */
    public void setType(ReferenceType newReferenceType) {
        this.type = newReferenceType;
    }

    /**
     * @return the datePublished
     */
    @Field(bridge = @FieldBridge(impl = DatePublishedBridge.class))
    public String getDate() {
        return date;
    }

    /**
     * @param newDatePublished
     *            the datePublished to set
     */
    public void setDate(String newDate) {
        this.date = newDate;
    }

    /**
     * @return the title
     */
    @Field
    public String getTitle() {
        return title;
    }

    /**
     * @return the type
     */
    public ReferenceType getType() {
        return type;
    }

    /**
     * The list of all taxa associated with this reference.
     *
     * @return a set of taxa
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "Taxon_Reference", joinColumns = {@JoinColumn(name = "references_id")}, inverseJoinColumns = {@JoinColumn(name = "Taxon_id")})
    @Cascade({ CascadeType.SAVE_UPDATE, CascadeType.MERGE })
    @JsonSerialize(contentUsing = TaxonSerializer.class)
    public Set<Taxon> getTaxa() {
        return taxa;
    }

    /**
     *
     * @param taxa
     *            Set the taxa associated with this reference
     */
    @JsonDeserialize(contentUsing = TaxonDeserializer.class)
    public void setTaxa(Set<Taxon> taxa) {
        this.taxa = taxa;
    }

    /**
     * @return the annotations
     */
    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "annotatedObjId")
    @Where(clause = "annotatedObjType = 'Reference'")
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
     * @return the abstract
     */
    @Lob
    @Field
    public String getDescription() {
        return description;
    }

    /**
     * @param description the abstract to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the keywords
     */
    @Field
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject the keywords to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Transient
    @JsonIgnore
    public String getClassName() {
        return "Reference";
    }

	/**
	 * @return the language
	 */
	public Locale getLanguage() {
		return language;
	}

	/**
	 * @param language the language to set
	 */
	public void setLanguage(Locale language) {
		this.language = language;
	}

	/**
	 * @return the taxonRemarks
	 */
	public String getTaxonRemarks() {
		return taxonRemarks;
	}

	/**
	 * @param taxonRemarks the taxonRemarks to set
	 */
	public void setTaxonRemarks(String taxonRemarks) {
		this.taxonRemarks = taxonRemarks;
	}

}
