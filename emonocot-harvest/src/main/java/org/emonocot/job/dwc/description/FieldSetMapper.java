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
package org.emonocot.job.dwc.description;

import java.util.Locale;

import org.emonocot.api.job.TermFactory;
import org.emonocot.job.dwc.read.OwnedEntityFieldSetMapper;
import org.emonocot.model.Description;
import org.emonocot.model.Reference;
import org.emonocot.model.constants.DescriptionType;
import org.gbif.dwc.terms.DcTerm;
import org.gbif.dwc.terms.Term;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;

public class FieldSetMapper extends OwnedEntityFieldSetMapper<Description> {

	public FieldSetMapper() {
		super(Description.class);
	}

	private Logger logger = LoggerFactory.getLogger(FieldSetMapper.class);

	@Override
	public final void mapField(final Description object,
			final String fieldName, final String value) throws BindException {
		super.mapField(object, fieldName, value);

		Term term = TermFactory.findTerm(fieldName);
		logger.info("Mapping " + fieldName + " " + " " + value + " to "
				+ object);
		if (term instanceof DcTerm) {
			DcTerm dcTerm = (DcTerm) term;
			switch (dcTerm) {
			case audience:
				object.setAudience(value);
				break;
			case creator:
				object.setCreator(value);
				break;
			case contributor:
				object.setContributor(value);
				break;
			case description:
				object.setDescription(htmlSanitizer.sanitize(value));
				break;
			case identifier:
				object.setIdentifier(value);
				break;
			case language:
				object.setLanguage(conversionService.convert(value, Locale.class));
				break;
			case references:
				if (value.indexOf(",") != -1) {
					String[] values = value.split(",");
					for (String v : values) {
						addReference(object, v);
					}
				} else {
					addReference(object,value);
				}
				break;
			case source:
				object.setSource(value);
				break;
			case type:
				object.setType(conversionService.convert(value, DescriptionType.class));
				break;
			default:
				break;
			}
		}
	}

	private void addReference(Description object, String value) {
		Reference reference = new Reference();
		reference.setIdentifier(value);
		object.getReferences().add(reference);
	}
}
