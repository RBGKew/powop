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

import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomObjectMapper extends ObjectMapper {

	private static final long serialVersionUID = 7839832006798715918L;

	Logger logger = LoggerFactory.getLogger(CustomObjectMapper.class);

	private ReferenceService referenceService;

	private TaxonService taxonService;

	private ImageService imageService;

	private OrganisationService organisationService;

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

	public void setJobInstanceService(JobInstanceService jobInstanceService) {
		this.jobInstanceService = jobInstanceService;
	}

	public void setOrganisationService(OrganisationService organisationService) {
		this.organisationService = organisationService;
	}

	public void setConceptService(ConceptService conceptService) {
		this.conceptService = conceptService;
	}

	 protected CustomObjectMapper() {
	}

	 protected void init() {
		logger.debug("init() called");
		CustomHandlerInstantiator handlerInstantiator = new CustomHandlerInstantiator();
		handlerInstantiator.setReferenceService(referenceService);
		handlerInstantiator.setTaxonService(taxonService);
		handlerInstantiator.setImageService(imageService);
		handlerInstantiator.setOrganisationService(organisationService);
		handlerInstantiator.setConceptService(conceptService);
		setHandlerInstantiator(handlerInstantiator);
	 }
}
