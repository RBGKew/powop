package org.emonocot.portal.controller;

import java.net.URI;
import java.net.URISyntaxException;

import org.emonocot.api.AnnotationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.emonocot.model.common.Annotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author ben
 *
 */
@Controller
public class AnnotationController {

   /**
    *
    */
    private static Logger logger = LoggerFactory
            .getLogger(AnnotationController.class);

    /**
     *
     */
    private AnnotationService service;

    /**
    *
    */
    private String baseUrl;

    /**
     *
     * @param newBaseUrl
     *            Set the base url
     */
    public final void setBaseUrl(final String newBaseUrl) {
        this.baseUrl = newBaseUrl;
    }

    /**
     * @param newService
     *            set the annotation service
     */
    @Autowired
    public final void setAnnotationService(final AnnotationService newService) {
        this.service = newService;
    }

    /**
     * @param identifier
     *            Set the identifier of the annotation
     * @return A model and view containing a annotation
     */
    @RequestMapping(value = "/annotation/{identifier}", method = RequestMethod.GET, headers = "Accept=application/json")
    public final ResponseEntity<Annotation> get(
            @PathVariable final String identifier) {
        return new ResponseEntity<Annotation>(service.find(identifier),
                HttpStatus.OK);
    }

    /**
     * @param annotation
     *            the job instance to save
     * @return A response entity containing a newly created annotation
     */
    @RequestMapping(value = "/annotation", method = RequestMethod.POST)
    public final ResponseEntity<Annotation> create(
            @RequestBody final Annotation annotation) {
        HttpHeaders httpHeaders = new HttpHeaders();
        try {
            httpHeaders.setLocation(new URI(baseUrl + "annotation/"
                    + annotation.getId()));
        } catch (URISyntaxException e) {
            logger.error(e.getMessage());
        }
        service.save(annotation);
        ResponseEntity<Annotation> response = new ResponseEntity<Annotation>(
                annotation, httpHeaders, HttpStatus.CREATED);
        return response;
    }

    /**
     * @param identifier
     *            Set the identifier of the annotation
     * @return A response entity containing the status
     */
    @RequestMapping(value = "/annotation/{identifier}", method = RequestMethod.DELETE)
    public final ResponseEntity<Annotation> delete(
            @PathVariable final String identifier) {
        service.delete(identifier);
        return new ResponseEntity<Annotation>(HttpStatus.OK);
    }
}
