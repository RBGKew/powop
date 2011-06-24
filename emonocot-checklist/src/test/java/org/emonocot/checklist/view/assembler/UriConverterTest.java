package org.emonocot.checklist.view.assembler;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;

import org.dozer.Mapper;
import org.dozer.spring.DozerBeanMapperFactoryBean;
import org.emonocot.checklist.model.ChangeEvent;
import org.emonocot.checklist.model.ChangeEventImpl;
import org.emonocot.checklist.model.ChangeType;
import org.emonocot.checklist.model.Taxon;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.openarchives.pmh.Header;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 *
 * @author ben
 *
 */
public class UriConverterTest {

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
        taxon.setId(1L);
        ChangeEvent<Taxon> changeEvent = new ChangeEventImpl<Taxon>(taxon,
                ChangeType.CREATE, new DateTime());
        Header header = mapper.map(changeEvent, Header.class);

        assertEquals("URI should be as expected ",
                header.getIdentifier(), new URI(Taxon.IDENTIFIER_PREFIX + 1L));
    }

}
