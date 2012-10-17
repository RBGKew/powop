package org.emonocot.portal.controller;

import org.emonocot.api.Service;
import org.emonocot.model.Base;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponentsBuilder;

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
     * @param identifier
     *            Set the identifier of the image
     * @return A model and view containing a image
     */
    @RequestMapping(value = "/{identifier}",
                    method = RequestMethod.GET,
                    produces = "application/json")
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
    		        produces = "application/json",
                    consumes = "application/json")
    public final ResponseEntity<T> create(@RequestBody final T object, UriComponentsBuilder builder) throws Exception {
    	logger.error("POST " + object);       
        
        try {
            service.merge(object);
        } catch(Exception e) {
        	logger.error(e.getLocalizedMessage());
        	for(StackTraceElement ste : e.getStackTrace()) {
        		logger.error(ste.toString());
        	}
        	throw e;
        }
        
        T persistedObject = service.find(object.getIdentifier());
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/" + getDirectory() + "/{id}").buildAndExpand(persistedObject.getId()).toUri());

        return new ResponseEntity<T>(object, headers, HttpStatus.CREATED);
    }

    /**
     * @param identifier
     *            Set the identifier of the image
     * @return A response entity containing the status
     */
    @RequestMapping(value = "/{id}",
                    method = RequestMethod.DELETE,
                    consumes = "application/json",
                    produces = "application/json")
    public final ResponseEntity<T> delete(@PathVariable final Long id) {
        service.deleteById(id);
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
