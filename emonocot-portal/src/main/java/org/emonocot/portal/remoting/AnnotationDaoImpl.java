package org.emonocot.portal.remoting;

import org.emonocot.model.Annotation;
import org.emonocot.model.constants.RecordType;
import org.emonocot.pager.Page;
import org.emonocot.persistence.dao.AnnotationDao;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ben
 *
 */
@Repository
public class AnnotationDaoImpl extends DaoImpl<Annotation> implements
        AnnotationDao {

    /**
     *
     */
    public AnnotationDaoImpl() {
        super(Annotation.class, "annotation");
    }

    public Page<Annotation> searchByExample(Annotation example, boolean ignoreCase,
            boolean useLike) {
        throw new UnsupportedOperationException("Remote searching by example is unimplemented");
    }

	@Override
	public Annotation findAnnotation(RecordType recordType, Long id, Long jobId) {
		throw new UnsupportedOperationException("This method is unimplemented");
	}

}
