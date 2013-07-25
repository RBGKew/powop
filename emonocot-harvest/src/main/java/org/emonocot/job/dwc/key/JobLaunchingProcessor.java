package org.emonocot.job.dwc.key;

import java.net.URL;
import java.util.UUID;

import org.apache.tika.Tika;
import org.emonocot.api.ResourceService;
import org.emonocot.api.job.ResourceAlreadyBeingHarvestedException;
import org.emonocot.harvest.common.AuthorityAware;
import org.emonocot.job.dwc.exception.ImageRetrievalException;
import org.emonocot.job.dwc.exception.NoIdentifierException;
import org.emonocot.model.IdentificationKey;
import org.emonocot.model.Image;
import org.emonocot.model.constants.ResourceType;
import org.emonocot.model.registry.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

public class JobLaunchingProcessor extends AuthorityAware implements ItemProcessor<Image, IdentificationKey> {
	
	private static Logger logger = LoggerFactory.getLogger(JobLaunchingProcessor.class);
	
		
	private ResourceService resourceService;
	
	private ItemProcessor<IdentificationKey,IdentificationKey> processor;
	
	@Autowired
	public void setResourceService(ResourceService resourceService) {
		this.resourceService = resourceService;
	}
	
	public void setProcessor(ItemProcessor<IdentificationKey,IdentificationKey> processor) {
		this.processor = processor;
	}

	@Override
	public IdentificationKey process(Image item) throws Exception {
		IdentificationKey key = null;
		if(item.getIdentifier() == null || item.getIdentifier().isEmpty()) {
			throw new NoIdentifierException(item);
		}
		
		if(item.getFormat() == null) {
			key = doProcess(item);
		} else {
		    switch(item.getFormat()) {
		    case xml:
		      key = doProcess(item);
		    default:
		    	break;		    
		    }
		}
		return key;
	}

	private IdentificationKey doProcess(Image item) throws Exception {
		logger.debug("doProcess " + item);
		Resource resource = resourceService.findByResourceUri(item.getIdentifier());
		IdentificationKey identificationKey = null;
		if(resource == null) {
			logger.debug("No Resource prexisting for " + item.getIdentifier());
			Tika tika = new Tika();
			try {
				
				String mimeType = tika.detect(new URL(item.getIdentifier()));
				logger.debug("Mime type is " + mimeType);
				if(mimeType.equals("application/sdd+xml")) {
					identificationKey = mapIdentificationKey(item);
					resource = new Resource();
					resource.setOrganisation(getSource());
					resource.setIdentifier(UUID.randomUUID().toString());
					resource.setUri(item.getIdentifier());
					resource.setResourceType(ResourceType.IDENTIFICATION_KEY);
					resource.setTitle("Resource " + item.getIdentifier());
					resourceService.saveOrUpdate(resource);
				} else {
					logger.debug("Returning null");
					return null;
				}
			} catch (Exception e) {
				throw new ImageRetrievalException(item.getIdentifier());
			}
		} else if(resource.getResourceType().equals(ResourceType.IDENTIFICATION_KEY)) {
			logger.debug("Resource " + resource + " exists for " + item.getIdentifier());
			identificationKey = mapIdentificationKey(item);
		} else {
			return null;
		}
		logger.debug("Processing " + identificationKey);
		identificationKey = processor.process(identificationKey);
		
		if(identificationKey != null) {
			try {
    			resourceService.harvestResource(resource.getId(), true);
			} catch(ResourceAlreadyBeingHarvestedException rabhe) {
				logger.warn("Tried to harvest " + item.getIdentifier() + " but it is already being harvested");
			}
		}		
		
		return identificationKey;
	}

	private IdentificationKey mapIdentificationKey(Image image) {
		IdentificationKey identificationKey = new IdentificationKey();
		identificationKey.setAccessRights(image.getAccessRights());
		identificationKey.setCreated(image.getCreated());
		identificationKey.setCreator(image.getCreator());
		identificationKey.setDescription(image.getDescription());
		identificationKey.setIdentifier(image.getIdentifier());
		identificationKey.setLicense(image.getLicense());
		identificationKey.setModified(image.getModified());
		identificationKey.setRights(image.getRights());
		identificationKey.setRightsHolder(image.getRightsHolder());
		identificationKey.setTaxa(image.getTaxa());
		identificationKey.setTitle(image.getTitle());
		return identificationKey;
	}
}