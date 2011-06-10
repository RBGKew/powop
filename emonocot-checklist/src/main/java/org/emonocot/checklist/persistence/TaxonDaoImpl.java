package org.emonocot.checklist.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.emonocot.checklist.model.ChangeEvent;
import org.emonocot.checklist.model.ChangeEventImpl;
import org.emonocot.checklist.model.ChangeType;
import org.emonocot.checklist.model.Taxon;
import org.emonocot.model.pager.DefaultPageImpl;
import org.emonocot.model.pager.Page;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
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
     *            The session factory to use
     */
    @Autowired
    public final void setHibernateSessionFactory(
            final SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }

    @Override
    @Transactional(readOnly = true)
    public final List<Taxon> search(final String search) {
        return (List<Taxon>) getSession().createCriteria(Taxon.class)
                .add(Restrictions.eq("name", search)).list();
    }

    @Override
    @Transactional(readOnly = true)
    public final Taxon get(final String id) {
        return (Taxon) getSession().load(Taxon.class, id);
    }

    @Override
    @Transactional(readOnly = true)
    public final ChangeEvent<Taxon> find(final Serializable identifier) {
        Taxon taxon = get((String) identifier);
        // Taxon cannot be null because we would have thrown an exception
        // prior to this point

        // Assume everything is created at this point as I don't have the
        // mappings for the other columns
        // TODO add updated / deleted items

        return new ChangeEventImpl<Taxon>(taxon, ChangeType.CREATE,
                new DateTime());
    }

    @Override
    @Transactional(readOnly = true)
    public final Page<ChangeEvent<Taxon>> page(final String set,
            final DateTime from, final DateTime until, final Integer pageSize,
            final Integer pageNumber) {
        Criteria criteria = getSession().createCriteria(Taxon.class);
        // TODO add filters for set, from and until
        if (pageSize != null) {
            criteria.setMaxResults(pageSize);
            if (pageNumber != null) {
                criteria.setFirstResult(pageSize * pageNumber);
            }
        }
        List<Taxon> taxa = criteria.list();
        if (taxa.isEmpty()) {
            return new DefaultPageImpl<ChangeEvent<Taxon>>(0, pageNumber,
                    pageSize, new ArrayList<ChangeEvent<Taxon>>());
        } else {
            List<ChangeEvent<Taxon>> results
                = new ArrayList<ChangeEvent<Taxon>>();
            for (Taxon taxon : taxa) {
                // Assume everything is created at this point as I don't have
                // the mappings for the other columns
                // TODO add updated / deleted items
                results.add(new ChangeEventImpl<Taxon>(taxon,
                        ChangeType.CREATE, new DateTime()));
            }
            // TODO add filters for set, from and until
            Criteria countCriteria = getSession().createCriteria(Taxon.class)
                    .setProjection(Projections.rowCount());
            return new DefaultPageImpl<ChangeEvent<Taxon>>(
                    (Integer) countCriteria.uniqueResult(), pageNumber,
                    pageSize, results);
        }
    }
}
