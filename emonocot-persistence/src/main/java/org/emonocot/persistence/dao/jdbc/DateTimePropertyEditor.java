/**
 * 
 */
package org.emonocot.persistence.dao.jdbc;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import java.util.Date;

import org.joda.time.DateTime;

/**
 * @author jk00kg
 *
 */
public class DateTimePropertyEditor extends PropertyEditorSupport {

    /* (non-Javadoc)
     * @see java.beans.PropertyEditor#setValue(java.lang.Object)
     */
    @Override
    public void setValue(final Object value) {
        if(value instanceof Date) {
            super.setValue(new DateTime(value));
        } else {
            super.setValue(value);
        }
    }

    /* (non-Javadoc)
     * @see java.beans.PropertyEditor#getValue()
     */
    @Override
    public Object getValue() {
        return super.getValue();
    }
}
