package org.emonocot.model.marshall.json;

import org.codehaus.jackson.map.ObjectMapper;
import org.emonocot.api.GroupService;
import org.emonocot.api.IdentificationKeyService;
import org.emonocot.api.ImageService;
import org.emonocot.api.JobInstanceService;
import org.emonocot.api.ReferenceService;
import org.emonocot.api.OrganisationService;
import org.emonocot.api.TaxonService;
import org.emonocot.api.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.module.hibernate.HibernateModule;

/**
*
*/
public class CustomObjectMapper extends ObjectMapper {
	
	Logger logger = LoggerFactory.getLogger(CustomObjectMapper.class);
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
    private OrganisationService organisationService;

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
     * @param newIdentificationKeyService the identification key service to set
     */
    public final void setIdentificationKeyService(final IdentificationKeyService newIdentificationKeyService) {
        this.identificationKeyService = newIdentificationKeyService;
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
     * 
     * @param newTaxonService
     *            Set the taxon service
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
     * @param newJobInstanceService
     *            the jobInstanceService to set
     */
    public final void setJobInstanceService(
            final JobInstanceService newJobInstanceService) {
        this.jobInstanceService = newJobInstanceService;
    }

    /**
     * @param newOrganisationService
     *            the sourceService to set
     */
    public final void setOrganisationService(final OrganisationService newOrganisationService) {
        this.organisationService = newOrganisationService;
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
    	logger.debug("init() called"); 
        CustomHandlerInstantiator handlerInstantiator = new CustomHandlerInstantiator();
        handlerInstantiator.setReferenceService(referenceService);
        handlerInstantiator.setTaxonService(taxonService);
        handlerInstantiator.setImageService(imageService);
        handlerInstantiator.setGroupService(groupService);
        handlerInstantiator.setUserService(userService);
        handlerInstantiator.setOrganisationService(organisationService);
        handlerInstantiator.setIdentificationKeyService(identificationKeyService);
        setHandlerInstantiator(handlerInstantiator);
        CustomModule module = new CustomModule(jobInstanceService);
        registerModule(module);
        registerModule(new HibernateModule());
    }
}
