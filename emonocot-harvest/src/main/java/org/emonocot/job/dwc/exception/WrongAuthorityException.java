package org.emonocot.job.dwc.exception;

import org.emonocot.model.constants.AnnotationCode;
import org.emonocot.model.constants.AnnotationType;
import org.emonocot.model.constants.RecordType;

public class WrongAuthorityException extends DarwinCoreProcessingException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1014545909015139552L;

	public WrongAuthorityException(String msg, RecordType recordType,Integer lineNumber) {
		super(msg, AnnotationCode.WrongAuthority, recordType, lineNumber.toString());
	}

	@Override
	public AnnotationType getType() {		
		return AnnotationType.Error;
	}

}
