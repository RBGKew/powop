package org.emonocot.portal.remoting;

import org.emonocot.model.common.Annotation;
import org.emonocot.persistence.dao.AnnotationDao;

/**
 *
 * @author ben
 *
 */
public class AnnotationDaoImpl extends DaoImpl<Annotation> implements
        AnnotationDao {

    /**
     *
     */
    public AnnotationDaoImpl() {
        super(Annotation.class, "annotation");
    }

}
