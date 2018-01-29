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
package org.powo.job.dwc.reference;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.powo.api.OrganisationService;
import org.powo.api.ReferenceService;
import org.powo.api.TaxonService;
import org.powo.job.dwc.reference.Processor;
import org.powo.model.Reference;
import org.powo.model.Taxon;
import org.powo.model.constants.ReferenceType;
import org.powo.model.registry.Organisation;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 *
 * @author ben
 *
 */
public class ReferenceProcessingTest {

	private Reference reference;

	private ReferenceService referenceService;

	private TaxonService taxonService;

	private OrganisationService sourceService;

	private Taxon taxon;

	private Organisation source = new Organisation();

	private Processor referenceValidator;


	@Before
	public void setUp() {
		reference = new Reference();
		taxon = new Taxon();
		taxon.setId(0L);
		taxon.setIdentifier("identifier");
		taxon.setFamily("Araceae");
		reference.getTaxa().add(taxon);
		reference.setType(ReferenceType.publication);
		reference.setIdentifier("http://build.e-monocot.org/test/test.pdf");
		referenceService = EasyMock.createMock(ReferenceService.class);
		taxonService = EasyMock.createMock(TaxonService.class);
		sourceService = EasyMock.createMock(OrganisationService.class);

		LocalValidatorFactoryBean validatorFactory = new LocalValidatorFactoryBean();
		validatorFactory.afterPropertiesSet();

		referenceValidator = new Processor();
		referenceValidator.setValidator(validatorFactory.getValidator());
		referenceValidator.setReferenceService(referenceService);
		referenceValidator.setOrganisationService(sourceService);
		referenceValidator.setTaxonService(taxonService);
		referenceValidator.setSourceName("test source");
		referenceValidator.setFamily("Araceae");
		referenceValidator.beforeStep(new StepExecution("teststep",
				new JobExecution(1L)));
	}
	/**
	 * @throws Exception if there is a problem
	 */
	@Test
	public void testProcessReference() throws Exception {
		EasyMock.expect(referenceService.find(EasyMock.isA(String.class)))
		.andReturn(null).anyTimes();
		EasyMock.expect(taxonService.find(EasyMock.eq("identifier"))).andReturn(taxon).anyTimes();
		EasyMock.expect(sourceService.load(EasyMock.eq("test source")))
		.andReturn(source);
		EasyMock.replay(referenceService, sourceService,taxonService);
		referenceValidator.process(reference);
		EasyMock.verify(referenceService, sourceService,taxonService);
	}

}
