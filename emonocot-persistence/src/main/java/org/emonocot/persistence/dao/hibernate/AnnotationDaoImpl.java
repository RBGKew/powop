package org.emonocot.persistence.dao.hibernate;

import java.util.HashMap;
import java.util.Map;

import org.emonocot.model.Annotation;
import org.emonocot.model.hibernate.Fetch;
import org.emonocot.persistence.dao.AnnotationDao;
import org.hibernate.FetchMode;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ben
 *
 */
@Repository
public class AnnotationDaoImpl extends SearchableDaoImpl<Annotation> implements
        AnnotationDao {

    /**
    *
    */
   private static Map<String, Fetch[]> FETCH_PROFILES;

   static {
       FETCH_PROFILES = new HashMap<String, Fetch[]>();
       FETCH_PROFILES.put("annotated-obj", new Fetch[] {
               new Fetch("annotatedObj", FetchMode.SELECT)
       });
   }

    /**
     *
     */
    public AnnotationDaoImpl() {
        super(Annotation.class);
    }
    
    @Override
    protected boolean isSearchableObject() {
		return false;
	}

    @Override
    protected final Fetch[] getProfile(final String profile) {
        return AnnotationDaoImpl.FETCH_PROFILES.get(profile);
    }
}
