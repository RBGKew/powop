package org.emonocot.job.dwc.taxon;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.easymock.EasyMock;
import org.emonocot.api.TaxonService;
import org.emonocot.job.dwc.taxon.FieldSetMapper;
import org.emonocot.job.oaipmh.TaxonomicStatusConverter;
import org.emonocot.model.Taxon;
import org.emonocot.model.convert.RankConverter;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 *
 * @author ben
 *
 */
public class TaxonParsingTest {

   /**
    *
    */
   private Resource content = new ClassPathResource(
           "/org/emonocot/job/dwc/test/taxa.txt");

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
               "http://rs.tdwg.org/dwc/terms/scientificNameID",
               "http://rs.tdwg.org/dwc/terms/scientificNameAuthorship",
               "http://rs.tdwg.org/dwc/terms/taxonRank",
               "http://rs.tdwg.org/dwc/terms/taxonomicStatus",
               "http://rs.tdwg.org/dwc/terms/parentNameUsageID",
               "http://rs.tdwg.org/dwc/terms/acceptedNameUsageID",
               "http://rs.tdwg.org/dwc/terms/genus",
               "http://rs.tdwg.org/dwc/terms/subgenus",
               "http://rs.tdwg.org/dwc/terms/specificEpithet",
               "http://rs.tdwg.org/dwc/terms/infraspecificEpithet",
               "http://purl.org/dc/elements/1.1/identifier",
               "http://purl.org/dc/elements/1.1/modified",
               "http://purl.org/dc/elements/1.1/source"
       };
       DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
       tokenizer.setDelimiter('\t');
       tokenizer.setNames(names);

       taxonService = EasyMock.createMock(TaxonService.class);
       Set<Converter> converters = new HashSet<Converter>();
       converters.add(new TaxonomicStatusConverter());
       converters.add(new RankConverter());

       ConversionServiceFactoryBean factoryBean = new ConversionServiceFactoryBean();
       factoryBean.setConverters(converters);
       factoryBean.afterPropertiesSet();
       ConversionService conversionService = factoryBean.getObject();

        FieldSetMapper fieldSetMapper = new FieldSetMapper();
        fieldSetMapper.setFieldNames(names);
        fieldSetMapper.setDefaultValues(new HashMap<String, String>());
        fieldSetMapper.setTaxonService(taxonService);
        fieldSetMapper.setConversionService(conversionService);
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
