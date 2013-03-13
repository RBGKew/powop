package org.emonocot.portal.controller;


import org.emonocot.api.ImageService;
import org.emonocot.model.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author ben
 *
 */
@Controller
@RequestMapping("/image")
public class ImageController extends GenericController<Image, ImageService> {
	
	private static Logger queryLog = LoggerFactory.getLogger("query");

    /**
     *
     * @param imageService
     *            Set the image service
     */
    @Autowired
    public final void setImageService(final ImageService imageService) {
        super.setService(imageService);
    }

    /**
     *
     */
    public ImageController() {
       super("image");
    }

    /**
     * @param identifier
     *            Set the identifier of the image
     * @param model Set the model
     * @return the name of the view
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = {"text/html", "*/*"})
    public final String show(@PathVariable final Long id, final Model model) {
        Image image = getService().load(id);
        model.addAttribute(image);
        queryLog.info("Image: \'{}\'", new Object[] {id});
        return "image/show";
    }
}
