/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
package org.powo.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.apache.solr.common.SolrInputDocument;
import org.gbif.ecat.voc.NomenclaturalCode;
import org.gbif.ecat.voc.NomenclaturalStatus;
import org.gbif.ecat.voc.Rank;
import org.powo.model.constants.TaxonomicStatus;
import org.powo.model.marshall.json.TaxonSerializer;
import org.powo.model.registry.Organisation;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Where;
import org.powo.model.solr.TaxonSolrInputDocument;
import org.springframework.context.ApplicationContext;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * Schema definition: http://tdwg.github.io/dwc/terms/index.html#Taxon
 */
@Entity
@JsonInclude(Include.NON_EMPTY)
public class Taxon extends SearchableObject {

	private static final long serialVersionUID = -3511287213090466877L;

	@JsonIgnore
	private Long id;

	private String bibliographicCitation;

	@JsonProperty("name")
	private String scientificName;

	@JsonProperty("authors")
	private String scientificNameAuthorship;

	private String genus;

	@JsonProperty("infragenus")
	private String subgenus;

	@JsonProperty("species")
	private String specificEpithet;

	@JsonProperty("infraspecies")
	private String infraspecificEpithet;

	@JsonProperty("rank")
	private Rank taxonRank;

	private TaxonomicStatus taxonomicStatus;

	private String kingdom;

	private String phylum;

	private String clazz;

	private String order;

	private String family;

	@JsonProperty("infrafamily")
	private String subfamily;

	private String tribe;

	private String subtribe;

	private String scientificNameID;

	private NomenclaturalCode nomenclaturalCode;

	private String source;

	private Integer namePublishedInYear;

	@JsonIgnore
	private String verbatimTaxonRank;

	private String taxonRemarks;

	@JsonProperty("reference")
	private String namePublishedInString;

	private NomenclaturalStatus nomenclaturalStatus;

	@JsonIgnore
	private Set<Annotation> annotations = new HashSet<Annotation>();

	@JsonIgnore
	private Reference namePublishedIn;

	@JsonIgnore
	private Reference nameAccordingTo;

	@JsonSerialize(contentUsing = TaxonSerializer.class)
	@JsonProperty("classification")
	private List<Taxon> higherClassification = null;

	@JsonSerialize(using = TaxonSerializer.class)
	@JsonProperty("basionym")
	private Taxon originalNameUsage;

	@JsonSerialize(contentUsing = TaxonSerializer.class)
	@JsonProperty("basionymOf")
	private Set<Taxon> subsequentNameUsages = new HashSet<Taxon>();

	@JsonIgnore
	private Taxon parentNameUsage;

	@JsonSerialize(contentUsing = TaxonSerializer.class)
	private Set<Taxon> childNameUsages = new HashSet<Taxon>();

	@JsonSerialize(using = TaxonSerializer.class)
	@JsonProperty("accepted")
	private Taxon acceptedNameUsage;

	@JsonSerialize(contentUsing = TaxonSerializer.class)
	@JsonProperty("synonyms")
	private Set<Taxon> synonymNameUsages = new HashSet<Taxon>();

	@JsonIgnore
	private Set<Organisation> authorities = new HashSet<Organisation>();

	@JsonIgnore
	private List<Image> images = new ArrayList<Image>();

	@JsonIgnore
	private Set<Reference> references = new HashSet<Reference>();

	@JsonIgnore
	private Set<Description> descriptions = new HashSet<Description>();

	@JsonIgnore
	private Set<Distribution> distribution = new HashSet<Distribution>();

	@JsonIgnore
	private Set<Identifier> identifiers = new HashSet<Identifier>();

	@JsonIgnore
	private Set<TypeAndSpecimen> typesAndSpecimens = new HashSet<TypeAndSpecimen>();

	@JsonIgnore
	private Set<VernacularName> vernacularNames = new HashSet<VernacularName>();

	@JsonIgnore
	private Set<MeasurementOrFact> measurementsOrFacts = new HashSet<MeasurementOrFact>();

	@JsonIgnore
	private Set<Concept> concepts = new HashSet<Concept>();

	@JsonIgnore
	private Set<Identification> identifications = new HashSet<>();

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "taxa")
	public Set<Concept> getConcepts() {
		return concepts;
	}

	public void setConcepts(Set<Concept> concepts) {
		this.concepts = concepts;
	}

	/**
	 * @param newId
	 *            Set the identifier of this object.
	 */
	public void setId(Long newId) {
		this.id = newId;
	}

	/**
	 * @return Get the identifier for this object.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "taxonSequenceGenerator")
	@SequenceGenerator(name = "taxonSequenceGenerator", allocationSize = 1000, sequenceName = "seq_taxon")
	public Long getId() {
		return id;
	}

	@Override
	@JsonIgnore(false)
	@JsonProperty("fqId")
	public String getIdentifier() {
		return super.getIdentifier();
	}

	/**
	 * @return a set of organisations that have provided data on a taxon
	 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "taxon_organisation", 
		joinColumns = {@JoinColumn(name = "taxon_id")}, 
		inverseJoinColumns = {@JoinColumn(name = "organisation_id")}
	)
	public Set<Organisation> getAuthorities() {
		return authorities;
	}

	/**
	 * Authorities are aggregated to accepted taxa (see addAuthorityToTaxonAndRelatedTaxa below).
	 * If this taxon has an accepted name, return those authorities. Otherwise return the current set.
	 */
	public Set<Organisation> getCombinedAuthorities() {
		if (getAcceptedNameUsage() != null) {
			return getAcceptedNameUsage().getAuthorities();
		}
		return authorities;
	}

	public void addAuthorityToTaxonAndRelatedTaxa(Organisation organisation) {
		authorities.add(organisation);
		// If this name is a synonym, add the organisation to the accepted taxonomy.
		if (getAcceptedNameUsage() != null) {
			getAcceptedNameUsage().addAuthorityToTaxonAndRelatedTaxa(organisation);
		} else if (isAccepted() && getParentNameUsage() != null) {
			// If this name is accepted also add the organisation to its parent.
			getParentNameUsage().addAuthorityToTaxonAndRelatedTaxa(organisation);
		}
	}

	/**
	 * @return a list of images of the taxon
	 */
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "taxa")
	@OrderBy("rating DESC")
	public List<Image> getImages() {
		return images;
	}

	/**
	 * @return a list of references about the taxon
	 */
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "taxa")
	public Set<Reference> getReferences() {
		return references;
	}

	/**
	 * @return a map of descriptions about the taxon
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "taxon", orphanRemoval = true)
	@Cascade({ CascadeType.ALL })
	public Set<Description> getDescriptions() {
		return descriptions;
	}

	/**
	 * @param newDescriptions
	 *            Set the descriptions associated with this taxon
	 */
	public void setDescriptions(Set<Description> newDescriptions) {
		this.descriptions = newDescriptions;
	}


	/**
	 * @param newAuthorities
	 *            Set the organisations associated with this taxon
	 */
	public void setAuthorities(Set<Organisation> newAuthorities) {
		this.authorities = newAuthorities;
	}


	/**
	 * @param newImages
	 *            Set the images associated with this taxon
	 */
	public void setImages(List<Image> newImages) {
		this.images = newImages;
	}

	/**
	 * @param newReferences
	 *            Set the references associated with this taxon
	 */
	public void setReferences(Set<Reference> newReferences) {
		this.references = newReferences;
	}

	/**
	 * @return the full taxonomic name of the taxon, including authority
	 */
	@Size(max = 128)
	public String getScientificName() {
		return scientificName;
	}

	/**
	 * @param newName
	 *            Set the taxonomic name of the taxon
	 */
	public void setScientificName(String newName) {
		this.scientificName = newName;
	}

	/**
	 * @return the namePublishedInString
	 */
	@Size(max = 255)
	public String getNamePublishedInString() {
		return namePublishedInString;
	}

	/**
	 * @param namePublishedInString
	 *            the namePublishedInString to set
	 */
	public void setNamePublishedInString(String namePublishedInString) {
		this.namePublishedInString = namePublishedInString;
	}

	/**
	 * @return the immediate taxonomic parent
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@Cascade({ CascadeType.SAVE_UPDATE })
	public Taxon getParentNameUsage() {
		return parentNameUsage;
	}

	/**
	 * @param newParent
	 *            Set the taxonomic parent
	 */
	public void setParentNameUsage(Taxon newParent) {
		this.parentNameUsage = newParent;
	}

	/**
	 * @return Get the immediate taxonomic children
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parentNameUsage")
	public Set<Taxon> getChildNameUsages() {
		return childNameUsages;
	}

	/**
	 * @param newChildren
	 *            Set the taxonomic children
	 */
	public void setChildNameUsages(Set<Taxon> newChildren) {
		this.childNameUsages = newChildren;
	}

	/**
	 * @return Get the subsequent usages of this name
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "originalNameUsage")
	public Set<Taxon> getSubsequentNameUsages() {
		return subsequentNameUsages;
	}

	/**
	 * @param subsequentNameUsages
	 *            Set the subsequent usages of this name
	 */
	public void setSubsequentNameUsages(Set<Taxon> subsequentNameUsages) {
		this.subsequentNameUsages = subsequentNameUsages;
	}

	/**
	 * @return get the accepted name of this synonym
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@Cascade({ CascadeType.SAVE_UPDATE })
	public Taxon getAcceptedNameUsage() {
		return acceptedNameUsage;
	}

	/**
	 * @param newAccepted
	 *            Set the accepted name
	 */
	public void setAcceptedNameUsage(Taxon newAccepted) {
		this.acceptedNameUsage = newAccepted;
	}

	/**
	 * @return the synonyms of this taxon
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "acceptedNameUsage")
	public Set<Taxon> getSynonymNameUsages() {
		return synonymNameUsages;
	}

	/**
	 * @param newSynonyms
	 *            Set the synonyms of this taxon
	 */
	public void setSynonymNameUsages(Set<Taxon> newSynonyms) {
		this.synonymNameUsages = newSynonyms;
	}

	/**
	 * @return the distribution associated with this taxon
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "taxon", orphanRemoval = true)
	@Cascade({ CascadeType.ALL })
	public Set<Distribution> getDistribution() {
		return distribution;
	}

	/**
	 * @param newDistribution
	 *            Set the distribution associated with this taxon
	 */
	public void setDistribution(Set<Distribution> newDistribution) {
		this.distribution = newDistribution;
	}

	/**
	 * @param newAuthorship
	 *            set the authorship
	 */
	public void setScientificNameAuthorship(String newAuthorship) {
		this.scientificNameAuthorship = newAuthorship;
	}

	/**
	 * @param newGenusPart
	 *            Set the genus part of the name
	 */
	public void setGenus(String newGenusPart) {
		this.genus = newGenusPart;
	}

	/**
	 * @param newSpecificEpithet
	 *            set the specific epithet
	 */
	public void setSpecificEpithet(String newSpecificEpithet) {
		this.specificEpithet = newSpecificEpithet;
	}

	/**
	 * @param newInfraspecificEpithet
	 *            Set the infraspecific epithet
	 */
	public void setInfraspecificEpithet(String newInfraspecificEpithet) {
		this.infraspecificEpithet = newInfraspecificEpithet;
	}

	/**
	 * @param newRank
	 *            set the rank of this taxon
	 */
	public void setTaxonRank(Rank newRank) {
		this.taxonRank = newRank;
	}

	/**
	 * @return the authorship
	 */
	@Size(max = 255)
	public String getScientificNameAuthorship() {
		return scientificNameAuthorship;
	}

	/**
	 * @return the genus
	 */
	@Size(max = 128)
	public String getGenus() {
		return genus;
	}

	/**
	 * @return the specificEpithet
	 */
	@Size(max = 128)
	public String getSpecificEpithet() {
		return specificEpithet;
	}

	/**
	 * @return the infraSpecificEpithet
	 */
	@Size(max = 128)
	public String getInfraspecificEpithet() {
		return infraspecificEpithet;
	}

	/**
	 * @return the rank
	 */
	@Enumerated(value = EnumType.STRING)
	public Rank getTaxonRank() {
		return taxonRank;
	}

	/**
	 * @param newStatus
	 *            Set the taxonomic status
	 */
	public void setTaxonomicStatus(TaxonomicStatus newStatus) {
		this.taxonomicStatus = newStatus;
	}

	@Enumerated(value = EnumType.STRING)
	public TaxonomicStatus getTaxonomicStatus() {
		return taxonomicStatus;
	}

	/**
	 * @param newInfraGenericEpithet
	 *            Set the infrageneric epithet
	 */
	public void setSubgenus(String newSubgenus) {
		this.subgenus = newSubgenus;
	}

	/**
	 * @return the infrageneric epithet
	 */
	@Size(max = 128)
	public String getSubgenus() {
		return subgenus;
	}

	/**
	 * @param newAccordingTo
	 *            Set the according to
	 */
	public void setNameAccordingTo(Reference newNameAccordingTo) {
		this.nameAccordingTo = newNameAccordingTo;
	}

	/**
	 * @return the according to
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@Cascade({ CascadeType.SAVE_UPDATE })
	public Reference getNameAccordingTo() {
		return nameAccordingTo;
	}

	/**
	 * @param newFamily
	 *            set the family
	 */
	public void setFamily(String newFamily) {
		this.family = newFamily;
	}

	/**
	 * @param newKingdom
	 *            set the kingdom
	 */
	public void setKingdom(String newKingdom) {
		this.kingdom = newKingdom;
	}

	/**
	 * @param newPhylum
	 *            set the phylum
	 */
	public void setPhylum(String newPhylum) {
		this.phylum = newPhylum;
	}

	/**
	 * @param newClass
	 *            set the class
	 */
	public void setClazz(String newClass) {
		this.clazz = newClass;
	}

	/**
	 * @return the taxonomic class the taxon classified in
	 */
	@Column(name = "class")
	@Size(max = 255)
	public String getClazz() {
		return clazz;
	}

	/**
	 * @param newOrder
	 *            set the order
	 */
	public void setOrder(String newOrder) {
		this.order = newOrder;
	}

	/**
	 * @param newNomenclaturalCode
	 */
	public void setNomenclaturalCode(NomenclaturalCode newNomenclaturalCode) {
		this.nomenclaturalCode = newNomenclaturalCode;
	}

	/**
	 * @return the nomenclatural code
	 */
	@Enumerated(EnumType.STRING)
	public NomenclaturalCode getNomenclaturalCode() {
		return nomenclaturalCode;
	}

	/**
	 * @return the family
	 */
	@Size(max = 128)
	public String getFamily() {
		return family;
	}

	/**
	 * @return the kingdom
	 */
	@Size(max = 128)
	public String getKingdom() {
		return kingdom;
	}

	/**
	 * @return the phylum
	 */
	@Size(max = 128)
	public String getPhylum() {
		return phylum;
	}

	/**
	 * @return the order
	 */
	@Column(name = "ordr")
	@Size(max = 128)
	public String getOrder() {
		return order;
	}

	/**
	 * @return the subfamily
	 */
	@Size(max = 255)
	public String getSubfamily() {
		return subfamily;
	}

	/**
	 * @param subfamily
	 *            the subfamily to set
	 */
	public void setSubfamily(String subfamily) {
		this.subfamily = subfamily;
	}

	/**
	 * @return the tribe
	 */
	@Size(max = 255)
	public String getTribe() {
		return tribe;
	}

	/**
	 * @param tribe
	 *            the tribe to set
	 */
	public void setTribe(String tribe) {
		this.tribe = tribe;
	}

	/**
	 * @return the subtribe
	 */
	@Size(max = 255)
	public String getSubtribe() {
		return subtribe;
	}

	/**
	 * @param subtribe
	 *            the subtribe to set
	 */
	public void setSubtribe(String subtribe) {
		this.subtribe = subtribe;
	}

	/**
	 * @return the bibliographicCitation
	 */
	@Size(max = 255)
	public String getBibliographicCitation() {
		return bibliographicCitation;
	}

	/**
	 * @param bibliographicCitation
	 *            the bibliographicCitation to set
	 */
	public void setBibliographicCitation(String bibliographicCitation) {
		this.bibliographicCitation = bibliographicCitation;
	}

	/**
	 * @return the annotations
	 */
	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "annotatedObjId")
	@Where(clause = "annotatedObjType = 'Taxon'")
	@Cascade({ CascadeType.SAVE_UPDATE, CascadeType.MERGE, CascadeType.DELETE })
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
	 * @param reference
	 *            set the protologue
	 */
	public void setNamePublishedIn(Reference reference) {
		this.namePublishedIn = reference;
	}

	/**
	 * @return the protologue
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@Cascade({ CascadeType.SAVE_UPDATE })
	public Reference getNamePublishedIn() {
		return namePublishedIn;
	}

	/**
	 * @return the ancestors of the taxon
	 */
	@Transient
	@JsonProperty("classification")
	public List<Taxon> getHigherClassification() {
		if(higherClassification == null) {
			List<Taxon> ancestors = new ArrayList<Taxon>();
			getAncestors(this, ancestors);
			this.setHigherClassification(ancestors);
		}
		return higherClassification;
	}

	private void getAncestors(Taxon t, List<Taxon> ancestors) {
		if (t.getParentNameUsage() != null) {
			getAncestors(t.getParentNameUsage(), ancestors);
		}
		ancestors.add(t);
	}

	/**
	 * @param ancestors
	 *            Set the ancestors of the taxon
	 */
	public void setHigherClassification(List<Taxon> newHigherClassification) {
		this.higherClassification = newHigherClassification;
	}

	/**
	 * @param newIdentifier
	 *            Set the name identifier
	 */
	public void setScientificNameID(String newIdentifier) {
		this.scientificNameID = newIdentifier;
	}

	/**
	 *
	 * @return the name identifier
	 */
	@Size(max = 255)
	public String getScientificNameID() {
		return scientificNameID;
	}

	/**
	 * @return a list of identifiers the taxon
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "taxon", orphanRemoval = true)
	@Cascade({ CascadeType.ALL })
	public Set<Identifier> getIdentifiers() {
		return identifiers;
	}

	/**
	 * @param newIdentifiers
	 *            Set the identifiers associated with this taxon
	 */
	public void setIdentifiers(Set<Identifier> newIdentifiers) {
		this.identifiers = newIdentifiers;
	}

	/**
	 * @return the source
	 */
	@Size(max = 255)
	public String getSource() {
		return source;
	}

	/**
	 * @param source
	 *            the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * @return the namePublishedInYear
	 */
	public Integer getNamePublishedInYear() {
		return namePublishedInYear;
	}

	/**
	 * @param namePublishedInYear
	 *            the namePublishedInYear to set
	 */
	public void setNamePublishedInYear(Integer namePublishedInYear) {
		this.namePublishedInYear = namePublishedInYear;
	}

	/**
	 * @return the verbatimTaxonRank
	 */
	@Size(max = 255)
	public String getVerbatimTaxonRank() {
		return verbatimTaxonRank;
	}

	/**
	 * @param verbatimTaxonRank
	 *            the verbatimTaxonRank to set
	 */
	public void setVerbatimTaxonRank(String verbatimTaxonRank) {
		this.verbatimTaxonRank = verbatimTaxonRank;
	}

	/**
	 * @return the taxonRemarks
	 */
	@Size(max = 255)
	public String getTaxonRemarks() {
		return taxonRemarks;
	}

	/**
	 * @param taxonRemarks
	 *            the taxonRemarks to set
	 */
	public void setTaxonRemarks(String taxonRemarks) {
		this.taxonRemarks = taxonRemarks;
	}

	/**
	 * @return the nomenclaturalStatus
	 */
	@Enumerated(value = EnumType.STRING)
	public NomenclaturalStatus getNomenclaturalStatus() {
		return nomenclaturalStatus;
	}

	/**
	 * @param nomenclaturalStatus
	 *            the nomenclaturalStatus to set
	 */
	public void setNomenclaturalStatus(NomenclaturalStatus nomenclaturalStatus) {
		this.nomenclaturalStatus = nomenclaturalStatus;
	}

	/**
	 * @return the originalNameUsage
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@Cascade({ CascadeType.SAVE_UPDATE })
	public Taxon getOriginalNameUsage() {
		return originalNameUsage;
	}

	/**
	 * @param originalNameUsage
	 *            the originalNameUsage to set
	 */
	public void setOriginalNameUsage(Taxon originalNameUsage) {
		this.originalNameUsage = originalNameUsage;
	}

	/**
	 * @return a list of types and specimens
	 */
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "taxa")
	public Set<TypeAndSpecimen> getTypesAndSpecimens() {
		return typesAndSpecimens;
	}

	/**
	 *
	 * @param typesAndSpecimens
	 */
	public void setTypesAndSpecimens(Set<TypeAndSpecimen> typesAndSpecimens) {
		this.typesAndSpecimens = typesAndSpecimens;
	}

	/**
	 * @return a map of vernacularNames for the taxon
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "taxon", orphanRemoval = true)
	@Cascade({ CascadeType.ALL })
	public Set<VernacularName> getVernacularNames() {
		return vernacularNames;
	}

	/**
	 * @param newVernacularNames
	 *            Set the vernacular names for this taxon
	 */
	public void setVernacularNames(Set<VernacularName> newVernacularNames) {
		this.vernacularNames = newVernacularNames;
	}

	/**
	 * @return a set of measurements or facts about the taxon
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "taxon", orphanRemoval = true)
	@Cascade({ CascadeType.ALL })
	public Set<MeasurementOrFact> getMeasurementsOrFacts() {
		return measurementsOrFacts;
	}

	/**
	 * @param newMeasurementsOrFacts
	 *            Set the measurements or facts about this taxon
	 */
	public void setMeasurementsOrFacts(Set<MeasurementOrFact> newMeasurementsOrFacts) {
		this.measurementsOrFacts = newMeasurementsOrFacts;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "taxon")
	@OrderBy("dateIdentified desc")
	public Set<Identification> getIdentifications() {
		return identifications;
	}

	public void setIdentifications(Set<Identification> identifications) {
		this.identifications = identifications;
	}

	@Transient
	public boolean looksAccepted() {
		// This method controls if a taxon displays a complete profile, like an accepted name.
		if (getTaxonomicStatus() == null) {
			return true;
		} else {
			switch (getTaxonomicStatus()) {
			case Synonym:
			case Heterotypic_Synonym:
			case Homotypic_Synonym:
			case DeterminationSynonym:
			case IntermediateRankSynonym:
			case Proparte_Synonym:
			case Misapplied:
				return false;
			case Accepted:
			case Doubtful:
			case PartiallyAccepted:
			default:
				return true;
			}
		}
	}

	@Transient
	public boolean isAccepted() {
		// This method controls if a taxon appears in an accepted names search and is displayed as accepted.
		if (getTaxonomicStatus() == null) {
			return false;
		} else {
			switch (getTaxonomicStatus()) {
			case Accepted:
			case PartiallyAccepted:
				return true;
			case Synonym:
			case Heterotypic_Synonym:
			case Homotypic_Synonym:
			case DeterminationSynonym:
			case IntermediateRankSynonym:
			case Proparte_Synonym:
			case Doubtful:
			case Misapplied:
			default:
				return false;
			}
		}
	}

	@Transient
	public boolean isSynonym() {
		//we want doubtful names to look like an accepted name, 
		//but not show up in an accepted names search, or say that the name is accepted.
		if (getTaxonomicStatus() == null) {
			return false;
		} else {
			switch (getTaxonomicStatus()) {
			case Synonym:
			case Heterotypic_Synonym:
			case Homotypic_Synonym:
			case DeterminationSynonym:
			case IntermediateRankSynonym:
			case Proparte_Synonym:
				return true;
			case Accepted:
			case Artificial_Hybrid:
			case Doubtful:
			case Misapplied:
			case PartiallyAccepted:
			default:
				return false;
			}
		}
	}

	@Transient
	public boolean isPlantae() {
		return identifier.startsWith("urn:lsid:ipni.org");
	}

	@Transient
	public boolean isFungi() {
		return identifier.startsWith("urn:lsid:indexfungorum.org");
	}

	@Override
	public SolrInputDocument toSolrInputDocument(ApplicationContext ctx) {
		return new TaxonSolrInputDocument(this, ctx).build();
	}

	@Override
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(scientificName);
		if(scientificNameAuthorship != null) {
			stringBuffer.append(" " + scientificNameAuthorship);
		}
		if(identifier != null) {
			stringBuffer.append(" <" + identifier + ">");
		}
		return stringBuffer.toString();
	}
}
