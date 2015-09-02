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
package org.emonocot.job.dwc.identifier;

import org.emonocot.job.dwc.read.OwnedEntityFieldSetMapper;
import org.emonocot.model.Identifier;
import org.gbif.dwc.terms.Term;
import org.gbif.dwc.terms.DcTerm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;

/**
 *
 * @author ben
 *
 */
public class FieldSetMapper extends OwnedEntityFieldSetMapper<Identifier> {

	/**
	 *
	 */
	public FieldSetMapper() {
		super(Identifier.class);
	}

	/**
	 *
	 */
	private Logger logger = LoggerFactory.getLogger(FieldSetMapper.class);

	@Override
	public final void mapField(final Identifier object, final String fieldName,
			final String value) throws BindException {

		super.mapField(object, fieldName, value);
		Term term = getTermFactory().findTerm(fieldName);
		if (term instanceof DcTerm) {
			DcTerm dcTerm = (DcTerm) term;
			switch (dcTerm) {
			case format:
				object.setFormat(value);
				break;
			case identifier:
				object.setIdentifier(value);
				break;
			case subject:
				object.setSubject(value);
				break;
			case title:
				object.setTitle(value);
				break;
			default:
				break;
			}
		}
	}
}
