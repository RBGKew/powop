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

import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeSet;

import static org.easymock.EasyMock.*;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.powo.api.TaxonService;
import org.powo.job.dwc.description.FieldSetMapper;
import org.powo.model.Description;
import org.powo.model.Taxon;
import org.powo.model.constants.DescriptionType;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class DescriptionParsingTest {

	private Resource content = new ClassPathResource("/__files/dwc/description.txt");

	private ConversionService conversionService = null;

	private FlatFileItemReader<Description> flatFileItemReader = new FlatFileItemReader<Description>();

	@Before
	public final void setUp() throws Exception {
		String[] names = new String[] {
				"http://rs.tdwg.org/dwc/terms/taxonID",
				"http://purl.org/dc/terms/created",
				"http://purl.org/dc/terms/modified",
				"http://purl.org/dc/terms/description",
				"http://purl.org/dc/terms/type",
				"http://purl.org/dc/terms/references",
				"http://purl.org/dc/terms/identifier",
		};
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setDelimiter(DelimitedLineTokenizer.DELIMITER_TAB);
		tokenizer.setNames(names);

		conversionService = createMock(ConversionService.class);

		FieldSetMapper fieldSetMapper = new FieldSetMapper();
		fieldSetMapper.setConversionService(conversionService);
		fieldSetMapper.setFieldNames(names);
		fieldSetMapper.setDefaultValues(new HashMap<String, String>());
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
		expect(conversionService.convert(isA(String.class), eq(DateTime.class))).andReturn(new DateTime()).anyTimes();
		flatFileItemReader.open(new ExecutionContext());
		flatFileItemReader.read();
	}
}
