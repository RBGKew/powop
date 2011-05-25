package org.emonocot.model.description;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import org.emonocot.model.common.Base;
import org.emonocot.model.geography.GeographicalRegion;
import org.emonocot.model.hibernate.Fetch;
import org.emonocot.model.taxon.Taxon;
import org.hibernate.annotations.Type;

/**
 *
 * @author ben
 *
 */
@Entity
public class Distribution extends Base {

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
   public void setTaxon(Taxon newTaxon) {
       this.taxon = newTaxon;
   }

  /**
   *
   * @return Get the taxon that this distribution is about.
   */
  @ManyToOne(fetch = FetchType.LAZY)
  public Taxon getTaxon() {
      return taxon;
  }
}
