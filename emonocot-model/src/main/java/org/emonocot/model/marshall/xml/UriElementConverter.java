package org.emonocot.model.marshall.xml;

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
