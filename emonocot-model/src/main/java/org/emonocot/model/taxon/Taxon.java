package org.emonocot.model.taxon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;
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
     * @param name Set the taxonomic name of the taxon
     */
    public void setName(String name) {
        this.name = name;
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
     * @param parent Set the taxonomic parent
     */
    public void setParent(Taxon parent) {
        this.parent = parent;
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
     * @param children Set the taxonomic children
     */
    public void setChildren(Set<Taxon> children) {
        this.children = children;
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
     * @param accepted Set the accepted name
     */
    public void setAccepted(Taxon accepted) {
        this.accepted = accepted;
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
     * @param synonyms Set the synonyms of this taxon
     */
    public void setSynonyms(Set<Taxon> synonyms) {
        this.synonyms = synonyms;
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
     * @param distribution Set the distribution associated with this taxon
     */
    public void setDistribution(Map<GeographicalRegion, Distribution> distribution) {
        this.distribution = distribution;
    }
}
