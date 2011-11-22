package org.emonocot.model.marshall.json;

import org.codehaus.jackson.map.ObjectMapper;
import org.emonocot.api.GroupService;
import org.emonocot.api.ImageService;
import org.emonocot.api.JobInstanceService;
import org.emonocot.api.ReferenceService;
import org.emonocot.api.SourceService;
import org.emonocot.api.TaxonService;
import org.emonocot.api.UserService;
import org.springframework.beans.factory.FactoryBean;

/**
 *
 * @author ben
 *
 */
public class CustomObjectMapperFactory implements FactoryBean<ObjectMapper> {

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
     * @return the object created by this factory
     */
    public final ObjectMapper getObject() {
        if (objectMapper == null) {
            objectMapper = new CustomObjectMapper();
            objectMapper.setTaxonService(taxonService);
            objectMapper.setReferenceService(referenceService);
            objectMapper.setImageService(imageService);
            objectMapper.setUserService(userService);
            objectMapper.setGroupService(groupService);
            objectMapper.setSourceService(sourceService);
            objectMapper.setJobInstanceService(jobInstanceService);
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
