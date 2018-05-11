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
package org.powo.job.common;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.powo.harvest.common.BOMIgnoringBufferedReaderFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.PassThroughFieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.core.io.ClassPathResource;


public class BOMIgnoringFlatFileItemReaderTest {

	private FlatFileItemReader<FieldSet> flatFileItemReader;

	@Before
	public void setUp() throws Exception {
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setDelimiter(DelimitedLineTokenizer.DELIMITER_TAB);
		lineTokenizer.setNames(new String[] {"foo","bar"});
		lineTokenizer.setStrict(false);

		DefaultLineMapper<FieldSet> lineMapper = new DefaultLineMapper<FieldSet>();
		lineMapper.setLineTokenizer(lineTokenizer);
		lineMapper.setFieldSetMapper(new PassThroughFieldSetMapper());

		flatFileItemReader = new FlatFileItemReader<FieldSet>();
		flatFileItemReader.setEncoding("UTF-8");
		flatFileItemReader.setLinesToSkip(0);
		flatFileItemReader.setLineMapper(lineMapper);
		flatFileItemReader.setResource(new ClassPathResource("org/emonocot/bom.txt"));
		flatFileItemReader.setBufferedReaderFactory(new BOMIgnoringBufferedReaderFactory());

		flatFileItemReader.afterPropertiesSet();
		flatFileItemReader.open(new ExecutionContext());
	}

	@Test
	public void test() throws Exception {
		FieldSet fieldSet = flatFileItemReader.read();
		assertEquals("There should be no Byte Order Mark", "urn:kew.org:wcs:family:1", fieldSet.getValues()[0]);
	}

}
