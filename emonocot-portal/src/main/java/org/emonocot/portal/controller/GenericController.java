package org.emonocot.portal.controller;

import org.codehaus.jackson.map.util.JSONPObject;
import org.emonocot.api.Service;
import org.emonocot.model.Base;
import org.emonocot.pager.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.hibernate3.HibernateObjectRetrievalFailureException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author ben
 * @param <T> the type of object served by this controller
 * @param <SERVICE> the service supplying this object
 */
public abstract class GenericController<T extends Base,
                                        SERVICE extends Service<T>> {

    private static Logger logger = LoggerFactory.getLogger(GenericController.class);

    private SERVICE service;

    private String directory;

    public GenericController(String newDirectory) {
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
                    consumes = "application/json",
                    produces = "application/json")
    public ResponseEntity<T> get(@PathVariable String identifier, @RequestParam(value = "fetch", required = false) String fetch) {
        return new ResponseEntity<T>(service.find(identifier,fetch), HttpStatus.OK);
    }
    
    /**
     * @param identifier
     *            Set the identifier of the image
     * @return A model and view containing a image
     */
    @RequestMapping(value = "/{identifier}",
    		        params = "callback",
                    method = RequestMethod.GET,
                    produces = "application/javascript")
    public ResponseEntity<JSONPObject> getJsonP(@PathVariable String identifier, @RequestParam(value = "fetch", required = false) String fetch,
    		                          @RequestParam(value = "callback", required = true) String callback) {
        return new ResponseEntity<JSONPObject>(new JSONPObject(callback,service.find(identifier,fetch)), HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.GET,
            consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<Page<T>> list(@RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
    		                                  @RequestParam(value = "start", required = false, defaultValue = "0") Integer start) {
        return new ResponseEntity<Page<T>>(service.list(start, limit, null), HttpStatus.OK);
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
    public ResponseEntity<T> create(@RequestBody T object, UriComponentsBuilder builder) throws Exception {
        
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
    public ResponseEntity<T> delete(@PathVariable Long id) {
        service.deleteById(id);
        return new ResponseEntity<T>(HttpStatus.OK);
    }

    /**
     *
     * @param newService Set the service
     */
    public void setService(SERVICE newService) {
        this.service = newService;
    }

    /**
     * @return the service
     */
    public SERVICE getService() {
        return service;
    }
    
    @ExceptionHandler(HibernateObjectRetrievalFailureException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public ModelAndView handleObjectNotFoundException(HibernateObjectRetrievalFailureException orfe) {
    	ModelAndView modelAndView = new ModelAndView("resourceNotFound");
    	modelAndView.addObject("exception", orfe);
        return modelAndView;
    }
}
