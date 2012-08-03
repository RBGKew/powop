package org.emonocot.model.marshall.json;

import org.codehaus.jackson.map.ObjectMapper;
import org.emonocot.api.GroupService;
import org.emonocot.api.IdentificationKeyService;
import org.emonocot.api.ImageService;
import org.emonocot.api.JobInstanceService;
import org.emonocot.api.ReferenceService;
import org.emonocot.api.SourceService;
import org.emonocot.api.TaxonService;
import org.emonocot.api.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;

/**
 *
 * @author ben
 *
 */
public class CustomObjectMapperFactory implements FactoryBean<ObjectMapper> {
	Logger logger = LoggerFactory.getLogger(CustomObjectMapperFactory.class);

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
    */
    private UserService userService;

   /**
    *
    */
    private GroupService groupService;

    /**
    *
    */
    private SourceService sourceService;

    /**
    *
    */
    private CustomObjectMapper objectMapper;

    /**
     *
     */
    private JobInstanceService jobInstanceService;

    /**
     *
     */
     private IdentificationKeyService identificationKeyService;

    /**
     * @param userService
     *            the userService to set
     */
    public final void setUserService(final UserService userService) {
        this.userService = userService;
    }

    /**
     * @param groupService
     *            the groupService to set
     */
    public final void setGroupService(final GroupService groupService) {
        this.groupService = groupService;
    }

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
     * @param newTaxonService
     *            the taxonService to set
     */
    public final void setTaxonService(final TaxonService newTaxonService) {
        this.taxonService = newTaxonService;
    }

    /**
     * @param newImageService
     *            the imageService to set
     */
    public final void setImageService(final ImageService newImageService) {
        this.imageService = newImageService;
    }

    /**
     * @param sourceService
     *            the sourceService to set
     */
    public final void setSourceService(final SourceService sourceService) {
        this.sourceService = sourceService;
    }

    /**
     * @param jobInstanceService the jobInstanceService to set
     */
    public final void setJobInstanceService(
            final JobInstanceService jobInstanceService) {
        this.jobInstanceService = jobInstanceService;
    }

    /**
     * @param newIdentificationKeyService the identification key service to set
     */
    public final void setIdentificationKeyService(final IdentificationKeyService newIdentificationKeyService) {
        this.identificationKeyService = newIdentificationKeyService;
    }
    
    /**
     * @return the object created by this factory
     */
    public final ObjectMapper getObject() {
    	logger.debug("getObject called");
        if (objectMapper == null) {
        	logger.debug("creating new CustomObjectMapper");
            objectMapper = new CustomObjectMapper();
            objectMapper.setTaxonService(taxonService);
            objectMapper.setReferenceService(referenceService);
            objectMapper.setImageService(imageService);
            objectMapper.setUserService(userService);
            objectMapper.setGroupService(groupService);
            objectMapper.setSourceService(sourceService);
            objectMapper.setJobInstanceService(jobInstanceService);
            objectMapper.setIdentificationKeyService(identificationKeyService);
            objectMapper.init();
        }
        logger.debug("Returning objectMapper " + objectMapper);
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
