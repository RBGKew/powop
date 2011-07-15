package org.emonocot.checklist.persistence;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.emonocot.checklist.format.ChecklistIdentifierFormatter;
import org.emonocot.checklist.model.ChangeEvent;
import org.emonocot.checklist.model.ChangeEventImpl;
import org.emonocot.checklist.model.ChangeType;
import org.emonocot.checklist.model.Taxon;
import org.emonocot.model.pager.DefaultPageImpl;
import org.emonocot.model.pager.Page;
import org.hibernate.Criteria;
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

    @Transactional(readOnly = true)
    public final Taxon get(final Long id) {
        // Retrieve taxon with the database id, rather than the
        // "identifier" we gave them and they passed in
        Criteria criteria = getSession()
          .createCriteria(Taxon.class).add(Restrictions.idEq(id));
        
        Taxon taxon = (Taxon) criteria.uniqueResult();
        if(taxon != null) {
        	Hibernate.initialize(taxon.getSynonyms());
        	inferRelatedTaxa(taxon);
        }
        
        return taxon;
    }

    /**
     *
     * @param taxon is the object to try and provide related taxa for
     */
    protected final void inferRelatedTaxa(final Taxon taxon) {
        // Add children
        Criteria criteria = getSession()
                .createCriteria(Taxon.class)
                .add(Restrictions.eq("genus", taxon.getGenus()))
                .add(Restrictions.isNull("acceptedName"));

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
            criteria.add(Restrictions.and(
                    Restrictions.isNotNull("infraspecificEpithet"),
                    Restrictions.ne("infraspecificEpithet", "")))
                    .add(Restrictions.eq("speciesHybridMarker",
                            taxon.getSpeciesHybridMarker()))
                    .add(Restrictions.eq("species", taxon.getSpecies()));
            break;
        default:
            // set up so no records are returned
            criteria.add(Restrictions.idEq(null));
            break;
        }

        taxon.setChildTaxa(new HashSet<Taxon>(criteria.list()));

        // Add Parent
        StringBuffer sb = new StringBuffer();
        /**
         *  genusHybridMarker can be null so we cannot pass it
         *  as an argument to the constructor of StringBuffer
         */
        sb.append(taxon.getGenusHybridMarker());
        if (sb.toString().trim().length()<1) {
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

        if(taxon.getDateDeleted() != null) {
    		return new ChangeEventImpl<Taxon>(taxon,
                    ChangeType.DELETE, taxon.getDateDeleted());
    	} else if(taxon.getDateModified() != null) {
    		return new ChangeEventImpl<Taxon>(taxon,
                    ChangeType.MODIFIED, taxon.getDateModified());
    	} else {
          return new ChangeEventImpl<Taxon>(taxon,
                ChangeType.CREATE, taxon.getDateEntered());
    	}
    }

    @Transactional(readOnly = true)
    public final Page<ChangeEvent<Taxon>> page(final String set,
            final DateTime from, final DateTime until, final Integer pageSize,
            final Integer pageNumber) {
        Criteria criteria = getSession().createCriteria(Taxon.class);

        if(set != null) {
        	criteria.add(Restrictions.eq("family", set));
        }
        
        if(from != null) {
        	criteria.add(Restrictions.disjunction()
        			.add(Restrictions.gt("dateDeleted", from))
        			.add(Restrictions.conjunction()
        			    .add(Restrictions.isNull("dateDeleted"))
        			    .add(Restrictions.gt("dateModified", from)))
        			.add(Restrictions.conjunction()
        					.add(Restrictions.isNull("dateDeleted"))
        					.add(Restrictions.isNull("dateModified"))
        					.add(Restrictions.gt("dateEntered", from))));
        }
        
        if(until != null) {
        	criteria.add(Restrictions.disjunction()
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
        List<Taxon> taxa = criteria.list();
        if (taxa.isEmpty()) {
            return new DefaultPageImpl<ChangeEvent<Taxon>>(0, pageNumber,
                    pageSize, new ArrayList<ChangeEvent<Taxon>>());
        } else {
            List<ChangeEvent<Taxon>> results = new ArrayList<ChangeEvent<Taxon>>();
            for (Taxon taxon : taxa) {
                if(taxon.getDateDeleted() != null) {
            		results.add(new ChangeEventImpl<Taxon>(taxon,
                            ChangeType.DELETE, taxon.getDateDeleted()));
            	} else if(taxon.getDateModified() != null) {
            		results.add(new ChangeEventImpl<Taxon>(taxon,
                            ChangeType.MODIFIED, taxon.getDateModified()));
            	} else {
                  results.add(new ChangeEventImpl<Taxon>(taxon,
                        ChangeType.CREATE, taxon.getDateEntered()));
            	}
            }
            
            Criteria countCriteria = getSession().createCriteria(Taxon.class)
                    .setProjection(Projections.rowCount());
            if(set != null) {
            	countCriteria.add(Restrictions.eq("family", set));
            }
            if(from != null) {
            	countCriteria.add(Restrictions.disjunction()
            			.add(Restrictions.gt("dateDeleted", from))
            			.add(Restrictions.conjunction()
            			    .add(Restrictions.isNull("dateDeleted"))
            			    .add(Restrictions.gt("dateModified", from)))
            			.add(Restrictions.conjunction()
            					.add(Restrictions.isNull("dateDeleted"))
            					.add(Restrictions.isNull("dateModified"))
            					.add(Restrictions.gt("dateEntered", from))));
            }
            if(until != null) {
            	countCriteria.add(Restrictions.disjunction()
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