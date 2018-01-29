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
package org.emonocot.job.dwc.exception;

import org.emonocot.model.constants.AnnotationCode;
import org.emonocot.model.constants.AnnotationType;
import org.emonocot.model.constants.RecordType;

/**
 *
 * @author ben
 *
 */
public class OutOfScopeTaxonException extends DarwinCoreProcessingException {

	/**
	 *
	 */
	private static final long serialVersionUID = 4236165074026471554L;

	/**
	 *
	 * @param msg Set the message
	 * @param recordType the type of object missing a taxon
	 * @param lineNumber the record number with a missing taxon identifier
	 */
	public OutOfScopeTaxonException(final String msg, final RecordType recordType,
			final Integer lineNumber) {
		super(msg, AnnotationCode.BadField, recordType, lineNumber.toString());
	}

	@Override
	public final AnnotationType getType() {
		return AnnotationType.Error;
	}

}
