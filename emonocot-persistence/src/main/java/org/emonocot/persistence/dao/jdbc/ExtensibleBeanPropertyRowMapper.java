/**
 * 
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
