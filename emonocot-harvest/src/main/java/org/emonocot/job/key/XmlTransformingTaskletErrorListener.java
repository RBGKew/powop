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