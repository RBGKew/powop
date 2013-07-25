package org.emonocot.job.dwc.exception;

import org.emonocot.model.constants.AnnotationCode;
import org.emonocot.model.constants.AnnotationType;
import org.emonocot.model.constants.RecordType;

public class ImageRetrievalException extends DarwinCoreProcessingException {

	private static final long serialVersionUID = 566244600533446756L;
	
	public ImageRetrievalException(String identifier) {
		super("Could not retrieve " + identifier, AnnotationCode.BadIdentifier, RecordType.Image, identifier);
	}

	@Override
	public AnnotationType getType() {
		return AnnotationType.Error;
	}

}
