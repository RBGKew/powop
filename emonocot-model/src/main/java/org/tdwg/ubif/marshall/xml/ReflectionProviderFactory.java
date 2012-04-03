package org.tdwg.ubif.marshall.xml;

import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.tdwg.ubif.TaxonName;

import com.thoughtworks.xstream.converters.reflection.FieldDictionary;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.converters.reflection.SortableFieldKeySorter;
import com.thoughtworks.xstream.converters.reflection.Sun14ReflectionProvider;

/**
 *
 * @author ben
 *
 */
public class ReflectionProviderFactory extends
        AbstractFactoryBean<ReflectionProvider> {
    /**
     *
     */
    private static final String[] TAXON_NAME_FIELDS = new String[] {
        };


    /**
     * Returns a ReflectionProvider that provides core reflection services.
     *
     * @return configured reflection provider.
     */
    @Override
    protected final ReflectionProvider createInstance() {
        SortableFieldKeySorter sorter = new SortableFieldKeySorter();

        sorter.registerFieldOrder(TaxonName.class,
                ReflectionProviderFactory.TAXON_NAME_FIELDS);

        FieldDictionary fieldDictionary = new FieldDictionary(sorter);

        ReflectionProvider reflectionProvider
            = new Sun14ReflectionProvider(fieldDictionary);
        return reflectionProvider;
    }

    @Override
    public final Class<?> getObjectType() {
        return ReflectionProvider.class;
    }

}
