package org.emonocot.job.common;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.emonocot.model.convert.TaxonomicStatusConverter;
import org.gbif.ecat.voc.TaxonomicStatus;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;

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
        ConversionServiceFactoryBean factoryBean = new ConversionServiceFactoryBean();
        converters.add(new TaxonomicStatusConverter());
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
                conversionService.convert("synonym",
                        TaxonomicStatus.class), TaxonomicStatus.Synonym);
    }

}
