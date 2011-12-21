package org.emonocot.job.dwc;

import java.util.HashMap;

import org.easymock.EasyMock;
import org.emonocot.job.dwc.description.DescriptionFieldSetMapper;
import org.emonocot.model.description.TextContent;
import org.emonocot.model.reference.Reference;
import org.emonocot.model.taxon.Taxon;
import org.emonocot.service.impl.ReferenceServiceImpl;
import org.emonocot.api.ReferenceService;
import org.emonocot.api.TaxonService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 *
 * @author ben
 *
 */
public class DescriptionParsingTest {

   /**
    *
    */
   private Resource content = new ClassPathResource(
           "/org/emonocot/job/dwc/description.txt");

   /**
    *
    */
   private TaxonService taxonService = null;

   /**
    *
    */
   private ReferenceService referenceService = null;

   /**
    *
    */
    private FlatFileItemReader<TextContent> flatFileItemReader = new FlatFileItemReader<TextContent>();

   /**
    *
    */
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
               "http://purl.org/dc/terms/relation",
               "http://purl.org/dc/terms/relationID"
       };
       DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
       tokenizer.setDelimiter('\t');
       tokenizer.setNames(names);

       taxonService = EasyMock.createMock(TaxonService.class);
       referenceService = EasyMock.createMock(ReferenceService.class);

        DescriptionFieldSetMapper fieldSetMapper = new DescriptionFieldSetMapper();
        fieldSetMapper.setFieldNames(names);
        fieldSetMapper.setDefaultValues(new HashMap<String, String>());
        fieldSetMapper.setTaxonService(taxonService);
        fieldSetMapper.setReferenceService(referenceService);
        DefaultLineMapper<TextContent> lineMapper
            = new DefaultLineMapper<TextContent>();
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
        EasyMock.expect(referenceService.find(EasyMock.isA(String.class))).andReturn(new Reference()).anyTimes();
        EasyMock.replay(taxonService, referenceService);
        flatFileItemReader.open(new ExecutionContext());
        flatFileItemReader.read();

    }

}
