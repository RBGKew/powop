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
package org.emonocot.job.dwc.phylo;

import java.net.URL;
import java.util.UUID;

import org.apache.tika.Tika;
import org.emonocot.api.ResourceService;
import org.emonocot.api.job.ResourceAlreadyBeingHarvestedException;
import org.emonocot.harvest.common.AuthorityAware;
import org.emonocot.job.dwc.exception.ImageRetrievalException;
import org.emonocot.job.dwc.exception.NoIdentifierException;
import org.emonocot.model.Multimedia;
import org.emonocot.model.PhylogeneticTree;
import org.emonocot.model.constants.ResourceType;
import org.emonocot.model.registry.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;

public class JobLaunchingProcessor extends AuthorityAware implements ItemProcessor<Multimedia, PhylogeneticTree> {
	
	private static Logger logger = LoggerFactory.getLogger(JobLaunchingProcessor.class);
	
		
	private ResourceService resourceService;
	
	private ConversionService conversionService;
	
	private ItemProcessor<PhylogeneticTree,PhylogeneticTree> processor;
	
	@Autowired
	public void setResourceService(ResourceService resourceService) {
		this.resourceService = resourceService;
	}
	
	@Autowired
	public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    public void setProcessor(ItemProcessor<PhylogeneticTree,PhylogeneticTree> processor) {
		this.processor = processor;
	}

	@Override
	public PhylogeneticTree process(Multimedia item) throws Exception {
		PhylogeneticTree phylogeny = null;
		if(item.getIdentifier() == null || item.getIdentifier().isEmpty()) {
			throw new NoIdentifierException(item);
		}
		
		if(item.getFormat() == null) {
			phylogeny = doProcess(item);
		} else {
		    switch(item.getFormat()) {
		    case xml:
		    case txt:
		      phylogeny = doProcess(item);
		    default:
		        logger.info(item.getFormat() + " is not a recognised format for a PhylogeneticTree");
		    	break;
		    }
		}
		return phylogeny;
	}

	private PhylogeneticTree doProcess(Multimedia item) throws Exception {
		logger.debug("doProcess " + item);
		Resource resource = resourceService.findByResourceUri(item.getIdentifier());
		PhylogeneticTree phylogeneticTree = null;
		if(resource == null) {
			logger.debug("No Resource prexisting for " + item.getIdentifier());
			Tika tika = new Tika();
			try {
				String mimeType = tika.detect(new URL(item.getIdentifier()));
				logger.debug("Mime type is " + mimeType);
	            phylogeneticTree = conversionService.convert(item, PhylogeneticTree.class);
				resource = new Resource();
				resource.setOrganisation(getSource());
				resource.setIdentifier(UUID.randomUUID().toString());
				resource.setUri(item.getIdentifier());
				resource.setResourceType(ResourceType.PHYLOGENETIC_TREE);
				resource.setTitle("Resource " + item.getIdentifier());
				if(mimeType.equals("application/phyloxml+xml")) {
					resource.getParameters().put("input.file.extension", "xml");
					resourceService.saveOrUpdate(resource);
				} else if(mimeType.equals("application/newick")) {
					resource.getParameters().put("input.file.extension", "nwk");
					resourceService.saveOrUpdate(resource);
				} else if(mimeType.equals("application/nexus")) {
					resource.getParameters().put("input.file.extension", "nex");
					resourceService.saveOrUpdate(resource);
				} else if(mimeType.equals("application/new-hampshire-extended")) {
					resource.getParameters().put("input.file.extension", "nhx");
					resourceService.saveOrUpdate(resource);
				} else {
					logger.debug("Returning null");
					return null;
				}
			} catch (Exception e) {
			    ImageRetrievalException ire = new ImageRetrievalException(item.getIdentifier());
			    ire.initCause(e);
				throw ire;
			}
		} else if(resource.getResourceType().equals(ResourceType.PHYLOGENETIC_TREE)) {
			logger.debug("Resource " + resource + " exists for " + item.getIdentifier());
			phylogeneticTree = conversionService.convert(item, PhylogeneticTree.class);
		} else {
			return null;
		}
		logger.debug("Processing " + phylogeneticTree);
		phylogeneticTree = processor.process(phylogeneticTree);
        logger.debug("Processing delegate returned " + phylogeneticTree);
		
		if(phylogeneticTree != null) {
			try {
    			resourceService.harvestResource(resource.getId(), true);
			} catch(ResourceAlreadyBeingHarvestedException rabhe) {
				logger.warn("Tried to harvest " + item.getIdentifier() + " but it is already being harvested");
			}
		}		
		
		return phylogeneticTree;
	}
}