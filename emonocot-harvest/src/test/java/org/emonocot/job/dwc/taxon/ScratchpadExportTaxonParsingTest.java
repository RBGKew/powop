package org.emonocot.job.dwc.taxon;

import java.util.HashMap;

import org.easymock.EasyMock;
import org.emonocot.api.TaxonService;
import org.emonocot.job.dwc.taxon.FieldSetMapper;
import org.emonocot.model.taxon.Taxon;
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
public class ScratchpadExportTaxonParsingTest {

   /**
    *
    */
   private Resource content = new ClassPathResource(
           "/org/emonocot/zingiberaceae/zingiberaceae/classification.txt");

   /**
    *
    */
   private TaxonService taxonService = null;

   /**
    *
    */
    private FlatFileItemReader<Taxon> flatFileItemReader = new FlatFileItemReader<Taxon>();

   /**
    *
    */
   @Before
   public final void setUp() throws Exception {
       String[] names = new String[] {
               "http://rs.tdwg.org/dwc/terms/taxonID",
               "http://rs.tdwg.org/dwc/terms/scientificName",
               "http://rs.tdwg.org/dwc/terms/vernacularName",
               "http://rs.tdwg.org/dwc/terms/taxonomicStatus",
               "http://rs.tdwg.org/dwc/terms/taxonRank",
               "http://rs.tdwg.org/dwc/terms/scientificNameAuthorship",
               "http://purl.org/dc/terms/bibliographicCitation",
               "http://rs.tdwg.org/dwc/terms/parentNameUsageID"
       };
       DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
       tokenizer.setDelimiter(',');
       tokenizer.setQuoteCharacter('\"');
       tokenizer.setNames(names);

       taxonService = EasyMock.createMock(TaxonService.class);

        FieldSetMapper fieldSetMapper = new FieldSetMapper();
        fieldSetMapper.setFieldNames(names);
        fieldSetMapper.setDefaultValues(new HashMap<String, String>());
        fieldSetMapper.setTaxonService(taxonService);
        DefaultLineMapper<Taxon> lineMapper
            = new DefaultLineMapper<Taxon>();
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
