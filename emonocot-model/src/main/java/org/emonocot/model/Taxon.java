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
package org.emonocot.model;

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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.apache.solr.common.SolrInputDocument;
import org.emonocot.api.job.WCSPTerm;
import org.emonocot.model.constants.Location;
import org.emonocot.model.marshall.json.ConceptDeserializer;
import org.emonocot.model.marshall.json.ConceptSerializer;
import org.emonocot.model.marshall.json.ImageSerializer;
import org.emonocot.model.marshall.json.NullDeserializer;
import org.emonocot.model.marshall.json.ReferenceDeserializer;
import org.emonocot.model.marshall.json.ReferenceSerializer;
import org.emonocot.model.marshall.json.TaxonDeserializer;
import org.emonocot.model.marshall.json.TaxonSerializer;
import org.emonocot.pager.FacetName;
import org.gbif.dwc.terms.IucnTerm;
import org.gbif.ecat.voc.NomenclaturalCode;
import org.gbif.ecat.voc.NomenclaturalStatus;
import org.gbif.ecat.voc.Rank;
import org.gbif.ecat.voc.TaxonomicStatus;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Where;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author ben
 */
@Entity
public class Taxon extends SearchableObject {

	private Logger logger = LoggerFactory.getLogger(Taxon.class);

	private static final long serialVersionUID = -3511287213090466877L;

	private Long id;

	private String bibliographicCitation;

	private String scientificName;

	private String scientificNameAuthorship;

	private String genus;

	private String subgenus;

	private String specificEpithet;

	private String infraspecificEpithet;

	private Rank taxonRank;

	private TaxonomicStatus taxonomicStatus;

	private String kingdom;

	private String phylum;

	private String clazz;

	private String order;

	private String family;

	private String subfamily;

	private String tribe;

	private String subtribe;

	private String scientificNameID;

	private NomenclaturalCode nomenclaturalCode;

	private String source;

	private Integer namePublishedInYear;

	private String verbatimTaxonRank;

	private String taxonRemarks;

	private String namePublishedInString;

	private NomenclaturalStatus nomenclaturalStatus;

	private Set<Annotation> annotations = new HashSet<Annotation>();

	private Reference namePublishedIn;

	private Reference nameAccordingTo;

	private List<Taxon> higherClassification = null;

	private Taxon originalNameUsage;

	private Set<Taxon> subsequentNameUsages = new HashSet<Taxon>();

	private Taxon parentNameUsage;

	private Set<Taxon> childNameUsages = new HashSet<Taxon>();

	private Taxon acceptedNameUsage;

	private Set<Taxon> synonymNameUsages = new HashSet<Taxon>();

	private List<Image> images = new ArrayList<Image>();

	private Set<IdentificationKey> keys = new HashSet<IdentificationKey>();

	// The Phylogenies I am the root taxon of
	private Set<PhylogeneticTree> trees = new HashSet<PhylogeneticTree>();

	// The Phylogenies I appear in
	private Set<PhylogeneticTree> phylogenies = new HashSet<PhylogeneticTree>();

	private Set<Reference> references = new HashSet<Reference>();

	private Set<Description> descriptions = new HashSet<Description>();

	private Set<Distribution> distribution = new HashSet<Distribution>();

	private Set<Identifier> identifiers = new HashSet<Identifier>();

	private Set<TypeAndSpecimen> typesAndSpecimens = new HashSet<TypeAndSpecimen>();

	private Set<VernacularName> vernacularNames = new HashSet<VernacularName>();

	private Set<MeasurementOrFact> measurementsOrFacts = new HashSet<MeasurementOrFact>();

	private Set<Concept> concepts = new HashSet<Concept>();

	private Set<Identification> identifications = new HashSet<>();

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "taxa")
	@JsonSerialize(contentUsing = ConceptSerializer.class)
	public Set<Concept> getConcepts() {
		return concepts;
	}

	@JsonDeserialize(contentUsing = ConceptDeserializer.class)
	public void setConcepts(Set<Concept> concepts) {
		this.concepts = concepts;
	}

	private List<Comment> comments = new ArrayList<Comment>();

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
	@GeneratedValue(generator = "table-hilo", strategy = GenerationType.TABLE)
	public Long getId() {
		return id;
	}

	/**
	 * @return a list of images of the taxon
	 */
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "taxa")
	@JsonSerialize(contentUsing = ImageSerializer.class)
	public List<Image> getImages() {
		return images;
	}

	/**
	 * @return a list of references about the taxon
	 */
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "taxa")
	@JsonSerialize(contentUsing = ReferenceSerializer.class)
	public Set<Reference> getReferences() {
		return references;
	}

	/**
	 * @return a map of descriptions about the taxon
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "taxon", orphanRemoval = true)
	@Cascade({ CascadeType.ALL })
	@JsonManagedReference("descriptions-taxon")
	public Set<Description> getDescriptions() {
		return descriptions;
	}

	/**
	 * @param newDescriptions
	 *            Set the descriptions associated with this taxon
	 */
	@JsonManagedReference("descriptions-taxon")
	public void setDescriptions(Set<Description> newDescriptions) {
		this.descriptions = newDescriptions;
	}

	/**
	 * @param newImages
	 *            Set the images associated with this taxon
	 */
	@JsonDeserialize(contentUsing = NullDeserializer.class)
	public void setImages(List<Image> newImages) {
		this.images = newImages;
	}

	/**
	 * @param newReferences
	 *            Set the references associated with this taxon
	 */
	@JsonDeserialize(contentUsing = NullDeserializer.class)
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
	@JsonSerialize(using = TaxonSerializer.class)
	public Taxon getParentNameUsage() {
		return parentNameUsage;
	}

	/**
	 * @param newParent
	 *            Set the taxonomic parent
	 */
	@JsonDeserialize(using = TaxonDeserializer.class)
	public void setParentNameUsage(Taxon newParent) {
		this.parentNameUsage = newParent;
	}

	/**
	 * @return Get the immediate taxonomic children
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parentNameUsage")
	@JsonIgnore
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
	@JsonIgnore
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
	@JsonSerialize(using = TaxonSerializer.class)
	public Taxon getAcceptedNameUsage() {
		return acceptedNameUsage;
	}

	/**
	 * @param newAccepted
	 *            Set the accepted name
	 */
	@JsonDeserialize(using = TaxonDeserializer.class)
	public void setAcceptedNameUsage(Taxon newAccepted) {
		this.acceptedNameUsage = newAccepted;
	}

	/**
	 * @return the synonyms of this taxon
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "acceptedNameUsage")
	@JsonIgnore
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
	@JsonManagedReference("distribution-taxon")
	public Set<Distribution> getDistribution() {
		return distribution;
	}

	/**
	 * @param newDistribution
	 *            Set the distribution associated with this taxon
	 */
	@JsonManagedReference("distribution-taxon")
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
	@Size(max = 128)
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
	@JsonDeserialize(using = ReferenceDeserializer.class)
	public void setNameAccordingTo(Reference newNameAccordingTo) {
		this.nameAccordingTo = newNameAccordingTo;
	}

	/**
	 * @return the according to
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@Cascade({ CascadeType.SAVE_UPDATE })
	@JsonSerialize(using = ReferenceSerializer.class)
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
	 * @param reference
	 *            set the protologue
	 */
	@JsonDeserialize(using = ReferenceDeserializer.class)
	public void setNamePublishedIn(Reference reference) {
		this.namePublishedIn = reference;
	}

	/**
	 * @return the protologue
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@Cascade({ CascadeType.SAVE_UPDATE })
	@JsonSerialize(using = ReferenceSerializer.class)
	public Reference getNamePublishedIn() {
		return namePublishedIn;
	}

	/**
	 * @return the ancestors of the taxon
	 */
	@JsonIgnore
	@Transient
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
	@JsonIgnore
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
	@JsonManagedReference("identifier-taxon")
	public Set<Identifier> getIdentifiers() {
		return identifiers;
	}

	/**
	 * @param newIdentifiers
	 *            Set the identifiers associated with this taxon
	 */
	@JsonManagedReference("identifier-taxon")
	public void setIdentifiers(Set<Identifier> newIdentifiers) {
		this.identifiers = newIdentifiers;
	}

	/**
	 * @return the keys
	 */
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "taxa")
	@JsonIgnore
	public Set<IdentificationKey> getKeys() {
		return keys;
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
	@JsonSerialize(using = TaxonSerializer.class)
	public Taxon getOriginalNameUsage() {
		return originalNameUsage;
	}

	/**
	 * @param originalNameUsage
	 *            the originalNameUsage to set
	 */
	@JsonDeserialize(using = TaxonDeserializer.class)
	public void setOriginalNameUsage(Taxon originalNameUsage) {
		this.originalNameUsage = originalNameUsage;
	}

	/**
	 * @param keys
	 *            the keys to set
	 */
	@JsonIgnore
	public void setKeys(Set<IdentificationKey> keys) {
		this.keys = keys;
	}

	/**
	 * @return a list of types and specimens
	 */
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "taxa")
	@JsonIgnore
	public Set<TypeAndSpecimen> getTypesAndSpecimens() {
		return typesAndSpecimens;
	}

	/**
	 *
	 * @param typesAndSpecimens
	 */
	@JsonIgnore
	public void setTypesAndSpecimens(Set<TypeAndSpecimen> typesAndSpecimens) {
		this.typesAndSpecimens = typesAndSpecimens;
	}

	/**
	 * @return a map of vernacularNames for the taxon
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "taxon", orphanRemoval = true)
	@Cascade({ CascadeType.ALL })
	@JsonManagedReference("vernacularNames-taxon")
	public Set<VernacularName> getVernacularNames() {
		return vernacularNames;
	}

	/**
	 * @param newVernacularNames
	 *            Set the vernacular names for this taxon
	 */
	@JsonManagedReference("vernacularNames-taxon")
	public void setVernacularNames(Set<VernacularName> newVernacularNames) {
		this.vernacularNames = newVernacularNames;
	}

	/**
	 * @return a set of measurements or facts about the taxon
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "taxon", orphanRemoval = true)
	@Cascade({ CascadeType.ALL })
	@JsonManagedReference("measurementsOrFacts-taxon")
	public Set<MeasurementOrFact> getMeasurementsOrFacts() {
		return measurementsOrFacts;
	}

	/**
	 * @param newMeasurementsOrFacts
	 *            Set the measurements or facts about this taxon
	 */
	@JsonManagedReference("measurementsOrFacts-taxon")
	public void setMeasurementsOrFacts(
			Set<MeasurementOrFact> newMeasurementsOrFacts) {
		this.measurementsOrFacts = newMeasurementsOrFacts;
	}

	/**
	 * @return the comments
	 */
	@OneToMany(fetch = FetchType.LAZY, orphanRemoval = true)
	@JoinColumn(name = "commentPage_id")
	@OrderBy("created DESC")
	@Where(clause = "commentPage_type = 'Taxon'")
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

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "taxa")
	@JsonIgnore
	public Set<PhylogeneticTree> getTrees() {
		return trees;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "leaves")
	@JsonIgnore
	public Set<PhylogeneticTree> getPhylogenies() {
		return phylogenies;
	}

	@JsonIgnore
	public void setPhylogenies(Set<PhylogeneticTree> phylogenies) {
		this.phylogenies = phylogenies;
	}

	@JsonIgnore
	public void setTrees(Set<PhylogeneticTree> trees) {
		this.trees = trees;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "taxon")
	@OrderBy("dateIdentified desc")
	public Set<Identification> getIdentifications() {
		return identifications;
	}

	public void setIdentifications(Set<Identification> identifications) {
		this.identifications = identifications;
	}

	@Override
	public SolrInputDocument toSolrInputDocument() {
		SolrInputDocument sid = super.toSolrInputDocument();
		sid.addField("searchable.label_sort", getScientificName());

		StringBuilder summary = new StringBuilder()
				.append(getBibliographicCitation()).append(" ")
				.append(getClazz()).append(" ")
				.append(getFamily()).append(" ")
				.append(getGenus()).append(" ")
				.append(getKingdom()).append(" ")
				.append(getNamePublishedInString()).append(" ")
				.append(getNamePublishedInYear()).append(" ")
				.append(getNomenclaturalStatus()).append(" ")
				.append(getOrder()).append(" ")
				.append(getPhylum()).append(" ")
				.append(getScientificName()).append(" ")
				.append(getScientificNameAuthorship()).append(" ")
				.append(getSource()).append(" ")
				.append(getSpecificEpithet()).append(" ")
				.append(getSubfamily()).append(" ")
				.append(getSubgenus()).append(" ")
				.append(getSubtribe()).append(" ")
				.append(getTaxonomicStatus()).append(" ")
				.append(getTaxonRank()).append(" ")
				.append(getTaxonRemarks()).append(" ")
				.append(getTribe()).append(" ")
				.append(getVerbatimTaxonRank());

		if(Rank.FAMILY.equals(getTaxonRank()) && getFamily() == null) {
			addField(sid,"taxon.family_ns", getScientificName());
			addField(sid,"taxon.family_ss", getScientificName());
		} else {
			addField(sid,"taxon.family_ns", getFamily());
			addField(sid,"taxon.family_ss", getFamily());
		}
		if(getAcceptedNameUsage() != null) {
			addField(sid,"taxon.family_ss", getAcceptedNameUsage().getFamily());
			summary.append(" ").append(getAcceptedNameUsage().getFamily());
		}

		addField(sid,FacetName.GENUS.getSolrField(), getGenus());
		if(getAcceptedNameUsage() != null) {
			addField(sid,FacetName.GENUS.getSolrField(), getAcceptedNameUsage().getGenus());
		}
		if(Rank.GENUS == getTaxonRank() && getGenus() == null) {
			addField(sid,"taxon.genus_ns", getScientificName());
			addField(sid,FacetName.GENUS.getSolrField(), getScientificName());
		} else {
			addField(sid,"taxon.genus_ns", getGenus());
		}

		addField(sid,"taxon.infraspecific_epithet_s", getInfraspecificEpithet());
		addField(sid,"taxon.infraspecific_epithet_ns", getInfraspecificEpithet());
		addField(sid,"taxon.order_s", getOrder());
		addField(sid,"taxon.scientific_name_t", getScientificName());
		addField(sid,"taxon.scientific_name_authorship_s", getScientificNameAuthorship());
		addField(sid,"taxon.specific_epithet_s", getSpecificEpithet());
		addField(sid,"taxon.specific_epithet_ns", getSpecificEpithet());

		addField(sid,FacetName.SUBFAMILY.getSolrField(), getSubfamily());
		if(Rank.Subfamily.equals(getTaxonRank()) && getSubfamily() == null) {
			addField(sid,FacetName.SUBFAMILY.getSolrField(), getScientificName());
		}
		if(getAcceptedNameUsage() != null) {
			addField(sid,FacetName.SUBFAMILY.getSolrField(), getAcceptedNameUsage().getSubfamily());
		}

		addField(sid,"taxon.subgenus_s", getSubgenus());

		addField(sid,FacetName.SUBTRIBE.getSolrField(), getSubtribe());
		if(Rank.Subtribe.equals(getTaxonRank()) && getSubtribe() == null) {
			addField(sid,FacetName.SUBTRIBE.getSolrField(), getScientificName());
		}
		if(getAcceptedNameUsage() != null) {
			addField(sid,FacetName.SUBTRIBE.getSolrField(), getAcceptedNameUsage().getSubtribe());
		}

		addField(sid,"taxon.taxonomic_status_s", getTaxonomicStatus());
		addField(sid,"taxon.taxon_rank_s", getTaxonRank());

		addField(sid,FacetName.TRIBE.getSolrField(), getTribe());
		if(Rank.Tribe.equals(getTaxonRank()) && getTribe() == null) {
			addField(sid,FacetName.TRIBE.getSolrField(), getScientificName());
		}
		if(getAcceptedNameUsage() != null) {
			addField(sid,FacetName.TRIBE.getSolrField(), getAcceptedNameUsage().getTribe());
		}

		sid.addField("taxon.descriptions_not_empty_b", !getDescriptions().isEmpty());

		for(Description d : getDescriptions()) {
			summary.append(" ").append(d.getDescription());
			if(d.getAuthority() != null) {
				sid.addField("searchable.sources_ss", d.getAuthority().getIdentifier());
			}
		}

		sid.addField("taxon.distribution_not_empty_b", !getDistribution().isEmpty());
		for(Distribution d : getDistribution()) {
			sid.addField("taxon.distribution_ss", d.getLocation().getCode());
			switch(d.getLocation().getLevel()) {
			case 0:
				for(Location r : (Set<Location>)d.getLocation().getChildren()) {
					for(Location c : (Set<Location>)r.getChildren()) {
						for(Location l : (Set<Location>)c.getChildren()) {
							indexLocality(l,sid);
						}
					}
				}
				break;
			case 1:
				for(Location c : (Set<Location>)d.getLocation().getChildren()) {
					for(Location l : (Set<Location>)c.getChildren()) {
						indexLocality(l,sid);
					}
				}
				break;
			case 2:
				for(Location l : (Set<Location>)d.getLocation().getChildren()) {
					indexLocality(l,sid);
				}
				break;
			case 3:
				indexLocality(d.getLocation(),sid);
				break;
			default:
				break;
			}

			if(d.getAuthority() != null) {
				sid.addField("searchable.sources_ss", d.getAuthority().getIdentifier());
			}
		}

		sid.addField("taxon.images_not_empty_b", !getImages().isEmpty());
		for(Image i : getImages()) {
			if(i != null && i.getAuthority() != null) {
				sid.addField("searchable.sources_ss", i.getAuthority().getIdentifier());
			}
		}

		sid.addField("taxon.references_not_empty_b", !getReferences().isEmpty());
		for(Reference r : getReferences()) {
			if(r != null && r.getAuthority() != null) {
				sid.addField("searchable.sources_ss", r.getAuthority().getIdentifier());
			}
		}

		boolean hasTaxonomicPlacement = (acceptedNameUsage != null || parentNameUsage != null);
		sid.addField("taxon.taxonomic_placement_not_empty_b", hasTaxonomicPlacement);

		sid.addField("taxon.types_and_specimens_not_empty_b", !getTypesAndSpecimens().isEmpty());
		for(TypeAndSpecimen t : getTypesAndSpecimens()) {
			if(t != null && t.getAuthority() != null) {
				sid.addField("searchable.sources_ss", t.getAuthority().getIdentifier());
			}
		}

		sid.addField("taxon.identifiers_not_empty_b", !getIdentifiers().isEmpty());
		for(Identifier i : getIdentifiers()) {
			if(i.getAuthority() != null) {
				sid.addField("searchable.sources_ss", i.getAuthority().getIdentifier());
			}
		}

		sid.addField("taxon.measurements_or_facts_not_empty_b", !getMeasurementsOrFacts().isEmpty());
		boolean hasLifeForm = false;
		boolean hasHabitat = false;
		boolean hasThreatStatus = false;
		for(MeasurementOrFact m : getMeasurementsOrFacts()) {
			sid.addField("taxon.measurement_or_fact_" + m.getMeasurementType().simpleName() + "_txt", m.getMeasurementValue());
			if(m.getMeasurementType().equals(WCSPTerm.Habitat)) {
				hasHabitat = true;
			} else if(m.getMeasurementType().equals(WCSPTerm.Lifeform)) {
				hasLifeForm = true;
			} else if(m.getMeasurementType().equals(IucnTerm.threatStatus)) {
				hasThreatStatus = true;
			}
			if(m.getAuthority() != null) {
				sid.addField("searchable.sources_ss", m.getAuthority().getIdentifier());
			}
		}
		if(!hasLifeForm) {
			sid.addField("taxon.measurement_or_fact_" + WCSPTerm.Lifeform.simpleName() + "_txt", "_NULL_");
		}
		if(!hasHabitat) {
			sid.addField("taxon.measurement_or_fact_" + WCSPTerm.Habitat.simpleName() + "_txt", "_NULL_");
		}
		if(!hasThreatStatus) {
			sid.addField("taxon.measurement_or_fact_" + IucnTerm.threatStatus.simpleName() + "_txt", "_NULL_");
		}

		sid.addField("taxon.vernacular_names_not_empty_b", !getVernacularNames().isEmpty());
		for(VernacularName v : getVernacularNames()) {
			summary.append(" ").append(v.getVernacularName());
			if(v.getAuthority() != null) {
				sid.addField("searchable.sources_ss", v.getAuthority().getIdentifier());
			}
		}

		sid.addField("taxon.name_used_b", !getIdentifications().isEmpty());

		Set<String> usedAt = new HashSet<>();
		for(Identification identification : getIdentifications()) {
			usedAt.add(identification.getIdentifiedBy());
		}
		for(String used : usedAt) {
			sid.addField("taxon.name_used_at_ss", used);
		}

		for(Taxon synonym : getSynonymNameUsages()) {
			summary.append(" ").append(synonym.getScientificName());
		}

		sid.addField("searchable.solrsummary_t", summary);
		sid.addField("taxon.has_data_b", hasUsefulData(sid));
		return sid;
	}

	private void indexLocality(Location g, SolrInputDocument sid) {
		if(g.getParent() != null) {
			indexLocality(g.getParent(), sid);
		}
		sid.addField("taxon.distribution_" + g.getPrefix() + "_" + g.getLevel() + "_ss", g.toString());
	}

	/*
	 * Used to determine if a taxon has useful data or if it is just a name
	 */
	private boolean hasUsefulData(SolrInputDocument sid) {
		String[] usefulFields = {
				"taxon.descriptions_not_empty_b",
				"taxon.distribution_not_empty_b",
				"taxon.identifiers_not_empty_b",
				"taxon.images_not_empty_b",
				"taxon.measurements_or_facts_not_empty_b",
				"taxon.name_used_b",
				"taxon.references_not_empty_b",
				"taxon.taxonomic_placement_not_empty_b",
				"taxon.types_and_specimens_not_empty_b",
				"taxon.vernacular_names_not_empty_b"
		};

		for(String field : usefulFields) {
			if((Boolean)sid.getFieldValue(field)) {
				return true;
			}
		}

		return false;
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