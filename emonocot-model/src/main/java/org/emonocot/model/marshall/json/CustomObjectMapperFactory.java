package org.emonocot.model.marshall.json;

import org.codehaus.jackson.map.ObjectMapper;
import org.emonocot.service.ImageService;
import org.emonocot.service.ReferenceService;
import org.emonocot.service.TaxonService;
import org.springframework.beans.factory.FactoryBean;

/**
 *
 * @author ben
 *
 */
public class CustomObjectMapperFactory implements FactoryBean<ObjectMapper>  {

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
    * @param newReferenceService Set the reference service
    */
    public final void setReferenceService(
            final ReferenceService newReferenceService) {
        this.referenceService = newReferenceService;
    }

    /**
     * @param newTaxonService the taxonService to set
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
    private CustomObjectMapper objectMapper;

    /**
     * @return the object created by this factory
     */
    public final ObjectMapper getObject() {
        if (objectMapper == null) {
            objectMapper = new CustomObjectMapper();
            objectMapper.setTaxonService(taxonService);
            objectMapper.setReferenceService(referenceService);
            objectMapper.setImageService(imageService);
            objectMapper.init();
        }
        return objectMapper;
    }

    /**
     * @return the type of object created by this factory
     */
    public final Class<?> getObjectType() {
        return ObjectMapper.class;
    }

    /**
     * @return true, if this object is a singleton, false otherwise
     */
    public final boolean isSingleton() {
        return true;
    }
}
