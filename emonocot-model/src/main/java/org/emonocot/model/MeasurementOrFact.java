package org.emonocot.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.emonocot.model.constants.MeasurementType;
import org.emonocot.model.constants.MeasurementUnit;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Indexed;
import org.joda.time.DateTime;

@Entity
@Indexed
public class MeasurementOrFact extends BaseData {
	
	private static final long serialVersionUID = -1400551717852313792L;
	
	/**
	 * 
	 */
	private Long id;
	
	private MeasurementType measurementType;
	
	private String measurementValue;
	
	private String measurementAccuracy;
	
	private MeasurementUnit measurementUnit;
	
	private DateTime measurementDeterminedDate;
	
	private String measurementDeterminedBy;
	
	private String measurementMethod;
	
	private String measurementRemarks;
	
	private Taxon taxon;

	@Id
    @GeneratedValue(generator = "system-increment")
    @DocumentId
	@Override
	public Long getId() {
		return id;
	}

	@Enumerated(value = EnumType.STRING)
	public MeasurementType getMeasurementType() {
		return measurementType;
	}

	public void setMeasurementType(MeasurementType measurementType) {
		this.measurementType = measurementType;
	}

	public String getMeasurementValue() {
		return measurementValue;
	}

	public void setMeasurementValue(String measurementValue) {
		this.measurementValue = measurementValue;
	}

	public String getMeasurementAccuracy() {
		return measurementAccuracy;
	}

	public void setMeasurementAccuracy(String measurementAccuracy) {
		this.measurementAccuracy = measurementAccuracy;
	}

	@Enumerated(value = EnumType.STRING)
	public MeasurementUnit getMeasurementUnit() {
		return measurementUnit;
	}

	public void setMeasurementUnit(MeasurementUnit measurementUnit) {
		this.measurementUnit = measurementUnit;
	}

	public DateTime getMeasurementDeterminedDate() {
		return measurementDeterminedDate;
	}

	public void setMeasurementDeterminedDate(DateTime measurementDeterminedDate) {
		this.measurementDeterminedDate = measurementDeterminedDate;
	}

	public String getMeasurementDeterminedBy() {
		return measurementDeterminedBy;
	}

	public void setMeasurementDeterminedBy(String measurementDeterminedBy) {
		this.measurementDeterminedBy = measurementDeterminedBy;
	}

	public String getMeasurementMethod() {
		return measurementMethod;
	}

	public void setMeasurementMethod(String measurementMethod) {
		this.measurementMethod = measurementMethod;
	}

	public String getMeasurementRemarks() {
		return measurementRemarks;
	}

	public void setMeasurementRemarks(String measurementRemarks) {
		this.measurementRemarks = measurementRemarks;
	}

	@ManyToOne(fetch = FetchType.LAZY)
    @ContainedIn
    @JsonBackReference("measurementsOrFacts-taxon")
	public Taxon getTaxon() {
		return taxon;
	}

	@JsonBackReference("measurementsOrFacts-taxon")
	public void setTaxon(Taxon taxon) {
		this.taxon = taxon;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
