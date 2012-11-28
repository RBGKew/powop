package org.emonocot.service.impl;

import org.emonocot.api.AnnotationService;
import org.emonocot.model.Annotation;
import org.emonocot.model.constants.RecordType;
import org.emonocot.persistence.dao.AnnotationDao;
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
