/**
 * 
 */
package org.emonocot.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.validation.constraints.Size;

import org.apache.solr.common.SolrInputDocument;
import org.emonocot.model.constants.MediaFormat;
import org.emonocot.model.marshall.json.TaxonDeserializer;
import org.emonocot.model.marshall.json.TaxonSerializer;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author jk00kg
 * 
 * See <a href="http://rs.gbif.org/extension/gbif/1.0/multimedia.xml">http://rs.gbif.org/extension/gbif/1.0/multimedia.xml</a>
 */
@MappedSuperclass
public abstract class Multimedia extends SearchableObject implements NonOwned, Media {

    private static final long serialVersionUID = -8178055800655899536L;

    private String title;

    private String description;

    private MediaFormat format;

    private Taxon taxon;

    private Set<Taxon> taxa = new HashSet<Taxon>();

    private Set<Annotation> annotations = new HashSet<Annotation>();

    private String creator;

    private String references;

    private String contributor;

    private String publisher;

    private String audience;

    private List<Comment> comments = new ArrayList<Comment>();
    
    private String source;

    /**
     * @return the title
     */
    @Size(max = 255)
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the description
     */
    @Lob
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the format
     */
    @Enumerated(EnumType.STRING)
    public MediaFormat getFormat() {
        return format;
    }

    /**
     * @param format the format to set
     */
    public void setFormat(MediaFormat format) {
        this.format = format;
    }

    /**
     * @return the taxon
     */
    @ManyToOne(fetch = FetchType.LAZY)
    public Taxon getTaxon() {
        return taxon;
    }

    /**
     * @param taxon the taxon to set
     */
    public void setTaxon(Taxon taxon) {
        this.taxon = taxon;
    }

    /**
     * @return the taxa
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "Taxon_Image", joinColumns = {@JoinColumn(name = "images_id")}, inverseJoinColumns = {@JoinColumn(name = "Taxon_id")})
    @Cascade({ CascadeType.SAVE_UPDATE, CascadeType.MERGE })
    @JsonSerialize(contentUsing = TaxonSerializer.class)
    public Set<Taxon> getTaxa() {
        return taxa;
    }

    /**
     * @param taxa the taxa to set
     */
    @JsonDeserialize(contentUsing = TaxonDeserializer.class)
    public void setTaxa(Set<Taxon> taxa) {
        this.taxa = taxa;
    }

    /**
     * @return the creator
     */
    @Size(max = 255)
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
     * @return the references
     */
    @Size(max = 255)
    public String getReferences() {
        return references;
    }

    /**
     * @param references the references to set
     */
    public void setReferences(String references) {
        this.references = references;
    }

    /**
     * @return the contributor
     */
    @Size(max = 255)
    public String getContributor() {
        return contributor;
    }

    /**
     * @param contributor the contributor to set
     */
    public void setContributor(String contributor) {
        this.contributor = contributor;
    }

    /**
     * @return the publisher
     */
    @Size(max = 255)
    public String getPublisher() {
        return publisher;
    }

    /**
     * @param publisher the publisher to set
     */
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    /**
     * @return the audience
     */
    @Size(max = 255)
    public String getAudience() {
        return audience;
    }

    /**
     * @param audience the audience to set
     */
    public void setAudience(String audience) {
        this.audience = audience;
    }

    /**
     * @return the comments
     */
    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "commentPage_id")
    @OrderBy("created DESC")
    @Where(clause = "commentPage_type = 'Image'")
    @Cascade({ CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.DELETE })
    @JsonIgnore
    public List<Comment> getComments() {
        return comments;
    }

    /**
     * @param comments the comments to set
     */
    @JsonIgnore
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    /**
     * @return the annotations
     */
    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "annotatedObjId")
    @Where(clause = "annotatedObjType = 'Image'")
    @Cascade({ CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.DELETE })
    @JsonIgnore
    public Set<Annotation> getAnnotations() {
        return annotations;
    }

    /* (non-Javadoc)
     * @see org.emonocot.model.Annotated#setAnnotations(java.util.Set)
     */
    @Override
    public void setAnnotations(Set<Annotation> annotations) {
        this.annotations = annotations;
    }

    /**
     * @return the source
     */
    public String getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public SolrInputDocument toSolrInputDocument() {
        SolrInputDocument sid = super.toSolrInputDocument();
        sid.addField("searchable.label_sort", getTitle());

        if(getTaxon() != null) {
            //addField(sid,"taxon.class_s", getTaxon().getClazz());
            addField(sid,"taxon.family_ss", getTaxon().getFamily());
            addField(sid,"taxon.genus_ss", getTaxon().getGenus());
            //addField(sid,"taxon.kingdom_s", getTaxon().getKingdom());
            //addField(sid,"taxon.phylum_s", getTaxon().getPhylum());
            addField(sid,"taxon.order_s", getTaxon().getOrder());           
            addField(sid,"taxon.subfamily_ss", getTaxon().getSubfamily());
            addField(sid,"taxon.subgenus_s", getTaxon().getSubgenus());
            addField(sid,"taxon.subtribe_ss", getTaxon().getSubtribe());
            addField(sid,"taxon.tribe_ss", getTaxon().getTribe());
        }

        return sid;
    }

}
