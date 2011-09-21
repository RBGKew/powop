package org.emonocot.portal.controller;

import java.net.URI;
import java.net.URISyntaxException;

import org.emonocot.model.media.Image;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.service.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class ImageController {

   /**
    *
    */
   private static Logger logger = LoggerFactory
           .getLogger(ImageController.class);
   /**
    *
    */
   private ImageService service;

   /**
    *
    */
   private String baseUrl;

   /**
    *
    * @param newBaseUrl Set the base url
    */
   public final void setBaseUrl(final String newBaseUrl) {
       this.baseUrl = newBaseUrl;
   }

   /**
   *
   * @param imageService
   *            Set the image service
   */
  @Autowired
  public final void setImageService(final ImageService imageService) {
      this.service = imageService;
  }

  /**
   * @param identifier
   *            Set the identifier of the image
   * @return A model and view containing a image
   */
  @RequestMapping(value = "/image/{identifier}",
                  method = RequestMethod.GET,
                  headers = "Accept=application/json")
  public final ResponseEntity<Image> get(
          @PathVariable final String identifier) {
      return new ResponseEntity<Image>(service.find(identifier),
              HttpStatus.OK);
  }

  /**
   * @param image
   *            the image to save
   * @return A response entity containing a newly created image
   */
    @RequestMapping(value = "/image",
                    method = RequestMethod.POST)
    public final ResponseEntity<Image> create(
            @RequestBody final Image image) {
        HttpHeaders httpHeaders = new HttpHeaders();
        try {
            httpHeaders.setLocation(new URI(baseUrl + "image/" + image.getIdentifier()));
        } catch (URISyntaxException e) {
            logger.error(e.getMessage());
        }
        ResponseEntity<Image> response = new ResponseEntity<Image>(
                service.save(image), httpHeaders, HttpStatus.CREATED);
        return response;
    }

  /**
   * @param identifier
   *            Set the identifier of the image
   * @return A response entity containing the status
   */
    @RequestMapping(value = "/image/{identifier}",
                    method = RequestMethod.DELETE)
    public final ResponseEntity<Image> delete(
            @PathVariable final String identifier) {
        service.delete(identifier);
        return new ResponseEntity<Image>(HttpStatus.OK);
    }
}
