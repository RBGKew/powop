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
package org.powo.harvest.media;

import org.joda.time.DateTime;
import org.powo.harvest.common.AbstractRecordAnnotator;
import org.powo.model.Annotation;
import org.powo.model.Image;
import org.powo.model.constants.AnnotationCode;
import org.powo.model.constants.AnnotationType;
import org.powo.model.constants.RecordType;

public class ImageAnnotatorImpl extends AbstractRecordAnnotator implements ImageAnnotator {

	private Long jobId;

	/**
	 * @param jobId the jobId to set
	 */
	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	/* (non-Javadoc)
	 * @see org.emonocot.harvest.media.ImageAnnotator#annotate(org.emonocot.model.Image, org.emonocot.model.constants.AnnotationCode, java.lang.String)
	 */
	@Override
	public void annotate(Image image, AnnotationType type, AnnotationCode code, String message) {
		Annotation a = new Annotation();
		a.setAnnotatedObj(image);
		a.setAuthority(getSource());
		if(jobId != null) {
			a.setJobId(jobId);
		}
		a.setCode(code);
		a.setType(type);
		a.setDateTime(new DateTime());
		a.setRecordType(RecordType.Image);
		a.setText(message);
		super.annotate(a);
	}
}
