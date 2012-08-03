package org.emonocot.portal.controller;

import java.net.URI;
import java.net.URISyntaxException;

import org.emonocot.api.Service;
import org.emonocot.model.common.Base;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author ben
 * @param <T> the type of object served by this controller
 * @param <SERVICE> the service supplying this object
 */
public abstract class GenericController<T extends Base,
                                        SERVICE extends Service<T>> {

    /**
    *
    */
    private static Logger logger = LoggerFactory.getLogger(GenericController.class);
   /**
    *
    */
    private SERVICE service;
    /**
    *
    */
    private String baseUrl;

    /**
     *
     */
    private String directory;

    /**
     * @param newDirectory Set the directory
     */
    public GenericController(final String newDirectory) {
        this.directory = newDirectory;
    }

    /**
     *
     * @return the directory where this resource is found
     */
    private String getDirectory() {
       return directory;
    }

    /**
     *
     * @param newBaseUrl
     *            Set the base url
     */
    public final void setBaseUrl(final String newBaseUrl) {
        this.baseUrl = newBaseUrl;
    }

    /**
     * @param identifier
     *            Set the identifier of the image
     * @return A model and view containing a image
     */
    @RequestMapping(value = "/{identifier}",
                    method = RequestMethod.GET,
                    headers = "Accept=application/json")
    public final ResponseEntity<T> get(@PathVariable final String identifier) {
        return new ResponseEntity<T>(service.find(identifier), HttpStatus.OK);
    }

    /**
     * @param object
     *            the object to save
     * @return A response entity containing a newly created image
     * @throws Exception 
     */
    @RequestMapping(method = RequestMethod.POST,
                    headers = "Content-Type=application/json")
    public final ResponseEntity<T> create(@RequestBody final T object) throws Exception {
    	logger.error("POST " + object);
        HttpHeaders httpHeaders = new HttpHeaders();
        try {
            httpHeaders.setLocation(new URI(baseUrl + getDirectory() + "/"
                    + object.getIdentifier()));
        } catch (URISyntaxException e) {
            logger.error(e.getMessage());
        }
        try {
            service.merge(object);
        } catch(Exception e) {
        	logger.error(e.getLocalizedMessage());
        	for(StackTraceElement ste : e.getStackTrace()) {
        		logger.error(ste.toString());
        	}
        	throw e;
        }
        ResponseEntity<T> response = new ResponseEntity<T>(object, httpHeaders,
                HttpStatus.CREATED);
        return response;
    }

    /**
     * @param identifier
     *            Set the identifier of the image
     * @return A response entity containing the status
     */
    @RequestMapping(value = "/{identifier}",
                    method = RequestMethod.DELETE,
                    headers = "Accept=application/json")
    public final ResponseEntity<T> delete(
            @PathVariable final String identifier) {
        service.delete(identifier);
        return new ResponseEntity<T>(HttpStatus.OK);
    }

    /**
     *
     * @param newService Set the service
     */
    public final void setService(final SERVICE newService) {
        this.service = newService;
    }

    /**
     * @return the service
     */
    public final SERVICE getService() {
        return service;
    }
}
