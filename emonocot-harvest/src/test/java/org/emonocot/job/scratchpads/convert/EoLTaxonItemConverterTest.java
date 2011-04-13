package org.emonocot.job.scratchpads.convert;

import static org.junit.Assert.*;

import org.easymock.EasyMock;
import org.emonocot.job.scratchpads.model.EoLTaxonItem;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.service.TaxonService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.convert.ConversionService;
import org.springframework.dao.DataRetrievalFailureException;

public class EoLTaxonItemConverterTest {
	
	private EoLTaxonItem taxonItem;
	private EoLTaxonItemConverter converter;
	private TaxonService taxonService;
	private ConversionService conversionService;
	
	@Before
	public void setUp() {
		converter = new EoLTaxonItemConverter();
		taxonService = EasyMock.createMock(TaxonService.class);
		conversionService = EasyMock.createMock(ConversionService.class);
		converter.setTaxonService(taxonService);
		converter.setConversionService(conversionService);
	}

	@Test
	public void testConvertWithNewObject() {
		EasyMock.expect(taxonService.load(EasyMock.eq("identifier"))).andThrow(new DataRetrievalFailureException("Could not find object"));
		taxonItem = new EoLTaxonItem();
		taxonItem.setIdentifier("identifier");
		EasyMock.replay(taxonService,conversionService);
		
		Taxon taxon = converter.convert(taxonItem);
		assertNotNull("Taxon should not be null",taxon);
		
		EasyMock.verify(taxonService,conversionService);
	}

}
