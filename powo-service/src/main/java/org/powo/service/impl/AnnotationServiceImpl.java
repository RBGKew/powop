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
package org.powo.service.impl;

import org.powo.api.AnnotationService;
import org.powo.model.Annotation;
import org.powo.model.constants.RecordType;
import org.powo.persistence.dao.AnnotationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ben
 *
 */
@Service
public class AnnotationServiceImpl extends
SearchableServiceImpl<Annotation, AnnotationDao> implements
AnnotationService {

	/**
	 *
	 * @param annotationDao Set the annotation dao
	 */
	@Autowired
	public final void setAnnotationDao(final AnnotationDao annotationDao) {
		this.dao = annotationDao;
	}

	@Override
	public Annotation findAnnotation(RecordType recordType, Long id, Long jobId) {
		return dao.findAnnotation(recordType, id, jobId);
	}
}
