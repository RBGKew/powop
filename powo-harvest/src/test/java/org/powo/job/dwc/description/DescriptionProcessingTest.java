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
package org.powo.job.dwc.description;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.SortedSet;

import com.google.common.collect.Sets;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.powo.api.DescriptionService;
import org.powo.api.OrganisationService;
import org.powo.api.ReferenceService;
import org.powo.api.TaxonService;
import org.powo.model.Description;
import org.powo.model.Taxon;
import org.powo.model.constants.DescriptionType;
import org.powo.model.registry.Organisation;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class DescriptionProcessingTest {

	private TaxonService taxonService;
	private ReferenceService referenceService;
	private DescriptionService descriptionService;

	private OrganisationService sourceService;

	private Organisation source = new Organisation();

	private Processor descriptionProcessor;

	@Before
	public void setUp() {
		taxonService = EasyMock.createMock(TaxonService.class);
		referenceService = EasyMock.createMock(ReferenceService.class);
		descriptionService = EasyMock.createMock(DescriptionService.class);
		sourceService = EasyMock.createMock(OrganisationService.class);

		LocalValidatorFactoryBean validatorFactory = new LocalValidatorFactoryBean();
		validatorFactory.afterPropertiesSet();

		descriptionProcessor = new Processor();
		descriptionProcessor.setValidator(validatorFactory.getValidator());
		descriptionProcessor.setTaxonService(taxonService);
		descriptionProcessor.setReferenceService(referenceService);
		descriptionProcessor.setDescriptionService(descriptionService);
		descriptionProcessor.setOrganisationService(sourceService);
		descriptionProcessor.setSourceName("test source");
		descriptionProcessor.beforeStep(new StepExecution("teststep", new JobExecution(1L)));

		source.setIdentifier("test source");
	}

	/**
	 * Test that the description processor links a new description to its associated taxon.
	 * 
	 * Also checks that the Organisation associated to the Processor is added to the taxon authorities.
	 */
	@Test
	public void testProcessNewDescription() throws Exception {
		var description = new Description();
		description.setIdentifier("desc:identifier");
		description.setTypes(Sets.newTreeSet(List.of(DescriptionType.use)));
		description.setDescription("This plant is used in a test");

		var prevAuthority = new Organisation();
		prevAuthority.setIdentifier("prev authority");

		var taxon = new Taxon();
		taxon.setId(0L);
		taxon.setIdentifier("taxon:identifier");
		taxon.setFamily("Araceae");
		taxon.setAuthorities(Sets.newHashSet(prevAuthority));

		description.setTaxon(taxon);

		EasyMock.expect(referenceService.find(EasyMock.isA(String.class))).andReturn(null).anyTimes();
		EasyMock.expect(taxonService.find(EasyMock.eq("taxon:identifier"))).andReturn(taxon).anyTimes();
		EasyMock.expect(sourceService.load(EasyMock.eq("test source"))).andReturn(source);
		EasyMock.replay(referenceService, sourceService, taxonService);
		descriptionProcessor.process(description);
		EasyMock.verify(referenceService, sourceService, taxonService);
		assertEquals(Sets.newHashSet(source, prevAuthority), taxon.getAuthorities());
	}

}
