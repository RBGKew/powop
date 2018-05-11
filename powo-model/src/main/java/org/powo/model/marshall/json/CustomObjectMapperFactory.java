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
package org.powo.model.marshall.json;

import org.powo.api.ConceptService;
import org.powo.api.ImageService;
import org.powo.api.JobInstanceService;
import org.powo.api.OrganisationService;
import org.powo.api.ReferenceService;
import org.powo.api.TaxonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomObjectMapperFactory implements FactoryBean<ObjectMapper> {
	Logger logger = LoggerFactory.getLogger(CustomObjectMapperFactory.class);

	private ReferenceService referenceService;

	private TaxonService taxonService;

	private ImageService imageService;

	private OrganisationService organisationService;

	private CustomObjectMapper objectMapper;

	private JobInstanceService jobInstanceService;

	private ConceptService conceptService;

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
			objectMapper.setOrganisationService(organisationService);
			objectMapper.setJobInstanceService(jobInstanceService);
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
