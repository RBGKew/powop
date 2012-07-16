package org.emonocot.job.common;

import static org.junit.Assert.assertEquals;

import org.emonocot.harvest.common.BOMIgnoringBufferedReaderFactory;
import org.junit.Before;
import org.junit.Test;
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
		lineTokenizer.setDelimiter('\t');
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
