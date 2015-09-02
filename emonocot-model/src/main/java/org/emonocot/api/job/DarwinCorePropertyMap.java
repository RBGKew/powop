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
package org.emonocot.api.job;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.emonocot.model.compare.TermComparator;
import org.gbif.dwc.terms.Term;
import org.gbif.dwc.terms.DcTerm;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.terms.GbifTerm;

public class DarwinCorePropertyMap {

	public static Map<Term,String> taxonTerms = new HashMap<Term,String>();

	public static Map<Term,String> distributionTerms = new HashMap<Term,String>();

	public static Map<Term,String> descriptionTerms = new HashMap<Term,String>();

	public static Map<Term,String> referenceTerms = new HashMap<Term,String>();

	public static Map<Term,String> imageTerms = new HashMap<Term,String>();

	public static Map<Term,String> identifierTerms = new HashMap<Term,String>();

	public static Map<Term,String> typeAndSpecimenTerms = new HashMap<Term,String>();

	public static Map<Term,String> vernacularNameTerms = new HashMap<Term,String>();

	public static Map<Term,String> measurementOrFactTerms = new HashMap<Term,String>();

	public static Map<Term,String> skosTerms = new HashMap<Term,String>();

	static {
		taxonTerms.put(DcTerm.accessRights,"accessRights");
		taxonTerms.put(DwcTerm.datasetID,"authority.identifier");
		taxonTerms.put(DwcTerm.datasetName,"authority.title");
		taxonTerms.put(DcTerm.created,"created");
		taxonTerms.put(DcTerm.license,"license");
		taxonTerms.put(DcTerm.modified,"modified");
		taxonTerms.put(DcTerm.rights,"rights");
		taxonTerms.put(DcTerm.rightsHolder,"rightsHolder");
		taxonTerms.put(DwcTerm.taxonID, "identifier");
		taxonTerms.put(DwcTerm.acceptedNameUsage,"acceptedNameUsage.scientificName");
		taxonTerms.put(DwcTerm.acceptedNameUsageID,"acceptedNameUsage.identifier");
		taxonTerms.put(EmonocotTerm.acceptedNameUsageFamily,"acceptedNameUsage.family");
		taxonTerms.put(DcTerm.bibliographicCitation,"bibliographicCitation");
		taxonTerms.put(DwcTerm.class_,"clazz");
		taxonTerms.put(DwcTerm.family,"family");
		taxonTerms.put(DwcTerm.genus,"genus");
		taxonTerms.put(DwcTerm.infraspecificEpithet,"infraspecificEpithet");
		taxonTerms.put(DwcTerm.kingdom,"kingdom");
		taxonTerms.put(DwcTerm.nameAccordingTo,"nameAccordingTo");
		taxonTerms.put(DwcTerm.namePublishedInID,"namePublishedIn.identifier");
		taxonTerms.put(DwcTerm.namePublishedIn,"namePublishedInString");
		taxonTerms.put(DwcTerm.namePublishedInYear,"namePublishedInYear");
		taxonTerms.put(DwcTerm.nomenclaturalCode,"nomenclaturalCode");
		taxonTerms.put(DwcTerm.nomenclaturalStatus,"nomenclaturalStatus");
		taxonTerms.put(DwcTerm.order,"order");
		taxonTerms.put(DwcTerm.originalNameUsageID,"originalNameUsage.identifier");
		taxonTerms.put(DwcTerm.originalNameUsage,"originalNameUsage.scientificName");
		taxonTerms.put(DwcTerm.parentNameUsageID,"parentNameUsage.identifier");
		taxonTerms.put(DwcTerm.parentNameUsage,"parentNameUsage.scientificName");
		taxonTerms.put(DwcTerm.phylum,"phylum");
		taxonTerms.put(DwcTerm.scientificName,"scientificName");
		taxonTerms.put(DwcTerm.scientificNameAuthorship,"scientificNameAuthorship");
		taxonTerms.put(DwcTerm.scientificNameID,"scientificNameID");
		taxonTerms.put(DcTerm.references,"source");
		taxonTerms.put(DwcTerm.specificEpithet,"specificEpithet");
		taxonTerms.put(EmonocotTerm.subfamily,"subfamily");
		taxonTerms.put(DwcTerm.subgenus,"subgenus");
		taxonTerms.put(EmonocotTerm.subtribe,"subtribe");
		taxonTerms.put(DwcTerm.taxonomicStatus,"taxonomicStatus");
		taxonTerms.put(DwcTerm.taxonRank,"taxonRank");
		taxonTerms.put(DwcTerm.taxonRemarks,"taxonRemarks");
		taxonTerms.put(EmonocotTerm.tribe,"tribe");
		taxonTerms.put(DwcTerm.verbatimTaxonRank,"verbatimTaxonRank");

		distributionTerms.put(DcTerm.accessRights,"accessRights");
		distributionTerms.put(DwcTerm.datasetID,"authority.identifier");
		distributionTerms.put(DwcTerm.datasetName,"authority.title");
		distributionTerms.put(DcTerm.created,"created");
		distributionTerms.put(DcTerm.license,"license");
		distributionTerms.put(DcTerm.modified,"modified");
		distributionTerms.put(DcTerm.rights,"rights");
		distributionTerms.put(DcTerm.rightsHolder,"rightsHolder");
		distributionTerms.put(DcTerm.identifier, "identifier");
		distributionTerms.put(DwcTerm.taxonID, "taxon.identifier");
		distributionTerms.put(DwcTerm.locality, "locality");
		distributionTerms.put(DwcTerm.locationID, "location");
		distributionTerms.put(DwcTerm.occurrenceRemarks, "occurrenceRemarks");
		distributionTerms.put(DwcTerm.occurrenceStatus, "occurrenceStatus");
		distributionTerms.put(DwcTerm.establishmentMeans, "establishmentMeans");
		distributionTerms.put(DcTerm.source, "references");

		descriptionTerms.put(DcTerm.accessRights,"accessRights");
		descriptionTerms.put(DwcTerm.datasetID,"authority.identifier");
		descriptionTerms.put(DwcTerm.datasetName,"authority.title");
		descriptionTerms.put(DcTerm.created,"created");
		descriptionTerms.put(DcTerm.license,"license");
		descriptionTerms.put(DcTerm.modified,"modified");
		descriptionTerms.put(DcTerm.rights,"rights");
		descriptionTerms.put(DcTerm.rightsHolder,"rightsHolder");
		descriptionTerms.put(DcTerm.identifier, "identifier");
		descriptionTerms.put(DwcTerm.taxonID, "taxon.identifier");
		descriptionTerms.put(DcTerm.audience, "audience");
		descriptionTerms.put(DcTerm.contributor, "contributor");
		descriptionTerms.put(DcTerm.creator, "creator");
		descriptionTerms.put(DcTerm.description, "description");
		descriptionTerms.put(DcTerm.language, "language");
		descriptionTerms.put(DcTerm.references, "source");
		descriptionTerms.put(DcTerm.source, "references");
		descriptionTerms.put(DcTerm.type, "type");

		referenceTerms.put(DcTerm.accessRights,"accessRights");
		referenceTerms.put(DwcTerm.datasetID,"authority.identifier");
		referenceTerms.put(DwcTerm.datasetName,"authority.title");
		referenceTerms.put(DcTerm.created,"created");
		referenceTerms.put(DcTerm.license,"license");
		referenceTerms.put(DcTerm.modified,"modified");
		referenceTerms.put(DcTerm.rights,"rights");
		referenceTerms.put(DcTerm.rightsHolder,"rightsHolder");
		referenceTerms.put(DcTerm.identifier, "identifier");
		referenceTerms.put(DwcTerm.taxonID, "taxa[0].identifier");
		referenceTerms.put(DcTerm.bibliographicCitation, "bibliographicCitation");
		referenceTerms.put(DcTerm.creator, "creator");
		referenceTerms.put(DcTerm.date, "date");
		referenceTerms.put(DcTerm.description, "description");
		referenceTerms.put(DcTerm.language, "language");
		referenceTerms.put(DcTerm.source, "source");
		referenceTerms.put(DcTerm.subject, "subject");
		referenceTerms.put(DwcTerm.taxonRemarks, "taxonRemarks");
		referenceTerms.put(DcTerm.title, "title");
		referenceTerms.put(DcTerm.type, "type");

		imageTerms.put(DcTerm.accessRights,"accessRights");
		imageTerms.put(DwcTerm.datasetID,"authority.identifier");
		imageTerms.put(DwcTerm.datasetName,"authority.title");
		imageTerms.put(DcTerm.created,"created");
		imageTerms.put(DcTerm.license,"license");
		imageTerms.put(DcTerm.modified,"modified");
		imageTerms.put(DcTerm.rights,"rights");
		imageTerms.put(DcTerm.rightsHolder,"rightsHolder");
		imageTerms.put(DcTerm.identifier, "identifier");
		imageTerms.put(DwcTerm.taxonID, "taxa[0].identifier");
		imageTerms.put(DcTerm.audience, "audience");
		imageTerms.put(DcTerm.contributor, "contributor");
		imageTerms.put(DcTerm.creator, "creator");
		imageTerms.put(DcTerm.description, "description");
		imageTerms.put(DcTerm.format, "format");
		imageTerms.put(Wgs84Term.latitude, "latitude");
		imageTerms.put(Wgs84Term.longitude, "longitude");
		imageTerms.put(DcTerm.spatial, "location");
		imageTerms.put(DcTerm.publisher, "publisher");
		imageTerms.put(DcTerm.references, "references");
		imageTerms.put(DcTerm.subject, "subject");
		imageTerms.put(DcTerm.title, "title");

		identifierTerms.put(DcTerm.accessRights,"accessRights");
		identifierTerms.put(DwcTerm.datasetID,"authority.identifier");
		identifierTerms.put(DwcTerm.datasetName,"authority.title");
		identifierTerms.put(DcTerm.created,"created");
		identifierTerms.put(DcTerm.license,"license");
		identifierTerms.put(DcTerm.modified,"modified");
		identifierTerms.put(DcTerm.rights,"rights");
		identifierTerms.put(DcTerm.rightsHolder,"rightsHolder");
		identifierTerms.put(DcTerm.identifier, "identifier");
		identifierTerms.put(DwcTerm.taxonID, "taxon.identifier");
		identifierTerms.put(DcTerm.format, "format");
		identifierTerms.put(DcTerm.title, "title");
		identifierTerms.put(DcTerm.subject, "subject");

		measurementOrFactTerms.put(DcTerm.accessRights,"accessRights");
		measurementOrFactTerms.put(DwcTerm.datasetID,"authority.identifier");
		measurementOrFactTerms.put(DwcTerm.datasetName,"authority.title");
		measurementOrFactTerms.put(DcTerm.created,"created");
		measurementOrFactTerms.put(DcTerm.license,"license");
		measurementOrFactTerms.put(DcTerm.modified,"modified");
		measurementOrFactTerms.put(DcTerm.rights,"rights");
		measurementOrFactTerms.put(DcTerm.rightsHolder,"rightsHolder");
		measurementOrFactTerms.put(DcTerm.identifier, "identifier");
		measurementOrFactTerms.put(DwcTerm.taxonID, "taxon.identifier");
		measurementOrFactTerms.put(DwcTerm.measurementAccuracy, "measurementAccuracy");
		measurementOrFactTerms.put(DwcTerm.measurementDeterminedBy, "measurementDeterminedBy");
		measurementOrFactTerms.put(DwcTerm.measurementDeterminedDate, "measurementDeterminedDate");
		measurementOrFactTerms.put(DwcTerm.measurementMethod, "measurementMethod");
		measurementOrFactTerms.put(DwcTerm.measurementRemarks, "measurementRemarks");
		measurementOrFactTerms.put(DwcTerm.measurementType, "measurementType");
		measurementOrFactTerms.put(DwcTerm.measurementUnit, "measurementUnit");
		measurementOrFactTerms.put(DwcTerm.measurementValue, "measurementValue");

		vernacularNameTerms.put(DcTerm.accessRights,"accessRights");
		vernacularNameTerms.put(DwcTerm.datasetID,"authority.identifier");
		vernacularNameTerms.put(DwcTerm.datasetName,"authority.title");
		vernacularNameTerms.put(DcTerm.created,"created");
		vernacularNameTerms.put(DcTerm.license,"license");
		vernacularNameTerms.put(DcTerm.modified,"modified");
		vernacularNameTerms.put(DcTerm.rights,"rights");
		vernacularNameTerms.put(DcTerm.rightsHolder,"rightsHolder");
		vernacularNameTerms.put(DcTerm.identifier, "identifier");
		vernacularNameTerms.put(DwcTerm.taxonID, "taxon.identifier");
		vernacularNameTerms.put(DwcTerm.countryCode, "countryCode");
		vernacularNameTerms.put(DcTerm.language, "language");
		vernacularNameTerms.put(DwcTerm.lifeStage, "lifeStage");
		vernacularNameTerms.put(DwcTerm.locality, "locality");
		vernacularNameTerms.put(DwcTerm.locationID, "location");
		vernacularNameTerms.put(GbifTerm.organismPart, "organismPart");
		vernacularNameTerms.put(GbifTerm.isPlural, "plural");
		vernacularNameTerms.put(GbifTerm.isPreferredName, "preferredName");
		vernacularNameTerms.put(DwcTerm.sex, "sex");
		vernacularNameTerms.put(DcTerm.source, "source");
		vernacularNameTerms.put(DwcTerm.taxonRemarks, "taxonRemarks");
		vernacularNameTerms.put(DcTerm.temporal, "temporal");
		vernacularNameTerms.put(DwcTerm.vernacularName, "vernacularName");

		typeAndSpecimenTerms.put(DcTerm.accessRights,"accessRights");
		typeAndSpecimenTerms.put(DwcTerm.datasetID,"authority.identifier");
		typeAndSpecimenTerms.put(DwcTerm.datasetName,"authority.title");
		typeAndSpecimenTerms.put(DcTerm.created,"created");
		typeAndSpecimenTerms.put(DcTerm.license,"license");
		typeAndSpecimenTerms.put(DcTerm.modified,"modified");
		typeAndSpecimenTerms.put(DcTerm.rights,"rights");
		typeAndSpecimenTerms.put(DcTerm.rightsHolder,"rightsHolder");
		typeAndSpecimenTerms.put(DcTerm.identifier, "identifier");
		typeAndSpecimenTerms.put(DwcTerm.taxonID, "taxa[0].identifier");
		typeAndSpecimenTerms.put(DcTerm.bibliographicCitation, "bibliographicCitation");
		typeAndSpecimenTerms.put(DwcTerm.catalogNumber, "catalogNumber");
		typeAndSpecimenTerms.put(DwcTerm.collectionCode, "collectionCode");
		typeAndSpecimenTerms.put(DwcTerm.decimalLatitude, "decimalLatitude");
		typeAndSpecimenTerms.put(DwcTerm.decimalLongitude, "decimalLongitude");
		typeAndSpecimenTerms.put(DwcTerm.institutionCode, "institutionCode");
		typeAndSpecimenTerms.put(DwcTerm.locality, "locality");
		typeAndSpecimenTerms.put(DwcTerm.locationID, "location");
		typeAndSpecimenTerms.put(DwcTerm.recordedBy, "recordedBy");
		typeAndSpecimenTerms.put(DwcTerm.scientificName, "scientificName");
		typeAndSpecimenTerms.put(DwcTerm.sex, "sex");
		typeAndSpecimenTerms.put(DcTerm.source, "source");
		typeAndSpecimenTerms.put(DwcTerm.taxonRank, "taxonRank");
		typeAndSpecimenTerms.put(GbifTerm.typeDesignatedBy, "typeDesignatedBy");
		typeAndSpecimenTerms.put(GbifTerm.typeDesignationType, "typeDesignationType");
		typeAndSpecimenTerms.put(DwcTerm.typeStatus, "typeStatus");
		typeAndSpecimenTerms.put(DwcTerm.verbatimEventDate, "verbatimEventDate");
		typeAndSpecimenTerms.put(GbifTerm.verbatimLabel, "verbatimLabel");
		typeAndSpecimenTerms.put(DwcTerm.verbatimLatitude, "verbatimLatitude");
		typeAndSpecimenTerms.put(DwcTerm.verbatimLongitude, "verbatimLongitude");

		skosTerms.put(DcTerm.accessRights,"accessRights");
		skosTerms.put(DwcTerm.datasetID,"authority.identifier");
		skosTerms.put(DwcTerm.datasetName,"authority.title");
		skosTerms.put(DcTerm.created,"created");
		skosTerms.put(DcTerm.license,"license");
		skosTerms.put(DcTerm.modified,"modified");
		skosTerms.put(DcTerm.rights,"rights");
		skosTerms.put(DcTerm.rightsHolder,"rightsHolder");
		skosTerms.put(DcTerm.identifier, "identifier");
		skosTerms.put(DwcTerm.taxonID, "taxa[0].identifier");
		skosTerms.put(DcTerm.creator, "creator");
		skosTerms.put(DcTerm.source, "source");
		skosTerms.put(SkosTerm.altLabel, "altLabel");
		skosTerms.put(SkosTerm.definition, "definition");
		skosTerms.put(SkosTerm.exactMatch, "exactMatch.identifier");
		skosTerms.put(SkosTerm.prefLabel, "prefLabel");
		skosTerms.put(SkosTerm.prefSymbol, "prefSymbol.identifier");
		skosTerms.put(SkosTerm.related, "related.identifier");


	}

	public static Map<Term, String> getPropertyMap(Term conceptTerm) {
		if(conceptTerm.equals(DwcTerm.Taxon)) {
			return taxonTerms;
		} else if(conceptTerm.equals(GbifTerm.Description)) {
			return descriptionTerms;
		} else if(conceptTerm.equals(GbifTerm.Distribution)) {
			return distributionTerms;
		} else if(conceptTerm.equals(GbifTerm.Identifier)) {
			return identifierTerms;
		} else if(conceptTerm.equals(DwcTerm.MeasurementOrFact)) {
			return measurementOrFactTerms;
		} else if(conceptTerm.equals(GbifTerm.VernacularName)) {
			return vernacularNameTerms;
		} else if(conceptTerm.equals(GbifTerm.Image)) {
			return imageTerms;
		} else if(conceptTerm.equals(GbifTerm.TypesAndSpecimen)) {
			return typeAndSpecimenTerms;
		} else if(conceptTerm.equals(GbifTerm.Reference)) {
			return referenceTerms;
		} else if(conceptTerm.equals(SkosTerm.Concept)) {
			return skosTerms;
		} else {
			throw new IllegalArgumentException(conceptTerm.qualifiedName() + " is not a supported term");
		}
	}

	public static SortedSet<Term> getConceptTerms(Term conceptTerm) {
		SortedSet<Term> conceptTerms = new TreeSet<Term>(new TermComparator());
		if(conceptTerm.equals(DwcTerm.Taxon)) {
			conceptTerms.addAll(taxonTerms.keySet());
		} else if(conceptTerm.equals(GbifTerm.Description)) {
			conceptTerms.addAll(descriptionTerms.keySet());
		} else if(conceptTerm.equals(GbifTerm.Distribution)) {
			conceptTerms.addAll(distributionTerms.keySet());
		} else if(conceptTerm.equals(GbifTerm.Identifier)) {
			conceptTerms.addAll(identifierTerms.keySet());
		} else if(conceptTerm.equals(DwcTerm.MeasurementOrFact)) {
			conceptTerms.addAll(measurementOrFactTerms.keySet());
		} else if(conceptTerm.equals(GbifTerm.VernacularName)) {
			conceptTerms.addAll(vernacularNameTerms.keySet());
		} else if(conceptTerm.equals(GbifTerm.Image)) {
			conceptTerms.addAll(imageTerms.keySet());
		} else if(conceptTerm.equals(GbifTerm.TypesAndSpecimen)) {
			conceptTerms.addAll(typeAndSpecimenTerms.keySet());
		} else if(conceptTerm.equals(GbifTerm.Reference)) {
			conceptTerms.addAll(referenceTerms.keySet());
		} else if(conceptTerm.equals(SkosTerm.Concept)) {
			conceptTerms.addAll(skosTerms.keySet());
		} else {
			throw new IllegalArgumentException(conceptTerm.qualifiedName() + " is not a supported term");
		}
		return conceptTerms;
	}

}
