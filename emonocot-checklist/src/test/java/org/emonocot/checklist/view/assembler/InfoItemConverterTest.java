package org.emonocot.checklist.view.assembler;

import static org.junit.Assert.assertEquals;

import java.net.URISyntaxException;

import org.dozer.Mapper;
import org.dozer.spring.DozerBeanMapperFactoryBean;
import org.emonocot.checklist.model.Climate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.emonocot.voc.Habitat;

/**
 *
 * @author ben
 *
 */
public class InfoItemConverterTest {

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
    public final void testClimateMapping() throws URISyntaxException {
        Climate climate = new Climate("abbreviation", "description");
		Habitat infoItem = mapper.map(climate, Habitat.class);
		assertEquals("Failed to map a Habitat InfoItem from Climate", climate.getDescription(), infoItem.getHasContent());
    }

}
