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
package org.powo.job.dwc.exception;

import org.powo.model.BaseData;
import org.powo.model.constants.AnnotationCode;
import org.powo.model.constants.AnnotationType;
import org.powo.model.constants.RecordType;

/**
 *
 * @author ben
 *
 */
public class RequiredFieldException extends DarwinCoreProcessingException {

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
	public RequiredFieldException(final String msg, final RecordType recordType, final Integer lineNumber) {
		super(msg, AnnotationCode.BadField, recordType, lineNumber.toString());
	}

	/**
	 * @param msg the message
	 * @param object the object missing a required field
	 */
	public RequiredFieldException(final String msg, final BaseData object) {
		super(msg, AnnotationCode.BadField, RecordType.forClass(object.getClass()), object.getIdentifier());
	}

	@Override
	public final AnnotationType getType() {
		return AnnotationType.Error;
	}

}
