package org.emonocot.model.marshall;

import com.thoughtworks.xstream.converters.SingleValueConverterWrapper;

/**
 *
 * @author ben
 *
 */
public class UriElementConverter extends SingleValueConverterWrapper {

    /**
     *
     */
    public UriElementConverter() {
        super(new UriConverter());
    }
}
