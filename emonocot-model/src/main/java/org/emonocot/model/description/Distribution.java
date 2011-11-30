package org.emonocot.model.description;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.emonocot.model.common.Annotation;
import org.emonocot.model.common.BaseData;
import org.emonocot.model.geography.GeographicalRegion;
import org.emonocot.model.hibernate.DistributionBridge;
import org.emonocot.model.marshall.json.GeographicalRegionDeserializer;
import org.emonocot.model.taxon.Taxon;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;
import org.hibernate.search.annotations.ClassBridge;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;

/**
 *
 * @author ben
 *
 */
@Entity
@Indexed
@ClassBridge(name = "geographicalRegion", impl = DistributionBridge.class, index = Index.UN_TOKENIZED)
public class Distribution extends BaseData {

    /**
     *
     */
    private static final long serialVersionUID = -970244833684895241L;

    /**
    *
    */
    private Taxon taxon;

    /**
     *
     */
    private GeographicalRegion region;

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
     * Set the lowest level this georegion is concerned with.
     *
     * @param geoRegion
     *            the geographical region this distribution is concerned with
     */
    @JsonDeserialize(using = GeographicalRegionDeserializer.class)
    public void setRegion(GeographicalRegion geoRegion) {
        this.region = geoRegion;
    }

    /**
     *
     * @return the lowest level geo region this distribution is concerned with
     */
    @Type(type = "tdwgRegionUserType")
    public GeographicalRegion getRegion() {
        return region;
    }

    /**
     *
     * @param newTaxon
     *            Set the taxon that this distribution is about.
     */
    @JsonBackReference("distribution-taxon")
    public void setTaxon(Taxon newTaxon) {
        this.taxon = newTaxon;
    }

    /**
     * @return the annotations
     */
    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "annotatedObjId")
    @Where(clause = "annotatedObjType = 'Distribution'")
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
     *
     * @return Get the taxon that this distribution is about.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @ContainedIn
    @JsonBackReference("distribution-taxon")
    public Taxon getTaxon() {
        return taxon;
    }

    @Transient
    @JsonIgnore
    public final String getClassName() {
        return "Distribution";
    }
}
