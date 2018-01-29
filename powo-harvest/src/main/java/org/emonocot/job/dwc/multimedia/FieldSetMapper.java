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
package org.emonocot.job.dwc.multimedia;

import org.emonocot.api.job.TermFactory;
import org.emonocot.common.HtmlSanitizer;
import org.emonocot.job.dwc.read.NonOwnedFieldSetMapper;
import org.emonocot.model.Image;
import org.emonocot.model.Multimedia;
import org.emonocot.model.constants.MediaFormat;
import org.emonocot.model.constants.MediaType;
import org.gbif.dwc.terms.DcTerm;
import org.gbif.dwc.terms.Term;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;

/**
 *
 * @author jk00kg
 *
 */
public class FieldSetMapper extends
NonOwnedFieldSetMapper<Multimedia> {

	/**
	 *
	 */
	public FieldSetMapper() {
		super(Multimedia.class);
	}

	/**
	 *
	 */
	private Logger logger = LoggerFactory.getLogger(FieldSetMapper.class);

	@Override
	public void mapField(Multimedia object, final String fieldName,
			final String value) throws BindException {
		super.mapField(object, fieldName, value);
		Term term = TermFactory.findTerm(fieldName);
		if (term instanceof DcTerm) {
			DcTerm dcTerm = (DcTerm) term;
			switch (dcTerm) {
			case audience:
				object.setAudience(HtmlSanitizer.sanitize(value));
				break;
			case contributor:
				object.setContributor(HtmlSanitizer.sanitize(value));
				break;
			case creator:
				object.setCreator(HtmlSanitizer.sanitize(value));
				break;
			case description:
				object.setDescription(HtmlSanitizer.sanitize(value));
				break;
			case format:
				object.setFormat(conversionService.convert(value, MediaFormat.class));
				break;
			case identifier:
				object.setIdentifier(value);
				break;
			case publisher:
				object.setPublisher(HtmlSanitizer.sanitize(value));
				break;
			case references:
				object.setReferences(value);
				break;
			case source:
				object.setSource(value);
				break;
			case title:
				object.setTitle(HtmlSanitizer.sanitize(value));
				break;
			case type:
				MediaType mediaType = conversionService.convert(value, MediaType.class);
				if (mediaType != null) {
					switch (mediaType) {
					case StillImage:
					case Image:
						object = conversionService.convert(object, Image.class);
						break;
					case InteractiveResource:
					default:
						object.setType(mediaType);
					}
				} else {
					logger.debug("No MediaType was provided. This should be added");
				}
				break;
			default:
				break;
			}
		}
	}
}
