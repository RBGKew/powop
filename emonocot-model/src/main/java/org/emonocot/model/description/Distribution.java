package org.emonocot.model.description;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.emonocot.model.common.Base;
import org.emonocot.model.geography.GeographicalRegion;
import org.emonocot.model.hibernate.DistributionBridge;
import org.emonocot.model.marshall.json.GeographicalRegionDeserializer;
import org.emonocot.model.taxon.Taxon;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.ClassBridge;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;

/**
 *
 * @author ben
 *
 */
@Entity
@Indexed
@ClassBridge(name = "geographicalRegion",
        impl = DistributionBridge.class,
        index = Index.UN_TOKENIZED)
public class Distribution extends Base {

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
    @Type(type="tdwgRegionUserType")
    public GeographicalRegion getRegion() {
        return region;
    }

   /**
    *
    * @param newTaxon
    *            Set the taxon that this distribution is about.
    */
   @JsonIgnore
   public void setTaxon(Taxon newTaxon) {
       this.taxon = newTaxon;
   }

  /**
   *
   * @return Get the taxon that this distribution is about.
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @ContainedIn
  @JsonIgnore
  public Taxon getTaxon() {
      return taxon;
  }

  @Transient
  @JsonIgnore
  public final String getClassName() {
    return "Distribution";
  }
}
