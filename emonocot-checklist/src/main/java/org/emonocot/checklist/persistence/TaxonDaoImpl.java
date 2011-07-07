package org.emonocot.checklist.persistence;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.emonocot.checklist.format.ChecklistIdentifierFormatter;
import org.emonocot.checklist.model.ChangeEvent;
import org.emonocot.checklist.model.ChangeEventImpl;
import org.emonocot.checklist.model.ChangeType;
import org.emonocot.checklist.model.Rank;
import org.emonocot.checklist.model.Taxon;
import org.emonocot.model.pager.DefaultPageImpl;
import org.emonocot.model.pager.Page;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
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
     */
    private ChecklistIdentifierFormatter identifierFormatter = new ChecklistIdentifierFormatter();

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
        List<Taxon> results = getSession().createCriteria(Taxon.class)
                .add(Restrictions.eq("name", search)).list();
        for (Taxon taxon : results) {
            inferRelatedTaxa(taxon);
        }

        return results;
    }

    @Override
    @Transactional(readOnly = true)
    public final Taxon get(final Long id) {
        // Retrieve taxon TODO with the database id, rather than the
        // "identifier" we gave them and they passed in
        Taxon taxon = (Taxon) getSession().load(Taxon.class, id);

        inferRelatedTaxa(taxon);
        return taxon;
    }

    /**
     * 
     * @param taxon is the object to try and provide related taxa for
     */
    protected final void inferRelatedTaxa(final Taxon taxon) {
        // Add children
        Criteria c = getSession()
                .createCriteria(Taxon.class)
                .add(Restrictions.eq("genusHybridMarker", taxon.getGenusHybridMarker()))
                .add(Restrictions.eq("genus", taxon.getGenus()));
        switch (taxon.getRank()) {
        case GENUS:
            c.add(Restrictions.isEmpty("infraspecificEpithet"));
            break;
        case SPECIES:
            c.add(Restrictions.isNotEmpty("infraspecificEpithet"))
                    .add(Restrictions.eq("speciesHybridMarker", taxon.getSpeciesHybridMarker()))
                    .add(Restrictions.eq("species", taxon.getSpecies()));
            break;
        default:
            // set up so no records are returned
            c.add(Restrictions.idEq(null));
            break;
        }

        taxon.setChildTaxa(new HashSet<Taxon>(c.list()));

        // Add Parent
        StringBuffer sb = new StringBuffer(taxon.getGenusHybridMarker());
        if (sb.toString().trim().isEmpty()) {
            sb.append(" ");
        }
        sb.append(taxon.getGenus());
        List<Taxon> results = null;
        switch (taxon.getRank()) {
        case SPECIES:
            break;
        case SUBSPECIES:
            // TODO: case other infrasp. ranks:
            if (taxon.getSpeciesHybridMarker() != null) {
                sb.append(taxon.getSpeciesHybridMarker());
            }
            sb.append(taxon.getSpecies());
            break;
        default:
            // we can't add anything for genus (no family records in checklist)
            // or unknown ranks
            sb = new StringBuffer("");
            return;
        }

        results = search(sb.toString());
        if (results.size() > 0) {
            for (Iterator<Taxon> iterator = results.iterator(); iterator.hasNext();) {
                Taxon t = iterator.next();
                if (taxon.getFamily().equals(t.getFamily())) {
                    taxon.setParentTaxon(results.remove(0));
                }
            }
        } else {
            // TODO log that we can't infer a parent
            taxon.setParentTaxon(null);
        }
        return;
    }

    @Override
    @Transactional(readOnly = true)
    public final ChangeEvent<Taxon> find(final Serializable identifier) {
        Taxon taxon = null;
        try {
            taxon = get(identifierFormatter.parse((String) identifier, null));
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
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
            List<ChangeEvent<Taxon>> results = new ArrayList<ChangeEvent<Taxon>>();
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
                    ((Long) countCriteria.uniqueResult()).intValue(),
                    pageNumber, pageSize, results);
        }
    }
}