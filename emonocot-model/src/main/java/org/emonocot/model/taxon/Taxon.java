package org.emonocot.model.taxon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonManagedReference;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.emonocot.model.common.Annotation;
import org.emonocot.model.common.SearchableObject;
import org.emonocot.model.description.Distribution;
import org.emonocot.model.description.Feature;
import org.emonocot.model.description.TextContent;
import org.emonocot.model.geography.GeographicalRegion;
import org.emonocot.model.hibernate.SpatialFilterFactory;
import org.emonocot.model.identifier.Identifier;
import org.emonocot.model.key.IdentificationKey;
import org.emonocot.model.marshall.json.ImageDeserializer;
import org.emonocot.model.marshall.json.ImageSerializer;
import org.emonocot.model.marshall.json.ReferenceDeserializer;
import org.emonocot.model.marshall.json.ReferenceSerializer;
import org.emonocot.model.marshall.json.TaxonDeserializer;
import org.emonocot.model.marshall.json.TaxonSerializer;
import org.emonocot.model.media.Image;
import org.emonocot.model.reference.Reference;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Where;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Fields;
import org.hibernate.search.annotations.FullTextFilterDef;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

/**
 * @author ben
 */
@Entity
@Indexed(index = "org.emonocot.model.common.SearchableObject")
@FullTextFilterDef(name = "spatialFilter", impl = SpatialFilterFactory.class)
public class Taxon extends SearchableObject {

    /**
     *
     */
    private static final long serialVersionUID = -3511287213090466877L;

    /**
     *
     */
    private boolean deleted;

    /**
     *
     */
    private String name;

    /**
     *
     */
    private Taxon parent;

    /**
     *
     */
    private Set<Taxon> children = new HashSet<Taxon>();

    /**
     *
     */
    private Taxon accepted;

    /**
     *
     */
    private Set<Taxon> synonyms = new HashSet<Taxon>();

    /**
     *
     */
    private List<Image> images = new ArrayList<Image>();

    /**
     *
     */
    private Set<IdentificationKey> keys = new HashSet<IdentificationKey>();

    /**
     *
     */
    private Set<Reference> references = new HashSet<Reference>();

    /**
     *
     */
    private Map<Feature, TextContent> content = new HashMap<Feature, TextContent>();

    /**
     *
     */
    private Map<GeographicalRegion, Distribution> distribution = new HashMap<GeographicalRegion, Distribution>();

    /**
     *
     */
    private String authorship;

    /**
     *
     */
    private String basionymAuthorship;

    /**
     *
     */
    private String uninomial;

    /**
     *
     */
    private String genus;

    /**
     *
     */
    private String specificEpithet;

    /**
     *
     */
    private String infraSpecificEpithet;

    /**
     *
     */
    private Rank rank;

    /**
     *
     */
    private TaxonomicStatus status;

    /**
     *
     */
    private String infraGenericEpithet;

    /**
     *
     */
    private String accordingTo;

    /**
     *
     */
    private String family;

    /**
     *
     */
    private String kingdom;

    /**
     *
     */
    private String phylum;

    /**
     *
     */
    private String clazz;

    /**
     *
     */
    private String order;
    
    /**
     *
     */
    private String subfamily;
    
    /**
     *
     */
    private String tribe;
    
    /**
     *
     */
    private String subtribe;

    /**
     *
     */
    private NomenclaturalCode nomenclaturalCode;

    /**
     *
     */
    private Set<Annotation> annotations = new HashSet<Annotation>();

    /**
     *
     */
    private Reference protologue;

    /**
     *
     */
    private String protologueMicroReference;

    /**
     *
     */
    private Long id;

    /**
     *
     */
    private Image image;

    /**
     *
     */
    private List<Taxon> ancestors = new ArrayList<Taxon>();

    /**
     *
     */
    private String nameIdentifier;

    /**
     *
     */
    private Set<Identifier> identifiers = new HashSet<Identifier>();

    /**
     * @param newId
     *            Set the identifier of this object.
     */
    public void setId(Long newId) {
        this.id = newId;
    }

    /**
     * @return Get the identifier for this object.
     */
    @Id
    @GeneratedValue(generator = "system-increment")
    @DocumentId
    public Long getId() {
        return id;
    }

    /**
     * @return a list of images of the taxon
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "Taxon_Image", joinColumns = {@JoinColumn(name = "Taxon_id")}, inverseJoinColumns = {@JoinColumn(name = "images_id")})
    @Cascade({ CascadeType.SAVE_UPDATE })
    @JsonSerialize(contentUsing = ImageSerializer.class)
    public List<Image> getImages() {
        return images;
    }

    /**
     * @return the image
     */
    @ManyToOne(fetch = FetchType.LAZY)
    public Image getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * @return a list of references about the taxon
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @Cascade({CascadeType.SAVE_UPDATE})
    @JoinTable(name = "Taxon_Reference", joinColumns = {@JoinColumn(name = "Taxon_id")}, inverseJoinColumns = {@JoinColumn(name = "references_id")})
    @JsonSerialize(contentUsing = ReferenceSerializer.class)
    public Set<Reference> getReferences() {
        return references;
    }

    /**
     * @return a map of content about the taxon, indexed by the subject
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "taxon", orphanRemoval = true)
    @Cascade({CascadeType.ALL })
    @MapKey(name = "feature")
    @JsonManagedReference("content-taxon")
    @IndexedEmbedded
    public Map<Feature, TextContent> getContent() {
        return content;
    }

    /**
     * @param newImages
     *            Set the images associated with this taxon
     */
    @JsonDeserialize(contentUsing = ImageDeserializer.class)
    public void setImages(List<Image> newImages) {
        this.images = newImages;
    }

    /**
     * @param newReferences
     *            Set the references associated with this taxon
     */
    @JsonDeserialize(contentUsing = ReferenceDeserializer.class)
    public void setReferences(Set<Reference> newReferences) {
        this.references = newReferences;
    }

    /**
     * @param newContent
     *            Set the content associated with this taxon
     */
    @JsonManagedReference("content-taxon")
    public void setContent(Map<Feature, TextContent> newContent) {
        this.content = newContent;
    }

    /**
     * @param newDeleted
     *            Should this taxon be deleted?
     */
    public void setDeleted(boolean newDeleted) {
        this.deleted = newDeleted;
    }

    /**
     * @return whether this taxon should be deleted or not
     */
    @Transient
    @JsonIgnore
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * @return the full taxonomic name of the taxon, including authority
     */
    @Fields(value = {@Field, @Field(name = "label", index = Index.UN_TOKENIZED)})
    @Size(max = 128)
    public String getName() {
        return name;
    }

    /**
     * @param newName
     *            Set the taxonomic name of the taxon
     */
    public void setName(String newName) {
        this.name = newName;
    }

    /**
     * @return the immediate taxonomic parent
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @Cascade({CascadeType.SAVE_UPDATE})
    @JsonSerialize(using = TaxonSerializer.class)
    public Taxon getParent() {
        return parent;
    }

    /**
     * @param newParent
     *            Set the taxonomic parent
     */
    @JsonDeserialize(using = TaxonDeserializer.class)
    public void setParent(Taxon newParent) {
        this.parent = newParent;
    }

    /**
     * @return Get the immediate taxonomic children
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    //@Cascade({CascadeType.SAVE_UPDATE })
    @JsonIgnore
    public Set<Taxon> getChildren() {
        return children;
    }

    /**
     * @param newChildren
     *            Set the taxonomic children
     */
    public void setChildren(Set<Taxon> newChildren) {
        this.children = newChildren;
    }

    /**
     * @return get the accepted name of this synonym
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @Cascade({CascadeType.SAVE_UPDATE})
    @JsonSerialize(using = TaxonSerializer.class)
    public Taxon getAccepted() {
        return accepted;
    }

    /**
     * @param newAccepted
     *            Set the accepted name
     */
    @JsonDeserialize(using = TaxonDeserializer.class)
    public void setAccepted(Taxon newAccepted) {
        this.accepted = newAccepted;
    }

    /**
     * @return the synonyms of this taxon
     */
    @IndexedEmbedded(depth = 1)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "accepted")
    //@Cascade({CascadeType.SAVE_UPDATE})
    @JsonIgnore
    public Set<Taxon> getSynonyms() {
        return synonyms;
    }

    /**
     * @param newSynonyms
     *            Set the synonyms of this taxon
     */
    public void setSynonyms(Set<Taxon> newSynonyms) {
        this.synonyms = newSynonyms;
    }

    /**
     * @return the distribution associated with this taxon
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "taxon", orphanRemoval = true)
    @Cascade({CascadeType.ALL})
    @MapKey(name = "region")
    @IndexedEmbedded
    @JsonManagedReference("distribution-taxon")
    public Map<GeographicalRegion, Distribution> getDistribution() {
        return distribution;
    }

    /**
     * @param newDistribution
     *            Set the distribution associated with this taxon
     */
    @JsonManagedReference("distribution-taxon")
    public void setDistribution(
            Map<GeographicalRegion, Distribution> newDistribution) {
        this.distribution = newDistribution;
    }

    /**
     * @param newAuthorship
     *            set the authorship
     */
    public void setAuthorship(String newAuthorship) {
        this.authorship = newAuthorship;
    }

    /**
     * @param newBasionymAuthorship
     *            set the basionymAuthorship
     */
    public void setBasionymAuthorship(String newBasionymAuthorship) {
        this.basionymAuthorship = newBasionymAuthorship;
    }

    /**
     * @param newUninomial
     *            Set the uninomial
     */
    public void setUninomial(String newUninomial) {
        this.uninomial = newUninomial;
    }

    /**
     * @param newGenusPart
     *            Set the genus part of the name
     */
    public void setGenus(String newGenusPart) {
        this.genus = newGenusPart;
    }

    /**
     * @param newSpecificEpithet
     *            set the specific epithet
     */
    public void setSpecificEpithet(String newSpecificEpithet) {
        this.specificEpithet = newSpecificEpithet;
    }

    /**
     * @param newInfraspecificEpithet
     *            Set the infraspecific epithet
     */
    public void setInfraSpecificEpithet(String newInfraspecificEpithet) {
        this.infraSpecificEpithet = newInfraspecificEpithet;
    }

    /**
     * @param newRank
     *            set the rank of this taxon
     */
    public void setRank(Rank newRank) {
        this.rank = newRank;
    }

    /**
     * @return the authorship
     */
    @Field
    @Size(max = 128)
    public String getAuthorship() {
        return authorship;
    }

    /**
     * @return the basionymAuthorship
     */
    @Field
    @Size(max = 128)
    public String getBasionymAuthorship() {
        return basionymAuthorship;
    }

    /**
     * @return the uninomial
     */
    @Field
    @Size(max = 128)
    public String getUninomial() {
        return uninomial;
    }

    /**
     * @return the genus
     */
    @Field
    @Size(max = 128)
    public String getGenus() {
        return genus;
    }

    /**
     * @return the specificEpithet
     */
    @Field
    @Size(max = 128)
    public String getSpecificEpithet() {
        return specificEpithet;
    }

    /**
     * @return the infraSpecificEpithet
     */
    @Field
    @Size(max = 128)
    public String getInfraSpecificEpithet() {
        return infraSpecificEpithet;
    }

    /**
     * @return the rank
     */
    @Field
    @Enumerated(value = EnumType.STRING)
    public Rank getRank() {
        return rank;
    }

    /**
     * @param newStatus
     *            Set the taxonomic status
     */
    public void setStatus(TaxonomicStatus newStatus) {
        this.status = newStatus;
    }

    @Field
    @Enumerated(value = EnumType.STRING)
    public TaxonomicStatus getStatus() {
        return status;
    }

    /**
     * @param newInfraGenericEpithet
     *            Set the infrageneric epithet
     */
    public void setInfraGenericEpithet(String newInfraGenericEpithet) {
        this.infraGenericEpithet = newInfraGenericEpithet;
    }

    /**
     * @return the infrageneric epithet
     */
    @Field
    @Size(max = 128)
    public String getInfraGenericEpithet() {
        return infraGenericEpithet;
    }

    /**
     * @param newAccordingTo
     *            Set the according to
     */
    public void setAccordingTo(String newAccordingTo) {
        this.accordingTo = newAccordingTo;
    }

    /**
     * @return the according to
     */
    @Field
    @Size(max = 128)
    public String getAccordingTo() {
        return accordingTo;
    }

    /**
     * @param newFamily
     *            set the family
     */
    public void setFamily(String newFamily) {
        this.family = newFamily;
    }

    /**
     * @param newKingdom
     *            set the kingdom
     */
    public void setKingdom(String newKingdom) {
        this.kingdom = newKingdom;
    }

    /**
     * @param newPhylum
     *            set the phylum
     */
    public void setPhylum(String newPhylum) {
        this.phylum = newPhylum;
    }

    /**
     * @param newClass
     *            set the class
     */
    public void setClazz(String newClass) {
        this.clazz = newClass;
    }

    /**
     * @return the taxonomic class the taxon classified in
     */
    @Field
    @Column(name = "class")
    public String getClazz() {
        return clazz;
    }

    /**
     * @param newOrder
     *            set the order
     */
    public void setOrder(String newOrder) {
        this.order = newOrder;
    }

    /**
     * @param newNomenclaturalCode
     */
    public void setNomenclaturalCode(NomenclaturalCode newNomenclaturalCode) {
        this.nomenclaturalCode = newNomenclaturalCode;
    }

    /**
     * @return the nomenclatural code
     */
    @Enumerated(EnumType.STRING)
    public NomenclaturalCode getNomenclaturalCode() {
        return nomenclaturalCode;
    }

    /**
     * @return the family
     */
    @Fields({@Field,
            @Field(analyzer = @Analyzer(definition = "facetAnalyzer"))})
    @Size(max = 128)
    public String getFamily() {
        return family;
    }

    /**
     * @return the kingdom
     */
    @Field
    @Size(max = 128)
    public String getKingdom() {
        return kingdom;
    }

    /**
     * @return the phylum
     */
    @Field
    @Size(max = 128)
    public String getPhylum() {
        return phylum;
    }

    /**
     * @return the order
     */
    @Field
    @Column(name = "ordr")
    @Size(max = 128)
    public String getOrder() {
        return order;
    }

    /**
     * @return the annotations
     */
    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "annotatedObjId")
    @Where(clause = "annotatedObjType = 'Taxon'")
    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.DELETE})
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
     * @param reference
     *            set the protologue
     */
    @JsonDeserialize(using = ReferenceDeserializer.class)
    public void setProtologue(Reference reference) {
        this.protologue = reference;
    }

    /**
     * @return the protologue
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @Cascade({CascadeType.SAVE_UPDATE})
    @JsonSerialize(using = ReferenceSerializer.class)
    @IndexedEmbedded
    public Reference getProtologue() {
        return protologue;
    }

    /**
     * @return the protologueMicroReference
     */
    @Size(max = 128)
    public String getProtologueMicroReference() {
        return protologueMicroReference;
    }

    /**
     * @param protologueMicroReference
     *            the protologueMicroReference to set
     */
    public void setProtologueMicroReference(String protologueMicroReference) {
        this.protologueMicroReference = protologueMicroReference;
    }

    /**
     * @param feature
     *            set the feature
     * @return content or null if this taxon has no content
     */
    @Transient
    @JsonIgnore
    public TextContent getContent(Feature feature) {
        return content.get(feature);
    }

    /**
     * @return the ancestors of the taxon
     */
    @Transient
    public List<Taxon> getAncestors() {
        return ancestors;
    }

    /**
     * @param ancestors
     *            Set the ancestors of the taxon
     */
    public void setAncestors(List<Taxon> ancestors) {
        this.ancestors = ancestors;
    }

    /**
     * @param newIdentifier
     *            Set the name identifier
     */
    public void setNameIdentifier(String newIdentifier) {
        this.nameIdentifier = newIdentifier;
    }

    /**
     *
     * @return the name identifier
     */
    public String getNameIdentifier() {
        return nameIdentifier;
    }

    /**
     * @return a list of identifiers the taxon
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "taxon", orphanRemoval = true)
    @Cascade({CascadeType.ALL })
    @JsonManagedReference("identifier-taxon")
    public Set<Identifier> getIdentifiers() {
        return identifiers;
    }

    /**
     * @param newIdentifiers
     *            Set the identifiers associated with this taxon
     */
    @JsonManagedReference("identifier-taxon")
    public void setIdentifiers(Set<Identifier> newIdentifiers) {
        this.identifiers = newIdentifiers;
    }

    /**
     * @return the keys
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "taxon")
    @JsonSerialize(contentUsing = ReferenceSerializer.class)
    public Set<IdentificationKey> getKeys() {
        return keys;
    }

    /**
     * @param keys the keys to set
     */
    @JsonDeserialize(contentUsing = ReferenceDeserializer.class)
    public void setKeys(Set<IdentificationKey> keys) {
        this.keys = keys;
    }

	/**
	 * @return the subfamily
	 */
    @Field
	public String getSubfamily() {
		return subfamily;
	}

	/**
	 * @param subfamily the subfamily to set
	 */
	public void setSubfamily(String subfamily) {
		this.subfamily = subfamily;
	}

	/**
	 * @return the tribe
	 */
    @Field
	public String getTribe() {
		return tribe;
	}

	/**
	 * @param tribe the tribe to set
	 */
	public void setTribe(String tribe) {
		this.tribe = tribe;
	}

	/**
	 * @return the subtribe
	 */
    @Field
	public String getSubtribe() {
		return subtribe;
	}

	/**
	 * @param subtribe the subtribe to set
	 */
	public void setSubtribe(String subtribe) {
		this.subtribe = subtribe;
	}
    
}
