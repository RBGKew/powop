package org.emonocot.model.marshall;

import java.net.URI;
import java.net.URISyntaxException;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 *
 * @author ben
 *
 */
public class UriConverter implements Converter {

    @Override
    public final boolean canConvert(final Class clazz) {
        if (clazz != null && clazz.equals(URI.class)) {
            return true;
        }
        return false;
    }

    @Override
    public final void marshal(final Object value,
            final HierarchicalStreamWriter writer,
            final MarshallingContext context) {
        URI uri = (URI) value;
        writer.setValue(uri.toString());
    }

    @Override
    public final Object unmarshal(final HierarchicalStreamReader reader,
            final UnmarshallingContext context) {
        String value = reader.getValue();
        try {
            return new URI(value);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(value
                    + " is does not follow the URI syntax", e);
        }
    }
}
