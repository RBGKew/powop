package org.emonocot.persistence.dao.hibernate;

import java.util.List;
import java.util.Map;

import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.util.Version;
import org.emonocot.model.common.Base;
import org.emonocot.model.hibernate.Fetch;
import org.emonocot.model.pager.DefaultPageImpl;
import org.emonocot.model.pager.Page;
import org.emonocot.persistence.QuerySyntaxException;
import org.emonocot.persistence.dao.Dao;
import org.emonocot.persistence.dao.FacetName;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.UnresolvableObjectException;
import org.hibernate.criterion.Restrictions;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.SearchFactory;
import org.hibernate.search.query.dsl.FacetContext;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.engine.spi.FacetManager;
import org.hibernate.search.query.facet.Facet;
import org.hibernate.search.query.facet.FacetingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateObjectRetrievalFailureException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 *
 * @author ben
 *
 * @param <T>
 *            the type of object managed by this dao
 */
public abstract class DaoImpl<T extends Base> extends HibernateDaoSupport
        implements Dao<T> {

    /**
     *
     * @param profile Set the name of the profile
     * @return a list of Fetch instances
     */
    protected abstract Fetch[] getProfile(String profile);

    /**
     *
     * @param criteria Set a Criteria instance
     * @param fetch Set the name of the fetch profile
     */
    protected final void enableProfile(final Criteria criteria,
            final String fetch) {
        if (fetch != null) {
            for (Fetch f : getProfile(fetch)) {
                criteria.setFetchMode(f.getAssociation(), f.getMode());
            }
        }
    }

    /**
     *
     */
    protected Class<T> type;

    /**
     *
     * @param newType Set the type of object handled by this DAO
     */
    public DaoImpl(final Class<T> newType) {
        this.type = newType;
    }

    /**
     *
     * @param sessionFactory Set the session factory
     */
    @Autowired
    public final void setHibernateSessionFactory(
            final SessionFactory sessionFactory) {
        this.setSessionFactory(sessionFactory);
    }

    public final T load(final String identifier) {
        return load(identifier, null);
    }

    public final T find(final String identifier) {
        return find(identifier, null);
    }

    public final T load(final String identifier, final String fetch) {

        Criteria criteria = getSession().createCriteria(type)
        .add(Restrictions.eq("identifier", identifier));
        enableProfile(criteria, fetch);
        T t = (T) criteria.uniqueResult();

        if (t == null) {
            throw new HibernateObjectRetrievalFailureException(
                    new UnresolvableObjectException(identifier,
                            "Object could not be resolved"));
        }
        return t;
    }

    public final T find(final String identifier, final String fetch) {
        Criteria criteria = getSession().createCriteria(type)
        .add(Restrictions.eq("identifier", identifier));
        enableProfile(criteria, fetch);
        T t = (T) criteria.uniqueResult();

        return t;
    }

   /**
    *
    * @param t The object to save.
    * @return the id of the object
    */
   public final Long save(final T t) {
       return (Long) getSession().save(t);
   }

  /**
   *
   * @param t The object to save.
   */
  public final void saveOrUpdate(final T t) {
      getSession().saveOrUpdate(t);
  }

  /**
   *
   * @param facetContext the facet context
   * @param facetName the facet name
   * @return The faceting request
   */
  protected abstract FacetingRequest createFacetingRequest(
          final FacetContext facetContext, final FacetName facetName);

  /**
   *
   * @return the fields to search by default
   */
  protected abstract String[] getDocumentFields();

    public final Page<T> search(final String query,
            final String spatialQuery,
            final Integer pageSize, final Integer pageNumber,
            final FacetName[] facets,
            final Map<FacetName, Integer> selectedFacets) {
        FullTextSession fullTextSession
         = Search.getFullTextSession(getSession());
        SearchFactory searchFactory = fullTextSession.getSearchFactory();

        try {
            org.apache.lucene.search.Query luceneQuery = null;
            QueryParser parser
            = new MultiFieldQueryParser(Version.LUCENE_31, getDocumentFields(),
                    searchFactory.getAnalyzer(getAnalyzerType()));
            if (query != null && !query.equals("")) {
               luceneQuery = parser.parse(query);
            } else {
                QueryBuilder queryBuilder
                    = searchFactory.buildQueryBuilder().forEntity(type).get();
                luceneQuery = queryBuilder.all().createQuery();
            }
            FullTextQuery fullTextQuery
                = fullTextSession.createFullTextQuery(luceneQuery, type);
            if (spatialQuery != null && spatialQuery.trim().length() != 0) {
                // TODO Implement spatial filter
            }

            if (pageSize != null) {
                fullTextQuery.setMaxResults(pageSize);
                if (pageNumber != null) {
                    fullTextQuery.setFirstResult(pageSize * pageNumber);
                }
            }
            if (facets != null && facets.length != 0) {
                FacetManager facetManager = fullTextQuery.getFacetManager();
              QueryBuilder queryBuilder = fullTextSession.getSearchFactory()
              .buildQueryBuilder().forEntity(getAnalyzerType()).get();
              for (FacetName facetName : facets) {
                FacetingRequest facetingRequest
                  = createFacetingRequest(queryBuilder.facet(),
                        facetName);
                facetManager.enableFaceting(facetingRequest);
              }
            }

            List<T> results = (List<T>) fullTextQuery.list();

            if (selectedFacets != null && !selectedFacets.isEmpty()) {
                FacetManager facetManager = fullTextQuery.getFacetManager();

                for (FacetName facetName : selectedFacets.keySet()) {
                    List<Facet> facetResults =
                        facetManager.getFacets(facetName.name());
                    facetManager.getFacetGroup(
                            facetName.name())
                            .selectFacets(
                                    facetResults.get(
                                            selectedFacets.get(facetName)));
                }
                results = (List<T>) fullTextQuery.list();
            }

            Page<T> page = new DefaultPageImpl<T>(
                    fullTextQuery.getResultSize(),
                    pageNumber, pageSize, results);
            if (facets != null && facets.length != 0) {
                FacetManager facetManager = fullTextQuery.getFacetManager();
                for (FacetName facetName : facets) {
                    page.addFacets(facetName.name(),
                        facetManager.getFacets(facetName.name()));
                }
            }
            if (selectedFacets != null && !selectedFacets.isEmpty()) {
                for (FacetName facetName : selectedFacets.keySet()) {
                    page.setSelectedFacet(facetName.name(),
                            selectedFacets.get(facetName));
                }
            }

            return page;
        } catch (ParseException e) {
            throw new QuerySyntaxException("Exception parsing " + query, e);
        }
    }

    /**
     * Given https://hibernate.onjira.com/browse/HSEARCH-703, we need this
     * workaround, currently.
     *
     * This should be removed once the issue is resolved by the hibernate
     * search team
     * @return a class to retrieve an analyzer for.
     */
    protected Class getAnalyzerType() {
        return type;
    }
}
