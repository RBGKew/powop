package org.emonocot.job.scratchpads.convert;

import static org.junit.Assert.assertNotNull;

import org.easymock.EasyMock;
import org.emonocot.job.scratchpads.model.EoLDataObject;
import org.emonocot.job.scratchpads.model.EoLReference;
import org.emonocot.job.scratchpads.model.EoLTaxonItem;
import org.emonocot.model.description.TextContent;
import org.emonocot.model.media.Image;
import org.emonocot.model.reference.Reference;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.service.TaxonService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.convert.converter.Converter;
import org.springframework.dao.DataRetrievalFailureException;

public class EoLTaxonItemConverterTest {
	
	private EoLTaxonItem taxonItem;
	private EoLTaxonItemConverter converter;
	private TaxonService taxonService;
	private Converter<EoLDataObject,TextContent> textDataConverter;
    private Converter<EoLDataObject,Image> imageConverter;
	private Converter<EoLReference,Reference> referenceConverter;
	
	@Before
	public void setUp() {
		converter = new EoLTaxonItemConverter();
		taxonService = EasyMock.createMock(TaxonService.class);
		textDataConverter = EasyMock.createMock(Converter.class);
		imageConverter = EasyMock.createMock(Converter.class);
		referenceConverter = EasyMock.createMock(Converter.class);

		converter.setTaxonService(taxonService);
		converter.setImageConverter(imageConverter);
		converter.setTextDataConverter(textDataConverter);
		converter.setReferenceConverter(referenceConverter);
	}

	@Test
	public void testConvertWithNewObject() {
		EasyMock.expect(taxonService.load(EasyMock.eq("identifier"))).andThrow(new DataRetrievalFailureException("Could not find object"));
		taxonItem = new EoLTaxonItem();
		taxonItem.setIdentifier("identifier");
		EasyMock.replay(taxonService,imageConverter,textDataConverter,referenceConverter);
		
		Taxon taxon = converter.convert(taxonItem);
		assertNotNull("Taxon should not be null",taxon);
		
		EasyMock.verify(taxonService,imageConverter,textDataConverter,referenceConverter);
	}

}
