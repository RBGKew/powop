package org.emonocot.checklist.persistence;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.emonocot.checklist.format.ChecklistIdentifierFormatter;
import org.emonocot.checklist.model.ChangeEvent;
import org.emonocot.checklist.model.ChangeEventImpl;
import org.emonocot.checklist.model.ChangeType;
import org.emonocot.checklist.model.Taxon;
import org.emonocot.model.pager.DefaultPageImpl;
import org.emonocot.model.pager.Page;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
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
     */
    private ChecklistIdentifierFormatter identifierFormatter
        = new ChecklistIdentifierFormatter();

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

    /**
     * @param search the full name of the taxon excluding authors to search with
     * @return a taxon
     */
    @Transactional(readOnly = true)
    public final List<Taxon> search(final String search) {
        List<Taxon> results = getSession().createCriteria(Taxon.class)
                .add(Restrictions.eq("name", search)).list();
        for (Taxon taxon : results) {
            inferRelatedTaxa(taxon);
            Hibernate.initialize(taxon.getSynonyms());
        }

        return results;
    }

    /**
     * @param id the identifier of the taxon to get
     * @return a taxon
     */
    @Transactional(readOnly = true)
    public final Taxon get(final Integer id) {
        // Retrieve taxon with the database id, rather than the
        // "identifier" we gave them and they passed in
        Criteria criteria = getSession().createCriteria(Taxon.class).add(
                Restrictions.idEq(id));
        criteria.setFetchMode("acceptedName", FetchMode.JOIN);
        criteria.setFetchMode("protologue", FetchMode.JOIN);

        Taxon taxon = (Taxon) criteria.uniqueResult();
        if (taxon != null) {
            Hibernate.initialize(taxon.getSynonyms());
            Hibernate.initialize(taxon.getDistribution());
            Hibernate.initialize(taxon.getAuthors());
            Hibernate.initialize(taxon.getCitations());
            inferRelatedTaxa(taxon);
        }

        return taxon;
    }

    /**
     *
     * @param taxon
     *            is the object to try and provide related taxa for
     */
    protected final void inferRelatedTaxa(final Taxon taxon) {

        Criteria criteria = getSession().createCriteria(Taxon.class)
                .add(Restrictions.eq("genus", taxon.getGenus()))
                .add(Restrictions.eqProperty("acceptedName.id", "id"))
                .add(Restrictions.eq("family", taxon.getFamily()));

        if (taxon.getGenusHybridMarker() == null) {
            criteria.add(Restrictions.isNull("genusHybridMarker"));
        } else {
            criteria.add(Restrictions.eq("genusHybridMarker",
                    taxon.getGenusHybridMarker()));
        }
        switch (taxon.getRank()) {
        case GENUS:
            criteria.add(Restrictions.and(Restrictions.isNotNull("species"),
                    Restrictions.ne("species", "")));
            criteria.add(Restrictions.or(
                    Restrictions.isNull("infraspecificEpithet"),
                    Restrictions.eq("infraspecificEpithet", "")));
            break;
        case SPECIES:
            criteria.add(
                    Restrictions.and(
                            Restrictions.isNotNull("infraspecificEpithet"),
                            Restrictions.ne("infraspecificEpithet", "")))
                    .add(Restrictions.eq("species", taxon.getSpecies()));
                if (taxon.getSpeciesHybridMarker() == null) {
                    criteria.add(Restrictions.isNull("speciesHybridMarker"));
                } else {
                    criteria.add(Restrictions.eq("speciesHybridMarker",
                            taxon.getSpeciesHybridMarker()));
                }
            break;
        default:
            // set up so no records are returned
            criteria.add(Restrictions.idEq(null));
            break;
        }

        taxon.setChildTaxa(new HashSet<Taxon>(criteria.list()));

        // Add Parent
        Criteria parentCriteria = getSession().createCriteria(Taxon.class)
        .add(Restrictions.eq("genus", taxon.getGenus()))
        .add(Restrictions.eqProperty("acceptedName.id", "id"))
        .add(Restrictions.eq("family", taxon.getFamily()));

        if (taxon.getGenusHybridMarker() == null) {
            parentCriteria.add(Restrictions.isNull("genusHybridMarker"));
        } else {
            parentCriteria.add(Restrictions.eq("genusHybridMarker",
                    taxon.getGenusHybridMarker()));
        }

        switch (taxon.getRank()) {
        case SPECIES:
            parentCriteria.add(Restrictions.or(Restrictions.isNull("species"),
                    Restrictions.eq("species", "")));
            break;
        case SUBSPECIES:
            // TODO: case other infrasp. ranks:
            parentCriteria.add(
                    Restrictions.or(
                            Restrictions.isNull("infraspecificEpithet"),
                            Restrictions.eq("infraspecificEpithet", "")))
                    .add(Restrictions.eq("species", taxon.getSpecies()));
                if (taxon.getSpeciesHybridMarker() != null) {
                   parentCriteria.add(Restrictions.eq("speciesHybridMarker",
                           taxon.getSpeciesHybridMarker()));
                } else {
                    parentCriteria.add(
                            Restrictions.isNull("speciesHybridMarker"));
                }
            break;
        case GENUS:
        default:
            return;
        }

        List<Taxon> results = (List<Taxon>) parentCriteria.list();
        if (results.size() == 1) {
            taxon.setParentTaxon(results.get(0));
        } else {
            // TODO log that we can't infer a parent
            taxon.setParentTaxon(null);
        }
        return;
    }

    /**
     * @param identifier
     *            Set the identifier of the taxon to find
     * @return a taxon
     */
    @Transactional(readOnly = true)
    public final ChangeEvent<Taxon> find(final Serializable identifier) {
        Taxon taxon = null;
        try {
            taxon = get(
                identifierFormatter.parse((String) identifier, null)
                .intValue());
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
        /**
         * If the taxon does not exist return null
         */
        if (taxon == null) {
            return null;
        }

        if (taxon.getDateDeleted() != null) {
            return new ChangeEventImpl<Taxon>(taxon, ChangeType.DELETE,
                    taxon.getDateDeleted());
        } else if (taxon.getDateModified() != null) {
            return new ChangeEventImpl<Taxon>(taxon, ChangeType.MODIFIED,
                    taxon.getDateModified());
        } else {
            return new ChangeEventImpl<Taxon>(taxon, ChangeType.CREATE,
                    taxon.getDateEntered());
        }
    }

    /**
     *
     * @param set
     *            The family to harvest from or null to harvest from all
     *            families
     * @param from
     *            the date time to harvest from or null to harvest from the
     *            beginning of the database
     * @param until
     *            the date time to harvest until or null to harvest to the end
     *            of the database
     * @param pageSize
     *            the maximum number of change events to return
     * @param pageNumber
     *            the offset (in pageSize pages) from the beginning of the
     *            result set (0-based!)
     * @return a page of change events of type taxon
     */
    @Transactional(readOnly = true)
    public final Page<ChangeEvent<Taxon>> page(final String set,
            final DateTime from, final DateTime until, final Integer pageSize,
            final Integer pageNumber) {
        Criteria criteria = getSession().createCriteria(Taxon.class);
        criteria.setFetchMode("acceptedName", FetchMode.JOIN);
        criteria.setFetchMode("protologue", FetchMode.JOIN);

        if (set != null) {
            criteria.add(Restrictions.eq("family", set));
        }

        if (from != null) {
            criteria.add(Restrictions
                    .disjunction()
                    .add(Restrictions.gt("dateDeleted", from))
                    .add(Restrictions.conjunction()
                            .add(Restrictions.isNull("dateDeleted"))
                            .add(Restrictions.gt("dateModified", from)))
                    .add(Restrictions.conjunction()
                            .add(Restrictions.isNull("dateDeleted"))
                            .add(Restrictions.isNull("dateModified"))
                            .add(Restrictions.gt("dateEntered", from))));
        }

        if (until != null) {
            criteria.add(Restrictions
                    .disjunction()
                    .add(Restrictions.lt("dateDeleted", until))
                    .add(Restrictions.conjunction()
                            .add(Restrictions.isNull("dateDeleted"))
                            .add(Restrictions.lt("dateModified", until)))
                    .add(Restrictions.conjunction()
                            .add(Restrictions.isNull("dateDeleted"))
                            .add(Restrictions.isNull("dateModified"))
                            .add(Restrictions.lt("dateEntered", until))));
        }

        if (pageSize != null) {
            criteria.setMaxResults(pageSize);
            if (pageNumber != null) {
                criteria.setFirstResult(pageSize * pageNumber);
            }
        }
        List<Taxon> taxa = (List<Taxon>) criteria.list();
        if (taxa.isEmpty()) {
            return new DefaultPageImpl<ChangeEvent<Taxon>>(0, pageNumber,
                    pageSize, new ArrayList<ChangeEvent<Taxon>>());
        } else {
            List<ChangeEvent<Taxon>> results
                = new ArrayList<ChangeEvent<Taxon>>();
            for (Taxon taxon : taxa) {
                Hibernate.initialize(taxon.getSynonyms());
                Hibernate.initialize(taxon.getDistribution());
                Hibernate.initialize(taxon.getAuthors());
                Hibernate.initialize(taxon.getCitations());
                inferRelatedTaxa(taxon);

                if (taxon.getDateDeleted() != null) {
                    results.add(new ChangeEventImpl<Taxon>(taxon,
                            ChangeType.DELETE, taxon.getDateDeleted()));
                } else if (taxon.getDateModified() != null) {
                    results.add(new ChangeEventImpl<Taxon>(taxon,
                            ChangeType.MODIFIED, taxon.getDateModified()));
                } else {
                    results.add(new ChangeEventImpl<Taxon>(taxon,
                            ChangeType.CREATE, taxon.getDateEntered()));
                }
            }

            Criteria countCriteria = getSession().createCriteria(Taxon.class)
                    .setProjection(Projections.rowCount());
            if (set != null) {
                countCriteria.add(Restrictions.eq("family", set));
            }
            if (from != null) {
                countCriteria.add(Restrictions
                        .disjunction()
                        .add(Restrictions.gt("dateDeleted", from))
                        .add(Restrictions.conjunction()
                                .add(Restrictions.isNull("dateDeleted"))
                                .add(Restrictions.gt("dateModified", from)))
                        .add(Restrictions.conjunction()
                                .add(Restrictions.isNull("dateDeleted"))
                                .add(Restrictions.isNull("dateModified"))
                                .add(Restrictions.gt("dateEntered", from))));
            }
            if (until != null) {
                countCriteria.add(Restrictions
                        .disjunction()
                        .add(Restrictions.lt("dateDeleted", until))
                        .add(Restrictions.conjunction()
                                .add(Restrictions.isNull("dateDeleted"))
                                .add(Restrictions.lt("dateModified", until)))
                        .add(Restrictions.conjunction()
                                .add(Restrictions.isNull("dateDeleted"))
                                .add(Restrictions.isNull("dateModified"))
                                .add(Restrictions.lt("dateEntered", until))));
            }
            return new DefaultPageImpl<ChangeEvent<Taxon>>(
                    ((Long) countCriteria.uniqueResult()).intValue(),
                    pageNumber, pageSize, results);
        }
    }
}
