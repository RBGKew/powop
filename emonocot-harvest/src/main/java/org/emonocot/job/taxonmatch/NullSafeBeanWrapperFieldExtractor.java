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
package org.emonocot.job.taxonmatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.Assert;

/**
 * @author jk00kg
 *
 */
public class NullSafeBeanWrapperFieldExtractor<T> extends
        BeanWrapperFieldExtractor<T> {

    private String[] names;

    /**
     * @param names field names to be extracted by the {@link #extract(Object)} method.
     */
    public void setNames(String[] names) {
            Assert.notNull(names, "Names must be non-null");
            this.names = Arrays.asList(names).toArray(new String[names.length]);
    }

    /**
     * @see org.springframework.batch.item.file.transform.FieldExtractor#extract(java.lang.Object)
     */
    public Object[] extract(T item) {
            List<Object> values = new ArrayList<Object>();

            BeanWrapper bw = new BeanWrapperImpl(item);
            for (String propertyName : this.names) {
                    try{
                        values.add(bw.getPropertyValue(propertyName));
                    } catch (NullPointerException npe) {
                        values.add(null);
                    }
            }
            return values.toArray();
    }
    
    
}
