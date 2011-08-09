package org.emonocot.model.taxon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.emonocot.model.common.Base;
import org.emonocot.model.description.Content;
import org.emonocot.model.description.Distribution;
import org.emonocot.model.description.Feature;
import org.emonocot.model.geography.GeographicalRegion;
import org.emonocot.model.media.Image;
import org.emonocot.model.reference.Reference;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
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
     * @return a list of images of the taxon
     */
    @ManyToMany(fetch = FetchType.LAZY)
    public List<Image> getImages() {
        return images;
    }

    /**
     *
     * @return a list of references about the taxon
     */
    @ManyToMany(fetch = FetchType.LAZY)
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
    public void setImages(final List<Image> newImages) {
        this.images = newImages;
    }

    /**
     *
     * @param newReferences
     *            Set the references associated with this taxon
     */
    public void setReferences(final Set<Reference> newReferences) {
        this.references = newReferences;
    }

    /**
     *
     * @param newContent Set the content associated with this taxon
     */
    public void setContent(final Map<Feature, Content> newContent) {
        this.content = newContent;
    }

    /**
     *
     * @param newDeleted Should this taxon be deleted?
     */
    public void setDeleted(final boolean newDeleted) {
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
    public void setName(final String newName) {
        this.name = newName;
    }

    /**
     *
     * @return the immediate taxonomic parent
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @Cascade({CascadeType.ALL })
    public Taxon getParent() {
        return parent;
    }

    /**
     *
     * @param newParent Set the taxonomic parent
     */
    public void setParent(final Taxon newParent) {
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
    public void setChildren(final Set<Taxon> newChildren) {
        this.children = newChildren;
    }

    /**
     *
     * @return get the accepted name of this synonym
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @Cascade({CascadeType.ALL })
    public Taxon getAccepted() {
        return accepted;
    }

    /**
     *
     * @param newAccepted Set the accepted name
     */
    public void setAccepted(final Taxon newAccepted) {
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
    public void setSynonyms(final Set<Taxon> newSynonyms) {
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
            final Map<GeographicalRegion, Distribution> newDistribution) {
        this.distribution = newDistribution;
    }

    /**
     *
     * @param newAuthorship set the authorship
     */
    public void setAuthorship(final String newAuthorship) {
        this.authorship = newAuthorship;
    }

    /**
     *
     * @param newBasionymAuthorship set the basionymAuthorship
     */
    public void setBasionymAuthorship(
            final String newBasionymAuthorship) {
        this.basionymAuthorship = newBasionymAuthorship;
    }

    /**
     *
     * @param newUninomial Set the uninomial
     */
    public void setUninomial(final String newUninomial) {
        this.uninomial = newUninomial;
    }

    /**
     *
     * @param newGenusPart Set the genus part of the name
     */
    public void setGenus(final String newGenusPart) {
        this.genus = newGenusPart;
    }

    /**
     *
     * @param newSpecificEpithet set the specific epithet
     */
    public void setSpecificEpithet(final String newSpecificEpithet) {
        this.specificEpithet = newSpecificEpithet;
    }

    /**
     *
     * @param newInfraspecificEpithet Set the infraspecific epithet
     */
    public void setInfraSpecificEpithet(
            final String newInfraspecificEpithet) {
        this.infraSpecificEpithet = newInfraspecificEpithet;
    }

    /**
     *
     * @param newRank set the rank of this taxon
     */
    public void setRank(final Rank newRank) {
        this.rank = newRank;
    }

    /**
     * @return the authorship
     */
    public String getAuthorship() {
        return authorship;
    }

    /**
     * @return the basionymAuthorship
     */
    public String getBasionymAuthorship() {
        return basionymAuthorship;
    }

    /**
     * @return the uninomial
     */
    public String getUninomial() {
        return uninomial;
    }

    /**
     * @return the genus
     */
    public String getGenus() {
        return genus;
    }

    /**
     * @return the specificEpithet
     */
    public String getSpecificEpithet() {
        return specificEpithet;
    }

    /**
     * @return the infraSpecificEpithet
     */
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
}
