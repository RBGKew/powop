package org.emonocot.persistence.dao;

import org.emonocot.model.Annotation;
import org.emonocot.model.constants.RecordType;

/**
 *
 * @author ben
 *
 */
public interface AnnotationDao extends SearchableDao<Annotation> {

	Annotation findAnnotation(RecordType recordType, Long id, Long jobId);

}
