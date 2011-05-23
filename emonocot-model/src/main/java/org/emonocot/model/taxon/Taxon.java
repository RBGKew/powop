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
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.emonocot.model.common.Base;
import org.emonocot.model.description.Content;
import org.emonocot.model.description.Feature;
import org.emonocot.model.media.Image;
import org.emonocot.model.reference.Reference;

/**
 *
 * @author ben
 *
 */
@Entity
public class Taxon extends Base {

    /**
     *
     */
    private boolean deleted;

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
}
