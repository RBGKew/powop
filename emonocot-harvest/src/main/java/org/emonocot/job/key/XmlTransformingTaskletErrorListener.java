package org.emonocot.job.key;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;

import org.emonocot.harvest.common.AbstractRecordAnnotator;
import org.emonocot.model.Annotation;
import org.emonocot.model.constants.AnnotationCode;
import org.emonocot.model.constants.AnnotationType;
import org.emonocot.model.constants.RecordType;
import org.joda.time.DateTime;

public class XmlTransformingTaskletErrorListener extends AbstractRecordAnnotator implements ErrorListener {
	private Long jobExecutionId;  
	
    public void setJobExecutionId(Long jobExecutionId) {
    	this.jobExecutionId = jobExecutionId;
    }
	
	private void doAnnotate(TransformerException exception, AnnotationType type) {
		Annotation annotation = new Annotation();
		annotation.setRecordType(RecordType.IdentificationKey);
		annotation.setJobId(jobExecutionId);
		annotation.setAuthority(getSource());
		annotation.setType(type);
		annotation.setDateTime(new DateTime());
		annotation.setCode(AnnotationCode.BadRecord);
		annotation.setText(exception.getMessage());
		super.annotate(annotation);
	}

	@Override
	public void warning(TransformerException exception)
			throws TransformerException {
		logger.warn("Warning processing xml: " + exception.getMessage(), exception);
		doAnnotate(exception,AnnotationType.Warn);
		
	}

	@Override
	public void error(TransformerException exception)
			throws TransformerException {
		logger.error("Error processing xml: " + exception.getMessage(), exception);
		doAnnotate(exception,AnnotationType.Error);
		
	}

	@Override
	public void fatalError(TransformerException exception)
			throws TransformerException {
		logger.error("Fatal error processing xml: " + exception.getMessage(), exception);
	}
	
}