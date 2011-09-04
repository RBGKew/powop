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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.emonocot.model.common.Annotation;
import org.emonocot.model.common.Base;
import org.emonocot.model.description.Content;
import org.emonocot.model.description.Distribution;
import org.emonocot.model.description.Feature;
import org.emonocot.model.geography.GeographicalRegion;
import org.emonocot.model.media.Image;
import org.emonocot.model.reference.Reference;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Where;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

/**
 *
 * @author ben
 *
 */
@Entity
@Indexed
public class Taxon extends Base {

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
    private Set<Reference> references = new HashSet<Reference>();

    /**
     *
     */
    private Map<Feature, Content> content = new HashMap<Feature, Content>();

    /**
     *
     */
    private Map<GeographicalRegion, Distribution> distribution
        = new HashMap<GeographicalRegion, Distribution>();

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
    private NomenclaturalCode nomenclaturalCode;

    /**
     *
     */
    private Set<Annotation> annotations = new HashSet<Annotation>();

    private Reference protologue;

    /**
     *
     * @return a list of images of the taxon
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @Cascade({CascadeType.SAVE_UPDATE})
    @JoinTable(
        name = "Taxon_Image",
        joinColumns = { @JoinColumn(name = "Taxon_id") },
        inverseJoinColumns = { @JoinColumn(name = "images_id") })
    public List<Image> getImages() {
        return images;
    }

    /**
     *
     * @return a list of references about the taxon
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @Cascade({CascadeType.SAVE_UPDATE})
    @JoinTable(
        name = "Taxon_Reference",
        joinColumns = { @JoinColumn(name = "Taxon_id") },
        inverseJoinColumns = { @JoinColumn(name = "references_id") })
    public Set<Reference> getReferences() {
        return references;
    }

    /**
     *
     * @return a map of content about the taxon, indexed by the subject
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "taxon")
    @MapKey(name = "feature")
    public Map<Feature, Content> getContent() {
        return content;
    }

    /**
     *
     * @param newImages
     *            Set the images associated with this taxon
     */
    public void setImages(List<Image> newImages) {
        this.images = newImages;
    }

    /**
     *
     * @param newReferences
     *            Set the references associated with this taxon
     */
    public void setReferences(Set<Reference> newReferences) {
        this.references = newReferences;
    }

    /**
     *
     * @param newContent Set the content associated with this taxon
     */
    public void setContent(Map<Feature, Content> newContent) {
        this.content = newContent;
    }

    /**
     *
     * @param newDeleted Should this taxon be deleted?
     */
    public void setDeleted(boolean newDeleted) {
        this.deleted = newDeleted;
    }

    /**
     *
     * @return whether this taxon should be deleted or not
     */
    @Transient
    public boolean isDeleted() {
        return deleted;
    }

    /**
     *
     * @return the full taxonomic name of the taxon, including authority
     */
    @Field
    public String getName() {
        return name;
    }

    /**
     *
     * @param newName Set the taxonomic name of the taxon
     */
    public void setName(String newName) {
        this.name = newName;
    }

    /**
     *
     * @return the immediate taxonomic parent
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @Cascade({CascadeType.SAVE_UPDATE })
    public Taxon getParent() {
        return parent;
    }

    /**
     *
     * @param newParent Set the taxonomic parent
     */
    public void setParent(Taxon newParent) {
        this.parent = newParent;
    }

    /**
     *
     * @return Get the immediate taxonomic children
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    @Cascade({CascadeType.SAVE_UPDATE })
    public Set<Taxon> getChildren() {
        return children;
    }

    /**
     *
     * @param newChildren Set the taxonomic children
     */
    public void setChildren(Set<Taxon> newChildren) {
        this.children = newChildren;
    }

    /**
     *
     * @return get the accepted name of this synonym
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @Cascade({CascadeType.SAVE_UPDATE })
    public Taxon getAccepted() {
        return accepted;
    }

    /**
     *
     * @param newAccepted Set the accepted name
     */
    public void setAccepted(Taxon newAccepted) {
        this.accepted = newAccepted;
    }

    /**
     *
     * @return the synonyms of this taxon
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "accepted")
    @Cascade({CascadeType.SAVE_UPDATE })
    public Set<Taxon> getSynonyms() {
        return synonyms;
    }

    /**
     *
     * @param newSynonyms Set the synonyms of this taxon
     */
    public void setSynonyms(Set<Taxon> newSynonyms) {
        this.synonyms = newSynonyms;
    }

    /**
     *
     * @return the distribution associated with this taxon
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "taxon", orphanRemoval = true)
    @Cascade({CascadeType.ALL })
    @MapKey(name = "region")
    @IndexedEmbedded
    public Map<GeographicalRegion, Distribution> getDistribution() {
        return distribution;
    }

    /**
     *
     * @param newDistribution Set the distribution associated with this taxon
     */
    public void setDistribution(
            Map<GeographicalRegion, Distribution> newDistribution) {
        this.distribution = newDistribution;
    }

    /**
     *
     * @param newAuthorship set the authorship
     */
    public void setAuthorship(String newAuthorship) {
        this.authorship = newAuthorship;
    }

    /**
     *
     * @param newBasionymAuthorship set the basionymAuthorship
     */
    public void setBasionymAuthorship(
            String newBasionymAuthorship) {
        this.basionymAuthorship = newBasionymAuthorship;
    }

    /**
     *
     * @param newUninomial Set the uninomial
     */
    public void setUninomial(String newUninomial) {
        this.uninomial = newUninomial;
    }

    /**
     *
     * @param newGenusPart Set the genus part of the name
     */
    public void setGenus(String newGenusPart) {
        this.genus = newGenusPart;
    }

    /**
     *
     * @param newSpecificEpithet set the specific epithet
     */
    public void setSpecificEpithet(String newSpecificEpithet) {
        this.specificEpithet = newSpecificEpithet;
    }

    /**
     *
     * @param newInfraspecificEpithet Set the infraspecific epithet
     */
    public void setInfraSpecificEpithet(
            String newInfraspecificEpithet) {
        this.infraSpecificEpithet = newInfraspecificEpithet;
    }

    /**
     *
     * @param newRank set the rank of this taxon
     */
    public void setRank(Rank newRank) {
        this.rank = newRank;
    }

    /**
     * @return the authorship
     */
    @Field
    public String getAuthorship() {
        return authorship;
    }

    /**
     * @return the basionymAuthorship
     */
    @Field
    public String getBasionymAuthorship() {
        return basionymAuthorship;
    }

    /**
     * @return the uninomial
     */
    @Field
    public String getUninomial() {
        return uninomial;
    }

    /**
     * @return the genus
     */
    @Field
    public String getGenus() {
        return genus;
    }

    /**
     * @return the specificEpithet
     */
    @Field
    public String getSpecificEpithet() {
        return specificEpithet;
    }

    /**
     * @return the infraSpecificEpithet
     */
    @Field
    public String getInfraSpecificEpithet() {
        return infraSpecificEpithet;
    }

    /**
     * @return the rank
     */
    @Enumerated(value = EnumType.STRING)
    public Rank getRank() {
        return rank;
    }

    /**
     *
     * @param newStatus Set the taxonomic status
     */
    public void setStatus(TaxonomicStatus newStatus) {
       this.status = newStatus;
    }

    @Enumerated(value = EnumType.STRING)
    public TaxonomicStatus getStatus() {
        return status;
    }

    /**
     *
     * @param newInfraGenericEpithet Set the infrageneric epithet
     */
    public void setInfraGenericEpithet(String newInfraGenericEpithet) {
        this.infraGenericEpithet = newInfraGenericEpithet;
    }

    /**
     * @return the infrageneric epithet
     */
    @Field
    public String getInfraGenericEpithet() {
        return infraGenericEpithet;
    }

    /**
     *
     * @param newAccordingTo Set the according to
     */
    public void setAccordingTo(String newAccordingTo) {
        this.accordingTo = newAccordingTo;
    }

    /**
     * @return the according to
     */
    @Field
    public String getAccordingTo() {
        return accordingTo;
    }

    /**
     *
     * @param newFamily set the family
     */
    public void setFamily(String newFamily) {
        this.family = newFamily;
    }

    /**
     *
     * @param newKingdom set the kingdom
     */
    public void setKingdom(String newKingdom) {
        this.kingdom = newKingdom;
    }

    /**
     *
     * @param newPhylum set the phylum
     */
    public void setPhylum(String newPhylum) {
        this.phylum = newPhylum;
    }

    /**
     *
     * @param newClass set the class
     */
    public void setClass(String newClass) {
        this.clazz = newClass;
    }

    /**
     *
     * @param newOrder set the order
     */
    public void setOrder(String newOrder) {
        this.order = newOrder;
    }

    /**
     *
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
    @Field
    public String getFamily() {
        return family;
    }

    /**
     * @return the kingdom
     */
    @Field
    public String getKingdom() {
        return kingdom;
    }

    /**
     * @return the phylum
     */
    @Field
    public String getPhylum() {
        return phylum;
    }

    /**
     * @return the order
     */
    @Field
    @Column(name = "ordr")
    public String getOrder() {
        return order;
    }

    /**
     * @return the annotations
     */
    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "annotatedObjId")
    @Where(clause = "annotatedObjType = 'Taxon'")
    @Cascade({CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.DELETE })
    public Set<Annotation> getAnnotations() {
        return annotations;
    }

    /**
     * @param annotations the annotations to set
     */
    public void setAnnotations(Set<Annotation> annotations) {
        this.annotations = annotations;
    }

    /**
     *
     * @param reference set the protologue
     */
    public void setProtologue(Reference reference) {
        this.protologue = reference;
    }

    /**
     * @return the protologue
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @Cascade({CascadeType.SAVE_UPDATE })
    public Reference getProtologue() {
        return protologue;
    }

    /**
     *
     * @param feature set the feature
     * @return content or null if this taxon has no content
     */
    @Transient
    public Content getContent(Feature feature) {
        return content.get(feature);
    }
}
