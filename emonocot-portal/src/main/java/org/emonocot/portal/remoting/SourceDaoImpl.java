package org.emonocot.portal.remoting;

import org.emonocot.model.Source;
import org.emonocot.persistence.dao.SourceDao;
import org.springframework.stereotype.Repository;

/**
 *
 * @author annapaola
 *
 */
@Repository
public class SourceDaoImpl extends DaoImpl<Source> implements SourceDao {
   /**
    *
    */
   public SourceDaoImpl() {
       super(Source.class, "source");
   }

}
