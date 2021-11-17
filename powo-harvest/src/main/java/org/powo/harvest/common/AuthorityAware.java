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
package org.powo.harvest.common;

import org.powo.api.OrganisationService;
import org.powo.model.Annotation;
import org.powo.model.Base;
import org.powo.model.constants.AnnotationCode;
import org.powo.model.constants.AnnotationType;
import org.powo.model.constants.RecordType;
import org.powo.model.registry.Organisation;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author ben
 *
 */
public class AuthorityAware implements StepExecutionListener {

	/**
	 *
	 */
	private StepExecution stepExecution;
	/**
	 *
	 */
	private OrganisationService organisationService;
	/**
	 *
	 */
	private Organisation source;
	/**
	 *
	 */
	private String sourceName;

	/**
	 *
	 * @param newSourceName Set the name of the Source
	 */
	public final void setSourceName(final String newSourceName) {
		this.sourceName = newSourceName;
	}

	/**
	 *
	 * @return the Source
	 */
	public final Organisation getSource() {
		if (source == null) {
			source = organisationService.load(sourceName);
		}
		return source;
	}

	/**
	 *
	 * @param newOrganisationService Set the source service
	 */
	@Autowired
	public final void setOrganisationService(final OrganisationService newOrganisationService) {
		this.organisationService = newOrganisationService;
	}

	/**
	 * @param newStepExecution Set the step exectuion
	 */
	public void beforeStep(final StepExecution newStepExecution) {
		this.stepExecution = newStepExecution;
	}

	/**
	 * @param newStepExecution Set the step execution
	 * @return the exit status
	 */
	public ExitStatus afterStep(final StepExecution newStepExecution) {
		return null;
	}

	/**
	 * @return the step execution
	 */
	public final StepExecution getStepExecution() {
		return stepExecution;
	}
}
