package org.emonocot.job.dwc.exception;

import org.emonocot.model.constants.AnnotationCode;
import org.emonocot.model.constants.AnnotationType;
import org.emonocot.model.constants.RecordType;

public class InvalidValuesException extends DarwinCoreProcessingException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7264927553122083298L;

	public InvalidValuesException(String msg, RecordType recordType, Integer lineNumber) {		
		super(msg, AnnotationCode.BadField, recordType, lineNumber.toString());
	}

	@Override
	public AnnotationType getType() {
		return AnnotationType.Error;
	}

}
