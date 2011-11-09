package org.emonocot.portal.remoting;

import java.util.Map;

import org.emonocot.model.common.Base;
import org.emonocot.api.Sorting;
import org.emonocot.model.pager.Page;
import org.emonocot.persistence.dao.Dao;
import org.emonocot.api.FacetName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author ben
 *
 * @param <T>
 */

public abstract class DaoImpl<T extends Base> implements Dao<T> {

    /**
     * Logger.
     */
    private static Logger logger
        = LoggerFactory.getLogger(DaoImpl.class);

   /**
    *
    */
    private RestTemplate restTemplate;

    /**
     *
     */
    private Class<T> type;

    /**
     *
     */
    private String baseUri;

    /**
     *
     */
    private String resourceDir;

    /**
     *
     */
    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     *
     * @param newType Set the type object handled by this dao
     * @param newResourceDir Set the resource directory
     */
    public DaoImpl(final Class<T> newType, final String newResourceDir) {
        this.type = newType;
        this.resourceDir = newResourceDir;
    }

    /**
     *
     * @param newBaseUri Set the base uri
     */
    public final void setBaseUri(final String newBaseUri) {
        this.baseUri = newBaseUri;
    }

    /**
     * @param identifier set the identifier
     * @return an object
     */
    public final T load(final String identifier) {
        return restTemplate.getForObject(baseUri + resourceDir + "/"
                + identifier, type);
    }

    /**
     * @param identifier the identifier of the object to delete
     */
    public final void delete(final String identifier) {
        restTemplate.delete(baseUri + resourceDir + "/" + identifier);
    }

    /**
     * @param identifier Set the identifier
     * @return an object
     */
    public final T find(final String identifier) {
        return restTemplate.getForObject(baseUri + resourceDir + "/"
                + identifier, type);
    }

    /**
     * @param identifier set the identifier
     * @param fetch set the fetch profile
     * @return an object
     */
    public final T load(final String identifier, final String fetch) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @param identifier set the identifier
     * @param fetch set the fetch profile
     * @return an object
     */
    public final T find(final String identifier, final String fetch) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @param t the object to save
     * @return the saved object
     */
    public final T save(final T t) {
        logger.debug("POST: " + baseUri + resourceDir);
        restTemplate.postForObject(baseUri + resourceDir, t, type);
        return t;
    }
    /**
     * @param t the object to update
     */
    public final void update(final T t) {
        logger.debug("POST: " + baseUri + resourceDir + "/" + t.getIdentifier());
        restTemplate.postForObject(
                baseUri + resourceDir + "/" + t.getIdentifier(), t, type);
    }

    /**
     * @param t the object to save or update
     */
    public void saveOrUpdate(final T t) {
        // TODO Auto-generated method stub
    }

    /**
     * @param query set the query
     * @param spatialQuery set the spatial query
     * @param pageSize set the page size
     * @param pageNumber set the page number
     * @param facets set the facets to be calculated
     * @param selectedFacets set the facets that are selected
     * @param fetch TODO
     * @return a page of objects
     */
    public final Page<T> search(final String query, final String spatialQuery,
            final Integer pageSize, final Integer pageNumber,
            final FacetName[] facets,
            final Map<FacetName, String> selectedFacets,
            final Sorting sort, String fetch) {
        // TODO Auto-generated method stub
        // TODO move persistence to persistence package?
        return null;
    }

}
