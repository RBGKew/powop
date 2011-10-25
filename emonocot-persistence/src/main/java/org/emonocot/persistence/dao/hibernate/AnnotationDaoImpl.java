package org.emonocot.persistence.dao.hibernate;

import org.emonocot.model.common.Annotation;
import org.emonocot.model.hibernate.Fetch;
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
        super(Annotation.class);
    }

    @Override
    protected Fetch[] getProfile(final String profile) {
        // TODO Auto-generated method stub
        return null;
    }
}
