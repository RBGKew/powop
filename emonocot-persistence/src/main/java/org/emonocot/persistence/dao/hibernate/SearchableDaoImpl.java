package org.emonocot.persistence.dao.hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.util.Version;
import org.emonocot.api.FacetName;
import org.emonocot.api.Sorting;
import org.emonocot.api.Sorting.SortDirection;
import org.emonocot.model.common.Base;
import org.emonocot.model.geography.Continent;
import org.emonocot.model.geography.Region;
import org.emonocot.model.media.Image;
import org.emonocot.model.pager.DefaultPageImpl;
import org.emonocot.model.pager.Page;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.persistence.QuerySyntaxException;
import org.emonocot.persistence.dao.SearchableDao;
import org.hibernate.Criteria;
import org.hibernate.criterion.Example;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.ProjectionConstants;
import org.hibernate.search.Search;
import org.hibernate.search.SearchFactory;
import org.hibernate.search.query.dsl.FacetContext;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.engine.spi.FacetManager;
import org.hibernate.search.query.facet.Facet;

/**
 *
 * @author ben
 *
 * @param <T>
 */
public abstract class SearchableDaoImpl<T extends Base> extends DaoImpl<T>
        implements SearchableDao<T> {
    /**
     * regex that matches queries with terms in them.
     */
    private Pattern pattern = Pattern.compile("\\w+:");

    /**
    *
    */
    protected static Class SEARCHABLE_CLASSES[] = new Class[] { Image.class,
            Taxon.class };

    /**
     *
     */
    private Class[] searchableClasses;

    /**
     *
     * @param newType
     *            Set the type of object handled by this class
     * @param searchTypes
     *            Set the subclasses of T to be searched for
     */
    public SearchableDaoImpl(final Class<T> newType, Class... searchTypes) {
        super(newType);
        this.searchableClasses = searchTypes;
    }

    /**
     *
     * @param facetContext
     *            the facet context
     * @param facetName
     *            the facet name
     * @param facetManager
     *            set the facet manager
     */
    protected abstract void createFacetingRequest(
            final FacetContext facetContext, final FacetName facetName,
            FacetManager facetManager);

    /**
     *
     * @param facetName
     *            Set the facet name
     * @param facetManager
     *            Set the facet manager
     * @param selectedFacetName
     *            Set the selected facet
     */
    protected void selectFacet(final FacetName facetName,
            final FacetManager facetManager, final String selectedFacetName) {
        switch (facetName) {
        case CLASS:
            break;
        default:
            doSelectFacet(facetName, facetManager, selectedFacetName);
            break;
        }
    }

    /**
     *
     * @param facetName
     *            Set the facet name
     * @param facetManager
     *            Set the facet manager
     * @param selectedFacetName
     *            Set the selected facet name
     */
    protected final void doSelectFacet(final FacetName facetName,
            final FacetManager facetManager, final String selectedFacetName) {
        List<Facet> facetResults = facetManager.getFacets(facetName.name());
        Facet selectedFacet = null;
        for (Facet f : facetResults) {
            if (f.getValue().equals(selectedFacetName)) {
                selectedFacet = f;
                break;
            }
        }
        if (selectedFacet != null) {
            facetManager.getFacetGroup(facetName.name()).selectFacets(
                    selectedFacet);
        }
    }

    /**
     *
     * @param page
     *            the page of results
     * @param facetName
     *            the facet name
     * @param facetManager
     *            the facet manager
     * @param selectedFacets
     *            add the select facets
     */
    protected void addFacet(final Page<T> page, final FacetName facetName,
            final FacetManager facetManager,
            Map<FacetName, String> selectedFacets) {
        switch (facetName) {
        case REGION:
            String selectedContinent = selectedFacets.get(FacetName.CONTINENT);
            if (selectedContinent != null) {
                Continent continent = Continent.valueOf(selectedContinent);
                List<Facet> facets = facetManager.getFacets(facetName.REGION
                        .name());
                List<Facet> filteredFacets = new ArrayList<Facet>();
                for (Facet f : facets) {
                    Region r = Region.valueOf(f.getValue());
                    if (r.getContinent().equals(continent)) {
                        filteredFacets.add(f);
                    }
                }
                page.addFacets(facetName.name(), filteredFacets);
            } else {
                // should not really get here
                page.addFacets(facetName.name(),
                        facetManager.getFacets(facetName.name()));
            }
            break;
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
     * @param query
     *            A lucene query
     * @param spatialQuery
     *            A spatial query to filter the results by
     * @param pageSize
     *            The maximum number of results to return
     * @param pageNumber
     *            The offset (in pageSize chunks, 0-based) from the beginning of
     *            the recordset
     * @param facets
     *            The names of the facets you want to calculate
     * @param selectedFacets
     *            A map of facets which you would like to restrict the search by
     * @param sort
     *            A representation for the order results should be returned in
     * @param fetch
     *            Set the fetch profile
     * @return a Page from the resultset
     */
    public final Page<T> search(final String query, final String spatialQuery,
            final Integer pageSize, final Integer pageNumber,
            final FacetName[] facets,
            final Map<FacetName, String> selectedFacets, final Sorting sort,
            final String fetch) {
        FullTextSession fullTextSession = Search
                .getFullTextSession(getSession());
        SearchFactory searchFactory = fullTextSession.getSearchFactory();

        try {
            // Create a lucene query
            org.apache.lucene.search.Query luceneQuery = null;

            if (query != null && !query.trim().equals("")) {
                Matcher matcher = pattern.matcher(query);
                QueryParser parser = null;
                if (matcher.matches()) {
                    parser = new QueryParser(Version.LUCENE_31,
                            getDefaultField(),
                            searchFactory.getAnalyzer(getAnalyzerType()));
                } else {
                    parser = new MultiFieldQueryParser(Version.LUCENE_31,
                            getDocumentFields(),
                            searchFactory.getAnalyzer(getAnalyzerType()));
                }
                luceneQuery = parser.parse(query);
            } else {
                QueryBuilder queryBuilder = searchFactory.buildQueryBuilder()
                        .forEntity(getAnalyzerType()).get();
                luceneQuery = queryBuilder.all().createQuery();
            }
            FullTextQuery fullTextQuery = fullTextSession.createFullTextQuery(
                    luceneQuery, searchableClasses);
            if (spatialQuery != null && spatialQuery.trim().length() != 0) {
                // TODO Implement spatial filter
            }

            // Set additional result parameters
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
                    createFacetingRequest(queryBuilder.facet(), facetName,
                            facetManager);
                }
            }

            if (sort == null) {
                // If a sort wasn't requested, go by Relevance/Lucene 'Score'
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

            // Run the search
            if (fetch != null
                    && (selectedFacets == null || selectedFacets.isEmpty())) {
                Criteria criteria = fullTextSession
                        .createCriteria(getAnalyzerType());
                boolean setCriteria = enableProfilePreQuery(criteria, fetch);
                if (setCriteria) {
                    fullTextQuery.setCriteriaQuery(criteria);
                }
            }
            List<T> results = (List<T>) fullTextQuery.list();
            if (selectedFacets != null && !selectedFacets.isEmpty()) {
                FacetManager facetManager = fullTextQuery.getFacetManager();

                for (FacetName facetName : selectedFacets.keySet()) {
                    selectFacet(facetName, facetManager,
                            selectedFacets.get(facetName));
                }
                Criteria criteria = fullTextSession
                        .createCriteria(getAnalyzerType());
                boolean setCriteria = enableProfilePreQuery(criteria, fetch);
                if (setCriteria) {
                    fullTextQuery.setCriteriaQuery(criteria);
                }
                results = (List<T>) fullTextQuery.list();
            }

            // Create the results page
            for (T t : results) {
                // TODO review
                enableProfilePostQuery(t, fetch);
            }

            Page<T> page = new DefaultPageImpl<T>(
                    fullTextQuery.getResultSize(), pageNumber, pageSize,
                    results);
            if (facets != null && facets.length != 0) {
                FacetManager facetManager = fullTextQuery.getFacetManager();
                for (FacetName facetName : facets) {
                    addFacet(page, facetName, facetManager, selectedFacets);
                }
            }
            if (selectedFacets != null && !selectedFacets.isEmpty()) {
                for (FacetName facetName : selectedFacets.keySet()) {
                    String selectedFacet = selectedFacets.get(facetName);
                    page.setSelectedFacet(facetName.name(), selectedFacet);
                }
            }

            return page;
        } catch (ParseException e) {
            throw new QuerySyntaxException("Exception parsing " + query, e);
        }
    }

    public abstract String getDefaultField();

    /**
     * Given https://hibernate.onjira.com/browse/HSEARCH-703, we need this
     * workaround, currently.
     * 
     * This should be removed once the issue is resolved by the hibernate search
     * team
     * 
     * @return a class to retrieve an analyzer for.
     */
    protected Class getAnalyzerType() {
        return type;
    }

    public Page<T> searchByExample(T example, boolean ignoreCase, boolean useLike) {
        Example criterion = Example.create(example);
        if(ignoreCase) {
            criterion.ignoreCase();
        }
        if(useLike) {
            criterion.enableLike();
        }
        Criteria criteria = getSession().createCriteria(Taxon.class);
        criteria.add(criterion);
        List<T> results = (List<T>) criteria.list();
        Page page = new DefaultPageImpl<T>(results.size(), null, null, results);
        return page;
    }

}
