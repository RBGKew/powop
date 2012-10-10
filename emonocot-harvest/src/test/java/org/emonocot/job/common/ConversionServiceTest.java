package org.emonocot.job.common;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.emonocot.job.oaipmh.TaxonomicStatusConverter;
import org.gbif.ecat.voc.TaxonomicStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author ben
 *
 */
public class ConversionServiceTest {
    /**
     *
     */
    private ConversionService conversionService;

    /**
     *
     */
    @Before
    public final void setUp() {
        Set<Converter> converters = new HashSet<Converter>();
        converters.add(new TaxonomicStatusConverter());

        ConversionServiceFactoryBean factoryBean = new ConversionServiceFactoryBean();
        factoryBean.setConverters(converters);
        factoryBean.afterPropertiesSet();
        conversionService = factoryBean.getObject();
    }

    /**
     *
     */
    @Test
    public final void convertValidString() {
        assertEquals("Conversion Service should convert valid strings properly",
                conversionService.convert("http://e-monocot.org/TaxonomicStatus#synonym",
                        TaxonomicStatus.class), TaxonomicStatus.Synonym);
    }

}
