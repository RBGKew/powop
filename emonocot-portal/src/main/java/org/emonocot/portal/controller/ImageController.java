package org.emonocot.portal.controller;


import org.emonocot.api.ImageService;
import org.emonocot.api.TaxonService;
import org.emonocot.model.media.Image;
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

    /**
     *
     */
    private TaxonService taxonService;

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
     * @param newTaxonService
     *            Set the taxon service
     */
    @Autowired
    public final void setTaxonService(final TaxonService newTaxonService) {
        this.taxonService = newTaxonService;
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
    @RequestMapping(value = "/{identifier}", method = RequestMethod.GET)
    public final String getPage(@PathVariable final String identifier,
            final Model model) {
        Image image = getService().load(identifier, "image-page");
        model.addAttribute(image);
        if (image.getTaxon() != null) {
            model.addAttribute(taxonService.load(image.getTaxon()
                    .getIdentifier(), "taxon-page"));
        }
        return "image/show";
    }
}
