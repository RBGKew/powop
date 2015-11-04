package org.emonocot.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;
import org.joda.time.DateTime;

/**
 * @see http://tdwg.github.io/dwc/terms/index.htm#Identification
 */
@Entity
public class Identification extends OwnedEntity {

	private static final long serialVersionUID = -5101558921584538122L;

	private Set<Annotation> annotations = new HashSet<>();

	private DateTime dateIdentified;

	private Long id;

	private String identificationQualifier;

	private String identificationReferences;

	private String identificationRemarks;

	private String identificationVerificationStatus;

	private String identifiedBy;

	private Taxon taxon;

	private String typeStatus;

	@Override
	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "annotatedObjId")
	@Where(clause = "annotatedObjType = 'Identification'")
	@Cascade({ CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.DELETE })
	public Set<Annotation> getAnnotations() {
		return annotations;
	}

	@Type(type="dateTimeUserType")
	public DateTime getDateIdentified() {
		return dateIdentified;
	}

	@Id
	@GeneratedValue(generator = "table-hilo", strategy = GenerationType.TABLE)
	public Long getId() {
		return id;
	}

	public String getIdentificationQualifier() {
		return identificationQualifier;
	}

	public String getIdentificationReferences() {
		return identificationReferences;
	}

	public String getIdentificationRemarks() {
		return identificationRemarks;
	}

	public String getIdentificationVerificationStatus() {
		return identificationVerificationStatus;
	}

	public String getIdentifiedBy() {
		return identifiedBy;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Taxon getTaxon() {
		return taxon;
	}

	public String getTypeStatus() {
		return typeStatus;
	}

	@Override
	public void setAnnotations(Set<Annotation> annotations) {
		this.annotations = annotations;
	}

	public void setDateIdentified(DateTime dateIdentified) {
		this.dateIdentified = dateIdentified;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setIdentificationQualifier(String identificationQualifier) {
		this.identificationQualifier = identificationQualifier;
	}

	public void setIdentificationReferences(String identificationReferences) {
		this.identificationReferences = identificationReferences;
	}

	public void setIdentificationRemarks(String identificationRemarks) {
		this.identificationRemarks = identificationRemarks;
	}

	public void setIdentificationVerificationStatus(String status) {
		this.identificationVerificationStatus = status;
	}

	public void setIdentifiedBy(String identifiedBy) {
		this.identifiedBy = identifiedBy;
	}

	@Override
	public void setTaxon(Taxon taxon) {
		this.taxon = taxon;
	}

	public void setTypeStatus(String typeStatus) {
		this.typeStatus = typeStatus;
	}
}
