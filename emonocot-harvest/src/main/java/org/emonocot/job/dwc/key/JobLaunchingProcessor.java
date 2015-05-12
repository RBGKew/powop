/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
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
import org.emonocot.model.Multimedia;
import org.emonocot.model.constants.ResourceType;
import org.emonocot.model.registry.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;

public class JobLaunchingProcessor extends AuthorityAware implements ItemProcessor<Multimedia, IdentificationKey> {
	
	private static Logger logger = LoggerFactory.getLogger(JobLaunchingProcessor.class);
	
		
	private ResourceService resourceService;
	
	private ConversionService conversionService;
	
	private ItemProcessor<IdentificationKey,IdentificationKey> processor;
	
	@Autowired
	public void setResourceService(ResourceService resourceService) {
		this.resourceService = resourceService;
	}
	
	@Autowired
	public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    public void setProcessor(ItemProcessor<IdentificationKey,IdentificationKey> processor) {
		this.processor = processor;
	}

	@Override
	public IdentificationKey process(Multimedia item) throws Exception {
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

	private IdentificationKey doProcess(Multimedia item) throws Exception {
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
					identificationKey = conversionService.convert(item, IdentificationKey.class);
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
			identificationKey = conversionService.convert(item, IdentificationKey.class);
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
}