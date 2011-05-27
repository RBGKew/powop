package org.emonocot.model.hibernate;

import java.util.Map;

import org.hibernate.search.bridge.builtin.DateBridge;
import org.joda.time.DateTime;

/**
 *
 * @author ben
 *
 */
public class DateTimeBridge extends DateBridge {

    @Override
    public final DateTime stringToObject(final String stringValue) {
        return new DateTime(super.stringToObject(stringValue));
    }

    @Override
    public final String objectToString(final Object object) {
        return super.objectToString(((DateTime) object).toDate());
    }

    @Override
    public final void setParameterValues(final Map parameters) {
        super.setParameterValues(parameters);
    }

}
