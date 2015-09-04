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
package org.emonocot.job.dwc.reference;

import java.util.Locale;

import org.emonocot.job.dwc.read.NonOwnedFieldSetMapper;
import org.emonocot.model.Reference;
import org.emonocot.model.constants.ReferenceType;
import org.gbif.dwc.terms.DcTerm;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.terms.Term;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;

/**
 *
 * @author ben
 *
 */
public class FieldSetMapper extends  NonOwnedFieldSetMapper<Reference> {

	/**
	 *
	 */
	public FieldSetMapper() {
		super(Reference.class);
	}

	/**
	 *
	 */
	private Logger logger = LoggerFactory.getLogger(FieldSetMapper.class);


	@Override
	public final void mapField(final Reference object, final String fieldName,
			final String value) throws BindException {
		super.mapField(object, fieldName, value);

		Term term = getTermFactory().findTerm(fieldName);
		if (term instanceof DcTerm) {
			DcTerm dcTerm = (DcTerm) term;
			switch (dcTerm) {
			case bibliographicCitation:
				object.setBibliographicCitation(htmlSanitizer.sanitize(value));
				break;
			case creator:
				object.setCreator(value);
				break;
			case date:
				object.setDate(value);
				break;
			case description:
				object.setDescription(value);
				break;
			case identifier:
				object.setIdentifier(value);
				break;
			case language:
				object.setLanguage(conversionService.convert(value, Locale.class));
				break;
			case relation:
				object.setUri(value);
				break;
			case source:
				object.setSource(value);
				break;
			case subject:
				object.setSubject(value);
				break;
			case title:
				object.setTitle(htmlSanitizer.sanitize(value));
				break;
			case type:
				object.setType(conversionService.convert(value, ReferenceType.class));
				break;
			default:
				break;
			}
		}

		// DwcTerms
		if (term instanceof DwcTerm) {
			DwcTerm dwcTerm = (DwcTerm) term;
			switch (dwcTerm) {
			case taxonRemarks:
				object.setTaxonRemarks(value);
				break;
			default:
				break;
			}
		}
	}
}
