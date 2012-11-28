package org.emonocot.api;

import org.emonocot.model.Annotation;
import org.emonocot.model.constants.RecordType;

/**
 *
 * @author ben
 *
 */
public interface AnnotationService extends SearchableService<Annotation> {

	Annotation findAnnotation(RecordType recordType, Long id, Long jobId);

}
