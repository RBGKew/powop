package org.emonocot.checklist.view.assembler;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.net.URISyntaxException;

import org.dozer.Mapper;
import org.dozer.spring.DozerBeanMapperFactoryBean;
import org.emonocot.checklist.model.Distribution;
import org.emonocot.checklist.model.Taxon;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.tdwg.voc.TaxonConcept;

/**
 * In response to http://129.67.24.160/bugzilla/show_bug.cgi?id=70
 * 'Null Pointer Exception in Geographical Region Converter'.
 *
 * @author ben
 *
 */
public class GeographicRegionConverterTest {

    /**
     *
     */
    private Mapper mapper = null;

    /**
     *
     * @throws Exception if there is a problem setting up the mapper
     */
    @Before
    public final void setUp() throws Exception {
        DozerBeanMapperFactoryBean mapperFactory
            = new DozerBeanMapperFactoryBean();
        mapperFactory.setMappingFiles(new Resource[] {
                new ClassPathResource(
                "/org/emonocot/checklist/view/assembler/mapping.xml") });
        mapperFactory.afterPropertiesSet();
        mapper = (Mapper) mapperFactory.getObject();
    }

    /**
     * @throws URISyntaxException if the uri is malformed
     */
    @Test
    public final void testHeaderMapping() throws URISyntaxException {
        Taxon taxon = new Taxon();
        taxon.setId(1);
        Distribution distribution = new Distribution();
        taxon.getDistribution().add(distribution);

        assertNull("Country should be null", distribution.getCountry());
        assertNull("Region should be null", distribution.getRegion());
        try {
            TaxonConcept taxonConcept = mapper.map(taxon, TaxonConcept.class);
        } catch (NullPointerException npe) {
            // Null pointer exception should not be thrown
            fail();
        }
    }

}
