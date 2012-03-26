package org.emonocot.portal.remoting;

import org.emonocot.model.common.Annotation;
import org.emonocot.model.pager.Page;
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

    public Page<Annotation> searchByExample(Annotation arg0, boolean arg1,
            boolean arg2) {
        // TODO Auto-generated method stub
        return null;
    }

}
