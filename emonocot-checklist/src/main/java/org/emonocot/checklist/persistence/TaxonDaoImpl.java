package org.emonocot.checklist.persistence;

import java.util.List;

import org.emonocot.checklist.model.Taxon;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ben
 *
 */
@Repository
public class TaxonDaoImpl extends HibernateDaoSupport implements TaxonDao {

    /**
     *
     * @param sessionFactory
     */
    @Autowired
    public final void setHibernateSessionFactory(
            final SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }

    @Override
    @Transactional(readOnly = true)
    public final List<Taxon> search(final String search) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public final Taxon get(final String id) {
        // TODO Auto-generated method stub
        return null;
    }

}
