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
package org.emonocot.job.dwc.measurementorfact;

import org.emonocot.job.dwc.read.OwnedEntityFieldSetMapper;
import org.emonocot.model.MeasurementOrFact;
import org.emonocot.model.constants.MeasurementUnit;
import org.gbif.dwc.terms.DcTerm;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.terms.Term;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;

/**
 *
 * @author ben
 *
 */
public class FieldSetMapper extends OwnedEntityFieldSetMapper<MeasurementOrFact> {

	/**
	 *
	 */
	public FieldSetMapper() {
		super(MeasurementOrFact.class);
	}

	/**
	 *
	 */
	private Logger logger = LoggerFactory.getLogger(FieldSetMapper.class);

	@Override
	public final void mapField(final MeasurementOrFact object,
			final String fieldName, final String value) throws BindException {
		super.mapField(object, fieldName, value);

		Term term = getTermFactory().findTerm(fieldName);
		logger.debug("Mapping " + fieldName + " " + " " + value + " to " + object);
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
			case measurementAccuracy:
				object.setMeasurementAccuracy(value);
				break;
			case measurementDeterminedBy:
				object.setMeasurementDeterminedBy(value);
				break;
			case measurementDeterminedDate:
				object.setMeasurementDeterminedDate(conversionService.convert(value, DateTime.class));
				break;
			case measurementMethod:
				object.setMeasurementMethod(value);
				break;
			case measurementRemarks:
				object.setMeasurementRemarks(value);
				break;
			case measurementType:
				object.setMeasurementType(conversionService.convert(value, Term.class));
				break;
			case measurementUnit:
				object.setMeasurementUnit(conversionService.convert(value,MeasurementUnit.class));
				break;
			case measurementValue:
				object.setMeasurementValue(value);
				break;
			case measurementID:
				object.setIdentifier(value);
			default:
				break;
			}
		}
	}
}
