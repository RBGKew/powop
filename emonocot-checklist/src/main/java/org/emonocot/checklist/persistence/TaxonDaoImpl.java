package org.emonocot.checklist.persistence;

import java.io.Serializable;
import java.util.List;

import org.emonocot.checklist.model.ChangeEvent;
import org.emonocot.checklist.model.Taxon;
import org.emonocot.checklist.persistence.pager.Page;
import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.openarchives.pmh.SetSpec;
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
     * @param sessionFactory The session factory to use
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

    @Override
    @Transactional(readOnly = true)
    public final ChangeEvent<Taxon> find(final Serializable identifier) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public final Page<ChangeEvent<Taxon>> page(final SetSpec set,
            final DateTime from, final DateTime until, final Integer pageSize,
            final Integer pageNumber) {
        // TODO Auto-generated method stub
        return null;
    }

}
