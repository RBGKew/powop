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
public abstract class DarwinCoreProcessingException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 5761352758463935293L;

	/**
	 *
	 */
	private AnnotationCode code;

	/**
	 *
	 */
	private String value;

	/**
	 *
	 */
	private RecordType recordType;

	/**
	 *
	 */
	public DarwinCoreProcessingException() {
		super();
	}

	/**
	 *
	 * @param message
	 *            Set the message
	 * @param code
	 *            Set the code
	 */
	public DarwinCoreProcessingException(final String message,
			AnnotationCode code, RecordType recordType, String value) {
		super(message);
		this.code = code;
		this.recordType = recordType;
		this.value = value;
	}

	/**
	 *
	 * @param cause
	 *            Set the cause
	 */
	public DarwinCoreProcessingException(final Throwable cause) {
		super(cause);
	}

	/**
	 *
	 * @param message
	 *            Set the message
	 * @param cause
	 *            Set the cause
	 */
	public DarwinCoreProcessingException(final String message,
			final Throwable cause) {
		super(message, cause);
	}

	/**
	 *
	 * @return a short code representing this class of error
	 */
	public final AnnotationCode getCode() {
		return code;
	}

	/**
	 *
	 * @return the type of annotation to create
	 */
	public abstract AnnotationType getType();

	/**
	 *
	 * @return the record type
	 */
	public final RecordType getRecordType() {
		return recordType;
	}

	/**
	 *
	 * @return the value
	 */
	public final String getValue() {
		return value;
	}

}
