package org.emonocot.portal.remoting;

import org.emonocot.model.registry.Organisation;
import org.emonocot.persistence.dao.OrganisationDao;
import org.springframework.stereotype.Repository;

/**
 *
 * @author annapaola
 *
 */
@Repository
public class OrganisationDaoImpl extends DaoImpl<Organisation> implements OrganisationDao {
   /**
    *
    */
   public OrganisationDaoImpl() {
       super(Organisation.class, "organisation");
   }

}
