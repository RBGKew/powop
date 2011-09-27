package org.emonocot.model.marshall.json;

import org.codehaus.jackson.map.ObjectMapper;
import org.emonocot.service.ImageService;
import org.emonocot.service.ReferenceService;
import org.emonocot.service.TaxonService;

/**
*
*/
public class CustomObjectMapper extends ObjectMapper {
   /**
    *
    */
    private ReferenceService referenceService;

    /**
     *
     */
    private TaxonService taxonService;

    /**
     *
     */
    private ImageService imageService;
    /**
     *
     * @param newReferenceService
     *            Set the reference service
     */
    public final void setReferenceService(
            final ReferenceService newReferenceService) {
        this.referenceService = newReferenceService;
    }

    /**
     *
     * @param newTaxonService Set the taxon service
     */
    public final void setTaxonService(final TaxonService newTaxonService) {
       this.taxonService = newTaxonService;
    }

    /**
     * @param newImageService the imageService to set
     */
    public final void setImageService(final ImageService newImageService) {
        this.imageService = newImageService;
    }
    /**
    *
    */
    protected CustomObjectMapper() {
    }

    /**
    *
    */
    protected final void init() {
        CustomHandlerInstantiator handlerInstantiator
            = new CustomHandlerInstantiator();
        handlerInstantiator.setReferenceService(referenceService);
        handlerInstantiator.setTaxonService(taxonService);
        handlerInstantiator.setImageService(imageService);
        setHandlerInstantiator(handlerInstantiator);
        CustomModule module = new CustomModule();
        registerModule(module);
    }
}
