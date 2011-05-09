package org.emonocot.checklist.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.emonocot.model.geography.Country;
import org.emonocot.model.geography.Region;

/**
 *
 * @author ben
 *
 */
@Entity
@Table(name = "Plant_Locality")
@TypeDef(name = "regionUserType", typeClass = RegionUserType.class)
public class Distribution {

    /**
     *
     */
    @Column(name = "Region_code_L2")
    @Type(type = "regionUserType")
    private Region region;

    /**
     *
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "Area_code_L3", length = Country.COUNTRY_CODE_LENGTH)
    private Country country;

    /**
     *
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Plant_name_id")
    private Taxon taxon;

    /**
     *
     */
    @Id
    @Column(name = "Plant_locality_id")
    private Long id;

    /**
     *
     * @return the country of this distribution record or null if a country is
     *         not mentioned
     */
    public final Country getCountry() {
        return country;
    }

    /**
     *
     * @return the region of this distribution record or null if a region is
     *         not mentioned
     */
    public final Region getRegion() {
        return region;
    }

    /**
     * Empty class constructor.
     */
    public Distribution() { }

    /**
     *
     * @param newTaxon The taxon this distribution record refers to
     * @param newRegion The region mentioned in this distribution record
     * @param newCountry The country mentioned in this distribution record
     */
    public Distribution(final Taxon newTaxon, final Region newRegion,
            final Country newCountry) {
        this.taxon = newTaxon;
        this.region = newRegion;
        this.country = newCountry;
    }
}
