package org.emonocot.job.dwc.phylo;

import java.net.URL;
import java.util.UUID;

import org.apache.tika.Tika;
import org.emonocot.api.ResourceService;
import org.emonocot.api.job.ResourceAlreadyBeingHarvestedException;
import org.emonocot.harvest.common.AuthorityAware;
import org.emonocot.job.dwc.exception.ImageRetrievalException;
import org.emonocot.job.dwc.exception.NoIdentifierException;
import org.emonocot.model.PhylogeneticTree;
import org.emonocot.model.Image;
import org.emonocot.model.PhylogeneticTree;
import org.emonocot.model.constants.ResourceType;
import org.emonocot.model.registry.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

public class JobLaunchingProcessor extends AuthorityAware implements ItemProcessor<Image, PhylogeneticTree> {
	
	private static Logger logger = LoggerFactory.getLogger(JobLaunchingProcessor.class);
	
		
	private ResourceService resourceService;
	
	private ItemProcessor<PhylogeneticTree,PhylogeneticTree> processor;
	
	@Autowired
	public void setResourceService(ResourceService resourceService) {
		this.resourceService = resourceService;
	}
	
	public void setProcessor(ItemProcessor<PhylogeneticTree,PhylogeneticTree> processor) {
		this.processor = processor;
	}

	@Override
	public PhylogeneticTree process(Image item) throws Exception {
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
		    	break;		    
		    }
		}
		return phylogeny;
	}

	private PhylogeneticTree doProcess(Image item) throws Exception {
		logger.debug("doProcess " + item);
		Resource resource = resourceService.findByResourceUri(item.getIdentifier());
		PhylogeneticTree PhylogeneticTree = null;
		if(resource == null) {
			logger.debug("No Resource prexisting for " + item.getIdentifier());
			Tika tika = new Tika();
			try {
				
				String mimeType = tika.detect(new URL(item.getIdentifier()));
				logger.debug("Mime type is " + mimeType);
				PhylogeneticTree = mapPhylogeneticTree(item);
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
				throw new ImageRetrievalException(item.getIdentifier());
			}
		} else if(resource.getResourceType().equals(ResourceType.PHYLOGENETIC_TREE)) {
			logger.debug("Resource " + resource + " exists for " + item.getIdentifier());
			PhylogeneticTree = mapPhylogeneticTree(item);
		} else {
			return null;
		}
		logger.debug("Processing " + PhylogeneticTree);
		PhylogeneticTree = processor.process(PhylogeneticTree);
		
		if(PhylogeneticTree != null) {
			try {
    			resourceService.harvestResource(resource.getId(), true);
			} catch(ResourceAlreadyBeingHarvestedException rabhe) {
				logger.warn("Tried to harvest " + item.getIdentifier() + " but it is already being harvested");
			}
		}		
		
		return PhylogeneticTree;
	}

	private PhylogeneticTree mapPhylogeneticTree(Image image) {
		PhylogeneticTree PhylogeneticTree = new PhylogeneticTree();
		PhylogeneticTree.setAccessRights(image.getAccessRights());
		PhylogeneticTree.setCreated(image.getCreated());
		PhylogeneticTree.setCreator(image.getCreator());
		PhylogeneticTree.setDescription(image.getDescription());
		PhylogeneticTree.setIdentifier(image.getIdentifier());
		PhylogeneticTree.setLicense(image.getLicense());
		PhylogeneticTree.setModified(image.getModified());
		PhylogeneticTree.setRights(image.getRights());
		PhylogeneticTree.setRightsHolder(image.getRightsHolder());
		PhylogeneticTree.setTaxa(image.getTaxa());
		PhylogeneticTree.setTitle(image.getTitle());
		return PhylogeneticTree;
	}
}