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
package org.emonocot.model.marshall.json;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.emonocot.api.ConceptService;
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
import org.springframework.beans.factory.FactoryBean;

/**
 *
 * @author ben
 *
 */
public class CustomObjectMapperFactory implements FactoryBean<ObjectMapper> {
	Logger logger = LoggerFactory.getLogger(CustomObjectMapperFactory.class);

	private ReferenceService referenceService;

	private TaxonService taxonService;

	private ImageService imageService;

	private UserService userService;

	private GroupService groupService;

	private OrganisationService organisationService;

	private CustomObjectMapper objectMapper;

	private JobInstanceService jobInstanceService;

	private IdentificationKeyService identificationKeyService;

	private PhylogeneticTreeService phylogeneticTreeService;

	private ConceptService conceptService;

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}

	public void setReferenceService(ReferenceService referenceService) {
		this.referenceService = referenceService;
	}

	public void setTaxonService(TaxonService taxonService) {
		this.taxonService = taxonService;
	}

	public void setImageService(ImageService imageService) {
		this.imageService = imageService;
	}

	public void setOrganisationService(OrganisationService organisationService) {
		this.organisationService = organisationService;
	}

	public void setJobInstanceService(JobInstanceService jobInstanceService) {
		this.jobInstanceService = jobInstanceService;
	}

	public void setIdentificationKeyService(IdentificationKeyService identificationKeyService) {
		this.identificationKeyService = identificationKeyService;
	}

	public void setPhylogeneticTreeService(PhylogeneticTreeService phylogeneticTreeService) {
		this.phylogeneticTreeService = phylogeneticTreeService;
	}

	public void setConceptService(ConceptService conceptService) {
		this.conceptService = conceptService;
	}

	/**
	 * @return the object created by this factory
	 */
	 public ObjectMapper getObject() {
		logger.debug("getObject called");
		if (objectMapper == null) {
			logger.debug("creating new CustomObjectMapper");
			objectMapper = new CustomObjectMapper();
			objectMapper.setTaxonService(taxonService);
			objectMapper.setReferenceService(referenceService);
			objectMapper.setImageService(imageService);
			objectMapper.setUserService(userService);
			objectMapper.setGroupService(groupService);
			objectMapper.setOrganisationService(organisationService);
			objectMapper.setJobInstanceService(jobInstanceService);
			objectMapper.setIdentificationKeyService(identificationKeyService);
			objectMapper.setPhylogeneticTreeService(phylogeneticTreeService);
			objectMapper.setConceptService(conceptService);
			objectMapper.init();
		}
		logger.debug("Returning objectMapper " + objectMapper);
		return objectMapper;
	 }

	 /**
	  * @return the type of object created by this factory
	  */
	 public Class<?> getObjectType() {
		 return ObjectMapper.class;
	 }

	 /**
	  * @return true, if this object is a singleton, false otherwise
	  */
	 public boolean isSingleton() {
		 return true;
	 }
}
