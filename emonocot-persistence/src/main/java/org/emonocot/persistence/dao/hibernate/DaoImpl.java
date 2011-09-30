package org.emonocot.persistence.dao.hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.util.Version;
import org.emonocot.model.common.Base;
import org.emonocot.model.comms.Sorting;
import org.emonocot.model.comms.Sorting.SortDirection;
import org.emonocot.model.hibernate.Fetch;
import org.emonocot.model.media.Image;
import org.emonocot.model.pager.DefaultPageImpl;
import org.emonocot.model.pager.Page;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.persistence.QuerySyntaxException;
import org.emonocot.persistence.dao.Dao;
import org.emonocot.service.FacetName;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.UnresolvableObjectException;
import org.hibernate.criterion.Restrictions;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.ProjectionConstants;
import org.hibernate.search.Search;
import org.hibernate.search.SearchFactory;
import org.hibernate.search.query.dsl.FacetContext;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.engine.spi.FacetManager;
import org.hibernate.search.query.facet.Facet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
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
    protected void enableProfilePreQuery(final Criteria criteria,
            final String fetch) {
        if (fetch != null) {
            for (Fetch f : getProfile(fetch)) {
                if (f.getMode().equals(FetchMode.JOIN)) {
                    criteria.setFetchMode(f.getAssociation(), f.getMode());
                }
            }
        }
    }

    /**
    *
    * @param t Set a the fetched object
    * @param fetch Set the name of the fetch profile
    */
   protected final void enableProfilePostQuery(final T t,
           final String fetch) {
       if (fetch != null && t != null) {
           for (Fetch f : getProfile(fetch)) {
               if (f.getMode().equals(FetchMode.SELECT)) {
                   Object proxy;
                    try {
                        proxy = PropertyUtils.getProperty(t, f.getAssociation());
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
     */
    protected Class<T> type;

    /**
     *
     */
    protected static Class SEARCHABLE_CLASSES[] = new Class[] { Image.class,
            Taxon.class };

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

    /**
     * @param identifier Set the identifier
     */
    public final void delete(final String identifier) {
        T t = load(identifier);
        getSession().delete(t);
    }

    /**
     * @param identifier set the identifier
     * @return the loaded object
     */
    public final T load(final String identifier) {
        return load(identifier, null);
    }

    /**
     * @param identifier Set the identifier
     * @return the object, or null if the object cannot be found
     */
    public final T find(final String identifier) {
        return find(identifier, null);
    }

    /**
     * @param identifier Set the identifier
     * @param fetch Set the fetch profile (can be null)
     * @return the loaded object
     */
    public final T load(final String identifier, final String fetch) {

        Criteria criteria = getSession().createCriteria(type).add(
                Restrictions.eq("identifier", identifier));

        enableProfilePreQuery(criteria, fetch);

        T t = (T) criteria.uniqueResult();

        if (t == null) {
            throw new HibernateObjectRetrievalFailureException(
                    new UnresolvableObjectException(identifier,
                            "Object could not be resolved"));
        }
        enableProfilePostQuery(t, fetch);
        return t;
    }

    /**
     * @param identifier Set the identifer
     * @param fetch Set the fetch profile
     * @return the object or null if it cannot be found
     */
    public final T find(final String identifier, final String fetch) {
        Criteria criteria = getSession().createCriteria(type)
        .add(Restrictions.eq("identifier", identifier));
        enableProfilePreQuery(criteria, fetch);
        T t = (T) criteria.uniqueResult();
        enableProfilePostQuery(t, fetch);

        return t;
    }

   /**
    *
    * @param t The object to save.
    * @return the saved object
    */
   public final T save(final T t) {
       getSession().save(t);
       return t;
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
   * @param facetManager set the facet manager
   */
  protected abstract void createFacetingRequest(
            final FacetContext facetContext, final FacetName facetName,
            FacetManager facetManager);

    /**
     *
     * @param facetName Set the facet name
     * @param facetManager Set the facet manager
     * @param selectedFacetIndex Set the selected facet
     */
    protected void selectFacet(final FacetName facetName,
            final FacetManager facetManager,
            final Integer selectedFacetIndex) {
        switch (facetName) {
        case CLASS:
            break;
        default:
            List<Facet> facetResults =
                facetManager.getFacets(facetName.name());
            Facet selectedFacet = facetResults.get(selectedFacetIndex);
            facetManager.getFacetGroup(facetName.name())
                    .selectFacets(selectedFacet);
            break;
        }
    }

    /**
     *
     * @param page the page of results
     * @param facetName the facet name
     * @param facetManager the facet manager
     */
    protected void addFacet(final Page<T> page, final FacetName facetName,
            final FacetManager facetManager) {
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
        default:
            page.addFacets(facetName.name(),
                    facetManager.getFacets(facetName.name()));
            break;
        }
    }

  /**
   *
   * @return the fields to search by default
   */
  protected abstract String[] getDocumentFields();

  /**
   *
   */
  public final Page<T> search(final String query,
            final String spatialQuery,
            final Integer pageSize, final Integer pageNumber,
            final FacetName[] facets,
            final Map<FacetName, Integer> selectedFacets,
            final Sorting sort) {
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
                    = searchFactory.buildQueryBuilder().forEntity(getAnalyzerType()).get();
                luceneQuery = queryBuilder.all().createQuery();
            }
            FullTextQuery fullTextQuery = fullTextSession.createFullTextQuery(luceneQuery, type);
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
                  createFacetingRequest(queryBuilder.facet(), facetName, facetManager);
              }
            }

            if (sort == null) {
                //If a sort wasn't requested, go by Relevance/Lucene 'Score'
                fullTextQuery.setSort(new Sort(new SortField(null,
                        SortField.SCORE, false)));
            } else if (sort.getFieldName() == null) {
                fullTextQuery.setSort(new Sort(new SortField(null,
                        SortField.SCORE, false)));
            } else {
                // otherwise figure out what type we are actually searching by
                // TODO cope with different datatypes, not just the string value
                // of the field
                fullTextQuery.setSort(new Sort(new SortField(sort
                        .getFieldName(), SortField.STRING_VAL, sort
                        .getDirection() == SortDirection.REVERSE)));
            }

            List<T> results = (List<T>) fullTextQuery.list();

            if (selectedFacets != null && !selectedFacets.isEmpty()) {
                FacetManager facetManager = fullTextQuery.getFacetManager();

                for (FacetName facetName : selectedFacets.keySet()) {
                    selectFacet(facetName, facetManager,
                            selectedFacets.get(facetName));
                }
                results = (List<T>) fullTextQuery.list();
            }

            Page<T> page = new DefaultPageImpl<T>(
                    fullTextQuery.getResultSize(),
                    pageNumber, pageSize, results);
            if (facets != null && facets.length != 0) {
                FacetManager facetManager = fullTextQuery.getFacetManager();
                for (FacetName facetName : facets) {
                    addFacet(page, facetName, facetManager);
                }
            }
            if (selectedFacets != null && !selectedFacets.isEmpty()) {
                for (FacetName facetName : selectedFacets.keySet()) {
                    Integer selectedFacetIndex = selectedFacets.get(facetName);
                    page.setSelectedFacet(facetName.name(), selectedFacetIndex);
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
