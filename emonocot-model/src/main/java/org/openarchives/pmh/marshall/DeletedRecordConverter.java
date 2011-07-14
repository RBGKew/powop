package org.openarchives.pmh.marshall;

import org.openarchives.pmh.DeletedRecord;

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
public class DeletedRecordConverter implements Converter {

    public final boolean canConvert(final Class clazz) {
        if (clazz != null && clazz.equals(DeletedRecord.class)) {
            return true;
        }
        return false;
    }

    public final void marshal(final Object value,
            final HierarchicalStreamWriter writer,
            final MarshallingContext context) {
       writer.setValue(((DeletedRecord) value).value());
    }

    public final Object unmarshal(final HierarchicalStreamReader reader,
            final UnmarshallingContext context) {
        return DeletedRecord.fromValue(reader.getValue());
    }

}
