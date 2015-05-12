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
package org.emonocot.persistence.dao.jdbc;


import java.beans.PropertyEditor;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.beans.BeanWrapper;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

/**
 * @author jk00kg
 * @param <T> The Class of the bean to map to
 *
 */
public class ExtensibleBeanPropertyRowMapper<T> extends BeanPropertyRowMapper<T> {
    
    /**
     * custom editors to register for BeanWrappers
     */
    private Map<Class, PropertyEditor> editors;
    
    /**
     * @return the custom editors
     */
    public Map<Class, PropertyEditor> getEditors() {
        return editors;
    }

    /**
     * @param editors additional editors to set
     */
    public void setEditors(Map<Class, PropertyEditor> editors) {
        this.editors = editors;
    }

    /* (non-Javadoc)
     * @see org.springframework.jdbc.core.BeanPropertyRowMapper#initBeanWrapper(org.springframework.beans.BeanWrapper)
     */
    @Override
    protected void initBeanWrapper(BeanWrapper bw) {
        super.initBeanWrapper(bw);
        if(editors != null) {
            for (Class clazz : editors.keySet()) {
                bw.registerCustomEditor(clazz, editors.get(clazz));
            }
        }
    }
}
