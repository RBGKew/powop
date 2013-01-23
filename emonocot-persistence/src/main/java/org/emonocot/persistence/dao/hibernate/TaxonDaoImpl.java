package org.emonocot.persistence.dao.hibernate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.emonocot.model.Taxon;
import org.emonocot.model.hibernate.Fetch;
import org.emonocot.pager.DefaultPageImpl;
import org.emonocot.pager.Page;
import org.emonocot.persistence.dao.TaxonDao;
import org.gbif.ecat.voc.Rank;
import org.gbif.ecat.voc.TaxonomicStatus;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.rc.retroweaver.runtime.Arrays;

/**
 * @author ben
 */
@Repository
public class TaxonDaoImpl extends DaoImpl<Taxon> implements TaxonDao {

    /**
     *
     */
    private static Map<String, Fetch[]> FETCH_PROFILES;

    static {
        FETCH_PROFILES = new HashMap<String, Fetch[]>();
        FETCH_PROFILES.put("taxon-with-children", new Fetch[] {new Fetch(
                "childNameUsages", FetchMode.SELECT)});
        FETCH_PROFILES.put("classification-tree", new Fetch[] {
        		new Fetch("childNameUsages", FetchMode.SELECT),
        		new Fetch("keys", FetchMode.SELECT)});
        FETCH_PROFILES.put("taxon-with-ancestors", new Fetch[] { new Fetch(
                "higherClassification", FetchMode.SELECT) });
        FETCH_PROFILES.put("taxon-with-annotations", new Fetch[] {new Fetch(
                "annotations", FetchMode.SELECT)});
        FETCH_PROFILES.put("taxon-with-related", new Fetch[] {
                new Fetch("parentNameUsage", FetchMode.JOIN),
                new Fetch("acceptedNameUsage", FetchMode.JOIN),
                new Fetch("childNameUsages", FetchMode.SELECT),
                new Fetch("synonymNameUsages", FetchMode.SELECT),
                new Fetch("annotations", FetchMode.SELECT)});
        FETCH_PROFILES.put("taxon-page", new Fetch[] {
                new Fetch("parentNameUsage", FetchMode.JOIN),
                new Fetch("acceptedNameUsage", FetchMode.JOIN),
                new Fetch("childNameUsages", FetchMode.SELECT),
                new Fetch("synonymNameUsages", FetchMode.SELECT),
                new Fetch("distribution", FetchMode.SELECT),
                new Fetch("descriptions.references", FetchMode.SELECT),
                new Fetch("descriptions", FetchMode.SELECT),
                new Fetch("descriptions.references", FetchMode.SELECT),
                new Fetch("images", FetchMode.SELECT),
                new Fetch("namePublishedIn", FetchMode.JOIN),
                new Fetch("references", FetchMode.SELECT),
                new Fetch("higherClassification", FetchMode.SELECT),
                new Fetch("authority", FetchMode.JOIN),
                new Fetch("sources", FetchMode.SELECT),
                new Fetch("identifiers", FetchMode.SELECT),
                new Fetch("vernacularNames", FetchMode.SELECT),
                new Fetch("measurementsOrFacts", FetchMode.SELECT),
                new Fetch("typesAndSpecimens", FetchMode.SELECT)});
        /* */
        FETCH_PROFILES.put("object-page", FETCH_PROFILES.get("taxon-page"));
        /* */
        FETCH_PROFILES.put("taxon-with-image", new Fetch[] {new Fetch("image",
                FetchMode.SELECT)});
        FETCH_PROFILES.put("taxon-with-content", new Fetch[] {
        		new Fetch("descriptions", FetchMode.SELECT),
        		new Fetch("sources", FetchMode.SELECT)});
        FETCH_PROFILES.put("taxon-ws", new Fetch[] {
                new Fetch("parentNameUsage", FetchMode.JOIN),
                new Fetch("acceptedNameUsage", FetchMode.JOIN),
                new Fetch("childNameUsages", FetchMode.SELECT),
                new Fetch("synonymNameUsages", FetchMode.SELECT),
                /**
                 *  ISSUE http://build.e-monocot.org/bugzilla/show_bug.cgi?id=180
                 *
                new Fetch("distribution", FetchMode.SELECT),*/
                new Fetch("namePublishedIn", FetchMode.JOIN)});
    }

    /**
     * The rank held by the the root(s) of the taxonomic classification.
     */
    private Rank rootRank;

    /**
     * @param rank
     *            Set the root rank
     */
    public final void setRootRank(final String rank) {
        this.rootRank = Rank.valueOf(rank);
    }

    /**
     *
     */
    public TaxonDaoImpl() {
        super(Taxon.class);
    }

    @Override
    protected final Fetch[] getProfile(final String profile) {
        return TaxonDaoImpl.FETCH_PROFILES.get(profile);
    }

    /**
     * @param t
     *            Set the taxon
     * @param fetch
     *            Set the fetch profile
     */
    @Override
    public final void enableProfilePostQuery(final Taxon t, final String fetch) {
        if (fetch != null && t != null) {
            for (Fetch f : getProfile(fetch)) {
                if (f.getAssociation().equals("higherClassification")) {
                    List<Taxon> ancestors = new ArrayList<Taxon>();
                    getAncestors(t, ancestors);
                    t.setHigherClassification(ancestors);
                } else if (f.getMode().equals(FetchMode.SELECT)) {
                    String association = f.getAssociation();
                    if (association.indexOf(".") == -1) {
                        initializeProperty(t, f.getAssociation());
                    } else {
                        List<String> associations = Arrays.asList(association
                                .split("\\."));
                        initializeProperties(t, associations);
                    }
                }
            }
        }
    }

    /**
     * @param t
     *            Set the taxon
     * @param ancestors
     *            Set the ancestors
     */
    private void getAncestors(final Taxon t, final List<Taxon> ancestors) {
        if (t.getParentNameUsage() != null) {
            getAncestors(t.getParentNameUsage(), ancestors);
        }
        ancestors.add(t);

    }

    /**
     * Returns the child taxa of a given taxon.
     *
     * @param identifier
     *            set the identifier
     * @param pageSize
     *            The maximum number of results to return
     * @param pageNumber
     *            The offset (in pageSize chunks, 0-based) from the beginning of
     *            the recordset
     * @param fetch
     *            Set the fetch profile
     * @return a Page from the resultset
     */
    public final List<Taxon> loadChildren(final String identifier,
            final Integer pageSize, final Integer pageNumber, final String fetch) {
        Criteria criteria = getSession().createCriteria(Taxon.class);
        if (identifier == null) {
            // return the root taxa
            criteria.add(Restrictions.isNull("parentNameUsage"));
            criteria.add(Restrictions.isNotNull("scientificName"));
            criteria.add(Restrictions.eq("taxonomicStatus", TaxonomicStatus.Accepted));
            if (rootRank != null) {
                criteria.add(Restrictions.eq("taxonRank", rootRank));
            }
        } else {
            criteria.createAlias("parentNameUsage", "p");
            criteria.add(Restrictions.eq("p.identifier", identifier));
        }

        if (pageSize != null) {
            criteria.setMaxResults(pageSize);
            if (pageNumber != null) {
                criteria.setFirstResult(pageSize * pageNumber);
            }
        }
        criteria.addOrder(Order.asc("scientificName"));
        enableProfilePreQuery(criteria, fetch);
        List<Taxon> results = (List<Taxon>) criteria.list();

        for (Taxon t : results) {
            enableProfilePostQuery(t, fetch);
        }
        return results;
    }
    
    /* (non-Javadoc)
	 * @see org.emonocot.persistence.dao.SearchableDao#searchByExample(org.emonocot.model.Base, boolean, boolean)
	 */
	@Override
	public Page<Taxon> searchByExample(Taxon example, boolean ignoreCase,
			boolean useLike) {
		Example criterion = Example.create(example);
        if(ignoreCase) {
            criterion.ignoreCase();
        }
        if(useLike) {
            criterion.enableLike();
        }
        Criteria criteria = getSession().createCriteria(Taxon.class);
        criteria.add(criterion);
        List<Taxon> results = (List<Taxon>) criteria.list();
        Page<Taxon> page = new DefaultPageImpl<Taxon>(results.size(), null, null, results, null);
        return page;
	}
}
