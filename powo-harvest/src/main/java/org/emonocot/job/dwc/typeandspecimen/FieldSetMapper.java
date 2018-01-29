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
package org.emonocot.job.dwc.typeandspecimen;

import org.emonocot.job.dwc.read.NonOwnedFieldSetMapper;
import org.emonocot.model.TypeAndSpecimen;
import org.emonocot.model.constants.TypeDesignationType;
import org.gbif.dwc.terms.DcTerm;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.terms.GbifTerm;
import org.gbif.dwc.terms.Term;
import org.gbif.ecat.voc.Rank;
import org.gbif.ecat.voc.Sex;
import org.gbif.ecat.voc.TypeStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;

/**
 *
 * @author ben
 *
 */
public class FieldSetMapper extends  NonOwnedFieldSetMapper<TypeAndSpecimen> {

	/**
	 *
	 */
	public FieldSetMapper() {
		super(TypeAndSpecimen.class);
	}

	/**
	 *
	 */
	private Logger logger = LoggerFactory.getLogger(FieldSetMapper.class);


	@Override
	public final void mapField(final TypeAndSpecimen object, final String fieldName,
			final String value) throws BindException {
		super.mapField(object, fieldName, value);

		Term term = getTermFactory().findTerm(fieldName);
		if (term instanceof DcTerm) {
			DcTerm dcTerm = (DcTerm) term;
			switch (dcTerm) {
			case bibliographicCitation:
				object.setBibliographicCitation(value);
				break;
			case source:
				object.setSource(value);
				break;

			default:
				break;
			}
		}

		// DwcTerms
		if (term instanceof DwcTerm) {
			DwcTerm dwcTerm = (DwcTerm) term;

			switch (dwcTerm) {
			case catalogNumber:
				object.setCatalogNumber(value);
				break;
			case collectionCode:
				object.setCollectionCode(value);
				break;
			case decimalLatitude:
				object.setDecimalLatitude(conversionService.convert(value, Double.class));
				break;
			case decimalLongitude:
				object.setDecimalLongitude(conversionService.convert(value, Double.class));
				break;
			case institutionCode:
				object.setInstitutionCode(value);
				break;
			case locality:
				object.setLocality(value);
				break;
			case occurrenceID:
				object.setIdentifier(value);
				break;
			case recordedBy:
				object.setRecordedBy(value);
				break;
			case scientificName:
				object.setScientificName(value);
				break;
			case sex:
				object.setSex(conversionService.convert(value,Sex.class));
				break;
			case taxonRank:
				object.setTaxonRank(conversionService.convert(value, Rank.class));
				break;
			case typeStatus:
				object.setTypeStatus(conversionService.convert(value, TypeStatus.class));
				break;
			case verbatimEventDate:
				object.setVerbatimEventDate(value);
				break;
			case verbatimLatitude:
				object.setVerbatimLatitude(value);
				break;
			case verbatimLongitude:
				object.setVerbatimLongitude(value);
				break;
			default:
				break;
			}
		}

		// Gbif Terms
		if (term instanceof GbifTerm) {
			GbifTerm gbifTerm = (GbifTerm) term;
			switch(gbifTerm) {
			case typeDesignatedBy:
				object.setTypeDesignatedBy(value);
				break;
			case typeDesignationType:
				object.setTypeDesignationType(conversionService.convert(value, TypeDesignationType.class));
				break;
			case verbatimLabel:
				object.setVerbatimLabel(value);
				break;
			default:
				break;
			}
		}
	}
}
