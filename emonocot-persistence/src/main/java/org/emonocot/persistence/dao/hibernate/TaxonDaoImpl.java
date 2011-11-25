package org.emonocot.persistence.dao.hibernate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.emonocot.api.FacetName;
import org.emonocot.model.hibernate.Fetch;
import org.emonocot.model.pager.Page;
import org.emonocot.model.taxon.Family;
import org.emonocot.model.taxon.Rank;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.model.taxon.TaxonomicStatus;
import org.emonocot.persistence.dao.TaxonDao;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.search.ProjectionConstants;
import org.hibernate.search.query.dsl.FacetContext;
import org.hibernate.search.query.engine.spi.FacetManager;
import org.hibernate.search.query.facet.Facet;
import org.hibernate.search.query.facet.FacetSortOrder;
import org.hibernate.search.query.facet.FacetingRequest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ben
 *
 */
@Repository
public class TaxonDaoImpl extends SearchableDaoImpl<Taxon> implements TaxonDao {

    /**
     *
     */
    private static Map<String, Fetch[]> FETCH_PROFILES;

    static {
        FETCH_PROFILES = new HashMap<String, Fetch[]>();
        FETCH_PROFILES.put("taxon-with-children", new Fetch[] {
                new Fetch("children", FetchMode.SELECT) });
        FETCH_PROFILES.put("taxon-with-related", new Fetch[] {
                new Fetch("parent", FetchMode.JOIN),
                new Fetch("accepted", FetchMode.JOIN),
                new Fetch("children", FetchMode.SELECT),
                new Fetch("synonyms", FetchMode.SELECT),
                new Fetch("annotations", FetchMode.SELECT) });
        FETCH_PROFILES.put("taxon-page", new Fetch[] {
                new Fetch("parent", FetchMode.JOIN),
                new Fetch("accepted", FetchMode.JOIN),
                new Fetch("children", FetchMode.SELECT),
                new Fetch("synonyms", FetchMode.SELECT),
                new Fetch("distribution", FetchMode.SELECT),
                new Fetch("content", FetchMode.SELECT),
                new Fetch("images", FetchMode.SELECT),
                new Fetch("protologue", FetchMode.JOIN),
                new Fetch("ancestors", FetchMode.SELECT) });
        FETCH_PROFILES.put("taxon-with-image", new Fetch[] { new Fetch("image",
                FetchMode.SELECT) });
    }

    /**
     *
     * @return the fields to search by default
     */
    @Override
    protected final String[] getDocumentFields() {
        return new String[] { "title", "name", "authorship", "content.content" };
    }

    /**
     *
     */
    public TaxonDaoImpl() {
        super(Taxon.class, Taxon.class);
    }

    @Override
    protected final void createFacetingRequest(final FacetContext facetContext,
            final FacetName facetName, final FacetManager facetManager) {

        FacetingRequest facetingRequest = null;

        switch (facetName) {
        case CONTINENT:
            facetingRequest = facetContext.name(facetName.name())
                    .onField("continent").discrete()
                    .orderedBy(FacetSortOrder.FIELD_VALUE)
                    .includeZeroCounts(true).createFacetingRequest();
            facetManager.enableFaceting(facetingRequest);
            break;
        case REGION:
            facetingRequest = facetContext.name(facetName.name())
                    .onField("region").discrete()
                    .orderedBy(FacetSortOrder.FIELD_VALUE)
                    .includeZeroCounts(true).createFacetingRequest();
            facetManager.enableFaceting(facetingRequest);
            break;
        case AUTHORITY:
            facetingRequest = facetContext.name(facetName.name())
                    .onField("sources.identifier").discrete()
                    .orderedBy(FacetSortOrder.FIELD_VALUE)
                    .includeZeroCounts(true).createFacetingRequest();
            facetManager.enableFaceting(facetingRequest);
            break;
        case FAMILY:
            facetingRequest = facetContext.name(facetName.name())
                    .onField("family").discrete()
                    .orderedBy(FacetSortOrder.FIELD_VALUE)
                    .includeZeroCounts(true).createFacetingRequest();
            facetManager.enableFaceting(facetingRequest);
            break;
        case RANK:
            facetingRequest = facetContext.name(facetName.name())
                    .onField("rank").discrete()
                    .orderedBy(FacetSortOrder.FIELD_VALUE)
                    .includeZeroCounts(true).createFacetingRequest();
            facetManager.enableFaceting(facetingRequest);
            break;
        case TAXONOMIC_STATUS:
            facetingRequest = facetContext.name(facetName.name())
                    .onField("status").discrete()
                    .orderedBy(FacetSortOrder.FIELD_VALUE)
                    .includeZeroCounts(true).createFacetingRequest();
            facetManager.enableFaceting(facetingRequest);
            break;
        default:
            break;
        }
    }

    /**
     *
     * @param facetName
     *            Set the facet name
     * @param facetManager
     *            Set the facet manager
     * @param selectedFacet
     *            Set the selected facet
     */
    @Override
    protected final void selectFacet(final FacetName facetName,
            final FacetManager facetManager, final String selectedFacet) {
        switch (facetName) {
        case CONTINENT:
        case REGION:
        case AUTHORITY:
        case FAMILY:
        case RANK:
        case TAXONOMIC_STATUS:
            doSelectFacet(facetName, facetManager, selectedFacet);
        default:
            break;
        }
    }

    @Override
    protected final void addFacet(final Page<Taxon> page,
            final FacetName facetName, final FacetManager facetManager) {
        switch (facetName) {
        case CLASS:
            List<Facet> facets = new ArrayList<Facet>();
            page.addFacets(facetName.name(), facets);
            for (Class clazz : SEARCHABLE_CLASSES) {
                if (clazz.equals(type)) {
                    facets.add(new FakeFacet("CLASS",
                            ProjectionConstants.OBJECT_CLASS, clazz.getName(),
                            page.getSize()));
                } else {
                    facets.add(new FakeFacet("CLASS",
                            ProjectionConstants.OBJECT_CLASS, clazz.getName(),
                            0));
                }
            }
            break;
        case CONTINENT:
        case REGION:
        case AUTHORITY:
        case FAMILY:
        case RANK:
        case TAXONOMIC_STATUS:
            page.addFacets(facetName.name(),
                    facetManager.getFacets(facetName.name()));
        default:
            break;
        }
    }

    @Override
    public final String getDefaultField() {
        return "name";
    }

    @Override
    protected final Fetch[] getProfile(final String profile) {
        return TaxonDaoImpl.FETCH_PROFILES.get(profile);
    }

    /**
     * @param t Set the taxon
     * @param fetch Set the fetch profile
     */
    @Override
    public final void enableProfilePostQuery(
                final Taxon t, final String fetch) {
        if (fetch != null && t != null) {
            for (Fetch f : getProfile(fetch)) {
                if (f.getAssociation().equals("ancestors")) {
                    List<Taxon> ancestors = new ArrayList<Taxon>();
                    getAncestors(t, ancestors);
                    t.setAncestors(ancestors);
                } else if (f.getMode().equals(FetchMode.SELECT)) {
                    Object proxy;
                    try {
                        proxy = PropertyUtils
                                .getProperty(t, f.getAssociation());
                    } catch (Exception e) {
                        throw new InvalidDataAccessApiUsageException(
                                "Cannot get proxy " + f.getAssociation()
                                        + " for class " + type, e);
                    }
                    Hibernate.initialize(proxy);
                }
            }
        }
    }

    /**
     *
     * @param t Set the taxon
     * @param ancestors Set the ancestors
     */
    private void getAncestors(final Taxon t, final List<Taxon> ancestors) {
        if (t.getParent() != null) {
            getAncestors(t.getParent(), ancestors);
        }
        ancestors.add(t);

    }

    /**
     * Returns the genera associated with this family. TODO Remove once families
     * are imported
     *
     * @param family
     *            the family
     * @return A list of genera
     */
    public final List<Taxon> getGenera(final Family family) {
        Criteria criteria = getSession().createCriteria(Taxon.class);
        criteria.add(Restrictions.eq("family", family.toString()));
        criteria.add(Restrictions.eq("rank", Rank.GENUS));
        criteria.add(Restrictions.eq("status", TaxonomicStatus.accepted));
        return (List<Taxon>) criteria.list();
    }

    /**
     * Returns the number of genera in a family. TODO Remove once families are
     * imported
     *
     * @param family
     *            the family
     * @return the number of accepted genera
     */
    public final Integer countGenera(final Family family) {
        Criteria criteria = getSession().createCriteria(Taxon.class);
        criteria.add(Restrictions.eq("family", family.toString()));
        criteria.add(Restrictions.eq("rank", Rank.GENUS));
        criteria.add(Restrictions.eq("status", TaxonomicStatus.accepted));
        criteria.setProjection(Projections.rowCount());
        return ((Long) criteria.uniqueResult()).intValue();
    }

    /**
     * Returns the child taxa of a given taxon.
     * @param identifier set the identifier
     *
     * @param pageSize
     *            The maximum number of results to return
     * @param pageNumber
     *            The offset (in pageSize chunks, 0-based) from the beginning of
     *            the recordset
     * @param fetch
     *            Set the fetch profile
     *
     * @return a Page from the resultset
     */
    public final List<Taxon> loadChildren(final String identifier,
            final Integer pageSize, final Integer pageNumber, final String fetch) {
        Criteria criteria = getSession().createCriteria(Taxon.class);
        if (identifier == null) {
            // return the root taxa
            criteria.add(Restrictions.isNull("parent"));
            criteria.add(Restrictions.isNotNull("name"));
            criteria.add(Restrictions.eq("status", TaxonomicStatus.accepted));
        } else {
            criteria.createAlias("parent", "p");
            criteria.add(Restrictions.eq("p.identifier", identifier));
        }

        if (pageSize != null) {
            criteria.setMaxResults(pageSize);
            if (pageNumber != null) {
                criteria.setFirstResult(pageSize * pageNumber);
            }
        }
        criteria.addOrder(Order.asc("name"));
        enableProfilePreQuery(criteria, fetch);
        List<Taxon> results = (List<Taxon>) criteria.list();

        for (Taxon t : results) {
            enableProfilePostQuery(t, fetch);
        }
        return  results;
    }
}
