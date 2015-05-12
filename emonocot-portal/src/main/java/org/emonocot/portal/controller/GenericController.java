/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
package org.emonocot.portal.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.emonocot.api.Service;
import org.emonocot.model.Base;
import org.emonocot.pager.Page;
import org.restdoc.api.GlobalHeader;
import org.restdoc.api.MethodDefinition;
import org.restdoc.api.ParamValidation;
import org.restdoc.api.ResponseDefinition;
import org.restdoc.api.RestDoc;
import org.restdoc.api.RestResource;
import org.restdoc.api.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.fasterxml.jackson.module.jsonSchema.factories.SchemaFactoryWrapper;

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
    
    private ObjectMapper objectMapper;

    private String directory;
    
    private Class<T> type;
    
    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
    	this.objectMapper = objectMapper;
    }

    public GenericController(String directory, Class<T> type) {
        this.directory = directory;
        this.type = type;
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
    
    @RequestMapping(method = RequestMethod.OPTIONS,
            produces = "application/json")
    public ResponseEntity<RestDoc> optionsResource() throws JsonMappingException {
        RestDoc restDoc = new RestDoc();
        HashMap<String,Schema> schemas = new HashMap<String,Schema>();
        Schema pagerSchema = new Schema();
        SchemaFactoryWrapper pageVisitor = new SchemaFactoryWrapper();
        objectMapper.acceptJsonFormatVisitor(objectMapper.constructType(Page.class), pageVisitor);
        pagerSchema.setSchema(pageVisitor.finalSchema());
        schemas.put("http://e-monocot.org#page", pagerSchema);
        Schema objectSchema = new Schema();
        SchemaFactoryWrapper objectVisitor = new SchemaFactoryWrapper();
        objectMapper.acceptJsonFormatVisitor(objectMapper.constructType(type), objectVisitor);
        objectSchema.setSchema(objectVisitor.finalSchema());
        schemas.put("http://e-monocot.org#" + directory, objectSchema);
        restDoc.setSchemas(schemas);
        
        GlobalHeader headers = new GlobalHeader();
        headers.request("Content-Type","Must be set to application/json",true);
        headers.request("Authorization","Supports HTTP Basic. Users may also use their api key",false);
        
        restDoc.setHeaders(headers);
        
        ParamValidation integerParam = new ParamValidation();
        integerParam.setType("match");
        integerParam.setPattern("\\d+");
        ParamValidation apikeyParam = new ParamValidation();
        apikeyParam.setType("match");
        apikeyParam.setPattern("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");
        ParamValidation stringParam = new ParamValidation();
        stringParam.setType("match");
        stringParam.setPattern("[0-9a-f]+");
        
        Set<RestResource> resources = new HashSet<RestResource>();
        RestResource listOfObjects = new RestResource();
        
        listOfObjects.setId(type.getSimpleName() + "List");
        listOfObjects.setPath("/" + directory + "{?limit,start,callback,apikey,fetch}");
        listOfObjects.param("limit", "The maximum number of resources to return", integerParam);
        listOfObjects.param("start", "The number of pages (of size _limit_) offset from the beginning of the recordset", integerParam);
        listOfObjects.param("apikey", "The apikey of the user account making the request", apikeyParam);
        listOfObjects.param("callback", "The name of the callback function used to wrap the JSON response", stringParam);
        listOfObjects.param("fetch", "The name of a valid 'fetch-profile' which will load some or all related objects prior to serialization. Try 'object-page' to return most related objects", stringParam);
        
        MethodDefinition listObjects = new MethodDefinition();
        listObjects.description("List " + type.getSimpleName() + " resources");
        ResponseDefinition listObjectsResponseDefinition = new ResponseDefinition();
        listObjectsResponseDefinition.type("application/json", "http://e-monocot.org#page");
        listObjectsResponseDefinition.type("application/javascript", "http://e-monocot.org#page");
        listObjects.response(listObjectsResponseDefinition);
        listObjects.statusCode("200", "Successfully retrieved a list of 0+ resources");
        listOfObjects.method("GET", listObjects);
        
        MethodDefinition createObject = new MethodDefinition();
        ResponseDefinition createdResponseDefinition = new ResponseDefinition();
        createdResponseDefinition.type("application/json", "http://e-monocot.org#" + type.getSimpleName());	
        createdResponseDefinition.header("Location", "The location of the created resource", true);
        createObject.response(createdResponseDefinition);
        createObject.description("Create a new " + type.getSimpleName() + " resource");
        createObject.accept("application/json", "http://e-monocot.org#" + type.getSimpleName());
        createObject.statusCode("201", "Successfully created the resource");
        listOfObjects.method("POST", createObject);
       
        resources.add(listOfObjects);
        
        RestResource singleObject = new RestResource();
        singleObject.setId(type.getSimpleName());
        singleObject.setPath("/" + directory + "/{identifier}{?apikey,callback}");
        singleObject.param("apikey", "The apikey of the user account making the request", apikeyParam);  
        singleObject.param("callback", "The name of the callback function used to wrap the JSON response", stringParam);
        singleObject.param("identifier", "The identifier of the object", stringParam);
        
        MethodDefinition getObject = new MethodDefinition();
        getObject.description("Get a " + type.getSimpleName() + " resource");
        ResponseDefinition getObjectResponseDefinition = new ResponseDefinition();        
        getObjectResponseDefinition.type("application/json", "http://e-monocot.org#" + type.getSimpleName());
        getObjectResponseDefinition.type("application/javascript", "http://e-monocot.org#" + type.getSimpleName());
        getObject.response(getObjectResponseDefinition);
        getObject.statusCode("200", "Successfully retrieved a resource");        
        singleObject.method("GET", getObject);
        
        MethodDefinition updateObject = new MethodDefinition();
        ResponseDefinition updatedObjectResponseDefinition = new ResponseDefinition();
        updatedObjectResponseDefinition.type("application/json", "http://e-monocot.org#" + type.getSimpleName());	
        updateObject.response(updatedObjectResponseDefinition);
        updateObject.description("Update an existing " + type.getSimpleName() + " resource");
        updateObject.accept("application/json", "http://e-monocot.org#" + type.getSimpleName());
        updateObject.statusCode("200", "Successfully updated the resource");        
        singleObject.method("POST", updateObject);
        
        MethodDefinition deleteObject = new MethodDefinition();
        ResponseDefinition deletedObjectResponseDefinition = new ResponseDefinition();
        deletedObjectResponseDefinition.type("application/json", "http://e-monocot.org#" + type.getSimpleName());	
        deleteObject.response(deletedObjectResponseDefinition);
        deleteObject.description("Delete an existing " + type.getSimpleName() + " resource");
        deleteObject.accept("application/json", "http://e-monocot.org#" + type.getSimpleName());
        deleteObject.statusCode("200", "Successfully deleted the resource");        
        singleObject.method("DELETE", deleteObject);
        
        resources.add(singleObject);
        
        restDoc.setResources(resources);
        
        return new ResponseEntity<RestDoc>(restDoc,HttpStatus.OK);
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
