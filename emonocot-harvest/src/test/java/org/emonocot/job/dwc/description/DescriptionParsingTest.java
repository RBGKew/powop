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
package org.emonocot.job.dwc.description;

import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeSet;

import static org.easymock.EasyMock.*;
import org.emonocot.api.TaxonService;
import org.emonocot.harvest.common.HtmlSanitizer;
import org.emonocot.model.Description;
import org.emonocot.model.Taxon;
import org.emonocot.model.constants.DescriptionType;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class DescriptionParsingTest {

	private Resource content = new ClassPathResource("/org/emonocot/job/dwc/description.txt");

	private TaxonService taxonService = null;

	private ConversionService conversionService = null;

	private FlatFileItemReader<Description> flatFileItemReader = new FlatFileItemReader<Description>();

	@Before
	public final void setUp() throws Exception {
		String[] names = new String[] {
				"http://rs.tdwg.org/dwc/terms/taxonID",
				"http://purl.org/dc/terms/type",
				"http://purl.org/dc/elements/1.1/source",
				"http://purl.org/dc/terms/modified",
				"http://purl.org/dc/terms/create",
				"http://purl.org/dc/terms/creator",
				"http://purl.org/dc/terms/description",
				"http://purl.org/dc/terms/references",
				"http://purl.org/dc/terms/source"
		};
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setDelimiter('\t');
		tokenizer.setNames(names);

		taxonService = createMock(TaxonService.class);
		conversionService = createMock(ConversionService.class);

		FieldSetMapper fieldSetMapper = new FieldSetMapper();
		fieldSetMapper.setConversionService(conversionService);
		HtmlSanitizer htmlSanitizer = new HtmlSanitizer();
		htmlSanitizer.afterPropertiesSet();
		fieldSetMapper.setHtmlSanitizer(htmlSanitizer);
		fieldSetMapper.setFieldNames(names);
		fieldSetMapper.setDefaultValues(new HashMap<String, String>());
		fieldSetMapper.setTaxonService(taxonService);
		DefaultLineMapper<Description> lineMapper = new DefaultLineMapper<Description>();
		lineMapper.setFieldSetMapper(fieldSetMapper);
		lineMapper.setLineTokenizer(tokenizer);

		flatFileItemReader.setEncoding("UTF-8");
		flatFileItemReader.setLinesToSkip(0);
		flatFileItemReader.setResource(content);
		flatFileItemReader.setLineMapper(lineMapper);
		flatFileItemReader.afterPropertiesSet();
	}

	/**
	 * @throws Exception if there is a problem accessing the file
	 */
	@Test
	public final void testRead() throws Exception {
		expect(conversionService.convert(isA(String.class), isA(TypeDescriptor.class), isA(TypeDescriptor.class))).andReturn(new TreeSet<>(Arrays.asList(DescriptionType.general)));
		expect(conversionService.convert(isA(String.class), eq(DateTime.class))).andReturn(new DateTime());
		expect(taxonService.find(isA(String.class))).andReturn(new Taxon()).anyTimes();
		expect(taxonService.find(isA(String.class), eq("taxon-with-content"))).andReturn(new Taxon()).anyTimes();
		replay(taxonService,conversionService);
		flatFileItemReader.open(new ExecutionContext());
		flatFileItemReader.read();
	}
}
