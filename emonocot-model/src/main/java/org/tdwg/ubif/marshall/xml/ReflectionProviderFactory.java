/*
 * This is eMonocot, a global online biodiversity information resource.
 *
 * Copyright © 2011–2015 The Board of Trustees of the Royal Botanic Gardens, Kew and The University of Oxford
 *
 * eMonocot is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * eMonocot is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * The complete text of the GNU Affero General Public License is in the source repository as the file
 * ‘COPYING’.  It is also available from <http://www.gnu.org/licenses/>.
 */
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
