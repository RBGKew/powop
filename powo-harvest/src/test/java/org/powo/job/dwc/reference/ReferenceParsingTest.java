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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.powo.api.TaxonService;
import org.powo.job.dwc.reference.FieldSetMapper;
import org.powo.model.Reference;
import org.powo.model.Taxon;
import org.powo.model.convert.ReferenceTypeConverter;
import org.powo.model.convert.StringToIsoDateTimeConverter;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class ReferenceParsingTest {

	private Resource content = new ClassPathResource("/__files/dwc/reference.txt");

	private TaxonService taxonService = null;

	private FlatFileItemReader<Reference> flatFileItemReader = new FlatFileItemReader<>();

	@Before
	public final void setUp() throws Exception {

		String[] names = new String[] {
				"http://rs.tdwg.org/dwc/terms/taxonID",
				"http://purl.org/dc/terms/modified",
				"http://purl.org/dc/terms/created",
				"http://purl.org/dc/terms/identifier",
				"http://purl.org/dc/terms/bibliographicCitation",
				"http://purl.org/dc/terms/type",
				"http://purl.org/dc/terms/title",
				"http://purl.org/dc/terms/description",
				"http://purl.org/dc/terms/date",
				"http://purl.org/dc/terms/creator",
		};

		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setDelimiter(DelimitedLineTokenizer.DELIMITER_TAB);
		tokenizer.setNames(names);
		@SuppressWarnings("rawtypes")
		Set<Converter> converters = new HashSet<>();
		converters.add(new ReferenceTypeConverter());
		converters.add(new StringToIsoDateTimeConverter());

		ConversionServiceFactoryBean factoryBean = new ConversionServiceFactoryBean();
		factoryBean.setConverters(converters);
		factoryBean.afterPropertiesSet();
		ConversionService conversionService = factoryBean.getObject();

		taxonService = EasyMock.createMock(TaxonService.class);

		FieldSetMapper fieldSetMapper = new FieldSetMapper();
		fieldSetMapper.setFieldNames(names);
		fieldSetMapper.setConversionService(conversionService);
		fieldSetMapper.setDefaultValues(new HashMap<>());
		fieldSetMapper.setTaxonService(taxonService);
		DefaultLineMapper<Reference> lineMapper = new DefaultLineMapper<>();
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
		EasyMock.expect(taxonService.find(EasyMock.isA(String.class))).andReturn(new Taxon()).anyTimes();
		EasyMock.replay(taxonService);
		flatFileItemReader.open(new ExecutionContext());
		flatFileItemReader.read();
	}
}
