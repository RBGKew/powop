package org.emonocot.model;

import java.util.Locale;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.emonocot.model.geography.GeographicalRegion;
import org.gbif.ecat.voc.LifeStage;
import org.gbif.ecat.voc.Sex;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Indexed;

@Entity
@Indexed
public class VernacularName extends BaseData {
	
	private static final long serialVersionUID = 5439026066792559240L;
	
	/**
	 * 
	 */
	private Long id;
	
	/**
	 *
	 */
	private String vernacularName;
	
	/**
	 *
	 */
	private String source;
	
	/**
	 *
	 */
	private Locale language;
	
	/**
	 *
	 */
	private String temporal;
	
	/**
	 *
	 */
	private GeographicalRegion location;
	
	/**
	 *
	 */
	private String locality;
	
	/**
	 *
	 */
	private String countryCode;
	
	/**
	 *
	 */
	private Sex sex;
	
	/**
	 *
	 */
	private LifeStage lifeStage;
	
	/**
	 *
	 */
	private Boolean preferredName;
	
	/**
	 *
	 */
	private Boolean plural;
	
	/**
	 *
	 */
	private String organismPart;
	
	/**
	 * 
	 */
	private String taxonRemarks;
	
	/**
	 *
	 */
	private Taxon taxon;

	public String getVernacularName() {
		return vernacularName;
	}

	public void setVernacularName(String vernacularName) {
		this.vernacularName = vernacularName;
	}

	public String getTemporal() {
		return temporal;
	}

	public void setTemporal(String temporal) {
		this.temporal = temporal;
	}

	@ManyToOne(fetch = FetchType.LAZY)
    @ContainedIn
    @JsonBackReference("vernacularNames-taxon")
	public Taxon getTaxon() {
		return taxon;
	}
	
	@JsonBackReference("vernacularNames-taxon")
	public void setTaxon(Taxon taxon) {
		this.taxon = taxon;
	}

	@Override
	@Id
    @GeneratedValue(generator = "system-increment")
    @DocumentId
	public Long getId() {
		return id;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Locale getLanguage() {
		return language;
	}

	public void setLanguage(Locale language) {
		this.language = language;
	}

	@Enumerated(value = EnumType.STRING)
	public GeographicalRegion getLocation() {
		return location;
	}

	public void setLocation(GeographicalRegion location) {
		this.location = location;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	@Enumerated(value = EnumType.STRING)
	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public LifeStage getLifeStage() {
		return lifeStage;
	}

	public void setLifeStage(LifeStage lifeStage) {
		this.lifeStage = lifeStage;
	}

	public Boolean getPreferredName() {
		return preferredName;
	}

	public void setPreferredName(Boolean preferredName) {
		this.preferredName = preferredName;
	}

	public Boolean getPlural() {
		return plural;
	}

	public void setPlural(Boolean plural) {
		this.plural = plural;
	}

	public String getOrganismPart() {
		return organismPart;
	}

	public void setOrganismPart(String organismPart) {
		this.organismPart = organismPart;
	}

	public String getTaxonRemarks() {
		return taxonRemarks;
	}

	public void setTaxonRemarks(String taxonRemarks) {
		this.taxonRemarks = taxonRemarks;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Transient
    @JsonIgnore
    public final String getClassName() {
        return "VernacularName";
    }
}
