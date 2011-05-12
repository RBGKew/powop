package org.openarchives.pmh.marshall;

import org.openarchives.pmh.Granularity;

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
public class GranularityConverter implements Converter {

    @Override
    public final boolean canConvert(final Class clazz) {
        if (clazz != null && clazz.equals(Granularity.class)) {
            return true;
        }
        return false;
    }

    @Override
    public final void marshal(final Object value,
            final HierarchicalStreamWriter writer,
            final MarshallingContext context) {
       writer.setValue(((Granularity) value).value());
    }

    @Override
    public final Object unmarshal(final HierarchicalStreamReader reader,
            final UnmarshallingContext context) {
        return Granularity.fromValue(reader.getValue());
    }

}
