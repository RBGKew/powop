package org.emonocot.model.marshall.json;

import org.codehaus.jackson.map.ObjectMapper;
import org.emonocot.api.GroupService;
import org.emonocot.api.IdentificationKeyService;
import org.emonocot.api.ImageService;
import org.emonocot.api.JobInstanceService;
import org.emonocot.api.PhylogeneticTreeService;
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

    private ReferenceService referenceService;

    private TaxonService taxonService;

    private ImageService imageService;

    private UserService userService;

    private GroupService groupService;

    private OrganisationService organisationService;

    private JobInstanceService jobInstanceService;

    private IdentificationKeyService identificationKeyService;
    
    private PhylogeneticTreeService phylogeneticTreeService;

    /**
     * @param userService
     *            the userService to set
     */
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * @param groupService
     *            the groupService to set
     */
    public void setGroupService(GroupService groupService) {
        this.groupService = groupService;
    }

    /**
     * @param newIdentificationKeyService the identification key service to set
     */
    public void setIdentificationKeyService(IdentificationKeyService newIdentificationKeyService) {
        this.identificationKeyService = newIdentificationKeyService;
    }

    /**
     * 
     * @param newReferenceService
     *            Set the reference service
     */
    public void setReferenceService(
            ReferenceService newReferenceService) {
        this.referenceService = newReferenceService;
    }

    /**
     * 
     * @param newTaxonService
     *            Set the taxon service
     */
    public void setTaxonService(TaxonService newTaxonService) {
        this.taxonService = newTaxonService;
    }

    /**
     * @param newImageService
     *            the imageService to set
     */
    public void setImageService(ImageService newImageService) {
        this.imageService = newImageService;
    }

    /**
     * @param newJobInstanceService
     *            the jobInstanceService to set
     */
    public void setJobInstanceService(
            JobInstanceService newJobInstanceService) {
        this.jobInstanceService = newJobInstanceService;
    }

    /**
     * @param newOrganisationService
     *            the sourceService to set
     */
    public void setOrganisationService(OrganisationService newOrganisationService) {
        this.organisationService = newOrganisationService;
    }

    public void setPhylogeneticTreeService(
			PhylogeneticTreeService phylogeneticTreeService) {
		this.phylogeneticTreeService = phylogeneticTreeService;
	}

	/**
    *
    */
    protected CustomObjectMapper() {
    }

    /**
    *
    */
    protected void init() {
    	logger.debug("init() called"); 
        CustomHandlerInstantiator handlerInstantiator = new CustomHandlerInstantiator();
        handlerInstantiator.setReferenceService(referenceService);
        handlerInstantiator.setTaxonService(taxonService);
        handlerInstantiator.setImageService(imageService);
        handlerInstantiator.setGroupService(groupService);
        handlerInstantiator.setUserService(userService);
        handlerInstantiator.setOrganisationService(organisationService);
        handlerInstantiator.setIdentificationKeyService(identificationKeyService);
        handlerInstantiator.setPhylogeneticTreeService(phylogeneticTreeService);
        setHandlerInstantiator(handlerInstantiator);
        CustomModule module = new CustomModule(jobInstanceService);
        registerModule(module);
        registerModule(new HibernateModule());
    }
}
