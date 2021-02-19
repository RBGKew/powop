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

import static org.junit.Assert.assertEquals;

import com.google.common.collect.Sets;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.powo.api.OrganisationService;
import org.powo.api.ReferenceService;
import org.powo.api.TaxonService;
import org.powo.model.Reference;
import org.powo.model.Taxon;
import org.powo.model.constants.ReferenceType;
import org.powo.model.registry.Organisation;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class ReferenceProcessingTest {

	private ReferenceService referenceService;

	private TaxonService taxonService;

	private OrganisationService sourceService;

	private Organisation source = new Organisation();

	private Processor referenceValidator;

	@Before
	public void setUp() {
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
		referenceValidator.beforeStep(new StepExecution("teststep", new JobExecution(1L)));

		source.setIdentifier("test source");
	}

	/**
	 * Test that the reference processor links a new reference to its associated taxon, and stores the reference in
	 * its bound objects.
	 */
	@Test
	public void testProcessNewReference() throws Exception {
		var reference = new Reference();

		var taxon = new Taxon();
		taxon.setId(0L);
		taxon.setIdentifier("identifier");
		taxon.setFamily("Araceae");

		reference.getTaxa().add(taxon);
		reference.setType(ReferenceType.publication);
		reference.setIdentifier("http://build.e-monocot.org/test/test.pdf");

		EasyMock.expect(referenceService.find(EasyMock.isA(String.class))).andReturn(null).anyTimes();
		EasyMock.expect(taxonService.findPersisted(EasyMock.eq("identifier"))).andReturn(taxon).anyTimes();
		EasyMock.expect(sourceService.load(EasyMock.eq("test source"))).andReturn(source);
		EasyMock.replay(referenceService, sourceService, taxonService);
		referenceValidator.doProcess(reference);
		EasyMock.verify(referenceService, sourceService, taxonService);
		assertEquals(Sets.newHashSet(taxon), reference.getTaxa());
		assertEquals(referenceValidator.lookupBound(reference), reference);
	}

	/**
	 * Test that the reference processor adds additional taxa to a reference that is "bound", i.e. has already appeared in the current
	 * batch of records being processed.
	 */
	@Test
	public void testProcessBoundReferenceWithNewTaxon() throws Exception {
		var existingReference = new Reference();

		var taxon = new Taxon();
		taxon.setId(0L);
		taxon.setIdentifier("identifier");
		taxon.setFamily("Araceae");

		existingReference.getTaxa().add(taxon);
		existingReference.setType(ReferenceType.publication);
		existingReference.setIdentifier("http://build.e-monocot.org/test/test.pdf");
		existingReference.setAuthority(source);

		var newReference = new Reference();

		var newTaxon = new Taxon();
		newTaxon.setId(1L);
		newTaxon.setIdentifier("newidentifier");
		newTaxon.setFamily("Araceae");

		newReference.getTaxa().add(newTaxon);
		newReference.setType(ReferenceType.publication);
		newReference.setIdentifier("http://build.e-monocot.org/test/test.pdf");

		referenceValidator.bind(existingReference);

		EasyMock.expect(taxonService.findPersisted(EasyMock.eq("identifier"))).andReturn(taxon).anyTimes();
		EasyMock.expect(taxonService.findPersisted(EasyMock.eq("newidentifier"))).andReturn(newTaxon).anyTimes();
		EasyMock.replay(referenceService, sourceService, taxonService);
		referenceValidator.doProcess(newReference);
		EasyMock.verify(referenceService, sourceService, taxonService);
		assertEquals(Sets.newHashSet(taxon, newTaxon), existingReference.getTaxa());
	}

	/**
	 * Test that the reference processor adds additional taxa to a reference that is "persisted", i.e. already exists in the database.
	 */
	@Test
	public void testProcessPersistedReferenceWithNewTaxon() throws Exception {
		var existingReference = new Reference();

		var taxon = new Taxon();
		taxon.setId(0L);
		taxon.setIdentifier("identifier");
		taxon.setFamily("Araceae");

		existingReference.getTaxa().add(taxon);
		existingReference.setType(ReferenceType.publication);
		existingReference.setIdentifier("http://build.e-monocot.org/test/test.pdf");
		existingReference.setAuthority(source);

		var newReference = new Reference();

		var newTaxon = new Taxon();
		newTaxon.setId(1L);
		newTaxon.setIdentifier("newidentifier");
		newTaxon.setFamily("Araceae");

		newReference.getTaxa().add(newTaxon);
		newReference.setType(ReferenceType.publication);
		newReference.setIdentifier("http://build.e-monocot.org/test/test.pdf");

		EasyMock.expect(referenceService.find(EasyMock.isA(String.class))).andReturn(existingReference).anyTimes();
		EasyMock.expect(taxonService.findPersisted(EasyMock.eq("identifier"))).andReturn(taxon).anyTimes();
		EasyMock.expect(taxonService.findPersisted(EasyMock.eq("newidentifier"))).andReturn(newTaxon).anyTimes();
		EasyMock.expect(sourceService.load(EasyMock.eq("test source"))).andReturn(source);
		EasyMock.replay(referenceService, sourceService, taxonService);
		referenceValidator.doProcess(newReference);
		EasyMock.verify(referenceService, sourceService, taxonService);
		assertEquals(Sets.newHashSet(taxon, newTaxon), existingReference.getTaxa());
	}

}
